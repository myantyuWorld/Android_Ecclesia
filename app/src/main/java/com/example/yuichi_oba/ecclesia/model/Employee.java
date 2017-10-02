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

//*** 「社員」クラス ***//
public class Employee  extends Person implements Serializable{

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
    private String emp_id;          //*** 社員ID ***//
    private String dep_id;          //*** 部署ID ***//
    private String pos_id;          //*** 役職ID ***//
    private String pos_name;        //*** 役職名 ***//
    private String pos_priority;    //*** 役職優先度 ***//

    //*** Constractor ***//
    public Employee() {
    }
    public Employee(String emp_id, String name, String tel, String mailaddr, String dep_id, String pos_id) {
        super(name, tel, mailaddr); //*** スーパクラスのコンストラクタコール ***//
        this.emp_id = emp_id;       //*** 社員ID ***//
        this.dep_id = dep_id;       //*** 部署ID ***//
        this.pos_id = pos_id;       //*** 役職ID ***//
    }


    //*** GetterSetter ***//
    public String getEmp_id() {
        return emp_id;
    }
    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }
    public String getDep_id() {
        return dep_id;
    }
    public void setDep_id(String dep_id) {
        this.dep_id = dep_id;
    }
    public String getPos_id() {
        return pos_id;
    }
    public void setPos_id(String pos_id) {
        this.pos_id = pos_id;
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

    //*** Self Made Method ***//
    //*** 参加会議を抽出するメソッド ***//
    public void extractParticipationMeet(String today) {
        Context context = ReserveListActivity.getInstance();
        SQLiteOpenHelper helper = new DB(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from v_reserve_member where mem_id = ? and re_startday = ?",
                new String[]{this.getEmp_id(), today});

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

}
