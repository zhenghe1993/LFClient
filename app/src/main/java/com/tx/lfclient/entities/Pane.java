package com.tx.lfclient.entities;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/4
 */
public class Pane {

    private String title;
    private int imageId;

    public Pane() {
    }

    public Pane(String title, int imageId) {
        this.title = title;
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }


    @Override
    public String toString() {
        return "Pane{" +
                "title='" + title + '\'' +
                ", imageId=" + imageId +
                '}';
    }
}
