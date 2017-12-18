package com.example.yuichi_oba.ecclesia.tools;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.yuichi_oba.ecclesia.activity.AddMemberActivity;
import com.example.yuichi_oba.ecclesia.activity.ReserveConfirmActivity;
import com.example.yuichi_oba.ecclesia.activity.ReserveListActivity;
import com.example.yuichi_oba.ecclesia.model.Employee;
import com.example.yuichi_oba.ecclesia.model.OutEmployee;
import com.example.yuichi_oba.ecclesia.model.Person;
import com.example.yuichi_oba.ecclesia.model.Reserve;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.*;

public class Util {

  public static final int COLUMN_INDEX = 1;
  public static final String DATE_PATTERN = "yyyy/MM/dd HH:mm";
  public static final String Q_SELECT_M_PURPOSE = "select * from m_purpose where pur_name = ?";

  /***
   * 項目引数に渡すと、その項目のインデックスを返すUtilityメソッド
   * @param spinner   スピナー
   * @param item      インデックスを検索したい文字列
   * @return 引数の文字列がそのスピナーの何番目にあるかを返す
   */
  public static int setSelection(Spinner spinner, String item) {
    SpinnerAdapter adapter = spinner.getAdapter();
    for (int i = 0; i < adapter.getCount(); i++) {
      if (adapter.getItem(i).equals(item)) {
        return i;
      }
    }
    // なければ０を返す
    return 0;
  }

  /***
   * 簡単・みやすいログの出力メソッド
   * @param args
   */
  public static void easyLog(String args) {
    Log.d(CALL, "_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/");
    Log.d(CALL, "_/");
    Log.d(CALL, "_/                   " + args);
    Log.d(CALL, "_/");
    Log.d(CALL, "_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/");
  }

  /***
   * 部署IDから、部署名をDB検索してリターンするメソッド
   * @param dep_id    部署ID
   * @return 検索した部署名（ヒットなしは 空文字を返す ）
   */
  public static String returnDepartName(String dep_id) {
    Log.d(CALL, "call returnDepartName()");
    Log.d(CALL, dep_id);
    SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
    SQLiteDatabase db = helper.getReadableDatabase();

    Cursor c = db.rawQuery("SELECT * FROM m_depart where dep_id = ?",
        new String[]{dep_id});
    String depName = "";
    if (c.moveToNext()) {
      depName = c.getString(COLUMN_INDEX); //*** 「部署名」**//
    }
    c.close();

    return depName; //*** 部署名を返す ***//
  }

  /***
   *
   * @param depName
   * @return
   */
  public static String returnDepartId(String depName) {
    SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
    SQLiteDatabase db = helper.getReadableDatabase();

    Cursor c = db.rawQuery("SELECT * FROM m_depart where dep_name = ?",
        new String[]{depName});
    String depId = "";
    if (c.moveToNext()) {
      depId = c.getString(0); //*** 「部署ID」**//
    }
    c.close();

    return depId;   //*** 部署ＩＤを返す ***//
  }

  /***
   *  役職IDから、役職名をMyHelper検索してリターンするメソッド
   * @param pos_id    役職ID
   * @return 検索した役職名（ヒットなしは 空文字 ヲ返す ）
   */
  public static AddMemberActivity.Position returnPostionName(String pos_id) {
    SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
    SQLiteDatabase db = helper.getReadableDatabase();

    Cursor c = db.rawQuery("SELECT * FROM m_position", null);
    String posName = "";
    AddMemberActivity.Position p = new AddMemberActivity.Position();
    if (c.moveToNext()) {
      posName = c.getString(COLUMN_INDEX);//*** 「役職名」 ***//
      p.posId = c.getString(0);
      p.posName = c.getString(1);
      p.posPriority = c.getString(2);
    }
    c.close();

    return p; //*** 役職情報のインスタンスを返す ***//
  }

  /***
   *
   * @param posName
   * @return
   */
  public static AddMemberActivity.Position returnPositionId(String posName) {
    SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
    SQLiteDatabase db = helper.getReadableDatabase();

    Cursor c = db.rawQuery("SELECT * FROM m_position where pos_name = ?",
        new String[]{posName});
    AddMemberActivity.Position p = new AddMemberActivity.Position();
    if (c.moveToNext()) {
      p.posId = c.getString(0);
      p.posName = c.getString(1);
      p.posPriority = c.getString(2);
    }
    c.close();

    return p;   //*** 役職情報のインスタンスを返す ***//
  }

  /***
   * 会議室名から、会議室ＩＤをＤＢ検索してリターンするメソッド
   * @param roomName  会議室名
   * @return 検索した会議室ＩＤ（ヒットなしは 空文字を返す ）
   */
  public static String returnRoomId(String roomName) {
    SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
    SQLiteDatabase db = helper.getReadableDatabase();

    Cursor c = db.rawQuery("select * from m_room where room_name = ?",
        new String[]{roomName});

    String roomId = "";
    if (c.moveToNext()) {
      roomId = c.getString(0);    //*** 会議室ID ***//
    }
    c.close();

    return roomId;  //*** 会議室ＩＤを返す ***//
  }

  /***
   *
   * @param roomId
   * @return
   */
  public static String returnRoomName(String roomId) {
    SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
    SQLiteDatabase db = helper.getReadableDatabase();

    Cursor c = db.rawQuery("select * from m_room where room_id = ?",
        new String[]{roomId});

    String roomName = "";
    if (c.moveToNext()) {
      roomName = c.getString(1);    //*** 会議室ID ***//
    }
    c.close();

    return roomName;//*** 会議室名を返す ***//
  }

  /***
   * 予約テーブルの予約IDの最大値＋１をMyHelper検索して、書式指定して返すメソッド
   * @return
   */
  public static String returnMaxReserveId() {
    Log.d(CALL, "call Util.returnMaxReserveId()");
    SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
    SQLiteDatabase db = helper.getReadableDatabase();

    String maxReserveId = "";
    //*** 予約テーブルの予約IDの最大値＋１をMyHelper検索 ***//
    Cursor c = db.rawQuery("select max(re_id) + 1 from t_reserve", null);
    if (c.moveToNext()) {
      maxReserveId = c.getString(0);          //*** 検索結果の代入（ここでは、未だ０埋めされていない） ***//
      Log.d(CALL, maxReserveId);
    }
    c.close();

    Log.d(CALL, String.format("新しい、予約ID : %04d", Integer.valueOf(maxReserveId)));
    return String.format("%04d", Integer.valueOf(maxReserveId));        //*** 書式指定付きで、０埋めして返す (ex: 0018) ***//
  }

  /***
   * 引数の社員名から、その社員の社員IDをMyHelper検索して値を返すメソッド
   * @param empName
   * @return
   */
  public static String returnEmpId(String empName) {
    Cursor c = rawQuery("select emp_id from t_emp where emp_id = ?", new String[]{empName});
    String empId = "";
    if (c.moveToNext()) {
      empId = c.getString(0); //***  ***//
    }
    return empId;               //***  ***//
  }

  /***
   * 引数の社員名から、その社員の社員IDをMyHelper検索して値を返すメソッド
   * @param empName
   * @return
   */
  public static String returnOutEmpId(String empName) {
    Cursor c = rawQuery("select out_id from m_out where out_name = ?", new String[]{empName});
    String outId = "";
    if (c.moveToNext()) {
      outId = c.getString(0);
    }
    //*** 社外者 ***//
    return outId;
  }

  /***
   * MyHelper簡単SQL発行メソッド
   * @param args
   * @param strArray
   * @return
   */
  public static Cursor rawQuery(String args, String[] strArray) {
    SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
    SQLiteDatabase db = helper.getReadableDatabase();

    return db.rawQuery(args, strArray);
  }

  /***
   * 引数の文字列を、Calenderクラスのインスタンス化したものを返すメソッド
   * 引数の渡し方 yyyy/MM/dd HH:mm となるように、args に String.formatで整形して渡す
   * @param args
   * @return
   * @throws ParseException
   */
  public static Calendar convertCalenderString(String args) throws ParseException {
    Date date = new Date((new SimpleDateFormat(DATE_PATTERN).parse(args).getTime()));
    Calendar c = Calendar.getInstance();
    c.setTime(date);

    return c;   //***  ***//
  }

  //    public static long insertReserve(Reserve reserve, float priorityAverage) {
  //
  //        ContentValues c = new ContentValues();
  //        c.put("re_id", reserve.getRe_id());                 //***  ***//
  //        c.put("re_overview", reserve.getRe_name());         //***  ***//
  //        c.put("re_startday", reserve.getRe_startDay());     //***  ***//
  //        c.put("re_endday", reserve.getRe_endDay());         //***  ***//
  //        c.put("re_starttime", reserve.getRe_startTime());   //***  ***//
  //        c.put("re_endtime", reserve.getRe_endTime());       //***  ***//
  //        c.put("re_switch", reserve.getRe_switch());         //***  ***//
  //        c.put("re_fixture", reserve.getRe_fixtures());      //***  ***//
  //        c.put("re_remarks", reserve.getRe_remarks());       //***  ***//
  //        c.put("re_priority", priorityAverage);              //***  ***//
  //        c.put("com_id", "");                                //***  ***//
  //        c.put("emp_id", reserve.getRe_applicant());         //***  ***//
  //        c.put("room_id", reserve.getRe_room_id());          //***  ***//
  //        c.put("pur_id", reserve.getRe_purpose_id());        //***  ***//
  //        c.put("re_applicant", reserve.getRe_applicant());    //***  ***//
  //
  //        //***  ***//
  //        SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
  //
  //        return helper.getWritableDatabase().insertOrThrow("t_reserve", null, c);  //***  ***//
  //    }
  //*** 会議目的IDを返す ***//
  public static String returnPurposeId(String purName) {
    MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getBaseContext());
    SQLiteDatabase db = helper.getReadableDatabase();
    Cursor c = db.rawQuery(Q_SELECT_M_PURPOSE, new String[]{purName});
    if (c.moveToNext()) {
      //*** 会議目的IDを返す ***//
      return c.getString(0);
    }
    c.close();

    return "";
  }

  //*** 引数の会議目的IDから、会議目的優先度を返すメソッド ***//
  public static int returnPurposePriority(String purId){
    MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
    SQLiteDatabase db = helper.getReadableDatabase();
    Cursor c = db.rawQuery(Q_SELECT_M_PURPOSE, new String[]{purId});
    if (c.moveToNext()) {
      //*** 会議目的優先度を返す ***//
      return Integer.valueOf(c.getString(2));
    }
    c.close();
    return 0;
  }


  /**
   * 指定した日時のカレンダーを取得します。
   *
   * @param y   :年
   * @param m   :月
   * @param d   :日
   * @param h   :時間
   * @param min :分
   */
  public static Calendar getCalender(int y, int m, int d, int h, int min) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, y);
    calendar.set(Calendar.MONTH, m);
    calendar.set(Calendar.DAY_OF_MONTH, d);
    calendar.set(Calendar.HOUR, h);
    calendar.set(Calendar.MINUTE, min);

    return calendar;

  }

  //*** 管理者認証済みか否かを返す ***//
  public static boolean isAuthAdmin(String authFlg) {
    if (authFlg.contains("1")) {
      return true;
    }
    return false;
  }

  public static List<Person> retHistoryPesonsList(String emp_id) {
    MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getBaseContext());
    SQLiteDatabase db = helper.getReadableDatabase();
    String sqlArgs = "select *, count(*) as cnt from v_reserve_member x " +
        "inner join m_depart y on x.dep_name = y.dep_name " +
        " where re_id in (select re_id from t_member where mem_id = ?) group by mem_id order by cnt desc limit 10 ";
    Cursor c = db.rawQuery(sqlArgs, new String[]{emp_id});
    List<Person> persons = new ArrayList<>();
    while (c.moveToNext()) {
      //*** [社員]クラスのインスタンスを生成 ***//
      Employee e = new Employee();
      e.setEmp_id(c.getString(11));           // ID
      e.setName(c.getString(12));         // 氏名
      e.setTel(c.getString(13));          // 電話番号
      e.setMailaddr(c.getString(14));     // メールアドレス
      e.setDep_id(c.getString(22));       //*** 部署ＩＤ ***//
      e.setPos_name(c.getString(16));     // 役職名
      e.setPos_priority(c.getString(17)); // 役職の優先度

      Log.d("call", String.format("社員情報 : %s", e.toString()));    //***  ***//

      persons.add(e);
    }
    c.close();


    c = db.rawQuery("select * from v_reserve_out_member where re_id in (select re_id from t_member where mem_id = ?)", new String[]{emp_id});
    while (c.moveToNext()) {
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
      persons.add(e);
    }
    return persons;
  }

  public static String returnReserveApplicant(String reId){
    MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getBaseContext());
    SQLiteDatabase db = helper.getReadableDatabase();
    Cursor c = db.rawQuery("select emp_id from t_reserve where re_id = ?", new String[]{reId});
    if (c.moveToNext()) {
      return c.getString(0);
    }
    return "NG";
  }

  /**
   * キャプチャを撮る
   * @return 撮ったキャプチャ(Bitmap)
   */
  public static Bitmap getViewCapture(View view) {
    view.setDrawingCacheEnabled(true);

    // Viewのキャッシュを取得
    Bitmap cache = view.getDrawingCache();
    if(cache == null){
      return null;
    }
    Bitmap screenShot = Bitmap.createBitmap(cache);
    view.setDrawingCacheEnabled(false);
    return screenShot;
  }

  /**
   * 撮ったキャプチャを保存
   * @param view
   */
  public static void saveCapture(View view, File file) {
    // キャプチャを撮る
    Bitmap capture = getViewCapture(view);
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(file, false);
      // 画像のフォーマットと画質と出力先を指定して保存
      assert capture != null;
      capture.compress(Bitmap.CompressFormat.JPEG, 100, fos);
      fos.flush();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (fos != null) {
        try {
          fos.close();
        } catch (IOException ie) {
          fos = null;
        }
      }
    }
  }

  public static Reserve getReserveInfo(String reId){
    MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getBaseContext());
    SQLiteDatabase db = helper.getWritableDatabase();
    Cursor c = db.rawQuery("select * from v_reserve_member where re_id = ?", new String[]{reId});
    Reserve r = new Reserve();
    // TODO: 2017/12/11 ここ、不十分にとっているので通知の画面遷移でエラーでますよ
    if (c.moveToNext()) {
      //*** 追い出し対象の予約インスタンスを生成 ***//
      r.setRe_id(reId);         //*** 予約ID ***//
      r.setRe_name(c.getString(1));           //*** 概要 ***//
      r.setRe_startDay(c.getString(2));       //*** 開始日時 ***//
      r.setRe_endDay(c.getString(3));         //*** 終了日時 ***//
      r.setRe_startTime(c.getString(4));      //*** 開始時刻 ***//
      r.setRe_endTime(c.getString(5));        //*** 終了時刻 ***/
      r.setRe_switch(c.getString(6));         //*** 社内社外区分 ***//
      r.setRe_fixtures(c.getString(7));       //*** 備品 ***//
      r.setRe_remarks(c.getString(8));        //*** 備考 ***//
      r.setRe_room_id(c.getString(10));       //*** 会議室ID ***//
      r.setRe_room_name(Util.returnRoomName(r.getRe_room_id()));
      r.setRe_purpose_name(c.getString(19));  //*** 会議目的名 ***//
        r.setRe_applicant(c.getString(21));
    }
    c.close();

    return r;
  }

    //*** --- SELF MADE METHOD --- 既に延長がされている会議かを判定するメソッド ***//
    public static String alreadyExtensionCheck(String re_id) {
        String result = FALSE;
        MyHelper helper = new MyHelper(ReserveConfirmActivity.getInstance().getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_ALREADY_EXTENSION_CHECK, new String[]{re_id});
        if (cursor.moveToNext()) {
            //*** 既に延長がされている場合、trueを返す ***//
            result = cursor.getString(ZERO) + SPACE + cursor.getString(ONE);
        }
        return result;
    }



}
