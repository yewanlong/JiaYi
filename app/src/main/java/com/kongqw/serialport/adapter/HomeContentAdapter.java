package com.kongqw.serialport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongqw.serialport.R;
import com.kongqw.serialport.bean.HomeBean;
import com.kongqw.serialport.bean.HomeListData;
import com.kongqw.serialport.weight.OptionsUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lkn on 2017/4/1.
 */

public class HomeContentAdapter extends BaseAdapter {
    private Context context;
    private List<HomeListData> list = new ArrayList<>();


    public HomeContentAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.itme_feature, parent, false);
            holder.imageView = (ImageView) view.findViewById(R.id.imageView);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_number = (TextView) view.findViewById(R.id.tv_number);
            holder.tv_money = (TextView) view.findViewById(R.id.tv_money);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_title.setText(list.get(position).getTitle());
        holder.tv_number.setText("剩余数量：" + list.get(position).getNum());
        holder.tv_money.setText("￥" + list.get(position).getPrice());
        ImageLoader.getInstance().displayImage(list.get(position).getImages(), holder.imageView, OptionsUtils.
                options(R.mipmap.ic_friends_sends_pictures_no));
        return view;
    }

    public class ViewHolder {
        TextView tv_title, tv_money, tv_number;
        ImageView imageView;

    }

    public void setData(List<HomeListData> list) {
        if (list == null) {
            return;
        }
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public List<HomeListData> getData() {
        return list;
    }
}
