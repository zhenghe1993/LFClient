package com.tx.lfclient.entities;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by (IMP)郑和明
 * Date is 2017/1/19
 */
@Table(name="USER")
public class User extends IdEntity {


    @Column(
            name = "NAME",
            property = "NOT NULL"

    )
    private String name;
    @Column(
            name = "PORTRAIT",
            property = "NOT NULL"

    )
    private String portrait;
    @Column(
            name = "PHONE_NUMBER",
            property = "NOT NULL"

    )
    private String phoneNumber;
    @Column(
            name = "LOCATION",
            property = "NOT NULL"

    )
    private String location;
    @Column(
            name = "CREATE_TIME",
            property = "NOT NULL"

    )
    private Date createTime;
    @Column(
            name = "PASSWORD",
            property = "NOT NULL"

    )
    private String password;
    @Column(
            name = "TOKEN",
            property = "NOT NULL"

    )
    private  String token;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", portrait='" + portrait + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", location='" + location + '\'' +
                ", createTime=" + createTime +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
