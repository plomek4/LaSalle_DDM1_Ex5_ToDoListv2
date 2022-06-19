package com.example.lasalle_ddm1_ex5_todolistv2;


import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{


    private ArrayList<Task> tasks;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public CheckBox checkBox;
        public ImageView crossImage, editImage;

        public ViewHolder(View view) {
            super(view);

            this.title = view.findViewById(R.id.textView);
            this.checkBox = view.findViewById(R.id.checkbox);
            this.crossImage = view.findViewById(R.id.delete);
            this.editImage = view.findViewById(R.id.edit);
        }
    }

    public Adapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        final Task item = this.tasks.get(position);

        holder.checkBox.setChecked(item.isDone());
        holder.title.setText(item.getTitle());

        if(item.isDone()) {
            holder.title.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.checkBox.setOnCheckedChangeListener((checkbox, value) -> {
            if(checkbox.isChecked()) {
                // Strike through text
                holder.title.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                // Clear strike through text
                holder.title.setPaintFlags(holder.title.getPaintFlags() &
                        (~ Paint.STRIKE_THRU_TEXT_FLAG));
            }

            item.setDone(value);
        });

        holder.crossImage.setOnClickListener(view -> {
            this.tasks.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, this.tasks.size());
        });

        holder.editImage.setOnClickListener(view -> {
            this.showEditDialog(view, position);
        });
    }

    private void showEditDialog(View view, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.create);

        // Set up the input
        final EditText input = new EditText(view.getContext());
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(R.string.edit, (dialog, which) -> {
            if(!input.getText().toString().isEmpty()) {
                this.tasks.get(position).setTitle(input.getText().toString());
                notifyDataSetChanged();
            } else {
                dialog.cancel();
            }
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public int getItemCount() {
        return this.tasks.size();
    }
}
