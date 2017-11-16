package com.tyranotyrano.steadyhard;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView.setText("버터나이프 테스트");
        Snackbar.make(getWindow().getDecorView().getRootView(), "스낵바 테스트", Snackbar.LENGTH_SHORT).show();
    }
}
