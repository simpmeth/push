package com.google.firebase.quickstart.fcm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class PushAdapter extends ArrayAdapter{

    List<Push> pushList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public PushAdapter(Context context, List<Push> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        pushList = objects;
    }

    @Override
    public Push getItem(int position) {
        return pushList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.list_row, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Push item = getItem(position);

        vh.time.setText(item.getTime());
        vh.title.setText(item.getTitle());
        vh.message.setText(item.getMessage());



        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final TextView time;
        public final TextView title;
        public final TextView message;
        public final TextView data;

        private ViewHolder(RelativeLayout rootView, TextView time, TextView title, TextView message, TextView data) {
            this.rootView = rootView;
            this.time = time;
            this.title = title;
            this.message = message;
            this.data = data;
        }

        public static ViewHolder create(RelativeLayout rootView) {

            TextView textViewTime = (TextView) rootView.findViewById(R.id.col_time);
            TextView textViewTitle= (TextView) rootView.findViewById(R.id.col_title);
            TextView textViewMessage= (TextView) rootView.findViewById(R.id.col_message);
            TextView textViewData= (TextView) rootView.findViewById(R.id.col_data);


            return new ViewHolder(rootView, textViewTime, textViewTitle, textViewMessage, textViewData);
        }
    }
}