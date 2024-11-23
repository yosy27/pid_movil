package com.example.myapplication.api;

import com.example.myapplication.entity.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceUsuarioApi {
    @POST("usuario")
    public abstract Call<Usuario> insertaUsuario(@Body Usuario obj);
}