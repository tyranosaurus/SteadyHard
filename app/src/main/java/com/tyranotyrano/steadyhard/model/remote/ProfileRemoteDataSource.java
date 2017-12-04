package com.tyranotyrano.steadyhard.model.remote;

import android.util.Log;

import com.tyranotyrano.steadyhard.model.remote.datasource.ProfileDataSource;
import com.tyranotyrano.steadyhard.network.NetworkDefineConstant;
import com.tyranotyrano.steadyhard.network.OkHttpAPICall;
import com.tyranotyrano.steadyhard.network.OkHttpInitSingtonManager;
import com.tyranotyrano.steadyhard.view.MainActivity;

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
 * Created by cyj on 2017-11-29.
 */

public class ProfileRemoteDataSource implements ProfileDataSource {
    static final String TAG = "ProfileRemoteDataSource";

    @Override
    public boolean clearSessionToken(String token) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String message = null;

        // 로그인 정보를 담은 RequestBody 생성
        RequestBody requestBody = new FormBody.Builder()
                .add("token", token)
                .build();

        try {
            response = OkHttpAPICall.DELETE(client, NetworkDefineConstant.SERVER_URL_SESSION_LOGOUT, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of clearSessionToken() is null.");

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
    public Map<String, Object> getSteadyProjectStatusCount() {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        Map<String, Object> map = new HashMap<>();
        boolean result = false;
        List<Integer> statusCountList = new ArrayList<>();
        String message = null;

        try {
            response = OkHttpAPICall.GET(client, NetworkDefineConstant.SERVER_URL_GET_PROJECT_STATUS+ MainActivity.user.getNo());

            if ( response == null ) {
                Log.e(TAG, "Response of clearSessionToken() is null.");

                return null;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());
                // 결과
                result = jsonFromServer.getBoolean("result");

                // Steady Project Status 저장
                if ( jsonFromServer.has("projectStatusCount") ) {
                    JSONObject projectStatusCount = jsonFromServer.getJSONObject("projectStatusCount");

                    statusCountList.add(projectStatusCount.getInt("success"));
                    statusCountList.add(projectStatusCount.getInt("ongoing"));
                    statusCountList.add(projectStatusCount.getInt("fail"));

                    map.put("result", result);
                    map.put("projectStatusCount", statusCountList);
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
