package com.tyranotyrano.steadyhard.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.LoginContract;
import com.tyranotyrano.steadyhard.presenter.LoginPresenter;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    static final String TAG = "==========LoginActivity";
    static final int CALL_CODE_ACTIVITY_HOME = 101;
    static final int CALL_CODE_ACTIVITY_JOIN = 102;

    LoginContract.Presenter mPresenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ButterKnife 세팅
        ButterKnife.bind(this);

        // Presenter 할당
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Presenter 해제
        mPresenter.detachView();
    }

    @OnClick({ R.id.buttonLogin, R.id.textViewJoin })
    public void onClick(View view) {
        switch (view.getId()) {
            // 로그인 버튼 처리
            case R.id.buttonLogin:
                mPresenter.callActivity(CALL_CODE_ACTIVITY_HOME);
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

    @Override
    public void callActivity(int callCode) {
        Intent intent = null;

        switch (callCode) {
            case LoginActivity.CALL_CODE_ACTIVITY_HOME:
                intent = new Intent(LoginActivity.this, MainActivity.class);
                break;
            case LoginActivity.CALL_CODE_ACTIVITY_JOIN:
                intent = new Intent(LoginActivity.this, JoinActivity.class);
                break;
            default:
                Log.e(TAG, "callActivity() method has an error. callCode is wrong");
                break;
        }

        startActivity(intent);
    }
}
