package com.gaurishankarprashad.gsstore.Models;

public class HomeCategoryModels {

    private String imageResource;
    private String title;
    private String tag;

    public HomeCategoryModels(String imageResource, String title,String tag ) {
        this.imageResource = imageResource;
        this.title = title;
        this.tag=tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}


