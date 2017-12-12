package com.tyranotyrano.steadyhard.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.application.SteadyHardApplication;
import com.tyranotyrano.steadyhard.contract.MainContract;
import com.tyranotyrano.steadyhard.model.MainRepository;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.data.User;
import com.tyranotyrano.steadyhard.model.remote.MainRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.MainDataSource;
import com.tyranotyrano.steadyhard.presenter.MainPresenter;
import com.tyranotyrano.steadyhard.view.fragment.ContentFragment;
import com.tyranotyrano.steadyhard.view.fragment.HomeFragment;
import com.tyranotyrano.steadyhard.view.fragment.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    public static final String TAG = "==========MainActivity";
    private static final int REQUEST_CODE_NEW_STEADY_PROJECT_ACTIVITY = 105;

    public static User user = null;

    // Presenter, Model
    MainContract.Presenter mPresenter = null;
    MainDataSource mRepository = null;

    boolean isLogout = false;
    Fragment fragment = null;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.floatingActionButton) FloatingActionButton floatingActionButton;
    @BindView(R.id.bottomNavigationView) BottomNavigationView bottomNavigationView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ButterKnife 세팅
        ButterKnife.bind(this);
        // 초기화
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 자동 로그인이 아닌 경우
        SharedPreferences autoLoginPreferences = getSharedPreferences("autoLogin", MODE_PRIVATE);
        boolean isAutoLogin = autoLoginPreferences.getBoolean("isAutoLogin", false);

        if ( !isAutoLogin ) {
            mPresenter.clearUserInfo();
        }
    }

    @Override
    public void finish() {
        if ( !isLogout ) {
            // 로그아웃 없이 MainActivity 종료시 세션에 있는 토큰 정보 삭제
            mPresenter.clearSessionToken(user.getToken());
        }

        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // SearchView 설정
        setSearchView(menu);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK ) {
            switch ( requestCode ) {
                case REQUEST_CODE_NEW_STEADY_PROJECT_ACTIVITY:
                    SteadyProject newSteadyProject = data.getParcelableExtra("newSteadyProject");
                    fragment = mPresenter.getCurrentFragment();

                    if ( fragment == null ) {
                        // 예외처리
                        String message = "프로젝트를 불러오는데 실패하였습니다. 새로고침을 해주세요.";
                        showSnackBar(message);

                        return;
                    }

                    if ( fragment instanceof HomeFragment ) {
                        ((HomeFragment)fragment).addNewProject(newSteadyProject);
                    } else {
                        String message = "프로젝트를 불러오는데 실패하였습니다. 새로고침을 해주세요.";
                        showSnackBar(message);

                        return;
                    }


                    String message = "새 프로젝트가 등록되었습니다.";
                    showSnackBar(message);
                    break;
                default:
                    break;
            }
        }
    }

    private void init() {
        // 스플래시 액티비티 종료
        SplashActivity.SPLASH_ACTIVITY.finish();

        // Presenter에 View 할당
        mPresenter = new MainPresenter();
        mPresenter.attachView(this);
        // Presenter에 Model 할당
        mRepository = new MainRepository(new MainRemoteDataSource());
        mPresenter.setMainRepository(mRepository);

        // 툴바 세팅
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // BottomNavigationView 세팅
        // 프래그먼트 매니저
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 홈 프래그먼트
        HomeFragment homeFragment = HomeFragment.newInstance("Home", "MainActivity");
        // 콘텐츠 프래그먼트
        ContentFragment contentFragment = ContentFragment.newInstance("Content", "MainActivity");
        // 프로필 프래그먼트
        ProfileFragment profileFragment = ProfileFragment.newInstance("Profile", "MainActivity");
        // 프래그먼트 초기화
        fragment = homeFragment;

        mPresenter.setBottomNavigationView(fragmentManager, homeFragment, contentFragment, profileFragment);

        // SwipeRefreshLayout 세팅
        setSwipeRefreshLayout();

        // 유저 정보
        setUserInfo();
    }

    public void setSearchView(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_searchview, menu);
        MenuItem searchItem = menu.findItem(R.id.menuItemSearch);
        mPresenter.setSearchViewSettings(searchItem);
    }

    public void setSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*if ( fragment instanceof HomeFragment ) {

                } else if ( fragment instanceof ContentFragment ) {

                } else if ( fragment instanceof ProfileFragment ) {

                }*/

                showSnackBar("새로고침 완료");

                // 새로고침 종료 - 원하는 작업 끝나면 호출할 것(아마 이거 종료하는 함수만들어서 onPostExcute() 에서 호출할 듯)
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
    }

    public void setUserInfo() {
        SharedPreferences userInfoPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        int no = userInfoPreferences.getInt("no", -1);
        String email = userInfoPreferences.getString("email", null);
        String token = userInfoPreferences.getString("token", null);
        String profile_image = userInfoPreferences.getString("profile_image", null);
        String nickname = userInfoPreferences.getString("nickname", null);
        user = new User(no, email, token, profile_image, nickname);
    }

    public void setUnAutoLogin() {
        SharedPreferences autoLoginPreferences = getSharedPreferences("autoLogin", MODE_PRIVATE);
        SharedPreferences.Editor autoLoginEditor = autoLoginPreferences.edit();
        autoLoginEditor.putBoolean("isAutoLogin", false);
        autoLoginEditor.commit();
    }

    public void setUserLogout() {
        isLogout = true;
    }

    @Override
    public void setOnBottomNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationItemSelectedListener) {
        if ( bottomNavigationItemSelectedListener != null ) {
            // 바텀네비게이션뷰에 itemSelected 리스너 세팅
            bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationItemSelectedListener);
        }
    }

    @OnClick(R.id.floatingActionButton)
    public void onClickFloatingActionButtion() {
        // 새 프로젝트 만드는 액티비티 호출
        Intent intent = new Intent(MainActivity.this, NewSteadyProjectActivity.class);
        startActivityForResult(intent, REQUEST_CODE_NEW_STEADY_PROJECT_ACTIVITY);
    }

    @Override
    public void clearCookie() {
        // SteadyHardApplication에 저장된 쿠키정보 초기화
        SteadyHardApplication.clearCookie();

        // SharedPreferences에 저장된 쿠키정보 삭제
        SharedPreferences cookiePreferences = getSharedPreferences("cookie", MODE_PRIVATE);
        SharedPreferences.Editor cookieInfoEditor = cookiePreferences.edit();
        cookieInfoEditor.clear();
        cookieInfoEditor.commit();
    }

    @Override
    public void clearUserInfo() {
        // 유저정보 삭제
        SharedPreferences userInfoPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor userInfoEditor = userInfoPreferences.edit();
        userInfoEditor.clear();
        userInfoEditor.commit();
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setFloatingActionButtionVisibility(int visibility) {
        floatingActionButton.setVisibility(visibility);
    }

    @Override
    public Context getActivityContext() {
        return MainActivity.this;
    }
}
