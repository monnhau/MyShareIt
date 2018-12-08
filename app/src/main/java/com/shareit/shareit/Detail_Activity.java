package com.shareit.shareit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.shareit.adapter.PostAdapter;
import com.shareit.api.NewsApi;
import com.shareit.entity.PostEntity;
import com.shareit.interfaces.HttpCallback;
import com.shareit.util.LogUtil;
import com.shareit.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Request;

public class Detail_Activity extends AppCompatActivity {
    ImageView imgBack;
    Context context = this;
    TextView tvDetailTitle;
    WebView wvDetail;
    Button mBtnSound;
    private TextToSpeech mTTs;
    private boolean isSound = true;
    private boolean isSpeaker = true;
    private ImageView mImvSpeaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_);
        Bundle bundle = getIntent().getExtras();
        final PostEntity postEntity = (PostEntity) bundle.getSerializable("post");
        boolean isShareItPostType = bundle.getBoolean("isShareItPostType");
        isSound = bundle.getBoolean("isSound");

        mImvSpeaker = findViewById(R.id.imv_speaker);
        tvDetailTitle = (TextView) findViewById(R.id.tv_detail_title);
        wvDetail = findViewById(R.id.wv_post);
        mBtnSound = findViewById(R.id.btn_sound);

        if (isSound) {//bat
            UnMuteAudio();
            mBtnSound.setText("Tắt tiếng");
        } else {
            MuteAudio();
            mBtnSound.setText("Bật tiếng");
        }
        imgBack = (ImageView) findViewById(R.id.img_back);

        mTTs = new TextToSpeech(Detail_Activity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTs.setLanguage(new Locale("vi_VN"));

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        mBtnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSound) {
                    MuteAudio();
                    isSound = false;
                    mBtnSound.setText("Bật tiếng");
                    isSpeaker = true;
                    mImvSpeaker.setImageResource(R.drawable.ic_volume_off);
                } else {
                    UnMuteAudio();
                    isSound = true;
                    mBtnSound.setText("Tắt tiếng");
                }
                mTTs.stop();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTTs.stop();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("isSound", isSound);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


        tvDetailTitle.setText(postEntity.getTitle());
        if (isShareItPostType == true) {
            int idPost = postEntity.getId();
            NewsApi.API_GET_POST_DETAIL_SHAREIT(context, idPost, new HttpCallback() {
                @Override
                public void onSucess(final String s) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                PostEntity postEntity1 = new PostEntity(jsonObject, true);
                                String data = "<html><head><style>*{max-width:100%}</style></head><body>" + postEntity1.getContent() + "</body></html>";
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
        } else {
            String link = postEntity.getLink();
            NewsApi.API_GET_POST_DETAIL(context, link, new HttpCallback() {
                @Override
                public void onSucess(final String s) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                PostEntity postEntity1 = new PostEntity(jsonObject);
                                String data = "<html><head><style>*{max-width:100%}</style></head><body>" + postEntity1.getContent() + "</body></html>";
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

        mImvSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSound) {
                    if (isSpeaker) {
                        isSpeaker = false;
                        mTTs.speak(postEntity.getText(), TextToSpeech.QUEUE_FLUSH, null);
                        Toast.makeText(Detail_Activity.this, postEntity.getText(), Toast.LENGTH_SHORT).show();
                        mImvSpeaker.setImageResource(R.drawable.ic_volume_up);
                    } else {
                        mTTs.stop();
                        mImvSpeaker.setImageResource(R.drawable.ic_volume_off);
                        isSpeaker = true;
                    }
                } else {
                    Toast.makeText(context, "Vui lòng bật tiếng", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        mTTs.stop();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("isSound", isSound);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void MuteAudio() {
        AudioManager mAlramMAnager = (AudioManager) getSystemService(this.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
        } else {
            mAlramMAnager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_ALARM, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_RING, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        }
    }

    public void UnMuteAudio() {
        AudioManager mAlramMAnager = (AudioManager) getSystemService(this.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_UNMUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0);
        } else {
            mAlramMAnager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_ALARM, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_RING, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        }
    }


}
