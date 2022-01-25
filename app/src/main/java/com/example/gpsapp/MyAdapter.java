package com.example.gpsapp;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>  {
    private ArrayList<Tracker> data;
    private Context context;
    private TextView loc;
    private TextView time;
    public MyAdapter(ArrayList<Tracker> tracker,Context c){
        data = tracker;
        context = c;
    }
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.list_layout,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            loc.setText(data.get(position).getAddress().getAddressLine(0));
            time.setText(data.get(position).getTime() + "");
    }

    public int getItemCount() {
        return data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ViewHolder(View itemView){
            super(itemView);
            loc = itemView.findViewById(R.id.b_loc);
            time = itemView.findViewById(R.id.b_time);
        }
        public void onClick(View view){

        }
    }
}
