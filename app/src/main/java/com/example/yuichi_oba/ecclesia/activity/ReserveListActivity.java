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
import com.example.yuichi_oba.ecclesia.model.ReserveInfo;
import com.example.yuichi_oba.ecclesia.tools.DB;
import com.example.yuichi_oba.ecclesia.view.TimeTableView;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/
// _/_/ 予約状況(リストで視覚的にわかりやすい）を表示するアクティビティ
// _/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
public class ReserveListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    public static class TimeTableView extends View {
//
//        public static final String TOKUBETSU = "0001";
//        public static final String ROOM_A = "0002";
//        public static final String ROOM_B = "0003";
//        public static final String ROOM_C = "0004";
//        private Paint p;
//        private Paint p2;
//        private Paint room;
//        private Paint tokubetsu;
//        private Paint roomA;
//        private Paint roomB;
//        private Paint roomC;
//
//        private float[] timeFloats = {300,500,700,900,1100,1300,1500};
//
//        public TimeTableView(Context context) {
//            super(context);
//            init();
//        }
//        public TimeTableView(Context context, @Nullable AttributeSet attrs) {
//            super(context, attrs);
//            init();
//        }
//        public TimeTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//            super(context, attrs, defStyleAttr);
//            init();
//        }
//
//        /***
//         * 描画するメソッド
//         * @param c
//         */
//        @Override
//        protected void onDraw(Canvas c) {
//            Log.d("call", "TimeTableView->onDraw()");
//            // 時間割の枠の描画
//            onDrawTimeTable(c);
//
//            // アプリを立ち上げた社員の予約情報の描画
//            int cnt = 0;
//            for (ReserveInfo r : reserveInfo) {
//                String sTime = r.getRe_startTime();
//                String eTime = r.getRe_endTime();
//                String room_id = r.getRe_roomId();
//
//                RectF rectF = retRectCooperation(sTime, eTime, room_id);
//                // 予約会議の座標情報を記録する
//                reserveInfo.get(cnt).setCoop(new float[]{rectF.left, rectF.top, rectF.right, rectF.bottom});
//                switch (room_id) {
//                    case "0001":
//                        room = tokubetsu;
//                        break;
//                    case "0002":
//                        room = roomA;
//                        break;
//                    case "0003":
//                        room = roomB;
//                        break;
//                    case "0004":
//                        room = roomC;
//                        break;
//                }
//                // 予約会議の描画
//                c.drawRoundRect(rectF, 30, 30, room);
//                cnt++;
//            }
//        }
//
//        /***
//         * 開始終了時刻・会議室を基に、描画すべき座標を返すメソッド
//         * @param sTime
//         * @param eTime
//         * @param room_id
//         */
//        private RectF retRectCooperation(String sTime, String eTime, String room_id) {
//            float sX = 0, eX = 0, sY = 0, eY = 0;
//            float x = 216;
//            switch (room_id) {
//                case TOKUBETSU:
//                    sX = x;
//                    eX = 2 * x;
//                    break;
//                case ROOM_A:
//                    sX = 2 * x;
//                    eX = 3 * x;
//                    break;
//                case ROOM_B:
//                    sX = 3 * x;
//                    eX = 4 * x;
//                    break;
//                case ROOM_C:
//                    sX = 4 * x;
//                    eX = 5 * x;
//                    break;
//            }
//            int s = Integer.parseInt(sTime.split("：")[0]) - 8; // 08:00 -> 8 => 8 - 8 = 0
//            sY = timeFloats[s];
//            if (Integer.parseInt(sTime.split("：")[1]) >= 30) {
//                sY += 100;
//            }
//
//            int e = Integer.parseInt(eTime.split("：")[0]) - 8; // 10:30 -> 10 - 8 = 2
//            eY = timeFloats[e];
//            if (Integer.parseInt(eTime.split("：")[1]) >= 30) { // 30 >= 30
//                eY += 100;
//            }
//
//            return new RectF(sX, sY, eX, eY);
//        }
//
//        /***
//         * 時間割の枠の描画
//         * @param canvas
//         */
//        private void onDrawTimeTable(Canvas canvas) {
//            float x = 216;
//            float y_timetable = 300;
//            for (int i = 1; i <= 4; i++) {
//                canvas.drawLine(i * x, y_timetable, i * x, MAX_HEIGHT, p2);
//            }
//            float y = 100;
//            for (int i = 4; i < 17; i++) {
//                canvas.drawLine(x, i * y, MAX_WIDTH, i * y, p2);
//                if (i % 2 == 1) {
//                    canvas.drawLine(ZERO, i * y, x, i * y, p);
//                }
//            }
//            canvas.drawRect(ZERO, y_timetable, MAX_WIDTH, MAX_HEIGHT, p);
//            canvas.drawLine(x, y_timetable, x, MAX_HEIGHT, p);
//        }
//
//        /***
//         * Paintクラスの初期化処理メソッド
//         */
//        private void init() {
//            Log.d("call", "call TimeTableView->init()");
//            // 枠線用
//            p = new Paint();
//            p.setColor(Color.DKGRAY);
//            p.setStyle(Paint.Style.STROKE);
//            p.setStrokeWidth(10);
//
//            p2 = new Paint();
//            p2.setStrokeWidth(2.0f);
//
//            // 特別会議室用
//            tokubetsu = new Paint();
//            tokubetsu.setColor(Color.parseColor("#E91E63"));
//            tokubetsu.setStyle(Paint.Style.FILL);
//            tokubetsu.setStrokeWidth(10);
//
//            // 会議室Ａ用
//            roomA = new Paint();
//            roomA.setColor(Color.parseColor("#536DFE"));
//            roomA.setStyle(Paint.Style.FILL);
//            roomA.setStrokeWidth(10);
//
//            // 会議室Ｂ用
//            roomB = new Paint();
//            roomB.setColor(Color.parseColor("#4CAF50"));
//            roomB.setStyle(Paint.Style.FILL);
//            roomB.setStrokeWidth(10);
//
//            // 会議室Ｃ用
//            roomC = new Paint();
//            roomC.setColor(Color.parseColor("#FFC107"));
//            roomC.setStyle(Paint.Style.FILL);
//            roomC.setStrokeWidth(10);
//        }
//
//        @Override
//        public boolean onTouchEvent(MotionEvent e) {
//            switch (e.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    // タップした座標を取得する
//                    float x = e.getX();
//                    float y = e.getY();
//                    Log.d("call", e.getX() + " : " + e.getY());
//                    // x座標を基に、どの会議室か特定する
//                    String room_id = "";
//                    float wX = 216;
//                    if (x > wX && x < 2 * wX) {
//                        Log.d("call", "tokubetu");
//                        room_id = TOKUBETSU;
//                    } else if (x > 2 * wX && x < 3 * wX) {
//                        Log.d("call", "roomA");
//                        room_id = ROOM_A;
//                    } else if (x > 3 * wX && x < 4 * wX) {
//                        Log.d("call", "roomB");
//                        room_id = ROOM_B;
//                    } else if (x > 4 * wX && x < 5 * wX) {
//                        Log.d("call", "roomC");
//                        room_id = ROOM_C;
//                    }
//                    // room_id と y座標を基に、どの会議がタップされたかを返す
//                    for (ReserveInfo r : reserveInfo) {
//                        if (r.getCoop()[1] < y && r.getCoop()[3] > y) {
//                            // 特定した
//                            if (room_id.equals(r.getRe_roomId())) {
//                                Log.d("call", r.getRe_id());
//                                // 特定した会議予約IDを予約確認（ReserveConfirmActivity）に渡す
//                            Intent in = new Intent(getApplicationContext(), ReserveConfirmActivity.class);
//                            in.putExtra("reserveInfo", r.getRe_id());
//                                Log.d("call", "ここで、予約確認に遷移させる");
//                            }
//                        }
//                    }
//                    // その予約情報をもって、予約確認（ReserveConfirmActivity）に飛ぶ
//                    break;
//            }
//            return false;
//        }
//    }

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

    @SuppressLint("StaticFieldLeak")
    static TextView txtDate;
    Employee employee;
//    public static List<ReserveInfo> reserveInfo;    // 予約情報記録クラスの変数   非同期エラーが起きるため使用禁止する！
    static TimeTableView timeTableView;
    private int thCnt = 0;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "ReserveListActivity->onCreate()");
        /*** 各ウィジェットの初期化処理 ***/
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
        /*** ここまで ***/



        // 予約情報の設定
//        getReserveInfo();
//        for (ReserveInfo r : reserveInfo) {
//            Log.d(TAG, r.getRe_id() + " : " + r.getRe_startTime() + "(" + r.getRe_endTime() + ") room_id : " + r.getRe_roomId());
//        }
        /*** 社員ID と アプリ起動時の日付を渡して、描画する ***/
        timeTableView = (TimeTableView) this.findViewById(R.id.timetable);
        timeTableView.reView(employee.getEmp_id(), txtDate.getText().toString());

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
                timeTableView.reView(employee.getEmp_id(), txtDate.getText().toString());
            }
        });


    }

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

    /***
     * 画面が表示・再表示されたらコールされる
     */
    @Override
    protected void onResume() {
        Log.d("call", "ReserveListActivity->onResume()");
        super.onResume();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (thCnt != 0) {
                    timeTableView.thread_flg = true;
                    timeTableView.x = 0;
                    timeTableView.y = 0;
                }
                Log.d("call", "Thread");
                String re_id = timeTableView.getSelectedReserve();
                Log.d("call", "re_id :: " + re_id);
                if (re_id.equals(NONE)) {
                    Intent intent = new Intent(getApplicationContext(), ReserveActivity.class);
                    startActivity(intent);
                }
                ReserveInfo reserveInfo = new ReserveInfo();
                reserveInfo.setRe_id(re_id);
                Intent in = new Intent(getApplicationContext(), ReserveConfirmActivity.class);
                in.putExtra("reserve_info", reserveInfo);
                startActivity(in);
                thCnt++;
            }
        });
        thread.start();
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
//        reserveInfo = new ArrayList<>();

        // Permission error となる・・・なんで？
        // 端末ＩＭＥＩの取得
        String terminalImei = getTerminalImei();
        // 社員情報の設定
        getEmployeeInfo(terminalImei);
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
        c.close();
        SQLiteOpenHelper helper2 = new DB(getApplicationContext());
        SQLiteDatabase db2 = helper2.getReadableDatabase();
        Log.d(TAG, employee.getEmp_id());
        // 社員ＩＤが空またはＮＵＬＬでなければ次のロジックを実行する
        if (!employee.getEmp_id().isEmpty()) {
            c = db2.rawQuery("select * from t_emp where emp_id = ?",
                    new String[]{employee.getEmp_id()});
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
            c.close();
        }
        Log.d(TAG, employee.getEmp_id() + " : " + employee.getEmp_name());
    }

    /***
     *
     */
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
