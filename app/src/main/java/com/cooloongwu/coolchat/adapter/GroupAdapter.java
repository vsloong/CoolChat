package com.cooloongwu.coolchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.activity.UserProfileActivity;
import com.cooloongwu.coolchat.entity.Group;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * 聊天会话列表页的适配器
 * Created by CooLoongWu on 2016-9-12 15:39.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Group> listData;
    private LayoutInflater layoutInflater;

    public GroupAdapter(Context context, ArrayList<Group> listData) {
        this.context = context;
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(listData.get(position).getGroupName());
        Picasso.with(context)
                .load(listData.get(position).getGroupAvatar())
                .into(holder.avatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, UserProfileActivity.class);
                intent.putExtra("name", listData.get(position).getGroupName());
                intent.putExtra("avatar", listData.get(position).getGroupAvatar());
                intent.putExtra("id", listData.get(position).getGroupId());
                intent.putExtra("type", "group");
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "长按了这一项", Toast.LENGTH_SHORT).show();
                //return true 后就不会再触发setOnClickListener事件
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;
        private TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.group_img_avatar);
            name = (TextView) itemView.findViewById(R.id.group_text_name);
        }
    }
}
