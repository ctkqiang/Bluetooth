package com.johnmelodyme.ble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class About extends AppCompatActivity {

    ListView listview_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        listview_about = findViewById(R.id.about_list_view_main);
        String[] about_about;
        about_about = new String[]{"About", "Version", "Source"};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i =0; i < about_about.length; ++i){
            list.add(about_about[i]);
        }

        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(this, R.layout.about_list, list);

    }
}
