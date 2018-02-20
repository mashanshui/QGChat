package com.example.qgchat.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.example.qgchat.util.EventBean;
import com.example.qgchat.util.StringUtil;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

public class QGService extends Service implements MediaPlayer.OnCompletionListener,MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnPreparedListener{
    private QGBinder mBinder = new QGBinder();
    public static String account = null;
    public String password = null;
    private MediaPlayer mediaPlayer = null;

    public QGService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        EventBus.getDefault().register(this);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnPreparedListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(EventBean.MusicUrl musicUrl) {
        if (!StringUtil.isEmpty(musicUrl.getMusicUrl())) {
            try {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                mediaPlayer.reset();
                mediaPlayer.setDataSource(musicUrl.getMusicUrl());
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(EventBean.Playing playing) {
        if (playing.isPlaying()) {
            if (mediaPlayer.isPlaying()) {
                return;
            } else {
                mediaPlayer.start();
            }
        } else {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                return;
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public class QGBinder extends Binder {

    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
        }
        @Override
        public void onContactDeleted(final String username) {
        }
        @Override
        public void onContactInvited(String username, String reason) {
        }
        @Override
        public void onFriendRequestAccepted(String username) {
        }
        @Override
        public void onFriendRequestDeclined(String username) {}
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
