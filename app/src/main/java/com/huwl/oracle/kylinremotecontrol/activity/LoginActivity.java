package com.huwl.oracle.kylinremotecontrol.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huwl.oracle.kylinremotecontrol.R;
import com.huwl.oracle.kylinremotecontrol.beans.NetMessage;
import com.huwl.oracle.kylinremotecontrol.beans.User;
import com.huwl.oracle.kylinremotecontrol.util.BarUtils;
import com.huwl.oracle.kylinremotecontrol.util.MessageHandle;

public class LoginActivity extends Activity implements TextWatcher, View.OnClickListener {
    private EditText username_et,password_et;
    private ImageView loginBtn;
    private TextView login_text,goToRegister;
    private RelativeLayout loginBtn_lay;

    public EditText getUsername_et() {
        return username_et;
    }

    public void setUsername_et(EditText username_et) {
        this.username_et = username_et;
    }

    public EditText getPassword_et() {
        return password_et;
    }

    public void setPassword_et(EditText password_et) {
        this.password_et = password_et;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_et=findViewById(R.id.username_et);
        password_et=findViewById(R.id.password_et);
        loginBtn=findViewById(R.id.loginBtn);
        goToRegister=findViewById(R.id.goToRegister);
        loginBtn_lay=findViewById(R.id.loginBtn_lay);
        login_text=findViewById(R.id.login_text);
        username_et.addTextChangedListener(this);
        password_et.addTextChangedListener(this);
        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        BarUtils.setWindowBarColor(this,R.color.colorBack,BarUtils.ALL);
        loginBtn_lay.setOnClickListener(this);


    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        if((!"".equals(username_et.getText().toString())) && (!"".equals(password_et.getText().toString()))){
            enableLoginBtn();
        }else{
            unableLoginBtn();
        }
    }
    private void unableLoginBtn(){
        loginBtn_lay.setOnClickListener(null);
        loginBtn.setImageResource(R.drawable.login_btn_unable);
        login_text.setTextColor(Color.rgb(167,168,169));
    }
    public void enableLoginBtn(){
        loginBtn_lay.setOnClickListener(this);
        loginBtn.setImageResource(R.drawable.login_btn_active);
        login_text.setTextColor(Color.rgb(255,255,255));
    }

    @Override
    public void onClick(View view) {
        final String username=username_et.getText().toString();
        final String pwd=password_et.getText().toString();

        new Thread(){
            @Override
            public void run() {
                NetMessage m=new NetMessage();
                m.setUser(new User(username,pwd));
                m.setForWhat(NetMessage.LOGIN);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        username_et.setEnabled(false);
                        password_et.setEnabled(false);
                        unableLoginBtn();
                    }
                });

                MessageHandle mh=new MessageHandle();
                mh.listenOne(LoginActivity.this);
                m.send(MessageHandle.getServer());

            }
        }.start();

    }
}
