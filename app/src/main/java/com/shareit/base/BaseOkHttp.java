package com.shareit.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.shareit.interfaces.HttpCallback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public abstract class BaseOkHttp implements Callback {
    private static OkHttpClient okHttpClient = null;
    public static OkHttpClient getOkHttpClient(){
        if(okHttpClient == null){
            okHttpClient = new OkHttpClient
                    .Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();
        }

        return okHttpClient;
    }

    private boolean wantDialogCannelable =  true;
    private boolean wantShowDialog =  true;
    private Context context;
    private ProgressDialog progress;
    private String tittle = "";
    private String message = "Message...";
    private HttpCallback httpCallback;

    protected BaseOkHttp(){

    }

    public void start(){
        if(this.context != null){
            progress = new ProgressDialog(this.context);
            progress.setCancelable(wantDialogCannelable);
            progress.setMessage(message);

            if(null != tittle && TextUtils.isEmpty(tittle)){
                progress.setTitle(tittle);
            }

        }

        if(progress != null && wantShowDialog){
            progress.show();
        }

        if(httpCallback != null){
            httpCallback.onStart();
        }
    }


    @Override
    public void onFailure(Call call, IOException e) {
        dissmisDiaLog();
        if(httpCallback != null){
            httpCallback.onFailure(call.request(), e);
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        dissmisDiaLog();

        if(response.isSuccessful()){
            if(httpCallback != null){
                String s = response.body().string();
                httpCallback.onSucess(s);
            }
        }else{
            onFailure(null, null);
        }
    }

    private void dissmisDiaLog(){
        if(progress != null && progress.isShowing()){
            try{
                if(context instanceof Activity){
                    if(((Activity)context).isFinishing()){
                        return;
                    }
                }
                progress.dismiss();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean isWantDialogCannelable(){
        return wantDialogCannelable;
    }

    public void setWantDialogCannelable(boolean wantDialogCannelable){
        this.wantDialogCannelable = wantDialogCannelable;
    }

    public boolean isWantShowDialog(){
        return wantShowDialog;
    }

    public void setHttpCallback(HttpCallback httpCallback){
        this.httpCallback = httpCallback;
    }

    public BaseOkHttp setWantShowDialog(boolean wantShowDialog){
        this.wantShowDialog = wantShowDialog;
        return null;
    }

    public Context getContext(){
        return context;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public ProgressDialog getProgress() {
        return progress;
    }

    public void setProgress(ProgressDialog progress){
        this.progress = progress;

    }

    public String getTittle(){
        return tittle;
    }

    public void setTittle(String tittle){
        this.tittle = tittle;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public static class Builder{
        private BaseOkHttp baseOkHttp = new BaseOkHttp() {
        };


        public Builder setWantShowDialog(boolean wantShowDialog){
            baseOkHttp.wantShowDialog = wantShowDialog;
            return this;
        }

        public Builder setHttpCallback(HttpCallback httpCallback){
            this.baseOkHttp.httpCallback = httpCallback;
            return this;
        }

        public Builder setContext(Context ctx){
            baseOkHttp.context = ctx;
            return Builder.this;
        }

        public Builder setWantDialogCancelable(boolean wantDialogCancelable){
            baseOkHttp.wantDialogCannelable = wantDialogCancelable;
            return Builder.this;
        }

        public Builder setTitle(String title){
            baseOkHttp.tittle = title;
            return Builder.this;
        }

        public Builder setMessage(String message){
            baseOkHttp.message = message;
            return Builder.this;
        }

        public BaseOkHttp build(){
            baseOkHttp.start();
            return baseOkHttp;
        }
    }
}
