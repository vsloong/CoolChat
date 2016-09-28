package com.cooloongwu.coolchat.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 将我们的java普通类变为一个能够被greenDAO识别的数据库类型的实体类
 * Created by CooLoongWu on 2016-9-14 11:44.
 */
@Entity
public class Conversation {

    @Id//通过这个注解标记的字段必须是Long类型的，这个字段在数据库中表示它就是主键，并且它默认就是自增的
    private Long id;
    private int multiId;            //群组或者好友或者其他ID
    private int unReadNum;          //未读消息数
    private String name;            //用户名或者群组名
    private String avatar;          //用户或者群组等头像的url
    private String type;            //群组或者朋友或者其他的
    private String content;         //群组或者朋友或者其他的
    private String contentType;     //群组或者朋友或者其他的
    private String time;            //群组或者朋友或者其他的

    @Generated(hash = 1015427614)
    public Conversation(Long id, int multiId, int unReadNum, String name,
                        String avatar, String type, String content, String contentType,
                        String time) {
        this.id = id;
        this.multiId = multiId;
        this.unReadNum = unReadNum;
        this.name = name;
        this.avatar = avatar;
        this.type = type;
        this.content = content;
        this.contentType = contentType;
        this.time = time;
    }
    @Generated(hash = 1893991898)
    public Conversation() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getMultiId() {
        return this.multiId;
    }
    public void setMultiId(int multiId) {
        this.multiId = multiId;
    }

    public int getUnReadNum() {
        return this.unReadNum;
    }

    public void setUnReadNum(int unReadNum) {
        this.unReadNum = unReadNum;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
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
