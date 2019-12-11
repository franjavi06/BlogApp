package com.example.blogapp.Service;

import com.example.blogapp.Entidades.Login;
import com.example.blogapp.Entidades.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SecurityService {

    @POST("login")
    Call<Usuario> login(@Body Login login);

}
