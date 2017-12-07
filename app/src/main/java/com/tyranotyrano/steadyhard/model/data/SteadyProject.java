package com.tyranotyrano.steadyhard.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cyj on 2017-11-20.
 */


public class SteadyProject implements Parcelable {
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
    @SerializedName("last_date")
    private String last_date;
    @SerializedName("user_no")
    private int userNo;

    public SteadyProject() { }

    public SteadyProject(int no, String projectTitle, String projectImage, int currentDays, int completeDays,
                         String description, String projectDate, int status, String last_date, int userNo) {
        this.no = no;
        this.projectTitle = projectTitle;
        this.projectImage = projectImage;
        this.currentDays = currentDays;
        this.completeDays = completeDays;
        this.description = description;
        this.projectDate = projectDate;
        this.status = status;
        this.last_date = last_date;
        this.userNo = userNo;
    }

    public SteadyProject(Parcel src) {
        no = src.readInt();
        projectTitle = src.readString();
        projectImage = src.readString();
        currentDays = src.readInt();
        completeDays = src.readInt();
        description = src.readString();
        projectDate = src.readString();
        status = src.readInt();
        last_date = src.readString();
        userNo = src.readInt();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SteadyProject createFromParcel(Parcel src) {
            return new SteadyProject(src);
        }

        public SteadyProject[] newArray(int size) {
            return new SteadyProject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(no);
        dest.writeString(projectTitle);
        dest.writeString(projectImage);
        dest.writeInt(currentDays);
        dest.writeInt(completeDays);
        dest.writeString(description);
        dest.writeString(projectDate);
        dest.writeInt(status);
        dest.writeString(last_date);
        dest.writeInt(userNo);
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

    public String getLast_date() {
        return last_date;
    }

    public void setLast_date(String last_date) {
        this.last_date = last_date;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }
}
