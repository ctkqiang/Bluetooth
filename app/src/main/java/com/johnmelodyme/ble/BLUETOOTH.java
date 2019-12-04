package com.johnmelodyme.ble;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import static android.bluetooth.BluetoothAdapter.*;
import static com.johnmelodyme.ble.R.mipmap.toggle;
import static com.johnmelodyme.ble.R.mipmap.toggleoff;

/**
 * @CREATOR: JOHN MELODY MELISSA ESKHOLAZHT .C.T.K.
 * @DATETIME: 12/12/2019
 * @COPYRIGHT: 2019 - 2023
 * @PROJECTNAME: BLUETOOTH LOW ENERGY TUTORIAL
 */

public class BLUETOOTH extends AppCompatActivity {
    // GLOBAL DECLARATION:
    boolean DoubleBackToExitPressedOne = false;
    private static final UUID MY_UUID;
    TextView on_off_BLuetooth_text_view, mac, bluetoothName, connected_device;
    Button toggle_off,  Button_scan;
    String TheBluetoothNAme;
    ListView the_bt_list_view;
    ArrayAdapter<String> AA;
    ArrayList<BluetoothDevice> bluetoothDeviceArrayList = new ArrayList<>();
    int REQUEST_CONNECT_DEVICE;
    int interlude;
    public static int REQUEST_BLUETOOTH = 1;
    private boolean Scanning;
    private Handler handler;
    private static final long SCAN_PERIOD = 10000;
    BluetoothAdapter.LeScanCallback scan;
    BluetoothAdapter BA;
    BluetoothGattService gattService;
    BluetoothDevice device;
    BluetoothSocket socket;
    Set<BluetoothDevice> paired_devices;
    DeviceListAdapter deviceListAdapter;

    static {
        MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    }
    {
        REQUEST_CONNECT_DEVICE = 1;
        TheBluetoothNAme = "i9ST";
        interlude = 1;
    }

    // DOUBLE_PRESSED ON BACK TO QUIT IN 2 SECOND:
    @Override
    public void onBackPressed() {
        if (DoubleBackToExitPressedOne) {
            super.onBackPressed();
            return;
        }
        this.DoubleBackToExitPressedOne = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DoubleBackToExitPressedOne = false;
            }
        }, 2000);
    }

    // GLB: WILL RUN THIS ON STARTING THE APPLICATION:
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

        checkBLUETOOTH(); // CHECK THE METHOD
        //getSupportActionBar().setTitle("Bluetooth Tutorial");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Toolbar tb;
        //tb = findViewById(R.id.toolbar);
        //setSupportActionBar(tb)
        //if (device.getName().equals(TheBluetoothNAme)){
        //}
        //  BROADCAST_RECEIVER FOR [ACTION_FOUND] ::
        final BroadcastReceiver broadcastReceiver;
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // WHEN DISCOVERY FINDS A DEVICE ::
                if (action.equals(BA.ACTION_STATE_CHANGED)){
                    final int state = intent.getIntExtra(EXTRA_STATE, BA.ERROR);

                    switch (state){
                        case BluetoothAdapter.STATE_OFF:
                            Toast.makeText(getApplicationContext(), "STATE_OFF",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            Toast.makeText(getApplicationContext(), "STATE_TURNING_OFF",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        case BluetoothAdapter.STATE_ON:
                            Toast.makeText(getApplicationContext(), "STATE_ON",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:
                            Toast.makeText(getApplicationContext(), "STATE_TURNING_ON",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            break;
                    }
                }
            }
        };

        // BROADCAST_RECEIVER FOR CHANGES MADE TO BLUETOOTH STATES ::
        final BroadcastReceiver BR_CHANGES = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                    int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,
                            BluetoothAdapter.ERROR);

                    switch (mode) {
                        case SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                            Toast.makeText(getApplicationContext(), "DISCOVERABILITY ENABLED",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        case SCAN_MODE_CONNECTABLE:
                            Toast.makeText(getApplicationContext(), "DISCOVERABILITY DISABLED, ABLE TO RECEIVE CONNECTIONS",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        case SCAN_MODE_NONE:
                            Toast.makeText(getApplicationContext(), "$NULL",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        case STATE_CONNECTING:
                            Toast.makeText(getApplicationContext(), "CONNECTING",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        case STATE_CONNECTED:
                            Toast.makeText(getApplicationContext(), "CONNECTED",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            break;
                    }
                }
            }
        };

        // BROADCAST_RECEIVER FOR LISTING DEVICE WHICH HAVEN'T PAIRED ::
        final BroadcastReceiver br_Listin_device = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String Action = intent.getAction();

                if (Action.equals(BluetoothDevice.ACTION_FOUND)){
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    bluetoothDeviceArrayList.add(device);
                    deviceListAdapter = new DeviceListAdapter(context, R.layout.ble_list, bluetoothDeviceArrayList);
                }
            }
        };

        // SUB-DECLARATION:
        toggle_off = findViewById(R.id.toggle);
        on_off_BLuetooth_text_view = findViewById(R.id.on_off_ble_textView);
        mac = findViewById(R.id.Mac);
        bluetoothName = findViewById(R.id.BleName);
        connected_device = findViewById(R.id.connected_device);
        the_bt_list_view = findViewById(R.id.LV);
        Button_scan = findViewById(R.id.button_scan);

        config_button_BLE(); //REFER METHOD

        // BUTTON ONCLICK CONFIG(ONE CLICK):
        toggle_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // " IF BLUETOOTH ADAPTER IS _NOT_ ENABLED, CHANGE THE ICON TO "toggle" ,
                //  ENABLE IT OR ELSE REVERSE IT ::
                if (!(BA.isEnabled())){
                    BA.startDiscovery();
                    toggle_off.setBackgroundResource(toggle);
                    BA.enable();
                    startSearching();
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

        // BUTTON ONCLICK CONFIG(LONG CLICK):
        toggle_off.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // WHEN LONG PRESS ON BUTTON DETECTED, RUN "OPEN_DEVICE_BLUETOOTH_SETTINGS();" METHOD::
                OPEN_DEVICE_BLUETOOTH_SETTINGS(); // REFER METHOD
                return false;
            }
        });

        Button_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDevice();
            }
        });
    }

    // THIS METHOD IS A DEVICE LISTING :
    private void showDevice() {
        paired_devices = BA.getBondedDevices();
        ArrayList arrayList = new ArrayList();
        if (paired_devices.size() > 0){
            for (BluetoothDevice bluetoothDevice : paired_devices){
                String DEVICE_NAME = bluetoothDevice.getName();
                String DEVICE_MAC_ADDRESS = bluetoothDevice.getAddress();
                arrayList.add("Device Name: " + DEVICE_NAME + "\n" + "MAC: " + DEVICE_MAC_ADDRESS);
            }
            AA = new ArrayAdapter(BLUETOOTH.this, R.layout.support_simple_spinner_dropdown_item, arrayList);
            the_bt_list_view.setAdapter(AA);
        }
    }

    // THIS METHOD WILL LEAD YOU TO THE DEVICE'S BLUETOOTH SETTINGS PROGRAMMATICALLY:
    private void OPEN_DEVICE_BLUETOOTH_SETTINGS() {
        Intent intentOpenBluetoothSettings = new Intent();
        intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(intentOpenBluetoothSettings);
    }

    // ON START CHECK THE ON_OFF , THEN CHANGE THE ICON:
    private void config_button_BLE() {
        if (!(BA.isEnabled())){
            toggle_off.setBackgroundResource(toggleoff);
        }
        else {
            toggle_off.setBackgroundResource(toggle);
        }
    }

    // BLUETOOTH SCANNER :
    private void DISCOVERABLE() {
        // BLUETOOTH SCANNER ::
        if (!(BA.isEnabled())){
            Intent enableBluetoothIntent;
            enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, interlude);
        }
    }

    // CHECK FOR BLUETOOTH SUPPORTABILITY :
    private void checkBLUETOOTH() {
        // GET BLUETOOTH ADAPTER:
        BA = getDefaultAdapter();
        // IF THE BLUETOOTH ADAPTER NOT FOUND:
        if (BA == null){
            // SHOW MESSAGE
            //REFER TO THE METHOD
            notSupport();
        } else {
            // IF ADAPTERS FOUND:: SYSTEM_OUT:
            System.out.println("BLUETOOTH PROCEED TO ENABLE");
        }
    }

    // METHOD FOR "checkBLUETOOTH()":
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

    // REF ::  https://stackoverflow.com/questions/46841534/pair-a-bluetooth-device-in-android-studio
    // BLUETOOTH DISCOVERY
    private void startSearching() {
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        BLUETOOTH.this.registerReceiver(receiver, intentFilter);
        BA.startDiscovery();
    }

    // BROADCASTER ::
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

    // onClick REDIRECTING TO ABOUT PAGE ::
    public void about(View v){
        Intent about;
        about = new Intent(BLUETOOTH.this, About.class);
        startActivity(about);
    }
}