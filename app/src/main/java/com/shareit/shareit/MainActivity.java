package com.shareit.shareit;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.shareit.adapter.MenuAdapter;
import com.shareit.adapter.PostAdapter;
import com.shareit.api.NewsApi;
import com.shareit.entity.MenuEntity;
import com.shareit.entity.PostEntity;
import com.shareit.interfaces.AdapterListenner;
import com.shareit.interfaces.HttpCallback;
import com.shareit.util.LogUtil;
import com.shareit.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    SlidingMenu menu;
    SlidingMenu menuItcNews;
    SlidingMenu menuDanTri;
    MenuAdapter menuAdapter;
    RecyclerView rvMenu;
    RecyclerView rvMenuITC;
    List<MenuEntity> menuEntities = new ArrayList<>();
    static int catId = 0;
    RecyclerView rvOfPosts;
    PostAdapter postAdapter;
    List<PostEntity> postEntities = new ArrayList<>();
    Context context = this;
    TextView tvTitle;
    SwipeRefreshLayout swipeRefreshLayout;
    ToggleButton toggoBtnSound;
    RelativeLayout rlItcNews;
    RelativeLayout rlDanTri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgMenu =(ImageView) findViewById(R.id.img_menu);
        rvOfPosts =(RecyclerView) findViewById(R.id.rv_of_posts);
        tvTitle =(TextView) findViewById(R.id.tv_title);
        toggoBtnSound =(ToggleButton) findViewById(R.id.toggo_btn_sound);

        toggoBtnSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Bật nút Âm thanh ==> thì làm gì đó
                }else{
                    //Tắt nút Âm thanh == > thì làm gì đó
                }
            }
        });

        swipeRefreshLayout =(SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        postEntities.clear();
                        postAdapter.notifyDataSetChanged();
                        getListPost();
                    }
                });
            }
        });

        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen._8sdp);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen._60sdp);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.layout_menu);

        menuItcNews = new SlidingMenu(this);
        menuItcNews.setMode(SlidingMenu.RIGHT);
        menuItcNews.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        menuItcNews.setShadowWidthRes(R.dimen._8sdp);
        menuItcNews.setShadowDrawable(R.drawable.shadow);
        menuItcNews.setBehindOffsetRes(R.dimen._60sdp);
        menuItcNews.setFadeDegree(0.35f);
        menuItcNews.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menuItcNews.setMenu(R.layout.layout_menu_itcnews);

        rlItcNews = (RelativeLayout) menu.findViewById(R.id.rl_itc_news);

        rlItcNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuItcNews.toggle();
            }
        });


        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });

        MenuEntity thoisu = new MenuEntity(1, "Thời sự", true);
        tvTitle.setText(thoisu.getName());
        MenuEntity internet = new MenuEntity(2, "Internet", false);
        MenuEntity phanmem = new MenuEntity(3, "Phần mềm", false);
        MenuEntity kinhdoanh = new MenuEntity(4, "Kinh doanh", false);
        MenuEntity thitruong = new MenuEntity(5, "Thị trường", false);
        MenuEntity game = new MenuEntity(6, "Game", false);
        MenuEntity congnghe360 = new MenuEntity(7, "Công nghệ 360", false);
        MenuEntity cntt = new MenuEntity(8, "CNTT", false);
        MenuEntity phancung = new MenuEntity(9, "Phần cứng", false);
        MenuEntity thegioiso = new MenuEntity(10, "Thế giói số", false);
        MenuEntity didong = new MenuEntity(11, "Di động", false);
        MenuEntity khoinghiep = new MenuEntity(12, "Khởi nghiệp", false);
        MenuEntity gocdoanhnghiep = new MenuEntity(13, "Góc doanh nghiệp", false);
        MenuEntity otoxemay = new MenuEntity(14, "Ô tô-Xe máy", false);
        MenuEntity videohot = new MenuEntity(15, "Video hot", false);

        menuEntities.add(thoisu);
        menuEntities.add(internet);
        menuEntities.add(phanmem);
        menuEntities.add(kinhdoanh);
        menuEntities.add(thitruong);
        menuEntities.add(game);
        menuEntities.add(congnghe360);
        menuEntities.add(cntt);
        menuEntities.add(phancung);
        menuEntities.add(thegioiso);
        menuEntities.add(didong);
        menuEntities.add(khoinghiep);
        menuEntities.add(gocdoanhnghiep);
        menuEntities.add(otoxemay);
        menuEntities.add(videohot);

        menuAdapter = new MenuAdapter(menuEntities, new AdapterListenner() {
            @Override
            public void onItemClickListenner(Object o, int pos, RecyclerView.ViewHolder holder) {
                MenuEntity menuEntity =(MenuEntity) o;

                //reaet Selected ve false
                for(int i=0; i<menuEntities.size(); ++i){
                    menuEntities.get(i).setSelected(false);
                }
                menuEntities.get(pos).setSelected(true);
                menuAdapter.notifyDataSetChanged();

                MainActivity.catId = menuEntity.getId();
                postEntities.clear();
                postAdapter.notifyDataSetChanged();
                getListPost();
                tvTitle.setText(menuEntity.getName());
                menu.toggle();
                menuItcNews.toggle();
            }
        });


        rvMenu =(RecyclerView) menu.findViewById(R.id.rv_Menu);
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        rvMenu.setItemAnimator(new DefaultItemAnimator());
        rvMenu.setAdapter(menuAdapter);

        rvMenuITC  = (RecyclerView) menuItcNews.findViewById(R.id.rv_Menu_ITC);
        rvMenuITC.setLayoutManager(new LinearLayoutManager(this));
        rvMenuITC.setItemAnimator(new DefaultItemAnimator());
        rvMenuITC.setAdapter(menuAdapter);

        postAdapter = new PostAdapter(postEntities, new AdapterListenner() {
            @Override
            public void onItemClickListenner(Object o, int pos, RecyclerView.ViewHolder holder) {
                if(holder instanceof PostAdapter.PostViewHolder){
                    PostEntity postEntity =(PostEntity) o;
                    Intent intent = new Intent(MainActivity.this, Detail_Activity.class);
                    intent.putExtra("post", postEntity);
                    startActivity(intent);
                }else{
                    getListPost();
                }
            }
        });

        rvOfPosts.setLayoutManager(new LinearLayoutManager(this));
        rvOfPosts.setItemAnimator(new DefaultItemAnimator());
        rvOfPosts.setAdapter(postAdapter);
        getListPost();

    }
    public void getListPost(){
        NewsApi.API_GET_LIST_POST(context, catId, 3, postEntities.size(), new HttpCallback() {
            @Override
            public void onSucess(final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            swipeRefreshLayout.setRefreshing(false);
                            LogUtil.d("GetListPost", s);
                            JSONArray jsonArray = new JSONArray(s);
                            for(int i=0; i<jsonArray.length(); ++i){
                                JSONObject jsonObject =(JSONObject) jsonArray.get(i);
                                PostEntity postEntity = new PostEntity(jsonObject);
                                postEntities.add(postEntity);
                            }
                            postAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


}
