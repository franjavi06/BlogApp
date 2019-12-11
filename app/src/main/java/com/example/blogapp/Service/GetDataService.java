package com.example.blogapp.Service;

import androidx.annotation.Nullable;

import com.example.blogapp.Entidades.Comment;
import com.example.blogapp.Entidades.Login;
import com.example.blogapp.Entidades.Post;
import com.example.blogapp.Entidades.Usuario;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GetDataService {


    @GET("post")
    Call<List<Post>> getAllPost(@Header("Authorization") String authToken);

    @GET("post/{id}")
    Call<Post> getPost(@Header("Authorization") String authToken, @Path("id") Integer id);

    @POST("post")
    Call<Post> createPost(@Header("Authorization") String authToken, @Body Post post);

    @PUT("post/{id}/like")
    Call<ResponseBody> likedPost(@Header("Authorization") String authToken, @Path("id") Integer id);

    @DELETE("post/{id}/like")
    Call<ResponseBody> unlikedPost(@Header("Authorization") String authToken, @Path("id") Integer id);

    @GET("post/{postId}/comment")
    Call<List<Comment>> getComments(@Header("Authorization") String authToken, @Path("postId") Integer postId);

    @POST("post/{postId}/comment")
    Call<Comment> addComments(@Header("Authorization") String authToken, @Path("postId") Integer postId, @Body Comment comment);

}
