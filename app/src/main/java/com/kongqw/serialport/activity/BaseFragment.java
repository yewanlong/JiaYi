package com.kongqw.serialport.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kongqw.serialport.InitApplication;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by Lkn on 2016/7/15.
 */
public abstract class BaseFragment extends Fragment {
    public static InitApplication app = InitApplication.getInstance();
    public ProgressDialog progressDialog;
    public View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isApplyEventBus()) EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(layoutId(), container, false);
            progressDialog = new ProgressDialog(getActivity());
            initView(view);
            initData();
            initListener();
        }
        return view;
    }

    protected abstract int layoutId();

    protected abstract void initView(View v);

    protected abstract void initData();

    protected abstract void initListener();

    protected abstract boolean isApplyEventBus();

    @Override
    public void onDestroy() {
        if (isApplyEventBus()) EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) view.getParent()).removeView(view);
    }

    //映射view的快捷方法
    public <T> T $(View v, int viewId) {
        return (T) v.findViewById(viewId);
    }

    public void setProgressDialog(String msg) {
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if(getActivity()==null){
            return;
        }
        if (!getActivity().isFinishing() && progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }


}
