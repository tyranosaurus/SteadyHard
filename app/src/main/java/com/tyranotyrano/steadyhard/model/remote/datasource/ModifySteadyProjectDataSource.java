package com.tyranotyrano.steadyhard.model.remote.datasource;

import java.util.Map;

/**
 * Created by cyj on 2017-12-07.
 */

public interface ModifySteadyProjectDataSource {
    String uploadModifyProjectImage(String modifyProjectImagePath);
    boolean deletedModifyProjectImage(String deleteFileName);
    Map<String, Object> modifySteadyProject(String projectTitle, String modifiedSteadyProjectImagePath, String description,
                                            String modifyProjectImageName, String originalProjectImageName, int modifyProjectNo);
}
