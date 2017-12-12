package com.tyranotyrano.steadyhard.presenter;

import android.app.Dialog;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.NewContentContract;
import com.tyranotyrano.steadyhard.model.data.SteadyContent;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.datasource.NewContentDataSource;
import com.tyranotyrano.steadyhard.view.MainActivity;

import java.util.Map;

/**
 * Created by cyj on 2017-12-10.
 */

public class NewContentPresenter implements NewContentContract.Presenter {
    NewContentContract.View mView = null;
    NewContentDataSource mRepository = null;

    @Override
    public void attachView(NewContentContract.View view) {
        if ( view != null ) {
            this.mView = view;
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void setNewContentRepository(NewContentDataSource newContentDataSource) {
        if ( newContentDataSource != null ) {
            this.mRepository = newContentDataSource;
        }
    }

    @Override
    public void selectPictureByGallery() {
        mView.selectPictureByGallery();
    }

    @Override
    public void uploadNewContentImage(String contentImagePath, SteadyProject steadyProject) {
        String parentProjectPath = steadyProject.getProjectTitle().replaceAll(" ", "_") + "_" + steadyProject.getNo();

        new NewContentImageUploadTask().execute(contentImagePath, parentProjectPath);
    }

    @Override
    public void deleteNewContentImage(String deleteFileName, SteadyProject steadyProject) {
        String parentProjectPath = steadyProject.getProjectTitle().replaceAll(" ", "_") + "_" + steadyProject.getNo();

        new NewContentImageDeleteTask().execute(deleteFileName, parentProjectPath);
    }

    @Override
    public void createNewContent(String newContentImagePath, String contextText, SteadyProject steadyProject) {
        int currentDays = steadyProject.getCurrentDays() + 1; // 1 증가
        int completeDays = steadyProject.getCompleteDays();
        int projectNo = steadyProject.getNo();

        String contentImageName = steadyProject.getProjectTitle().replaceAll(" ", "_") + "_" + steadyProject.getNo();

        String newContentImageURLPath = null;

        if ( newContentImagePath != null ) {
            newContentImageURLPath = newContentImagePath.replace(MainActivity.user.getEmail() + "_NewContentImage", contentImageName + "_content_" + (steadyProject.getCurrentDays() + 1));
        }

        if ( contextText == null || contextText.length() < 1 ) {
            String message = "오늘의 꾸준함에 대해 적어주세요.";
            mView.showSnackBar(message);
            mView.setKeyboardDown();

            return;
        }

        new NewContentCreateTask().execute(newContentImageURLPath, contextText, contentImageName, currentDays, completeDays, projectNo);
    }

    public class NewContentImageUploadTask extends AsyncTask<String, Integer, String> {
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
        protected String doInBackground(String... params) {
            String imagePath = params[0];
            String parentProjectPath = params[1];

            String newContentImagePath = mRepository.uploadNewContentImage(imagePath, parentProjectPath);

            return newContentImagePath;
        }

        @Override
        protected void onPostExecute(String newContentImagePath) {
            super.onPostExecute(newContentImagePath);
            progressDialog.dismiss();

            if ( newContentImagePath != null ) {
                // 이미지 전송에 성공한 경우
                mView.setNewSteadyContentImagePath(newContentImagePath);
            } else {
                // 이미지 전송에 실패한 경우
                String message = "프로젝트 사진 설정에 실패하였습니다.\n 다른 사진을 선택해주세요.";
                mView.showSnackBar(message);
                // 디폴트 이미지로 전환
                mView.setDefaultSteadyContentImage();
            }
        }
    }

    public class NewContentImageDeleteTask extends AsyncTask<String, Integer, Boolean> {
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
        protected Boolean doInBackground(String... params) {
            String deleteFileName = params[0];
            String parentProjectPath = params[1];

            boolean deleteResult = mRepository.deletedNewContentImage(deleteFileName, parentProjectPath);

            return deleteResult;
        }

        @Override
        protected void onPostExecute(Boolean deleteResult) {
            super.onPostExecute(deleteResult);
            progressDialog.dismiss();

            // empty
            /*if ( deleteResult ) {
                // 정상적으로 삭제된 경우
            } else {
                // 삭제가 안된 경우
            }*/
        }
    }

    public class NewContentCreateTask extends AsyncTask<Object, Integer, Map<String, Object>> {
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
        protected Map<String, Object> doInBackground(Object... params) {
            String newContentImageURLPath = null;
            String contextText = null;
            String contentImageName = null;
            int currentDays = 0;
            int completeDays = 0;
            int projectNo = 0;

            if ( params[0] instanceof String ) {
                newContentImageURLPath = (String)params[0];
            }

            if ( params[1] instanceof String ) {
                contextText = (String)params[1];
            }

            if ( params[2] instanceof String ) {
                contentImageName = (String)params[2];
            }

            if ( params[3] instanceof Integer ) {
                currentDays = (int)params[3];
            }

            if ( params[4] instanceof Integer ) {
                completeDays = (int)params[4];
            }

            if ( params[5] instanceof Integer ) {
                projectNo = (int)params[5];
            }

            Map<String, Object> map = mRepository.createNewContent(newContentImageURLPath, contextText, contentImageName, currentDays, completeDays, projectNo);

            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);
            progressDialog.dismiss();

            if ( map != null ) {
                if ( (boolean)map.get("result") ) {
                    SteadyContent newSteadyContent = (SteadyContent)map.get("newSteadyContent");

                    mView.completeNewSteadyContent(newSteadyContent);
                } else {
                    String message = "오늘의 꾸준함을 가져오는데 실패하였습니다.\n잠시후 다시 시도해주세요.";
                    mView.showSnackBar(message);
                }
            } else {
                String message = "오늘의 꾸준함을 등록하는데 실패하였습니다.\n잠시후 다시 시도해주세요.";
                mView.showSnackBar(message);
            }
        }
    }
}
