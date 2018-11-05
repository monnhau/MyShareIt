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

public class MenuAdapterShareItChildCommon extends RecyclerView.Adapter{
    List<MenuEntityShareIt> menuEntitiesChildByParent;
    AdapterListenner listenner;
    String parenNameCat;
    static final int ITEM_NORMAL=1;
    static final int ITEM_LABEL=0;

    public MenuAdapterShareItChildCommon(List<MenuEntityShareIt> menuEntitiesChildByParent, String parentNameCat, AdapterListenner listenner){
        this.menuEntitiesChildByParent = menuEntitiesChildByParent;
        this.parenNameCat = parentNameCat;
        this.listenner = listenner;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = null;
        if(viewType==ITEM_NORMAL){
            v = layoutInflater.inflate(R.layout.item_menu, null);
            return new MenuViewHolder(v);
        }

        v = layoutInflater.inflate(R.layout.item_menu_child_title, null);
        return new LabelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(position != 0){
            final MenuViewHolder menuViewHolder =(MenuViewHolder) holder;
            final MenuEntityShareIt menuEntityShareIt =(MenuEntityShareIt) menuEntitiesChildByParent.get(position-1);
            String itemName = (position == 1)?"Xem tất cả":""+menuEntityShareIt.getName().toString();
            menuViewHolder.tvTitleMenu.setText(itemName);
            menuViewHolder.rlItemMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenner != null){
                        listenner.onItemClickListenner(menuEntityShareIt, position-1, menuViewHolder);
                    }
                }
            });
        }
        else {
            LabelViewHolder labelViewHolder = (LabelViewHolder) holder;
            labelViewHolder.tvItemLabelMenu.setText(parenNameCat);
        }


    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitleMenu;
        RelativeLayout rlItemMenu;
        public MenuViewHolder(View itemView) {
            super(itemView);
            tvTitleMenu =(TextView) itemView.findViewById(R.id.tv_title_menu);
            rlItemMenu = (RelativeLayout) itemView.findViewById(R.id.rl_item_menu);
        }
    }

    public class LabelViewHolder extends RecyclerView.ViewHolder{
        TextView tvItemLabelMenu;
        public LabelViewHolder(View itemView) {
            super(itemView);
            tvItemLabelMenu = (TextView) itemView.findViewById(R.id.tv_item_label_menu);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0) return ITEM_LABEL;
        return ITEM_NORMAL;
    }

    @Override
    public int getItemCount() {
        return  menuEntitiesChildByParent.size()+1;
    }
}
