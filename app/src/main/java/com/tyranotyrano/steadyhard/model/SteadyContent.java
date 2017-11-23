package com.tyranotyrano.steadyhard.model;

/**
 * Created by cyj on 2017-11-23.
 */

public class SteadyContent {
    private SteadyProject steadyProject;
    private int contentImage;
    private String contentText;

    public SteadyContent(SteadyProject steadyProject, int contentImage, String contentText) {
        this.steadyProject = steadyProject;
        this.contentImage = contentImage;
        this.contentText = contentText;
    }

    public SteadyProject getSteadyProject() {
        return steadyProject;
    }

    public void setSteadyProject(SteadyProject steadyProject) {
        this.steadyProject = steadyProject;
    }

    public int getContentImage() {
        return contentImage;
    }

    public void setContentImage(int contentImage) {
        this.contentImage = contentImage;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }
}
