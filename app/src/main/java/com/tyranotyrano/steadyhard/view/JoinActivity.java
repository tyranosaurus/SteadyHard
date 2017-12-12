package com.tyranotyrano.steadyhard.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.JoinContract;
import com.tyranotyrano.steadyhard.model.JoinRepository;
import com.tyranotyrano.steadyhard.model.remote.JoinRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.JoinDataSource;
import com.tyranotyrano.steadyhard.presenter.JoinPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class JoinActivity extends AppCompatActivity implements JoinContract.View {

    static final String TAG = "==========JoinActivity";
    // 리퀘스트 코드
    static final int REQUEST_CODE_GALLERY = 301;

    JoinContract.Presenter mPresenter = null;
    JoinDataSource mRepository = null;

    boolean checkEmailDuplication = false;
    boolean isJoin = false;
    String profileImagePath = null;
    String userProfileImagePath = null;

    @BindView(R.id.editTextInputEmail) EditText editTextInputEmail;
    @BindView(R.id.editTextInputPasswordFirst) EditText editTextInputPasswordFirst;
    @BindView(R.id.editTextInputPasswordSecond) EditText editTextInputPasswordSecond;
    @BindView(R.id.circleImageViewProfileImage) CircleImageView circleImageViewProfileImage;
    @BindView(R.id.editTextNickname) EditText editTextNickname;
    @BindView(R.id.linearLayoutEmailPassword) LinearLayout linearLayoutEmailPassword;
    @BindView(R.id.linearLayoutProfileInfo) LinearLayout linearLayoutProfileInfo;
    @BindView(R.id.textViewDoJoin) TextView textViewDoJoin;
    @BindView(R.id.textViewSaveProfile) TextView textViewSaveProfile;
    @BindView(R.id.textViewCheckEmailDuplication) TextView textViewCheckEmailDuplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK ) {
            switch ( requestCode ) {
                case REQUEST_CODE_GALLERY:
                    // 가져온 이미지 보여주기
                    Glide.with(JoinActivity.this)
                            .load(data.getData())
                            .into(circleImageViewProfileImage);

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

                        String profileImageName = editTextInputEmail.getText().toString().trim() + "_profileImage.png";
                        File file = new File(getCacheDir(), profileImageName );
                        file.createNewFile();
                        FileOutputStream out = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                        profileImagePath = file.getAbsolutePath();

                        out.close();

                        // 프로필사진 서버 전송 후 저장된 경로 받아오기
                        mPresenter.uploadProfileImage(profileImagePath);
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
        mPresenter = new JoinPresenter();
        mPresenter.attachView(this);
        // Presenter에 Model 할당
        mRepository = new JoinRepository(new JoinRemoteDataSource());
        mPresenter.setJoinRepository(mRepository);
    }

    @OnClick(R.id.circleImageViewProfileImage)
    public void onProfileImageClick() {
        // 갤러리에서 프로필사진 가져오기
        mPresenter.selectPictureByGallery();
    }

    @OnClick(R.id.textViewCheckEmailDuplication)
    public void onCheckEmailDuplication() {
        // 키보드 내리기
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textViewCheckEmailDuplication.getWindowToken(), 0);

        String email = editTextInputEmail.getText().toString().trim();
        // 이메일 중복 체크
        mPresenter.checkEmailDuplication(email);
    }

    @OnClick(R.id.textViewDoJoin)
    public void onDoJoinClick() {
        if ( !checkEmailDuplication ) {
            showSnackBar("이메일 중복확인을 먼저 해주세요.");

            return;
        }

        String email = editTextInputEmail.getText().toString().trim();
        String passwordFirst = editTextInputPasswordFirst.getText().toString().trim();
        String passwordSecond = editTextInputPasswordSecond.getText().toString().trim();
        // 회원가입
        mPresenter.createNewUser(email, passwordFirst, passwordSecond);
    }

    @OnClick(R.id.textViewSaveProfile)
    public void onSaveProfileInfo() {
        String nickname = editTextNickname.getText().toString().trim();
        String email = editTextInputEmail.getText().toString().trim();

        mPresenter.saveProfileInfo(userProfileImagePath , nickname, email);
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
    public void checkedEmailDuplication() {
        checkEmailDuplication = true;
    }

    @Override
    public void joinedNewUser() {
        isJoin = true;
    }

    @Override
    public void showProfileSettings() {
        // 회원가입 후 프로필 정보 입력
        if ( isJoin ) {
            linearLayoutEmailPassword.setVisibility(View.GONE);
            textViewDoJoin.setVisibility(View.GONE);

            linearLayoutProfileInfo.setVisibility(View.VISIBLE);
            textViewSaveProfile.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setDefaultProfileImage() {
        Glide.with(JoinActivity.this)
                .load(R.drawable.icon_profile_default)
                .into(circleImageViewProfileImage);
    }

    @Override
    public void setUserProfileImagePath(String userProfileImagePath) {
        if ( userProfileImagePath != null ) {
            this.userProfileImagePath = userProfileImagePath;
        }
    }

    @Override
    public void finishJoinActivity(String message) {
        Intent intent = new Intent();
        intent.putExtra("message", message);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    @Override
    public Context getActivityContext() {
        return JoinActivity.this;
    }
}
