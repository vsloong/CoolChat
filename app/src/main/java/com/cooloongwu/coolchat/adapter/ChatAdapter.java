package com.cooloongwu.coolchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.bean.ChatBean;

import java.util.ArrayList;

/**
 * 聊天的适配器类，需根据消息类型加载不同的布局
 * Created by CooLoongWu on 2016-9-13 16:31.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<ChatBean> listData;

    //建立枚举 2个item 类型
    public enum ITEM_TYPE {
        PEER_TEXT,
        SELF_TEXT
    }

    public ChatAdapter(Context context, ArrayList<ChatBean> listData) {
        this.context = context;
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        String type = listData.get(position).getType();
        switch (type) {
            case "peer_text":
                return ITEM_TYPE.PEER_TEXT.ordinal();
            case "self_text":
                return ITEM_TYPE.SELF_TEXT.ordinal();
            default:
                return 0x00;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == ITEM_TYPE.PEER_TEXT.ordinal()) {
            itemView = layoutInflater.inflate(R.layout.item_chat_message_peer, parent, false);
            return new PeerViewHolder(itemView);
        } else if (viewType == ITEM_TYPE.SELF_TEXT.ordinal()) {
            itemView = layoutInflater.inflate(R.layout.item_chat_message_self, parent, false);
            return new SelfViewHolder(itemView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PeerViewHolder) {
            ((PeerViewHolder) holder).text_name.setText(listData.get(position).getName());
            ((PeerViewHolder) holder).text_content.setText(listData.get(position).getContent());
        } else if (holder instanceof SelfViewHolder) {
            ((SelfViewHolder) holder).text_name.setText(listData.get(position).getName());
            ((SelfViewHolder) holder).text_content.setText(listData.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class PeerViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_avatar;
        private TextView text_name;
        private TextView text_content;

        public PeerViewHolder(View itemView) {
            super(itemView);
            img_avatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_content = (TextView) itemView.findViewById(R.id.text_content);

        }
    }

    public class SelfViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_avatar;
        private TextView text_name;
        private TextView text_content;

        public SelfViewHolder(View itemView) {
            super(itemView);
            img_avatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_content = (TextView) itemView.findViewById(R.id.text_content);
        }
    }

}
