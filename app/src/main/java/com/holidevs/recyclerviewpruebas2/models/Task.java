package com.holidevs.recyclerviewpruebas2.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Task implements Serializable {

    private String title;
    private String date;
    private Boolean check = false;

    public Task(String task_name, String task_data, boolean check) {
        this.title = task_name;
        this.date = task_data;
        this.check = check;
    }

    public String getTitle() {
        return title;
    }
    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
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

