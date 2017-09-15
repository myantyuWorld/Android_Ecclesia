package com.example.yuichi_oba.ecclesia.model;

/**
 * Created by Yuichi-Oba on 2017/08/27.
 */

public class Employee {

    //*** Field ***//
    private String id;
    private String name;
    private String tel;
    private String mailaddr;
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

    //*** Self Made Method ***//
    //*** 参加会議を抽出するメソッド ***//

}
