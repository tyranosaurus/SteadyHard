package com.tyranotyrano.steadyhard.model;

import com.tyranotyrano.steadyhard.model.remote.datasource.NewSteadyProjectDataSource;

import java.util.Map;

/**
 * Created by cyj on 2017-12-03.
 */

public class NewSteadyProjectRepository implements NewSteadyProjectDataSource {
    private NewSteadyProjectDataSource mNewSteadyProjectRemoteDataSource = null;

    public NewSteadyProjectRepository(NewSteadyProjectDataSource newSteadyProjectRemoteDataSource) {
        if ( newSteadyProjectRemoteDataSource != null ) {
            this.mNewSteadyProjectRemoteDataSource = newSteadyProjectRemoteDataSource;
        }
    }

    @Override
    public String uploadSteadyProjectImage(String imagePath) {
        String steadyProjectImagePath = mNewSteadyProjectRemoteDataSource.uploadSteadyProjectImage(imagePath);

        return steadyProjectImagePath;
    }

    @Override
    public boolean deletedNewProjectImage(String deleteFileName) {
        boolean deleteResult = mNewSteadyProjectRemoteDataSource.deletedNewProjectImage(deleteFileName);

        return deleteResult;
    }

    @Override
    public Map<String, Object> createNewSteadyProject(String projectTitle, String steadyProjectImagePath, int completeDate, String description, String projectImageName) {
        Map<String, Object> map = mNewSteadyProjectRemoteDataSource.createNewSteadyProject(projectTitle, steadyProjectImagePath, completeDate, description, projectImageName);

        return map;
    }
}
