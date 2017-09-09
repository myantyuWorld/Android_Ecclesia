package com.example.yuichi_oba.ecclesia.model;

/**
 * Created by Yuichi-Oba on 2017/09/09.
 */

public class Member {
    private String emp_id;
    private String emp_name;
    private String emp_tel;
    private String emp_mailaddr;
    private String com_name;
    private String dep_name;
    private String po_name;
    private int count;

    public Member() {
    }

    public Member(String emp_id, String emp_name, String emp_tel, String emp_mailaddr, String com_name, String dep_name, String po_name, int count) {
        this.emp_id = emp_id;
        this.emp_name = emp_name;
        this.emp_tel = emp_tel;
        this.emp_mailaddr = emp_mailaddr;
        this.com_name = com_name;
        this.dep_name = dep_name;
        this.po_name = po_name;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getEmp_tel() {
        return emp_tel;
    }

    public String getEmp_mailaddr() {
        return emp_mailaddr;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public String getCom_name() {
        return com_name;
    }

    public String getDep_name() {
        return dep_name;
    }

    public String getPo_name() {
        return po_name;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setEmp_tel(String emp_tel) {
        this.emp_tel = emp_tel;
    }

    public void setEmp_mailaddr(String emp_mailaddr) {
        this.emp_mailaddr = emp_mailaddr;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public void setCom_name(String com_name) {
        this.com_name = com_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    public void setPo_name(String po_name) {
        this.po_name = po_name;
    }
}
