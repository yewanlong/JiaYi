package com.huahao.serialport.gpio;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.huahao.serialport.R;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class GpioActivity2 extends Activity {
    private static final String TAG = "MainActivity";
    private EditText mEt_num;
    private TextView mGpioState;
    private int gpio_num = 227;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpio2);
        initView();

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("提示").setMessage("选择你需要的操作").setPositiveButton("关机", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Log.v(TAG, "root Runtime->shutdown");
//                //Process proc =Runtime.getRuntime().exec(new String[]{"su","-c","shutdown"});  //关机
//                java.lang.Process proc = null;
//                try {
//                    proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot -p"});
//                    proc.waitFor();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).setNegativeButton("开机", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
        // builder.create().show();
    }

    private void initView() {
        mEt_num = (EditText) findViewById(R.id.et_gpio_num);
        mGpioState = (TextView) findViewById(R.id.tv_gpio_state);
        Button btn_success = (Button) findViewById(R.id.btn_success);
        btn_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mEt_num.getText().toString())) {
                    gpio_num = Integer.parseInt(mEt_num.getText().toString());
                    initGpio();
                    String gpioState = readGpio();
                    if (!TextUtils.isEmpty(gpioState)) {
                        mGpioState.setText(gpioState);
                    } else {
                        mGpioState.setText("读取失败");
                        Toast.makeText(GpioActivity2.this, "读取失败", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(GpioActivity2.this, "请输入需要操作的GPIO口", Toast.LENGTH_SHORT).show();
            }
        });
        Switch sw_gpio = (Switch) findViewById(R.id.sw_gpio);
        sw_gpio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d(TAG, "onCheckedChanged:" + b);
                if (gpio_num != -1) {
                    controlGpio(b);
                } else
                    Toast.makeText(GpioActivity2.this, "请输入需要操作的GPIO口", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initGpio() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(process.getOutputStream());
            //打开gpio引脚74，即status_led连接的引脚
            dos.writeBytes("echo " + gpio_num + " > /sys/class/gpio/export" + "\n");
            dos.flush();
            //设置引脚功能为输出
//            dos.writeBytes("echo out > /sys/class/gpio/gpio" + gpio_num + "/direction" + "\n");
            dos.writeBytes("echo in > /sys/class/gpio/gpio" + gpio_num + "/direction" + "\n");
            dos.flush();
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readGpio() {
        String gpioState = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/gpio/gpio" + gpio_num + "/value");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    gpioState = str.trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gpioState;
    }

    private void controlGpio(boolean isOpen) {
        DataOutputStream dos = null;
        try {
            Process process = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(process.getOutputStream());
            if (isOpen)
                dos.writeBytes("echo 1 > /sys/class/gpio/gpio" + gpio_num + "/value" + "\n");//开
            else
                dos.writeBytes("echo 0 > /sys/class/gpio/gpio" + gpio_num + "/value" + "\n");//关
            dos.flush();
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
