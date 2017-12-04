package com.tyranotyrano.steadyhard.model.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cyj on 2017-11-20.
 */


public class SteadyProject {
    @SerializedName("no")
    private int no;
    @SerializedName("title")
    private String projectTitle;
    @SerializedName("project_image")
    private String projectImage;
    @SerializedName("current_days")
    private int currentDays;
    @SerializedName("complete_days")
    private int completeDays;
    @SerializedName("description")
    private String description;
    @SerializedName("created_date")
    private String projectDate;
    @SerializedName("status")
    private int status;
    @SerializedName("today")
    private boolean today;
    @SerializedName("user_no")
    private int userNo;
    /**
     * 중요!
     * no, status, today, user_no : GSON으로 받아올떄 steady_project 테이블의 모든 컬럼내용 다 가져와야함!
     * projectImage : 지금은 int로 되어있는데 나중에 서버에 저장된 주소 가져와야 하므로 String 으로 바꿔야함.
     * GSON 사용할려면 서버에서보내주는 JSON의 키값이랑 변수이름이랑 일치해야됨!
     * */

    public SteadyProject() { }

    /**임시 생성자 - 나중에 지울 것 */
    public SteadyProject(String projectTitle, String projectImage, int currentDays, int completeDays, String projectDate, String description) {
        this.projectImage = projectImage;
        this.projectTitle = projectTitle;
        this.currentDays = currentDays;
        this.completeDays = completeDays;
        this.projectDate = projectDate;
        this.description = description;
    }

    public SteadyProject(int no, String projectTitle, String projectImage, int currentDays, int completeDays,
                         String description, String projectDate, int status, boolean today, int userNo) {
        this.no = no;
        this.projectTitle = projectTitle;
        this.projectImage = projectImage;
        this.currentDays = currentDays;
        this.completeDays = completeDays;
        this.description = description;
        this.projectDate = projectDate;
        this.status = status;
        this.today = today;
        this.userNo = userNo;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectDate() {
        return projectDate;
    }

    public void setProjectDate(String projectDate) {
        this.projectDate = projectDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }
}
