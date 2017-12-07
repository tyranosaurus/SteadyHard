package com.tyranotyrano.steadyhard.model.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.datasource.NewSteadyProjectDataSource;
import com.tyranotyrano.steadyhard.network.NetworkDefineConstant;
import com.tyranotyrano.steadyhard.network.OkHttpAPICall;
import com.tyranotyrano.steadyhard.network.OkHttpInitSingtonManager;
import com.tyranotyrano.steadyhard.view.MainActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by cyj on 2017-12-03.
 */

public class NewSteadyProjectRemoteDataSource implements NewSteadyProjectDataSource {
    final String TAG = "==NewSteadyProjectRDS";

    @Override
    public String uploadSteadyProjectImage(String imagePath) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String steadyProjectImagePath = null;
        String message = null;

        // 프로젝트 사진을 담은 RequestBody 생성
        String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        File sourFile = new File(imagePath);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", MainActivity.user.getEmail())
                .addFormDataPart("uploadSteadyProjectImage", fileName, RequestBody.create(MEDIA_TYPE, sourFile))
                .build();

        try {
            response = OkHttpAPICall.POST(client, NetworkDefineConstant.SERVER_URL_UPLOAD_STEADY_PROJECT_IMAGE, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of uploadSteadyProjectImage() is null.");

                return null;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());

                result = jsonFromServer.getBoolean("result");

                if ( jsonFromServer.has("imagePath") ) {
                    steadyProjectImagePath = jsonFromServer.getString("imagePath");
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

        return steadyProjectImagePath;
    }

    @Override
    public boolean deletedNewProjectImage(String deleteFileName) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String message = null;

        RequestBody requestBody = new FormBody.Builder()
                .add("email", MainActivity.user.getEmail())
                .add("deleteFileName", deleteFileName)
                .build();
        try {
            response = OkHttpAPICall.DELETE(client, NetworkDefineConstant.SERVER_URL_DELETE_NEW_PROJECT_IMAGE, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of deletedNewProjectImage() is null.");

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
    public Map<String, Object> createNewSteadyProject(String projectTitle, String steadyProjectImagePath, int completeDate, String description, String projectImageName) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;
        // Gson 객체 생성
        Gson gson = new Gson();

        Map<String, Object> map = new HashMap<>();
        SteadyProject newSteadyProject = null;
        boolean result = false;
        String message = null;

        // 새 프로젝트의 정보를 담은 RequestBody 생성
        FormBody.Builder requestBuilder = new FormBody.Builder();

        if ( steadyProjectImagePath != null ) {
            requestBuilder.add("steadyProjectImagePath", steadyProjectImagePath);
        }

        requestBuilder
                .add("userNo", String.valueOf(MainActivity.user.getNo()))
                .add("email", MainActivity.user.getEmail())
                .add("projectTitle", projectTitle)
                .add("completeDate", String.valueOf(completeDate))
                .add("description", description)
                .add("projectImageName", projectImageName);

        RequestBody requestBody = requestBuilder.build();

        try {
            response = OkHttpAPICall.POST(client, NetworkDefineConstant.SERVER_URL_CREATE_NEW_STEADY_PROJECT, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of uploadProfileImage() is null.");

                return null;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());

                result = jsonFromServer.getBoolean("result");
                map.put("result", result);

                if ( jsonFromServer.has("newProject") ) {
                    // gson, map 에 넣기
                    newSteadyProject = gson.fromJson(jsonFromServer.getJSONObject("newProject").toString(), SteadyProject.class);
                    map.put("newSteadyProject", newSteadyProject);
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

        return map;
    }
}
