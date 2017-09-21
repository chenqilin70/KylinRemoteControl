package com.huwl.oracle.kylinremotecontrol.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huwl.oracle.kylinremotecontrol.activity.LoginActivity;
import com.huwl.oracle.kylinremotecontrol.activity.MainActivity;
import com.huwl.oracle.kylinremotecontrol.activity.OptionActivity;
import com.huwl.oracle.kylinremotecontrol.activity.RegisterActivity;
import com.huwl.oracle.kylinremotecontrol.beans.NetMessage;
import com.huwl.oracle.kylinremotecontrol.beans.Terminal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aierxuan on 2017/9/18.
 */

public class MessageHandle {
    private static final String IP="27.29.145.7";
    private static final Integer PORT=5544;
    public static Socket server;
    public static Map<Class,Activity> activities=new HashMap<>();

    static {
        try {
            server=new Socket(IP,PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void listen(){
        new Thread(){
            @Override
            public void run() {
                try {
                    while(true){
                        final NetMessage m= (NetMessage) new ObjectInputStream(server.getInputStream()).readObject();
                        new Thread(){
                            @Override
                            public void run() {
                                if(m.getForWhat()== NetMessage.LOGIN){
                                    handleLogin(m);
                                }else if(m.getForWhat()== NetMessage.REGISTER){
                                    handleRegister(m);
                                }
                            }
                        }.start();


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public static void handleRegister(final NetMessage m){
        final RegisterActivity ra= (RegisterActivity) activities.get(RegisterActivity.class);
        final boolean flag=(Boolean)m.getMap().get("isRegister");
        ra.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(flag){
                    String username=ra.getRegister_username_et().getText().toString();
                    String password=ra.getRegister_password_et().getText().toString();
                    Intent intent=new Intent();
                    intent.putExtra("username",username).putExtra("password",password);
                    ra.setResult(1,intent);
                    ra.finish();
                }else{
                    Toast.makeText(ra,"该用户名已被使用",Toast.LENGTH_SHORT).show();
                    ra.getRegister_username_et().setEnabled(true);
                    ra.getRegister_password_et().setEnabled(true);
                }
            }
        });
    }
    public static void handleLogin(final NetMessage m){
        final boolean flag=(Boolean)m.getMap().get("isLogin");
        final LoginActivity la= (LoginActivity) activities.get(LoginActivity.class);
        la.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(flag){
                    la.startActivity(new Intent(la,OptionActivity.class));
                    Toast.makeText(la,"登录成功" +
                            "",Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences=la.getSharedPreferences("kylin_control", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    Gson gson=new Gson();
                    MainActivity.getTerminal().setUser(m.getUser());
                    editor.putString("terminal",gson.toJson(MainActivity.getTerminal()));
                    la.finish();
                }else{
                    Toast.makeText(la,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                    la.enableLoginBtn();
                    la.getUsername_et().setEnabled(true);
                    la.getPassword_et().setEnabled(true);
                }
            }
        });
    }

    public static String getIP() {
        return IP;
    }

    public static Integer getPORT() {
        return PORT;
    }

    public static Socket getServer() {
        return server;
    }

    public static void setServer(Socket server) {
        MessageHandle.server = server;
    }

    public static Map<Class, Activity> getActivities() {
        return activities;
    }
}
