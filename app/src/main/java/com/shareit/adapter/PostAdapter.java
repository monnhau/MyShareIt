package com.shareit.adapter;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shareit.entity.PostEntity;
import com.shareit.interfaces.AdapterListenner;
import com.shareit.shareit.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter{
    List<PostEntity> postEntities;
    AdapterListenner listenner;
    public static final int TYPE_ITEM_POST=0;
    public static final int TYPE_ITEM_LOAD_MORE=1;

    public PostAdapter(List<PostEntity> postEntities, AdapterListenner listenner){
        this.postEntities = postEntities;
        this.listenner = listenner;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType == TYPE_ITEM_POST){
            View v = layoutInflater.inflate(R.layout.item_post, null);
            return new PostViewHolder(v);
        }

        View v = layoutInflater.inflate(R.layout.item_load_more, null);
        return new LoadMoreViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof PostViewHolder){
            final PostViewHolder postViewHolder =(PostViewHolder) holder;
            final PostEntity postEntity = postEntities.get(position);
            postViewHolder.tvPostTitle.setText(postEntity.getTitle());
            postViewHolder.tvPostDesc.setText(postEntity.getDesc());
            Glide.with(postViewHolder.imgPost.getContext()).load(postEntity.getThumb()).into(postViewHolder.imgPost);
            postViewHolder.rlItemPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenner != null){
                        listenner.onItemClickListenner(postEntity, position, postViewHolder);
                    }
                }
            });
        }else{
            final LoadMoreViewHolder loadMoreViewHolder =(LoadMoreViewHolder) holder;
            loadMoreViewHolder.btnLoadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenner != null){
                        listenner.onItemClickListenner(null, position, loadMoreViewHolder);
                    }
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position < postEntities.size()) {
            return TYPE_ITEM_POST;
        }
        return TYPE_ITEM_LOAD_MORE;
    }

    @Override
    public int getItemCount() {
        return postEntities.size()+1;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPost;
        TextView tvPostTitle;
        TextView tvPostDesc;
        RelativeLayout rlItemPost;
        public PostViewHolder(View itemView) {
            super(itemView);
            imgPost =(ImageView) itemView.findViewById(R.id.img_post);
            tvPostTitle =(TextView) itemView.findViewById(R.id.tv_post_title);
            tvPostDesc =(TextView) itemView.findViewById(R.id.tv_post_desc);
            rlItemPost =(RelativeLayout) itemView.findViewById(R.id.rl_item_post);
        }
    }

    public class LoadMoreViewHolder  extends RecyclerView.ViewHolder{
        Button btnLoadMore;
        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            btnLoadMore = itemView.findViewById(R.id.btn_load_more);
        }
    }
}
