package com.tyranotyrano.steadyhard.model.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.tyranotyrano.steadyhard.model.data.SteadyContent;
import com.tyranotyrano.steadyhard.model.remote.datasource.ContentDataSource;
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

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by cyj on 2017-12-12.
 */

public class ContentRemoteDataSource implements ContentDataSource {
    static final String TAG = "ContentRemoteDataSource";

    @Override
    public Map<String, Object> getSteadyContentsByUserNo(int userNo) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        Gson gson = new Gson();

        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        List<SteadyContent> steadyContentList = new ArrayList<>();
        String message = null;

        try {
            response = OkHttpAPICall.GET(client, NetworkDefineConstant.SERVER_URL_GET_STEADY_CONTENTS + userNo);

            if ( response == null ) {
                Log.e(TAG, "Response of getSteadyContentsByUserNo() is null.");

                return null;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());
                // 결과
                result = jsonFromServer.getBoolean("result");
                map.put("result", result);

                // Steady Project List 저장
                if ( jsonFromServer.has("steadyContents") ) {
                    JSONArray steadyContentArray = jsonFromServer.getJSONArray("steadyContents");

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
}
