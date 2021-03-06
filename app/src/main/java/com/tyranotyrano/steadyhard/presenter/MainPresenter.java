package com.tyranotyrano.steadyhard.presenter;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.MainContract;
import com.tyranotyrano.steadyhard.model.remote.datasource.MainDataSource;
import com.tyranotyrano.steadyhard.view.fragment.ContentFragment;
import com.tyranotyrano.steadyhard.view.fragment.HomeFragment;
import com.tyranotyrano.steadyhard.view.fragment.ProfileFragment;

/**
 * Created by cyj on 2017-11-28.
 */

public class MainPresenter implements MainContract.Presenter {
    MainContract.View mView = null;
    MainDataSource mRepository = null;

    Fragment fragment = null;

    public void attachView(MainContract.View view) {
        if ( view != null ) {
            this.mView = view;
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void setMainRepository(MainDataSource mainDataSource) {
        if ( mainDataSource != null ) {
            this.mRepository = mainDataSource;
        }
    }

    @Override
    public void setBottomNavigationView(final FragmentManager fragmentManager, final HomeFragment homeFragment, final ContentFragment contentFragment, final ProfileFragment profileFragment) {
        // 첫 시작 프래그먼트로 홈 프래그먼트 지정
        fragmentManager.beginTransaction().replace(R.id.frameLayoutFragmentContainer, homeFragment).commit();
        // 프래그먼트 초기화
        fragment = homeFragment;

        BottomNavigationView.OnNavigationItemSelectedListener OnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_Home:
                        fragment = homeFragment;
                        break;
                    case R.id.bottom_Steady:
                        fragment = contentFragment;
                        break;
                    case R.id.bottom_Pofile:
                        fragment = profileFragment;
                        break;
                }

                // 이런식으로 프래그먼트에 따라 Floating Action 버튼 기능 설정
                if ( fragment instanceof HomeFragment ) {
                    mView.setFloatingActionButtionVisibility(View.VISIBLE);
                } else if ( fragment instanceof ContentFragment ) {
                    mView.setFloatingActionButtionVisibility(View.GONE);
                } else if ( fragment instanceof ProfileFragment ) {
                    mView.setFloatingActionButtionVisibility(View.GONE);
                }

                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out)
                        .replace(R.id.frameLayoutFragmentContainer, fragment)
                        .commit();
                return true;
            }
        };
        // BottomNavigationView 에 onNavigationItemSelectedListener 세팅
        mView.setOnBottomNavigationItemSelectedListener(OnNavigationItemSelectedListener);
    }

    @Override
    public void setSearchViewSettings(final MenuItem searchItem) {
        // 1. SearchView : hint 설정
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("검색어를 입력하세요.");
        // 2. SearchView : underline 제거
        searchView.findViewById(android.support.v7.appcompat.R.id.search_plate).setBackgroundColor(Color.TRANSPARENT);
        // 3. Event 리스너 설정
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 키보드 내리기
                searchItem.collapseActionView();

                // 검색어 완료시
                mView.showSnackBar("검색완료");

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    @Override
    public void clearSessionToken(String token) {
        new SessionLogoutTask().execute(token);
    }

    @Override
    public void clearUserInfo() {
        mView.clearUserInfo();
    }

    @Override
    public Fragment getCurrentFragment() {
        if ( fragment == null ) {
            return null;
        }

        return fragment;
    }

    public class SessionLogoutTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String token = params[0];
            boolean isSessionLogout = mRepository.clearSessionToken(token);

            return isSessionLogout;
        }

        @Override
        protected void onPostExecute(Boolean isSessionLogout) {
            super.onPostExecute(isSessionLogout);

            if ( isSessionLogout ) {
                mView.clearCookie();
            } else {
                // SessionLogout에 실패했을 경우
                Log.e("clearSessionToken()","There is no Token value for Session Logout.");
            }
        }
    }
}
