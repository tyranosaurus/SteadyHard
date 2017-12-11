package com.tyranotyrano.steadyhard.model;

import com.tyranotyrano.steadyhard.model.remote.datasource.ModifySteadyContentDataSource;

import java.util.Map;

/**
 * Created by cyj on 2017-12-11.
 */

public class ModifySteadyContentRepository implements ModifySteadyContentDataSource {
    private ModifySteadyContentDataSource mModifySteadyContentRemoteDataSource = null;

    public ModifySteadyContentRepository(ModifySteadyContentDataSource modifySteadyContentRemoteDataSource) {
        if ( modifySteadyContentRemoteDataSource != null ) {
            this.mModifySteadyContentRemoteDataSource = modifySteadyContentRemoteDataSource;
        }
    }

    @Override
    public String uploadModifyContentImage(String modifyContentImagePath, String parentProjectPath) {
        String modifyContentImageURLPath = mModifySteadyContentRemoteDataSource.uploadModifyContentImage(modifyContentImagePath, parentProjectPath);

        return modifyContentImageURLPath;
    }

    @Override
    public boolean deletedModifyContentImage(String deleteFileName, String parentProjectPath) {
        boolean deleteResult = mModifySteadyContentRemoteDataSource.deletedModifyContentImage(deleteFileName, parentProjectPath);

        return deleteResult;
    }

    @Override
    public Map<String, Object> modifySteadyContent(String contentText, String modifiedSteadyContentImagePath, String parentProjectPath, int currentDays, int contentNo) {
        Map<String, Object> map = mModifySteadyContentRemoteDataSource.modifySteadyContent(contentText, modifiedSteadyContentImagePath, parentProjectPath, currentDays, contentNo);

        return map;
    }
}
