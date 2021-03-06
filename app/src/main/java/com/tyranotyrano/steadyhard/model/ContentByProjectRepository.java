package com.tyranotyrano.steadyhard.model;

import com.tyranotyrano.steadyhard.model.remote.datasource.ContentByProjectDataSource;

import java.util.Map;

/**
 * Created by cyj on 2017-12-09.
 */

public class ContentByProjectRepository implements ContentByProjectDataSource {
    private ContentByProjectDataSource mContentByProjectRemoteDataSource = null;

    public ContentByProjectRepository(ContentByProjectDataSource contentByProjectRemoteDataSource) {
        if ( contentByProjectRemoteDataSource != null ) {
            this.mContentByProjectRemoteDataSource = contentByProjectRemoteDataSource;
        }
    }

    @Override
    public Map<String, Object> getContentsByProjectNo(int projectNo) {
        Map<String, Object> map = mContentByProjectRemoteDataSource.getContentsByProjectNo(projectNo);

        return map;
    }

    @Override
    public boolean deleteSteadyContent(int deleteContentNo, String userEmail, String deleteContentImageName, String parentProjectPath, int currentDays, int projectNo) {
        boolean deleteResult = mContentByProjectRemoteDataSource.deleteSteadyContent(deleteContentNo, userEmail, deleteContentImageName, parentProjectPath, currentDays, projectNo);

        return deleteResult;
    }
}
