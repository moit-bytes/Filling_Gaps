package com.moitbytes.coolieapp;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitApiService
{
    @GET("/")
    Call<String> getStationNames();

    @POST("/pnr")
    Call<String> getPNRDetails(@Query("pnr") String pnr);

    @POST("/station")
    Call<String> getStationRoutes(@Query("pnr") String pnr);

    @Headers("Content-Type: text/plain")
    @POST("/otp")
    Call<String> sendUserSMS(@Body RequestBody body);
}
