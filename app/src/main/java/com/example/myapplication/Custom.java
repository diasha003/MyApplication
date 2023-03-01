package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Custom extends BaseAdapter {

    private ArrayList<Model>modelArrayList;
    private Context context;
    private int layout;

    public Custom(ArrayList<Model> modelArrayList, Context context, int layout) {
        this.modelArrayList = modelArrayList;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return modelArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        TextView idtxt, titletxt, bodytxt;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {


        ViewHolder viewHolder = new ViewHolder();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layout, null);

        viewHolder.titletxt = convertView.findViewById(R.id.titletxt);

        Model model = modelArrayList.get(i);
        viewHolder.titletxt.setText("Title: - " + model.getTitle() + "\n");


        return convertView;
    }

}
