package com.kongqw.serialport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private HomeAddListener listener;

    public HomeContentAdapter(Context context, HomeAddListener listener) {
        this.context = context;
        this.listener = listener;
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
            holder.iv_add = (ImageView) view.findViewById(R.id.iv_add);
            holder.iv_reduce = (ImageView) view.findViewById(R.id.iv_reduce);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_number = (TextView) view.findViewById(R.id.tv_number);
            holder.tv_money = (TextView) view.findViewById(R.id.tv_money);
            holder.tv_add_number = (TextView) view.findViewById(R.id.tv_add_number);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_title.setText(list.get(position).getTitle());
        holder.tv_number.setText("剩余数量：" + list.get(position).getLeft_num());
        holder.tv_money.setText("￥" + list.get(position).getPrice());
        ImageLoader.getInstance().displayImage(list.get(position).getImages(), holder.imageView, OptionsUtils.
                options(R.mipmap.ic_friends_sends_pictures_no));
        if (list.get(position).getLeft_num() == 0) {
            holder.iv_add.setImageResource(R.mipmap.icon_add_false);
        } else {
            holder.iv_add.setImageResource(R.mipmap.icon_add);
            holder.iv_add.setTag(position);
            holder.iv_add.setOnClickListener(listener);
        }
        if(list.get(position).getNum() == 0){
            holder.iv_reduce.setVisibility(View.INVISIBLE);
            holder.tv_add_number.setVisibility(View.INVISIBLE);
        }else{
            holder.tv_add_number.setText(list.get(position).getNum()+"");
            holder.tv_add_number.setVisibility(View.VISIBLE);
            holder.iv_reduce.setTag(position);
            holder.iv_reduce.setOnClickListener(listener);
            holder.iv_reduce.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public class ViewHolder {
        TextView tv_title, tv_money, tv_number,tv_add_number;
        ImageView imageView, iv_add,iv_reduce;
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

    public static abstract class HomeAddListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_add:
                    addOnClick((Integer) v.getTag(), v);
                    break;
                case R.id.iv_reduce:
                    reduceOnClick((Integer) v.getTag(), v);
                    break;
                default:
                    break;
            }
        }


        protected abstract void addOnClick(int position, View v);

        protected abstract void reduceOnClick(int position, View v);

    }

}
