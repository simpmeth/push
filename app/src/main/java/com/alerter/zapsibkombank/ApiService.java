package com.alerter.zapsibkombank;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {


    @GET("/getData.jsp")
    Call<List<Push>> getData();//@FieldMap Map<String,String> params,@HeaderMap Map<String,String> headers);
}