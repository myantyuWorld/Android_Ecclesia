package com.example.yuichi_oba.ecclesia.model;

/**
 * Created by Yuichi-Oba on 2017/08/27.
 */

public class Employee {
    private String emp_id;
    private String emp_name;
    private String emp_tel;
    private String emp_mailaddr;
    private String dep_id;
    private String dep_name;
    private String pos_id;

    public String getEmp_id() {
        return emp_id;
    }
    public String getEmp_name() {
        return emp_name;
    }
    public String getEmp_tel() {
        return emp_tel;
    }
    public String getEmp_mailaddr() {
        return emp_mailaddr;
    }
    public String getDep_id() {
        return dep_id;
    }
    public String getDep_name() {
        return dep_name;
    }
    public String getPos_id() {
        return pos_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }
    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }
    public void setEmp_tel(String emp_tel) {
        this.emp_tel = emp_tel;
    }
    public void setEmp_mailaddr(String emp_mailaddr) {
        this.emp_mailaddr = emp_mailaddr;
    }
    public void setDep_id(String dep_id) {
        this.dep_id = dep_id;
    }
    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }
    public void setPos_id(String pos_id) {
        this.pos_id = pos_id;
    }

}
