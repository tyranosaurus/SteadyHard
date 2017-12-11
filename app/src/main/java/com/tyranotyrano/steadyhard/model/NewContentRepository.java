package com.tyranotyrano.steadyhard.model;

import com.tyranotyrano.steadyhard.model.remote.datasource.NewContentDataSource;

import java.util.Map;

/**
 * Created by cyj on 2017-12-10.
 */

public class NewContentRepository implements NewContentDataSource {
    private NewContentDataSource mNewContentRemoteDataSource = null;

    public NewContentRepository(NewContentDataSource newContentRemoteDataSource) {
        if ( newContentRemoteDataSource != null ) {
            this.mNewContentRemoteDataSource = newContentRemoteDataSource;
        }
    }

    @Override
    public String uploadNewContentImage(String imagePath, String parentProjectPath) {
        String newContentImagePath = mNewContentRemoteDataSource.uploadNewContentImage(imagePath, parentProjectPath);

        return newContentImagePath;
    }

    @Override
    public boolean deletedNewContentImage(String deleteFileName, String parentProjectPath) {
        boolean deleteResult = mNewContentRemoteDataSource.deletedNewContentImage(deleteFileName, parentProjectPath);

        return deleteResult;
    }

    @Override
    public Map<String, Object> createNewContent(String newContentImageURLPath, String contentText, String contentImageName, int currentDays, int completeDays, int projectNo) {
        Map<String, Object> map = mNewContentRemoteDataSource.createNewContent(newContentImageURLPath, contentText, contentImageName, currentDays, completeDays, projectNo);

        return map;
    }
}
