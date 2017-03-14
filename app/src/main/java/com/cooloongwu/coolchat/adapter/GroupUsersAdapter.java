package com.cooloongwu.coolchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.activity.FriendAddActivity;
import com.cooloongwu.coolchat.entity.GroupUsers;
import com.cooloongwu.coolchat.utils.ImgUrlUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * 群组中用户列表的适配器
 * Created by CooLoongWu on 2016-9-12 15:39.
 */
public class GroupUsersAdapter extends RecyclerView.Adapter<GroupUsersAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GroupUsers> listData;
    private LayoutInflater layoutInflater;

    public GroupUsersAdapter(Context context, ArrayList<GroupUsers> listData) {
        this.context = context;
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_group_profile_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(listData.get(position).getUserName());
        Picasso.with(context)
                .load(ImgUrlUtils.getUrl(listData.get(position).getUserAvatar()))
                .into(holder.avatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FriendAddActivity.class);
                intent.putExtra("userId", listData.get(position).getUserId());
                intent.putExtra("userName", listData.get(position).getUserName());
                intent.putExtra("userAvatar", listData.get(position).getUserAvatar());
                intent.putExtra("userSex", listData.get(position).getUserSex());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
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
            avatar = (ImageView) itemView.findViewById(R.id.group_profile_user_img_avatar);
            name = (TextView) itemView.findViewById(R.id.group_profile_user_text_name);
        }
    }
}
