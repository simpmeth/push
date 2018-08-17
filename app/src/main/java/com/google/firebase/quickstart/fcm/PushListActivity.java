package com.google.firebase.quickstart.fcm;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PushListActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    ProgressDialog mProgressDialog;
    List<Push> pushList;

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String phoneNumber="";
    String token = "";
    String sessionKey  = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_list);
        phoneNumber= getIntent().getStringExtra("PHONE");
        token= getIntent().getStringExtra("TOKEN");
        token= getIntent().getStringExtra("SESSIONKEY");

        pushList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.push_recycle_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        PushAdapter adapter  = new PushAdapter(pushList);
        recyclerView.setAdapter(adapter);

        /*Call<List<Push>> c = RetroClient.getApiService().getData();

        try {
            c.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RetroClient.getApiService().getData().enqueue(new Callback<List<Push>>() {
            @Override
            public void onResponse(Call<List<Push>> call, Response<List<Push>> response) {
                pushList.addAll(response.body());
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Push>> call, Throwable t) {
                Toast.makeText(PushListActivity.this,"Error reading data from wscb",Toast.LENGTH_SHORT).show();
            }
        });*/
        recyclerView = (RecyclerView) findViewById(R.id.push_recycle_view);
        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        sendRequest();
    }

    private  void sendRequest() {

        String url = "http://192.168.0.100:8080/";//https://inetbank.zapsibkombank.ru:4443/";
        String cmdUrl = url + "getData.jsp?";
        String query = "";


        Map<String, String> params = new HashMap<String, String>();

        params.put("appendParams", "true");
        params.put("errorFormat", "json");
        params.put("format", "json");
        params.put("sessionKey",MainActivity.getSessionKey());
        params.put("actionParam","ESBPushHistoryRq");
        params.put("App","Inetbank");
        params.put("DeviceType", "iPhone");

        params.put("Token","aaaaaaaaaaaaaaaaaaaaaaaaaa");

        params.put("PhoneNumber","70000000001");
        params.put("Page","1");



        String esbPushHistory = WebService.sendPost(cmdUrl+getQueryString(params),"", params);

        JSONParser jsonParser = new JSONParser(esbPushHistory);
        jsonParser.parseJSON();
        List<Push> pushList = jsonParser.getPushList();
       // params.put("OldToken","aaaaaaaaaaaaaaaaaaaaaaaaaa");

        //pushList

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        PushAdapter adapter  = new PushAdapter(pushList);
        recyclerView.setAdapter(adapter);

        Log.d( "1","1");
    }

    public static String getQueryString(Map<String, String> params){
        String query = "";
        for (Map.Entry<String, String> param : params.entrySet()) {
            query += param.getKey()+"="+param.getValue()+"&";
        }
        return query ;
    }
}