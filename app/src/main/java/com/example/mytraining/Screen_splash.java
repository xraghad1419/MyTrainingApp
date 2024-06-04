package com.example.mytraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
public class Screen_splash extends AppCompatActivity {

    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.screen_splash);
    Runnable runnable=new Runnable() {
    @Override
    public void run() {
        run_activity();
    }
    };
        handler.postDelayed(runnable,4*1000);
    }
    public void run_activity(){
        Intent intent=new Intent (this,Home_page.class);
        startActivity(intent);
        finish();
    }

}