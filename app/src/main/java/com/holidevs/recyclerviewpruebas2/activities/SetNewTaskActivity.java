package com.holidevs.recyclerviewpruebas2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.holidevs.recyclerviewpruebas2.R;
import com.holidevs.recyclerviewpruebas2.models.Task;

import java.util.ArrayList;

public class SetNewTaskActivity extends AppCompatActivity {

    private EditText titleNewTask;
    private EditText dateNewTask;
    private Button add_task_button;
    private ArrayList<Task> listDatos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_task);

        initComponents();
        setListeners();
    }

    private void initComponents() {
        titleNewTask = findViewById(R.id.titleNewTask);
        dateNewTask = findViewById(R.id.dateNewTask);
        add_task_button = findViewById(R.id.add_task_button);
    }

    private void setListeners() {
        add_task_button.setOnClickListener(v -> {
            createNewTask();
        });
    }

    private void createNewTask() {

        String title = titleNewTask.getText().toString();
        String date = dateNewTask.getText().toString();

        Task newTask = new Task(title, date, false);

        listDatos.add(newTask);

        Intent resultIntent = new Intent();

        resultIntent.putExtra("new_task_title", title);
        resultIntent.putExtra("new_task_date", date);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}