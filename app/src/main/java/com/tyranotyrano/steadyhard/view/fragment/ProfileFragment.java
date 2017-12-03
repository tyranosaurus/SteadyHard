package com.tyranotyrano.steadyhard.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.application.SteadyHardApplication;
import com.tyranotyrano.steadyhard.contract.ProfileContract;
import com.tyranotyrano.steadyhard.model.ProfileRepository;
import com.tyranotyrano.steadyhard.model.remote.ProfileRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.ProfileDataSource;
import com.tyranotyrano.steadyhard.presenter.ProfilePresenter;
import com.tyranotyrano.steadyhard.view.LoginActivity;
import com.tyranotyrano.steadyhard.view.MainActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Copyright 2016 Philipp Jahoda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 * */

public class ProfileFragment extends Fragment implements ProfileContract.View {
    private static final String TAG = "========ProfileFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Context mContext = null;
    private MainActivity activity = null;

    ProfilePresenter mPresenter = null;
    ProfileDataSource mRepository = null;

    Unbinder unbinder = null;

    @BindView(R.id.circleImageViewProfileFragmentProfileImage) CircleImageView circleImageViewProfileFragmentProfileImage;
    @BindView(R.id.textViewProfileFragmentProfileNickname) TextView textViewProfileFragmentProfileNickname;
    @BindView(R.id.textViewProfileFragmentProfileEmail) TextView textViewProfileFragmentProfileEmail;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mContext = context;
        this.activity = (MainActivity)getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_profile, container, false);
        // 버터나이프
        unbinder = ButterKnife.bind(this, rootView);

        init();

        /** 파이 차트 */
        PieChart pieChart = pieChart = (PieChart)rootView.findViewById(R.id.pieChart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        // Pie Chart 가운데 구멍 설정
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(getResources().getColor(R.color.colorWhite));
        pieChart.setTransparentCircleRadius(61f);

        // Pie Chart 가운데 글자 설정
        pieChart.setCenterText("나의 꾸준함\n점수는?");
        pieChart.setCenterTextSize(13);
        pieChart.setCenterTextColor(getResources().getColor(R.color.colorBlack));

        // Pie Chart y축 데이터 설정
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        yValues.add(new PieEntry(3f,"Success"));
        yValues.add(new PieEntry(2f,"Ongoing"));
        yValues.add(new PieEntry(1f,"Fail"));

        // Pie Chart 애니메이션 설정
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        // Pie Chart 간격, 색 템플릿, 크기 등 설정
        PieDataSet dataSet = new PieDataSet(yValues, null);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        // Pie Chart 데이터 포맷, 크기, 색 등 설정
        PieData data = new PieData((dataSet));
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(getResources().getColor(R.color.colorBlack));

        // Pie Chart x축 데이터 글씨색 설정
        pieChart.setEntryLabelColor(getResources().getColor(R.color.colorBlack));

        pieChart.setData(data);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
        mPresenter.detachView();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        this.mContext = null;
        this.activity = null;
    }

    private void init() {
        // Presenter에 View 할당
        mPresenter = new ProfilePresenter();
        mPresenter.attachView(this);
        // Presenter에 Model 할당
        mRepository = new ProfileRepository(new ProfileRemoteDataSource());
        mPresenter.setProfileRepository(mRepository);

        // 유저 프로필사진, 닉네임, 이메일 설정
        if ( MainActivity.user.getProfileImage() != null ) {
            Glide.with(ProfileFragment.this)
                    .load(MainActivity.user.getProfileImage())
                    .into(circleImageViewProfileFragmentProfileImage);
        } else {
            Glide.with(ProfileFragment.this)
                    .load(R.drawable.icon_profile_default_black)
                    .into(circleImageViewProfileFragmentProfileImage);
        }
        textViewProfileFragmentProfileNickname.setText(MainActivity.user.getNickname());
        textViewProfileFragmentProfileEmail.setText(MainActivity.user.getEmail());
    }

    @OnClick(R.id.textViewProfileFragmentLogout)
    public void onClick() {
        // 로그아웃 처리
        activity.setUserLogout();
        mPresenter.clearSessionToken(MainActivity.user.getToken());
    }

    @Override
    public void clearCookie() {
        // SteadyHardApplication에 저장된 쿠키정보 초기화
        SteadyHardApplication.clearCookie();

        // SharedPreferences에 저장된 쿠키정보 삭제
        SharedPreferences cookiePreferences = activity.getSharedPreferences("cookie", MODE_PRIVATE);
        SharedPreferences.Editor cookieInfoEditor = cookiePreferences.edit();
        cookieInfoEditor.clear();
        cookieInfoEditor.commit();
    }

    @Override
    public void clearUserInfo() {
        // SharedPreferences에 저장된 유저정보 삭제
        SharedPreferences userInfoPreferences = activity.getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor userInfoEditor = userInfoPreferences.edit();
        userInfoEditor.clear();
        userInfoEditor.commit();
    }

    @Override
    public void callLoginActivity() {
        Intent intent = new Intent(activity, LoginActivity.class);
        startActivity(intent);

        activity.finish();
    }

    @Override
    public void turnOffAutoLogin() {
        activity.setUnAutoLogin();
    }
}
