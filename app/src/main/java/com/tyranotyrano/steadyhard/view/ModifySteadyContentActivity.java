package com.tyranotyrano.steadyhard.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.ModifySteadyContentContract;
import com.tyranotyrano.steadyhard.model.ModifySteadyContentRepository;
import com.tyranotyrano.steadyhard.model.data.SteadyContent;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.ModifySteadyContentRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.ModifySteadyContentDataSource;
import com.tyranotyrano.steadyhard.presenter.ModifySteadyContentPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifySteadyContentActivity extends AppCompatActivity implements ModifySteadyContentContract.View {

    static final int REQUEST_CODE_GALLERY = 301;

    ModifySteadyContentContract.Presenter mPresenter = null;
    ModifySteadyContentDataSource mRepository = null;

    int modifyPosition = -1;
    String projectTitle = null;
    SteadyContent mModifyContent = null;
    SteadyProject steadyProject = null;
    String modifyContentImagePath = null;
    String modifiedSteadyContentImagePath = null;

    @BindView(R.id.imageViewModifyContentBack) ImageView imageViewModifyContentBack;
    @BindView(R.id.textViewModifyContentComplete) TextView textViewModifyContentComplete;
    @BindView(R.id.textViewContentProjectTitle) TextView textViewContentProjectTitle;
    @BindView(R.id.textViewContentAccomplishDate) TextView textViewContentAccomplishDate;
    @BindView(R.id.textViewOpenBracket) TextView textViewOpenBracket;
    @BindView(R.id.textViewCurrentDays) TextView textViewCurrentDays;
    @BindView(R.id.textViewPer) TextView textViewPer;
    @BindView(R.id.textViewCompleteDays) TextView textViewCompleteDays;
    @BindView(R.id.textViewCloseBracket) TextView textViewCloseBracket;
    @BindView(R.id.imageViewNewContentImage) ImageView imageViewNewContentImage;
    @BindView(R.id.editTextNewContentText) EditText editTextNewContentText;

    @BindViews({ R.id.textViewOpenBracket, R.id.textViewCurrentDays, R.id.textViewPer, R.id.textViewCompleteDays, R.id.textViewCloseBracket })
    List<TextView> textViewDateStatusList;

    final ButterKnife.Setter<TextView, Integer> setDateStatusColor = new ButterKnife.Setter<TextView, Integer>() {
        @Override public void set(TextView textView, Integer colorValue, int index) {
            textView.setTextColor(colorValue);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_steady_content);
        // ButterKnife 세팅
        ButterKnife.bind(this);
        // 초기화
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Repository 해제
        mRepository = null;
        // Presenter 해제
        mPresenter.detachView();
    }

    @Override
    public void finish() {
        // 콘텐츠 수정 취소시 서버에 저장된 콘텐츠 수정 이미지 삭제
        if ( modifiedSteadyContentImagePath != null && modifiedSteadyContentImagePath.contains("_ModifyContentImage")) {
            String deleteFileName = MainActivity.user.getEmail() + "_ModifyContentImage.png";
            mPresenter.deleteModifyContentImage(deleteFileName, steadyProject);
        }

        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_GALLERY:

                    // 가져온 이미지 보여주기
                    Glide.with(ModifySteadyContentActivity.this)
                            .load(data.getData())
                            .into(imageViewNewContentImage);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

                        String modifyContentImageName = MainActivity.user.getEmail() + "_ModifyContentImage.png";
                        File file = new File(getCacheDir(), modifyContentImageName );
                        file.createNewFile();
                        FileOutputStream out = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                        modifyContentImagePath = file.getAbsolutePath();

                        out.close();

                        // 프로젝트 사진 서버 전송 후 저장된 경로 받아오기
                        mPresenter.uploadModifyContentImage(modifyContentImagePath, steadyProject);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    break;
            }
        }
    }

    public void init() {
        // Presenter에 View 할당
        mPresenter = new ModifySteadyContentPresenter();
        mPresenter.attachView(this);
        // Presenter에 Model 할당
        mRepository = new ModifySteadyContentRepository(new ModifySteadyContentRemoteDataSource());
        mPresenter.setModifySteadyContentRepository(mRepository);

        // ContentByProjectActivity 에서 보낸 데이터 세팅
        Intent intent = getIntent();
        modifyPosition = intent.getIntExtra("modifyPosition", -1);
        projectTitle = intent.getStringExtra("projectTitle");
        mModifyContent = intent.getParcelableExtra("modifyContent");
        steadyProject = intent.getParcelableExtra("steadyProject");

        // 수정할 콘텐츠 정보 세팅
        textViewContentProjectTitle.setText(projectTitle);
        textViewContentAccomplishDate.setText(mModifyContent.getAccomplishDate());
        textViewCurrentDays.setText(String.valueOf(mModifyContent.getCurrentDays()));
        textViewCompleteDays.setText(String.valueOf(mModifyContent.getCompleteDays()));
        setDurationColor(mModifyContent.getCurrentDays(), mModifyContent.getCompleteDays());
        if ( mModifyContent.getContentImage() != null ) {
            Glide.with(ModifySteadyContentActivity.this)
                    .load(mModifyContent.getContentImage())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageViewNewContentImage);
        } else {
            Glide.with(ModifySteadyContentActivity.this)
                    .load(R.drawable.icon_project_image_default)
                    .into(imageViewNewContentImage);
        }
        editTextNewContentText.setText(mModifyContent.getContentText());
    }

    public void setDurationColor(int currentDays, int completeDays) {
        int percent = ( currentDays * 100 ) / completeDays;

        if ( currentDays == 0 ) {
            ButterKnife.apply(textViewDateStatusList, setDateStatusColor, getResources().getColor(R.color.colorBlack));
        } else if ( percent > 0 && percent <= 30 ) {
            ButterKnife.apply(textViewDateStatusList, setDateStatusColor, getResources().getColor(R.color.colorYellow));
        } else if ( percent >= 31 && percent <=70 ) {
            ButterKnife.apply(textViewDateStatusList, setDateStatusColor, getResources().getColor(R.color.colorBlueSky));
        } else if ( percent >= 71 && percent <= 100 ) {
            ButterKnife.apply(textViewDateStatusList, setDateStatusColor, getResources().getColor(R.color.colorGreen));
        }
    }

    @OnClick(R.id.imageViewModifyContentBack)
    public void onModifyContentBackClick() {
        finish();
    }

    @OnClick(R.id.textViewModifyContentComplete)
    public void onModifyContentCompleteClick() {
        // 수정할 데이터
        String contentText = editTextNewContentText.getText().toString().trim();

        mPresenter.modifySteadyContent(contentText, modifiedSteadyContentImagePath, mModifyContent, steadyProject);

        if ( modifiedSteadyContentImagePath != null ) {
            String parentProjectPath = steadyProject.getProjectTitle().replaceAll(" ", "_") + "_" + steadyProject.getNo();

            modifiedSteadyContentImagePath = modifiedSteadyContentImagePath.replace(MainActivity.user.getEmail() + "_ModifyContentImage",
                                                                                    parentProjectPath + "_content_" + mModifyContent.getCurrentDays())
                                                                           .replaceAll(" ", "_");
        }
    }

    @OnClick(R.id.imageViewNewContentImage)
    public void onModifyContentImageClick() {
        mPresenter.selectPictureByGallery();
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setKeyboardDown() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textViewModifyContentComplete.getWindowToken(), 0);
    }

    @Override
    public void selectPictureByGallery() {
        //사진가져오기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    @Override
    public void setModifiedContentImagePath(String modifyContentImageURLPath) {
        if ( modifyContentImageURLPath != null ) {
            this.modifiedSteadyContentImagePath = modifyContentImageURLPath;
        }
    }

    @Override
    public void completeModifySteadyContent(SteadyContent modifySteadyContent) {
        Intent intent = new Intent();
        intent.putExtra("modifyPosition", modifyPosition);
        // Parcelable 객체
        intent.putExtra("modifySteadyContent", modifySteadyContent);
        setResult(RESULT_OK, intent);

        finish();
    }
}
