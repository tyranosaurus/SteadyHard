package com.tyranotyrano.steadyhard.model;

import com.tyranotyrano.steadyhard.model.remote.datasource.HomeDataSource;

import java.util.Map;

/**
 * Created by cyj on 2017-12-06.
 */

public class HomeRepository implements HomeDataSource {
    private HomeDataSource mHomeRemoteDatasource = null;

    public HomeRepository(HomeDataSource homeRemoteDatasource) {
        if ( homeRemoteDatasource != null ) {
            this.mHomeRemoteDatasource = homeRemoteDatasource;
        }
    }

    @Override
    public Map<String, Object> getSteadyProjectsByUserNo(int userNo) {
        Map<String, Object> map = mHomeRemoteDatasource.getSteadyProjectsByUserNo(userNo);

        return map;
    }

    @Override
    public boolean deleteSteadyProject(int deleteProjectNo, String userEmail, String projectImageName) {
        boolean deleteResult = mHomeRemoteDatasource.deleteSteadyProject(deleteProjectNo, userEmail, projectImageName);

        return deleteResult;
    }
}
