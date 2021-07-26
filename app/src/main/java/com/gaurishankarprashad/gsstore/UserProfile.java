package com.gaurishankarprashad.gsstore;

public class UserProfile {

    public String email;
    public String user_name;
    public String phone;
    public String password;



    public UserProfile(String email,String user_name,String phone,String password){
        this.email =email;
        this.password=password;

        this.phone=phone;
        this.user_name=user_name;
    }


}
