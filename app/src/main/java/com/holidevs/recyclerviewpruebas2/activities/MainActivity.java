package com.holidevs.recyclerviewpruebas2.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.holidevs.recyclerviewpruebas2.R;
import com.holidevs.recyclerviewpruebas2.models.Task;
import com.holidevs.recyclerviewpruebas2.adapters.AdapterData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Task> listDatos;
    private Button btn_add;
    private AdapterData adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemAnimator();

        initComponents();
        setListeners();
        setupRecyclerView();
    }

    private void itemAnimator() {

        RecyclerView recyclerView = findViewById(R.id.recyclerID);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        itemAnimator.setRemoveDuration(150);
        //itemAnimator.setAddDuration(600);
        //itemAnimator.setChangeDuration(200);
        //itemAnimator.setMoveDuration(200);

        recyclerView.setItemAnimator(itemAnimator);
    }

    private void initComponents() {
        btn_add = findViewById(R.id.btn_add);
    }

    private void setListeners() {
        btn_add.setOnClickListener(v -> {
            Intent intent = new Intent(this, SetNewTaskActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String newTaskTitle = data.getStringExtra("new_task_title");
            String newTaskDate = data.getStringExtra("new_task_date");
            Task newTask = new Task(newTaskTitle, newTaskDate, false);
            listDatos.add(newTask);
            adapter.notifyDataSetChanged();
        }
    }

    private void setupRecyclerView() {

        RecyclerView recycler = findViewById(R.id.recyclerID);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        listDatos = new ArrayList<Task>();

        adapter = new AdapterData(listDatos);
        recycler.setAdapter(adapter);
    }
}