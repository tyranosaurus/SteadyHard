package com.tyranotyrano.steadyhard.model.remote;

import android.util.Log;

import com.tyranotyrano.steadyhard.model.remote.datasource.ProfileManagerDataSource;
import com.tyranotyrano.steadyhard.network.NetworkDefineConstant;
import com.tyranotyrano.steadyhard.network.OkHttpAPICall;
import com.tyranotyrano.steadyhard.network.OkHttpInitSingtonManager;
import com.tyranotyrano.steadyhard.view.MainActivity;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by cyj on 2017-12-04.
 */

public class ProfileManagerRemoteDataSource implements ProfileManagerDataSource {
    final String TAG = "===ProfileManagerRDS";

    @Override
    public boolean deleteUser(String deletePassword) {
        OkHttpClient client = OkHttpInitSingtonManager.getOkHttpClient();
        Response response = null;

        boolean result = false;
        String message = null;

        // 리퀘스트바디
        RequestBody requestBody = new FormBody.Builder()
                .add("email", MainActivity.user.getEmail())
                .add("deletePassword", deletePassword)
                .build();

        try {
            response = OkHttpAPICall.DELETE(client, NetworkDefineConstant.SERVER_URL_DELETE_USER, requestBody);

            if ( response == null ) {
                Log.e(TAG, "Response of deleteUser() is null.");

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
