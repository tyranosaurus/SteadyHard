package com.tyranotyrano.steadyhard.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.ContentContract;
import com.tyranotyrano.steadyhard.contract.adapter.SteadyContentAdapterContract;
import com.tyranotyrano.steadyhard.model.ContentRepository;
import com.tyranotyrano.steadyhard.model.data.SteadyContent;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.ContentRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.ContentDataSource;
import com.tyranotyrano.steadyhard.presenter.ContentPresenter;
import com.tyranotyrano.steadyhard.view.MainActivity;
import com.tyranotyrano.steadyhard.view.ModifySteadyContentActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ContentFragment extends Fragment implements ContentContract.View{

    private final static int REQUEST_CODE_MODIFY_STEADY_CONTENT_ACTIVITY = 109;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Context mContext = null;
    private MainActivity activity = null;

    private ContentPresenter mPresenter = null;
    private ContentDataSource mRepository = null;

    Unbinder unbinder = null;

    SteadyContentRecyclerViewAdapter adapter = null;

    @BindView(R.id.recyclerViewSteadyContent) RecyclerView recyclerViewSteadyContent;
    @BindView(R.id.linearLayoutContentDefault) LinearLayout linearLayoutContentDefault;
    @BindView(R.id.linearLayoutContentSteadyContent) LinearLayout linearLayoutContentSteadyContent;

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
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_content, container, false);
        // 버터나이프
        unbinder = ButterKnife.bind(this, rootView);
        // 초기화
        init();

        // DB에서 프로젝트 No 로 콘텐츠 정보 가져와서 adapter에 모두 넣어 화면 만들기
        mPresenter.getContents();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Glide 저장된 메모리 캐시 삭제
        Glide.get(activity).clearMemory();
        // Gilde 저장된 디스크 캐시 삭제
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(activity).clearDiskCache();
            }
        }).start();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        unbinder.unbind();
        mRepository = null;
        mPresenter.detachView();

        this.mContext = null;
        this.activity = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK ) {
            switch ( requestCode ) {
                case REQUEST_CODE_MODIFY_STEADY_CONTENT_ACTIVITY:
                    int modifyPosition = data.getIntExtra("modifyPosition", -1);
                    SteadyContent modifySteadyContent = data.getParcelableExtra("modifySteadyContent");

                    adapter.modifySteadyContent(modifyPosition, modifySteadyContent);

                    String modifyMessage = "오늘의 꾸준함이 수정되었습니다.";
                    showSnackBar(modifyMessage);

                    break;
                default:
                    break;
            }
        }
    }

    public void init() {
        // Presenter에 View 할당
        mPresenter = new ContentPresenter();
        mPresenter.attachView(this);
        // Presenter에 Model 할당
        mRepository = new ContentRepository(new ContentRemoteDataSource());
        mPresenter.setContentRepository(mRepository);

        // 리사이클러뷰 설정
        recyclerViewSteadyContent.setHasFixedSize(true);
        // 리사이클러뷰 어댑터 설정
        adapter = new SteadyContentRecyclerViewAdapter();
        recyclerViewSteadyContent.setAdapter(adapter);
        // 리사이클러뷰 레이아웃 매니저 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewSteadyContent.setLayoutManager(layoutManager);

        // SteadyContentAdapterContract의 View 할당
        mPresenter.setSteadyContentAdapterView(adapter);
        // SteadyContentAdapterContract의 Model 할당
        mPresenter.setSteadyContentAdapterModel(adapter);
        // SteadyContentAdapterContract의 OnItemClickListener 할당(SteadyContentAdapterContract의 View 할당한 이후에 호출해야함)
        mPresenter.setSteadyContentAdapterOnItemClickListener();
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showSteadyContentsLayout() {
        // 콘텐츠의 개수가 0 이면 디폴트 리니어레이아웃, 0 보다 크면 콘텐츠 리니어레이아웃
        if (adapter.getItemCount() > 0 ) {
            linearLayoutContentSteadyContent.setVisibility(View.VISIBLE);
            linearLayoutContentDefault.setVisibility(View.GONE);
        } else {
            linearLayoutContentDefault.setVisibility(View.VISIBLE);
            linearLayoutContentSteadyContent.setVisibility(View.GONE);
        }
    }

    @Override
    public Context getActivityContext() {
        return activity;
    }

    @Override
    public void refreshContentFragment() {
        mPresenter.getContents();
    }

    // 리사이클러뷰 어댑터
    public class SteadyContentRecyclerViewAdapter extends RecyclerView.Adapter<SteadyContentRecyclerViewAdapter.SteadyContentViewHolder>
                                                   implements SteadyContentAdapterContract.View, SteadyContentAdapterContract.Model {

        // SteadyContentAdapterContract 의 OnItemClickListener
        SteadyContentAdapterContract.OnSteadyContentClickListener onSteadyContentClickListener = null;

        // 아이템을 저장할 리스트
        ArrayList<SteadyContent> items = new ArrayList<>();

        @Override
        public SteadyContentRecyclerViewAdapter.SteadyContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_steady_content, parent, false);

            return new SteadyContentViewHolder(parent.getContext(), view, onSteadyContentClickListener);
        }

        @Override
        public void onBindViewHolder(SteadyContentRecyclerViewAdapter.SteadyContentViewHolder holder, int position) {
            // 뷰홀더에서 사용할 아이템을 가져온다.
            SteadyContent item = items.get(position);

            // 프로젝트 이미지 설정
            if ( item.getProjectImage() != null ) {
                Glide.with(ContentFragment.this)
                        .load(item.getProjectImage())
                        .override(72,72)
                        .error(R.drawable.icon_load_fail)
                        .into(holder.circleImageViewProjectImage);
            } else {
                Glide.with(ContentFragment.this)
                        .load(R.drawable.logo_black_star)
                        .into(holder.circleImageViewProjectImage);
            }
            // 프로젝트 타이틀 설정
            holder.textViewProjectTitle.setText(item.getProjectTitle());
            // 콘텐츠 현재 진행일수 설정
            holder.textViewCurrentDays.setText(String.valueOf(item.getCurrentDays()));
            // 콘텐츠 완료 일수 설정
            holder.textViewCompleteDays.setText(String.valueOf(item.getCompleteDays()));
            // 콘텐츠 등록 날짜 설정
            holder.textViewAccomplishDate.setText(holder.setAccomplishDateFormat(item.getAccomplishDate()));
            // 콘텐츠 이미지 설정
            if ( item.getContentImage() != null ) {
                holder.imageViewContentImage.setVisibility(View.VISIBLE);
                // 이미지뷰에 있는 drawable이 Glide에 영향을 주므로 초기화 진행
                holder.imageViewContentImage.setImageDrawable(null);
                // 이미지뷰 초기화 후 Glide 사용
                Glide.with(ContentFragment.this)
                        .load(item.getContentImage())
                        .error(R.drawable.icon_load_fail)
                        .into(holder.imageViewContentImage);
            } else {
                holder.imageViewContentImage.setVisibility(View.GONE);
            }
            // 콘텐츠 텍스트 설정
            holder.textViewContentText.setText(item.getContentText());

            // 콘텐츠 메뉴 Visibility 설정(오늘 등록한 콘텐츠만 수정/삭제 가능)
            if ( holder.checkSteadyProjectToday(item.getAccomplishDate()) ) {
                holder.imageViewContentMenu.setImageResource(R.drawable.icon_menu);
                holder.imageViewContentMenu.setTag(true);
            } else {
                holder.imageViewContentMenu.setImageResource(R.drawable.icon_menu_click_off);
                holder.imageViewContentMenu.setTag(false);
            }
            // 콘텐츠 현재 진행날짜 색깔 설정
            holder.setDurationColor(item.getCurrentDays(), item.getCompleteDays());
            // 콘텐츠 클릭 이벤트 설정
            holder.onBindItemClickListener(item, position);
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
        public void modifySteadyContent(int modifyPosition, SteadyContent modifySteadyContent) {
            items.set(modifyPosition, modifySteadyContent);

            // 해당 뷰홀더를 얻기
            SteadyContentViewHolder modifyViewHolder = (SteadyContentViewHolder)recyclerViewSteadyContent.findViewHolderForAdapterPosition(modifyPosition);
            // 콘텐츠 이미지 갱신
            if ( modifySteadyContent.getContentImage() != null ) {
                modifyViewHolder.imageViewContentImage.setVisibility(View.VISIBLE);
                // 이미지뷰에 있는 drawable이 Glide에 영향을 주므로 초기화 진행
                modifyViewHolder.imageViewContentImage.setImageDrawable(null);
                // 이미지뷰 초기화 후 Glide 사용
                Glide.with(activity)
                        .load(modifySteadyContent.getContentImage())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .error(R.drawable.icon_load_fail)
                        .into(modifyViewHolder.imageViewContentImage);
            } else {
                modifyViewHolder.imageViewContentImage.setVisibility(View.GONE);
            }
            // 콘텐츠 텍스트 갱신
            modifyViewHolder.textViewContentText.setText(modifySteadyContent.getContentText());

            recyclerViewSteadyContent.scrollToPosition(0);
        }

        @Override
        public void clearAdapter() {
            items.clear();
        }

        @Override
        public void setOnItemClickListener(SteadyContentAdapterContract.OnSteadyContentClickListener onSteadyContentClickListener) {
            if ( onSteadyContentClickListener != null ) {
                this.onSteadyContentClickListener = onSteadyContentClickListener;
            }
        }

        @Override
        public SteadyContent getSteadyContentItem(int position) {
            return items.get(position);
        }

        public class SteadyContentViewHolder extends RecyclerView.ViewHolder {

            Context context = null;
            SteadyContentAdapterContract.OnSteadyContentClickListener onSteadyContentClickListener = null;

            @BindView(R.id.circleImageViewProjectImage) CircleImageView circleImageViewProjectImage;
            @BindView(R.id.textViewProjectTitle) TextView textViewProjectTitle;
            @BindView(R.id.textViewOpenBracket) TextView textViewOpenBracket;
            @BindView(R.id.textViewCurrentDays) TextView textViewCurrentDays;
            @BindView(R.id.textViewPer) TextView textViewPer;
            @BindView(R.id.textViewCompleteDays) TextView textViewCompleteDays;
            @BindView(R.id.textViewCloseBracket) TextView textViewCloseBracket;
            @BindView(R.id.textViewAccomplishDate) TextView textViewAccomplishDate;
            @BindView(R.id.imageViewContentImage) ImageView imageViewContentImage;
            @BindView(R.id.textViewContentText) TextView textViewContentText;
            @BindView(R.id.imageViewContentMenu) ImageView imageViewContentMenu;

            @BindViews({ R.id.textViewOpenBracket, R.id.textViewCurrentDays, R.id.textViewPer, R.id.textViewCompleteDays, R.id.textViewCloseBracket })
            List<TextView> textViewDateStatusList;

            final ButterKnife.Setter<TextView, Integer> setDateStatusColor = new ButterKnife.Setter<TextView, Integer>() {
                @Override public void set(TextView textView, Integer colorValue, int index) {
                    textView.setTextColor(colorValue);
                }
            };

            public SteadyContentViewHolder(Context context, View itemView, SteadyContentAdapterContract.OnSteadyContentClickListener onSteadyContentClickListener) {
                super(itemView);
                // 버터나이프
                ButterKnife.bind(this, itemView);

                if ( context != null ) {
                    this.context = context;
                }

                if ( onSteadyContentClickListener != null ) {
                    this.onSteadyContentClickListener = onSteadyContentClickListener;
                }
            }

            @OnClick(R.id.imageViewContentMenu)
            public void onContentMenuClick() {
                if ( !(boolean)imageViewContentMenu.getTag() ) {
                    showSnackBar("오늘 등록한 콘텐츠만 수정 및 삭제 가능합니다.");

                    return;
                }

                // 팝업메뉴 생성(두번째 인자 : 팝업메뉴가 보여질 앵커)
                PopupMenu popupMenu = new PopupMenu(activity, imageViewContentMenu);
                // 팝업메뉴 인플레이션
                activity.getMenuInflater().inflate(R.menu.menu_content_fragment_popup, popupMenu.getMenu());
                // 팝업메뉴 클릭 이벤트 처리
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch ( item.getItemId() ) {
                            case R.id.popup_content_modify:
                                SteadyContent modifyContent = adapter.getSteadyContentItem(getAdapterPosition());

                                int status = 0;

                                if ( modifyContent.getCurrentDays() == modifyContent.getCompleteDays() ) {
                                    status = 1;
                                } else {
                                    status = 2;
                                }

                                SteadyProject steadyProject = new SteadyProject(modifyContent.getProjectNo(), modifyContent.getProjectTitle(), modifyContent.getProjectImage(),
                                                                                modifyContent.getCurrentDays(), modifyContent.getCompleteDays(), null, null, status, null, MainActivity.user.getNo());

                                Intent intent = new Intent(activity, ModifySteadyContentActivity.class);
                                intent.putExtra("modifyPosition", getAdapterPosition());
                                intent.putExtra("projectTitle", steadyProject.getProjectTitle());
                                intent.putExtra("modifyContent", modifyContent);
                                intent.putExtra("steadyProject", steadyProject);

                                startActivityForResult(intent, REQUEST_CODE_MODIFY_STEADY_CONTENT_ACTIVITY);

                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                // 팝업메뉴 보이기
                popupMenu.show();
            }

            public String setAccomplishDateFormat(String projectDate) {

                String formattedProjectDate = "0000.00.00";

                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(projectDate);
                    formattedProjectDate = new SimpleDateFormat("yyyy.MM.dd").format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return formattedProjectDate;
            }

            public boolean checkSteadyProjectToday(String accomplishDate) {
                Date accomplishContentDate = null;
                Date todayDate = null;

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String todayStr = dateFormat.format(new Date());

                try {
                    accomplishContentDate = dateFormat.parse(accomplishDate);
                    todayDate = dateFormat.parse(todayStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if ( accomplishContentDate.compareTo(todayDate) != 0 ) {
                    // 오늘 올린 콘텐츠가 아님
                    return false;
                } else if ( accomplishContentDate.compareTo(todayDate) == 0 ) {
                    // 오늘 올린 콘텐츠 임
                    return true;
                }

                return false;
            }

            public void setDurationColor(int currentDays, int completeDays) {
                int percent = ( currentDays * 100 ) / completeDays;

                if ( currentDays == 0 ) {
                    ButterKnife.apply(textViewDateStatusList, setDateStatusColor, getResources().getColor(R.color.colorBlack));
                } else if ( percent > 0 && percent <= 30 ) {
                    ButterKnife.apply(textViewDateStatusList, setDateStatusColor, getResources().getColor(R.color.colorYellow));
                } else if ( percent >= 31 && percent <=70 ) {
                    ButterKnife.apply(textViewDateStatusList, setDateStatusColor, getResources().getColor(R.color.colorBlueSky));
                } else if ( percent >= 71 && percent <= 100 ) {
                    ButterKnife.apply(textViewDateStatusList, setDateStatusColor, getResources().getColor(R.color.colorGreen));
                }
            }

            public void onBindItemClickListener(SteadyContent item, final int position) {
                // 여기서 사용하는 itemView 지역변수는 뷰홀더가 상속한 RecyclerView.ViewHolder의  itemView 이다.
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( onSteadyContentClickListener != null ) {
                            onSteadyContentClickListener.onSteadyContentClick(getAdapterPosition());
                        }
                    }
                });
            }
        }
    }
}
