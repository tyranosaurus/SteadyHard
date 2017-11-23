package com.tyranotyrano.steadyhard.view;

import android.content.Intent;
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
import com.tyranotyrano.steadyhard.view.fragment.ContentFragment;
import com.tyranotyrano.steadyhard.view.fragment.HomeFragment;
import com.tyranotyrano.steadyhard.view.fragment.ProfileFragment;

import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    /** BottomNavigationView 프래그먼트 세팅하는 부분*/
    Fragment fragment = null;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener= null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        /** 툴바 세팅 */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 앱 기본 이름 안보이게 하는 것.

        /** 플로팅 액션 버튼 세팅하는 부분 */
        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NewSteadyProjectActivity.class);
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
        final HomeFragment homeFragment = HomeFragment.newInstance("param1", "param2");
        // 콘텐츠 프래그먼트
        final ContentFragment contentFragment = ContentFragment.newInstance("param1", "param2");
        // 프로필 프래그먼트
        final ProfileFragment profileFragment = ProfileFragment.newInstance("param1", "param2");
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

                return true;
            }
        };
        // 바텀네비게이션뷰에 itemSelected 리스너 세팅
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /** init() 함수 만들 것 */
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
}
