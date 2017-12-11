package com.tyranotyrano.steadyhard.model.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.tyranotyrano.steadyhard.model.data.SteadyContent;
import com.tyranotyrano.steadyhard.model.remote.datasource.ModifySteadyContentDataSource;
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
 * Created by cyj on 2017-12-11.
 */

public class ModifySteadyContentRemoteDataSource implements ModifySteadyContentDataSource {
    final String TAG = "ModifySteadyContentRDS";

    @Override
    public String uploadModifyContentImage(String modifyContentImagePath, String parentProjectPath) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String modifiedContentImageURLPath = null;
        String message = null;

        // 수정된 콘텐츠 사진을 담은 RequestBody 생성
        String fileName = modifyContentImagePath.substring(modifyContentImagePath.lastIndexOf("/") + 1);
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        File sourFile = new File(modifyContentImagePath);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", MainActivity.user.getEmail())
                .addFormDataPart("parentProjectPath", parentProjectPath)
                .addFormDataPart("uploadModifySteadyContentImage", fileName, RequestBody.create(MEDIA_TYPE, sourFile))
                .build();

        try {
            response = OkHttpAPICall.POST(client, NetworkDefineConstant.SERVER_URL_UPLOAD_MODIFY_STEADY_CONTENT_IMAGE, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of uploadModifyContentImage() is null.");

                return null;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());

                result = jsonFromServer.getBoolean("result");

                if ( jsonFromServer.has("imagePath") ) {
                    modifiedContentImageURLPath = jsonFromServer.getString("imagePath");
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

        return modifiedContentImageURLPath;
    }

    @Override
    public boolean deletedModifyContentImage(String deleteFileName, String parentProjectPath) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String message = null;

        RequestBody requestBody = new FormBody.Builder()
                .add("email", MainActivity.user.getEmail())
                .add("deleteFileName", deleteFileName)
                .add("parentProjectPath", parentProjectPath)
                .build();

        try {
            response = OkHttpAPICall.DELETE(client, NetworkDefineConstant.SERVER_URL_DELETE_MODIFY_STEADY_CONTENT_IMAGE, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of deletedModifyContentImage() is null.");

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
    public Map<String, Object> modifySteadyContent(String contentText, String modifiedSteadyContentImagePath, String parentProjectPath, int currentDays, int contentNo) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;
        // Gson 객체 생성
        Gson gson = new Gson();

        Map<String, Object> map = new HashMap<>();
        SteadyContent modifySteadyContent = null;
        boolean result = false;
        String message = null;

        // 수정된 콘텐츠의 정보를 담은 RequestBody 생성
        FormBody.Builder requestBuilder = new FormBody.Builder();

        if ( modifiedSteadyContentImagePath != null ) {
            requestBuilder.add("modifiedSteadyContentImagePath", modifiedSteadyContentImagePath);
        }

        requestBuilder
                .add("userNo", String.valueOf(MainActivity.user.getNo()))
                .add("userEmail", MainActivity.user.getEmail())
                .add("contentText", contentText)
                .add("parentProjectPath", parentProjectPath)
                .add("currentDays", String.valueOf(currentDays))
                .add("contentNo", String.valueOf(contentNo));

        RequestBody requestBody = requestBuilder.build();

        try {
            response = OkHttpAPICall.PUT(client, NetworkDefineConstant.SERVER_URL_UPDATE_STEADY_CONTENT, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of modifySteadyContent() is null.");

                return null;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());

                result = jsonFromServer.getBoolean("result");
                map.put("result", result);

                if ( jsonFromServer.has("modifyContent") ) {
                    // gson, map 에 넣기
                    modifySteadyContent = gson.fromJson(jsonFromServer.getJSONObject("modifyContent").toString(), SteadyContent.class);
                    map.put("modifySteadyContent", modifySteadyContent);
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
