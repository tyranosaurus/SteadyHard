package com.tyranotyrano.steadyhard.network;

/**
 * Created by cyj on 2017-11-27.
 */

public class NetworkDefineConstant {
    // Host 주소
    public static final String HOST_URL = "http://192.168.123.108";

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
    // 새 프로젝트
    public static String SERVER_URL_UPLOAD_STEADY_PROJECT_IMAGE;
    public static String SERVER_URL_DELETE_NEW_PROJECT_IMAGE;
    public static String SERVER_URL_CREATE_NEW_STEADY_PROJECT;
    // 프로필
    public static String SERVER_URL_DELETE_USER;
    public static String SERVER_URL_GET_PROJECT_STATUS;
    public static String SERVER_URL_UPLOAD_NEW_PROFILE_IMAGE;
    public static String SERVER_URL_DELETE_NEW_PROFILE_IMAGE;
    public static String SERVER_URL_UPDATE_NEW_PROFILE_INFO;
    // 홈 프래그먼트
    public static String SERVER_URL_GET_STEADY_PROJECTS;
    public static String SERVER_URL_DELETE_STEADY_PROJECT;
    public static String SERVER_URL_UPLOAD_MODIFY_STEADY_PROJECT_IMAGE;
    public static String SERVER_URL_DELETE_MODIFY_PROJECT_IMAGE;
    public static String SERVER_URL_UPDATE_STEADY_PROJECT;
    public static String SERVER_URL_GET_CONTENT_BY_PROJECT;
    public static String SERVER_URL_UPLOAD_NEW_CONTENT_IMAGE;
    public static String SERVER_URL_DELETE_NEW_CONTENT_IMAGE;
    public static String SERVER_URL_CREATE_NEW_CONTENT;
    public static String SERVER_URL_UPLOAD_MODIFY_STEADY_CONTENT_IMAGE;
    public static String SERVER_URL_DELETE_MODIFY_STEADY_CONTENT_IMAGE;
    public static String SERVER_URL_UPDATE_STEADY_CONTENT;

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
        // 새 프로젝트
        SERVER_URL_UPLOAD_STEADY_PROJECT_IMAGE = HOST_URL + "/steadyhard/newproject/uploadSteadyPojectImage.php";
        SERVER_URL_DELETE_NEW_PROJECT_IMAGE = HOST_URL + "/steadyhard/newproject/deleteNewProjectImage.php";
        SERVER_URL_CREATE_NEW_STEADY_PROJECT = HOST_URL + "/steadyhard/newproject/createNewSteadyProject.php";
        // 프로필
        SERVER_URL_DELETE_USER = HOST_URL + "/steadyhard/profile/deleteUser.php";
        SERVER_URL_GET_PROJECT_STATUS = HOST_URL + "/steadyhard/profile/getProjectStatus.php?userNo=";
        SERVER_URL_UPLOAD_NEW_PROFILE_IMAGE = HOST_URL + "/steadyhard/profile/uploadNewProfileImage.php";
        SERVER_URL_DELETE_NEW_PROFILE_IMAGE = HOST_URL + "/steadyhard/profile/deleteNewProfileImage.php";
        SERVER_URL_UPDATE_NEW_PROFILE_INFO = HOST_URL + "/steadyhard/profile/updateNewProfileInfo.php";
        // 홈 프래그먼트
        SERVER_URL_GET_STEADY_PROJECTS = HOST_URL + "/steadyhard/home/getSteadyProjects.php?userNo=";
        SERVER_URL_DELETE_STEADY_PROJECT = HOST_URL + "/steadyhard/home/deleteSteadyProject.php";
        SERVER_URL_UPLOAD_MODIFY_STEADY_PROJECT_IMAGE = HOST_URL + "/steadyhard/home/uploadModifySteadyPojectImage.php";
        SERVER_URL_DELETE_MODIFY_PROJECT_IMAGE = HOST_URL + "/steadyhard/home/deleteModifyProjectImage.php";
        SERVER_URL_UPDATE_STEADY_PROJECT = HOST_URL + "/steadyhard/home/updateModifyProject.php";
        SERVER_URL_GET_CONTENT_BY_PROJECT = HOST_URL + "/steadyhard/home/getContentByProject.php?projectNo=";
        SERVER_URL_UPLOAD_NEW_CONTENT_IMAGE = HOST_URL + "/steadyhard/home/uploadNewContentImage.php";
        SERVER_URL_DELETE_NEW_CONTENT_IMAGE = HOST_URL + "/steadyhard/home/deleteNewContentImage.php";
        SERVER_URL_CREATE_NEW_CONTENT = HOST_URL + "/steadyhard/home/createNewContent.php";
        SERVER_URL_UPLOAD_MODIFY_STEADY_CONTENT_IMAGE = HOST_URL + "/steadyhard/home/uploadModifySteadyContentImage.php";
        SERVER_URL_DELETE_MODIFY_STEADY_CONTENT_IMAGE = HOST_URL + "/steadyhard/home/deleteModifySteadyContentImage.php";
        SERVER_URL_UPDATE_STEADY_CONTENT = HOST_URL + "/steadyhard/home/updateSteadyContent.php";
    }
}
