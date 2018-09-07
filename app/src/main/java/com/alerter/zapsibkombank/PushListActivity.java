package com.alerter.zapsibkombank;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PushListActivity extends AppCompatActivity {
    RecyclerView recyclerView;


    List<Push> pushList;
    public static String sessionKey;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        PushListActivity.token = token;
    }

    private static String token;
    private static String phone;
    public static String getSessionKey() {
        return sessionKey;
    }

    public static void setSessionKey(String sessionKey) {
        PushListActivity.sessionKey = sessionKey;
    }

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_list);
        String phoneNumber= getIntent().getStringExtra("PHONE");

        token= getIntent().getStringExtra("TOKEN");
        sessionKey= getIntent().getStringExtra("SESSIONKEY");

        pushList = new ArrayList<>();
        recyclerView = findViewById(R.id.push_recycle_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        PushAdapter adapter  = new PushAdapter(pushList);
        recyclerView.setAdapter(adapter);


        recyclerView = findViewById(R.id.push_recycle_view);
        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //FillSessionDate();
        sendRequest(phoneNumber,getToken(),getSessionKey());
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

    private void FillSessionDate() {

        final Map<String, String> params = new HashMap<String, String>();

        String url = getString(R.string.wscbUrl);//https://inetbank.zapsibkombank.ru:4443/";

        String cmdUrl = url + "getData.jsp?";

        String query="";
        String start = WebService.sendPost(url, query, params);

        params.put("format", "json");
        params.put("errorFormat", "json");
        params.put("appendParams", "true");
        params.put("actionParam", "GetSessionKey");

        String sessionKeyResp = WebService.sendPost(cmdUrl+PushListActivity.getQueryString(params), "", params);



        params.put("actionParam", "pbDeviceInfoRq");
        params.put("AppVersion", "2.1.1.96");
        params.put("UID", "8735687346578364785");
        params.put("FirstLogon", "N");
        params.put("DeviceType", "Android");


        String deviceInfo = WebService.sendPost(cmdUrl+PushListActivity.getQueryString(params), query, params);
        try {
            JSONObject jObj = new JSONObject(sessionKeyResp);
            setSessionKey( jObj.getString("SessionKey"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        params.put("sessionKey",getSessionKey());
        params.put("actionParam","GetAuthKey");

        String authKeyResp = WebService.sendPost(cmdUrl+PushListActivity.getQueryString(params),"", params);


    }
}