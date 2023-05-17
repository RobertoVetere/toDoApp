package com.holidevs.recyclerviewpruebas2.ChatGptModels;

import com.google.gson.annotations.SerializedName;

public class ChatGptChoice {
    @SerializedName("text")
    private String text;

    public String getText() {
        return text;
    }
}
