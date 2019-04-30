package com.mecodroid.movies_ytsapi.Begin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mecodroid.movies_ytsapi.R;
import com.mecodroid.movies_ytsapi.Views.MainActivity;

public class Beginning extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginning);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) { }
                Intent page = new Intent();
                page.setClass(Beginning.this, MainActivity.class);
                startActivity(page);
                finish();
            }
        }).start();
    }
}
