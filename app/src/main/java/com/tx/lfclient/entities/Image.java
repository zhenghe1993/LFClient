package com.tx.lfclient.entities;

import java.io.Serializable;

/**
 * Created by (IMP)郑和明
 * Date is 2017/1/19
 */

public class Image implements Serializable{

    private Long id;

    private String imageUrl;
    private String type;

    private Long dataId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }
}
