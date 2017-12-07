package com.tyranotyrano.steadyhard.presenter;

import android.os.AsyncTask;

import com.tyranotyrano.steadyhard.contract.NewSteadyProjectContract;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.datasource.NewSteadyProjectDataSource;

import java.util.Map;

/**
 * Created by cyj on 2017-12-03.
 */

public class NewSteadyProjectPresenter implements NewSteadyProjectContract.Presenter {

    NewSteadyProjectContract.View mView = null;
    NewSteadyProjectDataSource mRepository = null;

    @Override
    public void attachView(NewSteadyProjectContract.View view) {
        if ( view != null ) {
            this.mView = view;
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void setNewSteadyProjectRepository(NewSteadyProjectDataSource newSteadyProjectDataSource) {
        if ( newSteadyProjectDataSource != null) {
            this.mRepository = newSteadyProjectDataSource;
        }
    }

    @Override
    public void selectPictureByGallery() {
        mView.selectPictureByGallery();
    }

    @Override
    public void uploadSteadyProjectImage(String projectImagePath) {
        new SteadyProjectImageUploadTask().execute(projectImagePath);
    }

    @Override
    public void deleteNewProjectImage(String deleteFileName) {
        new NewProjectImageDeleteTask().execute(deleteFileName);
    }

    @Override
    public void createNewProject(String projectTitle, String steadyProjectImagePath, String completeDaysStr, String description) {
        int completeDate = 0;
        String projectImageName = null;

        if ( projectTitle == null || projectTitle.length() < 1 ) {
            String message = "프로젝트 타이틀을 입력해주세요.";
            mView.showSnackBar(message);
            mView.setKeyboardDown();

            return;
        } else {
            projectImageName = projectTitle.replaceAll(" ", "_");
        }

        if ( steadyProjectImagePath != null ) {
            steadyProjectImagePath = steadyProjectImagePath.replace("NewProjectImage", projectTitle);
            steadyProjectImagePath = steadyProjectImagePath.replaceAll(" ", "_");
        }

        if ( completeDaysStr == null || completeDaysStr.length() < 1 ) {
            String message = "프로젝트 기간을 입력해주세요.";
            mView.showSnackBar(message);
            mView.setKeyboardDown();

            return;
        } else {
            completeDate = Integer.parseInt(completeDaysStr);
        }

        if ( completeDate < 3 || completeDate > 365 ) {
            String message = "프로젝트 기간은 3 ~ 365일 사이로 입력해주세요.";
            mView.showSnackBar(message);
            mView.setKeyboardDown();

            return;
        }

        if ( description == null || description.length() < 1 ) {
            String message = "프로젝트 설명을 입력해주세요.";
            mView.showSnackBar(message);
            mView.setKeyboardDown();

            return;
        }

        new NewProjectCreateTask().execute(projectTitle, steadyProjectImagePath, completeDate, description, projectImageName);
    }

    public class SteadyProjectImageUploadTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
        }

        @Override
        protected String doInBackground(String... params) {
            String imagePath = params[0];
            String steadyProjectImagePath = mRepository.uploadSteadyProjectImage(imagePath);

            return steadyProjectImagePath;
        }

        @Override
        protected void onPostExecute(String steadyProjectImagePath) {
            super.onPostExecute(steadyProjectImagePath);

            if ( steadyProjectImagePath != null ) {
                // 이미지 전송에 성공한 경우
                mView.setSteadyProjectImagePath(steadyProjectImagePath);
            } else {
                // 이미지 전송에 실패한 경우
                String message = "프로젝트 사진 설정에 실패하였습니다.\n 다른 사진을 선택해주세요.";
                mView.showSnackBar(message);
                // 디폴트 이미지로 전환
                mView.setDefaultSteadyProjectImage();
            }
        }
    }

    public class NewProjectImageDeleteTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String deleteFileName = params[0];
            boolean deleteResult = mRepository.deletedNewProjectImage(deleteFileName);

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

    public class NewProjectCreateTask extends AsyncTask<Object, Integer, Map<String, Object>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
        }

        @Override
        protected Map<String, Object> doInBackground(Object... params) {
            String projectTitle = null;
            String steadyProjectImagePath = null;
            int completeDate = 0;
            String description = null;
            String projectImageName = null;

            if ( params[0] instanceof String ) {
                projectTitle = (String)params[0];
            }

            if ( params[1] instanceof String ) {
                steadyProjectImagePath = (String)params[1];
            }

            if ( params[2] instanceof Integer ) {
                completeDate = (int)params[2];
            }

            if ( params[3] instanceof String ) {
                description = (String)params[3];
            }

            if ( params[4] instanceof String ) {
                projectImageName = (String)params[4];
            }

            Map<String, Object> map = mRepository.createNewSteadyProject(projectTitle, steadyProjectImagePath, completeDate, description, projectImageName);

            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);

            if ( map != null ) {
                if ( (boolean)map.get("result") ) {
                    SteadyProject newSteadyProject = (SteadyProject)map.get("newSteadyProject");

                    mView.completeNewSteadyProject(newSteadyProject);
                } else {
                    String message = "새 프로젝트 정보를 가져오는데 실패하였습니다.\n잠시후 다시 시도해주세요.";
                    mView.showSnackBar(message);
                }
            } else {
                String message = "새 프로젝트 생성에 실패하였습니다.\n잠시후 다시 시도해주세요.";
                mView.showSnackBar(message);
            }
        }
    }
}
