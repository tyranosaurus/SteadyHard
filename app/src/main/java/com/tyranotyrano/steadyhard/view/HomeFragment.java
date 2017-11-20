package com.tyranotyrano.steadyhard.view;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
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
import com.tyranotyrano.steadyhard.model.SteadyProject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private HomeActivity activity = null;
    private OnFragmentInteractionListener mListener;

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

        activity = (HomeActivity)getActivity();

        /** 일단 주석처리. */
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }*/
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

        /** 프로젝트의 개수가 0 이면 홈 디폴트 리니어레이아웃, 0 보다 크면 홈 프로젝트 리니어레이아웃 */
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);

        /** Steady Project 가 들어있는 리사이클러뷰 설정 */
        RecyclerView recyclerViewSteadyProject = (RecyclerView)rootView.findViewById(R.id.recyclerViewSteadyProject);
        recyclerViewSteadyProject.setHasFixedSize(true);

        SteadyProjectRecyclerViewAdapter adapter = new SteadyProjectRecyclerViewAdapter();
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀11111111111111111111111111111111111111111111", 100, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀2", 70, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀3", 30, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀4", 4, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀5", 5, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀6", 6, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀7", 7, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀8", 8, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀9", 9, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀10", 10, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀11", 11, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀12", 12, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀13", 13, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀14", 14, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀15", 15, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀16", 16, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀17", 17, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀18", 18, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀19", 19, 100));
        adapter.addItem(new SteadyProject(R.drawable.logo_black_star, "타이틀20", 20, 100));
        recyclerViewSteadyProject.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewSteadyProject.setLayoutManager(layoutManager);

        //root.findViewById() 메소드를 이용해서 각 리니어레이아웃 가져온 다음 VISIBILITY, GONE 설정
        LinearLayout linearLayoutDefault = (LinearLayout) rootView.findViewById(R.id.linearLayoutHomeDefault);
        if (adapter.getItemCount() > 0 ) {
            recyclerViewSteadyProject.setVisibility(View.VISIBLE);
            linearLayoutDefault.setVisibility(View.GONE);
        } else {
            linearLayoutDefault.setVisibility(View.VISIBLE);
            recyclerViewSteadyProject.setVisibility(View.GONE);
        }

        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    /** 일단 리사이클러뷰의 어댑터를 여기에다가 정의 : 나중에 mvp 패턴에 맞게 수정할 것! */
    /** 리사이클러뷰 어댑터 */
    private class SteadyProjectRecyclerViewAdapter extends RecyclerView.Adapter<SteadyProjectRecyclerViewAdapter.SteadyProjectViewHolder> {

        // 아이템을 저장할 리스트
        List<SteadyProject> items = new ArrayList<>();

        // 리사이클러뷰 어댑터에 데이터를 추가해주는 메소드
        public void addItem(SteadyProject item) {
            items.add(item);
        }

        // 뷰홀더를 생성
        @Override
        public SteadyProjectRecyclerViewAdapter.SteadyProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 아이템이 배치될 레이아웃을 부모 뷰그룹에 인플레이션한다.
            // 여기서 부모 뷰그룹은 리사이클러뷰가 된다.
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_steady_project, parent, false);

            return new SteadyProjectViewHolder(view);
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

            /** currentDays에 따른 색깔 구분
             *  여기서는 완료날짜가 100으로 고정되어있지만 나중에는 완료날짜에 따른 현재날짜의 비율로 계산할 것
             *
             *  추가 : 프로젝트 실패시 빨간색으로 표시할 것.
             * */
            if ( item.getCurrentDays() >= 1 && item.getCurrentDays() <=30 ) {
                holder.textViewOpenBracket.setTextColor(Color.parseColor("#FFD54F"));
                holder.textViewCurrentDays.setTextColor(Color.parseColor("#FFD54F"));
                holder.textViewPer.setTextColor(Color.parseColor("#FFD54F"));
                holder.textViewCompleteDays.setTextColor(Color.parseColor("#FFD54F"));
                holder.textViewCloseBracket.setTextColor(Color.parseColor("#FFD54F"));
            } else if ( item.getCurrentDays() >= 31 && item.getCurrentDays() <=70 ) {
                holder.textViewOpenBracket.setTextColor(Color.parseColor("#4FC3F7"));
                holder.textViewCurrentDays.setTextColor(Color.parseColor("#4FC3F7"));
                holder.textViewPer.setTextColor(Color.parseColor("#4FC3F7"));
                holder.textViewCompleteDays.setTextColor(Color.parseColor("#4FC3F7"));
                holder.textViewCloseBracket.setTextColor(Color.parseColor("#4FC3F7"));
            } else if ( item.getCurrentDays() >= 71 && item.getCurrentDays() <= item.getCompleteDays() ) {
                holder.textViewOpenBracket.setTextColor(Color.parseColor("#00E676"));
                holder.textViewCurrentDays.setTextColor(Color.parseColor("#00E676"));
                holder.textViewPer.setTextColor(Color.parseColor("#00E676"));
                holder.textViewCompleteDays.setTextColor(Color.parseColor("#00E676"));
                holder.textViewCloseBracket.setTextColor(Color.parseColor("#00E676"));
            }
        }

        @Override
        public int getItemCount() {
            // 어댑터가 가지고있는 아이템의 총 개수를 반환한다.
            return items.size();
        }

        public class SteadyProjectViewHolder extends RecyclerView.ViewHolder {

            CircleImageView circleImageViewProjectImage = null;
            TextView textViewProjectTitle = null;
            TextView textViewOpenBracket = null;
            TextView textViewCurrentDays = null;
            TextView textViewPer = null;
            TextView textViewCompleteDays = null;
            TextView textViewCloseBracket = null;
            ImageView imageViewProjectMenu = null;

            public SteadyProjectViewHolder(View itemView) {
                super(itemView);

                circleImageViewProjectImage = (CircleImageView) itemView.findViewById(R.id.circleImageViewProjectImage);
                textViewProjectTitle = (TextView) itemView.findViewById(R.id.textViewProjectTitle);
                textViewOpenBracket = (TextView) itemView.findViewById(R.id.textViewOpenBracket);
                textViewCurrentDays = (TextView) itemView.findViewById(R.id.textViewCurrentDays);
                textViewPer = (TextView) itemView.findViewById(R.id.textViewPer);
                textViewCompleteDays = (TextView) itemView.findViewById(R.id.textViewCompleteDays);
                textViewCloseBracket = (TextView) itemView.findViewById(R.id.textViewCloseBracket);
                imageViewProjectMenu = (ImageView) itemView.findViewById(R.id.imageViewProjectMenu);
                imageViewProjectMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, "프로젝트의 메뉴버튼 클릭", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
