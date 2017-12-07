package com.tyranotyrano.steadyhard.contract;

import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.datasource.NewSteadyProjectDataSource;

/**
 * Created by cyj on 2017-12-03.
 */

public interface NewSteadyProjectContract {
    // NewSteadyProjectActivity 관련 View 처리
    interface View extends BaseView {
        void selectPictureByGallery();
        void showSnackBar(String message);
        void setSteadyProjectImagePath(String steadyProjectImagePath);
        void setDefaultSteadyProjectImage();
        void setKeyboardDown();
        void completeNewSteadyProject(SteadyProject newSteadyProject);
    }

    // NewSteadyProjectActivity 관련 Presenter 처리
    interface Presenter extends BasePresenter<NewSteadyProjectContract.View> {
        // model 관련 처리

        void selectPictureByGallery();
        void setNewSteadyProjectRepository(NewSteadyProjectDataSource newSteadyProjectDataSource);
        void uploadSteadyProjectImage(String projectImagePath);
        void deleteNewProjectImage(String deleteFileName);
        void createNewProject(String projectTitle, String steadyProjectImagePath, String completeDays, String description);
    }
}
