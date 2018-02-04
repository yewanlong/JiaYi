package com.kongqw.serialport.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.kongqw.serialport.HttpUtils;
import com.kongqw.serialport.R;
import com.kongqw.serialport.adapter.HomeContentAdapter;
import com.kongqw.serialport.adapter.HomeTitleAdapter;
import com.kongqw.serialport.bean.HomeBean;
import com.kongqw.serialport.bean.HomeListData;
import com.kongqw.serialport.bean.LunBoBean;
import com.kongqw.serialport.bean.LunBoList;
import com.kongqw.serialport.utils.ScreenUtil;
import com.kongqw.serialport.volley.RequestListener;
import com.kongqw.serialport.volley.StringRequest;
import com.kongqw.serialport.weight.ImageCycleView;
import com.kongqw.serialport.weight.OptionsUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lkn on 2018/1/31.
 */

public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private ImageCycleView cycleView;
    private HomeTitleAdapter adapter;
    private HomeContentAdapter contentAdapter;
    private ListView listView1, listView2;
    private List<HomeListData> copyList = new ArrayList<>();

    @Override
    protected int layoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View v) {
        cycleView = $(v, R.id.cycleView);
        listView1 = $(v, R.id.listView1);
        listView2 = $(v, R.id.listView2);
        adapter = new HomeTitleAdapter(getActivity());
        contentAdapter = new HomeContentAdapter(getActivity());
        listView1.setAdapter(adapter);
        listView2.setAdapter(contentAdapter);
        initList();
    }

    @Override
    protected void initData() {
    }

    public void initCycleView(final List<LunBoList> urlList) {
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
        listView1.setOnItemClickListener(this);
    }

    private void initList() {
        StringRequest request = HttpUtils.getAppList(listener);
        app.addRequestQueue(1001, request, this);
    }

    @Override
    protected boolean isApplyEventBus() {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        adapter.isTrue(i);
        contentAdapter.setData(adapter.getData().get(i).getP_list());
    }

    public RequestListener<String> listener = new RequestListener<String>() {
        @Override
        protected void onSuccess(int what, String response) {
            switch (what) {
                case 1001:
                    HomeBean base = JSON.parseObject(response, HomeBean.class);
                    if (base.getStatus() == HttpUtils.HTTP_STATUS) {
                        adapter.setData(base.getLists());
                        if (adapter.getData().size() != 0) {
                            adapter.isTrue(0);
                            contentAdapter.setData(base.getLists().get(0).getP_list());
                        }
                    }
                    break;
                case 1002:
                    LunBoBean lunBoBean = JSON.parseObject(response, LunBoBean.class);
                    if (lunBoBean.getStatus() == HttpUtils.HTTP_STATUS) {
                        if (lunBoBean.getLunbo().size() != 0) {
                            initCycleView(lunBoBean.getLunbo());
                        } else {
                            cycleView.setVisibility(View.GONE);
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        protected void onError(int what, int code, String message) {
        }
    };

}
