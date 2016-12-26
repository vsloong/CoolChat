package com.cooloongwu.coolchat.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 群组中用户的实体类
 * Created by CooLoongWu on 2016-12-22.
 */

@Entity
public class GroupUsers {

    @Id//通过这个注解标记的字段必须是Long类型的，这个字段在数据库中表示它就是主键，并且它默认就是自增的
    private Long id;
    private int userId;                //用户ID
    private String userName;           //用户名
    private String userAvatar;         //用户头像的url
    private String userSex;         //用户头像的url

    @Generated(hash = 1754621901)
    public GroupUsers(Long id, int userId, String userName, String userAvatar,
                      String userSex) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.userSex = userSex;
    }

    @Generated(hash = 1096481961)
    public GroupUsers() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getUserSex() {
        return this.userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }
}
