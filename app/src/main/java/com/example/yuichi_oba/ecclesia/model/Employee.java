package com.example.yuichi_oba.ecclesia.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yuichi_oba.ecclesia.activity.ReserveListActivity;
import com.example.yuichi_oba.ecclesia.tools.DB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuichi-Oba on 2017/08/27.
 */

public class Employee implements Serializable{

    public static final int RE_ID = 0;
    public static final int RE_NAME = 1;
    public static final int RE_START_DAY = 2;
    public static final int RE_START_TIME = 3;
    public static final int RE_END_DAY = 4;
    public static final int RE_END_TIME = 5;
    public static final int RE_FIXTURES = 6;
    public static final int RE_REMARKS = 12;
    public static final int RE_SWITCH = 3;
    //*** Field ***//
    private String id;
    private String name;
    private String tel;
    private String mailaddr;
    private String com_name;
    private String dep_name;
    private String pos_name;
    private String pos_priority;

    //*** GetterSetter ***//
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getMailaddr() {
        return mailaddr;
    }
    public void setMailaddr(String mailaddr) {
        this.mailaddr = mailaddr;
    }
    public String getDep_name() {
        return dep_name;
    }
    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }
    public String getPos_name() {
        return pos_name;
    }
    public void setPos_name(String pos_name) {
        this.pos_name = pos_name;
    }
    public String getPos_priority() {
        return pos_priority;
    }
    public void setPos_priority(String pos_priority) {
        this.pos_priority = pos_priority;
    }
    public String getCom_name() {
        return com_name;
    }
    public void setCom_name(String com_name) {
        this.com_name = com_name;
    }

    //*** Self Made Method ***//
    //*** 参加会議を抽出するメソッド ***//
    public void extractParticipationMeet(String today) {
        Context context = ReserveListActivity.getInstance();
        SQLiteOpenHelper helper = new DB(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from v_reserve_member where mem_id = ? and re_startday = ?",
                new String[]{this.getId(), today});

        List<Reserve> reserves = new ArrayList<>();
        while (c.moveToNext()) {
            // その社員が参加した会議情報をリストに追加する
//            Reserve r = new Reserve(
//                    c.getString(RE_ID),
//                    c.getString(RE_NAME),
//                    c.getString(RE_START_DAY),
//                    c.getString(RE_START_TIME),
//                    c.getString(RE_END_DAY),
//                    c.getString(RE_END_TIME),
//                    c.getString(RE_FIXTURES),
//                    c.getString(RE_REMARKS),
//                    c.getString(RE_SWITCH)
//            );
            Reserve r = new Reserve();
            reserves.add(r);
        }
    }

    @Override
    public String toString() {
        return this.getId();
    }
}
