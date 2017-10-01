package com.huwl.oracle.kylinremotecontrol.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huwl.oracle.kylinremotecontrol.R;
import com.huwl.oracle.kylinremotecontrol.activity.LoginActivity;
import com.huwl.oracle.kylinremotecontrol.activity.MainActivity;
import com.huwl.oracle.kylinremotecontrol.activity.OptionActivity;
import com.huwl.oracle.kylinremotecontrol.activity.RegisterActivity;
import com.huwl.oracle.kylinremotecontrol.beans.NetMessage;
import com.huwl.oracle.kylinremotecontrol.beans.Terminal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by aierxuan on 2017/9/18.
 */

public class MessageHandle {
    private static final String IP="120.24.244.103";
    private static final Integer PORT=5544;
    public static Socket server;
    public static Map<Class,Activity> activities=new HashMap<>();
    public static final Gson GSON=new Gson();

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
                    while(true){
                        NetMessage temp=null;
                        ObjectInputStream oin=null;
                        try {
                            oin=new ObjectInputStream(server.getInputStream());
                            Log.e("test","--------------------");
                            temp = (NetMessage)oin.readObject();
                            Log.e("test","++++++++++++++++++++");
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("test","输入流错误重来");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        final NetMessage m=temp;
                        new Thread(){
                            @Override
                            public void run() {
                                if(m.getForWhat()== NetMessage.LOGIN){
                                    handleLogin(m);
                                }else if(m.getForWhat()== NetMessage.REGISTER){
                                    handleRegister(m);
                                }else if(m.getForWhat()==NetMessage.AUTO_LOGIN){
                                    handleAutoLogin(m);
                                }else if(m.getForWhat()==NetMessage.ADD_CONTROLLABLE_TER){
                                    handleAddControllableTer(m);
                                }
                            }
                        }.start();


                    }
            }
        }.start();
    }
    public static void handleAddControllableTer(final NetMessage m){
        final OptionActivity oa= (OptionActivity) activities.get(OptionActivity.class);
        Toast.makeText(oa,"收到刷新listview的消息了",Toast.LENGTH_SHORT).show();
        Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {

                Set<Terminal> ts= (Set<Terminal>) m.getMap().get("otherTerminal");
                Toast.makeText(oa,GSON.toJson(ts),Toast.LENGTH_SHORT).show();
                try{
                    List<Map<String,Object>> list=oa.getListItem();
                    list.clear();
                    for(Terminal tempTer:ts){
                        if(tempTer.getSystemType().toLowerCase().contains("window")){
                            Map<String,Object> item=new HashMap();
                            item.put("terminalNameTV",tempTer.getName());
                            item.put("terminalTypeTV",tempTer.getSystemType());
                            item.put("radioImg", R.drawable.radio_unselect);
                            item.put("selected",false);
                            item.put("id",tempTer.getId());
                            list.add(item);
                        }
                    }
                    final ListView lv=oa.getTerminalsLV();
                    oa.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((SimpleAdapter)lv.getAdapter()).notifyDataSetChanged();
                            oa.findViewById(R.id.noTerminalRL).setVisibility(View.GONE);
                            oa.findViewById(R.id.terminalsLV).setVisibility(View.VISIBLE);
                        }
                    });
                }catch (final Exception e){
                    NetMessage em=new NetMessage();
                    em.getMap().put("e",e);
                    em.setForWhat(NetMessage.EXCEPTION);
                    em.send(server);
                }
            }
        };
        timer.schedule(task,3000);




    }
    public static void handleAutoLogin(final NetMessage m){
        MainActivity ma= (MainActivity) activities.get(MainActivity.class);
        SharedPreferences preferences=ma.getSharedPreferences("kylin_control",Context.MODE_PRIVATE);
        SharedPreferences.Editor e=preferences.edit();
        if(!((Boolean)m.getMap().get("isLogin"))){
            MainActivity.getTerminal().setUser(null);
            ma.startActivity(new Intent(ma,LoginActivity.class));
        }else{//登录成功了
            MainActivity.getTerminal().setUser(m.getUser());
            Intent intent=new Intent(ma,OptionActivity.class);
            Bundle bundle=new Bundle();
            Set<Terminal> set= (Set<Terminal>) m.getMap().get("otherTerminal");
            Type founderSetType = new TypeToken<HashSet<Terminal>>(){}.getType();
            bundle.putString("otherTerminal",GSON.toJson(set,founderSetType));
            intent.putExtras(bundle);
            ma.startActivity(intent);
        }
        e.putString("terminal",GSON.toJson(MainActivity.getTerminal()));
        e.commit();
        ma.finish();
        Log.e("test","test oa.finish()-->"+ma.isDestroyed());
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
                    Intent intent=new Intent(la,OptionActivity.class);
                    Bundle bundle=new Bundle();
                    Set<Terminal> set= (Set<Terminal>) m.getMap().get("otherTerminal");
                    Type founderSetType = new TypeToken<HashSet<Terminal>>(){}.getType();
                    bundle.putString("otherTerminal",GSON.toJson(set,founderSetType));
                    intent.putExtras(bundle);
                    la.startActivity(intent);

                    Toast.makeText(la,"登录成功" +
                            "",Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences=la.getSharedPreferences("kylin_control", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    MainActivity.getTerminal().setUser(m.getUser());
                    editor.putString("terminal",GSON.toJson(MainActivity.getTerminal()));
                    editor.commit();
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
