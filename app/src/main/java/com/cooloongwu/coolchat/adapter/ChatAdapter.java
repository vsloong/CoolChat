package com.cooloongwu.coolchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.base.AppConfig;
import com.cooloongwu.coolchat.entity.ChatFriend;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * 聊天的适配器类，需根据消息类型加载不同的布局
 * Created by CooLoongWu on 2016-9-13 16:31.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<ChatFriend> listData;

    //建立枚举 2个item 类型
    private enum ITEM_TYPE {
        PEER_TEXT,
        SELF_TEXT,
        PEER_IMAGE,
        SELF_IMAGE,
        PEER_AUDIO,
        SELF_AUDIO
    }

    public ChatAdapter(Context context, ArrayList<ChatFriend> listData) {
        this.context = context;
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        int userId = listData.get(position).getFromId();
        String contentType = listData.get(position).getContentType();
        if (userId == AppConfig.getUserId(context)) {
            if ("text".equals(contentType)) {
                return ITEM_TYPE.SELF_TEXT.ordinal();
            } else if ("image".equals(contentType)) {
                return ITEM_TYPE.SELF_IMAGE.ordinal();
            } else if ("audio".equals(contentType)) {
                return ITEM_TYPE.SELF_AUDIO.ordinal();
            } else {
                return 0;
            }
        } else {
            if ("text".equals(contentType)) {
                return ITEM_TYPE.PEER_TEXT.ordinal();
            } else if ("image".equals(contentType)) {
                return ITEM_TYPE.PEER_IMAGE.ordinal();
            } else if ("audio".equals(contentType)) {
                return ITEM_TYPE.PEER_AUDIO.ordinal();
            } else {
                return 0;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == ITEM_TYPE.PEER_TEXT.ordinal()) {
            itemView = layoutInflater.inflate(R.layout.item_chat_message_peer_text, parent, false);
            return new PeerTextViewHolder(itemView);
        } else if (viewType == ITEM_TYPE.SELF_TEXT.ordinal()) {
            itemView = layoutInflater.inflate(R.layout.item_chat_message_self_text, parent, false);
            return new SelfTextViewHolder(itemView);
        } else if (viewType == ITEM_TYPE.PEER_IMAGE.ordinal()) {
            itemView = layoutInflater.inflate(R.layout.item_chat_message_peer_image, parent, false);
            return new PeerImageViewHolder(itemView);
        } else if (viewType == ITEM_TYPE.SELF_IMAGE.ordinal()) {
            itemView = layoutInflater.inflate(R.layout.item_chat_message_self_image, parent, false);
            return new SelfImageViewHolder(itemView);
        } else if (viewType == ITEM_TYPE.PEER_AUDIO.ordinal()) {
            itemView = layoutInflater.inflate(R.layout.item_chat_message_peer_audio, parent, false);
            return new PeerAudioViewHolder(itemView);
        } else if (viewType == ITEM_TYPE.SELF_AUDIO.ordinal()) {
            itemView = layoutInflater.inflate(R.layout.item_chat_message_self_audio, parent, false);
            return new SelfAudioViewHolder(itemView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("消息类型", listData.get(position).getContentType());
        Log.e("消息内容", listData.get(position).getContent());
        if (holder instanceof PeerTextViewHolder) {
            //朋友或者其他发送的 文字
            ((PeerTextViewHolder) holder).text_name.setText(listData.get(position).getFromName());
            ((PeerTextViewHolder) holder).text_content.setText(listData.get(position).getContent());
            Picasso.with(context).load(listData.get(position).getFromAvatar()).into(((PeerTextViewHolder) holder).img_avatar);
        } else if (holder instanceof SelfTextViewHolder) {
            //自己发送的 文字
            ((SelfTextViewHolder) holder).text_name.setText(listData.get(position).getFromName());
            ((SelfTextViewHolder) holder).text_content.setText(listData.get(position).getContent());
            Picasso.with(context).load(listData.get(position).getFromAvatar()).into(((SelfTextViewHolder) holder).img_avatar);
        } else if (holder instanceof PeerImageViewHolder) {
            //朋友或者其他发送的 图片
            ((PeerImageViewHolder) holder).text_name.setText(listData.get(position).getFromName());
            Picasso.with(context).load(listData.get(position).getFromAvatar()).into(((PeerImageViewHolder) holder).img_avatar);
            Picasso.with(context).load(listData.get(position).getContent()).into(((PeerImageViewHolder) holder).image_content);
        } else if (holder instanceof SelfImageViewHolder) {
            //自己发送的 图片
            ((SelfImageViewHolder) holder).text_name.setText(listData.get(position).getFromName());
            Picasso.with(context).load(listData.get(position).getFromAvatar()).into(((SelfImageViewHolder) holder).img_avatar);
            Picasso.with(context).load(listData.get(position).getContent()).into(((SelfImageViewHolder) holder).image_content);
        } else if (holder instanceof PeerAudioViewHolder) {
            //朋友或者其他发送的 语音
            ((PeerAudioViewHolder) holder).text_name.setText(listData.get(position).getFromName());
            ((PeerAudioViewHolder) holder).text_content.setText(listData.get(position).getContent());
            Picasso.with(context).load(listData.get(position).getFromAvatar()).into(((PeerAudioViewHolder) holder).img_avatar);
        } else if (holder instanceof SelfAudioViewHolder) {
            //自己发送的 语音
            ((SelfAudioViewHolder) holder).text_name.setText(listData.get(position).getFromName());
            ((SelfAudioViewHolder) holder).text_content.setText(listData.get(position).getContent());
            Picasso.with(context).load(listData.get(position).getFromAvatar()).into(((SelfAudioViewHolder) holder).img_avatar);
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    private class PeerTextViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_avatar;
        private TextView text_name;
        private TextView text_content;

        PeerTextViewHolder(View itemView) {
            super(itemView);
            img_avatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_content = (TextView) itemView.findViewById(R.id.text_content);

        }
    }

    private class PeerImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_avatar;
        private TextView text_name;
        private ImageView image_content;

        PeerImageViewHolder(View itemView) {
            super(itemView);
            img_avatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            image_content = (ImageView) itemView.findViewById(R.id.image_content);
        }
    }

    private class PeerAudioViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_avatar;
        private TextView text_name;
        private TextView text_content;

        PeerAudioViewHolder(View itemView) {
            super(itemView);
            img_avatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_content = (TextView) itemView.findViewById(R.id.text_content);

        }
    }

    private class SelfTextViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_avatar;
        private TextView text_name;
        private TextView text_content;

        SelfTextViewHolder(View itemView) {
            super(itemView);
            img_avatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_content = (TextView) itemView.findViewById(R.id.text_content);
        }
    }

    private class SelfImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_avatar;
        private TextView text_name;
        private ImageView image_content;

        SelfImageViewHolder(View itemView) {
            super(itemView);
            img_avatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            image_content = (ImageView) itemView.findViewById(R.id.image_content);
        }
    }

    private class SelfAudioViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_avatar;
        private TextView text_name;
        private TextView text_content;

        SelfAudioViewHolder(View itemView) {
            super(itemView);
            img_avatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_content = (TextView) itemView.findViewById(R.id.text_content);

        }
    }
}
