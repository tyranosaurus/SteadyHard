package com.tyranotyrano.steadyhard.presenter;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.ProfileManagerContract;
import com.tyranotyrano.steadyhard.model.remote.datasource.ProfileManagerDataSource;

/**
 * Created by cyj on 2017-12-04.
 */

public class ProfileManagerPresenter implements ProfileManagerContract.Presenter {
    ProfileManagerContract.View mView = null;
    ProfileManagerDataSource mRepository = null;

    public void attachView(ProfileManagerContract.View view) {
        if ( view != null ) {
            this.mView = view;
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void setProfileManagerRepository(ProfileManagerDataSource profileManagerDataSource) {
        if ( profileManagerDataSource != null ) {
            this.mRepository = profileManagerDataSource;
        }
    }

    @Override
    public AlertDialog.Builder buildAlertDialog(AlertDialog.Builder builder, final EditText edittextInputPassword) {
        edittextInputPassword.setHint("비밀번호를 입력해주세요.");
        edittextInputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edittextInputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());


        builder.setIcon(R.drawable.logo_black_star);
        builder.setTitle("SteadyHard");
        builder.setMessage("\n그동안 이용해 주셔서 감사합니다.\n확인을 위해 비밀번호를 입력해주세요.");
        builder.setView(edittextInputPassword, 50, 0, 50, 0);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 회원탈퇴 처리==============================================================
                        // model 통신
                        String deletePassword = edittextInputPassword.getText().toString().trim();
                        new UserDeleteTask().execute(deletePassword);

                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder;
    }

    public class UserDeleteTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String deletePassword = params[0];
            boolean deleteResult = mRepository.deleteUser(deletePassword);

            return deleteResult;
        }

        @Override
        protected void onPostExecute(Boolean deleteResult) {
            super.onPostExecute(deleteResult);

            if ( deleteResult ) {
                // 회원탈퇴 성공
                mView.clearSharedPreferencesData();
                mView.callLoginActivity();
            } else {
                // 회원탈퇴 실패
                String message = "회원탈퇴에 실패하였습니다. 비밀번호를 확인해 주세요.";
                mView.showSnackBar(message);
            }
        }
    }
}
