package com.tx.lfclient.event;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/3
 */
public class MainEvent {

    private String type;//左 or 右
    private boolean isSetting;//是从第一个页面返回还是从第四个页面返回
    private String kind;//物品种类



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSetting() {
        return isSetting;
    }

    public void setSetting(boolean setting) {
        isSetting = setting;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "MainEvent{" +
                "type='" + type + '\'' +
                ", isSetting=" + isSetting +
                ", kind='" + kind + '\'' +
                '}';
    }
}
