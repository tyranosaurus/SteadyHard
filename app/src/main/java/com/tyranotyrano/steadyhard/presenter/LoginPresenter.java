package com.tyranotyrano.steadyhard.presenter;

import android.os.AsyncTask;

import com.tyranotyrano.steadyhard.contract.LoginContract;
import com.tyranotyrano.steadyhard.model.remote.datasource.LoginDataSource;

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
        // 이메일, 패스워드를 AsyncTask로 보내서 DB와 체크하여 결과 값을 boolean으로 받는다.
        // true : View의 callActivity 호출. - callCode 사용
        // false : "이메일 및 패스워드가 정확한지 확인해주세요" 스낵바 띄우고 입력창 초기화

        // doInBackground() : mRepository.checkLogin(email, password);
        // onPostExecute() : 위의 true/false 에 따른 결과 처리

        /** 중요 1
         * AsyncTask 처리할 때 이메일, 비번, callCode 이렇게 3개 보내야 하는데
         * 타입이 String 과 Integer로 서로 타입이 다른다.
         * 이때, AsyncTask의 매개변수의 타입을 Object로 해서 같이 넘겨준다.
         *
         * 위 방법이 좋은지는 모르겠지만 일단 이렇게 처리하자.
         * 처리할때 instanceof로 각 타입 체크 해준다.
         * */

        /** 중요 2
         * check box 에 따른 상황 구분해서 구현 할 것!
         * */

        new LoginCheckTask().execute(email, password, callCode);
    }

    @Override
    public void callActivity(int callCode) {
        mView.callActivity(callCode);
    }

    // 로그인 체크하는 AsyncTask
    public class LoginCheckTask extends AsyncTask<Object, String, Boolean> {

        int callcode = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            String email = null;
            String password = null;
            boolean isLogin = false;

            if ( params[0] instanceof String ) {
                email = (String)params[0];
            }

            if ( params[1] instanceof String ) {
                password = (String)params[1];
            }

            if ( params[2] instanceof Integer ) {
                callcode = (int)params[2];
            }

            isLogin = mRepository.checkLogin(email, password);

            return isLogin;
        }

        @Override
        protected void onPostExecute(Boolean isLogin) {
            super.onPostExecute(isLogin);
            // 결과값을 isLogin 변수로 받음.
            // true : View의 callActivity 호출. - callCode 사용
            // false : "이메일 및 패스워드가 정확한지 확인해주세요" 스낵바 띄우고 입력창 초기화
            //mView.callActivity(callCode);

            if ( isLogin ) {
                mView.callActivity(callcode);
            } else {
                String message = "이메일 또는 패스워드가 올바르지 않습니다.";
                mView.showSnackBar(message);
            }
        }
    }
}
