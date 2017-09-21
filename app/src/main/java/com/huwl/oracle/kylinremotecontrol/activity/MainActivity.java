package com.huwl.oracle.kylinremotecontrol.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huwl.oracle.kylinremotecontrol.R;
import com.huwl.oracle.kylinremotecontrol.beans.NetMessage;
import com.huwl.oracle.kylinremotecontrol.beans.Terminal;
import com.huwl.oracle.kylinremotecontrol.beans.User;
import com.huwl.oracle.kylinremotecontrol.util.MessageHandle;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

public class MainActivity extends Activity {
    private Animation animation;
    private ImageView text_icon;
    public static Terminal terminal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        addToContainer();
        initTerminal();
        autoLogin();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();


    }

    private void addToContainer() {
        new Thread(){
            @Override
            public void run() {
                MessageHandle.getActivities().put(MainActivity.class,MainActivity.this);
                MessageHandle.listen();
            }
        }.start();
    }

    private void autoLogin() {
        /*if(terminal.getUser()!=null){
            new Thread(){
                @Override
                public void run() {
                    NetMessage m=new NetMessage();
                    m.setUser(terminal.getUser());
                    m.setForWhat(NetMessage.LOGIN);
                    m.getMap().put("terminal",terminal);
                    m.send(MessageHandle.getServer());
                }
            }.start();
            startActivity(new Intent(this,OptionActivity.class));
        }*/
        Log.e("test",""+(terminal.getUser()==null));
        if(terminal.getUser()!=null){//已经登陆
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initTerminal() {
        SharedPreferences sharedPreferences = getSharedPreferences("kylin_control", Context.MODE_PRIVATE); //私有数据
        String result=sharedPreferences.getString("terminal",null);
        Gson gson=new Gson();
        if(result==null){
            Log.e("test","preferences中没有terminal");
            SharedPreferences.Editor editor=sharedPreferences.edit();
            Terminal t=new Terminal(UUID.randomUUID().toString(),Build.BRAND, "Android"+Build.VERSION.RELEASE);
            result=gson.toJson(t);
            editor.putString("terminal",result);
            editor.commit();

        }else{
            Log.e("test","preferences中有terminal"+result);
        }
        terminal=gson.fromJson(result,Terminal.class);
    }

    private void initUI() {
        animation=AnimationUtils.loadAnimation(this,R.anim.anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        text_icon=findViewById(R.id.text_icon);
        text_icon.setAnimation(animation);
    }

    public static Terminal getTerminal() {
        return terminal;
    }
}
