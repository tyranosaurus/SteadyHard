package com.tyranotyrano.steadyhard.model.remote;

import android.util.Log;

import com.tyranotyrano.steadyhard.model.remote.datasource.JoinDataSource;
import com.tyranotyrano.steadyhard.network.NetworkDefineConstant;
import com.tyranotyrano.steadyhard.network.OkHttpAPICall;
import com.tyranotyrano.steadyhard.network.OkHttpInitSingtonManager;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by cyj on 2017-11-30.
 */

public class JoinRemoteDataSource implements JoinDataSource {

    final String TAG = "==JoinRemoteDataSource";

    @Override
    public boolean isEmailDuplication(String email) {

        // 이메일 중복체크를 위한 네트워크 처리
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String message = null;

        // 이메일 정보를 담은 RequestBody 생성
        RequestBody requestBody = new FormBody.Builder()
                .add("email", email)
                .build();

        try {
            response = OkHttpAPICall.POST(client, NetworkDefineConstant.SERVER_URL_CHECK_EMAIL_DUPLICATION, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of isEmailDuplication() is null.");

                return false;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());

                result = jsonFromServer.getBoolean("result");

                if ( jsonFromServer.has("message") ) {
                    message = jsonFromServer.getString("message");
                    Log.e(TAG, message);
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ( response != null ) {
                response.close();
            }
        }

        return result;
    }

    @Override
    public boolean createNewUser(String email, String password) {

        // 회원가입을 위한 네트워크 처리
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String message = null;

        // 회원가입 정보를 담은 RequestBody 생성
        String nickname = email.substring(0, email.indexOf("@"));

        RequestBody requestBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .add("nickname", nickname)
                .build();

        try {
            response = OkHttpAPICall.POST(client, NetworkDefineConstant.SERVER_URL_CREATE_NEW_USER, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of createNewUser() is null.");

                return false;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());

                result = jsonFromServer.getBoolean("result");

                if ( jsonFromServer.has("message") ) {
                    message = jsonFromServer.getString("message");
                    Log.e(TAG, message);
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ( response != null ) {
                response.close();
            }
        }

        return result;
    }

    @Override
    public String uploadProfileImage(String imagePath) {
        // 프로필사진 전송을 위한 네트워크 처리
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String userProfileImagePath = null;
        String message = null;

        // 프로필사진을 담은 RequestBody 생성
        String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        File sourFile = new File(imagePath);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("uploadProfileImage", fileName, RequestBody.create(MEDIA_TYPE, sourFile))
                .build();

        try {
            response = OkHttpAPICall.POST(client, NetworkDefineConstant.SERVER_URL_UPLOAD_PROFILE_IMAGE, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of uploadProfileImage() is null.");

                return null;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());

                result = jsonFromServer.getBoolean("result");

                if ( jsonFromServer.has("imagePath") ) {
                    userProfileImagePath = jsonFromServer.getString("imagePath");
                }

                if ( jsonFromServer.has("message") ) {
                    message = jsonFromServer.getString("message");
                    Log.e(TAG, message);
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ( response != null ) {
                response.close();
            }
        }

        return userProfileImagePath;
    }

    @Override
    public boolean saveProfileInfo(String userProfileImagePath, String nickname, String email) {
        // 프로필 정보들 전송을 위한 네트워크 처리
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean saveResult = false;
        String message = null;

        // 프로필사진경로, 닉네임 담은 RequestBody 생성
        FormBody.Builder requestBuilder = new FormBody.Builder();

        if ( userProfileImagePath != null ) {
            requestBuilder.add("userProfileImagePath", userProfileImagePath);
        }

        requestBuilder
                .add("nickname", nickname)
                .add("email", email);

        RequestBody requestBody = requestBuilder.build();

        try {
            response = OkHttpAPICall.POST(client, NetworkDefineConstant.SERVER_URL_UPLOAD_PROFILEINFO, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of uploadProfileImage() is null.");

                return false;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());

                saveResult = jsonFromServer.getBoolean("result");

                if ( jsonFromServer.has("message") ) {
                    message = jsonFromServer.getString("message");
                    Log.e(TAG, message);
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ( response != null ) {
                response.close();
            }
        }

        return saveResult;
    }
}
