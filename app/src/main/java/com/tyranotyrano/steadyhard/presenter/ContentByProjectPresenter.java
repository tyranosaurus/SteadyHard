package com.tyranotyrano.steadyhard.presenter;

import android.app.Dialog;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.ContentByProjectContract;
import com.tyranotyrano.steadyhard.contract.adapter.ContentByProjectAdapterContract;
import com.tyranotyrano.steadyhard.model.data.SteadyContent;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.datasource.ContentByProjectDataSource;
import com.tyranotyrano.steadyhard.view.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by cyj on 2017-12-08.
 */

public class ContentByProjectPresenter implements ContentByProjectContract.Presenter, ContentByProjectAdapterContract.OnContentByProjectClickListener {
    ContentByProjectContract.View mView = null;
    ContentByProjectDataSource mRepository = null;

    ContentByProjectAdapterContract.View mAdapterView = null;
    ContentByProjectAdapterContract.Model mAdapterModel = null;

    public void attachView(ContentByProjectContract.View view) {
        if ( view != null ) {
            this.mView = view;
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void setContentByProjectRepository(ContentByProjectDataSource contentByProjectDataSource) {
        if ( contentByProjectDataSource != null ) {
            this.mRepository = contentByProjectDataSource;
        }
    }

    @Override
    public void setContentByProjectAdapterView(ContentByProjectAdapterContract.View adapterView) {
        if ( adapterView != null ) {
            this.mAdapterView = adapterView;
        }
    }

    @Override
    public void setContentByProjectAdapterModel(ContentByProjectAdapterContract.Model adapterModel) {
        if ( adapterModel != null ) {
            this.mAdapterModel = adapterModel;
        }
    }

    @Override
    public void setContentByProjectAdapterOnItemClickListener() {
        mAdapterView.setOnContentByProjectClickListener(this);
    }

    @Override
    public void onContentByProjectClick(int position) {
        SteadyContent item = mAdapterModel.getSteadyContentItem(position);
    }

    @Override
    public void getContentByProject(int projectNo) {
        new ContentByProjectGetTask().execute(projectNo);
    }

    @Override
    public void deleteSteadyContent(int position, SteadyProject steadyProject) {
        int deletePosition = position;
        SteadyContent deleteItem = mAdapterModel.getSteadyContentItem(position);
        int deleteContentNo = deleteItem.getNo();
        String parentProjectPath = steadyProject.getProjectTitle().replaceAll(" ", "_") + "_" + steadyProject.getNo();
        int currentDays = steadyProject.getCurrentDays() - 1;
        int projectNo = steadyProject.getNo();

        String deleteContentImageName = null;

        if ( deleteItem.getContentImage() != null ) {
            deleteContentImageName = deleteItem.getContentImage().substring(deleteItem.getContentImage().lastIndexOf("/") + 1);
        }

        // 어댑터에서 데이터 삭제
        mAdapterModel.deleteSteadyContent(deleteItem);
        // 콘텐츠 삭제
        new SteadyContentDeleteTask().execute(deleteContentNo, MainActivity.user.getEmail(), deleteContentImageName,
                                              parentProjectPath, deletePosition, currentDays, projectNo, steadyProject);
    }

    public class ContentByProjectGetTask extends AsyncTask<Integer, Integer, Map<String, Object>> {
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
            int projectNo = params[0];

            Map<String, Object> map = mRepository.getContentsByProjectNo(projectNo);

            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);
            progressDialog.dismiss();

            if ( map != null ) {
                if ( (boolean)map.get("result") ) {
                    // 데이터 제대로 잘 가지고 온 경우
                    List<SteadyContent> steadyContentList = new ArrayList<>();

                    if ( map.get("steadyContentList") instanceof List) {
                        steadyContentList.addAll((List<SteadyContent>) map.get("steadyContentList"));
                    }

                    mAdapterModel.clearAdapter();

                    for ( SteadyContent item : steadyContentList ) {
                        mAdapterModel.addItem(item);
                    }

                    mAdapterView.notifyAdapter();

                    mView.showContentByProjectLayout();
                } else {
                    // 데이터 제대로 못 가지고 온 경우
                    String message = "콘텐츠를 가져오는데 실패했습니다. 잠시후 다시 시도해주세요.";
                    mView.showSnackBar(message);
                }
            } else {
                // 통신 실패
                String message = "인터넷 연결이 원활하지 않습니다. 잠시후 다시 시도해주세요.";
                mView.showSnackBar(message);
            }
        }
    }

    public class SteadyContentDeleteTask extends AsyncTask<Object, Integer, Boolean> {
        Dialog progressDialog;

        int deletePosition = 0;
        SteadyProject steadyProject = null;

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
            int deleteContentNo = 0;
            String userEmail = null;
            String deleteContentImageName = null;
            String parentProjectPath = null;
            int currentDays = 0;
            int projectNo = 0;

            if ( params[0] instanceof Integer ) {
                deleteContentNo = (int)params[0];
            }

            if ( params[1] instanceof String ) {
                userEmail = (String)params[1];
            }

            if ( params[2] instanceof String ) {
                deleteContentImageName = (String)params[2];
            }

            if ( params[3] instanceof String ) {
                parentProjectPath = (String)params[3];
            }

            if ( params[4] instanceof Integer ) {
                deletePosition = (int)params[4];
            }

            if ( params[5] instanceof Integer ) {
                currentDays = (int)params[5];
            }

            if ( params[6] instanceof Integer ) {
                projectNo = (int)params[6];
            }

            if ( params[7] instanceof SteadyProject ) {
                steadyProject = (SteadyProject)params[7];
            }

            boolean deleteResult = mRepository.deleteSteadyContent(deleteContentNo, userEmail, deleteContentImageName, parentProjectPath, currentDays, projectNo);

            return deleteResult;
        }

        @Override
        protected void onPostExecute(Boolean deleteResult) {
            super.onPostExecute(deleteResult);
            progressDialog.dismiss();

            if ( deleteResult ) {
                // 프로젝트 삭제 성공
                String message = "오늘의 꾸준함이 성공적으로 삭제되었습니다.";
                mView.showSnackBar(message);
                // 화면갱신
                mAdapterView.notifyAdapterDelete(deletePosition);

                int currentDays = steadyProject.getCurrentDays() - 1;
                String lastDate = null;
                int status = 2;

                Date today = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(today);
                calendar.add(Calendar.DATE, -1);

                today = calendar.getTime();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                lastDate = dateFormat.format(today);

                mView.refreshSteadyProjectAndToolbar(currentDays, lastDate, status);
            } else {
                // 프로젝트 삭제 실패
                String message = "오늘의 꾸준함을 삭제하는데 실패했습니다. 잠시후 다시 시도해주세요.";
                mView.showSnackBar(message);
            }
        }
    }
}
