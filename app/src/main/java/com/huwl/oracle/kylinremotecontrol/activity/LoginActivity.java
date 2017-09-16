package com.huwl.oracle.kylinremotecontrol.activity;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huwl.oracle.kylinremotecontrol.R;

public class LoginActivity extends Activity implements TextWatcher {
    private EditText username_et,password_et;
    private ImageView loginBtn;
    private TextView login_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_et=findViewById(R.id.username_et);
        password_et=findViewById(R.id.password_et);
        loginBtn=findViewById(R.id.loginBtn);
        login_text=findViewById(R.id.login_text);
        username_et.addTextChangedListener(this);
        password_et.addTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        if((!"".equals(username_et.getText().toString())) && (!"".equals(password_et.getText().toString()))){
            loginBtn.setImageResource(R.drawable.login_btn_active);
            login_text.setTextColor(Color.rgb(255,255,255));
        }else{
            loginBtn.setImageResource(R.drawable.login_btn_unable);
            login_text.setTextColor(Color.rgb(167,168,169));
        }
    }
}
