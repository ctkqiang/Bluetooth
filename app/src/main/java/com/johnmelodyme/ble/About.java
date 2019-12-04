package com.johnmelodyme.ble;
/**
 * @CREATOR: JOHN MELODY MELISSA ESKHOLAZHT .C.T.K.
 * @DATETIME: 12/12/2019
 * @COPYRIGHT: 2019 - 2023
 * @PROJECTNAME: BLUETOOTH LOW ENERGY TUTORIAL
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class About extends AppCompatActivity {
    ListView listView;
    String [] about = {"About", "Version", "Source"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        listView = findViewById(R.id.about_list_view_main);
        ArrayAdapter<String> stringArrayAdapter;
        stringArrayAdapter = new ArrayAdapter<String>(About.this, R.layout.support_simple_spinner_dropdown_item, about);
        listView.setAdapter(stringArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent source;
                String url;
                url = "https://github.com/johnmelodyme/Bluetooth";
                source = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(source);
            }
        });
    }
}
