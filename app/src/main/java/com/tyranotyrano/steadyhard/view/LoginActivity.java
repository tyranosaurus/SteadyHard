package com.tyranotyrano.steadyhard.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.LoginContract;
import com.tyranotyrano.steadyhard.model.LoginRepository;
import com.tyranotyrano.steadyhard.model.remote.LoginRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.LoginDataSource;
import com.tyranotyrano.steadyhard.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    static final int CALL_CODE_ACTIVITY_HOME = 101;
    static final int CALL_CODE_ACTIVITY_JOIN = 102;
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

                mPresenter.checkLogin(email, password, CALL_CODE_ACTIVITY_HOME);
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
                finish();
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

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(linearLayoutLoginActivity, message, Snackbar.LENGTH_SHORT).show();
    }
}
