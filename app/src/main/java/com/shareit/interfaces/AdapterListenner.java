package com.shareit.interfaces;

import android.support.v7.widget.RecyclerView;

public interface AdapterListenner {
    public void onItemClickListenner(Object o, int pos, RecyclerView.ViewHolder holder);
}
