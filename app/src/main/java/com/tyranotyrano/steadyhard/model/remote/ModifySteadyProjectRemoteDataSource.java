package com.tyranotyrano.steadyhard.model.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.datasource.ModifySteadyProjectDataSource;
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
 * Created by cyj on 2017-12-07.
 */

public class ModifySteadyProjectRemoteDataSource implements ModifySteadyProjectDataSource {
    final String TAG = "ModifySteadyProjectRDS";

    @Override
    public String uploadModifyProjectImage(String modifyProjectImagePath) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String modifiedProjectImagePath = null;
        String message = null;

        // 수정된 프로젝트 사진을 담은 RequestBody 생성
        String fileName = modifyProjectImagePath.substring(modifyProjectImagePath.lastIndexOf("/") + 1);
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        File sourFile = new File(modifyProjectImagePath);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", MainActivity.user.getEmail())
                .addFormDataPart("uploadModifySteadyProjectImage", fileName, RequestBody.create(MEDIA_TYPE, sourFile))
                .build();

        try {
            response = OkHttpAPICall.POST(client, NetworkDefineConstant.SERVER_URL_UPLOAD_MODIFY_STEADY_PROJECT_IMAGE, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of uploadModifyProjectImage() is null.");

                return null;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());

                result = jsonFromServer.getBoolean("result");

                if ( jsonFromServer.has("imagePath") ) {
                    modifiedProjectImagePath = jsonFromServer.getString("imagePath");
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

        return modifiedProjectImagePath;
    }

    @Override
    public boolean deletedModifyProjectImage(String deleteFileName) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String message = null;

        RequestBody requestBody = new FormBody.Builder()
                .add("email", MainActivity.user.getEmail())
                .add("deleteFileName", deleteFileName)
                .build();

        try {
            response = OkHttpAPICall.DELETE(client, NetworkDefineConstant.SERVER_URL_DELETE_MODIFY_PROJECT_IMAGE, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of deletedModifyProjectImage() is null.");

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
    public Map<String, Object> modifySteadyProject(String projectTitle, String modifiedSteadyProjectImagePath, String description,
                                       String modifyProjectImageName, String originalProjectImageName, int modifyProjectNo) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;
        // Gson 객체 생성
        Gson gson = new Gson();

        Map<String, Object> map = new HashMap<>();
        SteadyProject modifySteadyProject = null;
        boolean result = false;
        String message = null;

        // 수정된 프로젝트의 정보를 담은 RequestBody 생성
        FormBody.Builder requestBuilder = new FormBody.Builder();

        if ( originalProjectImageName != null ) {
            requestBuilder.add("originalProjectImageName", originalProjectImageName);
        }

        if ( modifiedSteadyProjectImagePath != null ) {
            requestBuilder.add("modifiedSteadyProjectImagePath", modifiedSteadyProjectImagePath);
        }

        requestBuilder
                .add("userNo", String.valueOf(MainActivity.user.getNo()))
                .add("userEmail", MainActivity.user.getEmail())
                .add("modifyProjectNo", String.valueOf(modifyProjectNo))
                .add("projectTitle", projectTitle)
                .add("description", description)
                .add("modifyProjectImageName", modifyProjectImageName);

        RequestBody requestBody = requestBuilder.build();

        try {
            response = OkHttpAPICall.PUT(client, NetworkDefineConstant.SERVER_URL_UPDATE_STEADY_PROJECT, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of modifySteadyProject() is null.");

                return null;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());

                result = jsonFromServer.getBoolean("result");
                map.put("result", result);

                if ( jsonFromServer.has("modifyProject") ) {
                    // gson, map 에 넣기
                    modifySteadyProject = gson.fromJson(jsonFromServer.getJSONObject("modifyProject").toString(), SteadyProject.class);
                    map.put("modifySteadyProject", modifySteadyProject);
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
