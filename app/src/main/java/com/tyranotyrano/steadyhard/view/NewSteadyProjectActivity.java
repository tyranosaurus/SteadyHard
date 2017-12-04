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
import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.NewSteadyProjectContract;
import com.tyranotyrano.steadyhard.model.NewSteadyProjectRepository;
import com.tyranotyrano.steadyhard.model.remote.NewSteadyProjectRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.NewSteadyProjectDataSource;
import com.tyranotyrano.steadyhard.presenter.NewSteadyProjectPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewSteadyProjectActivity extends AppCompatActivity implements NewSteadyProjectContract.View {

    // 리퀘스트 코드
    static final int REQUEST_CODE_GALLERY = 301;

    NewSteadyProjectContract.Presenter mPresenter = null;
    NewSteadyProjectDataSource mRepository = null;

    String projectImagePath = null;
    String steadyProjectImagePath = null;

    @BindView(R.id.toolbarNewProject) Toolbar toolbar;
    @BindView(R.id.imageViewNewProjectBack) ImageView imageViewNewProjectBack;
    @BindView(R.id.textViewNewProjectCreate) TextView textViewNewProjectCreate;
    @BindView(R.id.editTextProjectTitle) EditText editTextProjectTitle;
    @BindView(R.id.imageViewProjectImage) ImageView imageViewProjectImage;
    @BindView(R.id.editTextProjectDuration) EditText editTextProjectDuration;
    @BindView(R.id.editTextProjectDescription) EditText editTextProjectDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_steady_project);
        // ButterKnife 세팅
        ButterKnife.bind(this);
        // 초기화
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 프로젝트 생성 취소시 서버에 저장된 프로젝트 이미지 삭제
        if ( steadyProjectImagePath != null && steadyProjectImagePath.contains("_NewProjectImage")) {
            String deleteFileName = MainActivity.user.getEmail() + "_NewProjectImage.png";
            mPresenter.deleteNewProjectImage(deleteFileName);
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
                    Glide.with(NewSteadyProjectActivity.this)
                            .load(data.getData())
                            .into(imageViewProjectImage);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

                        String profileImageName = MainActivity.user.getEmail() + "_NewProjectImage.png";
                        File file = new File(getCacheDir(), profileImageName );
                        file.createNewFile();
                        FileOutputStream out = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                        projectImagePath = file.getAbsolutePath();

                        out.close();

                        // 프로젝트 사진 서버 전송 후 저장된 경로 받아오기
                        mPresenter.uploadSteadyProjectImage(projectImagePath);
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
        mPresenter = new NewSteadyProjectPresenter();
        mPresenter.attachView(this);
        // Presenter에 Model 할당
        mRepository = new NewSteadyProjectRepository(new NewSteadyProjectRemoteDataSource());
        mPresenter.setNewSteadyProjectRepository(mRepository);

        // 툴바 세팅
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @OnClick(R.id.imageViewNewProjectBack)
    public void onNewProjectBackClick() {
        // 프로젝트 생성 취소시 서버에 저장된 프로젝트 이미지 삭제
        if ( steadyProjectImagePath != null ) {
            String deleteFileName = MainActivity.user.getEmail() + "_NewProjectImage.png";
            mPresenter.deleteNewProjectImage(deleteFileName);
        }

        finish();
    }

    @OnClick(R.id.textViewNewProjectCreate)
    public void onNewProjectCreateClick() {
        // 새 프로젝트 생성
        String projectTitle = editTextProjectTitle.getText().toString().trim();
        String completeDays = editTextProjectDuration.getText().toString().trim();
        String description = editTextProjectDescription.getText().toString().trim();

        mPresenter.createNewProject(projectTitle, steadyProjectImagePath, completeDays, description);

        steadyProjectImagePath = steadyProjectImagePath.replace("NewProjectImage", projectTitle);
    }

    @OnClick(R.id.imageViewProjectImage)
    public void onProjectImageClick() {
        mPresenter.selectPictureByGallery();
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
    public void showSnackBar(String message) {
        Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setSteadyProjectImagePath(String steadyProjectImagePath) {
        if ( steadyProjectImagePath != null ) {
            this.steadyProjectImagePath = steadyProjectImagePath;
        }
    }

    @Override
    public void setDefaultSteadyProjectImage() {
        Glide.with(NewSteadyProjectActivity.this)
                .load(R.drawable.icon_project_image_default)
                .into(imageViewProjectImage);
    }

    @Override
    public void setKeyboardDown() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textViewNewProjectCreate.getWindowToken(), 0);
    }

    @Override
    public void completeNewSteadyProject() {
        finish();
    }
}
