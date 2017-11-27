package com.example.yuichi_oba.ecclesia.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.yuichi_oba.ecclesia.activity.ReserveCheckActivity;
import com.example.yuichi_oba.ecclesia.activity.ReserveListActivity;
import com.example.yuichi_oba.ecclesia.tools.MyHelper;
import com.example.yuichi_oba.ecclesia.tools.Util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.CALL;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.FALSE;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.HH_MM;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.TRUE;

;

/**
 * Created by Yuichi-Oba on 2017/09/15.
 */

public class Reserve implements Serializable {

  private static final String Q_SAME_DAY_MEETING = "select * from t_reserve where re_startday = ? and room_id = ?";
  private static final String Q_SELECT_TEST = "select * from v_reserve_member x inner join m_room y on x.room_id = y.room_id inner join t_emp as z on x.re_applicant = z.emp_id where re_id = ?";
  private static final int YEAR = 0;
  private static final int MONTH = 1;
  private static final int DATE = 2;
  private static final int HOUR = 3;
  private static final int MINUTE = 4;
  private static final String SYANAI = "0";
  private static final String SYAGAI = "1";
  //    private MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getBaseContext());
  public static SQLiteDatabase db;
  //*** Field ***//
  private long id;                        //*** ID(long型)***//
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

  public String getRe_room_id() {
    return re_room_id;
  }

  public void setRe_room_id(String re_room_id) {
    this.re_room_id = re_room_id;
  }

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

  @Override
  public String toString() {
    return String.format("予約ID : %s 予約概要 : %s, 会議優先度 : %d", this.re_id, this.re_name, this.getRe_mem_priority());
  }

  //*** ------------------------ ***//
  //*** --- SELF MADE METHOD --- ***//
  //*** ------------------------ ***//
  //*** --- SELF MADE METHOD --- 参加者優先度を計算するメソッド ***//
  public int retMemberPriority() {
    return 1;
  }

  //*** --- SELF MADE METHOD --- 会議の時間帯の重複をチェックするメソッド ***//
  public String timeDuplicationCheck(Reserve r) {
    Util.easyLog("Reserve SELF MADE METHOD");
    Log.d(CALL, "call Reserve.timeDuplicateCheck()");
    //*** 引数の会議日と同じ会議をListで取得する ***//
    List<Reserve> list = getSameDayMeeting(r);

    //*** その日の同じ会議室で会議がない ***//
    if (list.size() == 0) {
      Log.d(CALL, "会議の重複ナッシング");
      return TRUE;        //*** 時間の重複なしを返す ***//
    }
    //***  ***/

    //*** 開始時刻からみて、同じ時間帯に会議があるかチェック ***//
    for (Reserve other : list) {      //*** 同じ日付の会議リストをループ ***//
      //*** 同じ時間帯かチェック ***//
      // (year, month, date, hour, minute) monthの範囲は0-11で1月は0
      int[] starts = argsStartIntValue(other);    //*** 同じ日付の会議の開始時刻 ***//
      int[] ends = argsEndIntValue(other);        //*** 同じ日付の会議の終了時刻 ***//

      Calendar startTime = new GregorianCalendar(starts[YEAR], starts[MONTH], starts[DATE], starts[HOUR], starts[MINUTE]);
      Calendar endTime = new GregorianCalendar(ends[YEAR], ends[MONTH], ends[DATE], ends[HOUR], ends[MINUTE]);
      Log.d(CALL, "startTime:" + startTime.get(Calendar.YEAR) + "/" + startTime.get(Calendar.MONTH) + "/" + startTime.get(Calendar.DAY_OF_MONTH));
      Log.d(CALL, "endTime:" + endTime.get(Calendar.YEAR) + "/" + endTime.get(Calendar.MONTH) + "/" + endTime.get(Calendar.DAY_OF_MONTH));

      if (!r.getRe_id().equals(other.getRe_id()) && !isPeriod(r, startTime, endTime)) {
        Log.d(CALL, "時間の重複が発生：（暫定）処理を終了します");
        Log.d(CALL, "本来はここで、優先度チェックを行い、追い出し処理を行う");
        //*** 本来はここで、優先度チェックを行い、追い出し処理を行う priorityCheck()***//
        if (priorityCheck(r, other)) {
          //*** 優先度で「勝ち」==> 追い出し処理を行う eviction()***//
//          r.eviction(other.getRe_id());
          Log.d(CALL, other.getRe_id());
          return other.getRe_id();  //*** 追い出し対象の、予約IDを返す ***//
        }
        //*** 時間の重複あり & 優先度のチェックでも負け***//
        return FALSE;         //*** 予約ができない！を返す ***//
      }
    }
    return TRUE;
  }

  //*** 引数の指定現在時刻が指定時間帯の範囲内かチェックするメソッド ***//
  //*** ここがうまく動いていないのか？ ***//
  private boolean isPeriod(Reserve r, Calendar startTime, Calendar endTime) {
    Log.d(CALL, "call Reserve.isPeriod()");
    //*** 取ろうとしている予約の開始情報 ***//
    Calendar calStart = Calendar.getInstance();
    calStart.set(
        Integer.parseInt(r.getRe_startDay().split("/")[0]),   //*** year ***//
        Integer.parseInt(r.getRe_startDay().split("/")[1]),   //*** month ***//
        Integer.parseInt(r.getRe_startDay().split("/")[2]),   //*** date ***//
        Integer.parseInt(r.getRe_startTime().split("：")[0]), //*** hour ***//
        Integer.parseInt(r.getRe_startTime().split("：")[1])  //*** minute ***//
    );
    //*** 取ろうとしている予約の終了情報 ***//
    Calendar calEnd = Calendar.getInstance();
    calEnd.set(
        Integer.parseInt(r.getRe_endDay().split("/")[0]),   //*** year ***//
        Integer.parseInt(r.getRe_endDay().split("/")[1]),   //*** month ***//
        Integer.parseInt(r.getRe_endDay().split("/")[2]),   //*** date ***//
        Integer.parseInt(r.getRe_endTime().split("：")[0]), //*** hour ***//
        Integer.parseInt(r.getRe_endTime().split("：")[1])  //*** minute ***//
    );
    Log.d(CALL, String.format("今からとる予約の、開始: %s", String.valueOf(calStart.getTime())));
    Log.d(CALL, String.format("今からとる予約の、終了: %s", String.valueOf(calEnd.getTime())));
    Log.d(CALL, String.format("過去の開始 %s", String.valueOf(startTime.getTime())));
    Log.d(CALL, String.format("過去の終了 %s", String.valueOf(endTime.getTime())));


    if (calStart.after(startTime) && calStart.before(endTime) && calEnd.after(startTime) && calEnd.before(endTime)) {
      //*** S ---- NOWSTART || NOWEND ---- E のパターン***//
      Log.d(CALL, "//*** S ---- NOWSTART || NOWEND ---- E のパターン***// で重複");
      return false; //*** 重複あり ***//
    }
    if (calStart.before(startTime) && calEnd.after(endTime)) {
      //*** NOWSTART ---- S ---- E ---- NOWEND のパターン***//
      Log.d(CALL, "//*** NOWSTART ---- S ---- E ---- NOWEND のパターン***// で重複");
      return false; //*** 重複あり ***//
    }
    if ((calStart.after(startTime) && calStart.before(endTime)) || (calEnd.after(startTime) && calEnd.before(endTime))) {
      Log.d(CALL, "start endの一方が重なっていたパターン");
      return false; //*** 重複あり ***//
    }
    return true;  //*** チェックに何も引っかからなかったら、True（重複なし）を返す ***//
  }

  //***  ***//
  private int[] argsStartIntValue(Reserve o) {
    int[] ints = new int[5];
    Log.d(CALL, String.format("他の、開始 %s %s", o.getRe_startDay(), o.getRe_startTime()));

    ints[0] = Integer.parseInt(o.getRe_startDay().split("/")[0]);
    ints[1] = Integer.parseInt(o.getRe_startDay().split("/")[1]);
    ints[2] = Integer.parseInt(o.getRe_startDay().split("/")[2]);
    ints[3] = Integer.parseInt(o.getRe_startTime().split("：")[0]);
    ints[4] = Integer.parseInt(o.getRe_startTime().split("：")[1]);

    return ints;
  }

  //***  ***//
  private int[] argsEndIntValue(Reserve o) {
    int[] ints = new int[5];
    Log.d(CALL, String.format("他の、終了 %s %s", o.getRe_endDay(), o.getRe_endTime()));

    ints[0] = Integer.parseInt(o.getRe_endDay().split("/")[0]);
    ints[1] = Integer.parseInt(o.getRe_endDay().split("/")[1]);
    ints[2] = Integer.parseInt(o.getRe_endDay().split("/")[2]);
    ints[3] = Integer.parseInt(o.getRe_endTime().split("：")[0]);
    ints[4] = Integer.parseInt(o.getRe_endTime().split("：")[1]);

    return ints;
  }

  //*** 引数の会議日と同じ会議をListで取得する ***//
  private List<Reserve> getSameDayMeeting(Reserve r) {
    Log.d("getSameDayMeeting", "getSameDayMeeting突入");
//    MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getBaseContext());
    MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
    SQLiteDatabase db = helper.getReadableDatabase();
    Cursor c = db.rawQuery(
        Q_SAME_DAY_MEETING,
        new String[]{r.getRe_startDay(), r.getRe_room_id()});

    List<Reserve> list = new ArrayList<>();
    while (c.moveToNext()) {
      //*** 予約のインスタンスを生成 ***//
      Reserve reserve = new Reserve();
      reserve.setRe_id(c.getString(0));
      reserve.setRe_startDay(c.getString(2));        //*** 開始日 ***//
      reserve.setRe_endDay(c.getString(3));          //*** 終了日 ***//
      reserve.setRe_startTime(c.getString(4));       //*** 開始時刻 ***//
      reserve.setRe_endTime(c.getString(5));         //*** 終了時刻 ***//
      reserve.setRe_purpose_id(c.getString(13));     //*** 会議目的ID ***//
      reserve.setRe_mem_priority(
          Integer.valueOf(c.getString(9)));      //*** 会議の優先度 ***//
      Log.d("getSameDayMeeting", "取得した予約" + reserve.getRe_startDay());
      list.add(reserve);  //*** インスタンスをリストに追加 ***//
    }
    c.close();
    db.close();

    return list;
  }

  //*** --- SELF MADE METHOD --- 優先度をチェックするメソッド  ***//
//*** true : 勝ち false : 負け                            ***//
  // TODO: 2017/11/13 会議目的優先度をみて、おなじなら、メンバーの優先度見るロジックのじっそう
  private boolean priorityCheck(Reserve r, Reserve o) {
    Log.d(CALL, "call Reserve->priorityCheck()");
    Log.d(CALL, "自分の予約ID：" + r.getRe_id());
    Log.d(CALL, "相手の予約ID：" + o.getRe_id());

    //*** 自分が「社内利用」で 他が「社外利用」 ***//
    if (r.getRe_switch().contains(SYANAI) && o.getRe_switch().contains(SYAGAI)) {
      return false;
    }
    //*** 会議目的で、勝敗を判定する ***//
    int myPurPriority = Util.returnPurposePriority(r.getRe_purpose_id());
    int otherPurPriority = Util.returnPurposePriority(o.getRe_purpose_id());

    if (myPurPriority < otherPurPriority) {
      return false;
    } else if (myPurPriority > otherPurPriority) {
      return true;
    } else {
      //*** 会議目的優先度が同じ ==> 優先度値を比較する 自分 ＜ 他 ***//
      Log.d(CALL, String.format("myPriority : %s", r.getRe_mem_priority()));  // DO: 2017/11/07 こっちNULLで来ることがある
      Log.d(CALL, String.format("otherPriority : %s", o.getRe_mem_priority()));

      // DO: 2017/11/07 初期データの会議に優先度つけていない？
      if (r.getRe_mem_priority() < o.getRe_mem_priority()) {
        return false;
      }
    }
    //*** 会議目的優先度が同じ ***//
    return true;
  }

  //*** --- SELF MADE METHOD --- 予約を確定するメソッド ***//
  public int reserveCorrenct(float priorityAverage) {
    Log.d(CALL, "call Reserve.reserveCorrect()");
    //*** 申請者の氏名－＞ 社員IDに変換して、予約インスタンスにセットする ***//
    this.setRe_applicant(Util.returnEmpId(this.getRe_applicant()));
    MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());

    //*** 予約IDの最大値＋１を取得する ***//
    String maxReId = Util.returnMaxReserveId();
    db = helper.getWritableDatabase();

    //*** 予約テーブルへのインサート ***//
    Log.d(CALL, "予約テーブルへのインサート開始");
    db.beginTransaction();
    try {
      try (SQLiteStatement st = db.compileStatement("insert into t_reserve values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
        st.bindString(1, maxReId);
        st.bindString(2, this.getRe_name());
        st.bindString(3, this.getRe_startDay());
        st.bindString(4, this.getRe_endDay());
        st.bindString(5, this.getRe_startTime());
        st.bindString(6, this.getRe_endTime());
        st.bindString(7, this.getRe_switch());
        st.bindString(8, this.getRe_fixtures());
        st.bindString(9, this.getRe_remarks());
        st.bindString(10, String.valueOf(priorityAverage));
        st.bindString(11, "company_name");
        st.bindString(12, this.getRe_applicant());
        st.bindString(13, this.getRe_room_id());
        st.bindString(14, this.getRe_purpose_id());
        st.bindString(15, this.getRe_applicant());
        Log.d(CALL, st.toString());
        st.executeInsert();
        // TODO: 2017/11/25 予約のインサーと情報をadbで確認すること
      }
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }
    Log.d(CALL, "予約テーブルへのインサート終了");

    //*** 参加者テーブルへのインサート ***//
    Log.d(CALL, "参加者テーブルへのインサート開始");
    db = helper.getWritableDatabase();
    db.beginTransaction();

    // TODO: 2017/11/13 ここで、本当にt_memberインサートしている？？
    SQLiteStatement st = db.compileStatement("insert into t_member values (?, ?)");
    for (Person m : this.getRe_member()) {
      st.bindString(1, maxReId);
      String mem_id = null;
      //***  ***//
      if (m instanceof Employee) {
        mem_id = ((Employee) m).getEmp_id();
        Log.d(CALL, String.format("社内mem_id : %s", mem_id));
      }
      //***  ***//
      else if (m instanceof OutEmployee) {
        mem_id = ((OutEmployee) m).getOut_id();
        Log.d(CALL, String.format("社外mem_id : %s", mem_id));
      }
      st.bindString(2, mem_id);
      st.executeInsert();
    }
    db.setTransactionSuccessful();
    db.endTransaction();
    Log.d(CALL, "参加者テーブルへのインサート終了");


    return 1;
  }

  //*** --- SELF MADE METHOD --- 予約をキャンセルするメソッド ***//
  public void reserveCancel(String re_id) {

  }

  //*** --- SELF MADE METHOD --- 早期退出するメソッド ***//
  public void earlyExit() {
    //*** DBインスタンス用意 ***//
    MyHelper helper = new MyHelper(ReserveCheckActivity.getInstance().getApplicationContext());
    SQLiteDatabase db = helper.getWritableDatabase();
    //*** 現在時刻取得 ***//
    Date ealDate = new Date();
    //*** フォ－マットを用意 ***//
    SimpleDateFormat ealFor = new SimpleDateFormat(HH_MM);
    //*** 現在時刻をフォーマットにかけてStringへ変換 ***//
    String ealTime = ealFor.format(ealDate);
    Log.d(CALL, "早期退出した時刻：" + ealTime);

    db.execSQL("update t_reserve set re_endtime = ? where re_id = ?", new Object[]{ealTime, re_id});
    //*** 各種クローズ ***//
    db.close();
    helper.close();
  }

  //*** --- SELF MADE METHOD --- 終了時間を延長するメソッド ***//
  public void endTimeExtention() {
    //*** 必要なインスタンス類を用意 ***//
    MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getBaseContext());
    db = helper.getWritableDatabase();

    db.execSQL("insert into t_extension values(?,?,?,?,?)",
        new Object[]{re_id,
            re_startDay,
            re_endDay,
            re_startTime,
            re_endTime});

    db.close();
    helper.close();
  }

  //*** --- SELF MADE METHOD --- 予約を変更するメソッド ***//
  public void reserveEdit(float priorityAverage) {
    //*** 必要なインスタンスを用意 ***//
//        SQLiteOpenHelper helper = new DB(ReserveCheckAcetInstance().getApplicationContext());
    MyHelper helper = new MyHelper(ReserveCheckActivity.getInstance().getApplicationContext());
    SQLiteDatabase db = helper.getWritableDatabase();
    //*** SQLでアップデートかける ***//
    db.execSQL("update t_reserve set re_overview = ? , re_startday = ?, re_endday = ?, re_starttime = ?, re_endtime = ?," +
        " re_switch = ?, re_fixture = ?, re_remarks = ?, re_priority = ?, room_id = ?, pur_id = ?" +
        " where re_id = ? ", new Object[]{re_name, re_startDay, re_endDay, re_startTime,
        re_endTime, re_switch, re_fixtures, re_remarks, priorityAverage, re_room_id
        , re_purpose_id, re_id});

    re_member.forEach(person -> {
      if (person instanceof Employee) {
        db.execSQL("replace into t_member values(?, ?) ", new Object[]{re_id, Util.returnEmpId(person.getName())});
      } else {
        db.execSQL("replace into t_member values(?, ?)", new Object[]{re_id, Util.returnOutEmpId(person.getName())});
      }
    });

    db.close();
    helper.close();
  }

  //*** --- SELF MADE METHOD --- 通知メールを送るメソッド ***//
  public void sentMail() {

  }

  //*** --- SELF MADE METHOD --- 追い出しを行うメソッド 引数：追い出し対象の予約ID***//
  public void eviction(String otherReId) {
    Log.d(CALL, "call Reserve.eviction()");
    Log.d(CALL, String.format("%s の 会議を削除します", otherReId));
    MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
    SQLiteDatabase db = helper.getWritableDatabase();

    //*** 追い出し（memberTableから削除） ***//
    Cursor cursor = db.rawQuery("delete from t_member where re_id = ?", new String[]{otherReId});
    cursor.moveToFirst();
    //*** 追い出し（ReserveTableから削除） ***//
    cursor = db.rawQuery("delete from t_reserve where re_id = ?", new String[]{otherReId});
    cursor.moveToFirst();

    cursor.close();

  }

  public static Reserve retReserveConfirm(String re_id) {
    Log.d(CALL, "Reserve->retReserveConfirm()");
    Log.d(CALL, String.format("%s の 予約情報を取得します", re_id));

    MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getBaseContext());
    SQLiteDatabase db = helper.getReadableDatabase();
    // TODO: 2017/11/13 v_reserve_member に新規登録した会議がない？
    // TODO: 2017/11/25 新規に予約したのち、一覧から選択すると、nullでおちる、adbに入って、チェックすること
    Cursor c = db.rawQuery(Q_SELECT_TEST,
        new String[]{re_id});
    //        List<String> list = new ArrayList<>();
//        List<Employee> list = new ArrayList<>();
    List<Person> list = new ArrayList<>();

    Reserve reserve = new Reserve();
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
      reserve.setRe_mem_priority(Integer.valueOf(c.getString(9)));
      reserve.setRe_purpose_id(c.getString(18));
      reserve.setRe_purpose_name(c.getString(19));
      reserve.setRe_applicant(c.getString(26));
      reserve.setRe_room_id(c.getString(22));
      reserve.setRe_room_name(c.getString(23));

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
    return reserve;            //*** 予約情報を返す ***//
  }


}
