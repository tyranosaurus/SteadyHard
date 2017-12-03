package com.tyranotyrano.steadyhard.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tyranotyrano.steadyhard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileManagerActivity extends AppCompatActivity {

    @BindView(R.id.textViewProfileManagerDelete) TextView textViewProfileManagerDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_manager);
        // ButterKnife 세팅
        ButterKnife.bind(this);

        textViewProfileManagerDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    public void showAlertDialog()
    {
        final EditText edittextInputPassword = new EditText(this);
        edittextInputPassword.setHint("비밀번호를 입력해주세요.");
        edittextInputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edittextInputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.logo_black_star);
        builder.setTitle("SteadyHard");
        builder.setMessage("\n그동안 이용해 주셔서 감사합니다.\n확인을 위해 비밀번호를 입력해주세요.");
        builder.setView(edittextInputPassword);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(getWindow().getDecorView().getRootView(), edittextInputPassword.getText().toString().trim(), Snackbar.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

}
