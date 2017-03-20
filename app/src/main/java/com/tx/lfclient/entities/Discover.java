package com.tx.lfclient.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by (IMP)郑和明
 * Date is 2017/1/19
 */

public class Discover implements Serializable {


    private Long id;
    private int isDelete;
    private String title;

    private Date createTime;
    private String detail;

    private Long shareCount;

    private Long commentsCount;

    private Long attentionCount;
    private transient User user;
    private transient List<String> imageUrls;

    private transient  List<Words> wordsList;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public List<Words> getWordsList() {
        return wordsList;
    }

    public void setWordsList(List<Words> wordsList) {
        this.wordsList = wordsList;
    }

    @Override
    public String toString() {
        return "Discover{" +
                "id=" + id +
                ", isDelete=" + isDelete +
                ", title='" + title + '\'' +
                ", createTime=" + createTime +
                ", detail='" + detail + '\'' +
                ", shareCount=" + shareCount +
                ", commentsCount=" + commentsCount +
                ", attentionCount=" + attentionCount +
                ", user=" + user +
                ", imageUrls=" + imageUrls +
                ", wordsList=" + wordsList +
                '}';
    }
}
