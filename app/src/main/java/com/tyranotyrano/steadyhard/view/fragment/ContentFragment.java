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
import com.tyranotyrano.steadyhard.contract.ContentContract;
import com.tyranotyrano.steadyhard.contract.adapter.SteadyContentAdapterContract;
import com.tyranotyrano.steadyhard.model.data.SteadyContent;
import com.tyranotyrano.steadyhard.presenter.ContentPresenter;
import com.tyranotyrano.steadyhard.view.MainActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContentFragment extends Fragment implements ContentContract.View{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private MainActivity activity = null;
    private ContentPresenter mPresenter = null;

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

        // Steady Content 프래그먼트 레이아웃
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_content, container, false);

        /** Steady Content 가 들어있는 리사이클러뷰 설정 */
        RecyclerView recyclerViewSteadyContent = (RecyclerView)rootView.findViewById(R.id.recyclerViewSteadyContent);
        recyclerViewSteadyContent.setHasFixedSize(true);

        SteadyContentRecyclerViewAdapter adapter = new SteadyContentRecyclerViewAdapter();
        /*adapter.addItem(new SteadyContent(new SteadyProject("타이틀11111111111111111111111111111111111111111111", R.drawable.logo_black_star, 100, 100, "2017-11-05", "설명")
                , R.drawable.icon_project_image_default, "내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용ㅍ"));
        adapter.addItem(new SteadyContent(new SteadyProject("타이틀2", R.drawable.logo_black_star, 70, 100, "2017-11-05", "설명"), R.drawable.icon_project_image_default, "내용2"));
        adapter.addItem(new SteadyContent(new SteadyProject("타이틀3", R.drawable.logo_black_star, 30, 100, "2017-11-05", "설명"), R.drawable.icon_project_image_default, "내용3"));
        adapter.addItem(new SteadyContent(new SteadyProject("타이틀4", R.drawable.logo_black_star, 4, 100, "2017-11-05", "설명"), R.drawable.icon_project_image_default, "내용4"));
        adapter.addItem(new SteadyContent(new SteadyProject("타이틀5", R.drawable.logo_black_star, 5, 100, "2017-11-05", "설명"), R.drawable.icon_project_image_default, "내용5"));
        adapter.addItem(new SteadyContent(new SteadyProject("타이틀6", R.drawable.logo_black_star, 6, 100, "2017-11-05", "설명"), R.drawable.icon_project_image_default, "내용6"));
        adapter.addItem(new SteadyContent(new SteadyProject("타이틀7", R.drawable.logo_black_star, 7, 100, "2017-11-05", "설명"), R.drawable.icon_project_image_default, "내용7"));
        adapter.addItem(new SteadyContent(new SteadyProject("타이틀8", R.drawable.logo_black_star, 8, 100, "2017-11-05", "설명"), R.drawable.icon_project_image_default, "내용8"));
        adapter.addItem(new SteadyContent(new SteadyProject("타이틀9", R.drawable.logo_black_star, 9, 100, "2017-11-05", "설명"), R.drawable.icon_project_image_default, "내용9"));
        adapter.addItem(new SteadyContent(new SteadyProject("타이틀10", R.drawable.logo_black_star, 10, 100, "2017-11-05", "설명"), R.drawable.icon_project_image_default, "내용10"));
        adapter.addItem(new SteadyContent(new SteadyProject("타이틀11", R.drawable.logo_black_star, 11, 100, "2017-11-05", "설명"), R.drawable.icon_project_image_default, "내용11"));
        adapter.addItem(new SteadyContent(new SteadyProject("타이틀12", R.drawable.logo_black_star, 12, 100, "2017-11-05", "설명"), R.drawable.icon_project_image_default, "내용12"));*/
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

        /** ContentPresenter 세팅하는 부분 : 코드 위치 맞는지 확인하고 다시 수정할 것.*/
        // Presenter 할당
        mPresenter = new ContentPresenter();
        // Presenter에 ContentContract.View 할당
        mPresenter.attachView(this);
        // SteadyContentAdapterContract의 View 할당
        mPresenter.setSteadyContentAdapterView(adapter);
        // SteadyContentAdapterContract의 Model 할당
        mPresenter.setSteadyContentAdapterModel(adapter);
        // // SteadyContentAdapterContract의 OnItemClickListener 할당(SteadyContentAdapterContract의 View 할당한 이후에 호출해야함)
        mPresenter.setSteadyContentAdapterOnItemClickListener();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Presenter 해제
        mPresenter = null;
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

    /** 리사이클러뷰 어댑터 */
    private class SteadyContentRecyclerViewAdapter extends RecyclerView.Adapter<SteadyContentRecyclerViewAdapter.SteadyContentViewHolder>
                                                   implements SteadyContentAdapterContract.View, SteadyContentAdapterContract.Model {

        // SteadyContentAdapterContract 의 OnItemClickListener
        SteadyContentAdapterContract.OnItemClickListener itemClickListener = null;

        // 아이템을 저장할 리스트
        ArrayList<SteadyContent> items = new ArrayList<>();

        @Override
        public SteadyContentRecyclerViewAdapter.SteadyContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_steady_content, parent, false);

            return new SteadyContentViewHolder(parent.getContext(), parent, itemClickListener);
        }

        @Override
        public void onBindViewHolder(SteadyContentRecyclerViewAdapter.SteadyContentViewHolder holder, int position) {
            // 뷰홀더에서 사용할 아이템을 가져온다.
            SteadyContent item = items.get(position);

            // 콘텐츠의 툴바
            /*holder.circleImageViewProjectImage.setImageResource(R.drawable.logo_black_star);
            holder.textViewProjectTitle.setText(item.getSteadyProject().getProjectTitle());
            holder.textViewCurrentDays.setText(String.valueOf(item.getSteadyProject().getCurrentDays()));
            holder.textViewCompleteDays.setText(String.valueOf(item.getSteadyProject().getCompleteDays()));

            // 콘텐츠의 내용
            holder.imageViewContentImage.setImageResource(item.getContentImage());
            holder.textViewContentText.setText(item.getContentText());

            // holder의 이벤트
            holder.setDurationColor(item.getSteadyProject().getCurrentDays(), item.getSteadyProject().getCompleteDays());
            holder.onBindOnItemClickListener(item, position);*/
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public void notifyAdapter() {
            notifyDataSetChanged();
        }

        @Override
        public void addItem(SteadyContent item) {
            items.add(item);
        }

        @Override
        public void setOnItemClickListener(SteadyContentAdapterContract.OnItemClickListener onItemClickListener) {
            if ( onItemClickListener != null ) {
                this.itemClickListener = onItemClickListener;
            }
        }

        @Override
        public SteadyContent getItem(int position) {
            return items.get(position);
        }

        public class SteadyContentViewHolder extends RecyclerView.ViewHolder {

            Context context = null;
            SteadyContentAdapterContract.OnItemClickListener itemClickListener = null;

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

            public SteadyContentViewHolder(Context context, ViewGroup parent, SteadyContentAdapterContract.OnItemClickListener itemClickListener) {
                super(LayoutInflater.from(context).inflate(R.layout.item_steady_content, parent, false));

                if ( context != null ) {
                    this.context = context;
                }

                if ( itemClickListener != null ) {
                    this.itemClickListener = itemClickListener;
                }

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

            public void onBindOnItemClickListener(SteadyContent item, final int position) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(position);
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
