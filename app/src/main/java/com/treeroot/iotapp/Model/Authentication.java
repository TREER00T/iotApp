package com.treeroot.iotapp.Model;


public class Authentication {

    String id, username, password, phone, email, token;

    public Authentication(String email, String password, String phone, String username) {

        this.phone = phone;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Authentication(String password, String username) {
        this.username = username;
        this.password = password;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
