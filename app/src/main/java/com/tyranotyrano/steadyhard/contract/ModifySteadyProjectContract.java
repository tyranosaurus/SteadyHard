package com.tyranotyrano.steadyhard.contract;

import android.content.Context;

import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.datasource.ModifySteadyProjectDataSource;

/**
 * Created by cyj on 2017-12-07.
 */

public interface ModifySteadyProjectContract {
    // LoginActivity 관련 View 처리
    interface View extends BaseView {
        void showSnackBar(String message);
        void selectPictureByGallery();
        void setModifiedProjectImagePath(String modifiedSteadyProjectImagePath);
        void setKeyboardDown();
        void completeModifySteadyProject(SteadyProject modifySteadyProject);
        Context getActivityContext();
    }

    // LoginActivity 관련 Presenter 처리
    interface Presenter extends BasePresenter<ModifySteadyProjectContract.View> {
        // model 관련 처리
        void setModifySteadyProjectRepository(ModifySteadyProjectDataSource modifySteadyProjectDataSource);

        void selectPictureByGallery();
        void uploadModifyProjectImage(String modifyProjectImagePath);
        void deleteModifyProjectImage(String deleteFileName);
        void modifySteadyProject(String projectTitle, String modifiedSteadyProjectImagePath, String description, String originalProjectImageName, int modifyProjectNo);
    }
}
