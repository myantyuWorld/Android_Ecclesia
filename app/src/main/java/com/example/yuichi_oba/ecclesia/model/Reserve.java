package com.example.yuichi_oba.ecclesia.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.yuichi_oba.ecclesia.activity.ReserveActivity;
import com.example.yuichi_oba.ecclesia.activity.ReserveCheckActivity;
import com.example.yuichi_oba.ecclesia.activity.ReserveConfirmActivity;
import com.example.yuichi_oba.ecclesia.activity.ReserveListActivity;
import com.example.yuichi_oba.ecclesia.tools.MyHelper;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.HH_MM;

/**
 * Created by Yuichi-Oba on 2017/09/15.
 */

public class Reserve implements Serializable{

//    private MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getBaseContext());
    public static SQLiteDatabase db;

    public static final String Q_SAME_DAY_MEETING = "select * from t_reserve where re_startday = ? and room_id = ?";
    //*** Field ***//
    private  long id;                       //*** ID(long型)***//
    private String re_id;                   //*** 予約ID ***//
    private String re_name;                 //*** 概要 ***//
    private String re_startDay;             //*** 開始日時 ***//
    private String re_startTime;            //*** 開始時刻 ***//
    private String re_endDay;               //*** 終了日時 ***//
    private String re_endTime;              //*** 終了時刻 ***//
    private String re_fixtures;             //*** 備品 ***//
    private String re_remarks;              //*** 備考 ***//
    private String re_switch;               //*** 社内/社外区分 0 社内 1 社外***//
    private String re_company;              //*** 参加する会社名 ***//
    private Integer re_mem_priority;        //*** 参加者全員の平均の優先度 ***//
    private String re_purpose_id;           //*** 会議目的ID ***//
    private String re_purpose_name;         //*** 会議目的名 ***//
    private String re_applicant;            //*** 申請者 ***//
    private String re_room_id;              //*** 会議室ID ***//
    private String re_room_name;            //*** 会議室名 ***//

//    private List<Employee> re_member;
    private List<Person> re_member;         //*** 参加者を保持するための、ポリモー使用のリスト ***//
    private float[] coop;                   //*** 予約情報の座標情報を保持するfloat型配列 ***//

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
    public String getRe_applicant() {
        return re_applicant;
    }
    public void setRe_applicant(String re_applicant) {
        this.re_applicant = re_applicant;
    }
    public String getRe_room_id() { return re_room_id; }
    public void setRe_room_id(String re_room_id) { this.re_room_id = re_room_id; }
    public float[] getCoop() {
        return coop;
    }
    public void setCoop(float[] coop) {
        this.coop = coop;
    }
    public String getRe_company() {
        return re_company;
    }
    public void setRe_company(String re_company) {
        this.re_company = re_company;
    }
    public List<Person> getRe_member() {
        return re_member;
    }
    public void setRe_member(List<Person> re_member) {
        this.re_member = re_member;
    }
    public String getRe_room_name() {
        return re_room_name;
    }
    public void setRe_room_name(String re_room_name) {
        this.re_room_name = re_room_name;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    //*** --- SELF MADE METHOD --- 参加者優先度を計算するメソッド ***//
    public int retMemberPriority() {
        return 1;
    }
    //*** --- SELF MADE METHOD --- 会議の時間帯の重複をチェックするメソッド ***//
    public boolean timeDuplicationCheck(Reserve r) {
        //*** 引数の会議日と同じ会議をListで取得する ***//
        List<Reserve> list = getSameDayMeeting(r);

        //*** その日の同じ会議室で会議がない ***//
        if (list.size() == 0)     return true;        //*** 時間の重複なしを返す ***//

        //*** 開始時刻からみて、同じ時間帯に会議があるかチェック ***//
        for (Reserve other : list) {
            //*** 同じ時間帯かチェック ***//
            if (r.getRe_startTime().split(":")[0].contains(other.getRe_startTime().split(":")[0])) {
                return false;                         //*** 暫定これで、重複ありとする ***//
            }
        }
        return true;
    }

    //*** 引数の会議日と同じ会議をListで取得する ***//
    private List<Reserve> getSameDayMeeting(Reserve r) {
        MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getBaseContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(
                Q_SAME_DAY_MEETING,
                new String[]{r.getRe_startDay(), r.getRe_room_id()});

        List<Reserve> list = new ArrayList<>();
        while (c.moveToNext()) {
            //*** 予約のインスタンスを生成 ***//
            Reserve reserve = new Reserve();
            reserve.setRe_startTime(c.getString(4));       //*** 開始時刻 ***//
            reserve.setRe_endTime(c.getString(5));         //*** 終了時刻 ***//
            reserve.setRe_purpose_id(c.getString(13));     //*** 会議目的ID ***//
            reserve.setRe_mem_priority(
                    Integer.valueOf(c.getString(9)));      //*** 会議の優先度 ***//

            list.add(reserve);  //*** インスタンスをリストに追加 ***//
        }
        c.close();
        db.close();

        return list;
    }

    //*** --- SELF MADE METHOD --- 優先度をチェックするメソッド ***//
    public boolean priorityCheck(Reserve r) {
        //*** 引数の会議日と同じ会議をListで取得する ***//
        List<Reserve> list = getSameDayMeeting(r);

        //***  ***//
        // TODO: 2017/10/14  ヌルポ エラー
//        for (Reserve other : list) {
//            if (other.getRe_mem_priority() > r.getRe_mem_priority()) {
//                return false;
//            }
//        }
        return true;
    }
    //*** --- SELF MADE METHOD --- 予約を確定するメソッド ***//
    public int reserveCorrenct(Reserve reserve, float priorityAverage) {

        ContentValues c = new ContentValues();
        c.put("re_id", reserve.getRe_id());                 //***  ***//
        c.put("re_overview", reserve.getRe_name());         //***  ***//
        c.put("re_startday", reserve.getRe_startDay());     //***  ***//
        c.put("re_endday", reserve.getRe_endDay());         //***  ***//
        c.put("re_starttime", reserve.getRe_startTime());   //***  ***//
        c.put("re_endtime", reserve.getRe_endTime());       //***  ***//
        c.put("re_switch", reserve.getRe_switch());         //***  ***//
        c.put("re_fixture", reserve.getRe_fixtures());      //***  ***//
        c.put("re_remarks", reserve.getRe_remarks());       //***  ***//
        c.put("re_priority", priorityAverage);              //***  ***//
        c.put("com_id", "");                                //***  ***//
        c.put("emp_id", reserve.getRe_applicant());         //***  ***//
        c.put("room_id", reserve.getRe_room_id());          //***  ***//
        c.put("pur_id", reserve.getRe_purpose_id());        //***  ***//
        c.put("re_applicant", reserve.getRe_applicant());    //***  ***//

        //***  ***//
        MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getBaseContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        int rs = (int) db.insert("t_reserve", null, c);
        db.close();


        return rs;
    }
    //*** --- SELF MADE METHOD --- 予約をキャンセルするメソッド ***//
    public void reserveCancel(String re_id) {

    }
    //*** --- SELF MADE METHOD --- 早期退出するメソッド ***//
    public void earlyExit() {
//        SQLiteOpenHelper helper = new DB(ReserveConfirmActivity.getInstance().getApplicationContext());
        MyHelper helper = new MyHelper(ReserveCheckActivity.getInstance().getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        //*** 現在時刻取得 ***//
        Date ealDate = new Date();
        //*** フォ－マットを用意 ***//
        SimpleDateFormat ealFor = new SimpleDateFormat(HH_MM);
        //*** 現在時刻をフォーマットにかけてStringへ変換 ***//
        String ealTime = ealFor.format(ealDate);
        Log.d("ealTIme", ealTime);

        db.execSQL("update t_reserve set re_endtime = ? where re_id = ?", new Object[]{ealTime, re_id});
    }
    //*** --- SELF MADE METHOD --- 終了時間を延長するメソッド ***//
    public void endTimeExtention(String exTime) {
        //*** 必要なインスタンス類を用意 ***//
//        SQLiteOpenHelper helper = new DB(ReserveConfirmActivity.getInstance().getApplicationContext());
//        MyHelper helper = new MyHelper(ReserveCheckActivity.getInstance().getApplicationContext());
//        SQLiteDatabase db = helper.getWritableDatabase();
        MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getBaseContext());
        db = helper.getWritableDatabase();
        //*** 延長による終了時刻を計算 ***//
        SimpleDateFormat endFor = new SimpleDateFormat(HH_MM);
        Calendar excal = Calendar.getInstance();
        Log.d("nowEnd", re_endTime);
        //*** フォーマットで変換をかけてCalenderにセット ***//
        try {
            excal.setTime(endFor.parse(re_endTime));
            Log.d("changeTime", String.valueOf(endFor.parse(re_endTime)));
        } catch (ParseException e) {
            e.getStackTrace();
        }
        //*** セットされたCalenderに延長時間を加算する ***//
        excal.add(Calendar.MINUTE, Integer.parseInt(exTime));
        //*** CalenderをDateに変換 ***//
        Date exDate = excal.getTime();
        //*** DateをフォーマットにかけてStringに変換 ***//
        exTime = endFor.format(exDate);
        Log.d("exTIme", exTime);

        db.execSQL("insert into t_extension values(?,?,?,?,?)",
                new Object[]{re_id,
                        re_startDay,
                        re_startTime,
                        re_endDay,
                        re_endTime});
    }
    //*** --- SELF MADE METHOD --- 予約を変更するメソッド ***//
    public void reserveEdit() {
        //*** 必要なインスタンスを用意 ***//
//        SQLiteOpenHelper helper = new DB(ReserveCheckAcetInstance().getApplicationContext());
        MyHelper helper = new MyHelper(ReserveCheckActivity.getInstance().getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("update t_reserve set re_overview = ? , re_startday = ?, re_endday = ?, re_starttime = ?, re_endtime = ?," +
                " re_switch = ?, re_fixture = ?, re_remarks = ?, re_priority = ?, room_id = ?, pur_id = ?" +
                " where re_id = ? ", new Object[]{re_name, re_startDay, re_endDay, re_startTime,
                re_endTime, re_switch, re_fixtures, re_remarks, "会議優先度", re_room_id
                , re_purpose_id, re_id});
    }
    //*** --- SELF MADE METHOD --- 通知メールを送るメソッド ***//
    public void sentMail() {

    }
    //*** --- SELF MADE METHOD --- 通知をするメソッド ***//
    public void notification(String re_id) {

    }
    //*** --- SELF MADE METHOD --- 追い出しを行うメソッド ***//
    public void eviction(String re_id) {
        MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        //*** 追い出し（DBから削除） ***//
        db.rawQuery("delete from t_reserve where re_id = ?", new String[]{re_id});

        //*** 追い出された側に通知 ***//
        //*** 未記述 ***//
    }
    //*** --- SELF MADE METHOD --- 予約内容をDB検索するメソッド ***//
    public static Reserve retReserveConfirm(String re_id) {
        Reserve reserve = new Reserve();

        if (!re_id.contains("0")) { //*** 新規ではない ***//
            Log.d("call", "新規ではない、予約内容のDB検索");
        }
        MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getBaseContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from v_reserve_member x \n" +
                        " inner join m_room y on x.room_id = y.room_id\n" +
                        " inner join t_emp as z on x.re_applicant = z.emp_id where re_id = ?",
                new String[]{re_id});
//        List<String> list = new ArrayList<>();
//        List<Employee> list = new ArrayList<>();
        List<Person> list = new ArrayList<>();

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
            reserve.setRe_purpose_id(c.getString(18));
            reserve.setRe_purpose_name(c.getString(19));
            reserve.setRe_applicant(c.getString(26));
            reserve.setRe_room_name(c.getString(22));

            reserve.setRe_id(re_id);


            //*** [社員]クラスのインスタンスを生成 ***//
            Employee e = new Employee();
            e.setEmp_id(c.getString(11));       // ID
            e.setName(c.getString(12));         // 氏名
            e.setTel(c.getString(13));          // 電話番号
            e.setMailaddr(c.getString(14));     // メールアドレス
            e.setPos_name(c.getString(16));     // 役職名
            e.setPos_priority(c.getString(17)); // 役職の優先度

            list.add(e);    //*** インスタンスを追加 ***//
        }

        c.close();
        //*** 社外者の参加者追加 ***//
        c = db.rawQuery("select * from v_reserve_out_member where re_id = ?", new String[]{re_id});
        while (c.moveToNext()) {
            // 18 company
            reserve.setRe_company(c.getString(18));

            //*** 社員(社外者）クラスのインスタンスを生成 ***//
//            Employee e = new Employee();
//            e.setEmp_id(c.getString(10));           // ID
//            e.setName(c.getString(11));         // 氏名
//            e.setTel(c.getString(12));          // 電話番号
//            e.setMailaddr(c.getString(13));     // メールアドレス
//            e.setDep_name(c.getString(14));     // 部署名
//            e.setPos_name(c.getString(15));     // 役職名
//            e.setPos_priority(c.getString(16)); // 役職の優先度
//            e.setCom_name(c.getString(18));     // 会社名
////            list.add(c.getString(18) + " : " + c.getString(11)); //*** 社外者の 「会社名 ： 名前」のadd ***//

            //*** [社外者]クラスのインスタンスを生成 ***//
            OutEmployee e = new OutEmployee(
                    c.getString(10),            //*** ID ***//
                    c.getString(11),            //*** 氏名 ***//
                    c.getString(12),            //*** 電話番号 ***//
                    c.getString(13),            //*** メールアドレス ***//
                    c.getString(14),            //*** 部署名 ***//
                    c.getString(15),            //*** 役職名 ***//
                    c.getString(16),            //*** 役職優先度 ***//
                    c.getString(18)             //*** 会社名 ***//
            );
            list.add(e);    //*** インスタンスを追加  ***//
        }
        c.close();

        reserve.setRe_member(list); //*** ポリモーフィズム使用のリストをセット ***//
        return  reserve;            //*** 予約情報を返す ***//
    }

    @Override
    public String toString() {
        return String.format("予約ID : %s 予約概要 : %s", this.re_id, this.re_name);
    }
}
