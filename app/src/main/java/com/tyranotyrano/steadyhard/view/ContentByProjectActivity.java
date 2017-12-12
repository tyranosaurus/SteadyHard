package com.tyranotyrano.steadyhard.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.tyranotyrano.steadyhard.contract.ContentByProjectContract;
import com.tyranotyrano.steadyhard.contract.adapter.ContentByProjectAdapterContract;
import com.tyranotyrano.steadyhard.model.ContentByProjectRepository;
import com.tyranotyrano.steadyhard.model.data.SteadyContent;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.ContentByProjectRemoteDataSource;
import com.tyranotyrano.steadyhard.model.remote.datasource.ContentByProjectDataSource;
import com.tyranotyrano.steadyhard.presenter.ContentByProjectPresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContentByProjectActivity extends AppCompatActivity implements ContentByProjectContract.View {

    private final static int REQUEST_CODE_NEW_CONTENT_ACTIVITY = 108;
    private final static int REQUEST_CODE_MODIFY_STEADY_CONTENT_ACTIVITY = 109;

    ContentByProjectContract.Presenter mPresenter = null;
    ContentByProjectDataSource mRepository = null;

    int adapterPosition = 0;
    boolean isBackClick = false;
    SteadyProject steadyProject = null;
    ContentByProjectRecyclerViewAdapter adapter = null;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.imageViewContentByProjectBack) ImageView imageViewContentByProjectBack;
    @BindView(R.id.textViewProjectTitle) TextView textViewProjectTitle;
    @BindView(R.id.textViewOpenBracket) TextView textViewOpenBracket;
    @BindView(R.id.textViewCurrentDays) TextView textViewCurrentDays;
    @BindView(R.id.textViewPer) TextView textViewPer;
    @BindView(R.id.textViewCompleteDays) TextView textViewCompleteDays;
    @BindView(R.id.textViewCloseBracket) TextView textViewCloseBracket;
    @BindView(R.id.textViewProjectDate) TextView textViewProjectDate;
    @BindView(R.id.textViewProjectStatus) TextView textViewProjectStatus;
    @BindView(R.id.imageViewProjectImage) ImageView imageViewProjectImage;
    @BindView(R.id.textViewProjectDescription) TextView textViewProjectDescription;
    @BindView(R.id.linearLayoutContentByProjectDefault) LinearLayout linearLayoutContentByProjectDefault;
    @BindView(R.id.recyclerViewContentByProject) RecyclerView recyclerViewContentByProject;
    @BindView(R.id.floatingActionButton) FloatingActionButton floatingActionButton;

    @BindViews({ R.id.textViewOpenBracket, R.id.textViewCurrentDays, R.id.textViewPer, R.id.textViewCompleteDays, R.id.textViewCloseBracket })
    List<TextView> textViewDateStatusList;

    final ButterKnife.Setter<TextView, Integer> setDateStatusGone = new ButterKnife.Setter<TextView, Integer>() {
        @Override public void set(TextView textView, Integer gone, int index) {
            textView.setVisibility(gone);
        }
    };

    final ButterKnife.Setter<TextView, Integer> setDateStatusColor = new ButterKnife.Setter<TextView, Integer>() {
        @Override public void set(TextView textView, Integer colorValue, int index) {
            textView.setTextColor(colorValue);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_by_project);
        // ButterKnife 세팅
        ButterKnife.bind(this);
        // 초기화
        init();

        // DB에서 프로젝트 No 로 콘텐츠 정보 가져와서 adapter에 모두 넣어 화면 만들기
        mPresenter.getContentByProject(steadyProject.getNo());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Repository 해제
        mRepository = null;
        // Presenter 해제
        mPresenter.detachView();

        // Glide 저장된 메모리 캐시 삭제
        Glide.get(ContentByProjectActivity.this).clearMemory();
        // Gilde 저장된 디스크 캐시 삭제
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(ContentByProjectActivity.this).clearDiskCache();
            }
        }).start();
    }

    @Override
    public void finish() {
        // 액티비티 종료하면서 프로젝트 갱신
        Intent intent = new Intent();
        // Parcelable 객체
        intent.putExtra("steadyProject", steadyProject);
        intent.putExtra("adapterPosition", adapterPosition);
        setResult(RESULT_OK, intent);

        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK ) {
            switch ( requestCode ) {
                case REQUEST_CODE_NEW_CONTENT_ACTIVITY:
                    SteadyContent newSteadyContent = data.getParcelableExtra("newSteadyContent");

                    addNewContent(newSteadyContent);

                    // 프로젝트 정보 갱신
                    steadyProject.setCurrentDays(newSteadyContent.getCurrentDays());
                    steadyProject.setLast_date(newSteadyContent.getAccomplishDate());

                    if ( newSteadyContent.getCurrentDays() == steadyProject.getCompleteDays() ) {
                        steadyProject.setStatus(1);
                    }

                    // 툴바 상태 갱신
                    textViewCurrentDays.setText(String.valueOf(steadyProject.getCurrentDays()));
                    setDurationColor(steadyProject.getCurrentDays(), newSteadyContent.getCompleteDays());

                    if ( steadyProject.getStatus() == 1 ) {
                        textViewProjectStatus.setVisibility(View.VISIBLE);
                        textViewProjectStatus.setText("[ Success ]");
                        textViewProjectStatus.setTextColor(getResources().getColor(R.color.colorGreen));
                        ButterKnife.apply(textViewDateStatusList, setDateStatusGone, View.GONE);
                    }

                    String message = "오늘의 꾸준함이 등록되었습니다.";
                    showSnackBar(message);
                    break;
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
        mPresenter = new ContentByProjectPresenter();
        mPresenter.attachView(this);
        // Presenter에 Model 할당
        mRepository = new ContentByProjectRepository(new ContentByProjectRemoteDataSource());
        mPresenter.setContentByProjectRepository(mRepository);

        // 리사이클러뷰 설정
        recyclerViewContentByProject.setHasFixedSize(true);
        // 리사이클러뷰 어댑터 설정
        adapter = new ContentByProjectRecyclerViewAdapter();
        recyclerViewContentByProject.setAdapter(adapter);
        // 리사이클러뷰 레이아웃 매니저 설정
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ContentByProjectActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewContentByProject.setLayoutManager(linearLayoutManager);

        // SteadyProjectAdapterContract의 View 할당
        mPresenter.setContentByProjectAdapterView(adapter);
        // SteadyProjectAdapterContract의 Model 할당
        mPresenter.setContentByProjectAdapterModel(adapter);
        // SteadyProjectAdapterContract의 OnItemClickListener 할당(리사이클러뷰 adapter 생성한 이후에 호출해야함)
        mPresenter.setContentByProjectAdapterOnItemClickListener();


        // 인텐트 데이터 할당
        Intent intent = getIntent();
        steadyProject = intent.getParcelableExtra("steadyProject");
        adapterPosition = intent.getIntExtra("adapterPosition", 0);

        // 툴바 세팅
        setSupportActionBar(toolbar);

        // 프로젝트 정보 세팅
        setProjectData();
    }

    public void setProjectData() {

        textViewProjectTitle.setText(steadyProject.getProjectTitle());
        textViewCurrentDays.setText(String.valueOf(steadyProject.getCurrentDays()));
        textViewCompleteDays.setText(String.valueOf(steadyProject.getCompleteDays()));
        textViewProjectDate.setText(setProjectDateFormat(steadyProject.getProjectDate()));

        if ( steadyProject.getStatus() == 1 ) {
            textViewProjectStatus.setVisibility(View.VISIBLE);
            textViewProjectStatus.setText("[ Success ]");
            textViewProjectStatus.setTextColor(getResources().getColor(R.color.colorGreen));

            setDurationGone();
        } else if ( steadyProject.getStatus() == 3 ) {
            textViewProjectStatus.setVisibility(View.VISIBLE);
            textViewProjectStatus.setText("[ Fail ]");
            textViewProjectStatus.setTextColor(getResources().getColor(R.color.colorRed));

            setDurationGone();
        }

        setDurationColor(steadyProject.getCurrentDays(), steadyProject.getCompleteDays());

        if ( steadyProject.getProjectImage() != null ) {
            Glide.with(ContentByProjectActivity.this)
                    .load(steadyProject.getProjectImage())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(R.drawable.icon_load_fail)
                    .into(imageViewProjectImage);
        } else {
            imageViewProjectImage.setVisibility(View.GONE);
        }

        textViewProjectDescription.setText(steadyProject.getDescription());
    }

    public String setProjectDateFormat(String dateString) {
        String formattedProjectDate = "0000.00.00";

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            formattedProjectDate = new SimpleDateFormat("yyyy.MM.dd").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedProjectDate;
    }

    public void setDurationGone() {
        ButterKnife.apply(textViewDateStatusList, setDateStatusGone, View.GONE);
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

    public void addNewContent(SteadyContent newSteadyContent) {
        adapter.addNewItem(newSteadyContent);
        adapter.notifyItemInserted(0);

        showContentByProjectLayout();

        recyclerViewContentByProject.scrollToPosition(0);
    }

    public boolean isCreatedContentToday(String lastContentDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = dateFormat.format(new Date());

        if ( lastContentDate.equals(todayString) ) {
            return true;
        }

        return false;
    }

    @OnClick(R.id.imageViewContentByProjectBack)
    public void onContentByProjectBack() {
        finish();
    }

    @OnClick(R.id.floatingActionButton)
    public void onClickFloatingActionButtion() {
        if ( steadyProject.getStatus() == 1 ) {
            String message = "[ " + steadyProject.getProjectTitle() + " ] 프로젝트의 꾸준함을 모두 달성하셨습니다.";
            showSnackBar(message);

            return;
        } else if ( steadyProject.getStatus() == 3 ) {
            String message = "[ " + steadyProject.getProjectTitle() + " ] 프로젝트의 꾸준함이 이어지지 못했습니다.\n새로운 프로젝트로 다시 도전해 주세요.";
            showSnackBar(message);

            return;
        }

        if ( isCreatedContentToday(steadyProject.getLast_date()) ) {
            String message = "이미 오늘의 꾸준함을 등록하셨습니다.";
            showSnackBar(message);

            return;
        }

        // 새 콘텐츠 만드는 액티비티 호출
        Intent intent = new Intent(ContentByProjectActivity.this, NewContentActivity.class);
        intent.putExtra("steadyProject", steadyProject);
        startActivityForResult(intent, REQUEST_CODE_NEW_CONTENT_ACTIVITY);
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showContentByProjectLayout() {
        if ( adapter.getItemCount() < 1 ) {
            linearLayoutContentByProjectDefault.setVisibility(View.VISIBLE);
            recyclerViewContentByProject.setVisibility(View.GONE);
        } else {
            linearLayoutContentByProjectDefault.setVisibility(View.GONE);
            recyclerViewContentByProject.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void refreshSteadyProjectAndToolbar(int currentDays, String lastDate, int status) {
        steadyProject.setCurrentDays(currentDays);
        steadyProject.setLast_date(lastDate);
        steadyProject.setStatus(status);

        // 툴바 상태 갱신
        textViewCurrentDays.setText(String.valueOf(currentDays));
        setDurationColor(currentDays, steadyProject.getCompleteDays());

        if ( steadyProject.getStatus() == 2 ) {
            textViewProjectStatus.setVisibility(View.GONE);
            ButterKnife.apply(textViewDateStatusList, setDateStatusGone, View.VISIBLE);
        }
    }

    @Override
    public Context getActivityContext() {
        return ContentByProjectActivity.this;
    }

    // 리사이클러뷰 어댑터
    public class ContentByProjectRecyclerViewAdapter extends RecyclerView.Adapter<ContentByProjectRecyclerViewAdapter.ContentByProjectViewHolder>
                                                     implements ContentByProjectAdapterContract.View, ContentByProjectAdapterContract.Model {

        // SteadyProjectAdapterContract 의 OnItemClickListener
        ContentByProjectAdapterContract.OnContentByProjectClickListener onContentByProjectClickListener = null;

        // 아이템을 저장할 리스트
        List<SteadyContent> items = new ArrayList<>();

        @Override
        public ContentByProjectRecyclerViewAdapter.ContentByProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content_by_project, parent, false);

            return new ContentByProjectViewHolder(parent.getContext(), view, onContentByProjectClickListener);
        }

        @Override
        public void onBindViewHolder(ContentByProjectRecyclerViewAdapter.ContentByProjectViewHolder holder, int position) {
            // 뷰홀더에서 사용할 아이템을 가져온다.
            SteadyContent item = items.get(position);

            // 콘텐츠 등록 날짜
            holder.textViewContentDate.setText(holder.setContentDateFormat(item.getAccomplishDate()));
            // 콘텐츠 현재 진행일수 설정
            holder.textViewCurrentDays.setText(String.valueOf(item.getCurrentDays()));
            // 콘텐츠 완료 일수 설정
            holder.textViewCompleteDays.setText(String.valueOf(item.getCompleteDays()));
            // 콘텐츠 이미지 설정
            if ( item.getContentImage() != null ) {
                holder.imageViewContentImage.setVisibility(View.VISIBLE);
                // 이미지뷰에 있는 drawable이 Glide에 영향을 주므로 초기화 진행
                holder.imageViewContentImage.setImageDrawable(null);
                // 이미지뷰 초기화 후 Glide 사용
                Glide.with(ContentByProjectActivity.this)
                        .load(item.getContentImage())
                        .error(R.drawable.icon_load_fail)
                        .into(holder.imageViewContentImage);
            } else {
                holder.imageViewContentImage.setVisibility(View.GONE);
            }
            // 콘텐츠 내용 설정
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
            // 데이터 갱신
            adapter.notifyDataSetChanged();
        }

        @Override
        public void setOnContentByProjectClickListener(ContentByProjectAdapterContract.OnContentByProjectClickListener onContentByProjectClickListener) {
            if ( onContentByProjectClickListener != null ) {
                this.onContentByProjectClickListener = onContentByProjectClickListener;
            }
        }

        @Override
        public void addItem(SteadyContent item) {
            items.add(item);
        }

        @Override
        public void clearAdapter() {
            items.clear();
        }

        @Override
        public SteadyContent getSteadyContentItem(int position) {
            return items.get(position);
        }

        @Override
        public void modifySteadyContent(int modifyPosition, SteadyContent modifySteadyContent) {
            items.set(modifyPosition, modifySteadyContent);

            // 해당 뷰홀더를 얻기
            ContentByProjectViewHolder modifyViewHolder = (ContentByProjectViewHolder)recyclerViewContentByProject.findViewHolderForAdapterPosition(modifyPosition);
            // 콘텐츠 이미지 갱신
            if ( modifySteadyContent.getContentImage() != null ) {
                modifyViewHolder.imageViewContentImage.setVisibility(View.VISIBLE);
                // 이미지뷰에 있는 drawable이 Glide에 영향을 주므로 초기화 진행
                modifyViewHolder.imageViewContentImage.setImageDrawable(null);
                // 이미지뷰 초기화 후 Glide 사용
                Glide.with(ContentByProjectActivity.this)
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

            recyclerViewContentByProject.scrollToPosition(0);
        }

        public void addNewItem(SteadyContent item) {
            items.add(0, item);
        }

        @Override
        public void deleteSteadyContent(SteadyContent deleteItem) {
            items.remove(deleteItem);
        }

        @Override
        public void notifyAdapterDelete(int deletePosition) {
            // 삭제 후 데이터 갱신(애니메이션)
            adapter.notifyItemRemoved(deletePosition);

            showContentByProjectLayout();
        }

        public class ContentByProjectViewHolder extends RecyclerView.ViewHolder {

            Context context = null;
            ContentByProjectAdapterContract.OnContentByProjectClickListener onContentByProjectClickListener = null;

            @BindView(R.id.textViewContentDate) TextView textViewContentDate;
            @BindView(R.id.textViewOpenBracket) TextView textViewOpenBracket;
            @BindView(R.id.textViewCurrentDays) TextView textViewCurrentDays;
            @BindView(R.id.textViewPer) TextView textViewPer;
            @BindView(R.id.textViewCompleteDays) TextView textViewCompleteDays;
            @BindView(R.id.textViewCloseBracket) TextView textViewCloseBracket;
            @BindView(R.id.imageViewContentMenu) ImageView imageViewContentMenu;
            @BindView(R.id.imageViewContentImage) ImageView imageViewContentImage;
            @BindView(R.id.textViewContentText) TextView textViewContentText;

            @BindViews({ R.id.textViewOpenBracket, R.id.textViewCurrentDays, R.id.textViewPer, R.id.textViewCompleteDays, R.id.textViewCloseBracket })
            List<TextView> textViewDateStatusList;

            final ButterKnife.Setter<TextView, Integer> setDateStatusGone = new ButterKnife.Setter<TextView, Integer>() {
                @Override public void set(TextView textView, Integer gone, int index) {
                    textView.setVisibility(gone);
                }
            };

            final ButterKnife.Setter<TextView, Integer> setDateStatusColor = new ButterKnife.Setter<TextView, Integer>() {
                @Override public void set(TextView textView, Integer colorValue, int index) {
                    textView.setTextColor(colorValue);
                }
            };

            public ContentByProjectViewHolder(Context context, View itemView,
                                              ContentByProjectAdapterContract.OnContentByProjectClickListener onContentByProjectClickListener) {
                super(itemView);
                // 버터나이프
                ButterKnife.bind(this, itemView);

                if ( context != null ) {
                    this.context = context;
                }

                if ( onContentByProjectClickListener != null ) {
                    this.onContentByProjectClickListener = onContentByProjectClickListener;
                }
            }

            @OnClick(R.id.imageViewContentMenu)
            public void onContentMenuClick() {
                if ( !(boolean)imageViewContentMenu.getTag() ) {
                    showSnackBar("오늘 등록한 콘텐츠만 수정 및 삭제 가능합니다.");

                    return;
                }

                // 팝업메뉴 생성(두번째 인자 : 팝업메뉴가 보여질 앵커)
                PopupMenu popupMenu = new PopupMenu(ContentByProjectActivity.this, imageViewContentMenu);
                // 팝업메뉴 인플레이션
                getMenuInflater().inflate(R.menu.menu_content_popup, popupMenu.getMenu());
                // 팝업메뉴 클릭 이벤트 처리
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch ( item.getItemId() ) {
                            case R.id.popup_content_modify:
                                SteadyContent modifyContent = adapter.getSteadyContentItem(getAdapterPosition());

                                Intent intent = new Intent(ContentByProjectActivity.this, ModifySteadyContentActivity.class);
                                intent.putExtra("modifyPosition", getAdapterPosition());
                                intent.putExtra("projectTitle", steadyProject.getProjectTitle());
                                intent.putExtra("modifyContent", modifyContent);
                                intent.putExtra("steadyProject", steadyProject);

                                startActivityForResult(intent, REQUEST_CODE_MODIFY_STEADY_CONTENT_ACTIVITY);

                                break;
                            case R.id.popup_content_delete:
                                mPresenter.deleteSteadyContent(getAdapterPosition(), steadyProject);
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

            public String setContentDateFormat(String contenttDate) {

                String formattedContentDate = "0000.00.00";

                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(contenttDate);
                    formattedContentDate = new SimpleDateFormat("yyyy.MM.dd").format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return formattedContentDate;
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
                        if ( onContentByProjectClickListener != null ) {
                            onContentByProjectClickListener.onContentByProjectClick(position);
                        }
                    }
                });
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
        }
    }
}
