package com.holidevs.recyclerviewpruebas2.ChatGptModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatGptResponse {
    @SerializedName("choices")
    private List<ChatGptChoice> choices;

    public String getSummary() {
        if (choices != null && !choices.isEmpty()) {
            return choices.get(0).getText().trim();
        }
        return "";
    }
}
