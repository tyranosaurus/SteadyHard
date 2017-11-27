package com.tyranotyrano.steadyhard.model.remote;

import android.util.Log;

import com.tyranotyrano.steadyhard.model.remote.datasource.LoginDataSource;
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
 * Created by cyj on 2017-11-26.
 */

public class LoginRemoteDataSource implements LoginDataSource {

    final String TAG = "==LoginRemoteDataSource";

    @Override
    public boolean checkLogin(String email, String password) {
        // 로그인 체크를 위한 네트워크 처리
        OkHttpClient client = null;
        Response response = null;
        boolean isLogin = false;

        client = OkHttpInitSingtonManager.getOkHttpClient();

        // 로그인 정보를 담은 RequestBody 생성
        RequestBody requestBody = new FormBody.Builder()
                .add("email", String.valueOf(email))
                .add("password", String.valueOf(password))
                .build();

        try {
            response = OkHttpAPICall.POST(client, NetworkDefineConstant.SERVER_URL_CHECK_LOGIN, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of checkLogin() is null.");

                return false;
            } else {
                JSONObject jsonFromServer = new JSONObject(response.body().string());
                isLogin = jsonFromServer.getBoolean("isLogin");
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

        return isLogin;
    }
}
