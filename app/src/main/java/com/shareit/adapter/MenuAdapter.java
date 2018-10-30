package com.shareit.adapter;

import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shareit.entity.MenuEntity;
import com.shareit.interfaces.AdapterListenner;
import com.shareit.shareit.R;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter {
    List<MenuEntity> menuEntities;
    AdapterListenner listenner;

    public MenuAdapter(List<MenuEntity>  menuEntities, AdapterListenner listenner){
        this.menuEntities = menuEntities;
        this.listenner = listenner;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.item_menu, null);
        return new MenuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final MenuViewHolder menuViewHolder =(MenuViewHolder) holder;
        final MenuEntity menuEntity = menuEntities.get(position);
        String name = menuEntity.getName();
        menuViewHolder.tvItemMenu.setText(name);
        if(menuEntity.isSelected()){
            menuViewHolder.rlItemMenu.setBackgroundResource(R.color.colorPrimary);
        }else{
            menuViewHolder.rlItemMenu.setBackgroundResource(R.color.colorWhite);
        }

        menuViewHolder.rlItemMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listenner !=  null){
                    listenner.onItemClickListenner(menuEntity, position, menuViewHolder);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuEntities.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{
        TextView tvItemMenu;
        RelativeLayout rlItemMenu;
        public MenuViewHolder(View itemView) {
            super(itemView);
            tvItemMenu =(TextView) itemView.findViewById(R.id.tv_title_menu);
            rlItemMenu =(RelativeLayout) itemView.findViewById(R.id.rl_item_menu);
        }
    }
}
