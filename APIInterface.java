package com.cryptophonecall.cv;

import org.json.JSONObject;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
//import retrofit2.http.Call;

public interface APIInterface {
    @POST("UserController")
    @FormUrlEncoded
    retrofit2.Call<LoginResponse> addUser(@Field("email") String email,
                                          @Field("pass") String pass,
                                          @Field("fname") String fname,
                                          @Field("lname") String lname,
                                          @Field("action") String action);


    @POST("PasswordController")
    @FormUrlEncoded
    retrofit2.Call<LoginResponse> genToken(@Field("email") String email,
                                           @Field("action") String action);
}
