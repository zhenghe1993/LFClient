package com.tx.lfclient.entities;

import java.util.Date;

/**
 * Created by (IMP)郑和明
 * Date is 2017/1/22
 */
public class SuggestMessage {
    private Long id;
    private Date createTime;

    private String content;
    private String contact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
