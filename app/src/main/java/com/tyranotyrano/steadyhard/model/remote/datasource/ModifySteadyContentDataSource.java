package com.tyranotyrano.steadyhard.model.remote.datasource;

import java.util.Map;

/**
 * Created by cyj on 2017-12-11.
 */

public interface ModifySteadyContentDataSource {
    String uploadModifyContentImage(String modifyContentImagePath, String parentProjectPath);
    boolean deletedModifyContentImage(String deleteFileName, String parentProjectPath);
    Map<String, Object> modifySteadyContent(String contentText, String modifiedSteadyContentImagePath, String parentProjectPath, int currentDays, int contentNo);
}
