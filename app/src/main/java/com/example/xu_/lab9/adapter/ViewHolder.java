package com.example.xu_.lab9.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Completable;

/**
 * Created by xu_ on 2017/12/25.
 */

public class ViewHolder extends RecyclerView.ViewHolder{
    public SparseArray<View> v_basket;   //回收站 删除的item放入basket
    public View v_this;          //当前视图
    public Context context;

    public ViewHolder(Context context, View v_item, ViewGroup viewGroup){   //构造函数
        super(v_item);    //调用父类的构造函数
        v_this = v_item;
        v_basket = new SparseArray<>();
        this.context = context;
    }
    public <T extends View> T getView(int view_id){  //T为模板类
        View v = v_basket.get(view_id);              //从basket中找id，找到了就拿出来
        if(v == null){                               //快速调用，优化速度
            v = v_this.findViewById(view_id);
            v_basket.put(view_id, v);
        }
        return (T) v;
    }
    public static ViewHolder get(Context context, ViewGroup viewGroup, int layoutId){
        View view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(context, view, viewGroup);
        return viewHolder;
    }
}
