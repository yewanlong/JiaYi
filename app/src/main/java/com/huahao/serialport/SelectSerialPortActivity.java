package com.huahao.serialport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.huahao.serialport.adapter.DeviceAdapter;
import com.kongqw.serialportlibrary.Device;
import com.kongqw.serialportlibrary.SerialPortFinder;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class SelectSerialPortActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DeviceAdapter mDeviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_serial_port);

        ListView listView = (ListView) findViewById(R.id.lv_devices);

        SerialPortFinder serialPortFinder = new SerialPortFinder();

        ArrayList<Device> devices = serialPortFinder.getDevices();

        if (listView != null) {
            listView.setEmptyView(findViewById(R.id.tv_empty));
            mDeviceAdapter = new DeviceAdapter(getApplicationContext(), devices);
            listView.setAdapter(mDeviceAdapter);
            listView.setOnItemClickListener(this);
        }
//        set(5555);
    }
    protected static int set(int paramInt) throws IOException,
            InterruptedException {
        Process localProcess = Runtime.getRuntime().exec("su");
        DataOutputStream localDataOutputStream = new DataOutputStream(
                (OutputStream) localProcess.getOutputStream());
        localDataOutputStream.writeBytes("setprop service.adb.tcp.port "
                + paramInt + "\n");
        localDataOutputStream.flush();
        localDataOutputStream.writeBytes("stop adbd\n");
        localDataOutputStream.flush();
        localDataOutputStream.writeBytes("start adbd\n");
        localDataOutputStream.flush();
        localDataOutputStream.writeBytes("exit\n");
        localDataOutputStream.flush();
        localProcess.waitFor();
        return localProcess.exitValue();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Device device = mDeviceAdapter.getItem(position);

        Intent intent = new Intent(this, SerialPortActivity.class);
        intent.putExtra(SerialPortActivity.DEVICE, device);
        startActivity(intent);
    }


}
