package com.tyranotyrano.steadyhard.model.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.tyranotyrano.steadyhard.model.data.SteadyContent;
import com.tyranotyrano.steadyhard.model.remote.datasource.NewContentDataSource;
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
 * Created by cyj on 2017-12-10.
 */

public class NewContentRemoteDataSource implements NewContentDataSource {
    final String TAG = "=======NewContentRDS";

    @Override
    public String uploadNewContentImage(String imagePath, String parentProjectPath) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String newContentImagePath = null;
        String message = null;

        // 콘텐츠 사진을 담은 RequestBody 생성
        String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        File sourFile = new File(imagePath);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", MainActivity.user.getEmail())
                .addFormDataPart("parentProjectPath", parentProjectPath)
                .addFormDataPart("uploadNewContentImage", fileName, RequestBody.create(MEDIA_TYPE, sourFile))
                .build();

        try {
            response = OkHttpAPICall.POST(client, NetworkDefineConstant.SERVER_URL_UPLOAD_NEW_CONTENT_IMAGE, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of uploadNewContentImage() is null.");

                return null;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());

                result = jsonFromServer.getBoolean("result");

                if ( jsonFromServer.has("imagePath") ) {
                    newContentImagePath = jsonFromServer.getString("imagePath");
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

        return newContentImagePath;
    }

    @Override
    public boolean deletedNewContentImage(String deleteFileName, String parentProjectPath) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String message = null;

        RequestBody requestBody = new FormBody.Builder()
                .add("email", MainActivity.user.getEmail())
                .add("parentProjectPath", parentProjectPath)
                .add("deleteFileName", deleteFileName)
                .build();

        try {
            response = OkHttpAPICall.DELETE(client, NetworkDefineConstant.SERVER_URL_DELETE_NEW_CONTENT_IMAGE, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of deletedNewContentImage() is null.");

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
    public Map<String, Object> createNewContent(String newContentImageURLPath, String contentText, String contentImageName, int currentDays, int completeDays, int projectNo) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;
        // Gson 객체 생성
        Gson gson = new Gson();

        Map<String, Object> map = new HashMap<>();
        SteadyContent newSteadyContent = null;
        boolean result = false;
        String message = null;

        // 새 콘텐츠의 정보를 담은 RequestBody 생성
        FormBody.Builder requestBuilder = new FormBody.Builder();

        if ( newContentImageURLPath != null ) {
            requestBuilder.add("newContentImageURLPath", newContentImageURLPath);
        }

        requestBuilder
                .add("email", MainActivity.user.getEmail())
                .add("contentText", contentText)
                .add("contentImageName", contentImageName)
                .add("currentDays", String.valueOf(currentDays))
                .add("completeDays", String.valueOf(completeDays))
                .add("projectNo", String.valueOf(projectNo))
                .add("userNo", String.valueOf(MainActivity.user.getNo()));

        RequestBody requestBody = requestBuilder.build();

        try {
            response = OkHttpAPICall.POST(client, NetworkDefineConstant.SERVER_URL_CREATE_NEW_CONTENT, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of createNewContent() is null.");

                return null;
            } else {

                JSONObject jsonFromServer = new JSONObject(response.body().string());

                result = jsonFromServer.getBoolean("result");
                map.put("result", result);

                if ( jsonFromServer.has("newContent") ) {
                    // gson, map 에 넣기
                    newSteadyContent = gson.fromJson(jsonFromServer.getJSONObject("newContent").toString(), SteadyContent.class);
                    map.put("newSteadyContent", newSteadyContent);
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
