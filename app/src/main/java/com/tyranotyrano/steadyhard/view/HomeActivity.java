package com.tyranotyrano.steadyhard.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tyranotyrano.steadyhard.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /** 툴바 세팅 */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 앱 기본 이름 안보이게 하는 것.
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
