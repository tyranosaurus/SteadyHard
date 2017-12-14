package com.tyranotyrano.steadyhard.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.tyranotyrano.steadyhard.contract.HomeContract;
import com.tyranotyrano.steadyhard.contract.adapter.SteadyProjectAdapterContract;
import com.tyranotyrano.steadyhard.model.HomeRepository;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.HomeRemoteDatasource;
import com.tyranotyrano.steadyhard.model.remote.datasource.HomeDataSource;
import com.tyranotyrano.steadyhard.presenter.HomePresenter;
import com.tyranotyrano.steadyhard.view.ContentByProjectActivity;
import com.tyranotyrano.steadyhard.view.MainActivity;
import com.tyranotyrano.steadyhard.view.ModifySteadyProjectActivity;

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

public class HomeFragment extends Fragment implements HomeContract.View {
    private static final int REQUEST_CODE_MODIFY_STEADY_PROJECT_ACTIVITY = 106;
    private static final int REQUEST_CODE_CONTENT_BY_PROJECT_ACTIVITY = 107;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Context mContext = null;
    private MainActivity activity = null;

    private HomeContract.Presenter mPresenter = null;
    private HomeDataSource mRepository = null;

    Unbinder unbinder = null;

    SteadyProjectRecyclerViewAdapter adapter = null;

    @BindView(R.id.recyclerViewSteadyProject) RecyclerView recyclerViewSteadyProject;
    @BindView(R.id.linearLayoutHomeDefault) LinearLayout linearLayoutHomeDefault;
    @BindView(R.id.linearLayoutHomeSteadyProject) LinearLayout linearLayoutHomeSteadyProject;

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
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);
        // 버터나이프
        unbinder = ButterKnife.bind(this, rootView);
        // 초기화
        init();

        // DB에서 프로젝트 정보 가져와서 adapter에 모두 넣어 화면 만들기
        mPresenter.getSteadyProjects();

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
                case REQUEST_CODE_MODIFY_STEADY_PROJECT_ACTIVITY:
                    int modifyPosition = data.getIntExtra("modifyPosition", -1);
                    SteadyProject modifySteadyProject = data.getParcelableExtra("modifySteadyProject");

                    adapter.modifySteadyProject(modifyPosition, modifySteadyProject);

                    String message = "프로젝트가 수정되었습니다.";
                    showSnackBar(message);
                    break;
                case REQUEST_CODE_CONTENT_BY_PROJECT_ACTIVITY:
                    // 프로젝트의 오늘 콘텐츠 등록했을때 화면 갱신해주는 처리
                    int position = data.getIntExtra("adapterPosition", -1);
                    SteadyProject refreshSteadyProject = data.getParcelableExtra("steadyProject");

                    adapter.refreshSteadyProject(position, refreshSteadyProject);
                default:
                    break;
            }
        }
    }

    public void init() {
        // Presenter에 View 할당
        mPresenter = new HomePresenter();
        mPresenter.attachView(this);
        // Presenter에 Model 할당
        mRepository = new HomeRepository(new HomeRemoteDatasource());
        mPresenter.setHomeRepository(mRepository);

        // 리사이클러뷰 설정
        recyclerViewSteadyProject.setHasFixedSize(true);
        // 리사이클러뷰 어댑터 설정
        adapter = new SteadyProjectRecyclerViewAdapter();
        recyclerViewSteadyProject.setAdapter(adapter);
        // 리사이클러뷰 레이아웃 매니저 설정
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewSteadyProject.setLayoutManager(linearLayoutManager);

        // SteadyProjectAdapterContract의 View 할당
        mPresenter.setSteadyProjectAdapterView(adapter);
        // SteadyProjectAdapterContract의 Model 할당
        mPresenter.setSteadyProjectAdapterModel(adapter);
        // SteadyProjectAdapterContract의 OnItemClickListener 할당(리사이클러뷰 adapter 생성한 이후에 호출해야함)
        mPresenter.setSteadyProjectAdapterOnItemClickListener();
    }

    public void addNewProject(SteadyProject newSteadyProject) {

        adapter.addNewItem(newSteadyProject);
        adapter.notifyItemInserted(0);

        showSteadyProjectsLayout();

        recyclerViewSteadyProject.scrollToPosition(0);
    }

    @Override
    public void showSnackBar(String message) {
        // getView() : 프래그먼트의 root view 가져옴
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showSteadyProjectsLayout() {
        // 프로젝트의 개수가 0 이면 홈 디폴트 리니어레이아웃, 0 보다 크면 홈 프로젝트 리니어레이아웃
        if (adapter.getItemCount() > 0 ) {
            linearLayoutHomeSteadyProject.setVisibility(View.VISIBLE);
            linearLayoutHomeDefault.setVisibility(View.GONE);
        } else {
            linearLayoutHomeDefault.setVisibility(View.VISIBLE);
            linearLayoutHomeSteadyProject.setVisibility(View.GONE);
        }
    }

    @Override
    public void callContentByProjectActivity(SteadyProject steadyProject, int position) {
        Intent intent = new Intent(activity, ContentByProjectActivity.class);
        intent.putExtra("steadyProject", steadyProject);
        intent.putExtra("adapterPosition", position);

        startActivityForResult(intent, REQUEST_CODE_CONTENT_BY_PROJECT_ACTIVITY);
    }

    @Override
    public Context getActivityContext() {
        return activity;
    }

    @Override
    public void refreshHomeFragment() {
        // 홈프래그먼트의 모든 SteadyProject 갱신
        mPresenter.refreshSteadyProjects();
    }

    // 리사이클러뷰 어댑터
    public class SteadyProjectRecyclerViewAdapter extends RecyclerView.Adapter<SteadyProjectRecyclerViewAdapter.SteadyProjectViewHolder>
                                                  implements SteadyProjectAdapterContract.View, SteadyProjectAdapterContract.Model {

        // SteadyProjectAdapterContract 의 OnItemClickListener
        SteadyProjectAdapterContract.OnSteadyProjectClickListener onSteadyProjectClickListener = null;

        // 아이템을 저장할 리스트
        List<SteadyProject> items = new ArrayList<>();

        // 뷰홀더를 생성
        @Override
        public SteadyProjectRecyclerViewAdapter.SteadyProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // 아이템이 배치될 레이아웃을 부모 뷰그룹에 인플레이션한다 : 여기서 부모 뷰그룹은 리사이클러뷰가 된다.
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_steady_project, parent, false);

            return new SteadyProjectViewHolder(parent.getContext(), view, onSteadyProjectClickListener);
        }

        // 뷰홀더를 데이터와 바인딩 해준다. (뷰홀더 재사용)
        // 절대 onBindViewHolder() 메소드 안에서 클릭 등 이벤트 처리 코드 구현하지 말 것!
        @Override
        public void onBindViewHolder(SteadyProjectRecyclerViewAdapter.SteadyProjectViewHolder holder, int position) {
            // 뷰홀더에서 사용할 아이템을 가져온다.
            SteadyProject item = items.get(position);

            // 프로젝트 오늘 등록여부 설정
            if ( item.getStatus() != 2 ) {
                holder.viewTodayCheck.setVisibility(View.INVISIBLE);
            } else {
                holder.viewTodayCheck.setVisibility(View.VISIBLE);
            }

            if ( holder.checkSteadyProjectToday(item.getLast_date()) ) {
                holder.viewTodayCheck.setBackgroundResource(R.drawable.circle_today_check_green);
            } else {
                holder.viewTodayCheck.setBackgroundResource(R.drawable.circle_today_check_red);
            }
            // 프로젝트 이미지 설정
            if ( item.getProjectImage() != null ) {
                Glide.with(HomeFragment.this)
                        .load(item.getProjectImage())
                        .override(72,72)
                        .error(R.drawable.icon_load_fail)
                        .into(holder.circleImageViewProjectImage);
            } else {
                Glide.with(HomeFragment.this)
                        .load(R.drawable.logo_black_star)
                        .into(holder.circleImageViewProjectImage);
            }
            // 프로젝트 타이틀 설정
            holder.textViewProjectTitle.setText(item.getProjectTitle());
            // 프로젝트 현재 진행일수 설정
            holder.textViewCurrentDays.setText(String.valueOf(item.getCurrentDays()));
            // 프로젝트 완료 일수 설정
            holder.textViewCompleteDays.setText(String.valueOf(item.getCompleteDays()));
            // 프로젝트 등록 날짜 설정
            holder.textViewProjectDate.setText(holder.setProjectDateFormat(item.getProjectDate()));
            // 프로젝트 Status 설정
            if ( item.getStatus() == 1 ) {
                holder.textViewProjectStatus.setVisibility(View.VISIBLE);
                holder.textViewProjectStatus.setText("[ Success ]");
                holder.textViewProjectStatus.setTextColor(getResources().getColor(R.color.colorGreen));

                holder.setDurationGone();
            } else if ( item.getStatus() == 3 ) {
                holder.textViewProjectStatus.setVisibility(View.VISIBLE);
                holder.textViewProjectStatus.setText("[ Fail ]");
                holder.textViewProjectStatus.setTextColor(getResources().getColor(R.color.colorRed));

                holder.setDurationGone();
            } else {
                holder.textViewProjectStatus.setVisibility(View.GONE);
                ButterKnife.apply(holder.textViewDateStatusList, holder.setDateStatusGone, View.VISIBLE);
            }
            // 프로젝트 현재 진행날짜 색깔 설정
            holder.setDurationColor(item.getCurrentDays(), item.getCompleteDays());
            // 프로젝트 클릭 이벤트 설정
            holder.onBindItemClickListener(item, position);
        }

        @Override
        public int getItemCount() {
            // 어댑터가 가지고있는 아이템의 총 개수를 반환한다.
            return items.size();
        }

        @Override
        public void notifyAdapter() {
            // 데이터 갱신
            adapter.notifyDataSetChanged();
        }

        @Override
        public void notifyAdapterDelete(int deletePosition) {
            // 삭제 후 데이터 갱신(애니메이션)
            adapter.notifyItemRemoved(deletePosition);

            showSteadyProjectsLayout();
        }

        @Override
        public void addItem(SteadyProject item) {
            items.add(item);
        }

        @Override
        public void addNewItem(SteadyProject item) {
            items.add(0, item);
        }

        @Override
        public void clearAdapter() {
            items.clear();
        }

        @Override
        public void setOnSteadyProjectClickListener(SteadyProjectAdapterContract.OnSteadyProjectClickListener onSteadyProjectClickListener) {
            if ( onSteadyProjectClickListener != null ) {
                this.onSteadyProjectClickListener = onSteadyProjectClickListener;
            }
        }

        @Override
        public SteadyProject getSteadyProjectItem(int position) {
            return items.get(position);
        }

        @Override
        public void deleteSteadyProject(SteadyProject deleteItem) {
            items.remove(deleteItem);
        }

        @Override
        public void modifySteadyProject(int modifyPosition, SteadyProject modifySteadyProject) {
            items.set(modifyPosition, modifySteadyProject);

            // 해당 뷰홀더를 얻기
            SteadyProjectViewHolder modifyViewHolder = (SteadyProjectViewHolder)recyclerViewSteadyProject.findViewHolderForAdapterPosition(modifyPosition);
            // 프로젝트 이미지 갱신
            if ( modifySteadyProject.getProjectImage() != null ) {

                Glide.with(HomeFragment.this)
                        .load(modifySteadyProject.getProjectImage())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .override(72,72)
                        .error(R.drawable.icon_load_fail)
                        .into(modifyViewHolder.circleImageViewProjectImage);
            } else {
                Glide.with(HomeFragment.this)
                        .load(R.drawable.logo_black_star)
                        .into(modifyViewHolder.circleImageViewProjectImage);
            }
            // 프로젝트 타이틀 갱신
            modifyViewHolder.textViewProjectTitle.setText(modifySteadyProject.getProjectTitle());
        }

        @Override
        public void refreshSteadyProject(int refreshPosition, SteadyProject refreshSteadyProject) {
            items.set(refreshPosition, refreshSteadyProject);

            // 해당 뷰홀더를 얻기
            SteadyProjectViewHolder modifyViewHolder = (SteadyProjectViewHolder)recyclerViewSteadyProject.findViewHolderForAdapterPosition(refreshPosition);
            adapter.onBindViewHolder(modifyViewHolder, refreshPosition);
        }

        public class SteadyProjectViewHolder extends RecyclerView.ViewHolder {

            Context context = null;
            SteadyProjectAdapterContract.OnSteadyProjectClickListener onSteadyProjectClickListener = null;

            @BindView((R.id.viewTodayCheck)) View viewTodayCheck;
            @BindView(R.id.circleImageViewProjectImage) CircleImageView circleImageViewProjectImage;
            @BindView(R.id.textViewProjectTitle) TextView textViewProjectTitle;
            @BindView(R.id.textViewOpenBracket) TextView textViewOpenBracket;
            @BindView(R.id.textViewCurrentDays) TextView textViewCurrentDays;
            @BindView(R.id.textViewPer) TextView textViewPer;
            @BindView(R.id.textViewCompleteDays) TextView textViewCompleteDays;
            @BindView(R.id.textViewCloseBracket) TextView textViewCloseBracket;
            @BindView(R.id.textViewProjectDate) TextView textViewProjectDate;
            @BindView(R.id.textViewProjectStatus) TextView textViewProjectStatus;
            @BindView(R.id.imageViewProjectMenu) ImageView imageViewProjectMenu;

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

            public SteadyProjectViewHolder(Context context, View itemView, SteadyProjectAdapterContract.OnSteadyProjectClickListener onSteadyProjectClickListener) {
                super(itemView);
                // 버터나이프
                ButterKnife.bind(this, itemView);

                if ( context != null ) {
                    this.context = context;
                }

                if ( onSteadyProjectClickListener != null ) {
                    this.onSteadyProjectClickListener = onSteadyProjectClickListener;
                }
            }

            @OnClick(R.id.imageViewProjectMenu)
            public void onProjectMenuClick() {
                // 팝업메뉴 생성(두번째 인자 : 팝업메뉴가 보여질 앵커)
                PopupMenu popupMenu = new PopupMenu(activity, imageViewProjectMenu);
                // 팝업메뉴 인플레이션
                activity.getMenuInflater().inflate(R.menu.menu_project_popup, popupMenu.getMenu());
                // 팝업메뉴 클릭 이벤트 처리
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch ( item.getItemId() ) {
                            case R.id.popup_project_modify:
                                SteadyProject modifyItem = adapter.getSteadyProjectItem(getAdapterPosition());

                                Intent intent = new Intent(activity, ModifySteadyProjectActivity.class);
                                intent.putExtra("modifyPosition", getAdapterPosition());
                                intent.putExtra("modifyItem", modifyItem);

                                startActivityForResult(intent, REQUEST_CODE_MODIFY_STEADY_PROJECT_ACTIVITY);

                                break;
                            case R.id.popup_project_delete:
                                // 프로젝트 삭제
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                                builder = mPresenter.buildDeleteAlertDialog(builder, getAdapterPosition());
                                builder.show();

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

            public boolean checkSteadyProjectToday(String lastDate) {
                Date lastContentDate = null;
                Date todayDate = null;

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String todayStr = dateFormat.format(new Date());

                try {
                    lastContentDate = dateFormat.parse(lastDate);
                    todayDate = dateFormat.parse(todayStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if ( lastContentDate.compareTo(todayDate) == -1 ) {
                    return false;
                } else if ( lastContentDate.compareTo(todayDate) == 0 ) {
                    return true;
                }

                return false;
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
                // 여기서 사용하는 itemView 지역변수는 뷰홀더가 상속한 RecyclerView.ViewHolder의  itemView 이다.
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( onSteadyProjectClickListener != null ) {
                            onSteadyProjectClickListener.onSteadyProjectClick(getAdapterPosition());
                        }
                    }
                });
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

            public void setDurationGone() {
                ButterKnife.apply(textViewDateStatusList, setDateStatusGone, View.GONE);
            }
        }
    }
}
