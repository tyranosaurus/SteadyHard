package com.tyranotyrano.steadyhard.model;

/**
 * Created by cyj on 2017-11-20.
 */


/**
 * mvp 패턴 적용 전이라 지금은 model 패키지에 저장했음 : mvp 패턴에 맞게 수정할 것!
 * */
public class SteadyProject {
    private int projectImage;
    private String projectTitle;
    private int currentDays;
    private int completeDays;

    public SteadyProject(int projectImage, String projectTitle, int currentDays, int completeDays) {
        this.projectImage = projectImage;
        this.projectTitle = projectTitle;
        this.currentDays = currentDays;
        this.completeDays = completeDays;
    }

    public int getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(int projectImage) {
        this.projectImage = projectImage;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public int getCurrentDays() {
        return currentDays;
    }

    public void setCurrentDays(int currentDays) {
        this.currentDays = currentDays;
    }

    public int getCompleteDays() {
        return completeDays;
    }

    public void setCompleteDays(int completeDays) {
        this.completeDays = completeDays;
    }
}
