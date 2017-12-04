package com.tyranotyrano.steadyhard.contract;

import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;
import com.tyranotyrano.steadyhard.model.remote.datasource.ProfileManagerDataSource;

/**
 * Created by cyj on 2017-12-04.
 */

public interface ProfileManagerContract {
    // ProfileManagerActivity 관련 View 처리
    interface View extends BaseView {
        void showSnackBar(String message);
        void clearSharedPreferencesData();
        void callLoginActivity();
    }

    // ProfileManagerActivity 관련 Presenter 처리
    interface Presenter extends BasePresenter<ProfileManagerContract.View> {
        // model 관련 처리
        void setProfileManagerRepository(ProfileManagerDataSource profileManagerDataSource);

        AlertDialog.Builder buildAlertDialog(AlertDialog.Builder builder, EditText edittextInputPassword);
    }
}
