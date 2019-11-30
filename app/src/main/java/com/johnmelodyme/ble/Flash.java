package com.johnmelodyme.ble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Flash extends AppCompatActivity {

    int flash;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        flash = 0;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent toMain;
                toMain = new Intent(Flash.this, BLUETOOTH.class);
                startActivity(toMain);
            }
        }, 2000);
    }
}
