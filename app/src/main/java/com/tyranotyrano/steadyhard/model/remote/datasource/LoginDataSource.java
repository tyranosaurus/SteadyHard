package com.tyranotyrano.steadyhard.model.remote.datasource;

/**
 * Created by cyj on 2017-11-26.
 */

public interface LoginDataSource {
    // 로그인 Email, Password 를 DB에 보내 체크하는 함수
    // 지금은 반환형이 Oject지만 나중에 Response로 바꿔야함!
    boolean checkLogin(String email, String password);
}
