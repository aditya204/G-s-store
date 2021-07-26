package com.gaurishankarprashad.gsstore;

public class Upload {

    private String name;
    private String mImageUrl;

    public Upload() {
    }

    public Upload(String name, String mImageUrl) {
        if(name.trim().equals( "" )){
            name="no name";
        }
        this.name = name;
        this.mImageUrl = mImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
