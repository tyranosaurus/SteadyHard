package com.tyranotyrano.steadyhard.presenter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.HomeContract;
import com.tyranotyrano.steadyhard.contract.adapter.SteadyProjectAdapterContract;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.datasource.HomeDataSource;
import com.tyranotyrano.steadyhard.view.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cyj on 2017-11-24.
 */

public class HomePresenter implements HomeContract.Presenter, SteadyProjectAdapterContract.OnSteadyProjectClickListener {
    HomeContract.View mView = null;
    HomeDataSource mRepository = null;

    SteadyProjectAdapterContract.View mAdapterView = null;
    SteadyProjectAdapterContract.Model mAdapterModel = null;


    public void attachView(HomeContract.View view) {
        if ( view != null ) {
            this.mView = view;
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void setHomeRepository(HomeDataSource homeDataSource) {
        if ( homeDataSource != null ) {
            this.mRepository = homeDataSource;
        }
    }

    @Override
    public void setSteadyProjectAdapterView(SteadyProjectAdapterContract.View adapterView) {
        if ( adapterView != null ) {
            this.mAdapterView = adapterView;
        }
    }

    @Override
    public void setSteadyProjectAdapterModel(SteadyProjectAdapterContract.Model adapterModel) {
        if ( adapterModel != null) {
            this.mAdapterModel = adapterModel;
        }
    }

    @Override
    public void setSteadyProjectAdapterOnItemClickListener() {
        mAdapterView.setOnSteadyProjectClickListener(this);
    }

    @Override
    public void onSteadyProjectClick(int position) {
        SteadyProject item = mAdapterModel.getSteadyProjectItem(position);

        // 해당 프로젝트의 콘텐츠들 보여주는 액티비티
        mView.callContentByProjectActivity(item, position);
    }

    @Override
    public void getSteadyProjects() {
        new SteadyProjectsGetTask().execute(MainActivity.user.getNo());
    }

    @Override
    public AlertDialog.Builder buildDeleteAlertDialog(AlertDialog.Builder builder, int position) {
        final int deletePosition = position;
        final SteadyProject deleteItem = mAdapterModel.getSteadyProjectItem(position);
        final int deleteProjectNo = deleteItem.getNo();

        String imageName = null;

        if ( deleteItem.getProjectImage() != null ) {
            imageName = deleteItem.getProjectImage().substring(deleteItem.getProjectImage().lastIndexOf("/") + 1);
        }

        final String deleteProjectImageName = imageName;

        builder.setIcon(R.drawable.logo_black_star);
        builder.setTitle("SteadyHard");
        builder.setMessage("\n프로젝트를 삭제하시겠습니까?");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 어댑터에서 데이터 삭제
                        mAdapterModel.deleteSteadyProject(deleteItem);
                        // 프로젝트 삭제
                        new SteadyProjectDeleteTask().execute(deleteProjectNo, MainActivity.user.getEmail(), deleteProjectImageName, deletePosition);
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder;
    }

    @Override
    public void refreshSteadyProjects() {
        new SteadyProjectsRefreshTask().execute(MainActivity.user.getNo());
    }

    public class SteadyProjectsGetTask extends AsyncTask<Integer, Integer, Map<String, Object>> {
        Dialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
            progressDialog = new Dialog(mView.getActivityContext(), R.style.SemoDialog);
            progressDialog.setCancelable(true);

            ProgressBar progressbar = new ProgressBar(mView.getActivityContext());
            progressbar.setIndeterminateDrawable(mView.getActivityContext().getDrawable(R.drawable.progress_dialog));

            progressDialog.addContentView(progressbar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            progressDialog.show();
        }

        @Override
        protected Map<String, Object> doInBackground(Integer... params) {
            int userNo = params[0];

            Map<String, Object> map = mRepository.getSteadyProjectsByUserNo(userNo);

            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);
            progressDialog.dismiss();

            if ( map != null ) {
                if ( (boolean)map.get("result") ) {
                    // 데이터 제대로 잘 가지고 온 경우
                    List<SteadyProject> steadyProjectList = new ArrayList<>();

                    if ( map.get("steadyProjectList") instanceof List) {
                        steadyProjectList.addAll((List<SteadyProject>) map.get("steadyProjectList"));
                    }

                    mAdapterModel.clearAdapter();

                    for ( SteadyProject item : steadyProjectList ) {
                        mAdapterModel.addItem(item);
                    }

                    mAdapterView.notifyAdapter();

                    mView.showSteadyProjectsLayout();
                } else {
                    // 데이터 제대로 못 가지고 온 경우
                    String message = "프로젝트를 가져오는데 실패했습니다. 잠시후 다시 시도해주세요.";
                    mView.showSnackBar(message);
                }
            } else {
                // 통신 실패
                String message = "인터넷 연결이 원활하지 않습니다. 잠시후 다시 시도해주세요.";
                mView.showSnackBar(message);
            }
        }
    }

    public class SteadyProjectDeleteTask extends AsyncTask<Object, Integer, Boolean> {
        Dialog progressDialog;

        int deletePosition = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
            progressDialog = new Dialog(mView.getActivityContext(), R.style.SemoDialog);
            progressDialog.setCancelable(true);

            ProgressBar progressbar = new ProgressBar(mView.getActivityContext());
            progressbar.setIndeterminateDrawable(mView.getActivityContext().getDrawable(R.drawable.progress_dialog));

            progressDialog.addContentView(progressbar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            int deleteProjectNo = 0;
            String userEmail = null;
            String projectImageName = null;

            if ( params[0] instanceof Integer ) {
                deleteProjectNo = (int)params[0];
            }

            if ( params[1] instanceof String ) {
                userEmail = (String)params[1];
            }

            if ( params[2] instanceof String ) {
                projectImageName = (String)params[2];
            }

            if ( params[3] instanceof Integer ) {
                deletePosition = (int)params[3];
            }

            boolean deleteResult = mRepository.deleteSteadyProject(deleteProjectNo, userEmail, projectImageName);

            return deleteResult;
        }

        @Override
        protected void onPostExecute(Boolean deleteResult) {
            super.onPostExecute(deleteResult);
            progressDialog.dismiss();

            if ( deleteResult ) {
                // 프로젝트 삭제 성공
                String message = "프로젝트 삭제 성공";
                mView.showSnackBar(message);
                // 화면갱신
                mAdapterView.notifyAdapterDelete(deletePosition);
            } else {
                // 프로젝트 삭제 실패
                String message = "프로젝트 삭제에 실패했습니다. 잠시후 다시 시도해주세요.";
                mView.showSnackBar(message);
            }
        }
    }

    public class SteadyProjectsRefreshTask extends AsyncTask<Integer, Integer, Map<String, Object>> {
        Dialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
            progressDialog = new Dialog(mView.getActivityContext(), R.style.SemoDialog);
            progressDialog.setCancelable(true);

            ProgressBar progressbar = new ProgressBar(mView.getActivityContext());
            progressbar.setIndeterminateDrawable(mView.getActivityContext().getDrawable(R.drawable.progress_dialog));

            progressDialog.addContentView(progressbar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            progressDialog.show();
        }

        @Override
        protected Map<String, Object> doInBackground(Integer... params) {
            int userNo = params[0];

            Map<String, Object> map = mRepository.getSteadyProjectsByUserNo(userNo);

            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);
            progressDialog.dismiss();

            if ( map != null ) {
                if ( (boolean)map.get("result") ) {
                    // 데이터 제대로 잘 가지고 온 경우
                    List<SteadyProject> steadyProjectList = new ArrayList<>();

                    if ( map.get("steadyProjectList") instanceof List) {
                        steadyProjectList.addAll((List<SteadyProject>) map.get("steadyProjectList"));
                    }

                    mAdapterModel.clearAdapter();

                    for ( SteadyProject item : steadyProjectList ) {
                        mAdapterModel.addItem(item);
                    }

                    mAdapterView.notifyAdapter();

                    mView.showSteadyProjectsLayout();
                    mView.showSnackBar("새로고침 완료");
                    ((MainActivity)mView.getActivityContext()).completeRefreshing();
                } else {
                    // 데이터 제대로 못 가지고 온 경우
                    String message = "프로젝트 새로고침에 실패했습니다. 잠시후 다시 시도해주세요.";
                    mView.showSnackBar(message);
                }
            } else {
                // 통신 실패
                String message = "인터넷 연결이 원활하지 않습니다. 잠시후 다시 시도해주세요.";
                mView.showSnackBar(message);
            }
        }
    }
}
