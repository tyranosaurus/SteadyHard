package com.tyranotyrano.steadyhard.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.application.SteadyHardApplication;
import com.tyranotyrano.steadyhard.contract.SplashContract;
import com.tyranotyrano.steadyhard.model.SplashRepository;
import com.tyranotyrano.steadyhard.model.remote.SplashRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.SplashDataSource;
import com.tyranotyrano.steadyhard.presenter.SplashPresenter;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {

    public static final int CALL_CODE_ACTIVITY_MAIN = 101;
    public static final int CALL_CODE_ACTIVITY_LOGIN = 102;
    final String TAG = "=========SplashActivity";

    public static SplashActivity SPLASH_ACTIVITY = null;

    SplashContract.Presenter mPresenter = null;
    SplashDataSource mRepository = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 다른 액티비티에서 스플래스 액티비티 종료
        SPLASH_ACTIVITY = SplashActivity.this;

        // Presenter에 View 할당
        mPresenter = new SplashPresenter();
        mPresenter.attachView(this);
        // Presenter에 Model 할당
        mRepository = new SplashRepository(new SplashRemoteDataSource());
        mPresenter.setSplashRepository(mRepository);

        // 스플래시 0.7초 정도 보여주기
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences autoLoginPreferences = getSharedPreferences("autoLogin", MODE_PRIVATE);
                boolean isAutoLogin = autoLoginPreferences.getBoolean("isAutoLogin", false);

                if ( isAutoLogin ) {
                    // 자동 로그인 인 경우
                    SharedPreferences userInfoPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                    String token = userInfoPreferences.getString("token", null);

                    mPresenter.startAutoLogin(token);
                } else {
                    // 자동 로그인이 아닌 경우 : 로그인 액티비티 호출
                    mPresenter.callActivity(CALL_CODE_ACTIVITY_LOGIN);
                }
            }
        }, 700);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRepository = null;
        mPresenter.detachView();
    }

    @Override
    public void callActivity(int callCode) {
        Intent intent = null;

        switch (callCode) {
            case SplashActivity.CALL_CODE_ACTIVITY_MAIN:
                intent = new Intent(SplashActivity.this, MainActivity.class);
                break;
            case SplashActivity.CALL_CODE_ACTIVITY_LOGIN:
                intent = new Intent(SplashActivity.this, LoginActivity.class);
                break;
            default:
                Log.e(TAG, "callActivity() method has an error. callCode is wrong.");
                break;
        }

        startActivity(intent);
    }

    @Override
    public void setCookieSharedpreferences(String cookie) {
        // 쿠키 정보 저장
        SharedPreferences cookiePreferences = getSharedPreferences("cookie", MODE_PRIVATE);
        SharedPreferences.Editor cookieInfoEditor = cookiePreferences.edit();

        cookieInfoEditor.putString("cookie", cookie);

        cookieInfoEditor.commit();

        // 어플리케이션 정보로 쿠키 저장
        SteadyHardApplication.setCookie(cookie);
    }
}
