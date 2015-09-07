package com.test.todolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private static final int CM_DELETE_ID = 2;
    private static final int CM_EDIT_ID = 3;
    private static final int CM_STATUS1_ID = 1;
    private static final int CM_STATUS2_ID = 4;

    private ArrayList<Note> listItems = new ArrayList();
    private EditText editText;

    ListView listView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        SharedPreferences prefs = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE);
        try {
            listItems = (ArrayList<Note>) ObjectSerializer.deserialize(prefs.getString("note", ObjectSerializer.serialize(new ArrayList<Note>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter = new MyAdapter(this, listItems);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
    }

    public void saveObject(String string) {
        Note note = new Note(string);
        listItems.add(note);
        save();
        adapter.notifyDataSetChanged();
    }

    public void save() {
        SharedPreferences.Editor editor = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE).edit();
        try {
            editor.putString("note" ,ObjectSerializer.serialize(listItems));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }

    public void loadList() {
        SharedPreferences prefs = getSharedPreferences("MAIN_STORAGE", Context.MODE_PRIVATE);

        try {
            listItems = (ArrayList<Note>) ObjectSerializer.deserialize(prefs.getString("note", ObjectSerializer.serialize(new ArrayList<Note>())));
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

            save();
            adapter.notifyDataSetChanged();
            return true;
        }
        if (item.getItemId() == CM_STATUS1_ID) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            (listItems.get(acmi.position)).setStatusDone();
            save();
            adapter.notifyDataSetChanged();
            return true;
        }
        if (item.getItemId() == CM_STATUS2_ID) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            (listItems.get(acmi.position)).setStatusDuring();
            save();
            adapter.notifyDataSetChanged();
            return true;
        }
        if (item.getItemId() == CM_EDIT_ID) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            intent.putExtra("LIST_ITEMS", listItems);
            intent.putExtra("ID", acmi.position);
            startActivity(intent);
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
        if (id == R.id.action_add_note) {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            intent.putExtra("LIST_ITEMS", listItems);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
