package com.example.yuichi_oba.ecclesia.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.Employee;
import com.example.yuichi_oba.ecclesia.model.ReserveInfo;
import com.example.yuichi_oba.ecclesia.tools.DB;

import java.util.ArrayList;
import java.util.List;

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/
// _/_/ 予約状況(リストで視覚的にわかりやすい）を表示するアクティビティ
// _/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
public class ReserveListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = ReserveListActivity.class.getSimpleName();
    public static final String RESERVE_INFO = "reserve_info";

    /***
     * 会議予約一覧を表示・選択するための、日付選択用ダイアログ
     */
    public static class MyDialog extends DialogFragment {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar cal = Calendar.getInstance();
            return new DatePickerDialog(
                    getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            txtDate.setText(String.format("%04d/%02d/%02d", year, month + 1, day));
                        }
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            );
        }
    }

    static TextView txtDate;
    Employee employee;
    public static List<ReserveInfo> reserveInfo;    // 予約情報記録クラスの変数

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "ReserveListActivity->onCreate()");
        // 各ウィジェットの初期化処理
        init();

        // Permission error となる・・・なんで？
        // 端末ＩＭＥＩの取得
        String terminalImei = getTerminalImei();
        // 社員情報の設定
        getEmployeeInfo(terminalImei);
        // 予約情報の設定
        getReserveInfo();
        for (ReserveInfo r : reserveInfo) {
            Log.d(TAG, r.getRe_id() + " : " + r.getRe_startTime() + "(" + r.getRe_endTime() + ") room_id : " + r.getRe_roomId());
        }
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // 画面情報の設定
        txtDate = (TextView) findViewById(R.id.txtDate);
//        Calendar c = Calendar.getInstance();
//        txtDate.setText(String.format(Locale.JAPAN, "%04d/%02d/%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE)));
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "txtDate click!");
                MyDialog d = new MyDialog();
                d.show(getFragmentManager(), "dateDialog");
            }
        });

    }


    // ようわからん(笑) ＝＝＞ HCPには書かんでいいよ
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /***
     * ナビを選択したときの処理
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // 選択したナビのIDを取得し、idに代入する
        int id = item.getItemId();

        // Intentクラスの変数を宣言し、nullで初期化
        Intent intent = null;
        // idで処理を分ける
        switch (id) {
            // 「履歴検索」が選択されたとき
            case R.id.nav_rireki:
                // Intentクラスのインスタンス生成し、画面遷移させる
                intent = new Intent(getApplicationContext(), HistorySearchActivity.class);
                startActivity(intent);
                break;
            // 「管理者認証」が選択されたとき
            case R.id.nav_admin_auth:
                // 管理者認証ダイアログを生成する
                AuthDialog authDialog = new AuthDialog();
                authDialog.show(getFragmentManager(), "aaa");
                break;

        }

        // ようわからん(笑) ＝＝＞ HCPには書かんでいいよ
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/

    /***
     * 画面のウィジェットの初期化処理メソッド
     */
    private void init() {
        Log.d(TAG, "init()");
        // 社員クラスのインスタンスを生成
        employee = new Employee();
        // 予約情報クラスのインスタンス生成
        reserveInfo = new ArrayList<>();
    }

    /***
     * アプリを立ち上げた社員の端末ＩＭＥＩを返すメソッド
     * @return 端末ＩＭＥＩ
     */
    public String getTerminalImei() {
        Log.d(TAG, "getTerminalImei()");
        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//        return manager.getDeviceId();
        return "352272080218786";   // 大馬 の 端末ＩＭＥＩ
    }

    /***
     * 端末ＩＭＥＩから社員情報を取得し、設定するメソッド
     * @param terminalImei  端末ＩＭＥＩ
     */
    private void getEmployeeInfo(String terminalImei) {
        Log.d(TAG, "getEmployeeInfo()");
        // 端末ＩＭＥＩから社員ＩＤを取得する
        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from m_terminal where ter_id = ?", new String[]{terminalImei});
        if (c.moveToNext()) {
            // 端末ＩＭＥＩから社員ＩＤ取得が成功した
            employee.setEmp_id(c.getString(1));
        }
        Log.d(TAG, employee.getEmp_id());
        // 社員ＩＤが空またはＮＵＬＬでなければ次のロジックを実行する
        if (!employee.getEmp_id().isEmpty()) {
            c = db.rawQuery("select * from t_emp where emp_id = ?", new String[]{employee.getEmp_id()});
            if (c.moveToNext()) {
                // 社員ＩＤから社員情報を検索して、設定する
                employee.setEmp_name(c.getString(1));
                employee.setEmp_tel(c.getString(2));
                employee.setEmp_mailaddr(c.getString(3));
                employee.setDep_id(c.getString(4));
                employee.setPos_id(c.getString(5));
            } else {
                Log.d(TAG, "社員情報の取得に失敗しました");
            }
        }
        Log.d(TAG, employee.getEmp_id() + " : " + employee.getEmp_name());
    }

    /***
     *
     */
    public void getReserveInfo() {
        Log.d(TAG, "getReserveInfo()");
        // 参加者テーブルから、予約ＩＤを取得
        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from v_reserve_member where emp_id = ?", new String[]{employee.getEmp_id()});
        while (c.moveToNext()) {
            // その社員が参加した会議情報をリストに追加する
            ReserveInfo r = new ReserveInfo(
                    c.getString(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5),
                    c.getString(6),
                    c.getString(11)
            );
            reserveInfo.add(r);
        }
    }




}
