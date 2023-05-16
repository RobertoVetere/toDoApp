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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.holidevs.recyclerviewpruebas2.R;
import com.holidevs.recyclerviewpruebas2.adapters.AdapterData;
import com.holidevs.recyclerviewpruebas2.models.Task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Task> listDatos;
    private Button btn_add;
    private Button btn_all_tasks;
    private Button btn_personal;
    private Button btn_professional;
    private AdapterData taskAdapter;

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
        //btn_all_tasks = findViewById(R.id.btn_allTasks);
        //btn_personal = findViewById(R.id.btn_personal);
        //btn_professional = findViewById(R.id.btn_professional);
    }

    private void initAdapterOnMain() {
        RecyclerView recycler = findViewById(R.id.recyclerID);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
            Task newTask = new Task(newTaskTitle, newTaskDate, false);
            listDatos.add(newTask);
            taskAdapter.updateTasks(listDatos);

            saveSharedPreferences();
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

    private void setupRecyclerView() {
        RecyclerView recycler = findViewById(R.id.recyclerID);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(taskAdapter);
    }
}