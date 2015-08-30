package com.test.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by FATAL1TY on 29.08.2015.
 */
public class MyAdapter extends BaseAdapter{
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Note> objects;

    MyAdapter(Context context, ArrayList<Note> notes) {
        ctx = context;
        objects = notes;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String tempStatus = "oops!";

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        Note p = getNote(position);

        ((TextView) view.findViewById(R.id.tvText)).setText(p.text);
        if (p.status == 0)
            tempStatus = "new";
        if (p.status == 1)
            tempStatus = "during";
        if (p.status == 2)
            tempStatus = "done";
        ((TextView) view.findViewById(R.id.textViewStatus)).setText(tempStatus);

        return view;
    }

    Note getNote(int position) {
        return ((Note) getItem(position));
    }
}
