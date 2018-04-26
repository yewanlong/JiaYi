package com.huahao.serialport.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.huahao.serialport.R;
import com.huahao.serialport.utils.ScreenUtil;
import com.huahao.serialport.weight.OptionsUtils;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by Lkn on 2016/7/25.
 */
public class CommentTypeDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private ImageView zfbImageView, wxImageView;
    private MyCountDownTimer mc;
    private CommentTypeClick typeClick;
    private String id;

    public CommentTypeDialog(Context context, int theme, CommentTypeClick typeClick, String id) {
        super(context, theme);
        this.context = context;
        this.typeClick = typeClick;
        this.id = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialoag_qcode_type);
        initManager();
        initView();
        initListener();
    }

    private void initView() {
        wxImageView = (ImageView) findViewById(R.id.iv_wx);
        zfbImageView = (ImageView) findViewById(R.id.iv_zfb);
        mc = new MyCountDownTimer(20000, 1000);
        mc.start();
    }

    public void setId(String id) {
        this.id = id;
    }

    private void initManager() {
        WindowManager.LayoutParams params = getWindow().getAttributes(); // 获取对话框当前的参数值
        params.width = (int) (ScreenUtil.getInstance(context).getWidth() * 0.55);
        params.gravity = Gravity.CENTER;
        onWindowAttributesChanged(params);
    }

    private void initListener() {
        zfbImageView.setOnClickListener(this);
        wxImageView.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_wx:
                typeClick.wxPay(id);
                dismiss();
                break;
            case R.id.iv_zfb:
                typeClick.zfbPay(id);
                dismiss();
                break;
        }
    }

    public interface CommentTypeClick {
        void wxPay(String id);

        void zfbPay(String id);
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            if (isShowing())
                dismiss();
        }

        public void onTick(long millisUntilFinished) {
        }
    }
}
