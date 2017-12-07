package com.tyranotyrano.steadyhard.model.remote.datasource;

import java.util.Map;

/**
 * Created by cyj on 2017-12-06.
 */

public interface HomeDataSource {
    Map<String, Object> getSteadyProjectsByUserNo(int userNo);
    boolean deleteSteadyProject(int deleteProjectNo, String userEmail, String projectImageName);
}
