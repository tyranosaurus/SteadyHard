package com.tyranotyrano.steadyhard.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 스플래시 액티비티 0.5초 동안 유지
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 메인 액티비티 호출
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        // 스플래시 액티비티 종료
        finish();
    }
}
