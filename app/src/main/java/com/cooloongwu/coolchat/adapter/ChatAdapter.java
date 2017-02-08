package com.cooloongwu.coolchat.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.base.AppConfig;
import com.cooloongwu.coolchat.entity.Chat;
import com.cooloongwu.emoji.utils.EmojiTextUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 聊天的适配器类，需根据消息类型加载不同的布局
 * Created by CooLoongWu on 2016-9-13 16:31.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Chat> listData;

    //建立枚举 2个item 类型
    private enum ITEM_TYPE {
        PEER_TEXT,
        SELF_TEXT,
        PEER_IMAGE,
        SELF_IMAGE,
        PEER_AUDIO,
        SELF_AUDIO,
        PEER_VIDEO,
        SELF_VIDEO
    }

    public ChatAdapter(Context context, ArrayList<Chat> listData) {
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
            } else if ("video".equals(contentType)) {
                return ITEM_TYPE.SELF_VIDEO.ordinal();
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
            } else if ("video".equals(contentType)) {
                return ITEM_TYPE.PEER_VIDEO.ordinal();
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
        } else if (viewType == ITEM_TYPE.PEER_VIDEO.ordinal()) {
            itemView = layoutInflater.inflate(R.layout.item_chat_message_peer_video, parent, false);
            return new PeerVideoViewHolder(itemView);
        } else if (viewType == ITEM_TYPE.SELF_VIDEO.ordinal()) {
            itemView = layoutInflater.inflate(R.layout.item_chat_message_self_video, parent, false);
            return new SelfVideoViewHolder(itemView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PeerTextViewHolder) {
            //朋友或者其他发送的 文字
            ((PeerTextViewHolder) holder).text_name.setText(listData.get(position).getFromName());
            //正则匹配下，如果有表情则显示表情
            ((PeerTextViewHolder) holder).text_content.setText(EmojiTextUtils.getEditTextContent(
                    listData.get(position).getContent(),
                    context,
                    ((PeerTextViewHolder) holder).text_content)
            );
            Picasso.with(context).load(listData.get(position).getFromAvatar()).into(((PeerTextViewHolder) holder).img_avatar);
        } else if (holder instanceof SelfTextViewHolder) {
            //自己发送的 文字
            ((SelfTextViewHolder) holder).text_name.setText(listData.get(position).getFromName());
            //正则匹配下，如果有表情则显示表情
            ((SelfTextViewHolder) holder).text_content.setText(EmojiTextUtils.getEditTextContent(
                    listData.get(position).getContent(),
                    context,
                    ((SelfTextViewHolder) holder).text_content));
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
            ((PeerAudioViewHolder) holder).text_content.setText("" + listData.get(position).getAudioLength() + "''");
            int audioLength = Integer.parseInt(listData.get(position).getAudioLength()) + 4;
            ((PeerAudioViewHolder) holder).text_content.setEms(audioLength < 15 ? audioLength : 15);
            Picasso.with(context).load(listData.get(position).getFromAvatar()).into(((PeerAudioViewHolder) holder).img_avatar);
            ((PeerAudioViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playSound(listData.get(position).getContent());
                }
            });
        } else if (holder instanceof SelfAudioViewHolder) {
            //自己发送的 语音
            ((SelfAudioViewHolder) holder).text_name.setText(listData.get(position).getFromName());
            ((SelfAudioViewHolder) holder).text_content.setText("" + listData.get(position).getAudioLength() + "''");
            int audioLength = Integer.parseInt(listData.get(position).getAudioLength()) + 4;
            ((SelfAudioViewHolder) holder).text_content.setEms(audioLength < 15 ? audioLength : 15);
            Picasso.with(context).load(listData.get(position).getFromAvatar()).into(((SelfAudioViewHolder) holder).img_avatar);
            ((SelfAudioViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playSound(listData.get(position).getContent());
                }
            });
        } else if (holder instanceof PeerVideoViewHolder) {
            //朋友或者其他发送的 视频
            ((PeerVideoViewHolder) holder).text_name.setText(listData.get(position).getFromName());
            Picasso.with(context).load(listData.get(position).getFromAvatar()).into(((PeerVideoViewHolder) holder).img_avatar);
            final IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
            ijkMediaPlayer.setKeepInBackground(false);
            try {
                ijkMediaPlayer.setDataSource(context, Uri.parse(listData.get(position).getContent()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            ((PeerVideoViewHolder) holder).surface_holder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {

                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                    ijkMediaPlayer.setDisplay(((PeerVideoViewHolder) holder).surface_holder);
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                }
            });

            ((PeerVideoViewHolder) holder).imgbtn_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ijkMediaPlayer.setDisplay(((PeerVideoViewHolder) holder).surface_holder);
                    ((PeerVideoViewHolder) holder).imgbtn_play.setVisibility(View.GONE);
                }
            });
        } else if (holder instanceof SelfVideoViewHolder) {
            //自己发送的 视频
            ((SelfVideoViewHolder) holder).text_name.setText(listData.get(position).getFromName());
            Picasso.with(context).load(listData.get(position).getFromAvatar()).into(((SelfVideoViewHolder) holder).img_avatar);
            final IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
            ijkMediaPlayer.setKeepInBackground(false);
            try {
                ijkMediaPlayer.setDataSource(context, Uri.parse(listData.get(position).getContent()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            ((SelfVideoViewHolder) holder).surface_holder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {

                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                    ijkMediaPlayer.setDisplay(((SelfVideoViewHolder) holder).surface_holder);
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                }
            });
            ijkMediaPlayer.prepareAsync();
            ((SelfVideoViewHolder) holder).imgbtn_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ijkMediaPlayer.setDisplay(((SelfVideoViewHolder) holder).surface_holder);
                    ((SelfVideoViewHolder) holder).imgbtn_play.setVisibility(View.GONE);
                }
            });
        }
    }

    private void playSound(String path) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    private class PeerTextViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_avatar;
        private TextView text_name;
        public TextView text_content;

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

    private class PeerVideoViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_avatar;
        private TextView text_name;
        private SurfaceView surface_view;
        private SurfaceHolder surface_holder;
        private ImageView img_thumbnail;
        private ImageButton imgbtn_play;

        PeerVideoViewHolder(View itemView) {
            super(itemView);
            img_avatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            surface_view = (SurfaceView) itemView.findViewById(R.id.surface_view);
            img_thumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            imgbtn_play = (ImageButton) itemView.findViewById(R.id.imgbtn_play);
            surface_holder = surface_view.getHolder();
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
        public TextView text_content;

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

    private class SelfVideoViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_avatar;
        private TextView text_name;
        private SurfaceView surface_view;
        private SurfaceHolder surface_holder;
        private ImageView img_thumbnail;
        private ImageButton imgbtn_play;

        SelfVideoViewHolder(View itemView) {
            super(itemView);
            img_avatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            surface_view = (SurfaceView) itemView.findViewById(R.id.surface_view);
            img_thumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            imgbtn_play = (ImageButton) itemView.findViewById(R.id.imgbtn_play);
            surface_holder = surface_view.getHolder();
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
