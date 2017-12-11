package com.tyranotyrano.steadyhard.model.remote.datasource;

import java.util.Map;

/**
 * Created by cyj on 2017-12-10.
 */

public interface NewContentDataSource {
    String uploadNewContentImage(String imagePath, String parentProjectPath);
    boolean deletedNewContentImage(String deleteFileName, String parentProjectPath);
    Map<String, Object>  createNewContent(String newContentImageURLPath, String contentText, String contentImageName, int currentDays, int completeDays, int projectNo);
}
