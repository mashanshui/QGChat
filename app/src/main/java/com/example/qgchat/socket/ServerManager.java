package com.example.qgchat.socket;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

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
    public HandlerThread sendUrgentDataThread = null;
    public Handler sendUrgentDataHandler = null;
    public HandlerThread sendMessageThread = null;
    public Handler sendMessageHandler = null;
    private boolean status = true;
    private static final ServerManager serverManager = new ServerManager();

    public static ServerManager getServerManager() {
        return serverManager;
    }

    private ServerManager() {
        receiveChatMsg = new ReceiveChatMsg();
        if (sendUrgentDataThread == null) {
            sendUrgentDataThread = new HandlerThread("sendUrgentDataThread");
            sendUrgentDataThread.start();
        }
        if (sendMessageThread == null) {
            sendMessageThread = new HandlerThread("sendMessageThread");
            sendMessageThread.start();
        }
        sendUrgentDataHandler = new Handler(sendUrgentDataThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    status=true;
                    while (status) {
                        try {
//                            Log.i("info", "run: mythread");
                            SystemClock.sleep(2000);
                            if (socket != null) {
                                socket.sendUrgentData(0xFF);
                            }
                        } catch (IOException e) {
                            status = false;
                            disconnect();
                        }
                    }
                }
            }
        };
        sendMessageHandler = new Handler(sendMessageThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
//                Log.i("info", "handleMessage: ");
                startSend(msg.obj.toString());
            }
        };
    }


    public void run() {
        while (true) {
            try {
                Log.i("info", "run: reply");
                connect();
                sendUrgentDataHandler.sendEmptyMessage(1);
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                EventBus.getDefault().post(new Connection(true));
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
//                Log.i("info", "run: catch");
            } finally {
                disconnect();
//                Log.i("info", "run: finally");
            }
        }
    }


    public void connect() {
        socket = null;
        while (socket == null) {
            try {
                Log.i("info", "run: conn");
                socket = new Socket(IP, 27777);
            } catch (IOException e) {
                SystemClock.sleep(1000);
            }
        }
    }

    public void disconnect() {
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
            setAccount(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void startSend(String msg) {
//        Log.i("info", "startSend: ");
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
//        Log.i("info", "sendMessage: ");
        Message tempMsg = sendMessageHandler.obtainMessage();
        tempMsg.obj = msg.toString();
        sendMessageHandler.sendMessage(tempMsg);
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

    public static class Connection{
        private boolean isConnection;

        public boolean isConnection() {
            return isConnection;
        }

        public void setConnection(boolean connection) {
            isConnection = connection;
        }

        public Connection(boolean isConnection) {
            this.isConnection = isConnection;
        }
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
