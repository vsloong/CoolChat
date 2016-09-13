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
import com.cooloongwu.coolchat.activity.ChatActivity;
import com.cooloongwu.coolchat.bean.ConversationBean;

import java.util.ArrayList;

/**
 * 聊天会话列表页的适配器
 * Created by CooLoongWu on 2016-9-12 15:39.
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ConversationBean> listData;
    private LayoutInflater layoutInflater;

    public ConversationAdapter(Context context, ArrayList<ConversationBean> listData) {
        this.context = context;
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_conversation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(listData.get(position).getName());
        holder.content.setText(listData.get(position).getContent());
        holder.time.setText(listData.get(position).getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, ChatActivity.class);
                intent.putExtra("name", listData.get(position).getName());
                intent.putExtra("avatar", listData.get(position).getAvatar());
                intent.putExtra("id", "userId或者roomId");
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "长按了这一项", Toast.LENGTH_SHORT).show();
                listData.remove(position);
                notifyDataSetChanged();
                return false;
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
        private TextView content;
        private TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.conversation_avatar);
            name = (TextView) itemView.findViewById(R.id.conversation_name);
            content = (TextView) itemView.findViewById(R.id.conversation_content);
            time = (TextView) itemView.findViewById(R.id.conversation_time);
        }
    }
}
