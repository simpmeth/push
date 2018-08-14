package com.google.firebase.quickstart.fcm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Push {

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("message")
    @Expose
    private String message;


    @SerializedName("data")
    @Expose
    private String data;

    private String elementPureHtml;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String name) {
        this.message = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getElementPureHtml() {
        return elementPureHtml;
    }

    /**
     * @param elementPureHtml The elementPureHtml
     */
    public void setElementPureHtml(String elementPureHtml) {
        this.elementPureHtml = elementPureHtml;
    }

}