package com.tyranotyrano.steadyhard.presenter;

import android.os.AsyncTask;

import com.tyranotyrano.steadyhard.contract.ModifySteadyContentContract;
import com.tyranotyrano.steadyhard.model.data.SteadyContent;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.datasource.ModifySteadyContentDataSource;
import com.tyranotyrano.steadyhard.view.MainActivity;

import java.util.Map;

/**
 * Created by cyj on 2017-12-11.
 */

public class ModifySteadyContentPresenter implements ModifySteadyContentContract.Presenter {
    ModifySteadyContentContract.View mView = null;
    ModifySteadyContentDataSource mRepository = null;

    public void attachView(ModifySteadyContentContract.View view) {
        if ( view != null ) {
            this.mView = view;
        }
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void setModifySteadyContentRepository(ModifySteadyContentDataSource modifySteadyContentDataSource) {
        if ( modifySteadyContentDataSource != null ) {
            this.mRepository = modifySteadyContentDataSource;
        }
    }

    @Override
    public void selectPictureByGallery() {
        mView.selectPictureByGallery();
    }

    @Override
    public void uploadModifyContentImage(String modifyContentImagePath, SteadyProject steadyProject) {
        String parentProjectPath = steadyProject.getProjectTitle().replaceAll(" ", "_") + "_" + steadyProject.getNo();

        new ModifyContentImageUploadTask().execute(modifyContentImagePath, parentProjectPath);
    }

    @Override
    public void deleteModifyContentImage(String deleteFileName, SteadyProject steadyProject) {
        String parentProjectPath = steadyProject.getProjectTitle().replaceAll(" ", "_") + "_" + steadyProject.getNo();

        new ModifyContentImageDeleteTask().execute(deleteFileName, parentProjectPath);
    }

    @Override
    public void modifySteadyContent(String contentText, String modifiedSteadyContentImagePath, SteadyContent mModifyContent, SteadyProject steadyProject) {
        String parentProjectPath = steadyProject.getProjectTitle().replaceAll(" ", "_") + "_" + steadyProject.getNo();
        int currentDays = mModifyContent.getCurrentDays();
        int contentNo = mModifyContent.getNo();

        if ( contentText == null || contentText.length() < 1 ) {
            String message = "오늘의 꾸준함에 대해 적어주세요.";
            mView.showSnackBar(message);
            mView.setKeyboardDown();

            return;
        }

        if ( modifiedSteadyContentImagePath != null ) {
            modifiedSteadyContentImagePath = modifiedSteadyContentImagePath.replace(MainActivity.user.getEmail() + "_ModifyContentImage",
                                                                                    parentProjectPath + "_content_" + mModifyContent.getCurrentDays())
                                                                           .replaceAll(" ", "_");
        }

        new SteadyContentModifyTask().execute(contentText, modifiedSteadyContentImagePath, parentProjectPath, currentDays, contentNo);
    }

    public class ModifyContentImageUploadTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
        }

        @Override
        protected String doInBackground(String... params) {
            String modifyContentImagePath = params[0];
            String parentProjectPath = params[1];

            String modifyContentImageURLPath = mRepository.uploadModifyContentImage(modifyContentImagePath, parentProjectPath);

            return modifyContentImageURLPath;
        }

        @Override
        protected void onPostExecute(String modifyContentImageURLPath) {
            super.onPostExecute(modifyContentImageURLPath);

            if ( modifyContentImageURLPath != null ) {
                // 프로젝트 이미지 수정 성공
                mView.setModifiedContentImagePath(modifyContentImageURLPath);
            } else {
                // 프로젝트 이미지 수정 실패
                String message = "오늘의 꾸준함 이미지 수정에 실패하였습니다. 잠시후 다시 시도해주세요.";
                mView.showSnackBar(message);
            }
        }
    }

    public class ModifyContentImageDeleteTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String deleteFileName = params[0];
            String parentProjectPath = params[1];

            boolean deleteResult = mRepository.deletedModifyContentImage(deleteFileName, parentProjectPath);

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

    public class SteadyContentModifyTask extends AsyncTask<Object, Integer, Map<String, Object>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바 다이얼로그 띄우는 용도로 사용
        }

        @Override
        protected Map<String, Object> doInBackground(Object... params) {
            String contentText = null;
            String modifiedSteadyContentImagePath = null;
            String parentProjectPath = null;
            int currentDays = 0;
            int contentNo = 0;

            if ( params[0] instanceof String ) {
                contentText = (String)params[0];
            }

            if ( params[1] instanceof String ) {
                modifiedSteadyContentImagePath = (String)params[1];
            }

            if ( params[2] instanceof String ) {
                parentProjectPath = (String)params[2];
            }

            if ( params[3] instanceof Integer ) {
                currentDays = (int)params[3];
            }

            if ( params[4] instanceof Integer ) {
                contentNo = (int)params[4];
            }

            Map<String, Object> map = mRepository.modifySteadyContent(contentText, modifiedSteadyContentImagePath, parentProjectPath, currentDays, contentNo);

            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);

            if ( map != null ) {
                // 프로젝트 수정 성공
                if ( (boolean)map.get("result") ) {
                    SteadyContent modifySteadyContent = (SteadyContent)map.get("modifySteadyContent");

                    mView.completeModifySteadyContent(modifySteadyContent);
                } else {
                    String message = "오늘의 꾸준함을 수정하는데 실패했습니다. 잠시후 다시 시도해주세요.";
                    mView.showSnackBar(message);
                }
            } else {
                // 프로젝트 수정 실패
                String message = "오늘의 꾸준함을 수정하는데 실패했습니다. 잠시후 다시 시도해주세요.";
                mView.showSnackBar(message);
            }
        }
    }
}
