package com.holidevs.recyclerviewpruebas2.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Task implements Serializable {

    private String title;
    private String date;
    private Boolean check = false;

    private String type;

    private String chatResponse;

    private boolean isDeleted;

    public Task(String task_name, String task_data, boolean check, String chatResponse) {
        this.title = task_name;
        this.date = task_data;
        this.check = check;
        this.chatResponse = chatResponse;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public Boolean getCheck() {
        return check;
    }

    public String getChatResponse() {
        return chatResponse;
    }

    public void setChatResponse(String chatResponse) {
        this.chatResponse = chatResponse;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    @NonNull
    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", check=" + check +
                '}';
    }
}

