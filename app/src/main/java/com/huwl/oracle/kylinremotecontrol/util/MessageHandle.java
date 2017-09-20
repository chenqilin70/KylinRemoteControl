package com.huwl.oracle.kylinremotecontrol.util;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.huwl.oracle.kylinremotecontrol.activity.LoginActivity;
import com.huwl.oracle.kylinremotecontrol.activity.OptionActivity;
import com.huwl.oracle.kylinremotecontrol.activity.RegisterActivity;
import com.huwl.oracle.kylinremotecontrol.beans.NetMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by aierxuan on 2017/9/18.
 */

public class MessageHandle {
    private static final String IP="27.22.95.52";
    private static final Integer PORT=5544;
    public static Socket server;

    static {
        try {
            server=new Socket(IP,PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void listenOne(final Activity activity){
        new Thread(){
            @Override
            public void run() {
                try {
                        final NetMessage m= (NetMessage) new ObjectInputStream(server.getInputStream()).readObject();
                        if(m.getForWhat()== NetMessage.LOGIN){
                            final boolean flag=(Boolean)m.getMap().get("isLogin");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(flag){
                                        activity.startActivity(new Intent(activity,OptionActivity.class));
                                        Toast.makeText(activity,"登录成功" +
                                                "",Toast.LENGTH_SHORT).show();
                                        activity.finish();
                                    }else{
                                        Toast.makeText(activity,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                                        LoginActivity la=((LoginActivity)activity);
                                        la.enableLoginBtn();
                                        la.getUsername_et().setEnabled(true);
                                        la.getPassword_et().setEnabled(true);
                                    }
                                }
                            });
                        }else if(m.getForWhat()== NetMessage.REGISTER){
                            final boolean flag=(Boolean)m.getMap().get("isRegister");
                                activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RegisterActivity ra=((RegisterActivity)activity);
                                    if(flag){
                                        String username=ra.getRegister_username_et().getText().toString();
                                        String password=ra.getRegister_password_et().getText().toString();
                                        Intent intent=new Intent();
                                        intent.putExtra("username",username).putExtra("password",password);
                                        activity.setResult(1,intent);
                                        activity.finish();
                                    }else{
                                        Toast.makeText(activity,"该用户名已被使用",Toast.LENGTH_SHORT).show();
                                        ra.getRegister_username_et().setEnabled(true);
                                        ra.getRegister_password_et().setEnabled(true);
                                    }

                                }
                            });

                        }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.start();
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


}
