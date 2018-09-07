package com.alerter.zapsibkombank;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    public static String[] titles;
    public static String[] messages;
    public static String[] times;
    private JSONArray pushList = null;


    List<Push> pushes ;


    private String json;

    public  JSONParser(String json){

        this.json = json;
    }


    protected void parseJSON(){
        JSONObject jsonObject=null;

        try {

            //pushList = new JSONArray(json);
            jsonObject = new JSONObject(json);
            pushList = jsonObject.getJSONObject("ESBPushHistoryRp").getJSONObject("Response").getJSONObject("List").getJSONArray("Row");

            titles = new String[pushList.length()];
            messages = new String[pushList.length()];
            times = new String[pushList.length()];

            pushes = new ArrayList<>();



            for(int i=0;i< pushList.length();i++){
                Push pushObject =  new Push();

                jsonObject = pushList.getJSONObject(i);

                titles[i] = jsonObject.getString("Message");
                messages[i] = jsonObject.getString("Message");
                times[i] = jsonObject.getString("RaiseTime");

                pushObject.setTitle(titles[i]);
                pushObject.setMessage(messages[i]);
                pushObject.setTime(times[i]);
                pushObject.setData(messages[i]);

                pushes.add(pushObject);



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    List<Push> getPushList()
    {

        return pushes;
    }


}