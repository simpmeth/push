package com.google.firebase.quickstart.fcm;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.List;

import retrofit2.Call;

public class PushListActivity extends ListActivity {
    ListView listview;
    PushAdapter adapter;
    ProgressDialog mProgressDialog;
    List<Push> pushList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_list);

        new DownloadXML(PushListActivity.this).execute();

    }

    private class DownloadXML extends AsyncTask<Void, Void, Void> {

        private Context _context;

        public DownloadXML(Context context) {
            this._context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(PushListActivity.this);
            mProgressDialog.setTitle("Загрузка данных");
            mProgressDialog.setMessage("Загрузка...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {

            //pushList =
            // pushList xml load
            ApiService api = RetroClient.getApiService();

            /**
             * Calling JSON
             */
            Call<List<Push> > call = api.getMyJSON();


            return null;

        }

        @SuppressLint("WrongViewCast")
        @Override
        protected void onPostExecute(Void args) {


            listview = (ListView) findViewById(R.id.list);

            if (pushList != null) {

                adapter = new PushAdapter(_context, pushList);

                listview.setAdapter(adapter);
                mProgressDialog.dismiss();
            }else {
                mProgressDialog.setTitle("Загрузка данных");
                mProgressDialog.setMessage("Загрузка не удалась!");
                //mProgressDialog.dismiss();
            }

        }
    }

}
