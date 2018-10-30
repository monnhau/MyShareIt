package com.shareit.api;

import android.content.Context;
import android.widget.RelativeLayout;

import com.shareit.base.BaseOkHttp;
import com.shareit.define.Define;
import com.shareit.interfaces.HttpCallback;
import com.shareit.util.LogUtil;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class NewsApi {
    public static void API_GET_LIST_CAT(Context ctx, HttpCallback httpCallback){
        //tao 1 Calnback nho vao BaseOkhttp
        BaseOkHttp baseOkHttp = new BaseOkHttp.Builder()
                .setHttpCallback(httpCallback)
                .setContext(ctx)
                .setWantDialogCancelable(true)
                .setWantShowDialog(true)
                .setMessage("Loading...")
                .setTitle("Loading...")
                .build();

        //Tao nhan vat chinh cua chung ta, la HttpClient
        OkHttpClient okHttpClient = BaseOkHttp.getOkHttpClient();
        String url = Define.API_GET_LIST_CAT;
        LogUtil.d("urlGetLIstcat", url);
        Request request = new Request.Builder()
                .url(url)
                .build();

        //dung OKHttpClient Tao new Call, with Request reques
        okHttpClient.newCall(request).enqueue(baseOkHttp);
    }
    public static void API_GET_LIST_POST(Context ctx,int catId, int limit, int offset, HttpCallback httpCallback){
        //Tao 1 callback dua tren doi tuong Baseokhttp
        BaseOkHttp baseOkHttp = new BaseOkHttp.Builder()
                .setHttpCallback(httpCallback)
                .setContext(ctx)
                .setWantShowDialog(true)
                .setWantDialogCancelable(true)
                .setMessage("Loading...")
                .setTitle("Loading...")
                .build();

        //Tao nhan vat chnh cua chung ta HttpClient
        OkHttpClient okHttpClient = BaseOkHttp.getOkHttpClient();
        String url = Define.API_GET_LIST_POST+"?cat_id="+catId+"&limit="+limit+"&offset="+offset;
        Request request = new Request.Builder()
                .url(url)
                .build();

        //Dua tren nhan vat chinh, ta dung no goi new Call, with Request
        okHttpClient.newCall(request).enqueue(baseOkHttp);
    }

    public static void API_GET_POST_DETAIL(Context ctx, String link, HttpCallback httpCallback ){
        //Tao 1 call back dua vao doi tuong baseOkhttp
        BaseOkHttp baseOkHttp = new BaseOkHttp.Builder()
                .setHttpCallback(httpCallback)
                .setContext(ctx)
                .setWantDialogCancelable(true)
                .setWantShowDialog(true)
                .setTitle("Loading...")
                .setMessage("Loading...")
                .build();
        //Tao nhan vat chinh OkHttpClient
        OkHttpClient okHttpClient = BaseOkHttp.getOkHttpClient();
        String url = Define.API_GET_POST_DETAIL+"?link="+link;
        Request request = new Request.Builder()
                .url(url)
                .build();

        //dung OkhttpCilent vua tao, de tao new Call , with request kem theo
        okHttpClient.newCall(request).enqueue(baseOkHttp);
    }
}
