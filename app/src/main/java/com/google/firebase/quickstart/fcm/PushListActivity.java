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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_list);
        String phoneNumber= getIntent().getStringExtra("PHONE");
        String token= getIntent().getStringExtra("TOKEN");
        String sessionKey= getIntent().getStringExtra("SESSIONKEY");

        pushList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.push_recycle_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        PushAdapter adapter  = new PushAdapter(pushList);
        recyclerView.setAdapter(adapter);


        recyclerView = (RecyclerView) findViewById(R.id.push_recycle_view);
        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        sendRequest(phoneNumber,token,sessionKey);
    }

    private  void sendRequest(String phoneNumber,String token,String sessionKey) {

        String url = getString(R.string.wscbUrl);//https://inetbank.zapsibkombank.ru:4443/";
        String cmdUrl = url + "getData.jsp?";
        String query = "";


        Map<String, String> params = new HashMap<String, String>();

        params.put("appendParams", "true");
        params.put("errorFormat", "json");
        params.put("format", "json");
        params.put("sessionKey",sessionKey);
        params.put("actionParam","ESBPushHistoryRq");
        params.put("App","Inetbank");
        params.put("DeviceType", "iPhone");

        params.put("Token",token);

        params.put("PhoneNumber",phoneNumber);
        params.put("Page","1");



        String esbPushHistory = WebService.sendPost(cmdUrl+getQueryString(params),"", params);

        JSONParser jsonParser = new JSONParser(esbPushHistory);
        jsonParser.parseJSON();
        List<Push> pushList = jsonParser.getPushList();


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