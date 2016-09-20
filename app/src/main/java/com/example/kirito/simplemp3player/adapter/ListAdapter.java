package com.example.kirito.simplemp3player.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kirito.simplemp3player.R;
import com.example.kirito.simplemp3player.entity.Mp3Item;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kirito on 2016/9/20.
 */
public class ListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Mp3Item> items;

    public ListAdapter(Context mContext, List<Mp3Item> items) {
        this.mContext = mContext;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitems,null);
            holder = new viewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (viewHolder) convertView.getTag();
        }
        Mp3Item item = items.get(position);
        holder.iv.setImageResource(R.drawable.music);
        holder.tv_date.setText(item.getLast_modify_date());
        holder.tv_name.setText(item.getName());
        holder.tv_size.setText(item.getSize());
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class viewHolder{
        @BindView(R.id.thumb) ImageView iv;
        @BindView(R.id.tv_date) TextView tv_date;
        @BindView(R.id.tv_name) TextView tv_name;
        @BindView(R.id.tv_size) TextView tv_size;

        public viewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}
