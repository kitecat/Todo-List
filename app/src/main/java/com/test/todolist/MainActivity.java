package com.test.todolist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends ActionBarActivity {

    private static final int CM_DELETE_ID = 2;
    private static final int CM_EDIT_ID = 3;
    private static final int CM_STATUS1_ID = 1;
    private static final int CM_STATUS2_ID = 4;

    private ArrayList<String> listItems = new ArrayList();
    private EditText editText;

    ListView listView;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);

        listView = (ListView) findViewById(R.id.listView);

        SharedPreferences prefs = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE);

        try {
            listItems = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString("note", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);

        listView.setAdapter(adapter);

        registerForContextMenu(listView);
    }

    public void onButtonClick(View view) {
        String note = editText.getText().toString();
        saveObject(note);
        adapter.notifyDataSetChanged();
    }

    public void saveObject(String string) {
        SharedPreferences.Editor editor = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE).edit();
        listItems.add(string);
        try {
            editor.putString("note" ,ObjectSerializer.serialize(listItems));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
        adapter.notifyDataSetChanged();
    }

    public void loadList() {
        SharedPreferences prefs = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE);

        try {
            listItems = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString("note", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_EDIT_ID, 0, R.string.edit_record);
        menu.add(0, CM_STATUS1_ID, 0, R.string.status_done);
        menu.add(0, CM_STATUS2_ID, 0, R.string.status_during);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete_record);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            listItems.remove(acmi.position);

            SharedPreferences.Editor editor = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE).edit();
            try {
                editor.putString("note" ,ObjectSerializer.serialize(listItems));
            } catch (IOException e) {
                e.printStackTrace();
            }
            editor.commit();
            loadList();
            return true;
        }
        if (item.getItemId() == CM_STATUS1_ID) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            //db.editStatus(acmi.id, "done");
            getSupportLoaderManager().getLoader(0).forceLoad();
            //scAdapter.notifyDataSetChanged();
            return true;
        }
        if (item.getItemId() == CM_STATUS2_ID) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            //db.editStatus(acmi.id, "during");
            getSupportLoaderManager().getLoader(0).forceLoad();
            //scAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
