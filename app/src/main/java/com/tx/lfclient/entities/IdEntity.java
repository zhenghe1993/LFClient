package com.tx.lfclient.entities;

import org.xutils.db.annotation.Column;

import java.io.Serializable;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/16
 */
public class IdEntity implements Serializable {

    @Column(name = "Id", isId = true, autoGen = false)
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
