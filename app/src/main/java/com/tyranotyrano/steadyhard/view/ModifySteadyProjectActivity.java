package com.tyranotyrano.steadyhard.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.ModifySteadyProjectContract;
import com.tyranotyrano.steadyhard.model.ModifySteadyProjectRepository;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.ModifySteadyProjectRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.ModifySteadyProjectDataSource;
import com.tyranotyrano.steadyhard.presenter.ModifySteadyProjectPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifySteadyProjectActivity extends AppCompatActivity implements ModifySteadyProjectContract.View {

    private static final String TAG = "ModifyProjectActivity";
    static final int REQUEST_CODE_GALLERY = 301;

    ModifySteadyProjectContract.Presenter mPresenter = null;
    ModifySteadyProjectDataSource mRepository = null;

    int modifyPosition = -1;
    SteadyProject mModifyItem = null;
    String modifyProjectImagePath = null;
    String modifiedSteadyProjectImagePath = null;

    @BindView(R.id.toolbarNewProject) Toolbar toolbar;
    @BindView(R.id.imageViewModifyProjectBack) ImageView imageViewModifyProjectBack;
    @BindView(R.id.textViewModifyProjectComplete) TextView textViewModifyProjectComplete;
    @BindView(R.id.editTextProjectTitle) EditText editTextProjectTitle;
    @BindView(R.id.imageViewProjectImage) ImageView imageViewProjectImage;
    @BindView(R.id.editTextProjectDuration) EditText editTextProjectDuration;
    @BindView(R.id.editTextProjectDescription) EditText editTextProjectDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_steady_project);
        // ButterKnife 세팅
        ButterKnife.bind(this);
        // 초기화
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 프로젝트 생성 취소시 서버에 저장된 프로젝트 이미지 삭제
        if ( modifiedSteadyProjectImagePath != null && modifiedSteadyProjectImagePath.contains("_ModifyProjectImage")) {
            String deleteFileName = MainActivity.user.getEmail() + "_ModifyProjectImage.png";
            mPresenter.deleteModifyProjectImage(deleteFileName);
        }

        // Repository 해제
        mRepository = null;
        // Presenter 해제
        mPresenter.detachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_GALLERY:

                    // 가져온 이미지 보여주기
                    Glide.with(ModifySteadyProjectActivity.this)
                            .load(data.getData())
                            .into(imageViewProjectImage);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

                        String modifyProjectImageName = MainActivity.user.getEmail() + "_ModifyProjectImage.png";
                        File file = new File(getCacheDir(), modifyProjectImageName );
                        file.createNewFile();
                        FileOutputStream out = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                        modifyProjectImagePath = file.getAbsolutePath();

                        out.close();

                        // 프로젝트 사진 서버 전송 후 저장된 경로 받아오기
                        mPresenter.uploadModifyProjectImage(modifyProjectImagePath);
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
        mPresenter = new ModifySteadyProjectPresenter();
        mPresenter.attachView(this);
        // Presenter에 Model 할당
        mRepository = new ModifySteadyProjectRepository(new ModifySteadyProjectRemoteDataSource());
        mPresenter.setModifySteadyProjectRepository(mRepository);

        // HomeFragment에서 보낸 데이터 세팅
        Intent intent = getIntent();
        modifyPosition = intent.getIntExtra("modifyPosition", -1);
        mModifyItem = intent.getParcelableExtra("modifyItem");

        // 수정할 프로젝트 정보 세팅
        editTextProjectTitle.setText(mModifyItem.getProjectTitle());
        if ( mModifyItem.getProjectImage() != null ) {
            Glide.with(ModifySteadyProjectActivity.this)
                    .load(mModifyItem.getProjectImage())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageViewProjectImage);
        } else {
            Glide.with(ModifySteadyProjectActivity.this)
                    .load(R.drawable.icon_project_image_default)
                    .into(imageViewProjectImage);
        }
        editTextProjectDuration.setText(String.valueOf(mModifyItem.getCompleteDays()));
        editTextProjectDescription.setText(mModifyItem.getDescription());

    }

    @OnClick(R.id.imageViewModifyProjectBack)
    public void onModifyProjectBackClick() {
        // 프로젝트 수정 취소시 서버에 저장된 프로젝트 이미지 삭제
        if ( modifiedSteadyProjectImagePath != null && modifiedSteadyProjectImagePath.contains("_ModifyProjectImage")) {
            String deleteFileName = MainActivity.user.getEmail() + "_ModifyProjectImage.png";
            mPresenter.deleteModifyProjectImage(deleteFileName);
        }

        finish();
    }

    @OnClick(R.id.textViewModifyProjectComplete)
    public void onModifyProjectCompleteClick() {
        String originalProjectImageName = null;

        // 프로젝트 수정
        String projectTitle = editTextProjectTitle.getText().toString().trim();
        String description = editTextProjectDescription.getText().toString().trim();
        // 원본 이미지 이름
        if ( mModifyItem.getProjectImage() != null ) {
            originalProjectImageName = mModifyItem.getProjectImage().substring(mModifyItem.getProjectImage().lastIndexOf("/") + 1);
        }
        // 수정할 프로젝트의 NO
        int modifyProjectNo = mModifyItem.getNo();

        mPresenter.modifySteadyProject(projectTitle, modifiedSteadyProjectImagePath, description, originalProjectImageName, modifyProjectNo);

        if ( modifiedSteadyProjectImagePath != null ) {
            modifiedSteadyProjectImagePath = modifiedSteadyProjectImagePath.replace("ModifyProjectImage", projectTitle).replaceAll(" ", "_");
        }
    }

    @OnClick(R.id.imageViewProjectImage)
    public void onModifyProjectImageClick() {
        mPresenter.selectPictureByGallery();
    }

    @OnClick(R.id.editTextProjectDuration)
    public void onModifyProjectDurationClick() {
        String message = "프로젝트 기간은 수정할 수 없습니다.";
        showSnackBar(message);
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT).show();
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
    public void setModifiedProjectImagePath(String modifiedSteadyProjectImagePath) {
        if ( modifiedSteadyProjectImagePath != null ) {
            this.modifiedSteadyProjectImagePath = modifiedSteadyProjectImagePath;
        }
    }

    @Override
    public void setKeyboardDown() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textViewModifyProjectComplete.getWindowToken(), 0);
    }

    @Override
    public void completeModifySteadyProject(SteadyProject modifySteadyProject) {
        Intent intent = new Intent();
        intent.putExtra("modifyPosition", modifyPosition);
        // Parcelable 객체
        intent.putExtra("modifySteadyProject", modifySteadyProject);
        setResult(RESULT_OK, intent);

        finish();
    }
}
