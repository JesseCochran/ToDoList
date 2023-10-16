package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private ListView list;
    private Button addButton;
    private ArrayAdapter<String> itemsAdapter;

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String TASKS_KEY = "tasks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list);
        addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additem(view);
            }
        });
        //retrieves tasks from storage
        items = new ArrayList<>(getSavedTasks());
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        list.setAdapter(itemsAdapter);

        //deletes item
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long l) {
                return remove(position);
            }
        });
    }
    private Set<String> getSavedTasks() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return preferences.getStringSet(TASKS_KEY, new HashSet<>());
    }

    private void saveTasks() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(TASKS_KEY, new HashSet<>(items));
        editor.apply();
    }

    private boolean remove(int position){
        Context context = getApplicationContext();
        Toast.makeText(context, "Item Removed", Toast.LENGTH_LONG).show();
        items.remove(position);
        itemsAdapter.notifyDataSetChanged();
        saveTasks();  // Save tasks after removal
        return true;
    }
    private void additem(View view) {
        EditText input = findViewById(R.id.editText);
        String itemText = input.getText().toString();
        itemText = itemText.trim();
        if (!(itemText.equals("")))
        {
            itemsAdapter.add(itemText);
            input.setText("");
            saveTasks();  // Save tasks after addition
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please Enter Text...", Toast.LENGTH_LONG).show();
        }
    }
}