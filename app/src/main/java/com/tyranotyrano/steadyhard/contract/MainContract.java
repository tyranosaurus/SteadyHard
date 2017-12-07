package com.tyranotyrano.steadyhard.contract;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;
import com.tyranotyrano.steadyhard.model.remote.datasource.MainDataSource;
import com.tyranotyrano.steadyhard.view.fragment.ContentFragment;
import com.tyranotyrano.steadyhard.view.fragment.HomeFragment;
import com.tyranotyrano.steadyhard.view.fragment.ProfileFragment;

/**
 * Created by cyj on 2017-11-28.
 */

public interface MainContract {
    // MainActivity 관련 View 처리
    interface View extends BaseView {
        void clearCookie();
        void clearUserInfo();
        void setOnBottomNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationItemSelectedListener);
        void showSnackBar(String message);
    }

    // MainActivity 관련 Presenter 처리
    interface Presenter extends BasePresenter<MainContract.View> {
        // Model 관련 처리
        void setMainRepository(MainDataSource mainDataSource);

        void setBottomNavigationView(FragmentManager fragmentManager, HomeFragment homeFragment, ContentFragment contentFragment, ProfileFragment profileFragment);
        void setSearchViewSettings(MenuItem searchItem);
        void clearSessionToken(String token);
        void clearUserInfo();
        Fragment getCurrentFragment();
    }
}
