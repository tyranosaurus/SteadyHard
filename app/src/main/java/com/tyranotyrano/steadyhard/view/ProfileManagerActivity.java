package com.tyranotyrano.steadyhard.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tyranotyrano.steadyhard.R;

import butterknife.ButterKnife;

public class ProfileManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_manager);
        // ButterKnife 세팅
        ButterKnife.bind(this);
    }
}
