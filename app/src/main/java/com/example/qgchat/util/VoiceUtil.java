package com.example.qgchat.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Vibrator;

import com.example.qgchat.R;

/**
 * Created by Administrator on 2017/12/7.
 */

public class VoiceUtil {
    private SoundPool pool;
    private Context context;
    private int sourceid;

    public VoiceUtil(Context context) {
        this.context = context;
        pool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        //载入音频流，返回在池中的id
        sourceid = pool.load(context, R.raw.hint,0);
    }

    private boolean isSlient(){
        AudioManager audioService = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            return false;
        }
        return true;
    }

    private void startSystemAlarm() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (notification == null) return;
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }

    public void startVibrate() {
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        //震动一次
        vibrator.vibrate(300);
        //第一个参数，指代一个震动的频率数组。每两个为一组，每组的第一个为等待时间，第二个为震动时间。
        //        比如  [2000,500,100,400],会先等待2000毫秒，震动500，再等待100，震动400
        //第二个参数，repest指代从 第几个索引（第一个数组参数） 的位置开始循环震动。
        //会一直保持循环，我们需要用 vibrator.cancel()主动终止
        //vibrator.vibrate(new long[]{300,500},0);
    }

    public void playBeepSoundAndVibrate() {
        startVibrate();
        if (!isSlient()) {
            //播放音频，第二个参数为左声道音量;第三个参数为右声道音量;第四个参数为优先级；第五个参数为循环次数，0不循环，-1循环;第六个参数为速率，速率    最低0.5最高为2，1代表正常速度
            pool.play(sourceid, 1, 1, 0, 0, 1);
        }
    }
}
