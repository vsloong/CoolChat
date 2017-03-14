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
import com.cooloongwu.coolchat.activity.UserProfileActivity;
import com.cooloongwu.coolchat.entity.Contact;
import com.cooloongwu.coolchat.utils.ImgUrlUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * 聊天会话列表页的适配器
 * Created by CooLoongWu on 2016-9-12 15:39.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Contact> listData;
    private LayoutInflater layoutInflater;

    public ContactAdapter(Context context, ArrayList<Contact> listData) {
        this.context = context;
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(listData.get(position).getName());
        Picasso.with(context)
                .load(ImgUrlUtils.getUrl(listData.get(position).getAvatar()))
                .into(holder.avatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, UserProfileActivity.class);
                intent.putExtra("name", listData.get(position).getName());
                intent.putExtra("avatar", listData.get(position).getAvatar());
                intent.putExtra("id", listData.get(position).getUserId());
                intent.putExtra("sex", listData.get(position).getSex());
                intent.putExtra("phone", listData.get(position).getPhone());
                intent.putExtra("type", "friend");
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
            avatar = (ImageView) itemView.findViewById(R.id.contact_img_avatar);
            name = (TextView) itemView.findViewById(R.id.contact_text_name);
        }
    }
}
