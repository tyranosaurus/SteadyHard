package com.tyranotyrano.steadyhard.network;

/**
 * Created by cyj on 2017-11-27.
 */

public class NetworkDefineConstant {
    // Host 주소
    public static final String HOST_URL = "";

    //요청 URL path
    // 로그인
    public static String SERVER_URL_CHECK_LOGIN;
    public static String SERVER_URL_AUTO_LOGIN;
    public static String SERVER_URL_SESSION_LOGOUT;
    // 회원가입
    public static String SERVER_URL_CHECK_EMAIL_DUPLICATION;
    public static String SERVER_URL_CREATE_NEW_USER;
    public static String SERVER_URL_UPLOAD_PROFILE_IMAGE;
    public static String SERVER_URL_UPLOAD_PROFILEINFO;

    static {
        // 로그인
        SERVER_URL_CHECK_LOGIN = HOST_URL + "/steadyhard/login/checkLogin.php";
        SERVER_URL_AUTO_LOGIN = HOST_URL + "/steadyhard/login/autoLogin.php";
        SERVER_URL_SESSION_LOGOUT = HOST_URL + "/steadyhard/login/sessionLogout.php";
        // 회원가입
        SERVER_URL_CHECK_EMAIL_DUPLICATION = HOST_URL + "/steadyhard/join/checkEmailDuplication.php";
        SERVER_URL_CREATE_NEW_USER = HOST_URL + "/steadyhard/join/createNewUser.php";
        SERVER_URL_UPLOAD_PROFILE_IMAGE = HOST_URL + "/steadyhard/join/uploadProfileImage.php";
        SERVER_URL_UPLOAD_PROFILEINFO = HOST_URL + "/steadyhard/join/uploadProfileInfo.php";

    }
}
