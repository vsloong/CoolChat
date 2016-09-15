package com.cooloongwu.coolchat.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 聊天的实体类
 * Created by CooLoongWu on 2016-9-15.
 */
@Entity
public class ChatGroup {

    @Id
    private Long id;
    private long userId;            //用户ID
    private long toGroupId;         //要发送给群组的ID
    private String userName;        //用户昵称
    private String userAvatar;      //用户头像
    private String content;         //发送内容
    private String contentType;     //发送内容的类型
    private String time;            //发送时间

    @Generated(hash = 1965504198)
    public ChatGroup(Long id, long userId, long toGroupId, String userName,
                     String userAvatar, String content, String contentType, String time) {
        this.id = id;
        this.userId = userId;
        this.toGroupId = toGroupId;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.content = content;
        this.contentType = contentType;
        this.time = time;
    }

    @Generated(hash = 266793083)
    public ChatGroup() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getToGroupId() {
        return this.toGroupId;
    }

    public void setToGroupId(long toGroupId) {
        this.toGroupId = toGroupId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return this.userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
