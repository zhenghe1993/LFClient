package com.tx.lfclient.entities;


import android.util.SparseArray;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by (IMP)郑和明
 * Date is 2017/1/19
 */
@Table(name = "MESSAGE",onCreated = "")
public class LFMessage extends IdEntity{

    @Column(name = "IS_DELETE",property = "NOT NULL")
    private int isDelete;
    @Column(name = "TYPE",property = "NOT NULL")
    private String type;  //LOST  FOUND
    @Column(name = "CREATE_TIME",property = "NOT NULL")
    private Date createTime;
    @Column(name = "TITLE",property = "NOT NULL")
    private String title;
    @Column(name = "LATITUDE",property = "NOT NULL")
    private double latitude;
    @Column(name = "LONGITUDE",property = "NOT NULL")
    private double longitude;
    @Column(name = "KIND",property = "NOT NULL")
    private String kind;
    @Column(name = "DETAIL",property = "NOT NULL")
    private String detail;
    @Column(name = "THING_TIME",property = "NOT NULL")
    private Date thingTime;
    @Column(name = "PHONE_NUMBER",property = "NOT NULL")
    private String phoneNumber;
    @Column(name = "SHARE_COUNT",property = "NOT NULL")
    private Long shareCount;
    @Column(name = "COMMENT_COUNT",property = "NOT NULL")
    private Long commentsCount;
    @Column(name = "ATTENTION_COUNT",property = "NOT NULL")
    private Long attentionCount;

    private transient User user;

    private transient List<String> imageUrls;

    private transient SparseArray<Words> wordsList;



    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getThingTime() {
        return thingTime;
    }

    public void setThingTime(Date thingTime) {
        this.thingTime = thingTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getShareCount() {
        return shareCount;
    }

    public void setShareCount(Long shareCount) {
        this.shareCount = shareCount;
    }

    public Long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Long getAttentionCount() {
        return attentionCount;
    }

    public void setAttentionCount(Long attentionCount) {
        this.attentionCount = attentionCount;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public SparseArray<Words> getWordsList() {
        return wordsList;
    }

    public void setWordsList(SparseArray<Words> wordsList) {
        this.wordsList = wordsList;
    }

    @Override
    public String toString() {
        return "LFMessage{" +
                "id=" + id +
                ", isDelete=" + isDelete +
                ", type='" + type + '\'' +
                ", createTime=" + createTime +
                ", title='" + title + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", kind='" + kind + '\'' +
                ", detail='" + detail + '\'' +
                ", thingTime=" + thingTime +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", shareCount=" + shareCount +
                ", commentsCount=" + commentsCount +
                ", attentionCount=" + attentionCount +
                ", user=" + user +
                ", imageUrls=" + imageUrls +
                ", wordsList=" + wordsList +
                '}';
    }
}
