package com.kongqw.serialport.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kongqw.serialport.R;
import com.kongqw.serialport.adapter.HomeGridAdapter;
import com.kongqw.serialport.bean.AppLbts;
import com.kongqw.serialport.utils.ScreenUtil;
import com.kongqw.serialport.weight.ImageCycleView;
import com.kongqw.serialport.weight.OptionsUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lkn on 2018/1/31.
 */

public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    private ImageCycleView cycleView;
    private GridView gridView;
    private HomeGridAdapter adapter;

    @Override
    protected int layoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View v) {
        cycleView = $(v, R.id.cycleView);
        gridView = $(v, R.id.gridView);
        adapter = new HomeGridAdapter(getActivity());
        gridView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        List<AppLbts> lbts = new ArrayList<>();
        AppLbts appLbts = new AppLbts();
        appLbts.setImage("http://d.hiphotos.baidu.com/image/pic/item/a044ad345982b2b713b5ad7d3aadcbef76099b65.jpg");
        appLbts.setLink("http://www.baidu.com");
        lbts.add(appLbts);
        appLbts = new AppLbts();
        appLbts.setImage("http://g.hiphotos.baidu.com/image/pic/item/10dfa9ec8a1363275cd315d09a8fa0ec08fac713.jpg");
        appLbts.setLink("http://www.baidu.com");
        lbts.add(appLbts);
        initCycleView(lbts);
    }

    public void initCycleView(final List<AppLbts> urlList) {
        cycleView.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.height = (int) (ScreenUtil.getInstance(getActivity()).getWidth() * 0.427);
        params.gravity = Gravity.BOTTOM;
        cycleView.setLayoutParams(params);
        ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
            @Override
            public void onImageClick(int position, View imageView) {
            }

            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                ImageLoader.getInstance().displayImage(imageURL, imageView, OptionsUtils.options(R.mipmap.ic_friends_sends_pictures_no));
            }
        };
        cycleView.setImageResources(urlList, mAdCycleViewListener);
        cycleView.startImageCycle();
    }

    @Override
    protected void initListener() {
        gridView.setOnItemClickListener(this);
    }

    @Override
    protected boolean isApplyEventBus() {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
