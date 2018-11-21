package com.shareit.shareit;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.shareit.adapter.MenuAdapter;
import com.shareit.adapter.MenuAdapterShareIt;
import com.shareit.adapter.MenuAdapterShareItChildCommon;
import com.shareit.adapter.PostAdapter;
import com.shareit.api.NewsApi;
import com.shareit.define.Define;
import com.shareit.entity.MenuEntity;
import com.shareit.entity.MenuEntityShareIt;
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
    SlidingMenu menu, menuItcNews, menuDanTri, menuChildCommon;
    MenuAdapterShareIt menuAdapterShareIt;
    MenuAdapter menuAdapterItcNews;
    MenuAdapter menuAdapterDanTri;
    RecyclerView rvMenu, rvMenuITC, rvDanTri, rvMenuChildCommon;
    List<MenuEntityShareIt>  menuEntities = new ArrayList<>();
    List<MenuEntityShareIt>  menuEntitiesShareItChildAll = new ArrayList<>();
    List<MenuEntityShareIt>  menuEntitiesShareItChildByParent = new ArrayList<>();
    List<MenuEntity> menuEntitiesItcNews = new ArrayList<>();
    static int catId = 0;
    static int catIdShareit = 0;
    static boolean isShareItPostType = false;
    RecyclerView rvOfPosts;
    PostAdapter postAdapter;
    List<PostEntity> postEntities = new ArrayList<>();
    Context context = this;
    TextView tvTitle;
    SwipeRefreshLayout swipeRefreshLayout;
    ToggleButton toggoBtnSound;
    RelativeLayout rlItcNews, rlDanTri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgMenu =(ImageView) findViewById(R.id.img_menu);
        rvOfPosts =(RecyclerView) findViewById(R.id.rv_of_posts);
        tvTitle =(TextView) findViewById(R.id.tv_title);
        toggoBtnSound =(ToggleButton) findViewById(R.id.toggo_btn_sound);
        swipeRefreshLayout =(SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

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

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        postEntities.clear();
                        //postAdapter.notifyDataSetChanged();
                        if(isShareItPostType == true) getListPostShareIt();
                        else getListPost();
                    }
                });
            }
        });

        /*BEGIN Sliding Menu for ShareIT*/
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen._8sdp);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen._100sdp);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.layout_menu);
        /*END Sliding Menu for ShareIT*/

        rlItcNews = (RelativeLayout) menu.findViewById(R.id.rl_itc_news);

        rlItcNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuItcNews.toggle();
            }
        });
        rlDanTri = (RelativeLayout) menu.findViewById(R.id.rl_dan_tri);

        rlDanTri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuDanTri.toggle();
            }
        });

        /*BEGIN Sliding Menu for ITC NEW*/
        menuItcNews = new SlidingMenu(this);
        menuItcNews.setMode(SlidingMenu.RIGHT);
        menuItcNews.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//menuItcNews.setShadowWidthRes(R.dimen._8sdp);
        menuItcNews.setShadowDrawable(null);
        menuItcNews.setBehindOffsetRes(R.dimen._100sdp);
        menuItcNews.setFadeDegree(0.35f);
        menuItcNews.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menuItcNews.setMenu(R.layout.layout_menu_itcnews);
        /*END Sliding Menu for ITC NEW*/

        /*BEGIN Sliding Menu for DANTRI*/
        menuDanTri = new SlidingMenu(this);
        menuDanTri.setMode(SlidingMenu.RIGHT);
        menuDanTri.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//menuDanTri.setShadowWidthRes(R.dimen._0sdp);
        menuDanTri.setShadowDrawable(null);
        menuDanTri.setBehindOffsetRes(R.dimen._100sdp);
        menuDanTri.setFadeDegree(0.35f);
        menuDanTri.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menuDanTri.setMenu(R.layout.layout_menu_dan_tri);
        /*END Sliding Menu for DAN TRI*/

        /*BEGIN Sliding Menu for ChildCommon*/
        menuChildCommon = new SlidingMenu(this);
        menuChildCommon.setMode(SlidingMenu.RIGHT);
        menuChildCommon.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);/*menuChildCommon.setShadowWidthRes(R.dimen._8sdp);*/
        menuChildCommon.setShadowDrawable(null);
        menuChildCommon.setBehindOffsetRes(R.dimen._100sdp);
        menuChildCommon.setFadeDegree(0.35f);
        menuChildCommon.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menuChildCommon.setMenu(R.layout.layout_menu_child_common);
        /*END Sliding Menu for ChildCommon*/



        /*Prepare menuEntities for RSS News*/
        Intent intent = getIntent();
        menuEntitiesItcNews = (List<MenuEntity>) intent.getSerializableExtra("menuEntitiesItcNews");
        tvTitle.setText(menuEntitiesItcNews.get(0).getName().toString());
        /*END Prepare menuEntities for RSS News*/

        /*BEGIN Prepare menuEntities for ShareIT News  (Lấy các danh mục cha)*/

        menuEntities = (List<MenuEntityShareIt>) intent.getSerializableExtra("parentCats");
        /*END Prepare menuEntities for ShareIT News  (Lấy các danh mục cha)*/

        /*BEGIN Prepare menuEntities for ShareIT News  (Lấy all các danh mục con)*/
        menuEntitiesShareItChildAll = (List<MenuEntityShareIt>) intent.getSerializableExtra("childCats");
        /*END Prepare menuEntities for ShareIT News  (Lấy all các danh mục con)*/

        /*BEGIN Prepare postEntities for RSS id 1 */
        postEntities = (List<PostEntity>) intent.getSerializableExtra("posts");
        /*END Prepare postEntities for RSS id 1 */

        /*BEGIN Prepare menuAdapter for Rss News*/
        menuAdapterItcNews = new MenuAdapter(menuEntitiesItcNews, new AdapterListenner() {
            @Override
            public void onItemClickListenner(Object o, int pos, RecyclerView.ViewHolder holder) {
                MenuEntity menuEntity =(MenuEntity) o;

                //reaet Selected ve false
                for(int i=0; i<menuEntitiesItcNews.size(); ++i){
                    menuEntitiesItcNews.get(i).setSelected(false);
                }
                menuEntitiesItcNews.get(pos).setSelected(true);
                menuAdapterItcNews.notifyDataSetChanged();

                MainActivity.catId = menuEntity.getId();
                postEntities.clear();
                //postAdapter.notifyDataSetChanged();
                getListPost();
                tvTitle.setText(menuEntity.getName());
                menu.toggle();
                menuItcNews.toggle();
            }
        });

        menuAdapterDanTri = new MenuAdapter(menuEntitiesItcNews, new AdapterListenner() {
            @Override
            public void onItemClickListenner(Object o, int pos, RecyclerView.ViewHolder holder) {
                MenuEntity menuEntity =(MenuEntity) o;

                //reaet Selected ve false
                for(int i=0; i<menuEntitiesItcNews.size(); ++i){
                    menuEntitiesItcNews.get(i).setSelected(false);
                }
                menuEntitiesItcNews.get(pos).setSelected(true);
                menuAdapterItcNews.notifyDataSetChanged();

                MainActivity.catId = menuEntity.getId();
                postEntities.clear();
                //postAdapter.notifyDataSetChanged();
                getListPost();
                tvTitle.setText(menuEntity.getName());
                menu.toggle();
                menuDanTri.toggle();
            }
        });
        /*END Prepare menuAdapter for Rss News*/

        /*BEGIN----- Prepare menuAdapter for ShareIT News*/
        menuAdapterShareIt = new MenuAdapterShareIt(menuEntities, new AdapterListenner() {
            @Override
            public void onItemClickListenner(Object o, int pos, RecyclerView.ViewHolder holder) {
                MenuEntityShareIt menuItemParent =(MenuEntityShareIt) o;
                int idNhan = menuItemParent.getId();
                String parentNameCat = menuItemParent.getName().toString();

                //chuan bi danh sach Cat Child (menuEntiesChildByParent) ung voi parent_id da chon
                menuEntitiesShareItChildByParent.clear();
                menuEntitiesShareItChildByParent.add(menuItemParent);

                for(int i=0; i<menuEntitiesShareItChildAll.size(); i++){
                    MenuEntityShareIt menuItemChild =(MenuEntityShareIt) menuEntitiesShareItChildAll.get(i);
                    int idCha =  menuItemChild.getParentId();
                    if( idCha == idNhan ){
                        menuEntitiesShareItChildByParent.add(menuItemChild);
                    }
                }
                //new 1 MenuAdapterShareItShareItChilCommon
                MenuAdapterShareItChildCommon menuAdapter = new MenuAdapterShareItChildCommon(menuEntitiesShareItChildByParent, parentNameCat, new AdapterListenner() {
                    @Override
                    public void onItemClickListenner(Object o, int pos, RecyclerView.ViewHolder holder) {
                        MenuEntityShareIt menuItem =(MenuEntityShareIt) o;
                        catIdShareit = menuItem.getId();
                        postEntities.clear();
                        getListPostShareIt();
                        tvTitle.setText(menuItem.getName());
                        menuChildCommon.toggle();
                        menu.toggle();
                    }
                });
                //set Adapter cho rvMenuChildCommon
                rvMenuChildCommon.setAdapter(menuAdapter);
                //mo sliding menu Cat_Childs ugn voi id_Cha da nhan
                menuChildCommon.toggle();
            }
        }) ;

        /*END----- Prepare menuAdapter for ShareIT News*/


        /*BEGIN Set Adapter for all rv Menu: ShareIT, ITCnew, DanTri*/
        rvMenu =(RecyclerView) menu.findViewById(R.id.rv_Menu);
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        rvMenu.setItemAnimator(new DefaultItemAnimator());
        rvMenu.setAdapter(menuAdapterShareIt);

        rvMenuITC  = (RecyclerView) menuItcNews.findViewById(R.id.rv_Menu_ITC);
        rvMenuITC.setLayoutManager(new LinearLayoutManager(this));
        rvMenuITC.setItemAnimator(new DefaultItemAnimator());
        rvMenuITC.setAdapter(menuAdapterItcNews);

        rvDanTri  = (RecyclerView) menuDanTri.findViewById(R.id.rv_Dan_Tri);
        rvDanTri.setLayoutManager(new LinearLayoutManager(this));
        rvDanTri.setItemAnimator(new DefaultItemAnimator());
        rvDanTri.setAdapter(menuAdapterDanTri);

        rvMenuChildCommon  = (RecyclerView) menuChildCommon.findViewById(R.id.rv_menu_child_common);
        rvMenuChildCommon.setLayoutManager(new LinearLayoutManager(this));
        rvMenuChildCommon.setItemAnimator(new DefaultItemAnimator());
                      //rvDanTri.setAdapter(menuAdapterDanTri);
        /*EDN Set Adapter for all rv Menu: ShareIT, ITCnew, DanTri*/





        /*BEGIN new() postAdapter để hiện thị trang index, set postAdapter cho RecyclView rvOfPosts*/
        postAdapter = new PostAdapter(postEntities, isShareItPostType, new AdapterListenner() {
            @Override
            public void onItemClickListenner(Object o, int pos, RecyclerView.ViewHolder holder) {
                if(holder instanceof PostAdapter.PostViewHolder){
                    PostEntity postEntity =(PostEntity) o;
                    Intent intent = new Intent(MainActivity.this, Detail_Activity.class);
                    intent.putExtra("post", postEntity);
                    intent.putExtra("isShareItPostType", isShareItPostType);
                    startActivity(intent);
                }else{
                    if(isShareItPostType == true) getListPostShareIt() ;
                    else getListPost();
                }
            }
        });

        rvOfPosts.setLayoutManager(new LinearLayoutManager(this));
        rvOfPosts.setItemAnimator(new DefaultItemAnimator());
        rvOfPosts.setAdapter(postAdapter);
        /*END  new() postAdapter để hiện thị trang index, set postAdapter cho RecyclView rvOfPosts*/

    }


    public void getListPost(){
        //neu chuyen tu doc tin ShareIt sang tin RSS, thi clear posEnties cũ của ShareIT
        if(isShareItPostType==true) {
            postEntities.clear();
            isShareItPostType=false;
            postAdapter.setShareItPostType(false);
        }
        NewsApi.API_GET_LIST_POST(context, catId, 3, postEntities.size(), new HttpCallback() {
            @Override
            public void onSucess(final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            swipeRefreshLayout.setRefreshing(false);
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

    public void getListPostShareIt(){
        //neu chuyen tu doc tin RSS sang tin Shareit, thi clear posEnties cũ của RSS
        if(isShareItPostType==false) {
            postEntities.clear();
            isShareItPostType=true;
            postAdapter.setShareItPostType(true);
        }
        NewsApi.API_GET_LIST_POST_SHAREIT(context, catIdShareit, (postEntities.size()/ Define.paginateNum)+1, new HttpCallback() {
            @Override
            public void onSucess(final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            swipeRefreshLayout.setRefreshing(false);
                            JSONArray jsonArray = new JSONArray(s);
                            for(int i=0; i<jsonArray.length(); ++i){
                                JSONObject jsonObject =(JSONObject) jsonArray.get(i);
                                PostEntity postEntity = new PostEntity(jsonObject, true);
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
