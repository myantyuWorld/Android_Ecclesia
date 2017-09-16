package com.example.yuichi_oba.ecclesia.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yuichi_oba.ecclesia.activity.ReserveListActivity;
import com.example.yuichi_oba.ecclesia.tools.DB;

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
    public boolean authAdminCheck() {
        Context context = ReserveListActivity.getInstance();
        SQLiteOpenHelper helper = new DB(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from m_admin where admin_id = ? and admin_pass = ?",
                new String[]{this.adminId, this.adminPass});
        if (c.moveToNext()) {
            // ログイン成功
            return true;
        }
        c.close();
        return false;
    }
}
