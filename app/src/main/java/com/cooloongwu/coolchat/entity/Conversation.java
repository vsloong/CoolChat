package com.cooloongwu.coolchat.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 将我们的java普通类变为一个能够被greenDAO识别的数据库类型的实体类
 * Created by CooLoongWu on 2016-9-14 11:44.
 */
@Entity
public class Conversation {

    @Id//通过这个注解标记的字段必须是Long类型的，这个字段在数据库中表示它就是主键，并且它默认就是自增的
    private Long id;
    private long multiId; //群组或者好友或者其他ID
    private String name;    //用户名或者群组名
    private String avatar;  //用户或者群组等头像的url
    private String content; //最后聊天内容
    private String time;    //最后聊天的时间
    private String type;    //群组或者朋友或者其他的

    @Transient//表明这个字段不会被写入数据库，只是作为一个普通的java类字段，用来临时存储数据的，不会被持久化
    private int tempUsageCount; // not persisted

    @Generated(hash = 756651384)
    public Conversation(Long id, long multiId, String name, String avatar,
                        String content, String time, String type) {
        this.id = id;
        this.multiId = multiId;
        this.name = name;
        this.avatar = avatar;
        this.content = content;
        this.time = time;
        this.type = type;
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

    public long getMultiId() {
        return this.multiId;
    }

    public void setMultiId(long multiId) {
        this.multiId = multiId;
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

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
