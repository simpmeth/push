package com.google.firebase.quickstart.fcm;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static String sessionKey;
    @SuppressLint("HardwareIds") String mPhoneNumber = "";

    public static String getSessionKey() {
        return sessionKey;
    }

    public static void setSessionKey(String sessionKey) {
        MainActivity.sessionKey = sessionKey;
    }

    public void showOnOutputText(String string){
        TextView textView = findViewById(R.id.output);
        textView.setText(string);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        Log.e(TAG, "------------------------------");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

        }else {
            mPhoneNumber =  tMgr.getLine1Number().toString().length()!=0 ? tMgr.getLine1Number() : "89222698821";
        }
        final String token = "aaaaaaaaaaaaaaaaaaaaaaaaaa"; FirebaseInstanceId.getInstance().getToken();

        mPhoneNumber =  mPhoneNumber.length()==0 ? "70000000001" : mPhoneNumber;



        final TextView outputText = findViewById(R.id.output);

        if (token!=null && token.length()==0){
            outputText.setText("token is null!");
            //return;
        }
        if (mPhoneNumber.length()==0){
            outputText.setText("phone number not readed!");
            //return;
        }


        final String url = getString(R.string.wscbUrl);//https://inetbank.zapsibkombank.ru:4443/";
        final String cmdUrl = url + "getData.jsp?";
        final String query = "";


        final Map<String, String> params = new HashMap<String, String>();
        //start new session

        String start = WebService.sendPost(url, query, params);

        params.put("format", "json");
        params.put("errorFormat", "json");
        params.put("appendParams", "true");
        params.put("actionParam", "GetSessionKey");

        String sessionKeyResp = WebService.sendPost(cmdUrl+PushListActivity.getQueryString(params), "", params);

        try {
            JSONObject jObj = new JSONObject(sessionKeyResp);
            setSessionKey( jObj.getString("SessionKey"));
           showOnOutputText("SessionKey = "+getSessionKey());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        params.put("actionParam", "pbDeviceInfoRq");
        params.put("AppVersion", "2.1.1.96");
        params.put("UID", "8735687346578364785");
        params.put("FirstLogon", "N");
        params.put("DeviceType", "iPhone");


        String deviceInfo = WebService.sendPost(cmdUrl+PushListActivity.getQueryString(params), query, params);


        params.put("sessionKey",MainActivity.getSessionKey());
        params.put("actionParam","GetAuthKey");

        String authKeyResp = WebService.sendPost(cmdUrl+PushListActivity.getQueryString(params),"", params);


        Button historyButton = findViewById(R.id.historybutton);
        historyButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PushListActivity.class);
                intent.putExtra("PHONE", mPhoneNumber);
                intent.putExtra("TOKEN", token);
                intent.putExtra("SESSIONKEY", getSessionKey());

                startActivity(intent);

            }
        });

        Button logTokenButton = findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Map<String,String> reqparameter = new HashMap<String, String>();

                reqparameter.put("format", "json");
                reqparameter.put("errorFormat", "json");
                reqparameter.put("appendParams", "true");
                reqparameter.put("actionParam", "ESBPushTokenRq");
                reqparameter.put("sessionKey",getSessionKey());

                reqparameter.put("Method", "add");
                reqparameter.put("PhoneNumber","74898551937");
                reqparameter.put("App", "Inetbank");
                reqparameter.put("Token","11111111111111111111111111111111");
                reqparameter.put("DeviceType", "iPhone");
                reqparameter.put("OldToken","22222211111111111111113311111111");


                String responce = WebService.sendPost(cmdUrl+PushListActivity.getQueryString(reqparameter), query, reqparameter);
                TextView textView = findViewById(R.id.output);
                textView.setText(responce);
            }
        });

    }

}
