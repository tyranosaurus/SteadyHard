package com.tyranotyrano.steadyhard.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.ProfileManagerContract;
import com.tyranotyrano.steadyhard.model.ProfileManagerRepository;
import com.tyranotyrano.steadyhard.model.remote.ProfileManagerRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.ProfileManagerDataSource;
import com.tyranotyrano.steadyhard.presenter.ProfileManagerPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.tyranotyrano.steadyhard.view.MainActivity.user;

public class ProfileManagerActivity extends AppCompatActivity implements ProfileManagerContract.View {

    static final String TAG = "ProfileManagerActivity";
    // 리퀘스트 코드
    static final int REQUEST_CODE_GALLERY = 301;

    ProfileManagerContract.Presenter mPresenter = null;
    ProfileManagerDataSource mRepository = null;

    String newProfileImagePath = null;

    @BindView(R.id.circleImageViewProfileManagerProfileImage) CircleImageView circleImageViewProfileManagerProfileImage;
    @BindView(R.id.editTextProfileManagerNickname) EditText editTextProfileManagerNickname;
    @BindView(R.id.textViewProfileManagerDelete) TextView textViewProfileManagerDelete;
    @BindView(R.id.textViewProfileManagerEmail) TextView textViewProfileManagerEmail;
    @BindView(R.id.editTextProfileManagerPasswordOriginal) EditText editTextProfileManagerPasswordOriginal;
    @BindView(R.id.editTextProfileManagerNewPasswordFirst) EditText editTextProfileManagerNewPasswordFirst;
    @BindView(R.id.editTextProfileManagerNewPasswordSecond) EditText editTextProfileManagerNewPasswordSecond;
    @BindView(R.id.textViewProfilePrivateInfoPolicy) TextView textViewProfilePrivateInfoPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_manager);
        // ButterKnife 세팅
        ButterKnife.bind(this);
        // 초기화
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 프로필 변경 취소시 서버에 저장된 프로젝트 이미지 삭제
        if ( newProfileImagePath != null && newProfileImagePath.contains("_profileImage")) {
            String deleteFileName = user.getEmail() + "_profileImageTMP.png";
            mPresenter.deleteNewProfileImage(deleteFileName);
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
                    Glide.with(ProfileManagerActivity.this)
                            .load(data.getData())
                            .into(circleImageViewProfileManagerProfileImage);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

                        String newProfileImageName = user.getEmail() + "_profileImageTMP.png";
                        File file = new File(getCacheDir(), newProfileImageName );
                        file.createNewFile();
                        FileOutputStream out = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                        String projectImagePath = file.getAbsolutePath();

                        out.close();

                        // 프로필 사진 서버 전송
                        mPresenter.uploadNewProfileImage(projectImagePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    break;
            }
        }
    }

    @OnClick(R.id.imageViewProfileManagerBack)
    public void onProfileManagerBackClick() {
        // 프로필 변경 취소시 서버에 저장된 프로젝트 이미지 삭제
        if ( newProfileImagePath != null && newProfileImagePath.contains("_profileImage")) {
            String deleteFileName = user.getEmail() + "_profileImageTMP.png";
            mPresenter.deleteNewProfileImage(deleteFileName);
        }

        finish();
    }

    @OnClick(R.id.textViewProfileManagerComplete)
    public void onProfileManagerCompleteClick() {
        String newNickname = editTextProfileManagerNickname.getText().toString().trim();
        String originalPassword = editTextProfileManagerPasswordOriginal.getText().toString().trim();
        String newPasswordFirst = editTextProfileManagerNewPasswordFirst.getText().toString().trim();
        String newPasswordSecond = editTextProfileManagerNewPasswordSecond.getText().toString().trim();

        mPresenter.updateNewProfile(newProfileImagePath, newNickname, originalPassword, newPasswordFirst, newPasswordSecond);
    }

    @OnClick(R.id.circleImageViewProfileManagerProfileImage)
    public void onProfileImageModifyClick() {
        mPresenter.selectPictureByGallery();
    }

    @OnClick(R.id.textViewProfileManagerDelete)
    public void onProfileManagerDeleteClick() {
        final EditText edittextInputPassword = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder = mPresenter.buildAlertDialog(builder, edittextInputPassword);
        builder.show();
    }

    @OnClick(R.id.textViewProfilePrivateInfoPolicy)
    public void onPrivateInfoPolicyClick() {
        String privateInfoPolicyURL = "http://blog.naver.com/tyrano_1/221164727191";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(privateInfoPolicyURL));
        startActivity(intent);
    }

    public void init() {
        // Presenter에 View 할당
        mPresenter = new ProfileManagerPresenter();
        mPresenter.attachView(this);
        // Presenter에 Model 할당
        mRepository = new ProfileManagerRepository(new ProfileManagerRemoteDataSource());
        mPresenter.setProfileManagerRepository(mRepository);

        // 유저정보 세팅
        if ( user.getProfileImage() != null ) {
            Glide.with(ProfileManagerActivity.this)
                    .load(user.getProfileImage())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(circleImageViewProfileManagerProfileImage);
        } else {
            Glide.with(ProfileManagerActivity.this)
                    .load(R.drawable.icon_profile_default_black)
                    .into(circleImageViewProfileManagerProfileImage);
        }
        editTextProfileManagerNickname.setText(user.getNickname());
        textViewProfileManagerEmail.setText(user.getEmail());
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void clearSharedPreferencesData() {
        // 자동 로그인 삭제
        SharedPreferences autoLoginPreferences = getSharedPreferences("autoLogin", MODE_PRIVATE);
        SharedPreferences.Editor autoLoginEditor = autoLoginPreferences.edit();
        autoLoginEditor.clear();
        autoLoginEditor.commit();

        // 유저정보 삭제
        SharedPreferences userInfoPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor userInfoEditor = userInfoPreferences.edit();
        userInfoEditor.clear();
        userInfoEditor.commit();

        // SharedPreferences에 저장된 쿠키정보 삭제
        SharedPreferences cookiePreferences = getSharedPreferences("cookie", MODE_PRIVATE);
        SharedPreferences.Editor cookieInfoEditor = cookiePreferences.edit();
        cookieInfoEditor.clear();
        cookieInfoEditor.commit();
    }

    @Override
    public void callLoginActivity() {
        Intent intent = new Intent(ProfileManagerActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
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
    public void setOriginalProfileImage() {
        if ( user.getProfileImage() != null ) {
            Glide.with(ProfileManagerActivity.this)
                    .load(user.getProfileImage())
                    .into(circleImageViewProfileManagerProfileImage);
        } else {
            Glide.with(ProfileManagerActivity.this)
                    .load(R.drawable.icon_profile_default_black)
                    .into(circleImageViewProfileManagerProfileImage);
        }
    }

    @Override
    public void setNewProfileImagePath(String newProfileImagePath) {
        if ( newProfileImagePath != null ) {
            this.newProfileImagePath = newProfileImagePath;
        }
    }

    @Override
    public void updatedNewProfileInfo() {
        String newNickname = editTextProfileManagerNickname.getText().toString().trim();

        SharedPreferences userInfoPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor userInfoEditor = userInfoPreferences.edit();

        if ( newProfileImagePath != null ) {
            userInfoEditor.putString("profile_image", newProfileImagePath);
        }

        userInfoEditor.putString("nickname", newNickname);
        userInfoEditor.commit();

        MainActivity.user.setProfileImage(newProfileImagePath);
        MainActivity.user.setNickname(newNickname);

        setResult(RESULT_OK);
        finish();
    }

    @Override
    public Context getActivityContext() {
        return ProfileManagerActivity.this;
    }
}
