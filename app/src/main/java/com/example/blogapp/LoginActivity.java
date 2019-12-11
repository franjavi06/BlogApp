package com.example.blogapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blogapp.Entidades.Login;
import com.example.blogapp.Entidades.Usuario;
import com.example.blogapp.Service.BlogApiServices;
import com.example.blogapp.Service.SecurityService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Context context;
    EditText email, password;
    Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        login_btn = findViewById(R.id.button_login);

        //Cargar Preferencias
        SharedPreferences preferences = getSharedPreferences("blog_app_credentials",Context.MODE_PRIVATE);
        String spuser = preferences.getString("user","Null");
        String sptoken = preferences.getString("token","Null");

        if (sptoken == "Null"){
            Toast.makeText(LoginActivity.this, "El token no existe", Toast.LENGTH_SHORT).show();
        }else{
            Intent listaPost = new Intent(context, MainActivity.class);
            listaPost.putExtra("TOKEN", sptoken);
            startActivity(listaPost);
        }

        //Toast.makeText(LoginActivity.this, spuser+"-"+sptoken, Toast.LENGTH_SHORT).show();


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().length() == 0 ||  password.getText().toString().length() == 0){
                    Toast.makeText(getApplicationContext(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
                }else{
                    Login login = new Login(email.getText().toString(),password.getText().toString());
                    Log_In(login);
                    //Toast.makeText(LoginActivity.this, email.getText()+" "+password.getText(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void Log_In(Login login){

        SecurityService securityService = BlogApiServices
                .getInstance()
                .getSecurityService();

        Call<Usuario> cuser = securityService.login(login);
        cuser.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.code() == 201){
                    Usuario usuario = response.body();
                    Toast.makeText(LoginActivity.this, usuario.toString(), Toast.LENGTH_SHORT).show();

                    //Guardar Preferencias
                    SharedPreferences preferences = getSharedPreferences("blog_app_credentials",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user",usuario.getEmail());
                    editor.putString("token",usuario.getToken());
                    editor.commit();

                    Intent listaPost = new Intent(context, MainActivity.class);
                    listaPost.putExtra("TOKEN", usuario.getToken());
                    startActivity(listaPost);

                }else{
                    Toast.makeText(LoginActivity.this, "Error en el Login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Something went wrong...Please try later! ErrorMessage: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
