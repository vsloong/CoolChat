package com.cooloongwu.coolchat.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 将我们的java普通类变为一个能够被greenDAO识别的数据库类型的实体类
 * Created by CooLoongWu on 2016-9-26 17:04.
 */
@Entity
public class Contact {

    @Id//通过这个注解标记的字段必须是Long类型的，这个字段在数据库中表示它就是主键，并且它默认就是自增的
    private Long id;
    private int userId;             //好友ID
    private String name;            //用户名
    private String remarkName;      //备注名
    private String avatar;          //用户头像的url
    private String sex;             //用户性别
    private String phone;           //用户手机

    @Generated(hash = 225590820)
    public Contact(Long id, int userId, String name, String remarkName,
                   String avatar, String sex, String phone) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.remarkName = remarkName;
        this.avatar = avatar;
        this.sex = sex;
        this.phone = phone;
    }

    @Generated(hash = 672515148)
    public Contact() {
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

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemarkName() {
        return this.remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }


}
