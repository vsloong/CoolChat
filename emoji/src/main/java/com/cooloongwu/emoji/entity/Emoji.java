package com.cooloongwu.emoji.entity;

/**
 * Emoji的实体类
 * Created by CooLoongWu on 2017-2-6 16:44.
 */

public class Emoji {

    private int emojiUri;
    private String content;

    public int getEmojiUri() {
        return emojiUri;
    }

    public void setEmojiUri(int emojiUri) {
        this.emojiUri = emojiUri;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
