package com.huahao.serialport.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
public class CommentDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView tv_close, tv_content;
    private ImageView imageView;
    private String url;
    private Handler handler = new Handler();
    private MyCountDownTimer mc;

    public CommentDialog(Context context, int theme, String url) {
        super(context, theme);
        this.context = context;
        this.url = url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialoag_qcode);
        initManager();
        initView();
        initListener();
    }

    private void initView() {
        tv_close = (TextView) findViewById(R.id.tv_close);
        tv_content = findViewById(R.id.tv_content);
        imageView = findViewById(R.id.imageView);
        mc = new MyCountDownTimer(70000, 1000);
        mc.start();
        imageView.setImageResource(R.mipmap.ic_friends_sends_pictures_no);
        ImageLoader.getInstance().displayImage(url, imageView, OptionsUtils.
                options(R.mipmap.ic_friends_sends_pictures_no));
    }

    private void initManager() {
        WindowManager.LayoutParams params = getWindow().getAttributes(); // 获取对话框当前的参数值
        params.width = (int) (ScreenUtil.getInstance(context).getWidth() * 0.75);
        params.height = (int) (ScreenUtil.getInstance(context).getHeight() * 0.65);
        params.gravity = Gravity.CENTER;
        onWindowAttributesChanged(params);
    }

    private void initListener() {
        tv_close.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void setUrl(String url) {
        imageView.setImageResource(R.mipmap.ic_friends_sends_pictures_no);
        ImageLoader.getInstance().displayImage(url, imageView, OptionsUtils.
                options(R.mipmap.ic_friends_sends_pictures_no));
        this.url = url;
        mc.start();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_close:
                dismiss();
                break;
        }
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            tv_content.setText("70秒后自动返回");
        }

        public void onFinish() {
            dismiss();
        }

        public void onTick(long millisUntilFinished) {
            tv_content.setText((millisUntilFinished / 1000) + "秒后自动返回");
        }
    }
}
