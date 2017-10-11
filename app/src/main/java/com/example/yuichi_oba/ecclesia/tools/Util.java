package com.example.yuichi_oba.ecclesia.tools;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.yuichi_oba.ecclesia.activity.AddMemberActivity;
import com.example.yuichi_oba.ecclesia.activity.ReserveListActivity;

public class Util {

    public static final int COLUMN_INDEX = 1;

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
        Log.d("call", "_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/");
        Log.d("call", "_/");
        Log.d("call", "_/                   " + args);
        Log.d("call", "_/");
        Log.d("call", "_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/");
    }
    /***
     * 部署IDから、部署名をDB検索してリターンするメソッド
     * @param dep_id    部署ID
     * @return 検索した部署名（ヒットなしは 空文字を返す ）
     */
    public static String returnDepartName(String dep_id) {
        Log.d("call", "call returnDepartName()");
        Log.d("call", dep_id);
        SQLiteOpenHelper helper = new DB(ReserveListActivity.getInstance().getApplicationContext());
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
        SQLiteOpenHelper helper = new DB(ReserveListActivity.getInstance().getApplicationContext());
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
     *  役職IDから、役職名をDB検索してリターンするメソッド
     * @param pos_id    役職ID
     * @return 検索した役職名（ヒットなしは 空文字 ヲ返す ）
     */
    public static AddMemberActivity.Position returnPostionName(String pos_id) {
        SQLiteOpenHelper helper = new DB(ReserveListActivity.getInstance().getApplicationContext());
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

    public static AddMemberActivity.Position returnPositionId(String posName) {
        SQLiteOpenHelper helper = new DB(ReserveListActivity.getInstance().getApplicationContext());
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
        SQLiteOpenHelper helper = new DB(ReserveListActivity.getInstance().getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.rawQuery("select * from m_room", null);
        String roomId = "";
        if (c.moveToNext()) {
            roomId = c.getString(0);    //*** 会議室ID ***//
        }
        c.close();

        return roomId;  //*** 会議室ＩＤを返す ***//
    }
    /***
     * 予約テーブルの予約IDの最大値＋１をDB検索して、書式指定して返すメソッド
     * @return
     */
    public static String returnMaxReserveId() {
        SQLiteOpenHelper helper = new DB(ReserveListActivity.getInstance().getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        String maxReserveId = "";
        //*** 予約テーブルの予約IDの最大値＋１をDB検索 ***//
        Cursor c = db.rawQuery("select max(re_id) + 1 from t_reserve", null);
        if (c.moveToNext()) {
            maxReserveId = c.getString(0);          //*** 検索結果の代入（ここでは、未だ０埋めされていない） ***//
            Log.d("call", maxReserveId);
        }
        c.close();

        String newReId = String.format("%04d", Integer.valueOf(maxReserveId));
        Log.d("call", String.format("新しい予約ＩＤ : %s", newReId));     //***  ***//
        return newReId;        //*** 書式指定付きで、０埋めして返す (ex: 0018) ***//
    }
    /***
     * 引数の社員名から、その社員の社員IDをDB検索して値を返すメソッド
     * @param empName
     * @return
     */
    public static String returnEmpId(String empName){
        Cursor c = rawQuery("select emp_id from t_emp where emp_id = ?", new String[]{empName});
        String empId = "";
        if (c.moveToNext()) {
            empId = c.getString(0); //***  ***//
        }
        return empId;               //***  ***//
    }
    /***
     * DB簡単SQL発行メソッド
     * @param args
     * @param strArray
     * @return
     */
    public static Cursor rawQuery(String args, String[] strArray) {
        SQLiteOpenHelper helper = new DB(ReserveListActivity.getInstance().getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        return db.rawQuery(args, strArray);
    }


}
