package com.tyranotyrano.steadyhard.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.ProfileManagerContract;
import com.tyranotyrano.steadyhard.model.ProfileManagerRepository;
import com.tyranotyrano.steadyhard.model.remote.ProfileManagerRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.ProfileManagerDataSource;
import com.tyranotyrano.steadyhard.presenter.ProfileManagerPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileManagerActivity extends AppCompatActivity implements ProfileManagerContract.View {

    static final String TAG = "ProfileManagerActivity";

    ProfileManagerContract.Presenter mPresenter = null;
    ProfileManagerDataSource mRepository = null;

    @BindView(R.id.circleImageViewProfileManagerProfileImage) CircleImageView circleImageViewProfileManagerProfileImage;
    @BindView(R.id.editTextProfileManagerNickname) EditText editTextProfileManagerNickname;
    @BindView(R.id.textViewProfileManagerDelete) TextView textViewProfileManagerDelete;
    @BindView(R.id.editTextProfileManagerPasswordOriginal) EditText editTextProfileManagerPasswordOriginal;
    @BindView(R.id.editTextProfileManagerNewPasswordFirst) EditText editTextProfileManagerNewPasswordFirst;
    @BindView(R.id.editTextProfileManagerNewPasswordSecond) EditText editTextProfileManagerNewPasswordSecond;

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
        // Repository 해제
        mRepository = null;
        // Presenter 해제
        mPresenter.detachView();
    }

    @OnClick(R.id.textViewProfileManagerDelete)
    public void onProfileManagerDeleteClick() {
        final EditText edittextInputPassword = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder = mPresenter.buildAlertDialog(builder, edittextInputPassword);
        builder.show();
    }

    public void init() {
        // Presenter에 View 할당
        mPresenter = new ProfileManagerPresenter();
        mPresenter.attachView(this);
        // Presenter에 Model 할당
        mRepository = new ProfileManagerRepository(new ProfileManagerRemoteDataSource());
        mPresenter.setProfileManagerRepository(mRepository);

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
}
