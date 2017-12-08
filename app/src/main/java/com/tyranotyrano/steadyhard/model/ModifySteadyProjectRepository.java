package com.tyranotyrano.steadyhard.model;

import com.tyranotyrano.steadyhard.model.remote.datasource.ModifySteadyProjectDataSource;

import java.util.Map;

/**
 * Created by cyj on 2017-12-07.
 */

public class ModifySteadyProjectRepository implements ModifySteadyProjectDataSource {
    private ModifySteadyProjectDataSource mModifySteadyProjectRemoteDataSource = null;

    public ModifySteadyProjectRepository(ModifySteadyProjectDataSource modifySteadyProjectRemoteDataSource) {
        if ( modifySteadyProjectRemoteDataSource != null ) {
            this.mModifySteadyProjectRemoteDataSource = modifySteadyProjectRemoteDataSource;
        }
    }

    @Override
    public String uploadModifyProjectImage(String modifyProjectImagePath) {
        String modifiedProjectImagePath = mModifySteadyProjectRemoteDataSource.uploadModifyProjectImage(modifyProjectImagePath);

        return modifiedProjectImagePath;
    }

    @Override
    public boolean deletedModifyProjectImage(String deleteFileName) {
        boolean deleteResult = mModifySteadyProjectRemoteDataSource.deletedModifyProjectImage(deleteFileName);

        return deleteResult;
    }

    @Override
    public Map<String, Object> modifySteadyProject(String projectTitle, String modifiedSteadyProjectImagePath, String description,
                                                   String modifyProjectImageName, String originalProjectImageName, int modifyProjectNo) {
        Map<String, Object> map = mModifySteadyProjectRemoteDataSource.modifySteadyProject(projectTitle, modifiedSteadyProjectImagePath, description,
                                                                                        modifyProjectImageName, originalProjectImageName, modifyProjectNo);

        return map;
    }
}
