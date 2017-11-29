package com.tyranotyrano.steadyhard.model.remote;

import android.util.Log;

import com.tyranotyrano.steadyhard.model.remote.datasource.SplashDataSource;
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
 * Created by cyj on 2017-11-28.
 */

public class SplashRemoteDataSource implements SplashDataSource {
    final String TAG = "=SplashRemoteDataSource";

    @Override
    public String startAutoLogin(String token) {
        // 자동로그인을 위한 네트워크 처리
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        String cookie = null;
        String message = null;

        // 토큰 정보를 담은 RequestBody 생성
        /*RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .build();*/
        RequestBody requestBody = new FormBody.Builder()
                .add("token", token)
                .build();

        try {
            response = OkHttpAPICall.POST(client, NetworkDefineConstant.SERVER_URL_AUTO_LOGIN, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of startAutoLogin() is null.");

                return null;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());
                cookie = response.header("Set-Cookie");

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

        return cookie;
    }
}
