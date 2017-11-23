package com.tyranotyrano.steadyhard.view.fragment;

import android.content.Context;
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
import com.tyranotyrano.steadyhard.model.SteadyContent;
import com.tyranotyrano.steadyhard.model.SteadyProject;
import com.tyranotyrano.steadyhard.view.HomeActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContentFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private HomeActivity activity = null;
    private OnFragmentInteractionListener mListener;

    public ContentFragment() {
        // Required empty public constructor
    }

    public static ContentFragment newInstance(String param1, String param2) {
        ContentFragment fragment = new ContentFragment();
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

        // Steady Content 프래그먼트 레이아웃
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_content, container, false);

        /** Steady Content 가 들어있는 리사이클러뷰 설정 */
        RecyclerView recyclerViewSteadyContent = (RecyclerView)rootView.findViewById(R.id.recyclerViewSteadyContent);
        recyclerViewSteadyContent.setHasFixedSize(true);

        SteadyContentRecyclerViewAdapter adapter = new SteadyContentRecyclerViewAdapter();
        adapter.addItem(new SteadyContent(new SteadyProject(R.drawable.logo_black_star, "타이틀11111111111111111111111111111111111111111111", 100, 100, "설명")
                , R.drawable.icon_project_image_default, "내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용ㅍ"));
        adapter.addItem(new SteadyContent(new SteadyProject(R.drawable.logo_black_star, "타이틀2", 70, 100, "설명"), R.drawable.icon_project_image_default, "내용2"));
        adapter.addItem(new SteadyContent(new SteadyProject(R.drawable.logo_black_star, "타이틀3", 30, 100, "설명"), R.drawable.icon_project_image_default, "내용3"));
        adapter.addItem(new SteadyContent(new SteadyProject(R.drawable.logo_black_star, "타이틀4", 4, 100, "설명"), R.drawable.icon_project_image_default, "내용4"));
        adapter.addItem(new SteadyContent(new SteadyProject(R.drawable.logo_black_star, "타이틀5", 5, 100, "설명"), R.drawable.icon_project_image_default, "내용5"));
        adapter.addItem(new SteadyContent(new SteadyProject(R.drawable.logo_black_star, "타이틀6", 6, 100, "설명"), R.drawable.icon_project_image_default, "내용6"));
        adapter.addItem(new SteadyContent(new SteadyProject(R.drawable.logo_black_star, "타이틀7", 7, 100, "설명"), R.drawable.icon_project_image_default, "내용7"));
        adapter.addItem(new SteadyContent(new SteadyProject(R.drawable.logo_black_star, "타이틀8", 8, 100, "설명"), R.drawable.icon_project_image_default, "내용8"));
        adapter.addItem(new SteadyContent(new SteadyProject(R.drawable.logo_black_star, "타이틀9", 9, 100, "설명"), R.drawable.icon_project_image_default, "내용9"));
        adapter.addItem(new SteadyContent(new SteadyProject(R.drawable.logo_black_star, "타이틀10", 10, 100, "설명"), R.drawable.icon_project_image_default, "내용10"));
        adapter.addItem(new SteadyContent(new SteadyProject(R.drawable.logo_black_star, "타이틀11", 11, 100, "설명"), R.drawable.icon_project_image_default, "내용11"));
        adapter.addItem(new SteadyContent(new SteadyProject(R.drawable.logo_black_star, "타이틀12", 12, 100, "설명"), R.drawable.icon_project_image_default, "내용12"));
        recyclerViewSteadyContent.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewSteadyContent.setLayoutManager(layoutManager);

        //root.findViewById() 메소드를 이용해서 각 리니어레이아웃 가져온 다음 VISIBILITY, GONE 설정
        LinearLayout linearLayoutContentDefault = (LinearLayout) rootView.findViewById(R.id.linearLayoutContentDefault);
        LinearLayout linearLayoutContentSteadyContent = (LinearLayout) rootView.findViewById(R.id.linearLayoutContentSteadyContent);
        if (adapter.getItemCount() > 0 ) {
            linearLayoutContentSteadyContent.setVisibility(View.VISIBLE);
            linearLayoutContentDefault.setVisibility(View.GONE);
        } else {
            linearLayoutContentDefault.setVisibility(View.VISIBLE);
            linearLayoutContentSteadyContent.setVisibility(View.GONE);
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

    /** 리사이클러뷰 어댑터 */
    private class SteadyContentRecyclerViewAdapter extends RecyclerView.Adapter<SteadyContentRecyclerViewAdapter.SteadyContentViewHolder> {

        // 아이템을 저장할 리스트
        ArrayList<SteadyContent> items = new ArrayList<>();

        public void addItem(SteadyContent item) {
            items.add(item);
        }

        @Override
        public SteadyContentRecyclerViewAdapter.SteadyContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_steady_content, parent, false);

            return new SteadyContentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SteadyContentRecyclerViewAdapter.SteadyContentViewHolder holder, int position) {
            // 뷰홀더에서 사용할 아이템을 가져온다.
            SteadyContent item = items.get(position);

            // 콘텐츠의 툴바
            holder.circleImageViewProjectImage.setImageResource(item.getSteadyProject().getProjectImage());
            holder.textViewProjectTitle.setText(item.getSteadyProject().getProjectTitle());
            holder.textViewCurrentDays.setText(String.valueOf(item.getSteadyProject().getCurrentDays()));
            holder.textViewCompleteDays.setText(String.valueOf(item.getSteadyProject().getCompleteDays()));

            setDurationColor(holder, item.getSteadyProject().getCurrentDays(), item.getSteadyProject().getCompleteDays());

            // 콘텐츠의 내용
            holder.imageViewContentImage.setImageResource(item.getContentImage());
            holder.textViewContentText.setText(item.getContentText());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void setDurationColor(SteadyContentRecyclerViewAdapter.SteadyContentViewHolder holder, int currentDays, int completeDays) {
            if ( currentDays >= 1 && currentDays <=30 ) {
                holder.textViewOpenBracket.setTextColor(getResources().getColor(R.color.colorYellow));
                holder.textViewCurrentDays.setTextColor(getResources().getColor(R.color.colorYellow));
                holder.textViewPer.setTextColor(getResources().getColor(R.color.colorYellow));
                holder.textViewCompleteDays.setTextColor(getResources().getColor(R.color.colorYellow));
                holder.textViewCloseBracket.setTextColor(getResources().getColor(R.color.colorYellow));
            } else if ( currentDays >= 31 && currentDays <=70 ) {
                holder.textViewOpenBracket.setTextColor(getResources().getColor(R.color.colorBlueSky));
                holder.textViewCurrentDays.setTextColor(getResources().getColor(R.color.colorBlueSky));
                holder.textViewPer.setTextColor(getResources().getColor(R.color.colorBlueSky));
                holder.textViewCompleteDays.setTextColor(getResources().getColor(R.color.colorBlueSky));
                holder.textViewCloseBracket.setTextColor(getResources().getColor(R.color.colorBlueSky));
            } else if ( currentDays >= 71 && currentDays <= completeDays ) {
                holder.textViewOpenBracket.setTextColor(getResources().getColor(R.color.colorGreen));
                holder.textViewCurrentDays.setTextColor(getResources().getColor(R.color.colorGreen));
                holder.textViewPer.setTextColor(getResources().getColor(R.color.colorGreen));
                holder.textViewCompleteDays.setTextColor(getResources().getColor(R.color.colorGreen));
                holder.textViewCloseBracket.setTextColor(getResources().getColor(R.color.colorGreen));
            }
        }

        public class SteadyContentViewHolder extends RecyclerView.ViewHolder {

            // 콘텐츠의 툴바
            CircleImageView circleImageViewProjectImage = null;
            TextView textViewProjectTitle = null;
            TextView textViewOpenBracket = null;
            TextView textViewCurrentDays = null;
            TextView textViewPer = null;
            TextView textViewCompleteDays = null;
            TextView textViewCloseBracket = null;
            ImageView imageViewProjectMenu = null;

            // 콘텐츠의 내용
            ImageView imageViewContentImage = null;
            TextView textViewContentText = null;

            public SteadyContentViewHolder(View itemView) {
                super(itemView);

                // 콘텐츠의 툴바
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
                        Snackbar.make(v, "콘텐츠의 메뉴버튼 클릭", Snackbar.LENGTH_SHORT).show();
                    }
                });

                // 콘텐츠의 내용
                imageViewContentImage = (ImageView) itemView.findViewById(R.id.imageViewContentImage);
                textViewContentText = (TextView) itemView.findViewById(R.id.textViewContentText);
            }
        }
    }
}
