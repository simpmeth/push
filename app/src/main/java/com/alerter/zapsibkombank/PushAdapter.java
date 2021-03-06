package com.alerter.zapsibkombank;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import java.util.List;

public class PushAdapter extends RecyclerView.Adapter<PushAdapter.ViewHolder>{

    private List<Push> pushList;

    private LayoutInflater mInflater;

    // Constructors
    public PushAdapter(List<Push> objects) {

        this.pushList = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row ,parent,false);
       return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            Push  push =  pushList.get(position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){


            }
            holder.time.setText(Html.fromHtml(push.getTime()));

        holder.message.setText(Html.fromHtml(push.getMessage()));
    }

    @Override
    public int getItemCount() {
        if (pushList == null)
        return 0;
        return pushList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView time;

        public final TextView message;



        public ViewHolder (View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.col_time);

            message= itemView.findViewById(R.id.col_message);


        }
    }
}