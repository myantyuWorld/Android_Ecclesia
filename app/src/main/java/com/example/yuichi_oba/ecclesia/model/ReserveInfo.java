package com.example.yuichi_oba.ecclesia.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuichi-Oba on 2017/07/24.
 */

public class ReserveInfo implements Serializable {

    /***
     * Field
     */
    private String re_id;
    private String re_overview;
    private String re_purpose;
    private String re_startTime;
    private String re_endTime;
    private String re_rePerson;
    private List<String> re_member = new ArrayList<>();
    private int re_flg;
    private String re_conference_room;
    private String re_marks;

    /***
     * Constractor
     */
    public ReserveInfo() {
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
}
