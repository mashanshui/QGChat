package com.example.qgchat.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.qgchat.R;
import com.example.qgchat.util.AccessNetwork;

import org.greenrobot.eventbus.EventBus;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, String.valueOf(AccessNetwork.getNetworkState(context)), Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post(new NetWorkChange(AccessNetwork.getNetworkState(context)));
    }

    public class NetWorkChange {
        private int networkState;

        public int getNetworkState() {
            return networkState;
        }

        public void setNetworkState(int networkState) {
            this.networkState = networkState;
        }

        public NetWorkChange(int networkState) {
            this.networkState = networkState;
        }
    }
}
