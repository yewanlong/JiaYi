package com.huahao.serialport.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.huahao.serialport.HttpUtils;
import com.huahao.serialport.R;
import com.huahao.serialport.adapter.HomeContentAdapter;
import com.huahao.serialport.adapter.HomeOrderAdapter;
import com.huahao.serialport.adapter.HomeTitleAdapter;
import com.huahao.serialport.bean.HomeBean;
import com.huahao.serialport.bean.HomeListData;
import com.huahao.serialport.bean.HomeOrder;
import com.huahao.serialport.bean.LunBoBean;
import com.huahao.serialport.bean.LunBoList;
import com.huahao.serialport.bean.OrderData;
import com.huahao.serialport.bean.UpdateApp;
import com.huahao.serialport.utils.CommonUtils;
import com.huahao.serialport.utils.ScreenUtil;
import com.huahao.serialport.utils.VToast;
import com.huahao.serialport.volley.RequestListener;
import com.huahao.serialport.volley.StringRequest;
import com.huahao.serialport.weight.ImageCycleView;
import com.huahao.serialport.weight.OptionsUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lkn on 2018/1/31.
 */

public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, CommentTypeDialog.CommentTypeClick {
    private ImageCycleView cycleView;
    private HomeTitleAdapter adapter;
    private HomeContentAdapter contentAdapter;
    private HomeOrderAdapter orderAdapter;
    private ListView listView1, listView2, listView4, orderListView;
    private double addPrice = 0;
    private TextView tv_money, tv_next, tv_subtract, tv_order_number, textView, tv_connect;
    private ImageView iv_cart;
    private int numberThis = 0, titlePosition;
    private List<HomeListData> copyList = new ArrayList<>();
    private LinearLayout layout_order, layout_no_data, layout_no_next;

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
        textView = $(v, R.id.textView);
        iv_cart = $(v, R.id.iv_cart);
        tv_next = $(v, R.id.tv_next);
        layout_order = $(v, R.id.layout_order);
        orderListView = $(v, R.id.lv_order);
        tv_subtract = $(v, R.id.tv_subtract);
        tv_order_number = $(v, R.id.tv_order_number);
        listView4 = $(v, R.id.listView4);
        layout_no_data = $(v, R.id.layout_no_data);
        layout_no_next = $(v, R.id.layout_no_next);
        tv_connect = $(v, R.id.tv_connect);
    }

    @Override
    protected void initData() {
        adapter = new HomeTitleAdapter(getActivity());
        contentAdapter = new HomeContentAdapter(getActivity(), addListener);
        orderAdapter = new HomeOrderAdapter(getActivity(), orderListener, copyList);
        listView1.setAdapter(adapter);
        listView2.setAdapter(contentAdapter);
        orderListView.setAdapter(orderAdapter);
        getUdapte();
    }

    public void initLog() {
        listView4.setVisibility(View.GONE);
    }

    public void initLog(ArrayList<String> list) {
        listView4.setVisibility(View.VISIBLE);
        listView4.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.my_spinner, list));
    }

    private void initCycleView(final List<LunBoList> urlList) {
        cycleView.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.height = (int) (ScreenUtil.getInstance(getActivity()).getWidth() * 0.427);
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
        layout_order.setOnClickListener(this);
        tv_subtract.setOnClickListener(this);
        tv_money.setOnClickListener(this);
        textView.setOnClickListener(this);
        layout_no_next.setOnClickListener(this);
        tv_money.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(HttpUtils.IMEI);
                return false;
            }
        });
    }

    public void initList() {
        if (CommonUtils.isNetWorkConnected(getActivity())) {
            StringRequest request = HttpUtils.getAppList(listener);
            app.addRequestQueue(1001, request, this);
        } else {
            VToast.showLong("网络异常");
        }
    }

    public void initList2() {
        copyList.clear();
        addPrice = 0;
        numberThis = 0;
        tv_money.setText("合计：" + addPrice + "元");
        initList();
    }

    public void getLunbo() {
        if (CommonUtils.isNetWorkConnected(getActivity())) {

            StringRequest request = HttpUtils.getLunbo(listener);
            app.addRequestQueue(1002, request, this);
        } else {
            VToast.showLong("网络异常");
        }
    }

    public void getUdapte() {
        if (CommonUtils.isNetWorkConnected(getActivity())) {
            VToast.showLong("版本号：" + CommonUtils.getAppVersionCode(getActivity()));
            StringRequest request = HttpUtils.getUpdate(listener, CommonUtils.getAppVersionCode(getActivity()));
            app.addRequestQueue(1005, request, this);
        } else {
            VToast.showLong("网络异常");
        }
    }

    @Override
    protected boolean isApplyEventBus() {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        titlePosition = i;
        adapter.isTrue(i);
        contentAdapter.setData(adapter.getData().get(i).getP_list());
    }

    public RequestListener<String> listener = new RequestListener<String>() {
        @Override
        protected void onSuccess(int what, String response) {
            try {
                switch (what) {
                    case 1001:
                        HomeBean base = JSON.parseObject(response, HomeBean.class);
                        if (base.getStatus() == HttpUtils.HTTP_STATUS) {
                            adapter.setData(base.getLists());
                            if (adapter.getData().size() != 0) {
                                adapter.isTrue(0);
                                contentAdapter.setData(base.getLists().get(0).getP_list());
                                addPrice = 0;
                                numberThis = 0;
                                tv_money.setText("合计：" + addPrice + "元");
                                layout_no_data.setVisibility(View.GONE);
                            } else {
                                layout_no_data.setVisibility(View.VISIBLE);
                                contentAdapter.setData(new ArrayList<HomeListData>());
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
                    case 1003:
                        HomeOrder homeOrder = JSON.parseObject(response, HomeOrder.class);
                        if (homeOrder.getStatus() == HttpUtils.HTTP_STATUS) {
                            showTypeDialog(homeOrder.getResult().getOid());
                        } else {
                            dismissProgressDialog();
                            VToast.showLong(homeOrder.getReason());
                        }
                        break;
                    case 1004:
                        OrderData orderData = JSON.parseObject(response, OrderData.class);
                        if (orderData.getStatus() == HttpUtils.HTTP_STATUS) {
                            dismissProgressDialog();
                            showCommentDialog(orderData.getQr_code(), "请使用微信支付");
                        } else {
                            dismissProgressDialog();
                            VToast.showLong(orderData.getReason());
                        }
                        break;
                    case 1005:
                        UpdateApp updateApp = JSON.parseObject(response, UpdateApp.class);
                        if (updateApp.getStatus() == HttpUtils.HTTP_STATUS) {
                            if (!TextUtils.isEmpty(updateApp.getApk_url()) && !"".equals(updateApp.getApk_url())) {
                                UpdateService2.Builder.create(updateApp.getApk_url())
                                        .build(getActivity());

                            }
                        }
                        break;
                    case 1006:
                        OrderData orderData2 = JSON.parseObject(response, OrderData.class);
                        if (orderData2.getStatus() == HttpUtils.HTTP_STATUS) {
                            dismissProgressDialog();
                            showCommentDialog(orderData2.getQr_code(), "请使用支付宝支付");
                        } else {
                            dismissProgressDialog();
                            VToast.showLong(orderData2.getReason());
                        }
                        break;
                    default:
                        break;
                }

            } catch (Exception e) {
                VToast.showLong("JSON错误日志：" + e.getMessage());
            }
        }

        @Override
        protected void onError(int what, int code, String message) {
            dismissProgressDialog();
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
    private HomeOrderAdapter.HomeOrderListener orderListener = new HomeOrderAdapter.HomeOrderListener() {
        @Override
        protected void addOnClick(int position, View v) {
            addCopyData(position);
        }

        @Override
        protected void reduceOnClick(int position, View v) {
            reduceCopyData(position);
        }
    };

    private void reduceCopyData(int position) {
        int num = copyList.get(position).getNum();
        num--;
        numberThis--;
        adapter.getData().get(copyList.get(position).getTitlePosition()).getP_list().get(copyList.get(position).getContentPosition()).setNum(num);
        copyList.get(position).setNum(num);
        BigDecimal bigDecimal = new BigDecimal(Double.toString(addPrice));
        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(copyList.get(position).getPrice()));
        BigDecimal bigDecimalSubtract = bigDecimal.subtract(bigDecimal2);
        addPrice = bigDecimalSubtract.doubleValue();
        tv_order_number.setText("已选商品(" + numberThis + ")");
        if (num == 0) {
            copyList.remove(position);
        }
        contentAdapter.notifyDataSetChanged();
        orderAdapter.notifyDataSetChanged();
        addMoeny();
    }

    private void addCopyData(int position) {
        if (numberThis >= 10) {
            VToast.showLong("一次购买数量控制在10以内");
            return;
        }
        if (copyList.get(position).getNum() >= copyList.get(position).getLeft_num()) {
            VToast.showLong("库存不足");
        } else {
            int num = copyList.get(position).getNum();
            num++;
            numberThis++;
            copyList.get(position).setNum(num);
            adapter.getData().get(copyList.get(position).getTitlePosition()).getP_list().get(copyList.get(position).
                    getContentPosition()).setNum(num);
            BigDecimal bigDecimal = new BigDecimal(Double.toString(addPrice));
            BigDecimal bigDecimal2 = new BigDecimal(Double.toString(copyList.get(position).getPrice()));
            BigDecimal bigDecimalAdd = bigDecimal.add(bigDecimal2);
            addPrice = bigDecimalAdd.doubleValue();
            contentAdapter.notifyDataSetChanged();
            orderAdapter.notifyDataSetChanged();
            addMoeny();
            initCopyList(contentAdapter.getData().get(position), position);
        }
        tv_order_number.setText("已选商品(" + numberThis + ")");
    }

    private void reduceData(int position) {
        int num = contentAdapter.getData().get(position).getNum();
        num--;
        numberThis--;
        contentAdapter.getData().get(position).setNum(num);
        contentAdapter.notifyDataSetChanged();
        BigDecimal bigDecimal = new BigDecimal(Double.toString(addPrice));
        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(contentAdapter.getData().get(position).getPrice()));
        BigDecimal bigDecimalSubtract = bigDecimal.subtract(bigDecimal2);
        addPrice = bigDecimalSubtract.doubleValue();
        addMoeny();
        if (num == 0) {
            removeCopyList(contentAdapter.getData().get(position));
        } else {
            initCopyList(contentAdapter.getData().get(position), position);
        }
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
            BigDecimal bigDecimal = new BigDecimal(Double.toString(addPrice));
            BigDecimal bigDecimal2 = new BigDecimal(Double.toString(contentAdapter.getData().get(position).getPrice()));
            BigDecimal bigDecimalAdd = bigDecimal.add(bigDecimal2);
            addPrice = bigDecimalAdd.doubleValue();
            contentAdapter.notifyDataSetChanged();
            addMoeny();
            initCopyList(contentAdapter.getData().get(position), position);
        }
    }

    private void initCopyList(HomeListData data, int position) {
        if (copyList.size() == 0) {
            data.setContentPosition(position);
            data.setTitlePosition(titlePosition);
            copyList.add(data);
        } else {
            for (int i = 0; i < copyList.size(); i++) {
                if (copyList.get(i).getPid().equals(data.getPid())) {
                    copyList.get(i).setNum(data.getNum());
                    break;
                } else if (i == (copyList.size() - 1)) {
                    data.setContentPosition(position);
                    data.setTitlePosition(titlePosition);
                    copyList.add(data);
                }
            }
        }
    }

    private void removeCopyList(HomeListData data) {
        for (int i = 0; i < copyList.size(); i++) {
            if (copyList.get(i).getPid().equals(data.getPid())) {
                copyList.remove(i);
                break;
            }
        }
    }

    private void addMoeny() {
        tv_money.setText("合计：" + addPrice + "元");
        if (addPrice == 0) {
            iv_cart.setImageResource(R.mipmap.icon_cart);
            iv_cart.setOnClickListener(null);
            layout_order.setVisibility(View.GONE);
        } else {
            iv_cart.setImageResource(R.mipmap.icon_cart2);
            iv_cart.setOnClickListener(this);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cart:
                if (layout_order.getVisibility() == View.VISIBLE) {
                    layout_order.setVisibility(View.GONE);
                } else {
                    tv_order_number.setText("已选商品(" + numberThis + ")");
                    layout_order.setVisibility(View.VISIBLE);
                    orderAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.layout_order:
                layout_order.setVisibility(View.GONE);
                break;
            case R.id.tv_subtract:
                subtractList();
                break;
            case R.id.tv_next:
                if (addPrice == 0) {
                    VToast.showLong("请选择商品");
                    return;
                } else {
                    getOrder();
                }
                break;
            case R.id.textView:
                getActivity().startService(new Intent(getActivity(),SystemOverlayMenuService.class));
                app.finishActivity();
                break;
            case R.id.tv_money:
                textView.setVisibility(View.GONE);
                break;
            case R.id.layout_no_next:
                tv_connect.setText("正在重连...");
                MainActivity parentActivity = (MainActivity) getActivity();
                parentActivity.onConnect();
                break;
        }
    }

    public void toConnect() {
        layout_no_next.setVisibility(View.VISIBLE);
        tv_connect.setText("啊哦~连接失败，点我重新试试");
    }

    public void toConnect2() {
        layout_no_next.setVisibility(View.GONE);
    }

    public void subtractList() {
        if (adapter.getData().size() == 0) {
            return;
        }
        for (int i = 0; i < copyList.size(); i++) {
            adapter.getData().get(copyList.get(i).getTitlePosition()).getP_list().get(copyList.get(i).getContentPosition()).setNum(0);
        }
        copyList.clear();
        numberThis = 0;
        contentAdapter.notifyDataSetChanged();
        layout_order.setVisibility(View.GONE);
        addPrice = 0;
        addMoeny();
    }

    private void getOrder() {
        if (CommonUtils.isNetWorkConnected(getActivity())) {
            setProgressDialog("正在获取订单...");
            String pic = "";
            for (int i = 0; i < copyList.size(); i++) {
                if (i == 0) {
                    pic = copyList.get(i).getPid() + ":" + copyList.get(i).getNum();
                } else {
                    pic = pic + "," + copyList.get(i).getPid() + ":" + copyList.get(i).getNum();
                }
            }
            StringRequest request = HttpUtils.getOrder(listener, pic);
            app.addRequestQueue(1003, request, this);
        } else {
            VToast.showLong("网络异常");
        }
    }

    private void getOrder2(String oid) {
        if (CommonUtils.isNetWorkConnected(getActivity())) {
            StringRequest request = HttpUtils.getOrder2(listener, oid);
            app.addRequestQueue(1004, request, this);
        } else {
            VToast.showLong("网络异常");
        }
    }

    private void getOrderPay(String oid) {
        if (CommonUtils.isNetWorkConnected(getActivity())) {
            StringRequest request = HttpUtils.getOrderPay(listener, oid);
            app.addRequestQueue(1006, request, this);
        } else {
            VToast.showLong("网络异常");
        }
    }

    private CommentDialog commentDialog;
    private CommentTypeDialog typeDialog;

    public void dialogDismiss() {
        if (commentDialog.isShowing()) {
            commentDialog.dismiss();
        }
    }

    private void showCommentDialog(String url, String type) {
        if (commentDialog == null) {
            commentDialog = new CommentDialog(getActivity(), R.style.myDialogTheme, url, type);
            commentDialog.setCanceledOnTouchOutside(false);
            commentDialog.show();
        } else {
            commentDialog.setUrl(url, type);
            commentDialog.show();
        }
    }

    private void showTypeDialog(String order) {
        if (commentDialog == null) {
            typeDialog = new CommentTypeDialog(getActivity(), R.style.myDialogTheme, this, order);
            typeDialog.setCanceledOnTouchOutside(true);
            typeDialog.show();
        } else {
            typeDialog.setId(order);
            typeDialog.show();
        }
    }

    @Override
    public void wxPay(String id) {
        getOrder2(id);
    }

    @Override
    public void zfbPay(String id) {
        getOrderPay(id);
    }
}
