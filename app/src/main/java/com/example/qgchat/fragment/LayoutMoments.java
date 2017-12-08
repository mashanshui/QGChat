package com.example.qgchat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qgchat.R;
import com.example.qgchat.adapter.MomentsRecycleAdapter;
import com.example.qgchat.bean.ShowMusicItem;
import com.example.qgchat.bean.UserMoments;
import com.example.qgchat.broadcast.NetworkChangeReceiver;
import com.example.qgchat.util.AccessNetwork;
import com.example.qgchat.util.EventBean;
import com.example.qgchat.util.GridItemDecoration;
import com.example.qgchat.util.MusicUtil;
import com.example.qgchat.util.VoiceUtil;
import com.example.qgchat.view.VoiceButton;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.security.KeyRep;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class LayoutMoments extends Fragment {
    @BindView(R.id.momentsRecycleView)
    RecyclerView momentsRecycleView;
    Unbinder unbinder;
    @BindView(R.id.voiceButton)
    VoiceButton voiceButton;
    private View rootView;
    private List<ShowMusicItem> showMusicItems = new ArrayList<>();
    private MomentsRecycleAdapter adapter;
    private static final String TAG = "info";
    private SpeechRecognizer mIat;

    public LayoutMoments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout_moments, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mIat = SpeechRecognizer.createRecognizer(getActivity(), null);
        voiceButton.setOnLongClickListener(new MyLongClickListener());
        initAdapter();
        return rootView;
    }

    private class MyLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            //new VoiceUtil(getActivity()).playBeepSoundAndVibrate();
            int ret = mIat.startListening(mRecognizerListener);
            if (ret != ErrorCode.SUCCESS) {
                Log.i(TAG, "onLongClick: 识别失败");
            } else {
                Log.i(TAG, "onLongClick: 开始说话");
            }
            return false;
        }
    }
    private void initAdapter() {
        adapter = new MomentsRecycleAdapter(showMusicItems);
        momentsRecycleView.setAdapter(adapter);
        momentsRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        momentsRecycleView.addItemDecoration(new GridItemDecoration(getActivity(),R.drawable.recycleview_divider));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(EventBean.MusicListMessage listMessage) {
        showMusicItems = listMessage.getShowMusicItems();
        adapter.notifyDataSetChanged();
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            Log.i(TAG, "onBeginOfSpeech: ");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            Log.i(TAG, "onError: 识别失败");
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.i(TAG, results.getResultString());
            String keyword=printResult(results);
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            //Log.i(TAG, "onBeginOfSpeech: "+"当前正在说话，音量大小：" + volume);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private String printResult(RecognizerResult results) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(results.getResultString());
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
