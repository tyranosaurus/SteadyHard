package com.tyranotyrano.steadyhard.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.view.fragment.HomeFragment;

import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @Override
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

        /** 홈 프래그먼트 세팅 */
        HomeFragment homeFragment = HomeFragment.newInstance("param1", "param2");
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragmentContainer, homeFragment).commit();

        /** 프로필 프래그먼트 세팅하는 부분 */
        //ProfileFragment profileFragment = ProfileFragment.newInstance("param1", "param2");
        //getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragmentContainer, profileFragment).commit();

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
