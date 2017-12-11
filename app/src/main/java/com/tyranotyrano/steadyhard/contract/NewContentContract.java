package com.tyranotyrano.steadyhard.contract;

import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;
import com.tyranotyrano.steadyhard.model.data.SteadyContent;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.datasource.NewContentDataSource;

/**
 * Created by cyj on 2017-12-10.
 */

public interface NewContentContract {
    // NewContentActivity 관련 View 처리
    interface View extends BaseView {
        void showSnackBar(String message);
        void selectPictureByGallery();
        void setDefaultSteadyContentImage();
        void setNewSteadyContentImagePath(String newContentImagePath);
        void setKeyboardDown();
        void completeNewSteadyContent(SteadyContent newSteadyContent);
    }

    // NewContentActivity 관련 Presenter 처리
    interface Presenter extends BasePresenter<NewContentContract.View> {
        // model 관련 처리
        void setNewContentRepository(NewContentDataSource newContentDataSource);

        void selectPictureByGallery();
        void uploadNewContentImage(String contentImagePath, SteadyProject steadyProject);
        void deleteNewContentImage(String deleteFileName, SteadyProject steadyProject);
        void createNewContent(String newContentImagePath, String contextText, SteadyProject steadyProject);
    }
}
