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
import android.companion.BluetoothLeDeviceFilter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.johnmelodyme.ble.R.mipmap;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import static android.bluetooth.BluetoothAdapter.*;
import static com.johnmelodyme.ble.R.mipmap.*;
import static com.johnmelodyme.ble.R.mipmap.toggle;

public class BLUETOOTH extends AppCompatActivity {

    private static final UUID MY_UUID;

    static {
        MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    }

    TextView on_off_BLuetooth_text_view;
    TextView mac;
    TextView bluetoothName;
    Button toggle_off;
    ListView list_View_list_bluetooth;
    TextView name, macadd;
    public static int REQUEST_BLUETOOTH = 1;
    private boolean Scanning;
    private Handler handler;
    private static final long SCAN_PERIOD = 10000;
    BluetoothAdapter.LeScanCallback scan;
    BluetoothAdapter BA;
    String TheBluetoothNAme;
    BluetoothGattService gattService;
    BluetoothDevice device;
    BluetoothSocket socket;

    {
        TheBluetoothNAme = "i9ST";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  BLUETOOTH SUPPORTABILITY:
        BA = getDefaultAdapter();
        if (BA == null){
            notSupport();
        } else {
            System.out.println("BLUETOOTH PROCEED TO ENABLE");
        }

        //if (device.getName().equals(TheBluetoothNAme)){
        //}

        /*
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

        BluetoothAdapter aa = BluetoothAdapter.getDefaultAdapter();
        Set <BluetoothDevice> pairedDevice = aa.getBondedDevices();

        List<String> streeng = new ArrayList<String>();
        for(BluetoothDevice btDevice : pairedDevice)
            streeng.add(btDevice.getName());

        ListView listView = findViewById(R.id.listOfDEVICE);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.ble_list));

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

                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(receiver, filter);
                    /*
                    Intent intentOpenBluetoothSettings = new Intent();
                    intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                    startActivity(intentOpenBluetoothSettings);
                     */

                    String macAdd = BA.getAddress();
                    String BluetoothName = BA.getName();
                    on_off_BLuetooth_text_view.setText(R.string.ble_on);
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
/*
    private void getConnectedDevice(){
        boolean retval = true;
        try {
            retval = android.bluetooth.BluetoothProfile.STATE_DISCONNECTED != BluetoothAdapter
                    .getDefaultAdapter()
                    .getProfileConnectionState(android.bluetooth.BluetoothProfile.HEADSET);

        } catch (Exception exc) {
            // nothing to do
        }
    }
 */

public void DeviceDiscovery(View v){
    scanningMSG();

}

    private void scanningMSG() {
        Toast.makeText(BLUETOOTH.this,
                "Scanning",
                Toast.LENGTH_SHORT)
                .show();
    }

    private void list_of_device() {

    }
}