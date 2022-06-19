package com.example.lasalle_ddm1_ex5_todolistv2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private RecyclerView tasks;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences sharedPrefs;



    private ArrayList<Task> list = new ArrayList<>(Arrays.asList(
            new Task("Hacer Deberes"),
            new Task("Dar de comer a los gatos"),
            new Task("Mirar eStudy"),
            new Task("Acabar el proyecto"),
            new Task("Ir al gimnasio")
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.sharedPrefs = getSharedPreferences("TodoList",
                MODE_PRIVATE);

        if(!this.sharedPrefs.getString("Tasks", "").isEmpty() ){
            String json = this.sharedPrefs.getString("Tasks", "");
            Type listType = new TypeToken<ArrayList<Task>>(){}.getType();


            this.list.clear();
            this.list = new Gson().fromJson(json, listType);
        }

        this.fab = findViewById(R.id.button);
        this.fab.setOnClickListener(view -> this.showCreateTaskDialog());

        this.tasks = findViewById(R.id.recycle);
        this.layoutManager = new LinearLayoutManager(this);
        this.tasks.setLayoutManager(layoutManager);

        this.adapter = new Adapter(this.list);
        this.tasks.setAdapter(this.adapter);
    }

    private void showCreateTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.create);

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton(R.string.save, (dialog, which) -> {
            if(!input.getText().toString().isEmpty()) {
                this.list.add(new Task(input.getText().toString()));
            }
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = this.sharedPrefs.edit();

        String json = new Gson().toJson(this.list);

        editor.remove("Tasks");
        editor.putString("Tasks", json);
        editor.apply();
    }
}