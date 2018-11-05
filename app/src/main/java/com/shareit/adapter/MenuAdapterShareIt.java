package com.shareit.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shareit.entity.MenuEntityShareIt;
import com.shareit.interfaces.AdapterListenner;
import com.shareit.shareit.R;

import java.util.List;

public class MenuAdapterShareIt extends RecyclerView.Adapter {
    List<MenuEntityShareIt> menuEntityShareIts;
    AdapterListenner listenner;
    public MenuAdapterShareIt(List<MenuEntityShareIt> menuEntityShareIts, AdapterListenner listenner){
        this.menuEntityShareIts = menuEntityShareIts;
        this.listenner = listenner;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v  = layoutInflater.inflate(R.layout.item_menu, null);
        return new MenuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final MenuViewHolder menuViewHolder =(MenuViewHolder) holder;
        final MenuEntityShareIt menuEntityShareIt = menuEntityShareIts.get(position);
        menuViewHolder.tvItemMenu.setText(menuEntityShareIt.getName().toString());
        menuViewHolder.rlItemMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listenner != null){
                    listenner.onItemClickListenner(menuEntityShareIt, position, menuViewHolder);
                }
            }
        });
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{
        TextView tvItemMenu;
        RelativeLayout rlItemMenu;
        public MenuViewHolder(View itemView) {
            super(itemView);
            tvItemMenu = itemView.findViewById(R.id.tv_title_menu);
            rlItemMenu = itemView.findViewById(R.id.rl_item_menu);
        }
    }

    @Override
    public int getItemCount() {
        return menuEntityShareIts.size();
    }
}
