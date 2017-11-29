package com.tyranotyrano.steadyhard.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.application.SteadyHardApplication;
import com.tyranotyrano.steadyhard.contract.MainContract;
import com.tyranotyrano.steadyhard.model.MainRepository;
import com.tyranotyrano.steadyhard.model.data.User;
import com.tyranotyrano.steadyhard.model.remote.MainRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.MainDataSource;
import com.tyranotyrano.steadyhard.presenter.MainPresenter;
import com.tyranotyrano.steadyhard.view.fragment.ContentFragment;
import com.tyranotyrano.steadyhard.view.fragment.HomeFragment;
import com.tyranotyrano.steadyhard.view.fragment.ProfileFragment;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    public static final String TAG = "==========MainActivity";
    public static User user = null;

    // User SharedPreferences
    SharedPreferences userInfoPreferences = null;

    // Presenter, Movel
    MainContract.Presenter mPresenter = null;
    MainDataSource mRepository = null;

    /** BottomNavigationView 프래그먼트 세팅하는 부분 */
    Fragment fragment = null;
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener= null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // ButterKnife 세팅
        ButterKnife.bind(this);
        // 스플래시 액티비티 종료
        SplashActivity.SPLASH_ACTIVITY.finish();

        userInfoPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);

        // Presenter에 View 할당
        mPresenter = new MainPresenter();
        mPresenter.attachView(this);
        // Presenter에 Model 할당
        mRepository = new MainRepository(new MainRemoteDataSource());
        mPresenter.setMainRepository(mRepository);

        /** 툴바 세팅 */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 앱 기본 이름 안보이게 하는 것.

        /** 플로팅 액션 버튼 세팅하는 부분 */
        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewSteadyProjectActivity.class);
                startActivity(intent);
            }
        });

        /** 홈 프래그먼트 세팅하는 부분 */
        //HomeFragment homeFragment = HomeFragment.newInstance("param1", "param2");
        //getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragmentContainer, homeFragment).commit();

        /** 콘텐츠 프래그먼트 세팅하는 부분 */
        //ContentFragment contentFragment = ContentFragment.newInstance("param1", "param2");
        //getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragmentContainer, contentFragment).commit();

        /** 프로필 프래그먼트 세팅하는 부분 */
        //ProfileFragment profileFragment = ProfileFragment.newInstance("param1", "param2");
        //getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragmentContainer, profileFragment).commit();

        /** BottomNavigationView 프래그먼트 세팅하는 부분*/
        // 홈 프래그먼트
        final HomeFragment homeFragment = HomeFragment.newInstance("Home", "MainActivity");
        // 콘텐츠 프래그먼트
        final ContentFragment contentFragment = ContentFragment.newInstance("Content", "MainActivity");
        // 프로필 프래그먼트
        final ProfileFragment profileFragment = ProfileFragment.newInstance("Profile", "MainActivity");
        // 바텀 네비게이션 뷰
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        // 첫 시작 프래그먼트로 홈 프래그먼트 지정
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayoutFragmentContainer, homeFragment).commit();

        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
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

                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out)
                        .replace(R.id.frameLayoutFragmentContainer, fragment)
                        .commit();

                /** 이런식으로 프래그먼트에 따라 Floating Action 버튼 기능 설정할 것 - 기능은 함수로 따로 구현 */
                /*if ( fragment instanceof ProfileFragment ) {
                    floatingActionButton.setVisibility(View.GONE);
                }*/

                return true;
            }
        };
        // 바텀네비게이션뷰에 itemSelected 리스너 세팅
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /** init() 함수 만들 것 */
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // MainActivity 종료시 세션에 있는 토큰 정보 삭제
        mPresenter.clearSessionToken(user.getToken());

        // 자동 로그인이 아닌 경우
        SharedPreferences autoLoginPreferences = getSharedPreferences("autoLogin", MODE_PRIVATE);
        boolean isAutoLogin = autoLoginPreferences.getBoolean("isAutoLogin", false);

        if ( !isAutoLogin ) {
            mPresenter.clearUserInfo();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_searchview, menu);

        /** SearchView 설정 */
        MenuItem searchItem = menu.findItem(R.id.menuItemSearch);
        // 1. SearchView : hint 설정
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("검색어를 입력하세요.");
        // 2. SearchView : underline 제거
        searchView.findViewById(android.support.v7.appcompat.R.id.search_plate).setBackgroundColor(Color.parseColor("#ffffffff"));

        return true;
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
        SharedPreferences.Editor userInfoEditor = userInfoPreferences.edit();
        userInfoEditor.clear();
        userInfoEditor.commit();
    }

    public void setUnAutoLogin() {
        SharedPreferences autoLoginPreferences = getSharedPreferences("autoLogin", MODE_PRIVATE);
        SharedPreferences.Editor autoLoginEditor = autoLoginPreferences.edit();
        autoLoginEditor.putBoolean("isAutoLogin", false);
        autoLoginEditor.commit();
    }

    private void init() {
        int no = userInfoPreferences.getInt("no", -1);
        String email = userInfoPreferences.getString("email", null);
        String token = userInfoPreferences.getString("token", null);
        String profile_image = userInfoPreferences.getString("profile_image", null);
        String nickname = userInfoPreferences.getString("nickname", null);

        user = new User(no, email, token, profile_image, nickname);
    }
}
