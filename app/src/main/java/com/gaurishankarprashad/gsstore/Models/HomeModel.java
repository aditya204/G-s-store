package com.gaurishankarprashad.gsstore.Models;

import java.util.ArrayList;
import java.util.List;

public class HomeModel {


    public static final int BANNER_SLIDER = 1;
    public static final int DEAL_OF_THE_DAY = 2;
    public static final int TRENDING = 3;
    public static final int CICULAR_HORIZONTAL=4;
    public static final int FOUR_IMAGE=5;


    private int type,type1,type2;

    /////BANNER_SLIDER

    private List<sliderModel> sliderModelList;


    public HomeModel(int type, List<sliderModel> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;
    }



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<sliderModel> getSliderModelList() {
        return sliderModelList;
    }

    public void setSliderModelList(List<sliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    ////BANNER_SLIDER

    ///DEAL_OF_THE_DAY

    private String title,background_color;
    private ArrayList<String> ids;

    private List<dealsofthedayModel> dealsofthedayModelList;
    public HomeModel(int type, String title, List<dealsofthedayModel> dealsofthedayModelList,ArrayList<String> ids,String background_color) {
        this.type = type;
        this.title = title;
        this.dealsofthedayModelList = dealsofthedayModelList;
        this.ids=ids;
        this.background_color=background_color;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    public List<dealsofthedayModel> getDealsofthedayModelList() {
        return dealsofthedayModelList;
    }

    public void setDealsofthedayModelList(List<dealsofthedayModel> dealsofthedayModelList) {
        this.dealsofthedayModelList = dealsofthedayModelList;
    }

    ///DEAL_OF_THE_DAY

    private String circularTitle;
    private List<HomeCategoryModels> homeCategoryModelsList;

    public HomeModel(int type,int type1, String circularTitle, List<HomeCategoryModels> homeCategoryModelsList) {
        this.type = type;
        this.type1=type1;
        this.circularTitle = circularTitle;
        this.homeCategoryModelsList = homeCategoryModelsList;
    }


    public String getCircularTitle() {
        return circularTitle;
    }

    public void setCircularTitle(String circularTitle) {
        this.circularTitle = circularTitle;
    }

    public List<HomeCategoryModels> getHomeCategoryModelsList() {
        return homeCategoryModelsList;
    }

    public void setHomeCategoryModelsList(List<HomeCategoryModels> homeCategoryModelsList) {
        this.homeCategoryModelsList = homeCategoryModelsList;
    }


    ////FOURiMAGE

    private String name_1,name_2,name_3,name_4;
    private String tag_1,tag_2,tag_3,tag_4;
    private String image_1,image_2,image_3,image_4;
    private String fourImageTitle;

    public HomeModel(int type, String name_1, String name_2, String name_3, String name_4, String image_1, String image_2, String image_3, String image_4, String fourImageTitle,String tag_1,String tag_2,String tag_3,String tag_4) {
        this.type = type;
        this.name_1 = name_1;
        this.name_2 = name_2;
        this.name_3 = name_3;
        this.name_4 = name_4;
        this.image_1 = image_1;
        this.image_2 = image_2;
        this.image_3 = image_3;
        this.image_4 = image_4;
        this.fourImageTitle = fourImageTitle;
        this.tag_1=tag_1;
        this.tag_2=tag_2;
        this.tag_3=tag_3;
        this.tag_4=tag_4;

    }

    public String getTag_1() {
        return tag_1;
    }

    public void setTag_1(String tag_1) {
        this.tag_1 = tag_1;
    }

    public String getTag_2() {
        return tag_2;
    }

    public void setTag_2(String tag_2) {
        this.tag_2 = tag_2;
    }

    public String getTag_3() {
        return tag_3;
    }

    public void setTag_3(String tag_3) {
        this.tag_3 = tag_3;
    }

    public String getTag_4() {
        return tag_4;
    }

    public void setTag_4(String tag_4) {
        this.tag_4 = tag_4;
    }

    public String getName_1() {
        return name_1;
    }

    public void setName_1(String name_1) {
        this.name_1 = name_1;
    }

    public String getName_2() {
        return name_2;
    }

    public void setName_2(String name_2) {
        this.name_2 = name_2;
    }

    public String getName_3() {
        return name_3;
    }

    public void setName_3(String name_3) {
        this.name_3 = name_3;
    }

    public String getName_4() {
        return name_4;
    }

    public void setName_4(String name_4) {
        this.name_4 = name_4;
    }

    public String getImage_1() {
        return image_1;
    }

    public void setImage_1(String image_1) {
        this.image_1 = image_1;
    }

    public String getImage_2() {
        return image_2;
    }

    public void setImage_2(String image_2) {
        this.image_2 = image_2;
    }

    public String getImage_3() {
        return image_3;
    }

    public void setImage_3(String image_3) {
        this.image_3 = image_3;
    }

    public String getImage_4() {
        return image_4;
    }

    public void setImage_4(String image_4) {
        this.image_4 = image_4;
    }

    public String getFourImageTitle() {
        return fourImageTitle;
    }

    public void setFourImageTitle(String fourImageTitle) {
        this.fourImageTitle = fourImageTitle;
    }

    ////FOURiMAGE


}
