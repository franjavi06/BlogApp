package com.example.blogapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogapp.Entidades.Comment;
import com.example.blogapp.Entidades.Post;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{

    Context mContext;
    List<Comment> mData;

    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_comment,parent,false);
        return new CommentAdapter.CommentViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {

        holder.username.setText(mData.get(position).getUserName());
        long fechalong = mData.get(position).getCreatedAt();
        Date fechadate = new Date(fechalong);
        holder.datetime.setText(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(fechadate));
        holder.body.setText(mData.get(position).getBody());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView username, datetime, body;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_comment);
            datetime = itemView.findViewById(R.id.datetime_comment);
            body = itemView.findViewById(R.id.body_comment);
        }
    }
}
