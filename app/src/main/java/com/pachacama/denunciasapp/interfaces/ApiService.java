package com.pachacama.denunciasapp.interfaces;

import com.pachacama.denunciasapp.models.Denuncia;
import com.pachacama.denunciasapp.models.ResponseMessage;
import com.pachacama.denunciasapp.models.Usuario;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    String API_BASE_URL = "https://munidenuncias-yanelaalexandra2.c9users.io/";

    @FormUrlEncoded
    @POST("/api/v1/login")
    Call<Usuario> login(@Field("username") String username,
                        @Field("password") String password);

    @FormUrlEncoded
    @POST("/api/v1/usuarios")
    Call<ResponseMessage> createUser(@Field("username") String username,
                                     @Field("password") String password,
                                     @Field("nombres")  String nombres,
                                     @Field("tipo")     String tipo);

    @GET("api/v1/usuarios/{id}")
    Call<Usuario> showUser(@Path("id") Integer id);



    @GET("api/v1/denuncias")
    Call<List<Denuncia>> obtenerListaGeneralDenuncias();


    @Multipart
    @POST("/api/v1/denuncias")
    Call<ResponseMessage> crearDenunciaConImagen(
            @Part("usuarios_id") RequestBody usuarios_id,
            @Part("titulo")      RequestBody titulo,
            @Part("descripcion") RequestBody descripcion,
            @Part("direccion")   RequestBody direccion,
            @Part MultipartBody.Part imagen );


    @FormUrlEncoded
    @POST("/api/v1/denuncias")
    Call<ResponseMessage> crearDenuncia(@Field("usuarios_id")  Integer usuarios_id,
                                         @Field("titulo")      String titulo,
                                         @Field("descripcion") String descripcion,
                                         @Field("direccion")   String direccion);

    @GET("api/v1/denuncias/show/{usuarios_id}")
    Call<List<Denuncia>> obtenerListaPersonalDenuncias(@Path("usuarios_id") Integer usuarios_id);


}
