package com.tyranotyrano.steadyhard.model.remote;

import android.util.Log;

import com.tyranotyrano.steadyhard.model.remote.datasource.ProfileDataSource;
import com.tyranotyrano.steadyhard.network.NetworkDefineConstant;
import com.tyranotyrano.steadyhard.network.OkHttpAPICall;
import com.tyranotyrano.steadyhard.network.OkHttpInitSingtonManager;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

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
}
