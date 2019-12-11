package com.example.blogapp;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogapp.Entidades.Post;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostAdapter  extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    Context mContext;
    List<Post> mData;
    Boolean likedval;
    private OnPostListener mOnPostListener;
    PostViewHolder auxholder;

    public PostAdapter(Context mContext, List<Post> mData, OnPostListener onPostListener) {
        this.mContext = mContext;
        this.mData = mData;
        this.mOnPostListener = onPostListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_post_new,parent,false);
        return new PostViewHolder(layout,mOnPostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int position) {

        /*int lastPosition = 10;
        if(position > lastPosition){
            //first time animations
            //holder.userimg.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_animation));
            //holder.container.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));
            lastPosition = position;
            Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation);
            holder.itemView.startAnimation(animation);

        }*/

        //holder.userimg.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_animation));
        //holder.container.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));

            holder.titulo.setText(mData.get(position).getTitle());
            holder.descripcion.setText(mData.get(position).getBody());
            holder.usuario.setText(mData.get(position).getUserName());
            //holder.fecha.setText(String.valueOf(mData.get(position).getCreatedAt()));

            long fechalong = mData.get(position).getCreatedAt();
            Date fechadate = new Date(fechalong);
            holder.fecha.setText(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(fechadate));

            holder.userimg.setImageResource(R.drawable.user);

            holder.likes.setText(mData.get(position).getLikes().toString());
            holder.comments.setText(mData.get(position).getComments().toString());
            holder.views.setText(mData.get(position).getViews().toString());

            likedval = mData.get(position).getLiked();
            //holder.likedimg.setTag(likedval);
            if(likedval){
                holder.likedimg.setImageResource(R.drawable.ic_favorite_black_24dp);
            }else{
                holder.likedimg.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }

            String[] tags = mData.get(position).getTags();
            holder.tagschipgroup.removeAllViews();
            for (int i = 0; i < tags.length; i++){
                Chip chip = new Chip(mContext);
                chip.setText(tags[i]);
                holder.tagschipgroup.addView(chip);
            }

        /*holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });

        holder.likedimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, mData.get(position).getLiked().toString(), Toast.LENGTH_SHORT).show();
                ImageView img = (ImageView) v;
                if(likedval){
                    img.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    likedval = false;
                }else{
                    img.setImageResource(R.drawable.ic_favorite_black_24dp);
                    likedval = true;
                }
            }
        });*/



    }

    public void onBindViewHolder(@NonNull PostViewHolder holder, final int position, Boolean parcialUpdate) {

        //holder.container.clearAnimation();
        //holder.userimg.clearAnimation();

        holder.likes.setText(mData.get(position).getLikes().toString());
        holder.comments.setText(mData.get(position).getComments().toString());
        holder.views.setText(mData.get(position).getViews().toString());

        likedval = mData.get(position).getLiked();
        //holder.likedimg.setTag(likedval);
        if(likedval){
            holder.likedimg.setImageResource(R.drawable.ic_favorite_black_24dp);
        }else{
            holder.likedimg.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView titulo, descripcion, usuario, fecha, likes, comments, views;
        ChipGroup tagschipgroup;
        ImageView userimg, likedimg;
        RelativeLayout container;
        OnPostListener onPostListener;

        public PostViewHolder(@NonNull View itemView, OnPostListener onPostListener){
            super(itemView);
            container = itemView.findViewById(R.id.container_itemblog);
            titulo = itemView.findViewById(R.id.posttitle_itemblog);
            descripcion = itemView.findViewById(R.id.postdescription_itemblog);
            usuario = itemView.findViewById(R.id.postusername_itemblog);
            fecha = itemView.findViewById(R.id.postdate_itemblog);
            userimg = itemView.findViewById(R.id.img_user);
            likes = itemView.findViewById(R.id.postlikes_itemblog);
            comments = itemView.findViewById(R.id.postcomments_itemblog);
            views = itemView.findViewById(R.id.postviews_itemblog);
            likedimg = itemView.findViewById(R.id.postliked_itemblog);
            tagschipgroup = itemView.findViewById(R.id.postchips_itemblog);

            this.onPostListener = onPostListener;

            titulo.setOnClickListener(this);
            likedimg.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPostListener.onPostClick(getAdapterPosition(),v);
        }
    }


    public interface OnPostListener{
        void onPostClick(int position, View view);
    }
}
