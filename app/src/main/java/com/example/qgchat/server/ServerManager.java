package com.example.qgchat.server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerManager extends Thread {
    private static final String TAG = "ServerManager";
    private static final String IP = "182.254.151.61";
    public Socket socket=null;
    private String account=null;
    private String message = null;
    private BufferedReader bufferedReader=null;
    private BufferedWriter bufferedWriter=null;
    public ReceiveChatMsg receiveChatMsg;
    private static final ServerManager serverManager = new ServerManager();

    public static ServerManager getServerManager() {
        return serverManager;
    }

    private ServerManager() {
        receiveChatMsg = new ReceiveChatMsg();
    }

    public void run() {
        try {
            socket = new Socket(IP, 27777);
            bufferedReader =  new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

            String m = null;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.equals("-1")) {
                    m += line;
                } else {
                    Log.i("TAG", "receive : " + m);
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
                setAccount(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void sendMessage(String msg) {
        try {
            while (socket == null) ;
            if (bufferedWriter != null) {
//                Log.d("TAG", "send : " + msg);
                bufferedWriter.write(msg + "\n");
                bufferedWriter.flush();
                bufferedWriter.write("-1\n");
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
