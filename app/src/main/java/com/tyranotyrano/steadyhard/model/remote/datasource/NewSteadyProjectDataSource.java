package com.tyranotyrano.steadyhard.model.remote.datasource;

import java.util.Map;

/**
 * Created by cyj on 2017-12-03.
 */

public interface NewSteadyProjectDataSource {
    String uploadSteadyProjectImage(String imagePath);
    boolean deletedNewProjectImage(String deleteFileName);
    Map<String, Object> createNewSteadyProject(String projectTitle, String steadyProjectImagePath, int completeDate, String description, String projectImageName);
}
