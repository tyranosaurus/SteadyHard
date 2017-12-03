package com.tyranotyrano.steadyhard.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.HomeContract;
import com.tyranotyrano.steadyhard.contract.adapter.SteadyProjectAdapterContract;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.presenter.HomePresenter;
import com.tyranotyrano.steadyhard.view.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment implements HomeContract.View {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private MainActivity activity = null;
    private HomePresenter mPresenter = null;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Activity 할당
        activity = (MainActivity)getActivity();
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

        /** MVP 패턴으로 모두 바꿀 것! */
        /** 프로젝트의 개수가 0 이면 홈 디폴트 리니어레이아웃, 0 보다 크면 홈 프로젝트 리니어레이아웃 */
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);

        /** Steady Project 가 들어있는 리사이클러뷰 설정 */
        RecyclerView recyclerViewSteadyProject = (RecyclerView)rootView.findViewById(R.id.recyclerViewSteadyProject);
        recyclerViewSteadyProject.setHasFixedSize(true);

        SteadyProjectRecyclerViewAdapter adapter = new SteadyProjectRecyclerViewAdapter();
        /*adapter.addItem(new SteadyProject("타이틀11111111111111111111111111111111111111111111", R.drawable.logo_black_star, 100, 100, "2017-11-01", "설명"));
        adapter.addItem(new SteadyProject("타이틀2 : 매일 아침에 조깅 10km 하기!", R.drawable.logo_black_star, 70, 100, "2017-11-01", "설명"));
        adapter.addItem(new SteadyProject("타이틀3", R.drawable.logo_black_star, 30, 100, "2017-11-01", "설명"));
        adapter.addItem(new SteadyProject("타이틀4", R.drawable.logo_black_star, 4, 100, "2017-11-02", "설명"));
        adapter.addItem(new SteadyProject("타이틀5", R.drawable.logo_black_star, 5, 100, "2017-11-02", "설명"));
        adapter.addItem(new SteadyProject("타이틀6", R.drawable.logo_black_star, 6, 100, "2017-11-02", "설명"));
        adapter.addItem(new SteadyProject("타이틀7", R.drawable.logo_black_star, 7, 100, "2017-11-02", "설명"));
        adapter.addItem(new SteadyProject("타이틀8", R.drawable.logo_black_star, 8, 100, "2017-11-03", "설명"));
        adapter.addItem(new SteadyProject("타이틀9", R.drawable.logo_black_star, 9, 100, "2017-11-03", "설명"));
        adapter.addItem(new SteadyProject("타이틀10", R.drawable.logo_black_star, 10, 100, "2017-11-03", "설명"));
        adapter.addItem(new SteadyProject("타이틀11", R.drawable.logo_black_star, 11, 100, "2017-11-03", "설명"));
        adapter.addItem(new SteadyProject("타이틀12", R.drawable.logo_black_star, 12, 100, "2017-11-04", "설명"));
        adapter.addItem(new SteadyProject("타이틀13", R.drawable.logo_black_star, 13, 100, "2017-11-04", "설명"));
        adapter.addItem(new SteadyProject("타이틀14", R.drawable.logo_black_star, 14, 100, "2017-11-04", "설명"));
        adapter.addItem(new SteadyProject("타이틀15", R.drawable.logo_black_star, 15, 100, "2017-11-04", "설명"));
        adapter.addItem(new SteadyProject("타이틀16", R.drawable.logo_black_star, 16, 100, "2017-11-05", "설명"));
        adapter.addItem(new SteadyProject("타이틀17", R.drawable.logo_black_star, 17, 100, "2017-11-05", "설명"));
        adapter.addItem(new SteadyProject("타이틀18", R.drawable.logo_black_star, 18, 100, "2017-11-05", "설명"));
        adapter.addItem(new SteadyProject("타이틀19", R.drawable.logo_black_star, 19, 100, "2017-11-06", "설명"));
        adapter.addItem(new SteadyProject("타이틀20", R.drawable.logo_black_star, 20, 100, "2017-11-06", "설명"));*/
        recyclerViewSteadyProject.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewSteadyProject.setLayoutManager(layoutManager);

        //root.findViewById() 메소드를 이용해서 각 리니어레이아웃 가져온 다음 VISIBILITY, GONE 설정
        LinearLayout linearLayoutDefault = (LinearLayout) rootView.findViewById(R.id.linearLayoutHomeDefault);
        LinearLayout linearLayoutHomeSteadyProject = (LinearLayout) rootView.findViewById(R.id.linearLayoutHomeSteadyProject);
        if (adapter.getItemCount() > 0 ) {
            linearLayoutHomeSteadyProject.setVisibility(View.VISIBLE);
            linearLayoutDefault.setVisibility(View.GONE);
        } else {
            linearLayoutDefault.setVisibility(View.VISIBLE);
            linearLayoutHomeSteadyProject.setVisibility(View.GONE);
        }

        /** HomePresenter 세팅하는 부분 : 코드 위치 맞는지 확인하고 다시 수정할 것.*/
        // Presenter 할당
        mPresenter = new HomePresenter();
        // Presenter에 HomeContract.View 할당
        mPresenter.attachView(this);
        // SteadyProjectAdapterContract의 View 할당
        mPresenter.setSteadyProjectAdapterView(adapter);
        // SteadyProjectAdapterContract의 Model 할당
        mPresenter.setSteadyProjectAdapterModel(adapter);
        // SteadyProjectAdapterContract의 OnItemClickListener 할당(SteadyProjectAdapterContract의 View 할당한 이후에 호출해야함)
        mPresenter.setSteadyProjectAdapterOnItemClickListener();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Presenter 해제
        mPresenter.detachView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Activity 해제
        activity = null;
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    /** 일단 리사이클러뷰의 어댑터를 여기에다가 정의 : 나중에 mvp 패턴에 맞게 수정할 것! */
    /** 리사이클러뷰 어댑터 */
    private class SteadyProjectRecyclerViewAdapter extends RecyclerView.Adapter<SteadyProjectRecyclerViewAdapter.SteadyProjectViewHolder>
                                                   implements SteadyProjectAdapterContract.View, SteadyProjectAdapterContract.Model {

        // SteadyProjectAdapterContract 의 OnItemClickListener
        SteadyProjectAdapterContract.OnItemClickListener itemClickListener = null;

        // 아이템을 저장할 리스트
        List<SteadyProject> items = new ArrayList<>();

        // 뷰홀더를 생성
        @Override
        public SteadyProjectRecyclerViewAdapter.SteadyProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 아이템이 배치될 레이아웃을 부모 뷰그룹에 인플레이션한다.
            // 여기서 부모 뷰그룹은 리사이클러뷰가 된다.
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_steady_project, parent, false);

            return new SteadyProjectViewHolder(parent.getContext(), parent, itemClickListener);
        }

        // 뷰홀더를 데이터와 바인딩 해준다. (뷰홀더 재사용)
        // 절대 onBindViewHolder() 메소드 안에서 클릭 등 이벤트 처리 코드 구현하지 말 것!
        @Override
        public void onBindViewHolder(SteadyProjectRecyclerViewAdapter.SteadyProjectViewHolder holder, int position) {
            // 뷰홀더에서 사용할 아이템을 가져온다.
            SteadyProject item = items.get(position);

            // 프로젝트 이미지 설정
            holder.circleImageViewProjectImage.setImageResource(item.getProjectImage());
            // 프로젝트 타이틀 설정
            holder.textViewProjectTitle.setText(item.getProjectTitle());
            // 프로젝트 현재 진행일수 설정
            holder.textViewCurrentDays.setText(String.valueOf(item.getCurrentDays()));
            // 프로젝트 완료 일수 설정
            holder.textViewCompleteDays.setText(String.valueOf(item.getCompleteDays()));
            // 프로젝트 등록 날짜 설정
            holder.textViewProjectDate.setText(holder.setProjectDateFormat(item.getProjectDate())); /**  아이템에 날짜 추가하기*/

            /** currentDays에 따른 색깔 구분
             *  여기서는 완료날짜가 100으로 고정되어있지만 나중에는 완료날짜에 따른 현재날짜의 비율로 계산할 것
             *
             *  추가 : 프로젝트 실패시 빨간색으로 표시할 것.
             * */
            // holder의 이벤트
            holder.setDurationColor(item.getCurrentDays(), item.getCompleteDays());
            holder.onBindItemClickListener(item, position);
        }

        @Override
        public int getItemCount() {
            // 어댑터가 가지고있는 아이템의 총 개수를 반환한다.
            return items.size();
        }

        @Override
        public void notifyAdapter() {
            notifyDataSetChanged();
        }

        @Override
        public void addItem(SteadyProject item) {
            items.add(item);
        }

        @Override
        public void setOnItemClickListener(SteadyProjectAdapterContract.OnItemClickListener onItemClickListener) {
            if ( onItemClickListener != null ) {
                this.itemClickListener = onItemClickListener;
            }
        }

        @Override
        public SteadyProject getItem(int position) {
            return items.get(position);
        }

        public class SteadyProjectViewHolder extends RecyclerView.ViewHolder {

            Context context = null;
            SteadyProjectAdapterContract.OnItemClickListener itemClickListener = null;

            CircleImageView circleImageViewProjectImage = null;
            TextView textViewProjectTitle = null;
            TextView textViewOpenBracket = null;
            TextView textViewCurrentDays = null;
            TextView textViewPer = null;
            TextView textViewCompleteDays = null;
            TextView textViewCloseBracket = null;
            TextView textViewProjectDate = null;
            ImageView imageViewProjectMenu = null;

            public SteadyProjectViewHolder(Context context, ViewGroup parent, SteadyProjectAdapterContract.OnItemClickListener itemClickListener) {
                super(LayoutInflater.from(context).inflate(R.layout.item_steady_project, parent, false));

                if ( context != null ) {
                    this.context = context;
                }

                if ( itemClickListener != null ) {
                    this.itemClickListener = itemClickListener;
                }

                circleImageViewProjectImage = (CircleImageView) itemView.findViewById(R.id.circleImageViewProjectImage);
                textViewProjectTitle = (TextView) itemView.findViewById(R.id.textViewProjectTitle);
                textViewOpenBracket = (TextView) itemView.findViewById(R.id.textViewOpenBracket);
                textViewCurrentDays = (TextView) itemView.findViewById(R.id.textViewCurrentDays);
                textViewPer = (TextView) itemView.findViewById(R.id.textViewPer);
                textViewCompleteDays = (TextView) itemView.findViewById(R.id.textViewCompleteDays);
                textViewCloseBracket = (TextView) itemView.findViewById(R.id.textViewCloseBracket);
                textViewProjectDate = (TextView) itemView.findViewById(R.id.textViewProjectDate);
                imageViewProjectMenu = (ImageView) itemView.findViewById(R.id.imageViewProjectMenu);
                imageViewProjectMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, "프로젝트의 메뉴버튼 클릭", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }

            public String setProjectDateFormat(String projectDate) {

                String formattedProjectDate = "0000.00.00";

                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(projectDate);
                    formattedProjectDate = new SimpleDateFormat("yyyy.MM.dd").format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return formattedProjectDate;
            }

            public void onBindItemClickListener(SteadyProject item, final int position) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( itemClickListener != null ) {
                            itemClickListener.onItemClick(position);
                        }
                    }
                });
            }

            public void setDurationColor(int currentDays, int completeDays) {
                if ( currentDays >= 1 && currentDays <=30 ) {
                    textViewOpenBracket.setTextColor(Color.parseColor("#FFD54F"));
                    textViewCurrentDays.setTextColor(Color.parseColor("#FFD54F"));
                    textViewPer.setTextColor(Color.parseColor("#FFD54F"));
                    textViewCompleteDays.setTextColor(Color.parseColor("#FFD54F"));
                    textViewCloseBracket.setTextColor(Color.parseColor("#FFD54F"));
                } else if ( currentDays >= 31 && currentDays <=70 ) {
                    textViewOpenBracket.setTextColor(Color.parseColor("#4FC3F7"));
                    textViewCurrentDays.setTextColor(Color.parseColor("#4FC3F7"));
                    textViewPer.setTextColor(Color.parseColor("#4FC3F7"));
                    textViewCompleteDays.setTextColor(Color.parseColor("#4FC3F7"));
                    textViewCloseBracket.setTextColor(Color.parseColor("#4FC3F7"));
                } else if ( currentDays >= 71 && currentDays <= completeDays ) {
                    textViewOpenBracket.setTextColor(Color.parseColor("#00E676"));
                    textViewCurrentDays.setTextColor(Color.parseColor("#00E676"));
                    textViewPer.setTextColor(Color.parseColor("#00E676"));
                    textViewCompleteDays.setTextColor(Color.parseColor("#00E676"));
                    textViewCloseBracket.setTextColor(Color.parseColor("#00E676"));
                }
            }
        }
    }
}
