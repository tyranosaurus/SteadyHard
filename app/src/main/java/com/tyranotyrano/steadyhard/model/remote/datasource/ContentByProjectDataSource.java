package com.tyranotyrano.steadyhard.model.remote.datasource;

import java.util.Map;

/**
 * Created by cyj on 2017-12-09.
 */

public interface ContentByProjectDataSource {
    Map<String, Object> getContentsByProjectNo(int projectNo);
}
