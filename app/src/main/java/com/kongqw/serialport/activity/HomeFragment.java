package com.kongqw.serialport.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.kongqw.serialport.HttpUtils;
import com.kongqw.serialport.R;
import com.kongqw.serialport.adapter.HomeContentAdapter;
import com.kongqw.serialport.adapter.HomeTitleAdapter;
import com.kongqw.serialport.bean.HomeBean;
import com.kongqw.serialport.bean.LunBoBean;
import com.kongqw.serialport.bean.LunBoList;
import com.kongqw.serialport.utils.ScreenUtil;
import com.kongqw.serialport.utils.VToast;
import com.kongqw.serialport.volley.RequestListener;
import com.kongqw.serialport.volley.StringRequest;
import com.kongqw.serialport.weight.ImageCycleView;
import com.kongqw.serialport.weight.OptionsUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Lkn on 2018/1/31.
 */

public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ImageCycleView cycleView;
    private HomeTitleAdapter adapter;
    private HomeContentAdapter contentAdapter;
    private ListView listView1, listView2;
    private double addNumber = 0;
    private TextView tv_money, tv_next;
    private ImageView iv_cart;
    private int numberThis = 0;

    @Override
    protected int layoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View v) {
        cycleView = $(v, R.id.cycleView);
        listView1 = $(v, R.id.listView1);
        listView2 = $(v, R.id.listView2);
        tv_money = $(v, R.id.tv_money);
        iv_cart = $(v, R.id.iv_cart);
        tv_next = $(v, R.id.tv_next);
    }

    @Override
    protected void initData() {
        adapter = new HomeTitleAdapter(getActivity());
        contentAdapter = new HomeContentAdapter(getActivity(), addListener);
        listView1.setAdapter(adapter);
        listView2.setAdapter(contentAdapter);
        initList();
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
        tv_next.setOnClickListener(this);
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

    private HomeContentAdapter.HomeAddListener addListener = new HomeContentAdapter.HomeAddListener() {
        @Override
        protected void addOnClick(int position, View v) {
            addData(position);
        }

        @Override
        protected void reduceOnClick(int position, View v) {
            reduceData(position);
        }
    };

    private void reduceData(int position) {
        int num = contentAdapter.getData().get(position).getNum();
        num--;
        numberThis--;
        contentAdapter.getData().get(position).setNum(num);
        adapter.notifyDataSetChanged();
        addNumber = addNumber - contentAdapter.getData().get(position).getPrice();
        addMoeny();
    }

    private void addData(int position) {
        if (numberThis >= 10) {
            VToast.showLong("一次购买数量控制在10以内");
            return;
        }
        if (contentAdapter.getData().get(position).getNum() >= contentAdapter.getData().get(position).getLeft_num()) {
            VToast.showLong("库存不足");
        } else {
            int num = contentAdapter.getData().get(position).getNum();
            num++;
            numberThis++;
            contentAdapter.getData().get(position).setNum(num);
            addNumber = addNumber + contentAdapter.getData().get(position).getPrice();
            adapter.notifyDataSetChanged();
            addMoeny();
        }
    }

    private void addMoeny() {
        tv_money.setText("合计：" + addNumber + "元");
        if (addNumber == 0) {
            iv_cart.setImageResource(R.mipmap.icon_cart);
            iv_cart.setOnClickListener(null);
        } else {
            iv_cart.setImageResource(R.mipmap.icon_cart2);
            iv_cart.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cart:
                break;
            case R.id.tv_next:
                if (addNumber == 0) {
                    VToast.showLong("请选择商品");
                    return;
                }
                break;
        }
    }
}
