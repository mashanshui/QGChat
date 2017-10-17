package com.example.qgchat.socket;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;

import com.example.qgchat.util.HttpUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerManager extends Thread {
    private static final String TAG = "ServerManager";
    private static final String IP = "139.199.158.151";
    public Socket socket = null;
    private String account = null;
    private String message = null;
    private BufferedReader bufferedReader = null;
    private BufferedWriter bufferedWriter = null;
    public ReceiveChatMsg receiveChatMsg;
    public HandlerThread handlerThread = null;
    public Handler handler = null;
    private static final ServerManager serverManager = new ServerManager();

    public static ServerManager getServerManager() {
        return serverManager;
    }

    private ServerManager() {
        receiveChatMsg = new ReceiveChatMsg();
        if (handlerThread == null) {
            handlerThread = new HandlerThread("sendMessageThread");
            handlerThread.start();
        }
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                startSend(msg.obj.toString());
            }
        };
    }

    public void run() {
        socket = null;
        while (socket == null) {
            try {
                socket = new Socket(IP, 27777);
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            } catch (IOException e) {
                SystemClock.sleep(1000);
                e.printStackTrace();
            }
        }

        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            String m = null;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.equals("-1")) {
                    m += line;
                } else {
//                    Log.i("TAG", "receive : " + m);
                    receiveChatMsg.delMessage(m);
                    message = m;
                    m = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (socket != null) {
                    socket.close();
                }
//                Log.i("info", "连接断开------");
                setAccount(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void startSend(String msg) {
        try {
            while (socket == null) ;
            if (bufferedWriter != null) {
                bufferedWriter.write(msg + "\n");
                bufferedWriter.flush();
                bufferedWriter.write("-1\n");
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        Message tempMsg = handler.obtainMessage();
        tempMsg.obj = msg.toString();
        handler.sendMessage(tempMsg);
    }

    public String getMessage() {
        for (int i = 0; i < 5; i++) {
            if (message != null) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
