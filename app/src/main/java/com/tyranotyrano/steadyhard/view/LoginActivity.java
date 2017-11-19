package com.tyranotyrano.steadyhard.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tyranotyrano.steadyhard.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /**
         * 버터나이프 빼먹지 말것!
         * ButterKnife.bind(this);
         * */

        Button buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
