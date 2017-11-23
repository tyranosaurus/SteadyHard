package com.tyranotyrano.steadyhard.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.LoginContract;
import com.tyranotyrano.steadyhard.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    @BindView(R.id.buttonLogin) Button buttonLogin;
    @BindView(R.id.textViewJoin) TextView textViewJoin;

    LoginContract.Presenter mPresenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ButterKnife 세팅
        ButterKnife.bind(this);

        // Presenter 세팅
        mPresenter = new LoginPresenter();
        mPresenter.setView(this);

        // 로그인 버튼 처리
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                mPresenter.callActivity(intent);
            }
        });

        // 회원가입 버튼 처리
        textViewJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                mPresenter.callActivity(intent);
            }
        });
    }

    @Override
    public void callActivity(Intent intent) {
        startActivity(intent);
    }
}
