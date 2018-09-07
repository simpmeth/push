package com.alerter.zapsibkombank;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static String sessionKey;
    public static String token;




    public static String GetToken() {
        return token;
    }

    public static void setToken(String token) {
        MainActivity.token = token;
    }

    @SuppressLint("HardwareIds") String mPhoneNumber = "";

    private static String url ="";
    private static String cmdUrl = "";
    private static String query = "";

    SharedPreferences sPref;

    final String SAVED_PHONE = "saved_phone";

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
    protected void onResume() {
        super.onResume();
     //   FillSessionDate();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       FillSessionDate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        url = getString(R.string.wscbUrl);//https://inetbank.zapsibkombank.ru:4443/";

        cmdUrl = url + "getData.jsp?";
        try{
            String messagePush  = getIntent().getStringExtra("MESSAGE");

            if (messagePush.toString()!=""){
                showOnOutputText(messagePush);
            }
        }catch(Exception e){

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_DEFAULT));
        }

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_NUMBERS)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            mPhoneNumber = "79";
        }
        else
        {
           mPhoneNumber = tMgr.getLine1Number();
           mPhoneNumber = mPhoneNumber.replaceFirst("^8","7");

        }
        token = FirebaseInstanceId.getInstance().getToken();

        sPref = getPreferences(MODE_PRIVATE);
        String savedPhone = sPref.getString(SAVED_PHONE, "");


        mPhoneNumber =  savedPhone.length()!=0 ? savedPhone : mPhoneNumber;

        final EditText phoneEditText= findViewById(R.id.phoneEditText);


        phoneEditText.setText(mPhoneNumber);

        final TextView outputText = findViewById(R.id.output);

        if (token!=null && token.length()==0){
            outputText.setText("token is null!");
            //return;
        }
        if (mPhoneNumber.length()==0){
            outputText.setText("phone number not readed!");
            //return;
        }


        FillSessionDate();

        Button historyButton = findViewById(R.id.historybutton);
        historyButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PushListActivity.class);
                intent.putExtra("PHONE", GetPhone());
                intent.putExtra("TOKEN", GetToken());
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
                reqparameter.put("PhoneNumber", GetPhone());
                reqparameter.put("App", "Alerter");
                reqparameter.put("Token", GetToken());
                reqparameter.put("Platform", "Android");
                reqparameter.put("Version", "8.1.1");

                sPref = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(SAVED_PHONE,GetPhone());
                ed.commit();



                String responce = WebService.sendPost(cmdUrl+PushListActivity.getQueryString(reqparameter), query, reqparameter);
                TextView textView = findViewById(R.id.output);
                //textView.setText(responce);
                showOnOutputText(responce+",phone = "+GetPhone());

            }
        });

    }

    private void FillSessionDate() {
    final Map<String, String> params = new HashMap<String, String>();

        if (sessionKey!="") WebService.msCookieManager.getCookieStore().removeAll();
        String start = WebService.sendPost(url, query, params);

        params.put("format", "json");
        params.put("errorFormat", "json");
        params.put("appendParams", "true");
        params.put("actionParam", "GetSessionKey");

        String sessionKeyResp = WebService.sendPost(cmdUrl+PushListActivity.getQueryString(params), "", params);

        try {
            JSONObject jObj = new JSONObject(sessionKeyResp);
            setSessionKey( jObj.getString("SessionKey"));
            showOnOutputText("SessionKey = "+getSessionKey()+",phone = "+mPhoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        params.put("actionParam", "pbDeviceInfoRq");
        params.put("AppVersion", "2.1.1.96");
        params.put("UID", "8735687346578364785");
        params.put("FirstLogon", "N");
        params.put("DeviceType", "Android");


        String deviceInfo = WebService.sendPost(cmdUrl+PushListActivity.getQueryString(params), query, params);


        params.put("sessionKey",MainActivity.getSessionKey());
        params.put("actionParam","GetAuthKey");

        String authKeyResp = WebService.sendPost(cmdUrl+PushListActivity.getQueryString(params),"", params);
    }



    private String GetPhone() {
        final EditText phoneEditText= findViewById(R.id.phoneEditText);
        return  phoneEditText.getText().toString();

    }
}
