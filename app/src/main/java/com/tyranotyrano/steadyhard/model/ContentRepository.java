package com.tyranotyrano.steadyhard.model;

import com.tyranotyrano.steadyhard.model.remote.datasource.ContentDataSource;

import java.util.Map;

/**
 * Created by cyj on 2017-12-12.
 */

public class ContentRepository implements ContentDataSource {
    private ContentDataSource mContentRemoteDataSource = null;

    public ContentRepository(ContentDataSource contentRemoteDataSource) {
        if ( contentRemoteDataSource != null ) {
            this.mContentRemoteDataSource = contentRemoteDataSource;
        }
    }

    @Override
    public Map<String, Object> getSteadyContentsByUserNo(int userNo) {
        Map<String, Object> map = mContentRemoteDataSource.getSteadyContentsByUserNo(userNo);

        return map;
    }
}
