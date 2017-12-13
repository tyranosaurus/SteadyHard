package com.tyranotyrano.steadyhard.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.NewContentContract;
import com.tyranotyrano.steadyhard.model.NewContentRepository;
import com.tyranotyrano.steadyhard.model.data.SteadyContent;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.NewContentRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.NewContentDataSource;
import com.tyranotyrano.steadyhard.presenter.NewContentPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewContentActivity extends AppCompatActivity implements NewContentContract.View {
    static final int REQUEST_CODE_GALLERY = 301;

    NewContentContract.Presenter mPresenter = null;
    NewContentDataSource mRepository = null;

    SteadyProject steadyProject = null;
    String contentImagePath = null;
    String newContentImagePath = null;

    @BindView(R.id.toolbarNewContent) Toolbar toolbar;
    @BindView(R.id.imageViewNewContentBack) ImageView imageViewNewContentBack;
    @BindView(R.id.textViewNewContentCreate) TextView textViewNewContentCreate;
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
        setContentView(R.layout.activity_new_content);
        // ButterKnife 세팅
        ButterKnife.bind(this);
        // 초기화
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 콘텐츠 생성 취소시 서버에 저장된 콘텐츠 이미지 삭제
        if ( newContentImagePath != null && newContentImagePath.contains("_NewContentImage") ) {
            String deleteFileName = MainActivity.user.getEmail() + "_NewContentImage.png";
            mPresenter.deleteNewContentImage(deleteFileName, steadyProject);
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
                    Glide.with(NewContentActivity.this)
                            .load(data.getData())
                            .into(imageViewNewContentImage);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

                        String contentImageName = MainActivity.user.getEmail() + "_NewContentImage.png";
                        File file = new File(getCacheDir(), contentImageName );
                        file.createNewFile();
                        FileOutputStream out = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                        contentImagePath = file.getAbsolutePath();

                        out.close();

                        // 프로젝트 사진 서버 전송 후 저장된 경로 받아오기
                        mPresenter.uploadNewContentImage(contentImagePath, steadyProject);
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
        mPresenter = new NewContentPresenter();
        mPresenter.attachView(this);
        // Presenter에 Model 할당
        mRepository = new NewContentRepository(new NewContentRemoteDataSource());
        mPresenter.setNewContentRepository(mRepository);

        // 인텐트 데이터 할당
        Intent intent = getIntent();
        steadyProject = intent.getParcelableExtra("steadyProject");

        // 툴바 세팅
        setSupportActionBar(toolbar);

        // 프로젝트 정보 세팅
        textViewContentProjectTitle.setText(steadyProject.getProjectTitle());
        textViewContentAccomplishDate.setText(getTodayDate());
        textViewCurrentDays.setText(String.valueOf(steadyProject.getCurrentDays() + 1));
        textViewCompleteDays.setText(String.valueOf(steadyProject.getCompleteDays()));
        setDurationColor(steadyProject.getCurrentDays(), steadyProject.getCompleteDays());

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

    public String getTodayDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String todayDateString = dateFormat.format(new Date());

        return todayDateString;
    }

    @OnClick(R.id.imageViewNewContentBack)
    public void onNewContentBackClick() {
        // 콘텐츠 생성 취소시 서버에 저장된 콘텐츠 이미지 삭제
        if ( newContentImagePath != null && newContentImagePath.contains("_NewContentImage") ) {
            String deleteFileName = MainActivity.user.getEmail() + "_NewContentImage.png";
            mPresenter.deleteNewContentImage(deleteFileName, steadyProject);
        }

        finish();
    }

    public boolean isTodayContentAccomplishable() {
        Date lastDate = null;
        Date todayDate = new Date();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            lastDate = dateFormat.parse(steadyProject.getLast_date());

            calendar.setTime(todayDate);
            calendar.add(Calendar.DATE, -1);

            todayDate = dateFormat.parse(dateFormat.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("========", lastDate+"");
        Log.e("========", todayDate+"");
        if ( lastDate.compareTo(todayDate) == 0 ) {
            return true;
        }

        return false;
    }

    @OnClick(R.id.textViewNewContentCreate)
    public void onNewContentCreateClick() {
        // 새 콘텐츠(오늘의 꾸준함)이 오늘안에 등록하는지 체크
        if ( !isTodayContentAccomplishable() ) {

            String message = "오늘의 꾸준함이 이어지지 못했습니다. 프로젝트를 확인해주세요.";
            showSnackBar(message);

            return;
        }

        // 새 콘텐츠(오늘의 꾸준함) 생성
        String contentImageName = steadyProject.getProjectTitle() + "_" + steadyProject.getNo() + "_content_" + (steadyProject.getCurrentDays() + 1);
        contentImageName = contentImageName.replaceAll(" ", "_");

        String contextText = editTextNewContentText.getText().toString().trim();

        mPresenter.createNewContent(newContentImagePath, contextText, steadyProject);

        if ( newContentImagePath != null ) {
            newContentImagePath = newContentImagePath.replace(MainActivity.user.getEmail() + "_NewContentImage", contentImageName);
        }
    }

    @OnClick(R.id.imageViewNewContentImage)
    public void onNewContentImageClick() {
        // 콘텐츠 이미지 처리
        mPresenter.selectPictureByGallery();
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
    public void setDefaultSteadyContentImage() {
        Glide.with(NewContentActivity.this)
                .load(R.drawable.icon_project_image_default)
                .into(imageViewNewContentImage);
    }

    @Override
    public void setNewSteadyContentImagePath(String newContentImagePath) {
        if ( newContentImagePath != null ) {
            this.newContentImagePath = newContentImagePath;
        }
    }

    @Override
    public void setKeyboardDown() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textViewNewContentCreate.getWindowToken(), 0);
    }

    @Override
    public void completeNewSteadyContent(SteadyContent newSteadyContent) {
        Intent intent = new Intent();
        // Parcelable 객체
        intent.putExtra("newSteadyContent", newSteadyContent);
        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public Context getActivityContext() {
        return NewContentActivity.this;
    }
}
