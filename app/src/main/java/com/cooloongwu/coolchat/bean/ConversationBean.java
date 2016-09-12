package com.cooloongwu.coolchat.bean;

/**
 * 会话列表的Bean类
 * Created by CooLoongWu on 2016-9-12 15:36.
 */
public class ConversationBean {
    private String avatar;
    private String name;
    private String content;
    private String time;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
