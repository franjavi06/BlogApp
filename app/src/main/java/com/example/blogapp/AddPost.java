package com.example.blogapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blogapp.Entidades.Post;
import com.example.blogapp.Network.RetrofitClientInstance;
import com.example.blogapp.R;
import com.example.blogapp.Service.GetDataService;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.security.Key;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class AddPost extends AppCompatActivity {

    EditText titulo,body,tag;
    Button cancelbtn, savebtn;
    ProgressDialog progressDialog;
    String token;
    Context contexto;
    ChipGroup tagschipgroup;
    String[] ntags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        titulo = findViewById(R.id.title_addpost);
        body = findViewById(R.id.body_addpost);
        tag = findViewById(R.id.tag_addpost);
        cancelbtn = findViewById(R.id.cancel_addpost);
        savebtn = findViewById(R.id.save_addpost);
        tagschipgroup = findViewById(R.id.tagschipgroup_addpost);
        contexto = this;

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null) {
            token = b.getString("TOKEN");
        }

        //Toast.makeText(AddPost.this, token, Toast.LENGTH_SHORT).show();

        tag.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction()== KeyEvent.ACTION_DOWN) && keyCode== KeyEvent.KEYCODE_ENTER && tag.getText().length()>0){
                    //Toast.makeText(AddPost.this, tag.getText().toString(), Toast.LENGTH_SHORT).show();
                    Chip chip = new Chip(contexto);
                    //chip.setChipIconResource(R.drawable.ic_label_black_24dp);
                    chip.isClickable();
                    chip.setCloseIconVisible(true);
                    chip.setText(tag.getText());
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tagschipgroup.removeView(v);
                        }
                    });
                    tagschipgroup.addView(chip);
                    //Toast.makeText(AddPost.this, String.valueOf(tagschipgroup.getChildCount()), Toast.LENGTH_SHORT).show();
                    tag.setText("");
                    return true;
                }
                else {
                    return false;
                }
            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AddPost.this, titulo.getText().toString()+" "+body.getText().toString()+" "+tag.getText().toString(), Toast.LENGTH_SHORT).show();

                Post newpost = new Post();
                newpost.setTitle(titulo.getText().toString());
                newpost.setBody(body.getText().toString());

                ntags = new String[tagschipgroup.getChildCount()];

                for (int i=0; i<tagschipgroup.getChildCount(); i++){
                    Chip auxchip = (Chip) tagschipgroup.getChildAt(i);
                    ntags[i] = auxchip.getText().toString();
                }

                newpost.setTags(ntags);


                progressDialog = new ProgressDialog(AddPost.this);
                progressDialog.setMessage("Loading....");
                progressDialog.show();

                /*Create handle for the RetrofitInstance interface*/
                GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                Call<Post> call = service.createPost("Bearer "+token, newpost);
                call.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        progressDialog.dismiss();
                        Toast.makeText(AddPost.this, response.body().getId().toString(), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(AddPost.this, "Something went wrong...Please try later! ErrorMessage: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
