package com.cooloongwu.coolchat.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 将我们的java普通类变为一个能够被greenDAO识别的数据库类型的实体类
 * Created by CooLoongWu on 2016-9-26 17:04.
 */
@Entity
public class Group {

    @Id//通过这个注解标记的字段必须是Long类型的，这个字段在数据库中表示它就是主键，并且它默认就是自增的
    private Long id;
    private int groupId;                //群组ID
    private String groupName;           //群组名
    private String groupAvatar;         //群组头像的url
    private String createTime;          //群组创建时间

    @Generated(hash = 71752244)
    public Group(Long id, int groupId, String groupName, String groupAvatar,
                 String createTime) {
        this.id = id;
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupAvatar = groupAvatar;
        this.createTime = createTime;
    }

    @Generated(hash = 117982048)
    public Group() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGroupId() {
        return this.groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupAvatar() {
        return this.groupAvatar;
    }

    public void setGroupAvatar(String groupAvatar) {
        this.groupAvatar = groupAvatar;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


}
