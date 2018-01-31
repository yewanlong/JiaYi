package com.kongqw.serialport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongqw.serialport.R;
import com.kongqw.serialport.bean.HomeFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lkn on 2017/4/1.
 */

public class HomeGridAdapter extends BaseAdapter {
    private Context context;
    private List<HomeFeature> list = new ArrayList<>();


    public HomeGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 17;
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
            holder.tv_content = (TextView) view.findViewById(R.id.tv_money);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        return view;
    }

    public class ViewHolder {
        TextView tv_content, tv_money;
        ImageView imageView;

    }

    public void setData(List<HomeFeature> list) {
        if (list == null) {
            return;
        }
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public List<HomeFeature> getData() {
        return list;
    }
}
