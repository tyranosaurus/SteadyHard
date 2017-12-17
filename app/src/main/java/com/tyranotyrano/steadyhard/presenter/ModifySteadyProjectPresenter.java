package com.tyranotyrano.steadyhard.presenter;

import android.app.Dialog;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tyranotyrano.steadyhard.R;
import com.tyranotyrano.steadyhard.contract.ModifySteadyProjectContract;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.datasource.ModifySteadyProjectDataSource;

import java.util.Map;

/**
 * Created by cyj on 2017-12-07.
 */

public class ModifySteadyProjectPresenter implements ModifySteadyProjectContract.Presenter {
    ModifySteadyProjectContract.View mView = null;
    ModifySteadyProjectDataSource mRepository = null;

    public void attachView(ModifySteadyProjectContract.View view) {
        if ( view != null ) {
            this.mView = view;
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void setModifySteadyProjectRepository(ModifySteadyProjectDataSource modifySteadyProjectDataSource) {
        if ( modifySteadyProjectDataSource != null ) {
            this.mRepository = modifySteadyProjectDataSource;
        }
    }

    @Override
    public void selectPictureByGallery() {
        mView.selectPictureByGallery();
    }

    @Override
    public void uploadModifyProjectImage(String modifyProjectImagePath) {
        new ModifyProjectImageUploadTask().execute(modifyProjectImagePath);
    }

    @Override
    public void deleteModifyProjectImage(String deleteFileName) {
        new ModifyProjectImageDeleteTask().execute(deleteFileName);
    }

    @Override
    public void modifySteadyProject(String projectTitle, String modifiedSteadyProjectImagePath, String description, String originalProjectImageName, int modifyProjectNo) {
        String modifyProjectImageName = null;

        if ( projectTitle == null || projectTitle.length() < 1 ) {
            String message = "프로젝트 타이틀을 입력해주세요.";
            mView.showSnackBar(message);
            mView.setKeyboardDown();

            return;
        } else {
            projectTitle = projectTitle.replace("\'", "");
            projectTitle = projectTitle.replace("\"", "");

            modifyProjectImageName = projectTitle.replaceAll(" ", "_") + "_" + modifyProjectNo;
        }

        if ( modifiedSteadyProjectImagePath != null ) {
            modifiedSteadyProjectImagePath = modifiedSteadyProjectImagePath.replace("ModifyProjectImage", modifyProjectImageName);
        }

        if ( description == null || description.length() < 1 ) {
            String message = "프로젝트 설명을 입력해주세요.";
            mView.showSnackBar(message);
            mView.setKeyboardDown();

            return;
        } else {
            description = description.replace("\'", "");
            description = description.replace("\"", "");
        }

        new SteadyProjectModifyTask().execute(projectTitle, modifiedSteadyProjectImagePath, description, modifyProjectImageName, originalProjectImageName, modifyProjectNo);
    }

    public class ModifyProjectImageUploadTask extends AsyncTask<String, Integer, String> {
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
            String modifyProjectImagePath = params[0];
            String modifiedProjectImagePath = mRepository.uploadModifyProjectImage(modifyProjectImagePath);

            return modifiedProjectImagePath;
        }

        @Override
        protected void onPostExecute(String modifiedProjectImagePath) {
            super.onPostExecute(modifiedProjectImagePath);
            progressDialog.dismiss();

            if ( modifiedProjectImagePath != null ) {
                // 프로젝트 이미지 수정 성공
                mView.setModifiedProjectImagePath(modifiedProjectImagePath);
            } else {
                // 프로젝트 이미지 수정 실패
                String message = "프로젝트 이미지 수정에 실패하였습니다. 잠시후 다시 시도해주세요.";
                mView.showSnackBar(message);
            }
        }
    }

    public class ModifyProjectImageDeleteTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String deleteFileName = params[0];
            boolean deleteResult = mRepository.deletedModifyProjectImage(deleteFileName);

            return deleteResult;
        }

        @Override
        protected void onPostExecute(Boolean deleteResult) {
            super.onPostExecute(deleteResult);

            // empty
            /*if ( deleteResult ) {
                // 정상적으로 삭제된 경우
            } else {
                // 삭제가 안된 경우
            }*/
        }
    }

    public class SteadyProjectModifyTask extends AsyncTask<Object, Integer, Map<String, Object>> {
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

            String projectTitle = null;
            String modifiedSteadyProjectImagePath = null;
            String description = null;
            String modifyProjectImageName = null;
            String originalProjectImageName = null;
            int modifyProjectNo = 0;

            if ( params[0] instanceof String ) {
                projectTitle = (String)params[0];
            }

            if ( params[1] instanceof String ) {
                modifiedSteadyProjectImagePath = (String)params[1];
            }

            if ( params[2] instanceof String ) {
                description = (String)params[2];
            }

            if ( params[3] instanceof String ) {
                modifyProjectImageName = (String)params[3];
            }

            if ( params[4] instanceof String ) {
                originalProjectImageName = (String)params[4];
            }

            if ( params[5] instanceof Integer ) {
                modifyProjectNo = (int)params[5];
            }

            Map<String, Object> map = mRepository.modifySteadyProject(projectTitle, modifiedSteadyProjectImagePath, description,
                                                                   modifyProjectImageName, originalProjectImageName, modifyProjectNo);

            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);
            progressDialog.dismiss();

            if ( map != null ) {
                // 프로젝트 수정 성공
                if ( (boolean)map.get("result") ) {
                    SteadyProject modifySteadyProject = (SteadyProject)map.get("modifySteadyProject");

                    mView.completeModifySteadyProject(modifySteadyProject);
                } else {
                    String message = "프로젝트 수정에 실패했습니다. 잠시후 다시 시도해주세요.";
                    mView.showSnackBar(message);
                }
            } else {
                // 프로젝트 수정 실패
                String message = "프로젝트 수정에 실패했습니다. 잠시후 다시 시도해주세요.";
                mView.showSnackBar(message);
            }
        }
    }
}
