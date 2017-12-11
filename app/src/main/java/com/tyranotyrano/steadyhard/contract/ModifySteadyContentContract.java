package com.tyranotyrano.steadyhard.contract;

import com.tyranotyrano.steadyhard.contract.base.BasePresenter;
import com.tyranotyrano.steadyhard.contract.base.BaseView;
import com.tyranotyrano.steadyhard.model.data.SteadyContent;
import com.tyranotyrano.steadyhard.model.data.SteadyProject;
import com.tyranotyrano.steadyhard.model.remote.datasource.ModifySteadyContentDataSource;

/**
 * Created by cyj on 2017-12-11.
 */

public interface ModifySteadyContentContract {
    // ModifySteadyContentActivity 관련 View 처리
    interface View extends BaseView {
        void showSnackBar(String message);
        void setKeyboardDown();
        void selectPictureByGallery();
        void setModifiedContentImagePath(String modifyContentImageURLPath);
        void completeModifySteadyContent(SteadyContent modifySteadyContent);
    }

    // ModifySteadyContentActivity 관련 Presenter 처리
    interface Presenter extends BasePresenter<ModifySteadyContentContract.View> {
        // model 관련 처리
        void setModifySteadyContentRepository(ModifySteadyContentDataSource modifySteadyContentDataSource);

        void selectPictureByGallery();
        void uploadModifyContentImage(String modifyContentImagePath, SteadyProject steadyProject);
        void deleteModifyContentImage(String deleteFileName, SteadyProject steadyProject);
        void modifySteadyContent(String contentText, String modifiedSteadyContentImagePath, SteadyContent mModifyContent, SteadyProject steadyProject);
    }
}
