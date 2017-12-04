package com.tyranotyrano.steadyhard.model.remote.datasource;

import java.util.Map;

/**
 * Created by cyj on 2017-11-29.
 */

public interface ProfileDataSource {
    boolean clearSessionToken(String token);
    Map<String, Object> getSteadyProjectStatusCount();
}
