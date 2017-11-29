package com.tyranotyrano.steadyhard.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.application.SteadyHardApplication;
import com.tyranotyrano.steadyhard.contract.LoginContract;
import com.tyranotyrano.steadyhard.model.LoginRepository;
import com.tyranotyrano.steadyhard.model.data.User;
import com.tyranotyrano.steadyhard.model.remote.LoginRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.LoginDataSource;
import com.tyranotyrano.steadyhard.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    static final int CALL_CODE_ACTIVITY_MAIN = 101;
    static final int CALL_CODE_ACTIVITY_JOIN = 103;
    final String TAG = "==========LoginActivity";

    LoginContract.Presenter mPresenter = null;
    LoginDataSource mRepository = null;

    @BindView(R.id.editTextEmail) EditText editTextEmail;
    @BindView(R.id.editTextPassword) EditText editTextPassword;
    @BindView(R.id.checkBoxAutoLogin) CheckBox checkBoxAutoLogin;
    @BindView(R.id.linearLayoutLoginActivity) LinearLayout linearLayoutLoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // ButterKnife 세팅
        ButterKnife.bind(this);
        // 스플래시 액티비티 종료
        SplashActivity.SPLASH_ACTIVITY.finish();

        // 자동로그인 체크박스 기본값 세팅
        setAutoLoginCheckBox();

        // Presenter에 View 할당
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
        // Presenter에 Model 할당
        mRepository = new LoginRepository(new LoginRemoteDataSource());
        mPresenter.setLoginRepository(mRepository);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Repository 해제
        mRepository = null;
        // Presenter 해제
        mPresenter.detachView();
    }

    @OnClick({ R.id.buttonLogin, R.id.textViewJoin })
    public void onClick(View view) {
        switch (view.getId()) {
            // 로그인 버튼 처리
            case R.id.buttonLogin:
                // 로그인 Email, Password 체크
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                mPresenter.checkLogin(email, password, CALL_CODE_ACTIVITY_MAIN);
                break;
            // 회원가입 버튼 처리
            case R.id.textViewJoin:
                mPresenter.callActivity(CALL_CODE_ACTIVITY_JOIN);
                break;
            default:
                Log.e(TAG, "onClick() method has an error. ID value of view is wrong.");
                break;
        }
    }

    public void setAutoLoginCheckBox() {
        SharedPreferences autoLoginPreferences = getSharedPreferences("autoLogin", MODE_PRIVATE);
        boolean isAutoLogin = autoLoginPreferences.getBoolean("isAutoLogin", false);

        if ( isAutoLogin ) {
            checkBoxAutoLogin.setChecked(true);
        } else {
            checkBoxAutoLogin.setChecked(false);
        }
    }

    @Override
    public void callActivity(int callCode) {
        Intent intent = null;

        switch (callCode) {
            case LoginActivity.CALL_CODE_ACTIVITY_MAIN:
                intent = new Intent(LoginActivity.this, MainActivity.class);
                finish();
                break;
            case LoginActivity.CALL_CODE_ACTIVITY_JOIN:
                intent = new Intent(LoginActivity.this, JoinActivity.class);
                break;
            default:
                Log.e(TAG, "callActivity() method has an error. callCode is wrong.");
                break;
        }

        startActivity(intent);
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(linearLayoutLoginActivity, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setLoginSharedpreferences(User user) {
        // 자동 로그인 설정값 저장
        SharedPreferences autoLoginPreferences = getSharedPreferences("autoLogin", MODE_PRIVATE);
        SharedPreferences.Editor autoLoginEditor = autoLoginPreferences.edit();

        if ( checkBoxAutoLogin.isChecked() ) {
            autoLoginEditor.putBoolean("isAutoLogin", true);
        } else {
            autoLoginEditor.putBoolean("isAutoLogin", false);
        }

        autoLoginEditor.commit();

        // 유저 정보 저장
        SharedPreferences userInfoPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor userInfoEditor = userInfoPreferences.edit();

        userInfoEditor.putInt("no", user.getNo());
        userInfoEditor.putString("email", user.getEmail());
        userInfoEditor.putString("token", user.getToken());
        userInfoEditor.putString("profile_image", user.getProfile_image());
        userInfoEditor.putString("nickname", user.getNickname());

        userInfoEditor.commit();

        // 쿠키 정보 저장
        SharedPreferences cookiePreferences = getSharedPreferences("cookie", MODE_PRIVATE);
        SharedPreferences.Editor cookieInfoEditor = cookiePreferences.edit();

        cookieInfoEditor.putString("cookie", user.getCookie());

        cookieInfoEditor.commit();

        // 어플리케이션 정보로 쿠키 저장
        SteadyHardApplication.setCookie(user.getCookie());
    }
}
