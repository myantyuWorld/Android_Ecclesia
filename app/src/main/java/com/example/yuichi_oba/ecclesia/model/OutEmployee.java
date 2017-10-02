package com.example.yuichi_oba.ecclesia.model;

import java.io.Serializable;

/**
 * Created by Yuichi-Oba on 2017/09/30.
 */

//*** 「社外者」 ***//
public class OutEmployee extends Person implements Serializable {
    //*** Filed ***//
    private String out_id;          //*** 社外者ID ***//
    private String dep_name;        //*** 社外部署名 ***//
    private String pos_name;        //*** 社外役職名 ***//
    private String pos_priority;    //*** 社外役職優先度 ***//

    //*** Constractor ***//
    public OutEmployee() {
    }

    //*** GetterSetter ***//
    public String getOut_id() {
        return out_id;
    }
    public void setOut_id(String out_id) {
        this.out_id = out_id;
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


}
