package com.tyranotyrano.steadyhard.model.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.datasource.HomeDataSource;
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
 * Created by cyj on 2017-12-06.
 */

public class HomeRemoteDatasource implements HomeDataSource {
    static final String TAG = "HomeRemoteDatasource";

    @Override
    public Map<String, Object> getSteadyProjectsByUserNo(int userNo) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        Gson gson = new Gson();

        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        List<SteadyProject> steadyProjectList = new ArrayList<>();
        String message = null;

        try {
            response = OkHttpAPICall.GET(client, NetworkDefineConstant.SERVER_URL_GET_STEADY_PROJECTS + userNo);

            if ( response == null ) {
                Log.e(TAG, "Response of getSteadyProjectsByUserNo() is null.");

                return null;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());
                // 결과
                result = jsonFromServer.getBoolean("result");
                map.put("result", result);

                // Steady Project List 저장
                if ( jsonFromServer.has("steadyProjects") ) {
                    JSONArray steadyProjectArray = jsonFromServer.getJSONArray("steadyProjects");

                    SteadyProject steadyProject = null;
                    for ( int i = 0; i < steadyProjectArray.length(); i++ ) {
                        steadyProject = gson.fromJson(steadyProjectArray.getJSONObject(i).toString(), SteadyProject.class);
                        steadyProjectList.add(steadyProject);
                    }

                    map.put("steadyProjectList", steadyProjectList);
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
    public boolean deleteSteadyProject(int deleteProjectNo, String userEmail, String projectImageName) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String message = null;

        FormBody.Builder requestBuilder = new FormBody.Builder();

        if ( projectImageName != null ) {
            requestBuilder.add("projectImageName", projectImageName);
        }

        requestBuilder
                .add("deleteProjectNo", String.valueOf(deleteProjectNo))
                .add("userEmail", userEmail);

        RequestBody requestBody = requestBuilder.build();

        try {
            response = OkHttpAPICall.DELETE(client, NetworkDefineConstant.SERVER_URL_DELETE_STEADY_PROJECT, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of deleteSteadyProject() is null.");

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
