package com.johnmelodyme.ble;
/**
 * @CREATOR: JOHN MELODY MELISSA ESKHOLAZHT .C.T.K.
 * @DATETIME: 02/12/2019
 * @COPYRIGHT: 2019 - 2023
 * @PROJECTNAME: BLUETOOTH LOW ENERGY TUTORIAL
 */

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import static android.bluetooth.BluetoothAdapter.*;
import static com.johnmelodyme.ble.R.id.ble;
import static com.johnmelodyme.ble.R.id.start;
import static com.johnmelodyme.ble.R.mipmap.toggle;

public class BLUETOOTH extends AppCompatActivity {


    private static final UUID MY_UUID;
    TextView on_off_BLuetooth_text_view, mac, bluetoothName, connected_device;
    Button toggle_off;
    String TheBluetoothNAme;
    ListView the_bt_list_view;
    ArrayAdapter<String> AA;
    int REQUEST_CONNECT_DEVICE;
    public static int REQUEST_BLUETOOTH = 1;
    private boolean Scanning;
    private Handler handler;
    private static final long SCAN_PERIOD = 10000;
    BluetoothAdapter.LeScanCallback scan;
    BluetoothAdapter BA;
    BluetoothGattService gattService;
    BluetoothDevice device;
    BluetoothSocket socket;

    static {
        MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    }
    {
        REQUEST_CONNECT_DEVICE = 1;
        TheBluetoothNAme = "i9ST";
    }

    public void onStart(){
        super.onStart();
        String msg;
        msg = "THIS A BLUETOOTH APPLICATION";
        System.out.println(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().setTitle("Bluetooth Tutorial");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Toolbar tb;
        //tb = findViewById(R.id.toolbar);
        //setSupportActionBar(tb)
        checkBLUETOOTH();

        //if (device.getName().equals(TheBluetoothNAme)){
        //}

        /*
        getSupportActionBar().setTitle("Bluetooth Tutorial");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!(BA.isEnabled())){
            Intent enableBluetoothIntent;
            enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, 1);
        }
         */

        // ON AND OFF TOGGLE::
        toggle_off = findViewById(R.id.toggle);
        on_off_BLuetooth_text_view = findViewById(R.id.on_off_ble_textView);
        mac = findViewById(R.id.Mac);
        bluetoothName = findViewById(R.id.BleName);
        connected_device = findViewById(R.id.connected_device);

        toggle_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(BA.isEnabled())){
                    BA.startDiscovery();
                    toggle_off.setBackgroundResource(toggle);
                    BA.enable();

                    startSearching();
                    if (BA.startDiscovery()){
                        System.out.println("Scanning");
                    } else {
                        System.out.println("Opps");
                    }

                    /*
                    // OPEN DEVICE _ BLUETOOTH SETTING ::
                    Intent intentOpenBluetoothSettings = new Intent();
                    intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                    startActivity(intentOpenBluetoothSettings);
                     */
                    String macAdd = BA.getAddress();
                    String BluetoothName = BA.getName();
                    on_off_BLuetooth_text_view.setText(R.string.ble_on);
                    System.out.println("BLUETOOTH TURNING ON");
                    mac.setText(macAdd);
                    bluetoothName.setText(BluetoothName);

                }
                else {
                    BA.disable();
                    System.out.println("BLUETOOTH TURNING OFF");
                    toggle_off.setBackgroundResource(R.mipmap.toggleoff);
                    on_off_BLuetooth_text_view.setText(R.string.ble_off);
                    String empty = "";
                    mac.setText(empty);
                    bluetoothName.setText(empty);
                }
            }
        });
    }

    private void checkBLUETOOTH() {
        //  BLUETOOTH SUPPORTABILITY:
        BA = getDefaultAdapter();
        if (BA == null){
            notSupport();
        } else {
            System.out.println("BLUETOOTH PROCEED TO ENABLE");
        }
    }

    // https://stackoverflow.com/questions/46841534/pair-a-bluetooth-device-in-android-studio
    private void startSearching() {
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        BLUETOOTH.this.registerReceiver(receiver, intentFilter);
        BA.startDiscovery();
    }

    private void pairdevice(BluetoothDevice d) {
        try{
            d.getAddress();
            d.createBond();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notSupport() {
        Toast.makeText(this,
                "BLUETOOTH IS NOT SUPPORTED",
                Toast.LENGTH_LONG)
                .show();

        new AlertDialog.Builder(this)
                .setTitle("Not compatible")
                .setMessage("Your phone does not support Bluetooth")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                // DISCOVERY FOUND A DEVICE :: CONNECT TO THE DEVICE ::
                BluetoothDevice anotherDevice;
                anotherDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = anotherDevice.getName();
            }
        }
    };

    public void about(View v){
        Intent about;
        about = new Intent(BLUETOOTH.this, About.class);
        startActivity(about);
    }
}
