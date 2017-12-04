package com.tyranotyrano.steadyhard.model.remote;

import android.util.Log;

import com.tyranotyrano.steadyhard.model.remote.datasource.ProfileManagerDataSource;
import com.tyranotyrano.steadyhard.network.NetworkDefineConstant;
import com.tyranotyrano.steadyhard.network.OkHttpAPICall;
import com.tyranotyrano.steadyhard.network.OkHttpInitSingtonManager;
import com.tyranotyrano.steadyhard.view.MainActivity;

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
 * Created by cyj on 2017-12-04.
 */

public class ProfileManagerRemoteDataSource implements ProfileManagerDataSource {
    final String TAG = "===ProfileManagerRDS";

    @Override
    public boolean deleteUser(String deletePassword) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String message = null;

        // 리퀘스트바디
        RequestBody requestBody = new FormBody.Builder()
                .add("email", MainActivity.user.getEmail())
                .add("deletePassword", deletePassword)
                .build();

        try {
            response = OkHttpAPICall.DELETE(client, NetworkDefineConstant.SERVER_URL_DELETE_USER, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of deleteUser() is null.");

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
    public String uploadNewProfileImage(String imagePath) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String newProfileImagePath = null;
        String message = null;

        // 새 프로필 사진을 담은 RequestBody 생성
        String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        File sourFile = new File(imagePath);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", MainActivity.user.getEmail())
                .addFormDataPart("newProfileImage", fileName, RequestBody.create(MEDIA_TYPE, sourFile))
                .build();

        try {
            response = OkHttpAPICall.POST(client, NetworkDefineConstant.SERVER_URL_UPLOAD_NEW_PROFILE_IMAGE, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of uploadNewProfileImage() is null.");

                return null;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());

                result = jsonFromServer.getBoolean("result");

                if ( jsonFromServer.has("profileImagePath") ) {
                    newProfileImagePath = jsonFromServer.getString("profileImagePath");
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

        return newProfileImagePath;
    }

    @Override
    public boolean deletedNewProfileImage(String deleteFileName) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String message = null;

        RequestBody requestBody = new FormBody.Builder()
                .add("email", MainActivity.user.getEmail())
                .add("deleteProfileImage", deleteFileName)
                .build();

        try {
            response = OkHttpAPICall.DELETE(client, NetworkDefineConstant.SERVER_URL_DELETE_NEW_PROFILE_IMAGE, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of deletedNewProfileImage() is null.");

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
    public boolean updateNewProfile(String newProfileImagePath, String newNickname, String originalPassword, String newPassword) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String message = null;

        // 새 프로필 정보를 담은 RequestBody 생성
        FormBody.Builder requestBuilder = new FormBody.Builder();

        requestBuilder
                .add("userNo", String.valueOf(MainActivity.user.getNo()))
                .add("userEmail", MainActivity.user.getEmail())
                .add("newNickname", newNickname);

        if ( newProfileImagePath != null ) {
            requestBuilder.add("newProfileImagePath", newProfileImagePath);
        }

        if ( originalPassword != null && originalPassword.length() >= 8) {
            requestBuilder
                    .add("originalPassword", originalPassword)
                    .add("newPassword", newPassword);
        }

        RequestBody requestBody = requestBuilder.build();

        try {
            response = OkHttpAPICall.PUT(client, NetworkDefineConstant.SERVER_URL_UPDATE_NEW_PROFILE_INFO, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of deletedNewProfileImage() is null.");

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
}
