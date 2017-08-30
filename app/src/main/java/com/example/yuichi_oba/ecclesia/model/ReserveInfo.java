package com.example.yuichi_oba.ecclesia.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuichi-Oba on 2017/07/24.
 */

/***
 * 予約情報を管理するクラス
 */
public class ReserveInfo implements Serializable {

    /***
     * Field
     */
    private String re_id;                               // 予約ID
    private String re_overview;                         // 概要
    private String re_purpose;                          // 会議目的名
    private String re_startDay;                        // 開始日時
    private String re_endDay;                          // 終了日時
    private String re_startTime;                          // 終了日時
    private String re_endTime;                          // 終了日時
    private String re_roomId;
    private String re_rePerson;                         // ―→ 現状使っていないため、HCP不要
    private List<String> re_member = new ArrayList<>(); // 会議参加者を記録するリスト
    private int re_flg;                                 // 社内（０）社外（１）
    private String re_conference_room;                  // 会議室名
    private String re_marks;                            // 備考？だったけ(笑)
    // 座標情報を格納する(sX sY eX eY)
    private float[] coop;

    /***
     * Constractor
     */
    public ReserveInfo() {
    }

    public ReserveInfo(String re_id, String re_overview, String re_startDay, String re_endDay, String re_startTime, String re_endTime, String re_flg, String room_id) {
        this.re_id = re_id;
        this.re_overview = re_overview;
        this.re_startDay = re_startDay;
        this.re_endDay = re_endDay;
        this.re_startTime = re_startTime;
        this.re_endTime = re_endTime;
        this.re_flg = Integer.parseInt(re_flg);
        this.re_roomId = room_id;
    }

    /***
     * Getter/Setter
     */
    public String getRe_id() {
        return re_id;
    }
    public String getRe_overview() {
        return re_overview;
    }
    public String getRe_startTime() {
        return re_startTime;
    }
    public String getRe_endTime() {
        return re_endTime;
    }
    public String getRe_rePerson() {
        return re_rePerson;
    }
    public List<String> getRe_member() {
        return re_member;
    }
    public int getRe_flg() {
        return re_flg;
    }
    public String getRe_conference_room() {
        return re_conference_room;
    }
    public String getRe_marks() {
        return re_marks;
    }
    public String getRe_purpose() {
        return re_purpose;
    }
    public String getRe_startDay() {
        return re_startDay;
    }
    public String getRe_endDay() {
        return re_endDay;
    }
    public String getRe_roomId() {
        return re_roomId;
    }
    public float[] getCoop() {
        return coop;
    }

    public void setRe_id(String re_id) {
        this.re_id = re_id;
    }
    public void setRe_overview(String re_overview) {
        this.re_overview = re_overview;
    }
    public void setRe_startTime(String re_startTime) {
        this.re_startTime = re_startTime;
    }
    public void setRe_endTime(String re_endTime) {
        this.re_endTime = re_endTime;
    }
    public void setRe_rePerson(String re_rePerson) {
        this.re_rePerson = re_rePerson;
    }
    public void setRe_member(List<String> re_member) {
        this.re_member = re_member;
    }
    public void setRe_flg(int re_flg) {
        this.re_flg = re_flg;
    }
    public void setRe_conference_room(String re_conference_room) {
        this.re_conference_room = re_conference_room;
    }
    public void setRe_marks(String re_marks) {
        this.re_marks = re_marks;
    }
    public void setRe_purpose(String re_purpose) {
        this.re_purpose = re_purpose;
    }
    public void setRe_startDay(String re_startDay) {
        this.re_startDay = re_startDay;
    }
    public void setRe_endDay(String re_endDay) {
        this.re_endDay = re_endDay;
    }
    public void setRe_roomId(String re_roomId) {
        this.re_roomId = re_roomId;
    }
    public void setCoop(float[] coop) {
        this.coop = coop;
    }
}
