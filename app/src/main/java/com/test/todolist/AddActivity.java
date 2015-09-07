package com.test.todolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by FATAL1TY on 31.08.2015.
 */
public class AddActivity extends ActionBarActivity {

    private EditText editText;
    private ArrayList<Note> listItems = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();
        listItems = (ArrayList<Note>) intent.getSerializableExtra("LIST_ITEMS");
    }

    public void onButtonClick(View view) {
        editText = (EditText) findViewById(R.id.editText3);
        String noteText = editText.getText().toString();
        Note tempNote = new Note(noteText);
        listItems.add(tempNote);
        SharedPreferences.Editor editor = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE).edit();
        try {
            editor.putString("note" ,ObjectSerializer.serialize(listItems));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();

        Intent backIntent = new Intent(AddActivity.this, MainActivity.class);
        startActivity(backIntent);
    }
}
