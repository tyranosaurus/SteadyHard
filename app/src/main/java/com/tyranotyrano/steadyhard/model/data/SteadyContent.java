package com.tyranotyrano.steadyhard.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cyj on 2017-11-23.
 */

public class SteadyContent implements Parcelable {
    @SerializedName("no")
    private int no;
    @SerializedName("content_image")
    private String contentImage;
    @SerializedName("content_text")
    private String contentText;
    @SerializedName("accomplish_date")
    private String accomplishDate;
    @SerializedName("current_days")
    private int currentDays;
    @SerializedName("complete_days")
    private int completeDays;
    @SerializedName("project_no")
    private int projectNo;
    @SerializedName("user_no")
    private int userNo;
    @SerializedName("project_title")
    private String projectTitle;
    @SerializedName("project_image")
    private String projectImage;

    public SteadyContent() { }

    public SteadyContent(int no, String contentImage, String contentText, String accomplishDate, int currentDays, int completeDays, int projectNo, int userNo, String projectImage, String projectTitle) {
        this.no = no;
        this.contentImage = contentImage;
        this.contentText = contentText;
        this.accomplishDate = accomplishDate;
        this.currentDays = currentDays;
        this.completeDays = completeDays;
        this.projectNo = projectNo;
        this.userNo = userNo;
        this.projectImage = projectImage;
        this.projectTitle = projectTitle;
    }

    public SteadyContent(Parcel src) {
        no = src.readInt();
        contentImage = src.readString();
        contentText = src.readString();
        accomplishDate = src.readString();
        currentDays = src.readInt();
        completeDays = src.readInt();
        projectNo = src.readInt();
        userNo = src.readInt();

        projectImage = src.readString();
        projectTitle = src.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SteadyContent createFromParcel(Parcel src) {
            return new SteadyContent(src);
        }

        public SteadyContent[] newArray(int size) {
            return new SteadyContent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(no);
        dest.writeString(contentImage);
        dest.writeString(contentText);
        dest.writeString(accomplishDate);
        dest.writeInt(currentDays);
        dest.writeInt(completeDays);
        dest.writeInt(projectNo);
        dest.writeInt(userNo);

        dest.writeString(projectImage);
        dest.writeString(projectTitle);

    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getContentImage() {
        return contentImage;
    }

    public void setContentImage(String contentImage) {
        this.contentImage = contentImage;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getAccomplishDate() {
        return accomplishDate;
    }

    public void setAccomplishDate(String accomplishDate) {
        this.accomplishDate = accomplishDate;
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

    public int getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(int projectNo) {
        this.projectNo = projectNo;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }
}
