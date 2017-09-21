package com.huwl.oracle.kylinremotecontrol.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
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

public class RegisterActivity extends Activity implements TextWatcher, View.OnClickListener {
    private EditText register_username_et,register_password_et;
    private ImageView registerBtn;
    private TextView register_text,returnToLogin;
    private RelativeLayout registerBtn_lay;

    public EditText getRegister_username_et() {
        return register_username_et;
    }

    public void setRegister_username_et(EditText register_username_et) {
        this.register_username_et = register_username_et;
    }

    public EditText getRegister_password_et() {
        return register_password_et;
    }

    public void setRegister_password_et(EditText register_password_et) {
        this.register_password_et = register_password_et;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        addToContainer();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_username_et=findViewById(R.id.register_username_et);
        register_password_et=findViewById(R.id.register_password_et);
        registerBtn=findViewById(R.id.registerBtn);
        register_text=findViewById(R.id.register_text);
        returnToLogin=findViewById(R.id.returnToLogin);
        registerBtn_lay=findViewById(R.id.registerBtn_lay);
        register_username_et.addTextChangedListener(this);
        register_password_et.addTextChangedListener(this);
        returnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.this.finish();
            }
        });
        BarUtils.setWindowBarColor(this,R.color.colorBack,BarUtils.ALL);

    }

    private void addToContainer() {
        new Thread(){
            @Override
            public void run() {
                MessageHandle.getActivities().put(RegisterActivity.class,RegisterActivity.this);
            }
        }.start();
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        String username=register_username_et.getText().toString();
        String pwd=register_password_et.getText().toString();
        if(username.length()==0 || username.length()>20 || pwd.length()<6 || pwd.length()>20){
            unableLoginBtn();
        }else{
            enableLoginBtn();
        }
    }

    private void unableLoginBtn(){
        registerBtn_lay.setOnClickListener(null);
        registerBtn.setImageResource(R.drawable.login_btn_unable);
        register_text.setTextColor(Color.rgb(167,168,169));
    }
    public void enableLoginBtn(){
        registerBtn_lay.setOnClickListener(this);
        registerBtn.setImageResource(R.drawable.login_btn_active);
        register_text.setTextColor(Color.rgb(255,255,255));
    }


    @Override
    public void onClick(View view) {
        final String username=register_username_et.getText().toString();
        final String pwd=register_password_et.getText().toString();
        if(username.length()==0 || username.length()>20){
            Toast.makeText(this,"用户名长度必须在0-20个字符之间",Toast.LENGTH_SHORT).show();
        }
        if(pwd.length()<6 || pwd.length()>20){
            Toast.makeText(this,"密码长度必须在6-20个字符之间",Toast.LENGTH_SHORT).show();
        }

        new Thread(){
            @Override
            public void run() {
                NetMessage m=new NetMessage();
                m.setUser(new User(username,pwd));
                m.setForWhat(NetMessage.REGISTER);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        register_username_et.setEnabled(false);
                        register_password_et.setEnabled(false);
                        unableLoginBtn();
                    }
                });
                m.send(MessageHandle.getServer());

            }
        }.start();

    }
}
