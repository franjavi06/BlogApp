package com.example.blogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.blogapp.Entidades.Post;
import com.example.blogapp.Network.RetrofitClientInstance;
import com.example.blogapp.Service.GetDataService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PostAdapter.OnPostListener{

    RecyclerView postRecyclerview;
    PostAdapter postAdapter;
    EditText searchtext;
    ProgressDialog progressDialog;
    String token;
    List<Post> postlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        searchtext = findViewById(R.id.searchtext_post);
        postRecyclerview = findViewById(R.id.rv_post);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null) {
            token = b.getString("TOKEN");
        }

        progressDialog.show();
        getDataList();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPost = new Intent(view.getContext(),AddPost.class);
                addPost.putExtra("TOKEN", token);
                startActivity(addPost);
            }
        });
    }

    private void getDataList(){

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Post>> call = service.getAllPost("Bearer "+token);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                progressDialog.dismiss();
                postlist = response.body();
                generateDataList();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later! ErrorMessage: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void generateDataList() {

        //adapter
        Collections.sort(postlist);
        postAdapter = new PostAdapter(this,postlist,this);
        progressDialog.dismiss();
        postRecyclerview.setAdapter(postAdapter);
        postRecyclerview.setLayoutManager(new LinearLayoutManager(this));

    }


    private void setLiked(Integer id, final int position){

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResponseBody> call = service.likedPost("Bearer "+token, id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                postlist.get(position).setLiked(true);
                postlist.get(position).incrementLikes();
                postAdapter.notifyItemChanged(position,true);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later! ErrorMessage: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUnliked(Integer id, final int position){

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ResponseBody> call = service.unlikedPost("Bearer "+token, id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                postlist.get(position).setLiked(false);
                postlist.get(position).decrementLikes();
                postAdapter.notifyItemChanged(position,true);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later! ErrorMessage: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume(){

        super.onResume();
        progressDialog.show();
        getDataList();

    }

    @Override
    public void onPostClick(int position, View view) {

        //Click en Like
        if (view.getId() == R.id.postliked_itemblog){

            if (postlist.get(position).getLiked()){
                setUnliked(postlist.get(position).getId(),position);
            }else{
                setLiked(postlist.get(position).getId(),position);
            }

        }
        else {
            Intent detallePost = new Intent(this, PostDetail.class);
            detallePost.putExtra("TOKEN", token);
            detallePost.putExtra("ID", postlist.get(position).getId().toString());
            startActivity(detallePost);
        }
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
