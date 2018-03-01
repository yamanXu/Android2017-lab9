package com.example.xu_.lab9.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.xu_.lab9.model.Github;
import com.example.xu_.lab9.model.Repos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xu_ on 2017/12/25.
 */


public abstract class CardAdapter extends RecyclerView.Adapter<ViewHolder>{
    List<Map<String, Object>> data;
    Context context;
    int layoutId;
    LayoutInflater layoutInflater;
    MyOnItemClickListener adapter_click = null;

    public CardAdapter(Context context, int layoutId, List<Map<String, Object>> data){
        this.context = context;
        this.layoutId = layoutId;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = ViewHolder.get(context,parent,layoutId);
        return viewHolder;
    }

    public interface MyOnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setOnItemClickListener(MyOnItemClickListener myOnItemClickListener){
        this.adapter_click = myOnItemClickListener;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        convert(holder, data.get(position));
        if(adapter_click!=null){
            holder.v_this.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter_click.onClick(holder.getAdapterPosition());
                }
            });
            holder.v_this.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    adapter_click.onLongClick(holder.getAdapterPosition());
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(Github github){
        HashMap<String, Object> item = new HashMap<>();
        item.put("name", github.getLogin());
        item.put("id", "id:"+github.getId());
        item.put("blog", "blog:"+github.getBlog());
        data.add(item);
        notifyDataSetChanged();
    }
    public void add(Repos repos){
        HashMap<String, Object> item = new HashMap<>();
        item.put("name", repos.getName());
        item.put("language", cutString(repos.getLanguage(), 20));
        item.put("description", cutString(repos.getDescription(), 20));
        item.put("url", repos.getUrl());
        data.add(item);
        notifyDataSetChanged();
    }
    public void delete(int position){
        data.remove(position);
        notifyDataSetChanged();
    }
    public void clear(){
        data.removeAll(data);
        notifyDataSetChanged();
    }
    public String getData(int position, String s){
        return data.get(position).get(s).toString();
    }

    private String cutString(String s, int limit){
        String temp = s;
        if(s==null) temp = "";
        else if(s.length() > limit){
            temp = s.substring(0, limit) + "...";
        }
        return temp;
    }

    public abstract void convert(ViewHolder holder, Map<String, Object> data);

}