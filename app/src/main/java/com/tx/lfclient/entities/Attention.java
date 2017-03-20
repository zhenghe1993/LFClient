package com.tx.lfclient.entities;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by (IMP)郑和明
 * Date is 2017/1/19
 */
@Table(name = "ATTENTION")
public class Attention extends IdEntity {

    @Column(name = "TYPE", property = "NOT NULL")
    private String type;
    @Column(name = "USER_ID", property = "NOT NULL")
    private Long UserId;
    @Column(name = "DATA_ID", property = "NOT NULL")
    private Long dataId;


    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "Attention{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", UserId=" + UserId +
                ", dataId=" + dataId +
                '}';
    }
}
