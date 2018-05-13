package com.pachacama.denunciasapp.singletons;

import com.pachacama.denunciasapp.interfaces.ApiService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceGenerator {


            private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            private static Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(ApiService.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            private static Retrofit retrofit;


            private static final ApiServiceGenerator ourInstance = new ApiServiceGenerator();

        public static ApiServiceGenerator getInstance() {
            return ourInstance;
        }

    private ApiServiceGenerator() {
        }

        public static <S> S createService(Class<S> serviceClass) {
            if(retrofit == null) {

            HttpLoggingInterceptor Logging = new HttpLoggingInterceptor();
            Logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(Logging);

            retrofit = builder.client(httpClient.build()).build();
        }
        return retrofit.create(serviceClass);
    }




}
