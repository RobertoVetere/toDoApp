package com.holidevs.recyclerviewpruebas2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.holidevs.recyclerviewpruebas2.R;
import com.holidevs.recyclerviewpruebas2.models.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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

        dateNewTask.setOnClickListener(v -> {
            showDatePicker();
        });
    }

    private void createNewTask() {

        String title = titleNewTask.getText().toString();
        String date = dateNewTask.getText().toString();

        Task newTask = new Task(title, date, false);

        listDatos.add(newTask);

        if (title.isEmpty() || date.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Por favor ingrese un título y una fecha válida", Toast.LENGTH_SHORT).show();
        }else {
            Intent resultIntent = new Intent();

            resultIntent.putExtra("new_task_title", title);
            resultIntent.putExtra("new_task_date", date);

            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            // Aquí puedes manejar la fecha seleccionada por el usuario
            String formattedDate = formatDate(year, monthOfYear, dayOfMonth);
            dateNewTask.setText(formattedDate);
        };

        // Obtén la fecha actual para establecerla como fecha predeterminada en el DatePicker
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Crea un cuadro de diálogo de DatePicker y muestra el DatePicker
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, currentYear, currentMonth, currentDayOfMonth);
        datePickerDialog.show();
    }

    private String formatDate(int year, int month, int day) {
        // Obtén la fecha actual
        Calendar currentDate = Calendar.getInstance();

        // Crea el objeto Calendar para la fecha seleccionada
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day);

        // Verifica si la fecha seleccionada es anterior a la fecha actual
        if (selectedDate.before(currentDate)) {
            // La fecha seleccionada es anterior a la fecha actual, muestra un mensaje de error o realiza alguna acción adecuada
            // Por ejemplo, puedes mostrar un Toast indicando que no se permite seleccionar una fecha en el pasado
            Toast.makeText(getApplicationContext(), "Selecciona una fecha valida", Toast.LENGTH_SHORT).show();
            return null; // Devuelve null o algún otro valor adecuado en caso de error
        }

        // Formatea la fecha seleccionada como deseado (ejemplo: "dd/MM/yyyy")
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(selectedDate.getTime());
    }
}