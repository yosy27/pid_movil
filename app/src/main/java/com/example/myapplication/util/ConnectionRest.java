package com.example.myapplication.util;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionRest {

    private static Retrofit retrofit;
    private static final String URL = "https://romantic-expression-production.up.railway.app/api/v1/";

    public static Retrofit getConnection() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL) // Asegúrate de que esta URL esté correcta
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
            return retrofit;
        }
}
