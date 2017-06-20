package com.literature.eoghk.yunpoem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by eoghk on 2017-06-06.
 */

public class AllPoemActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.see_all_poem);
        Intent intent = getIntent();
        intent.getExtras();
        ((TextView)findViewById(R.id.ttt)).setText(intent.getStringExtra("ttt"));
        ((TextView)findViewById(R.id.www)).setText(intent.getStringExtra("www"));
        ((TextView)findViewById(R.id.ccc)).setText(intent.getStringExtra("ccc"));
    }
}
