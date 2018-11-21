package com.shareit.shareit;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shareit.api.NewsApi;
import com.shareit.define.Define;
import com.shareit.entity.MenuEntity;
import com.shareit.entity.MenuEntityShareIt;
import com.shareit.entity.PostEntity;
import com.shareit.interfaces.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class FlashActivity extends AppCompatActivity {
    TextView tvLoading, tvLoi, tvPhienban;
    ProgressBar pgLoading;
    Context context = this;
    List<PostEntity> postEntities = new ArrayList<>();
    List<MenuEntityShareIt> menuEntities = new ArrayList<>();
    List<MenuEntityShareIt> menuEntitiesShareItChildAll = new ArrayList<>();
    List<MenuEntity> menuEntitiesItcNews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        tvLoading = (TextView) findViewById(R.id.tv_loading);
        tvLoi = (TextView) findViewById(R.id.tv_loi_internet);
        tvPhienban = (TextView) findViewById(R.id.tv_phien_ban);
        pgLoading = (ProgressBar) findViewById(R.id.pg_loading);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            tvPhienban.setText("Phiên bản:"+packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        LayDanhSachTinTucDauTien();

    }

    class XuLiLoiKetNoi implements Runnable{
        @Override
        public void run() {
            postEntities.clear();
            menuEntities.clear();
            menuEntitiesShareItChildAll.clear();
            tvLoading.setVisibility(View.INVISIBLE);
            pgLoading.setVisibility(View.INVISIBLE);
            tvLoi.setVisibility(View.VISIBLE);
            tvLoi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvLoading.setVisibility(View.VISIBLE);
                            pgLoading.setVisibility(View.VISIBLE);
                            tvLoi.setVisibility(View.INVISIBLE);
                        }
                    });
                    LayDanhSachTinTucDauTien();
                }
            });
        }
    }

    public void LayDanhSachDanhMucItcRss1Cap(){
        MenuEntity thoisu = new MenuEntity(1, "Thời sự", true);
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

        menuEntitiesItcNews.add(thoisu);
        menuEntitiesItcNews.add(internet);
        menuEntitiesItcNews.add(phanmem);
        menuEntitiesItcNews.add(kinhdoanh);
        menuEntitiesItcNews.add(thitruong);
        menuEntitiesItcNews.add(game);
        menuEntitiesItcNews.add(congnghe360);
        menuEntitiesItcNews.add(cntt);
        menuEntitiesItcNews.add(phancung);
        menuEntitiesItcNews.add(thegioiso);
        menuEntitiesItcNews.add(didong);
        menuEntitiesItcNews.add(khoinghiep);
        menuEntitiesItcNews.add(gocdoanhnghiep);
        menuEntitiesItcNews.add(otoxemay);
        menuEntitiesItcNews.add(videohot);

        //Toast.makeText(context, "kiemtra4"+menuEntitiesItcNews.toString(), Toast.LENGTH_SHORT).show();

    }

    public void LayDanhSachDanhMucConTuBienTap(){
        NewsApi.API_GET_LIST_CHILD_CAT_SHAREIT_FLASH(context, new HttpCallback() {
            @Override
            public void onSucess(final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(s);
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject =(JSONObject) jsonArray.get(i);
                                MenuEntityShareIt menuEntityShareIt = new MenuEntityShareIt(jsonObject, false);
                                menuEntitiesShareItChildAll.add(menuEntityShareIt);
                            }
                            //Toast.makeText(context, "kiemtra3"+menuEntitiesShareItChildAll.toString(), Toast.LENGTH_SHORT).show();
                            LayDanhSachDanhMucItcRss1Cap();
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("posts", (Serializable) postEntities);
                            intent.putExtra("parentCats", (Serializable) menuEntities);
                            intent.putExtra("childCats", (Serializable) menuEntitiesShareItChildAll);
                            intent.putExtra("menuEntitiesItcNews", (Serializable) menuEntitiesItcNews);
                            startActivity(intent);
                            LayDanhSachDanhMucItcRss1Cap();
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
                runOnUiThread(new XuLiLoiKetNoi());
            }
        });
    }


    public void LayDanhSachDanhMucChaTuBienTap(){
        NewsApi.API_GET_LIST_PARENT_CAT_SHAREIT_FLASH(context, new HttpCallback() {
            @Override
            public void onSucess(final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(s);
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject =(JSONObject) jsonArray.get(i);
                                MenuEntityShareIt menuEntityShareIt = new MenuEntityShareIt(jsonObject, false);
                                menuEntities.add(menuEntityShareIt);
                            }
                            //Toast.makeText(context, "kiemtra2"+menuEntities.toString(), Toast.LENGTH_SHORT).show();
                            LayDanhSachDanhMucConTuBienTap();

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
                runOnUiThread(new XuLiLoiKetNoi());
            }
        });
    }

    public void LayDanhSachTinTucDauTien(){
        NewsApi.API_GET_LIST_POST_FLASH(context, 1, Define.paginateNumRss, 0, new HttpCallback() {
            @Override
            public void onSucess(final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(s);
                            for (int i=0; i< jsonArray.length(); ++i){
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                PostEntity postEntity = new PostEntity(jsonObject);
                                postEntities.add(postEntity);
                            }
                            //Toast.makeText(context, "kiemtra1"+postEntities.toString(), Toast.LENGTH_SHORT).show();
                            LayDanhSachDanhMucChaTuBienTap();
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
                runOnUiThread(new XuLiLoiKetNoi());
            }
        });
    }
}
