package com.johnmelodyme.ble;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {
    private LayoutInflater inflater;
    private ArrayList<BluetoothDevice> bluetoothDevices;
    private int VIEW_RESOURCES_ID;

    public DeviceListAdapter(Context context, int RESOURCES_ID, ArrayList<BluetoothDevice> bluetooth_Devices){
        super(context, RESOURCES_ID, bluetooth_Devices);
        this.bluetoothDevices = bluetooth_Devices;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        convertView = inflater.inflate(VIEW_RESOURCES_ID, null);
        BluetoothDevice device = bluetoothDevices.get(position);

        if (device != null){
            TextView BLUETOOTH_DEVICE_NAME =(TextView) convertView.findViewById(R.id.Bluetooth_Name);
            TextView BLUETOOTH_DEVICE_MAC_ADDRESS =(TextView) convertView.findViewById(R.id.MAcAddresses);

            if (BLUETOOTH_DEVICE_NAME != null){
                BLUETOOTH_DEVICE_NAME.setText(device.getName());
            }

            if (BLUETOOTH_DEVICE_MAC_ADDRESS != null){
                BLUETOOTH_DEVICE_MAC_ADDRESS.setText(device.getAddress());
            }
        }

        return convertView;
    }
}
