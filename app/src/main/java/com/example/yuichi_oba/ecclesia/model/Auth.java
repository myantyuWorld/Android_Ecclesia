package com.example.yuichi_oba.ecclesia.model;

/**
 * Created by Yuichi-Oba on 2017/09/15.
 */

public class Auth {

    //*** Field ***//
    private String adminId;
    private String adminPass;

    //*** GetterSetter ***//
    public String getAdminId() {
        return adminId;
    }
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
    public String getAdminPass() {
        return adminPass;
    }
    public void setAdminPass(String adminPass) {
        this.adminPass = adminPass;
    }

    //*** SelfMadeMethod ***//
    //*** 管理者を認証するメソッド ***//
}
