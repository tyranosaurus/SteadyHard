package com.tyranotyrano.steadyhard.model.remote.datasource;

import java.util.Map;

/**
 * Created by cyj on 2017-12-12.
 */

public interface ContentDataSource {
    Map<String, Object> getSteadyContentsByUserNo(int userNo);
}
