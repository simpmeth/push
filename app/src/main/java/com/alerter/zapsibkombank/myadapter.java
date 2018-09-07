package com.alerter.zapsibkombank;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.List;

public class myadapter extends RecyclerView.Adapter<myadapter.ViewHolder> {
    Context context;


    private List<Push> mDataset;
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public ImageView imageView;


        public ViewHolder(View v) {
            super(v);
            text = (TextView) v.findViewById(R.id.text);
            imageView = (ImageView) v.findViewById(R.id.icon);


        }
    }
    public void add(int position, Push item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }
    public void remove(Push item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }
    public myadapter(List<Push> myDataset) {
        mDataset = myDataset;
    }
    @Override
    public myadapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_push_list, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.text.setText(mDataset.get(position).getTitle());
        Glide.with(holder.imageView.getContext()).load(mDataset.get(position).getMessage()).into(holder.imageView);



    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }



}