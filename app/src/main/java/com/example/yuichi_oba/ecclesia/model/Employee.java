package com.example.yuichi_oba.ecclesia.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yuichi_oba.ecclesia.activity.ReserveListActivity;
import com.example.yuichi_oba.ecclesia.tools.MyHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuichi-Oba on 2017/08/27.
 */

//*** 「社員」クラス ***//
public class Employee extends Person implements Serializable {

//    private MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getBaseContext());
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
        SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        //*** 部署テーブルを検索 ***//
        Cursor c = db.rawQuery("SELECT * FROM m_depart", null);
        if (c.moveToNext()) {
            this.dep_id = c.getString(0); //*** 部署IDの設定 ***//
        }
        c.close();
    }

    public String getPos_id() {
        return pos_id;
    }

    public void setPos_id(String pos_id) {
        SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        //*** 役職テーブルの検索 ***//
        Cursor c = db.rawQuery("SELECT * FROM m_position", null);
        if (c.moveToNext()) {
            this.pos_id = c.getString(0);   //*** 役職IDの設定 ***//
        }
        c.close();
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

    //*** --- SELF MADE METHOD --- 参加会議を抽出するメソッド ***//
    public void extractParticipationMeet(String today) {
        Context context = ReserveListActivity.getInstance();
        MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getBaseContext());
        SQLiteDatabase db = helper.getWritableDatabase();
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

    @Override
    public String toString() {
        //*** ＩＤ 氏名 役職ＩＤ 部署ＩＤ を返す ***//
        return String.format("%s : %s : %s : %s", getEmp_id(), getName(), getPos_id(), getDep_id());
    }
}
