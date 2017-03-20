package com.tx.lfclient.entities;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by (IMP)郑和明
 * Date is 2017/1/19
 */
@Table(name="WORDS")
public class Words implements Serializable{

    @Column(
            name = "ID",
            isId = true,
            autoGen = false
    )
    private Long id;
    @Column(
            name = "IS_DELETE",
            property = "NOT NULL"

    )
    private int isDelete;
    @Column(
            name = "TYPE",
            property = "NOT NULL"

    )
    private String type;//LF 失物招领   DIS发现
    @Column(
            name = "KIND",
            property = "NOT NULL"

    )
    private String kind;//TOW 评论 words数据 TOM 评论 message数据
    @Column(
            name = "FORM_USER_ID",
            property = "NOT NULL"

    )
    private Long fromUserId;
    @Column(
            name = "TO_USER_ID",
            property = "NOT NULL"

    )
    private Long toUserId;
    @Column(
            name = "CREATE_TIME",
            property = "NOT NULL"

    )
    private Date createTime;
    @Column(
            name = "DETAIL",
            property = "NOT NULL"

    )
    private String detail;
    @Column(
            name = "DATA_ID",
            property = "NOT NULL"

    )
    private Long dataId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
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

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    @Override
    public String toString() {
        return "Words{" +
                "id=" + id +
                ", isDelete=" + isDelete +
                ", type='" + type + '\'' +
                ", kind='" + kind + '\'' +
                ", fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", createTime=" + createTime +
                ", detail='" + detail + '\'' +
                ", dataId=" + dataId +
                '}';
    }
}
