package com.huahao.serialport.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.huahao.serialport.R;
import com.huahao.serialport.utils.SilentInstall;

/**
 * Created by Lkn on 2018/2/26.
 */

public class RebootActivity extends YBaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_reboot;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RebootActivity.this);
                builder.setTitle("提示").setMessage("选择你需要的操作").setPositiveButton("重启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SilentInstall.reboot();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.create().show();
            }
        });
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SilentInstall.reboot();
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected boolean isApplyEventBus() {
        return false;
    }
}
