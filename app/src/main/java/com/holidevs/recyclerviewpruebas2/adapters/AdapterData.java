package com.holidevs.recyclerviewpruebas2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.holidevs.recyclerviewpruebas2.R;
import com.holidevs.recyclerviewpruebas2.activities.MainActivity;
import com.holidevs.recyclerviewpruebas2.models.Task;

import java.util.ArrayList;

public class AdapterData extends RecyclerView.Adapter<AdapterData.ViewHolderTask> {

    private ArrayList<Task> listDatos;

    private MainActivity mainActivity;


    public AdapterData(ArrayList<Task> listDatos, AdapterData adapterData) {
        this.listDatos = listDatos;
    }

    public AdapterData(ArrayList<Task> listDatos, MainActivity mainActivity) {
        this.listDatos = listDatos;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolderTask onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null, false);
        return new ViewHolderTask(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTask holder, int position) {

        AdapterData adapter = new AdapterData(listDatos, this);

        String title = listDatos.get(position).getTitle();
        String date = listDatos.get(position).getDate();
        holder.title.setText(title);
        holder.date.setText(date);
        //holder.checkTask.setChecked(task.isChecked());


        holder.btnDeleteTask.setOnClickListener(view -> {

            listDatos.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());

            mainActivity.saveSharedPreferences();
            mainActivity.sharedPreferencesLoad(); // Llama al método sharedPreferencesLoad() de MainActivity

        });


        holder.checkTask.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            Task task = listDatos.get(holder.getAdapterPosition());
            task.setCheck(isChecked);
            if (isChecked) {
                Toast.makeText(holder.itemView.getContext(), "Tarea finalizada: " + task.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public static class ViewHolderTask extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView date;
        private Button btnDeleteTask;
        private CheckBox checkTask;

        public ViewHolderTask(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_task);
            date = itemView.findViewById(R.id.txt_date);
            btnDeleteTask = itemView.findViewById(R.id.btn_delete);
            checkTask = itemView.findViewById(R.id.checkboxTask);
        }
    }
}