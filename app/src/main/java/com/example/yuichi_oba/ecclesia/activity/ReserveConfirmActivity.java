package com.example.yuichi_oba.ecclesia.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.Employee;
import com.example.yuichi_oba.ecclesia.model.OutEmployee;
import com.example.yuichi_oba.ecclesia.model.Person;
import com.example.yuichi_oba.ecclesia.model.Reserve;
import com.example.yuichi_oba.ecclesia.tools.DB;
import com.example.yuichi_oba.ecclesia.tools.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.EX;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.KEYCHANGE;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.KEYEX;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.ONE;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.ZERO;

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/
// _/_/ 予約の詳細・確認を行うアクティビティ
// _/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// DO: 2017/09/19 延長ダイアログの正常動作の実装
// TODO: 2017/09/19 延長ダイアログのレイアウト調整およびデザインの考察 
public class ReserveConfirmActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //***  ***//
//    public static Reserve reserve;
    private Employee employee;
    public static String re_id;
    public static String gamen;
    public static Reserve reserve;
    private Button btn_confirm;
    // 内部クラスからgetApplicationContextするためのやつ(普通にやるとno-staticで怒られる)
    private static ReserveConfirmActivity instance = null;
    // デバッグ用
    private static final String TAG = ReserveConfirmActivity.class.getSimpleName();

    //*** 会議参加者をリスト形式で出す、ダイアログフラグメントクラス ***//
    public static class MemberConfirmDialog extends DialogFragment {
        // ダイアログを生成するメソッド
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Log.d("call", "call MemberConfirmDialog->onCreateDialog()");
            // 会議参加者データ
//            CharSequence[] items = reserve.getRe_member().toArray(new CharSequence[reserve.getRe_member().size()]);
            CharSequence[] items;                       //***  ***//
            List<String> list = new ArrayList<>();      //***  ***//

            //***  ***//
            for (Person p : reserve.getRe_member()) {
                if (p instanceof Employee) {            //***  ***//
                    list.add(String.format("社内 : %s", p.getName()));                                    //***  ***//
                } else if (p instanceof OutEmployee) {  //***  ***//
                    list.add(String.format("%s : %s", ((OutEmployee) p).getCom_name(), p.getName()));     //***  ***//
                }
            }
            items = (CharSequence[]) list.toArray(new CharSequence[list.size()]);    //***  ***//
            return new AlertDialog.Builder(getActivity())
                    .setTitle("会議参加者一覧")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .create();
        }

        // ダイアログを破棄するメソッドーー＞HCP不要
        @Override
        public void onPause() {
            super.onPause();
            dismiss();
        }
    }

    //*** 早期退出」オプション選択時の ダイアログフラグメントクラス ***//
    public static class EarlyOutDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("早期退出")
                    .setMessage("早期退出しますか？")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(getActivity(), "早期退出", Toast.LENGTH_SHORT).show();
                            //*** 必要なインスタンスを用意 ***//
                            SQLiteOpenHelper helper = new DB(instance.getApplicationContext());
                            SQLiteDatabase db = helper.getWritableDatabase();
                            //*** トランザクション開始 ***//
                            db.beginTransaction();
                            //*** DBへ更新をかけるために用意 ***//
                            ContentValues con = new ContentValues();
                            //*** 現在時刻取得 ***//
                            Date ealDate = new Date();
                            //*** フォ－マットを用意 ***//
                            SimpleDateFormat ealFor = new SimpleDateFormat("HH：mm");
                            //*** 現在時刻をフォーマットにかけてStringへ変換 ***//
                            String ealTime = ealFor.format(ealDate);
                            //*** 早期退出による終了時刻をセット ***//
                            con.put("re_endTime", ealTime);
                            Log.d("ealTIme", ealTime);
                            //*** where句を用意 ***//
                            String where = "re_id = ?";
                            //*** ?に入れるものを指定する ***//
                            String whereArgs[] = new String[ONE];
                            whereArgs[ZERO] = reserve.getRe_id();
                            //*** updateをかける ***//
                            db.update("t_reserve", con, where, whereArgs);
                            //*** コミットかける ***//
                            db.setTransactionSuccessful();
                            //*** トランザクション終了 ***//
                            db.endTransaction();
                            //*** 早期退出完了通知ダイアログを表示 ***//
                            EalryOutResultDialog ealryOutResultDialog = new EalryOutResultDialog();
                            ealryOutResultDialog.show(getFragmentManager(), "out");
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .create();
        }

        @Override
        public void onPause() {
            super.onPause();
            dismiss();
        }
    }

    //*** 早期退出完了通知ダイアログ ***//
    public static class EalryOutResultDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity()).setTitle("早期退出完了")
                    .setMessage("早期退出が完了しました").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create();
        }
    }

    //*** 延長完了を通知するダイアログ ***//
    public static class ExtentResultDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity()).setTitle("延長完了")
                    .setMessage("延長が完了しました").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create();
        }

        @Override
        public void onPause() {
            super.onPause();
            dismiss();
        }
    }

    //*** 延長オプション選択時の ダイアログフラグメントクラス ***//
    public static class ExtentionDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final RelativeLayout layout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.extention_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            return builder.setTitle(EX)
                    .setView(layout)
                    .setPositiveButton(EX, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String exTime = "";
                            //*** スピナーで選択された延長時間を代入 ***//
                            Spinner spTime = (Spinner) layout.findViewById(R.id.extentionDia_time);
                            exTime = spTime.getSelectedItem().toString();
                            //*** 必要なインスタンス類を用意 ***//
                            SQLiteOpenHelper helper = new DB(instance.getApplicationContext());
                            SQLiteDatabase db = helper.getWritableDatabase();
                            //*** トランザクション開始 ***//
                            db.beginTransaction();
                            //*** 延長情報をDBへ投げるために用意 ***//
                            ContentValues con = new ContentValues();
                            //*** 延長による終了時刻を計算 ***//
                            SimpleDateFormat endFor = new SimpleDateFormat("HH：mm");
                            Calendar excal = Calendar.getInstance();
                            Log.d("nowEnd", reserve.getRe_endTime());
                            //*** フォーマットで変換をかけてCalenderにセット ***//
                            try {
                                excal.setTime(endFor.parse(reserve.getRe_endTime()));
                                Log.d("changeTime", String.valueOf(endFor.parse(reserve.getRe_endTime())));
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
                            //*** DBにインサートする延長情報をセット ***//
                            con.put("re_id", reserve.getRe_id());
                            con.put("ex_startDay", reserve.getRe_startDay());
                            con.put("ex_startTime", reserve.getRe_startTime());
                            con.put("ex_endDay", reserve.getRe_endDay());
                            con.put("ex_endTime", exTime);
                            con.put("ex_endtime", exTime);
                            //*** 延長テーブルにインサートをかける ***//
                            db.insert("t_extension", null, con);
                            //*** コミットみたいな感じ ***//
                            db.setTransactionSuccessful();
                            //*** トランザクション終了 ***//
                            db.endTransaction();
                            //*** 延長結果ダイアログを表示 ***//
                            ExtentResultDialog extentResultDialog = new ExtentResultDialog();
                            extentResultDialog.show(getFragmentManager(), KEYEX);

//                            //*** スピナーで選択された延長時間を代入 ***//
//                            Spinner spTime = (Spinner) layout.findViewById(R.id.extentionDia_time);
//                            exTime = spTime.getSelectedItem().toString();
//                            //*** 必要なインスタンス類を用意 ***//
//                            SQLiteOpenHelper helper = new DB(instance.getApplicationContext());
//                            SQLiteDatabase db = helper.getWritableDatabase();
//                            //*** トラン始まり ***//
//                            db.beginTransaction();
//                            //*** 延長情報をDBへ投げるために用意 ***//
//                            ContentValues con = new ContentValues();
//                            //*** 延長による終了時刻を計算 ***//
//                            SimpleDateFormat endFor = new SimpleDateFormat("HH：mm");
//                            Calendar excal = Calendar.getInstance();
//                            Log.d("nowEnd", reserve.getRe_endTime());
//                            try {
//                                //*** フォーマットで変換をかけてCalenderにセット ***//
//                                excal.setTime(endFor.parse(reserve.getRe_endTime()));
//                                Log.d("changeTime", String.valueOf(endFor.parse(reserve.getRe_endTime())));
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                            //*** セットされたCalenderに延長時間を加算する ***//
//                            excal.add(Calendar.MINUTE, Integer.parseInt(exTime));
//                            //*** CalenderをDateに変換 ***//
//                            Date exDate = excal.getTime();
//                            //*** DateをフォーマットにかけてStringに変換 ***//
//                            exTime = endFor.format(exDate);
//                            Log.d("exTIme", exTime);
//                            //*** DBにインサートする延長情報をセット ***//
//                            con.put("re_id", reserve.getRe_id());
//                            con.put("ex_startDay", reserve.getRe_startDay());
//                            con.put("ex_startTime", reserve.getRe_startTime());
//                            con.put("ex_endDay", reserve.getRe_endDay());
////                            con.put("ex_endTime", reserve.getRe_endTime());
//                            con.put("ex_endtime", exTime);
//                            //*** 延長テーブルにインサートをかける ***//
//                            db.insert("t_extension", null, con);
//                            //*** コミット ***//
//                            db.setTransactionSuccessful();
//                            //*** トラン終わり ***//
//                            db.endTransaction();
//                            //*** 延長結果ダイアログを表示 ***//
//                            ExtentResultDialog extentResultDialog = new ExtentResultDialog();
//                            extentResultDialog.show(getFragmentManager(), KEYEX);
                        }
                    }).setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create();
        }

        @Override
        public void onPause() {
            super.onPause();
            dismiss();
        }
    }

    //*** 参加者をリスト形式で、表示するためのダイアログフラグメントクラス ***//

    //*** onCreate ***//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("call", "ReserveConfirmActivity->onCreate()");

        //*** 前画面からの引数を受け取る ***//
        Intent intent = getIntent();
        gamen = intent.getStringExtra("gamen").contains("0") ? "新規" : "一覧"; //*** 0: 新規  1: 一覧　からの画面遷移 ***//

        // TODO: 2017/10/03 遷移元画面から、Reserveクラスのインスタンスをもらうよう修正する
        Log.d("call", "画面遷移元　" + gamen);
        //*** 画面遷移元によって、処理を分ける ***//
        if (gamen.contains("新規")) {    //*** 「新規」画面からの画面遷移 ***//
            employee = (Employee) intent.getSerializableExtra("emp");        //*** 社員情報の取得 ***//
            reserve = (Reserve) intent.getSerializableExtra("reserve");     //*** 予約情報のインスタンスを取得 ***//

        } else {                         //*** 「一覧」画面からの画面遷移 ***//
            reserve = (Reserve) intent.getSerializableExtra("reserve");     //*** 予約情報のインスタンスを取得 ***//
        }
        instance = this;

        setReserveDetail(); //***  ***//

        /***
         * レイアウト情報をマッピングする
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_confirm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /***
         * ここまで
         */

        // 予約詳細をDB検索して、画面にマッピングするメソッド
//        dbSearchReserveConfirm();
    }


    //*** アクティビティのライフサイクルとして、別の画面にいってまた帰ってきたとき、コールされる ***//
    @Override
    protected void onStart() {
        super.onStart();
//        dbSearchReserveConfirm();
    }

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

    //*** オプション画面を作成するメソッド ***//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reserve_confirm, menu);
        return true;
    }

    //*** オプションを選択したときの処理 ***//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 選択されたオプションのIDを取得し、代入する
        int id = item.getItemId();

        Intent intent;
        //*** 現在時刻取得 ***//
        Calendar cal = Calendar.getInstance();
        //*** 比較用Calender ***//
        Calendar cmp = Calendar.getInstance();
        //*** フォーマット用意 ***//
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/MM/dd HH：mm");
        // idによって処理を分ける
        switch (id) {
            // 「早期退出」が選択された
            case R.id.option_earlyOut:
                try {
                    //*** Calenderにセット ***//
                    cmp.setTime(timeFormat.parse(reserve.getRe_endDay() + " " + reserve.getRe_endTime()));
                } catch (ParseException e) {
                    e.getStackTrace();
                    break;
                }
                //*** 退出しようとしている会議が現在日付・時刻に矛盾していないか ***//
                if ((cal.get(Calendar.YEAR) == cmp.get(Calendar.YEAR)) && (cal.get(Calendar.MONTH) == cmp.get(Calendar.MONTH)) && (cal.get(Calendar.DAY_OF_MONTH) == cmp.get(Calendar.DAY_OF_MONTH))
                        && (cal.get(Calendar.HOUR_OF_DAY) <= cmp.get(Calendar.HOUR_OF_DAY)) && (cal.get(Calendar.MINUTE) < cmp.get(Calendar.MINUTE))) {
                    //*** 早期退出ダイアログを表示 ***//
                    EarlyOutDialog earlyOutDialog = new EarlyOutDialog();
                    earlyOutDialog.show(getFragmentManager(), "out");
                } else {
                    Toast.makeText(this, "早期退出できる会議ではありません", Toast.LENGTH_SHORT).show();
                    //*** 試験的に、ダメでも出来るようにしておく（いずれ削除） ***//
                    EarlyOutDialog earlyOutDialog = new EarlyOutDialog();
                    earlyOutDialog.show(getFragmentManager(), "out");
                }
                break;
            // 「予約変更」が選択された
            case R.id.option_reserveChange:
                re_id = reserve.getRe_id();
                try {
                    //*** 変更しようとしている会議の開始時間をセット ***//
                    cmp.setTime(timeFormat.parse(reserve.getRe_startDay() + " " + reserve.getRe_startTime()));
                } catch (ParseException e) {
                    e.getStackTrace();
                    break;
                }
                //*** 変更しようとしている会議が現在日付・時刻に矛盾していないか ***//
                if ((cal.get(Calendar.YEAR) == cmp.get(Calendar.YEAR)) && (cal.get(Calendar.MONTH) == cmp.get(Calendar.MONTH)) && cal.get(Calendar.DAY_OF_MONTH) == cmp.get(Calendar.DAY_OF_MONTH)
                        && (cal.get(Calendar.HOUR_OF_DAY)) <= cal.get(Calendar.HOUR_OF_DAY) && (cal.get(Calendar.MINUTE) < cmp.get(Calendar.MINUTE))) {
                    //*** 次画面（ReserveChangeActivity）に予約インスタンスを渡す ***//
                    intent = new Intent(getApplicationContext(), ReserveChangeActivity.class);
                    intent.putExtra(KEYCHANGE, reserve);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "変更できる会議ではありません", Toast.LENGTH_SHORT).show();
                    //*** 試験的に、ダメでも出来るようにしておく（いずれ削除） ***//
                    intent = new Intent(getApplicationContext(), ReserveChangeActivity.class);
                    intent.putExtra(KEYCHANGE, reserve);
                    startActivity(intent);
                }
//                Toast.makeText(this, "予約変更", Toast.LENGTH_SHORT).show();
                break;
            // 「延長」が選択された
            case R.id.option_extention:
                //*** 比較用Calenderその２ ***//
                Calendar cmp2 = Calendar.getInstance();
                try {
                    //*** 延長を試みる会議の開始終了時刻をセット ***//
                    cmp.setTime(timeFormat.parse(reserve.getRe_startDay() + " " + reserve.getRe_startTime()));
                    cmp2.setTime(timeFormat.parse(reserve.getRe_endDay() + " " + reserve.getRe_endTime()));
                } catch (ParseException e) {
                    e.getStackTrace();
                    break;
                }
                //*** 延長しようとしている会議が現在日付・時刻に矛盾していないか ***//
                if (((cal.get(Calendar.YEAR) == cmp.get(Calendar.YEAR)) || (cal.get(Calendar.YEAR) == cmp2.get(Calendar.YEAR)))
                        && ((cal.get(Calendar.MONTH) == cmp.get(Calendar.MONTH)) || (cal.get(Calendar.MONTH) == cmp2.get(Calendar.MONTH)))
                        && ((cal.get(Calendar.DAY_OF_MONTH) == cmp.get(Calendar.DAY_OF_MONTH)) || (cal.get(Calendar.DAY_OF_MONTH) == cmp2.get(Calendar.DAY_OF_MONTH)))
                        && (cal.get(Calendar.HOUR_OF_DAY) <= cmp2.get(Calendar.HOUR_OF_DAY)) && (cal.get(Calendar.MINUTE) < cmp2.get(Calendar.MINUTE))) {
                    //*** 延長ダイアログを表示 ***//
                    ExtentionDialog extentionDialog = new ExtentionDialog();
                    extentionDialog.show(getFragmentManager(), KEYEX);
                } else {
                    Toast.makeText(this, "延長ができる会議ではありません", Toast.LENGTH_SHORT).show();
                    //*** 試験的に、ダメでも出来るようにしておく（いずれ削除） ***//
                    ExtentionDialog extentionDialog = new ExtentionDialog();
                    extentionDialog.show(getFragmentManager(), KEYEX);
                }
                break;
        }
        // 選択された結果（項目）を返す
        return super.onOptionsItemSelected(item);
    }

    //*** ナビを選択したときの処理 ***//
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // 選択されたIDを取得し、代入する
        int id = item.getItemId();
        Intent intent = null;
        switch (id) {
            // 「予約一覧」が選択された
            case R.id.nav_reserve_list:
                // Intentクラスのインスタンスを生成し、画面遷移情報を記録する
                intent = new Intent(getApplicationContext(), ReserveListActivity.class);
                break;
            // 「履歴検索」が選択された
            case R.id.nav_rireki:
                // 同上
                intent = new Intent(getApplicationContext(), HistorySearchActivity.class);
                break;
            // 「管理者認証」が選択された
            case R.id.nav_admin_auth:
                // 管理者認証ダイアログを生成する
                AuthDialog authDialog = new AuthDialog();
                authDialog.show(getFragmentManager(), "aaa");
                break;

        }
        // 画面遷移する
        if (intent != null) {
            startActivity(intent);
        }
        // HCP不要
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    // これでどこからでもgetApplicationContextできる
    // 同一Activityから呼び出す際は不要
//    public static ReserveConfirmActivity getInstance() {
//        return instance;
//    }

    //*** --- SELF MADE METHOD --- 各ウィジェットの初期化処理メソッド ***//
    public void init() {
        btn_confirm = (Button) findViewById(R.id.arconfirm_btn_mem_confirm);    //*** 参加者確認ボタン ***//
    }

    //*** --- SELF MADE METHOD --- 参加者確認ボタン押下時の処理 ***//
    public void onClickMemConfirm(View view) {
        Log.d("call", "btn_confirm_member->onClick()");
        //*** 参加者一覧ダイアログを表示する ***//
        MemberConfirmDialog dialog = new MemberConfirmDialog();
        dialog.show(getFragmentManager(), "confirm_a");
    }

    //*** --- SELF MADE METHOD --- 確定ボタン押下時の処理 ***//
    public void onClickKakutei(View view) throws ParseException {
        Log.d("call", "call onClickKakutei");


        //*** 申請者の氏名－＞ 社員IDに変換して、予約インスタンスにセットする ***//
        reserve.setRe_applicant(Util.returnEmpId(reserve.getRe_applicant()));

        // TODO: 2017/10/14 社外利用ならば、問答無用で、インサート、元あった会議は、追い出し


        //*** 会議時間帯の重複 優先度チェック ***//
        if (!reserve.timeDuplicationCheck(reserve) || !reserve.priorityCheck(reserve)){
            return ;
        }

        //*** 予約の確定メソッドコールし、予約確定をおこなう ***//
        int rs = reserve.reserveCorrenct(reserve, setReserveDetail());   //***  ***//
        Log.d("call", String.format("登録結果 %d 件", rs));

//        db.execSQL("insert into t_reserve values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
//                new Object[]{
//                        reserve.getRe_id(),
//                        reserve.getRe_name(),
//                        reserve.getRe_startDay(),
//                        reserve.getRe_endDay(),
//                        reserve.getRe_startTime(),
//                        reserve.getRe_endTime(),
//                        reserve.getRe_switch(),
//                        reserve.getRe_fixtures(),
//                        reserve.getRe_remarks(),
//                        priorityAverage,
//                        "aa",
//                        reserve.getRe_applicant(),
//                        reserve.getRe_room_id(),
//                        reserve.getRe_purpose_id(),
//                        reserve.getRe_applicant()
//                });
//
//        if (ret == -1) {
//            Log.d("call", "予約情報のインサート処理失敗!");
//        } else {
//            Log.d("call", "予約情報のインサート処理成功！");
//        }
//        db.close();
    }

    //*** --- SELF MADE METHOD --- 予約インスタンスの情報を、DBに書き込める形にまで設定するメソッド ***//
    private float setReserveDetail() {
        Integer sumPriority = 0;
        // TODO: 2017/10/06 会議目的優先度をどう処理するか考察

        //*** 参加者の優先度の合計を算出する ***//
        for (Person p : reserve.getRe_member()) {
            if (p instanceof Employee) {                //*** 社員クラス ***//
                //***  ***//
                sumPriority += Integer.valueOf(((Employee) p).getPos_priority());
            } else if (p instanceof OutEmployee) {      //*** 社外者クラス ***//
                //***  ***//
                sumPriority += Integer.valueOf(((OutEmployee) p).getPos_priority());
            }
        }
        //*** 参加者の優先度合計の平均を算出してその値を返す ***//
        return sumPriority / reserve.getRe_member().size();
    }
}
