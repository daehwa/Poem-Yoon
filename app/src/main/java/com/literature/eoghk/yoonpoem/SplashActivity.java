package com.literature.eoghk.yoonpoem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


/**
 * Created by eoghk on 2017-05-31.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView todayFact = (TextView) findViewById(R.id.today_fact);

        InputStream is = null;
        try {
            is = getAssets().open("fact/윤동주에 관하여");
            BufferedReader bIn = new BufferedReader(new InputStreamReader(is));
            String str="";
            int num = new Random().nextInt(39);
            int count = 0;
            while ((str=bIn.readLine()) != null) {
                if (count==num){
                    todayFact.setText(str);
                    break;
                }
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, 2500);
    }
}
