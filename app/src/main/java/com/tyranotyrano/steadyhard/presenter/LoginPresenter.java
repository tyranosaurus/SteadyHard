package com.tyranotyrano.steadyhard.presenter;

import android.app.Dialog;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.LoginContract;
import com.tyranotyrano.steadyhard.model.data.User;
import com.tyranotyrano.steadyhard.model.remote.datasource.LoginDataSource;

import java.util.regex.Pattern;

/**
 * Created by cyj on 2017-11-24.
 */

public class LoginPresenter implements LoginContract.Presenter {
    LoginContract.View mView = null;
    LoginDataSource mRepository = null;

    @Override
    public void attachView(LoginContract.View view) {
        if ( view != null ) {
            this.mView = view;
        }
    }

    @Override
    public void detachView() {
        this.mRepository = null;
        this.mView = null;
    }

    @Override
    public void setLoginRepository(LoginDataSource loginDataSource) {
        if ( loginDataSource != null ) {
            this.mRepository = loginDataSource;
        }
    }

    @Override
    public void checkLogin(String email, String password, int callCode) {

        String regExp = "\\w+@\\w+\\.\\w+(\\.\\w+)?";
        boolean checkEmail = Pattern.matches(regExp,email);

        if ( email == null || email.length() < 1 ) {
            String message = "이메일을 입력해주세요.";
            mView.showSnackBar(message);

            return;
        }

        if ( !checkEmail ) {
            String message = "이메일 형식이 올바르지 않습니다.";
            mView.showSnackBar(message);

            return;
        }

        if ( password == null || password.length() < 1 ) {
            String message = "비밀번호를 입력해주세요.";
            mView.showSnackBar(message);

            return;
        }

        new LoginCheckTask().execute(email, password, callCode);
    }

    @Override
    public void callActivity(int callCode) {
        mView.callActivity(callCode);
    }

    // 로그인 체크하는 AsyncTask
    public class LoginCheckTask extends AsyncTask<Object, String, User> {
        Dialog progressDialog;

        int callcode = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
            progressDialog = new Dialog(mView.getActivityContext(), R.style.SemoDialog);
            progressDialog.setCancelable(true);

            ProgressBar progressbar = new ProgressBar(mView.getActivityContext());
            progressbar.setIndeterminateDrawable(mView.getActivityContext().getDrawable(R.drawable.progress_dialog));

            progressDialog.addContentView(progressbar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            progressDialog.show();
        }

        @Override
        protected User doInBackground(Object... params) {
            String email = null;
            String password = null;

            if ( params[0] instanceof String ) {
                email = (String)params[0];
            }

            if ( params[1] instanceof String ) {
                password = (String)params[1];
            }

            if ( params[2] instanceof Integer ) {
                callcode = (int)params[2];
            }

            User userInfo = mRepository.checkLogin(email, password);

            return userInfo;
        }

        @Override
        protected void onPostExecute(User userInfo) {
            super.onPostExecute(userInfo);
            progressDialog.dismiss();

            if ( userInfo != null ) {
                // 유저정보 Sharedpreferences에 저장
                mView.setLoginSharedpreferences(userInfo);
                // MainActivity 호출
                mView.callActivity(callcode);
            } else {
                String message = "이메일 또는 패스워드가 올바르지 않습니다.";
                mView.showSnackBar(message);
            }
        }
    }
}
