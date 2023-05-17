package com.holidevs.recyclerviewpruebas2.ChatGptModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatGptRequest {
    @SerializedName("prompt")
    private String prompt;

    public ChatGptRequest(String prompt) {
        this.prompt = prompt;
    }
}

