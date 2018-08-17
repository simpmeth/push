package com.google.firebase.quickstart.fcm;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface ApiService {


    @GET("/getData.jsp")
    Call<List<Push>> getData();//@FieldMap Map<String,String> params,@HeaderMap Map<String,String> headers);
}