package com.holidevs.recyclerviewpruebas2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.holidevs.recyclerviewpruebas2.R;
import com.holidevs.recyclerviewpruebas2.activities.MainActivity;
import com.holidevs.recyclerviewpruebas2.models.Task;

import java.util.ArrayList;
import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.ViewHolderTask> {

    private ArrayList<Task> listDatos;

    private MainActivity mainActivity;


    public AdapterData(ArrayList<Task> listDatos) {
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

        Task task2 = listDatos.get(position);

        String title = listDatos.get(position).getTitle();
        String date = listDatos.get(position).getDate();
        Boolean isCheck = listDatos.get(position).getCheck();
        holder.title.setText(title);
        holder.date.setText(date);
        holder.checkTask.setChecked(isCheck);


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
            task.setDeleted(isChecked); // Marcar la tarea como eliminada en lugar de eliminarla

            mainActivity.saveSharedPreferences();
            mainActivity.sharedPreferencesLoad();
        });

        holder.btnShowResponse.setOnClickListener(view -> {
            Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_animation_on_click);
            view.startAnimation(animation);
            mainActivity.showChatResponse(task2);
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

        private ConstraintLayout btnShowResponse;

        public ViewHolderTask(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_task);
            date = itemView.findViewById(R.id.txt_date);
            btnDeleteTask = itemView.findViewById(R.id.btn_delete);
            checkTask = itemView.findViewById(R.id.checkboxTask);
            btnShowResponse = itemView.findViewById(R.id.box_clic_Show);
        }
    }

    public void updateTasks(ArrayList<Task> listDatos) {
        this.listDatos = listDatos;
        notifyDataSetChanged();
    }

    public void setTaskDeleted(int position, boolean isDeleted) {
        Task task = listDatos.get(position);
        task.setDeleted(isDeleted);
        notifyItemChanged(position);
    }
}