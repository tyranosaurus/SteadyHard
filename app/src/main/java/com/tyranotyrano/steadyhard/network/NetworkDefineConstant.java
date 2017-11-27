package com.tyranotyrano.steadyhard.network;

/**
 * Created by cyj on 2017-11-27.
 */

public class NetworkDefineConstant {
    // Host 주소
    public static final String HOST_URL = "";

    //요청 URL path
    // 로그인 체크
    public static String SERVER_URL_CHECK_LOGIN;

    static {
        SERVER_URL_CHECK_LOGIN = HOST_URL + "/steadyhard/login/checkLogin.php";
    }
}
