package com.shareit.shareit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.shareit.api.NewsApi;
import com.shareit.entity.PostEntity;
import com.shareit.interfaces.HttpCallback;
import com.shareit.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Request;

public class Detail_Activity extends AppCompatActivity {
    ImageView imgBack;
    Context context = this;
    TextView tvDetailTitle;
    WebView wvDetail;
    ToggleButton toggoBtnSound1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_);
        tvDetailTitle =(TextView) findViewById(R.id.tv_detail_title);
        wvDetail = findViewById(R.id.wv_post);
        toggoBtnSound1 =(ToggleButton) findViewById(R.id.toggo_btn_sound1);

        imgBack =(ImageView) findViewById(R.id.img_back);

        toggoBtnSound1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Bật âm thanh ==> làm gì đó
                }else{
                    //Tắt âm thanh ==> Làm gì đó
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        PostEntity postEntity =(PostEntity) bundle.getSerializable("post");
        tvDetailTitle.setText(postEntity.getTitle());

        String link = postEntity.getLink();

        LogUtil.d("linkDetail", link);

        NewsApi.API_GET_POST_DETAIL(context, link, new HttpCallback() {
            @Override
            public void onSucess(final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            PostEntity postEntity1 = new PostEntity(jsonObject);
                            String data = "<html><head><style>*{max-width:100%}</style></head><body>"+postEntity1.getContent()+"</body></html>";
                            wvDetail.loadData(data, "text/html; charset=utf-8", "utf-8");
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
                        Toast.makeText(context, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }


}
