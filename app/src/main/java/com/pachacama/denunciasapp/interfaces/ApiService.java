package com.pachacama.denunciasapp.interfaces;

import com.pachacama.denunciasapp.models.Usuario;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Alumno on 11/05/2018.
 */

public interface ApiService {

    String API_BASE_URL = "https://munidenuncias-yanelaalexandra2.c9users.io/";

    @FormUrlEncoded
    @POST("/api/v1/login")
    Call<Usuario> login(@Field("username") String username,
                        @Field("password") String password);

}
