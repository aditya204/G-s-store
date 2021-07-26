package com.gaurishankarprashad.gsstore.Models;

public class AddProductDescriptionModel {
   private String descContent;

    public AddProductDescriptionModel( String descContent) {

        this.descContent = descContent;
    }



    public String getDescContent() {
        return descContent;
    }

    public void setDescContent(String descContent) {
        this.descContent = descContent;
    }
}
