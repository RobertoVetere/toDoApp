package com.holidevs.recyclerviewpruebas2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.holidevs.recyclerviewpruebas2.R;
import com.holidevs.recyclerviewpruebas2.models.Task;
import com.holidevs.recyclerviewpruebas2.adapters.AdapterData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Task> listDatos;
    private Button btn_add;
    private Button btn_all_tasks;
    private Button btn_personal;
    private Button btn_professional;
    private AdapterData taskAdapter;
    private List<Task> filteredTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferencesLoad();

        itemAnimator();
        initComponents();
        initAdapterOnMain();
        setListeners();
        setupRecyclerView();
    }

    public void sharedPreferencesLoad() {
        // Inicializar SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);

        // Cargar las tareas guardadas en SharedPreferences
        Gson gson = new Gson();
        String json = sharedPreferences.getString("tasks", "");
        Type type = new TypeToken<List<Task>>() {}.getType();
        List<Task> tasks = gson.fromJson(json, type);

        // Inicializar la lista listDatos con los datos cargados
        listDatos = tasks != null ? tasks : new ArrayList<>();
    }

    private void itemAnimator() {
        RecyclerView recyclerView = findViewById(R.id.recyclerID);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setRemoveDuration(150);
        recyclerView.setItemAnimator(itemAnimator);
    }

    private void initComponents() {
        filteredTasks = new ArrayList<>(listDatos);
        btn_add = findViewById(R.id.btn_add);
        btn_all_tasks = findViewById(R.id.btn_allTasks);
        btn_personal = findViewById(R.id.btn_personal);
        btn_professional = findViewById(R.id.btn_professional);
    }

    private void initAdapterOnMain() {
        RecyclerView recycler = findViewById(R.id.recyclerID);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        taskAdapter = new AdapterData(listDatos, this); // Inicializar el adaptador
        recycler.setAdapter(taskAdapter);
    }

    private void setListeners() {
        btn_add.setOnClickListener(v -> {
            Intent intent = new Intent(this, SetNewTaskActivity.class);
            startActivityForResult(intent, 1);
        });

        btn_all_tasks.setOnClickListener(v -> {
            showAllTasks();
        });

        btn_personal.setOnClickListener(v -> {
            filteredTasks = filterTasksByCategory("Personal");
            taskAdapter.updateTasks(filteredTasks);
        });

        btn_professional.setOnClickListener(v -> {
            filteredTasks = filterTasksByCategory("Profesional");
            taskAdapter.updateTasks(filteredTasks);
        });
    }

    private void showAllTasks() {
        filteredTasks = new ArrayList<>(listDatos);
        taskAdapter.updateTasks(filteredTasks);
    }

    private List<Task> filterTasksByCategory(String category) {
        List<Task> filteredTasks = new ArrayList<>();

        for (Task task : listDatos) {
            if (task != null && task.getType() != null && task.getType().equalsIgnoreCase(category) && !task.isDeleted()) {
                filteredTasks.add(task);
            }
        }

        return filteredTasks;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);