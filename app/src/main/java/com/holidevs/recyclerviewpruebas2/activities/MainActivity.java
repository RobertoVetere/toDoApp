package com.holidevs.recyclerviewpruebas2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.holidevs.recyclerviewpruebas2.ChatGptModels.ChatGptRequest;
import com.holidevs.recyclerviewpruebas2.ChatGptModels.ChatGptResponse;
import com.holidevs.recyclerviewpruebas2.R;
import com.holidevs.recyclerviewpruebas2.adapters.AdapterData;
import com.holidevs.recyclerviewpruebas2.interfaces.ChatGptService;
import com.holidevs.recyclerviewpruebas2.models.Task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Task> listDatos;
    private Button btn_add;

    private TextView textViewSummary;
    private AdapterData taskAdapter;

    private Task task;

    private static final String API_KEY = "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";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferencesLoad();
        itemAnimator();
        initComponents();
        setupRecyclerView();
        setListeners();
    }

    private void callToChatGPT(String taskTitle, String taskDate, Task task) {
        // Ejemplo de llamada a la API de ChatGPT
        //String taskTitle = "Título de la tarea"; // Obtén el título de la tarea de alguna manera en tu aplicación
        //String taskDate = "Fecha de la tarea"; // Obtén la fecha de la tarea de alguna manera en tu aplicación


        String prompt = "Resumen detallado para la tarea: " + taskTitle + ", Fecha: " + taskDate;

        // Configura Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crea una instancia del servicio de la API
        ChatGptService chatGptService = retrofit.create(ChatGptService.class);

        // Crea el objeto de solicitud
        ChatGptRequest request = new ChatGptRequest(prompt);

        // Agrega el encabezado de autorización con tu clave de API
        String authorizationHeader = "Bearer " + API_KEY;
        Headers headers = new Headers.Builder().add("Authorization", authorizationHeader).build();


        Log.i("ChatGptRequest", "Prompt: " + request.getPrompt());
        Log.i("ChatGptRequest", "Authorization Header: " + authorizationHeader);

        // Realiza la solicitud a la API
        Call<ChatGptResponse> call = chatGptService.sendMessage(request);
        call.enqueue(new Callback<ChatGptResponse>() {
            @Override
            public void onResponse(Call<ChatGptResponse> call, Response<ChatGptResponse> response) {
                if (response.isSuccessful()) {
                    ChatGptResponse chatResponse = response.body();
                    // Procesa la respuesta y muestra el resumen al usuario
                    String summary = chatResponse.getSummary();
                    // Muestra el resumen en la vista de texto (TextView)
                    task.setChatResponse(summary);
                    System.out.println(summary);

                    Log.i("ChatResponse", "OK");
                    Log.i("ChatGptResponse", "Response: " + response);
                } else {
                    Log.i("ChatResponse", "KO");
                }
            }

            @Override
            public void onFailure(Call<ChatGptResponse> call, Throwable t) {
                System.out.println("La tarea no ha llegado");
            }
        });


    }

    public void sharedPreferencesLoad() {
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("tasks", "");
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        listDatos = gson.fromJson(json, type);

        if (listDatos == null) {
            listDatos = new ArrayList<>();
        }
    }

    private void itemAnimator() {
        RecyclerView recyclerView = findViewById(R.id.recyclerID);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setRemoveDuration(150);
        recyclerView.setItemAnimator(itemAnimator);
    }

    private void initComponents() {
        btn_add = findViewById(R.id.btn_add);
        textViewSummary = findViewById(R.id.textChatGptResponse);
        //btn_all_tasks = findViewById(R.id.btn_allTasks);
        //btn_personal = findViewById(R.id.btn_personal);
        //btn_professional = findViewById(R.id.btn_professional);
    }

    private void setupRecyclerView() {
        RecyclerView recycler = findViewById(R.id.recyclerID);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManager);
        taskAdapter = new AdapterData(listDatos, this);
        recycler.setAdapter(taskAdapter);
    }

    private void setListeners() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SetNewTaskActivity.class);
                startActivityForResult(intent, 1);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String newTaskTitle = data.getStringExtra("new_task_title");
            String newTaskDate = data.getStringExtra("new_task_date");
            //boolean isChecked = data.getBooleanExtra("is_checked", false);
            Task newTask = new Task(newTaskTitle, newTaskDate, false, "");
            listDatos.add(newTask);
            taskAdapter.updateTasks(listDatos);

            saveSharedPreferences();

            //obtener la respuesta de Chatgpt
            callToChatGPT(newTaskTitle, newTaskDate, newTask);
        }
    }

    public void saveSharedPreferences() {

        // Guardar la lista actualizada en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String taskListJson = new Gson().toJson(listDatos);
        editor.putString("tasks", taskListJson);
        editor.apply();

    }

}