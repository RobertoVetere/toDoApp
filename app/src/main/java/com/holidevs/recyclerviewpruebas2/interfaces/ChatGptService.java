package com.holidevs.recyclerviewpruebas2.interfaces;

import com.holidevs.recyclerviewpruebas2.ChatGptModels.ChatGptRequest;
import com.holidevs.recyclerviewpruebas2.ChatGptModels.ChatGptResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatGptService {
    @POST("v1/engines/davinci-codex/completions")
    Call<ChatGptResponse> sendMessage(@Body ChatGptRequest request);
}
