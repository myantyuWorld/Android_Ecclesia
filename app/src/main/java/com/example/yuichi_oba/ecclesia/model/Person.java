package com.example.yuichi_oba.ecclesia.model;

import java.io.Serializable;

/**
 * Created by Yuichi-Oba on 2017/09/30.
 */

//*** 「個人」クラス ※社員・社外者クラスのスーパークラス ***//
public class Person implements Serializable{

    //*** Field ***//
    private String name;        //*** 氏名 ***//
    private String tel;         //*** 電話番号 ***//
    private String mailaddr;    //*** メールアドレス ***//
    //*** Constrator ***//
    public Person() {
    }

    public Person(String name, String tel, String mailaddr) {
        this.name = name;
        this.tel = tel;
        this.mailaddr = mailaddr;
    }

    //*** GetterSetter ***//
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

    @Override
    public String toString() {
        return String.format("name :%s tel:%s mailaddr:%s", this.name, this.tel, this.mailaddr);
    }
}
