package com.example.blogapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blogapp.Entidades.Comment;
import com.example.blogapp.Entidades.Post;
import com.example.blogapp.Network.RetrofitClientInstance;
import com.example.blogapp.Service.GetDataService;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetail extends AppCompatActivity {

    String token;
    String postid;
    Post post;
    ProgressDialog progressDialog;
    TextView titulo, descripcion, usuario, fecha, likes, comments, views, comentario;
    Button btn_comentar;
    ChipGroup tagschipgroup;
    ImageView userimg, likedimg;
    RelativeLayout container;
    RecyclerView commentRecyclerview;
    CommentAdapter commentAdapter;
    List<Comment> commentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        progressDialog = new ProgressDialog(PostDetail.this);
        progressDialog.setMessage("Loading....");

        container = findViewById(R.id.container_itemblog);
        titulo = findViewById(R.id.posttitle_itemblog);
        descripcion = findViewById(R.id.postdescription_itemblog);
        usuario = findViewById(R.id.postusername_itemblog);
        fecha = findViewById(R.id.postdate_itemblog);
        userimg = findViewById(R.id.img_user);
        likes = findViewById(R.id.postlikes_itemblog);
        comments = findViewById(R.id.postcomments_itemblog);
        views = findViewById(R.id.postviews_itemblog);
        likedimg = findViewById(R.id.postliked_itemblog);
        tagschipgroup = findViewById(R.id.postchips_itemblog);
        comentario = findViewById(R.id.text_usercomment);
        btn_comentar = findViewById(R.id.button_usercomment);
        commentRecyclerview = findViewById(R.id.rv_comments);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null) {
            token = b.getString("TOKEN");
            postid = b.getString("ID");
        }

        progressDialog.show();
        getPostData();

        likedimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post.getLiked()){
                    setUnliked(post.getId());
                }else{
                    setLiked(post.getId());
                }
            }
        });

        btn_comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PostDetail.this,comentario.getText(),Toast.LENGTH_SHORT).show();
                Comment newcomment = new Comment();
                newcomment.setBody(comentario.getText().toString());
                progressDialog.show();
                commentPost(newcomment);
            }
        });

        //Toast.makeText(PostDetail.this, postid+" "+token, Toast.LENGTH_SHORT).show();
    }

    private void getPostData(){

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Post> call = service.getPost("Bearer "+token, Integer.parseInt(postid));
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                progressDialog.dismiss();
                post = response.body();
                post.incrementViews();
                updatePostData();
                progressDialog.show();
                getComments();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PostDetail.this, "Something went wrong...Please try later! ErrorMessage: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updatePostData(){

        titulo.setText(post.getTitle());
        descripcion.setText(post.getBody());
        usuario.setText(post.getUserName());

        long fechalong = post.getCreatedAt();
        Date fechadate = new Date(fechalong);
        fecha.setText(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(fechadate));

        userimg.setImageResource(R.drawable.user);

        likes.setText(post.getLikes().toString());
        comments.setText(post.getComments().toString());
        views.setText(post.getViews().toString());

        if(post.getLiked()){
            likedimg.setImageResource(R.drawable.ic_favorite_black_24dp);
        }else{
            likedimg.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

        String[] tags = post.getTags();
        tagschipgroup.removeAllViews();
        for (int i = 0; i < tags.length; i++){
            Chip chip = new Chip(PostDetail.this);
            chip.setText(tags[i]);
            tagschipgroup.addView(chip);
        }

    }

    private void updatePostIconsData(){

        likes.setText(post.getLikes().toString());
        comments.setText(post.getComments().toString());
        views.setText(post.getViews().toString());

        if(post.getLiked()){
            likedimg.setImageResource(R.drawable.ic_favorite_black_24dp);
        }else{
            likedimg.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

        String[] tags = post.getTags();
        tagschipgroup.removeAllViews();
        for (int i = 0; i < tags.length; i++){
            Chip chip = new Chip(PostDetail.this);
            chip.setText(tags[i]);
            tagschipgroup.addView(chip);
        }

    }

    private void setLiked(Integer id){

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResponseBody> call = service.likedPost("Bearer "+token, id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                post.setLiked(true);
                post.incrementLikes();
                updatePostIconsData();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //progressDialog.dismiss();
                Toast.makeText(PostDetail.this, "Something went wrong...Please try later! ErrorMessage: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUnliked(Integer id){

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResponseBody> call = service.unlikedPost("Bearer "+token, id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                post.setLiked(false);
                post.decrementLikes();
                updatePostIconsData();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //progressDialog.dismiss();
                Toast.makeText(PostDetail.this, "Something went wrong...Please try later! ErrorMessage: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getComments(){

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Comment>> call = service.getComments("Bearer "+token, Integer.parseInt(postid));
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                progressDialog.dismiss();
                commentsList = response.body();
                renderComments();
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PostDetail.this, "Something went wrong...Please try later! ErrorMessage: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void renderComments() {

        //adapter
        Collections.sort(commentsList);
        commentAdapter = new CommentAdapter(this,commentsList);
        progressDialog.dismiss();
        commentRecyclerview.setAdapter(commentAdapter);
        commentRecyclerview.setLayoutManager(new LinearLayoutManager(this));

    }

    private void commentPost(Comment comment){

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Comment> call = service.addComments("Bearer "+token, Integer.parseInt(postid), comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                progressDialog.dismiss();
                comentario.setText("");
                Toast.makeText(PostDetail.this, "Comentario Enviado!", Toast.LENGTH_SHORT).show();
                post.incrementComments();
                updatePostIconsData();
                progressDialog.show();
                getComments();
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PostDetail.this, "Something went wrong...Please try later! ErrorMessage: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
