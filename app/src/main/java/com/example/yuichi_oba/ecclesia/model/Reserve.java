package com.example.yuichi_oba.ecclesia.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yuichi_oba.ecclesia.activity.ReserveListActivity;
import com.example.yuichi_oba.ecclesia.tools.DB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuichi-Oba on 2017/09/15.
 */

public class Reserve {

    //*** Field ***//
    private String re_id;
    private String re_name;
    private String re_startDay;
    private String re_startTime;
    private String re_endDay;
    private String re_endTime;
    private String re_fixtures;
    private String re_remarks;
    private String re_switch;
    private Integer re_mem_priority;
    private String re_purpose_id;
    private String re_purpose_name;
    private String re_pur_priority;
    private String re_applicant;
    private String re_room_id;
    private String re_room_name;
    private List<String> re_member;
    private float[] coop;

    public Reserve() {
    }

    //*** GetterSetter ***//
    public String getRe_id() {
        return re_id;
    }
    public void setRe_id(String re_id) {
        this.re_id = re_id;
    }
    public String getRe_name() {
        return re_name;
    }
    public void setRe_name(String re_name) {
        this.re_name = re_name;
    }
    public String getRe_startDay() {
        return re_startDay;
    }
    public void setRe_startDay(String re_startDay) {
        this.re_startDay = re_startDay;
    }
    public String getRe_startTime() {
        return re_startTime;
    }
    public void setRe_startTime(String re_startTime) {
        this.re_startTime = re_startTime;
    }
    public String getRe_endDay() {
        return re_endDay;
    }
    public void setRe_endDay(String re_endDay) {
        this.re_endDay = re_endDay;
    }
    public String getRe_endTime() {
        return re_endTime;
    }
    public void setRe_endTime(String re_endTime) {
        this.re_endTime = re_endTime;
    }
    public String getRe_fixtures() {
        return re_fixtures;
    }
    public void setRe_fixtures(String re_fixtures) {
        this.re_fixtures = re_fixtures;
    }
    public String getRe_remarks() {
        return re_remarks;
    }
    public void setRe_remarks(String re_remarks) {
        this.re_remarks = re_remarks;
    }
    public String getRe_switch() {
        return re_switch;
    }
    public void setRe_switch(String re_switch) {
        this.re_switch = re_switch;
    }
    public Integer getRe_mem_priority() {
        return re_mem_priority;
    }
    public void setRe_mem_priority(Integer re_mem_priority) {
        this.re_mem_priority = re_mem_priority;
    }
    public String getRe_purpose_id() {
        return re_purpose_id;
    }
    public void setRe_purpose_id(String re_purpose_id) {
        this.re_purpose_id = re_purpose_id;
    }
    public String getRe_purpose_name() {
        return re_purpose_name;
    }
    public void setRe_purpose_name(String re_purpose_name) {
        this.re_purpose_name = re_purpose_name;
    }
    public String getRe_pur_priority() {
        return re_pur_priority;
    }
    public void setRe_pur_priority(String re_pur_priority) {
        this.re_pur_priority = re_pur_priority;
    }
    public String getRe_applicant() {
        return re_applicant;
    }
    public void setRe_applicant(String re_applicant) {
        this.re_applicant = re_applicant;
    }
    public String getRe_room_id() { return re_room_id; }
    public void setRe_room_id(String re_room_id) { this.re_room_id = re_room_id; }
    public String getRe_room_name() {return re_room_name; }
    public void setRe_room_name(String re_room_name) { this.re_room_name = re_room_name; }
    public float[] getCoop() {
        return coop;
    }
    public void setCoop(float[] coop) {
        this.coop = coop;
    }
    public List<String> getRe_member() {
        return re_member;
    }
    public void setRe_member(List<String> re_member) {
        this.re_member = re_member;
    }
    //*** SelfMadeMethod ***//

    //*** 参加者優先度を計算するメソッド ***//

    //*** 会議の時間帯の重複をチェックするメソッド ***//

    //*** 優先度をチェックするメソッド ***//

    //*** 予約を確定するメソッド ***//

    //*** 予約をキャンセルするメソッド ***//

    //*** 早期退出するメソッド ***//

    //*** 終了時間を延長するメソッド ***//

    //*** 予約を変更するメソッド ***//

    //*** 通知メールを送るメソッド ***//

    //*** 通知をするメソッド ***//

    //*** 追い出しを行うメソッド ***//

    //*** 予約内容をDB検索するメソッド ***//
    public static Reserve retReserveConfirm(String re_id) {
        Reserve reserve = new Reserve();

        Context context = ReserveListActivity.getInstance();
        SQLiteOpenHelper helper = new DB(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from v_reserve_member x inner join m_room y on x.room_id = y.room_id where re_id = ?",
                new String[]{re_id});
        List<String> list = new ArrayList<>();
        while (c.moveToNext()) {
            //*** 予約の共通部分 ***//
            reserve.setRe_name(c.getString(1));
            reserve.setRe_startDay(c.getString(2));
            reserve.setRe_endDay(c.getString(3));
            reserve.setRe_startTime(c.getString(4));
            reserve.setRe_endTime(c.getString(5));
            reserve.setRe_switch(c.getString(6));
            reserve.setRe_fixtures(c.getString(7));
            reserve.setRe_remarks(c.getString(8));
            reserve.setRe_room_name(c.getString(20));

            list.add("社内" + " : " + c.getString(12)); //*** 参加者の 「社内：名前」のadd ***//
        }
        c.close();
        //*** 社外者の参加者追加 ***//
        c = db.rawQuery("select * from v_reserve_out_member where re_id = ?", new String[]{re_id});
        while (c.moveToNext()) {
            list.add(c.getString(18) + " : " + c.getString(11)); //*** 社外者の 「会社名 ： 名前」のadd ***//
        }
        c.close();

        reserve.setRe_member(list);
        return  reserve;
    }
}
