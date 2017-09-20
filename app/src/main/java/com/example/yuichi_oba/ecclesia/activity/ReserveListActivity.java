package com.example.yuichi_oba.ecclesia.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.Employee;
import com.example.yuichi_oba.ecclesia.tools.DB;
import com.example.yuichi_oba.ecclesia.view.TimeTableView;

import java.util.Calendar;
import java.util.Locale;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.NONE;

//import static com.example.yuichi_oba.ecclesia.activity.MyDialog.employee;

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/
// _/_/ 予約状況(リストで視覚的にわかりやすい）を表示するアクティビティ
// _/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// TODO: 2017/09/19  長押し対応は無理か？ 一覧での、タップは反応するが、長押しには反応しない・・・
public class ReserveListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public static final int EMP_NAME = 1;
    public static final int EMP_TEL = 2;
    public static final int EMP_MAIL_ADDR = 4;
    public static final int DEP_NAME = 5;
    public static final int POS_NAME = 7;
    public static final int POS_PRIORITY = 8;

    //*** 端末IMEIクラス ***//
    public class Imei {

        //*** Field ***//
        private String id;
        private String imeiNumber;

        //*** GetterSetter ***//
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getImeiNumber() {
            return imeiNumber;
        }
        public void setImeiNumber(String imeiNumber) {
            this.imeiNumber = imeiNumber;
        }

        //*** SelfMadeMethod ***//
        //*** 端末IMEIを取得するメソッド ***//
        public void getTerminalImei() {
            Log.d(TAG, "getTerminalImei()");
            TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//        return manager.getDeviceId();
            // TODO: 2017/09/15 ハードコーディング!
            this.setImeiNumber("352272080218786");   // 大馬 の 端末ＩＭＥＩ
        }

        //*** 社員を認証するメソッド ***//
        public String authEmployee() {
            Log.d("call", "ReserveListActivity->authEmployee()");
            String emp_id = "";
            SQLiteOpenHelper helper = new DB(getApplicationContext());
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor c = db.rawQuery("select * from m_terminal where ter_id = ?", new String[]{this.imeiNumber});
            if (c.moveToNext()) {
                // 端末ＩＭＥＩから社員ＩＤ取得が成功した
                emp_id = c.getString(EMP_NAME);
            }
            c.close();
            return emp_id;
        }

        //*** 認証済み社員を生成するメソッド ***//
        public Employee getEmployeeInfo() {
            Log.d("call", TAG + "->getEmployeeInfo()");
            String emp_id = authEmployee();
            if (!emp_id.isEmpty()) {
                SQLiteOpenHelper helper2 = new DB(getApplicationContext());
                SQLiteDatabase db2 = helper2.getReadableDatabase();
                // 社員ＩＤが空またはＮＵＬＬでなければ次のロジックを実行する
                Cursor c = db2.rawQuery("select * from v_employee where emp_id = ?",
                        new String[]{emp_id});
                if (c.moveToNext()) {
                    // 社員ＩＤから社員情報を検索して、設定する
                    Employee e = new Employee();
                    e.setId(emp_id);
                    e.setName(c.getString(EMP_NAME));
                    e.setTel(c.getString(EMP_TEL));
                    e.setMailaddr(c.getString(EMP_MAIL_ADDR));
                    e.setDep_name(c.getString(DEP_NAME));
                    e.setPos_name(c.getString(POS_NAME));
                    e.setPos_priority(c.getString(POS_PRIORITY));

                    return e;
                }
                c.close();
            }
            return null;
        }
    }

    //*** 日付選択用ダイアログフラグメント ***//
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


    private static final String TAG = ReserveListActivity.class.getSimpleName();
    public static final String RESERVE_INFO = "reserve_info";


    @SuppressLint("StaticFieldLeak")
    static TextView txtDate;
    static TimeTableView timeTableView;
    public static Employee employee;
    private int thCnt = 0;
    public static ReserveListActivity instance = null;

//    public static List<Reserve> reserveInfo;    // 予約情報記録クラスの変数   非同期エラーが起きるため使用禁止する！

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        Log.d(TAG, "ReserveListActivity->onCreate()");
        /*** 各ウィジェットの初期化処理 && 社員情報の取得 ***/
        init();

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
        final Calendar c = Calendar.getInstance();
        txtDate.setText(String.format(Locale.JAPAN, "%04d/%02d/%02d", c.get(Calendar.YEAR) + 1, 1, 17));

        //*** コンテキストメニューの発生元ビューを取得 ***//
        registerForContextMenu(findViewById(R.id.content_main));

        /*** ここまで ***/


        // 予約情報の設定
//        getReserveInfo();
//        for (ReserveInfo r : reserveInfo) {
//            Log.d(TAG, r.getRe_id() + " : " + r.getRe_startTime() + "(" + r.getRe_endTime() + ") room_id : " + r.getRe_roomId());
//        }
        /*** 社員ID と アプリ起動時の日付を渡して、描画する ***/
        timeTableView = (TimeTableView) this.findViewById(R.id.timetable);
        Log.d("call", employee.getId());
        Log.d("call", txtDate.getText().toString());
        timeTableView.reView(employee.getId(), txtDate.getText().toString());

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "txtDate click!");
                MyDialog d = new MyDialog();
                d.show(getFragmentManager(), "dateDialog");
            }
        });

        /*** 予約情報リストの同期エラーがでるため、コメアウトします ***/
//        Button btPrev = (Button) findViewById(R.id.bt_prev);
//        btPrev.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(ReserveListActivity.this, "Prev", Toast.LENGTH_SHORT).show();
//                getReserveInfo();
//                timeTableView.reView(reserveInfo);
//            }
//        });
//        Button btNext = (Button) findViewById(R.id.bt_next);
//        btNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(ReserveListActivity.this, "Next", Toast.LENGTH_SHORT).show();
//                getReserveInfo();
//                timeTableView.reView(reserveInfo);
//            }
//        });
        Button bt_search = (Button) findViewById(R.id.bt_search);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReserveListActivity.this, txtDate.getText().toString(), Toast.LENGTH_SHORT).show();
                timeTableView.reView(employee.getId(), txtDate.getText().toString());
            }
        });


    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        getMenuInflater().inflate(R.menu.menu, menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        Toast.makeText(getApplicationContext(), "ContextMenu click!", Toast.LENGTH_SHORT).show();
//        return true;
//    }

    //*** 戻るボタン押下時の処理 ***//
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //*** ナビを選択したときの処理 ***//
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
    //*** 画面が表示・再表示されたらコールされる (画面遷移はここ！)***//
    @Override
    protected void onResume() {
        Log.d("call", "ReserveListActivity->onResume()");
        super.onResume();
        timeTableView.thread_flg = true;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (thCnt != 0) {
                    timeTableView.thread_flg = true;
                    timeTableView.x = 0;
                    timeTableView.y = 0;
                    thCnt = 0;
                }
                Log.d("call", "Thread");
                String re_id = timeTableView.getSelectedReserve();
                Log.d("call", "re_id :: " + re_id);
                //*** 新規予約登録画面への遷移 ***//
                if (re_id.equals(NONE)) {
                    Log.d("call", "新規予約登録画面への遷移");
                    Intent intent = new Intent(getApplicationContext(), ReserveActivity.class);
                    intent.putExtra("emp", employee);
                    startActivity(intent);
                } else {
                    Log.d("call", "予約確認画面への遷移");
                    Intent intent = new Intent(getApplicationContext(), ReserveConfirmActivity.class);
                    intent.putExtra("re_id", re_id);
                    intent.putExtra("emp", employee);
                    startActivity(intent);
                }
//                ReserveInfo reserveInfo = new ReserveInfo();
                // TODO: 2017/09/15  //*** 考えるので、いったんコメアウト ***//

//                Reserve reserveInfo = new Reserve();
//                reserveInfo.setRe_id(re_id);
//                Intent in = new Intent(getApplicationContext(), ReserveConfirmActivity.class);
////                in.putExtra("reserve_info", reserveInfo);
//                startActivity(in);
                thCnt++;
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }
    //*** SelfMadeMethod ***//
    //*** ウィジェットの初期化処理メソッド ***//
    private void init() {
        Log.d(TAG, "init()");
        // IMEIクラスのインスタンスを生成
        Imei imei = new Imei();
        imei.getTerminalImei(); // 端末IMEIを取得する

        // 社員情報の設定
        Object o = imei.getEmployeeInfo(); // 端末IMEIから、社員クラスのインスタンスを生成
        if (o != null) {
            employee = (Employee) o;
            Log.d("call", employee.toString());
        }
    }
    //*** この画面のインスタンスを返すメソッド（非アクティビティクラスで、DB検索する際に使用する） ***//
    public static ReserveListActivity getInstance() {
        return instance;
    }

    //    /***
//     * アプリを立ち上げた社員の端末ＩＭＥＩを返すメソッド
//     * @return 端末ＩＭＥＩ
//     */
//    public String getTerminalImei() {
//        Log.d(TAG, "getTerminalImei()");
//        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
////        return manager.getDeviceId();
//        return "352272080218786";   // 大馬 の 端末ＩＭＥＩ
//    }
//    private void getEmployeeInfo(String terminalImei) {
//        Log.d(TAG, "getEmployeeInfo()");
//
//        // 端末ＩＭＥＩから社員ＩＤを取得する
//        SQLiteOpenHelper helper = new DB(getApplicationContext());
//        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor c = db.rawQuery("select * from m_terminal where ter_id = ?", new String[]{terminalImei});
//        if (c.moveToNext()) {
//            // 端末ＩＭＥＩから社員ＩＤ取得が成功した
//            employee.setEmp_id(c.getString(1));
//        }
//        c.close();
//        SQLiteOpenHelper helper2 = new DB(getApplicationContext());
//        SQLiteDatabase db2 = helper2.getReadableDatabase();
//        Log.d(TAG, employee.getEmp_id());
//        // 社員ＩＤが空またはＮＵＬＬでなければ次のロジックを実行する
//        if (!employee.getEmp_id().isEmpty()) {
//            c = db2.rawQuery("select * from t_emp where emp_id = ?",
//                    new String[]{employee.getEmp_id()});
//            if (c.moveToNext()) {
//                // 社員ＩＤから社員情報を検索して、設定する
//                employee.setEmp_name(c.getString(1));
//                employee.setEmp_tel(c.getString(2));
//                employee.setEmp_mailaddr(c.getString(3));
//                employee.setDep_id(c.getString(4));
//                employee.setPos_id(c.getString(5));
//            } else {
//                Log.d(TAG, "社員情報の取得に失敗しました");
//            }
//            c.close();
//        }
//        Log.d(TAG, employee.getEmp_id() + " : " + employee.getEmp_name());
//    }

//    public void getReserveInfo() {
//        Log.d(TAG, "getReserveInfo()");
//        // 参加者テーブルから、予約ＩＤを取得
//        SQLiteOpenHelper helper = new DB(getApplicationContext());
//        SQLiteDatabase db = helper.getReadableDatabase();
//        String today = txtDate.getText().toString();    // アプリ起動時の日付を取得（作品展用に来年の１月１７日を設定）
//        Log.d("call", today);
//
//        // ***  アプリ起動時の日付で自分の参加会議を検索する *** //
//        Cursor c = db.rawQuery("select * from v_reserve_member where mem_id = ? and re_startday = ?",
//                new String[]{employee.getEmp_id(), today});
//        while (c.moveToNext()) {
//            // その社員が参加した会議情報をリストに追加する
//            ReserveInfo r = new ReserveInfo(
//                    c.getString(0),
//                    c.getString(1),
//                    c.getString(2),
//                    c.getString(3),
//                    c.getString(4),
//                    c.getString(5),
//                    c.getString(6),
//                    c.getString(12)
//            );
//            reserveInfo.add(r);
//        }
//        c.close();
//    }

}
