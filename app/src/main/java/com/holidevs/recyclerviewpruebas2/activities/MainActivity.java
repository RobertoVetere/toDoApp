package com.holidevs.recyclerviewpruebas2.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.holidevs.recyclerviewpruebas2.R;
import com.holidevs.recyclerviewpruebas2.adapters.AdapterData;
import com.holidevs.recyclerviewpruebas2.models.Task;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Task> listDatos;
    private Button btnAdd;
    private TextView textViewSummary;
    private AdapterData taskAdapter;
    private RequestQueue requestQueue;
    private String API_KEY;

    private static final int DELAY_MILLIS = 30; // Velocidad de escritura en milisegundos

    private Handler messageHandler;
    private Runnable messageRunnable;

    private AnimatorSet dotsAnimatorSet;

    private static final String URL = "https://api.openai.com/v1/chat/completions";

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

    private void initComponents() {
        btnAdd = findViewById(R.id.btn_add);
        textViewSummary = findViewById(R.id.textChatGptResponse);
        requestQueue = Volley.newRequestQueue(this);
        API_KEY = readApiKeyFromConfig();
        Log.i("GPT", API_KEY);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerID);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        taskAdapter = new AdapterData(listDatos, this);
        recyclerView.setAdapter(taskAdapter);
    }

    private void setListeners() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SetNewTaskActivity.class);
                startActivityForResult(intent, 1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String newTaskTitle = data.getStringExtra("new_task_title");
            String newTaskDate = data.getStringExtra("new_task_date");
            Task newTask = new Task(newTaskTitle, newTaskDate, false, "");
            listDatos.add(newTask);
            taskAdapter.updateTasks(listDatos);

            saveSharedPreferences();
            Log.i("Pruebas GPT", "On Activity OK ");
            makeAPIRequest(newTaskTitle, newTask);
        }
    }

    public void saveSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String taskListJson = new Gson().toJson(listDatos);
        editor.putString("tasks", taskListJson);
        editor.apply();
    }

    private String readApiKeyFromConfig() {
        String apiKey = null;
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.config);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String jsonContent = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonContent);
            apiKey = jsonObject.getString("api_key");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return apiKey;
    }

    private void makeAPIRequest(String title, Task task) {

        Log.i("Pruebas GPT", "On api Request OK ");
        JSONArray messages = new JSONArray();
        JSONObject message1 = new JSONObject();

        try {
            message1.put("role", "user");
            message1.put("content",  title);
            messages.put(message1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-3.5-turbo");
            jsonBody.put("messages", messages);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray choices = response.getJSONArray("choices");
                            JSONObject completion = choices.getJSONObject(0);
                            String generatedText = completion.getJSONObject("message").getString("content");



                            // Configurar la respuesta generada en el objeto Task
                            task.setChatResponse(generatedText);
                            Log.i("Pruebas GPT", "On response OK ");
                            Toast.makeText(MainActivity.this, "Respuesta: " + generatedText, Toast.LENGTH_SHORT).show();
                            Log.i("Pruebas GPT", "Respuesta: " + generatedText);
                            Log.i("ChatResponse", "Respuesta: " + task.getChatResponse());

                            showChatResponse(task);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error al hacer la llamada a la API", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + API_KEY);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(request);
    }

        public void showChatResponse(Task task) {
            // Cancelar el mensaje anterior si hay uno en curso
            if (messageHandler != null && messageRunnable != null) {
                messageHandler.removeCallbacks(messageRunnable);
            }

            String response = task.getChatResponse();
            textViewSummary.setText(""); // Limpiar el texto actual del TextView

            // Mostrar los puntos suspensivos
            final StringBuilder stringBuilder = new StringBuilder("...");
            textViewSummary.setText(stringBuilder.toString());

            // Mostrar el mensaje letra a letra después de los puntos suspensivos
            messageHandler = new Handler();
            messageRunnable = new Runnable() {
                int index = 0;

                @Override
                public void run() {
                    if (index < response.length()) {
                        stringBuilder.append(response.charAt(index++));
                        textViewSummary.setText(stringBuilder.toString());
                        messageHandler.postDelayed(this, DELAY_MILLIS);
                    }
                }
            };

            // Iniciar la tarea de mostrar el mensaje
            messageHandler.postDelayed(messageRunnable, DELAY_MILLIS * 4); // Retraso de 4 veces el DELAY_MILLIS para los puntos suspensivos
        }

}