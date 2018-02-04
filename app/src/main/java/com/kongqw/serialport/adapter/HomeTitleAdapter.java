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
import com.kongqw.serialport.bean.HomeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lkn on 2017/4/1.
 */

public class HomeTitleAdapter extends BaseAdapter {
    private Context context;
    private List<HomeList> list = new ArrayList<>();
    private int isTrue;


    public HomeTitleAdapter(Context context) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_home_title, parent, false);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (position == isTrue) {
            holder.tv_title.setBackgroundResource(R.color.grey);
        } else {
            holder.tv_title.setBackgroundResource(R.color.white);
        }
        holder.tv_title.setText(list.get(position).getP_type());
        return view;
    }

    public class ViewHolder {
        TextView tv_title;

    }

    public void isTrue(int isTrue) {
        this.isTrue = isTrue;
        notifyDataSetChanged();
    }

    public void setData(List<HomeList> list) {
        if (list == null) {
            return;
        }
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public List<HomeList> getData() {
        return list;
    }
}
