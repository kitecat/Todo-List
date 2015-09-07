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
 * Created by FATAL1TY on 30.08.2015.
 */
public class EditActivity extends ActionBarActivity {

    private EditText editText;
    private ArrayList<Note> listItems = new ArrayList();
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editText = (EditText) findViewById(R.id.editText2);

        Intent intent = getIntent();
        listItems = (ArrayList<Note>) intent.getSerializableExtra("LIST_ITEMS");
        id = intent.getIntExtra("ID", 0);

        editText.setText((listItems.get(id)).text);
    }

    public void onButtonClick(View view) {
        String noteText = editText.getText().toString();
        (listItems.get(id)).setText(noteText);
        SharedPreferences.Editor editor = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE).edit();
        try {
            editor.putString("note" ,ObjectSerializer.serialize(listItems));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();

        Intent backIntent = new Intent(EditActivity.this, MainActivity.class);
        startActivity(backIntent);
    }
}
