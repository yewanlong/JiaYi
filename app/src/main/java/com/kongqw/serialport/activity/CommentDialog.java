package com.kongqw.serialport.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kongqw.serialport.R;
import com.kongqw.serialport.utils.ScreenUtil;


/**
 * Created by Lkn on 2016/7/25.
 */
public class CommentDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private CommentListener onclickListener;
    private TextView phoneTextView, videoTextView;
    private int position, type;

    public CommentDialog(Context context, int theme, int type, int position) {
        super(context, theme);
        this.context = context;
        this.position = position;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog_social_main);
        initManager();
        initView();
        initListener();
    }

    private void initView() {
//        phoneTextView = (TextView) findViewById(R.id.tv_phone);
//        videoTextView = (TextView) findViewById(R.id.tv_video);
        //type = 0，1表示是自己的评论 2表示回复别人
        if (type == 0) {
            phoneTextView.setText("删除");
            videoTextView.setVisibility(View.GONE);
        } else if (type == 1) {
            phoneTextView.setText("删除");
            videoTextView.setText("复制");
            phoneTextView.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            phoneTextView.setText("回复");
            videoTextView.setText("复制");
        }
    }

    private void initManager() {
        WindowManager.LayoutParams params = getWindow().getAttributes(); // 获取对话框当前的参数值
        int width = (int) (ScreenUtil.getInstance(context).getWidth() * 0.75);
        params.width = width;
        params.gravity = Gravity.CENTER;
        onWindowAttributesChanged(params);
    }

    private void initListener() {
        videoTextView.setOnClickListener(this);
        phoneTextView.setOnClickListener(this);
    }

    public void setDialogClickListener(CommentListener dialogOnclickListener) {
        this.onclickListener = dialogOnclickListener;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
//            case R.id.tv_phone:
//                onclickListener.dialogOnclick(phoneTextView.getText().toString(), position);
//                dismiss();
//                break;
//            case R.id.tv_video:
//                onclickListener.dialogOnclick(videoTextView.getText().toString(), position);
//                dismiss();
//                break;
        }
    }

    public interface CommentListener {
        void dialogOnclick(String type, int position);
    }
}
