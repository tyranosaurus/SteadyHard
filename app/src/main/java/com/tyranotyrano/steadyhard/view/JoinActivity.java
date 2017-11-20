package com.tyranotyrano.steadyhard.view;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.tyranotyrano.steadyhard.R;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        /** 가입하기 버튼 처리 */
        TextView textViewDoJoin = (TextView) findViewById(R.id.textViewDoJoin);
        textViewDoJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입이 성공하면 현재 액티비티를 종료하고
                // LoginActivity 에서 리퀘스트코드 or onActivityResult() 메소드에서 받아 스낵바 띄우도록 한다.
                Snackbar.make(v, "회원가입 성공!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
