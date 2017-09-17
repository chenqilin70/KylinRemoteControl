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
import android.widget.TextView;

import com.huwl.oracle.kylinremotecontrol.R;
import com.huwl.oracle.kylinremotecontrol.util.BarUtils;

public class RegisterActivity extends Activity implements TextWatcher {
    private EditText register_username_et,register_password_et;
    private ImageView registerBtn;
    private TextView register_text,returnToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_username_et=findViewById(R.id.register_username_et);
        register_password_et=findViewById(R.id.register_password_et);
        registerBtn=findViewById(R.id.registerBtn);
        register_text=findViewById(R.id.register_text);
        returnToLogin=findViewById(R.id.returnToLogin);
        register_username_et.addTextChangedListener(this);
        register_password_et.addTextChangedListener(this);
        returnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.this.finish();
            }
        });
        BarUtils.setWindowBarColor(this,R.color.colorBack,BarUtils.ALL);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4以上
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        if((!"".equals(register_username_et.getText().toString())) && (!"".equals(register_password_et.getText().toString()))){
            registerBtn.setImageResource(R.drawable.login_btn_active);
            register_text.setTextColor(Color.rgb(255,255,255));
        }else{
            registerBtn.setImageResource(R.drawable.login_btn_unable);
            register_text.setTextColor(Color.rgb(167,168,169));
        }
    }
}
