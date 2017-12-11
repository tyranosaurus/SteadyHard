package com.tyranotyrano.steadyhard.model.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.tyranotyrano.steadyhard.model.data.SteadyContent;
import com.tyranotyrano.steadyhard.model.remote.datasource.ContentByProjectDataSource;
import com.tyranotyrano.steadyhard.network.NetworkDefineConstant;
import com.tyranotyrano.steadyhard.network.OkHttpAPICall;
import com.tyranotyrano.steadyhard.network.OkHttpInitSingtonManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by cyj on 2017-12-09.
 */

public class ContentByProjectRemoteDataSource implements ContentByProjectDataSource {
    static final String TAG = "==ContentByProjectRDS";

    @Override
    public Map<String, Object> getContentsByProjectNo(int projectNo) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        Gson gson = new Gson();

        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        List<SteadyContent> steadyContentList = new ArrayList<>();
        String message = null;

        try {
            response = OkHttpAPICall.GET(client, NetworkDefineConstant.SERVER_URL_GET_CONTENT_BY_PROJECT + projectNo);

            if ( response == null ) {
                Log.e(TAG, "Response of getContentsByProjectNo() is null.");

                return null;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());
                // 결과
                result = jsonFromServer.getBoolean("result");
                map.put("result", result);

                // Steady Content List 저장
                if ( jsonFromServer.has("contentByProject") ) {
                    JSONArray steadyContentArray = jsonFromServer.getJSONArray("contentByProject");

                    SteadyContent steadyContent = null;
                    for ( int i = 0; i < steadyContentArray.length(); i++ ) {
                        steadyContent = gson.fromJson(steadyContentArray.getJSONObject(i).toString(), SteadyContent.class);
                        steadyContentList.add(steadyContent);
                    }

                    map.put("steadyContentList", steadyContentList);
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

    @Override
    public boolean deleteSteadyContent(int deleteContentNo, String userEmail, String deleteContentImageName, String parentProjectPath, int currentDays, int projectNo) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String message = null;

        FormBody.Builder requestBuilder = new FormBody.Builder();

        if ( deleteContentImageName != null ) {
            requestBuilder.add("deleteContentImageName", deleteContentImageName);
        }

        requestBuilder
                .add("userEmail", userEmail)
                .add("deleteContentNo", String.valueOf(deleteContentNo))
                .add("currentDays", String.valueOf(currentDays))
                .add("projectNo", String.valueOf(projectNo))
                .add("parentProjectPath", parentProjectPath);

        RequestBody requestBody = requestBuilder.build();

        try {
            response = OkHttpAPICall.DELETE(client, NetworkDefineConstant.SERVER_URL_DELETE_STEADY_CONTENT, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of deleteSteadyContent() is null.");

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
