package com.tyranotyrano.steadyhard.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.application.SteadyHardApplication;
import com.tyranotyrano.steadyhard.contract.LoginContract;
import com.tyranotyrano.steadyhard.model.LoginRepository;
import com.tyranotyrano.steadyhard.model.data.User;
import com.tyranotyrano.steadyhard.model.remote.LoginRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.LoginDataSource;
import com.tyranotyrano.steadyhard.presenter.LoginPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    static final int MULTIPLE_PERMISSIONS = 100;
    static final int REQUEST_CODE_ACTIVITY_MAIN = 101;
    static final int REQUEST_CODE_ACTIVITY_JOIN = 103;
    static final String TAG = "==========LoginActivity";

    LoginContract.Presenter mPresenter = null;
    LoginDataSource mRepository = null;

    boolean getAllPermissions = false;
    String[] permissions = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @BindView(R.id.editTextEmail) EditText editTextEmail;
    @BindView(R.id.editTextPassword) EditText editTextPassword;
    @BindView(R.id.checkBoxAutoLogin) CheckBox checkBoxAutoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        if ( resultCode == RESULT_OK ) {
            switch ( requestCode ) {
                case REQUEST_CODE_ACTIVITY_JOIN:
                    String message = data.getStringExtra("message");
                    showSnackBar(message);
                    break;
                default:
                    break;
            }
        }
    }

    public void init() {
        // 스플래시 액티비티 종료
        SplashActivity.SPLASH_ACTIVITY.finish();
        // 앱 사용에 필요한 권한 요청
        requestPermissions();
        // 자동로그인 체크박스 기본값 세팅
        setAutoLoginCheckBox();

        // Presenter에 View 할당
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
        // Presenter에 Model 할당
        mRepository = new LoginRepository(new LoginRemoteDataSource());
        mPresenter.setLoginRepository(mRepository);
    }

    @OnClick({ R.id.buttonLogin, R.id.textViewJoin })
    public void onClick(View view) {

        if ( !getAllPermissions) {
            showSnackBar("권한 요청에 동의하신 후 사용할 수 있습니다.");

            return;
        }

        switch (view.getId()) {
            // 로그인 버튼 처리
            case R.id.buttonLogin:
                // 로그인 Email, Password 체크
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                mPresenter.checkLogin(email, password, REQUEST_CODE_ACTIVITY_MAIN);
                break;
            // 회원가입 버튼 처리
            case R.id.textViewJoin:
                mPresenter.callActivity(REQUEST_CODE_ACTIVITY_JOIN);
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
            case LoginActivity.REQUEST_CODE_ACTIVITY_MAIN:
                intent = new Intent(LoginActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
                break;
            case LoginActivity.REQUEST_CODE_ACTIVITY_JOIN:
                intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ACTIVITY_JOIN);
                break;
            default:
                Log.e(TAG, "callActivity() method has an error. callCode is wrong.");
                break;
        }
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT).show();
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

    // 앱 사용에 필요한 퍼미션 체크 및 요청
    public void requestPermissions() {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            checkPermissions();
        }
    }

    public void checkPermissions() {
        List<String> permissionList = new ArrayList<String>();

        for ( String permission : permissions ) {
            if ( ContextCompat.checkSelfPermission(LoginActivity.this, permission) != PackageManager.PERMISSION_GRANTED ) {
                // 획득하지 못한 퍼미션을 리스트에 추가
                permissionList.add(permission);
            }
        }

        if ( !permissionList.isEmpty() ) {
            ActivityCompat.requestPermissions(LoginActivity.this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return;
        }

        getAllPermissions = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 권한 요청 중 하나라도 얻지 못하면 getAllPermissions = false 로 바꾼다.
        getAllPermissions = true;

        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                for ( int i = 0; i < grantResults.length; i++ ) {
                    if ( grantResults[i] != PackageManager.PERMISSION_GRANTED ) {
                        String message = "권한 요청에 동의해주셔야 이용 가능합니다.\n앱 설정에서 권한 요청에 동의해주세요.";
                        showSnackBar(message);

                        getAllPermissions = false;
                    }
                }
                break;
            }
        }
    }
}
