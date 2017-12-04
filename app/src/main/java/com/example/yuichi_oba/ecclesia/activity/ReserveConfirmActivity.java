package com.example.yuichi_oba.ecclesia.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AdminLogOut;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.Employee;
import com.example.yuichi_oba.ecclesia.model.OutEmployee;
import com.example.yuichi_oba.ecclesia.model.Person;
import com.example.yuichi_oba.ecclesia.model.Reserve;
import com.example.yuichi_oba.ecclesia.tools.MyHelper;
import com.example.yuichi_oba.ecclesia.tools.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.yuichi_oba.ecclesia.activity.ReserveListActivity.authFlg;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.*;

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/
// _/_/ 予約の詳細・確認を行うアクティビティ
// _/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// DO: 2017/09/19 延長ダイアログの正常動作の実装
// TODO: 2017/09/19 延長ダイアログのレイアウト調整およびデザインの考察
public class ReserveConfirmActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String NON_AUTH = "0";
    private MyHelper helper = new MyHelper(this);
    private static SQLiteDatabase db;

    //***  ***//
//    public static Reserve reserve;
    private Employee employee;
    public static String re_id;
    public static String gamen;
    public static Reserve reserve;
    public static boolean evictionFlg = false;
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
            Log.d(CALL, "call MemberConfirmDialog->onCreateDialog()");
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
                    .setPositiveButton(OK, new DialogInterface.OnClickListener() {
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

    //*** 早期退出オプション選択時の ダイアログフラグメントクラス ***//
    public static class EarlyOutDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(EARLY)
                    .setMessage(EARLY + RUNQUESTION)
                    .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //*** メソッドによる早期退出 ***//
                            reserve.earlyExit();

                            //*** 早期退出完了ダイアログを出す ***//
                            Bundle diaBundle = new Bundle();
                            diaBundle.putString(KEYRESULT, KEYEAR);
                            ResultDialog resultDialog = new ResultDialog();
                            resultDialog.setArguments(diaBundle);
                            resultDialog.show(getFragmentManager(), KEYEAR);
                        }
                    })
                    .setNegativeButton(CANCEL, new DialogInterface.OnClickListener() {
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

  //*** 延長ないし早期退出完了通知のダイアログフラグメントクラス ***//
  public static class ResultDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      String str = EMPTY, title = EMPTY;
      switch (getArguments().getString(KEYRESULT)) {
        case KEYSMALLEX:
          title = EX + COMPLETE;
          str = EX + RUNMESSAGE;
          break;
        case KEYEAR:
          title = EARLY + COMPLETE;
          str = EARLY + RUNMESSAGE;
          break;

//        case "change":
//          title = "変更完了";
//          str = "変更が完了しました";
//          break;
            }

            return new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(str).setPositiveButton(OK, null).create();
        }
    }

    //*** 延長オプション選択時の ダイアログフラグメントクラス ***//
    //*** 結果ダイアログが即出る 見た目をもうちょっとマシに ***//
    public static class ExtensionDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final RelativeLayout layout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.extention_dialog, null);
            TextView endTime = (TextView) layout.findViewById(R.id.dia_endTime);
            endTime.setText(reserve.getRe_endTime());
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            return builder.setTitle(EX)
                    .setView(layout)
                    .setPositiveButton(EX, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String exTime;
                            //*** スピナーで選択された延長時間を代入 ***//
                            Spinner spTime = (Spinner) layout.findViewById(R.id.extentionDia_time);
                            exTime = spTime.getSelectedItem().toString();
                            //*** 延長による終了時刻を計算 ***//
                            SimpleDateFormat endDayTimeFor = new SimpleDateFormat(YYYY_MM_DD_HH_MM);
                            SimpleDateFormat endTimeFor = new SimpleDateFormat(HH_MM);
                            SimpleDateFormat endDayFor = new SimpleDateFormat(YYYY_MM_DD);
                            Calendar excal = Calendar.getInstance();
                            Log.d(CALL, "現在の終了時間：" + reserve.getRe_endTime());
                            //*** フォーマットで変換をかけてCalenderにセット ***//
                            try {
                                excal.setTime(endDayTimeFor.parse(reserve.getRe_endDay() + SPACE + reserve.getRe_endTime()));
                            } catch (ParseException e) {
                                e.getStackTrace();
                                Log.d(CALL, "Parse失敗");
                            }
                            //*** セットされたCalenderに延長時間を加算する ***//
                            excal.add(Calendar.MINUTE, Integer.parseInt(exTime));
                            //*** CalenderをDateに変換 ***//
                            Date exDate = excal.getTime();
                            //*** DateをフォーマットにかけてStringに変換 ***//
                            exTime = endTimeFor.format(exDate);
                            Log.d(CALL, "延長時間：" + exTime);

                            //*** 延長による開始終了時間の書き換え ***//
                            reserve.setRe_startDay(reserve.getRe_endDay());
                            reserve.setRe_startTime(reserve.getRe_endTime());
                            reserve.setRe_endDay(endDayFor.format(excal.getTime()));
                            reserve.setRe_endTime(exTime);
                            Log.d(CALL, "延長開始日：" + reserve.getRe_startDay());
                            Log.d(CALL, "延長終了日：" + reserve.getRe_endDay());
                            Log.d(CALL, "延長開始時刻：" + reserve.getRe_startTime());
                            Log.d(CALL, "延長終了時刻：" + reserve.getRe_endTime());

                            //*** 延長による追い出し処理 ***//
                            String checkResult = reserve.timeDuplicationCheck(reserve);
                            if (checkResult.equals(FALSE)) {
                                Log.d(CALL, "重複発生 優先度も負け");
                                return;
                            } else if (checkResult.equals(TRUE)) {
                                Log.d(CALL, "重複なし 追い出し処理を行わない");
                            } else {
                                Log.d(CALL, "追い出しされる予約IDは" + checkResult);
                                reserve.eviction(checkResult);
                            }

                            //*** メソッドによる延長insert ***//
                            reserve.endTimeExtention();

                            //*** 延長完了ダイアログの表示 ***//
                            Bundle diaBundle = new Bundle();
                            diaBundle.putString(KEYRESULT, KEYSMALLEX);
                            ResultDialog resultDialog = new ResultDialog();
                            resultDialog.setArguments(diaBundle);
                            resultDialog.show(getFragmentManager(), KEYSMALLEX);
                          }
                        }).setNegativeButton(CANCEL, new DialogInterface.OnClickListener() {
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
        Log.d(CALL, "ReserveConfirmActivity->onCreate()");

//        helper = new DB(getApplicationContext());

        //*** 前画面からの引数を受け取る ***//
        Intent intent = getIntent();
        gamen = intent.getStringExtra("gamen").contains("0") ? "新規" : "一覧"; //*** 0: 新規  1: 一覧　からの画面遷移 ***//


        Log.d(CALL, "画面遷移元　" + gamen);
        //*** 画面遷移元によって、処理を分ける ***//
        if (gamen.contains("新規")) {    //*** 「新規」画面からの画面遷移 ***//
            employee = (Employee) intent.getSerializableExtra("emp");        //*** 社員情報の取得 ***//
            reserve = (Reserve) intent.getSerializableExtra("reserve");     //*** 予約情報のインスタンスを取得 ***//

        } else {                         //*** 「一覧」画面からの画面遷移 ***//
            reserve = (Reserve) intent.getSerializableExtra("reserve");     //*** 予約情報のインスタンスを取得 ***//
            Log.d("call", reserve.toString());
            employee = (Employee) intent.getSerializableExtra("employee");
            Log.d("Emp in Confirm:", employee.toString());

//          btn_confirm = (Button) findViewById(R.id.arconfirm_btn_mem_confirm);    //*** 参加者確認ボタン ***//
//          btn_confirm.setText("戻る");
        }
        instance = this;

//    intent.getIntExtra("gamen", 1);
        reserve.setRe_mem_priority((int) setReserveDetail()); //*** 会議優先度をセットする ***//

        /***
         * レイアウト情報をマッピングする
         */
        super.onCreate(savedInstanceState);
        //*** 管理者認証済みだったら、テーマを変更する ***//
        if (Util.isAuthAdmin(authFlg)) {
            setTheme(R.style.SecondTheme);
        }
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
        if (gamen.contains("一覧")) {
            btn_confirm = (Button) findViewById(R.id.arconfirm_btn_correct);
            btn_confirm.setText("戻る");
        }

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
        Calendar start = Calendar.getInstance();
        //*** 比較用Calenderその２ ***//
        Calendar end = Calendar.getInstance();
        //*** フォーマット用意 ***//
        SimpleDateFormat timeFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM);

//        Bundle diaBundle = new Bundle();
        // idによって処理を分ける
        switch (id) {
            // 「早期退出」が選択された
            case R.id.option_earlyOut:
                try {
                    //*** Calenderにセット ***//
                    start.setTime(timeFormat.parse(reserve.getRe_startDay() + SPACE + reserve.getRe_startTime()));
                    end.setTime(timeFormat.parse(reserve.getRe_endDay() + SPACE + reserve.getRe_endTime()));
                } catch (ParseException e) {
                    e.getStackTrace();
                    break;
                }
                //*** 退出しようとしている会議が現在日付・時刻に矛盾していないか ***//
                //*** 会議の最中であるか、自分が参加している会議かの判定 ***//
                if (cal.after(start) && cal.before(end) && reserve.getRe_member().indexOf(employee.getEmp_id()) != MINUSONE) {
                    //*** 早期退出ダイアログを表示 ***//
                    EarlyOutDialog earlyOutDialog = new EarlyOutDialog();
                    earlyOutDialog.show(getFragmentManager(), KEYOUT);
                } else {
                    Toast.makeText(this, "本番では早期退出禁止", Toast.LENGTH_SHORT).show();
                    //*** 試験的に、ダメでも出来るようにしておく（いずれ削除） ***//
                    EarlyOutDialog earlyOutDialog = new EarlyOutDialog();
                    earlyOutDialog.show(getFragmentManager(), KEYOUT);
                }
                break;
            // 「予約変更」が選択された
            case R.id.option_reserveChange:
                re_id = reserve.getRe_id();
//                String[] startDay = reserve.getRe_startDay().split("/");
                try {
                    //*** 変更しようとしている会議の開始時間をセット ***//
                    start.setTime(timeFormat.parse(reserve.getRe_startDay() + SPACE + reserve.getRe_startTime()));
                    end.setTime(timeFormat.parse(reserve.getRe_endDay() + SPACE + reserve.getRe_endTime()));
                } catch (ParseException e) {
                    e.getStackTrace();
                    break;
                }
                end.add(Calendar.MINUTE, M_THIRTY);
                //*** 変更しようとしている会議が現在日付・時刻に矛盾していないか ***//
                //*** まだ始まっていない会議か、自分の参加する会議もしくは自分が申請した会議かの判定 ***//
                if (cal.before(start) && (reserve.getRe_member().indexOf(employee.getEmp_id()) != MINUSONE || reserve.getRe_applicant().equals(employee.getName()))) {
                    //*** 次画面（ReserveChangeActivity）に予約インスタンスを渡す ***//
                    intent = new Intent(getApplicationContext(), ReserveChangeActivity.class);
                    intent.putExtra(KEYCHANGE, reserve);
                    Log.d(CALL, employee.toString());
                    intent.putExtra("employee", employee);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "本番では変更禁止", Toast.LENGTH_SHORT).show();
                    //*** 試験的に、ダメでも出来るようにしておく（いずれ削除） ***//
                    intent = new Intent(getApplicationContext(), ReserveChangeActivity.class);
                    intent.putExtra(KEYCHANGE, reserve);
                    Log.d(CALL, employee.toString());
                    intent.putExtra("employee", employee);
                    startActivity(intent);
                }
                //                Toast.makeText(this, "予約変更", Toast.LENGTH_SHORT).show();
                break;
            // 「延長」が選択された
            case R.id.option_extention:
                try {
                    //*** 延長を試みる会議の開始終了時刻をセット ***//
                    start.setTime(timeFormat.parse(reserve.getRe_startDay() + SPACE + reserve.getRe_startTime()));
                    end.setTime(timeFormat.parse(reserve.getRe_endDay() + SPACE + reserve.getRe_endTime()));
                } catch (ParseException e) {
                    e.getStackTrace();
                    break;
                }
                //*** 既に延長されているかの確認を行う ***//
                //*** 延長の有無により処理を分ける ***//
                MyHelper helper = new MyHelper(instance.getApplicationContext());
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor cursor = db.rawQuery(SQL_ALREADY_EXTENSION_CHECK, new String[]{reserve.getRe_id()});
                //*** 延長できるのは会議終了の30分前まで ***//
                end.add(Calendar.MINUTE, M_THIRTY);
                //*** 延長しようとしている会議が現在日付・時刻に矛盾していないか ***//
                //*** 会議の最中かどうか、自分が参加している会議かの判定 ***//
                if (cal.after(start) && cal.before(end) && reserve.getRe_member().indexOf(employee.getEmp_id()) != MINUSONE) {
                    if (!cursor.moveToNext()) {
                        //*** 延長ダイアログを表示 ***//
                        ExtensionDialog extensionDialog = new ExtensionDialog();
                        extensionDialog.show(getFragmentManager(), KEYEX);
                    } else {
                        Toast.makeText(this, "既に延長されています", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "本番では延長禁止", Toast.LENGTH_SHORT).show();
                    //*** 試験的に、ダメでも出来るようにしておく（いずれ削除） ***//
                    if (!cursor.moveToNext()) {
                        //*** 延長ダイアログを表示 ***//
                        ExtensionDialog extensionDialog = new ExtensionDialog();
                        extensionDialog.show(getFragmentManager(), KEYEX);
                    } else {
                        Toast.makeText(this, "既に延長されています", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        // 選択された結果（項目）を返す
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
//    db.close();
    }

    //*** SelfMadeMethod ***//
    //*** getApplicationContext用 ***//
    public static ReserveConfirmActivity getInstance() {
        return instance;
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
            //*** 「管理者ログアウト」が選択されたとき ***//
            case R.id.nav_admin_logout:
                AdminLogOut adminLogOut = new AdminLogOut();
                adminLogOut.show(getFragmentManager(), "adminLogOut");

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

    //*** --- SELF MADE METHOD --- 参加者確認ボタン押下時の処理 ***//
    public void onClickMemConfirm(View view) {
        Log.d(CALL, "btn_confirm_member->onClick()");
        //*** 参加者一覧ダイアログを表示する ***//
        MemberConfirmDialog dialog = new MemberConfirmDialog();
        dialog.show(getFragmentManager(), "confirm_a");
    }

    //*** --- SELF MADE METHOD --- 確定ボタン押下時の処理 ***//
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClickKakutei(View view) {
        Log.d(CALL, "call onClickKakutei");

        //*** 新規からの画面遷移でなければ、以降の処理は無効なので、戻る ***//
        if (gamen.contains("一覧")) {
            finish();
            return;
        }
        //*** --------------------------------------------------- ***//


        // TODO: 2017/11/10 管理者認証済みなら、特権的に、何でもできちゃうロジックを実装せよ
        //***   管理者認証されていない --> 時間の重複、優先度のチェックを行う***//
        if (authFlg.contains(NON_AUTH)) {
            //*** 会議の重複をチェックする ***//
            String resultCode = reserve.timeDuplicationCheck(reserve);
            if (resultCode.equals(FALSE)) {
                //*** 重複あり ***//
                Log.d(CALL, "時間の重複が発生！ 優先度も負けたパターン:処理を抜けます");
                Toast.makeText(this, "予約不可能です", Toast.LENGTH_SHORT).show();
                return;
            } else if (resultCode.equals(TRUE)) {
                Log.d(CALL, "(重複なし)で追い出しを行わない");
            } else {
                Log.d(CALL, "追い出し処理検知！追い出された予約情報を通知します");
                Log.d(CALL, "追い出しされる予約IDは" + resultCode);
                notificationEviction(resultCode);
                reserve.eviction(resultCode);
            }

            Log.d(CALL, "予約ID:" + reserve.getRe_id());
            //*** 時間の重複も、優先度チェックも何も必要なし＝＝＞ そのままインサートする ***//
            reserve.setRe_applicant(employee.getEmp_id());
            reserve.reserveCorrenct(setReserveDetail());     //*** 予約テーブル,参加者テーブル へのインサート ***//
            reserve = null;                                  //*** 予約を確定したので、reserveをnullにする ***//

        }
        //*** 管理者認証ずみ ***//
        else {
            //*** 追い出す会議だけ特定して、ヘッドアップ通知を行う ※時間重複も、優先度も関係なし ***//
            String resultCode = reserve.timeDuplicationCheck(reserve);
            Log.d(CALL, resultCode);
            if (!resultCode.equals(TRUE) && !resultCode.equals(FALSE)) {
                notificationEviction(resultCode);            //*** 会議追い出し"通知"を行う ***//
                reserve.eviction(resultCode);                //*** 会議の追い出しを行う ***//
            }
            //*** 会議をインサートする ***//
            reserve.reserveCorrenct(setReserveDetail());
            reserve = null;
        }

        //*** 画面を殺す 結果を、ReserveActivityに返す ***//
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();

    }


    //*** ------------------------ ***//
    //*** --- SELF MADE METHOD --- ***//
    //*** ------------------------ ***//

    //*** --- SELF MADE METHOD --- 各ウィジェットの初期化処理メソッド ***//
    public void init() {
        btn_confirm = (Button) findViewById(R.id.arconfirm_btn_mem_confirm);    //*** 参加者確認ボタン ***//
    }

    //*** --- SELF MADE METHOD --- 予約インスタンスの情報を、DBに書き込める形にまで設定するメソッド ***//
    private float setReserveDetail() {
        //*** 会議目的IDをセットする ***//
        String purName = reserve.getRe_purpose_name();  //***  ***//
        String purId = Util.returnPurposeId(purName);   //*** 会議目的名を ***//
        reserve.setRe_purpose_id(purId);


        //*** 会議参加者の優先度を計算する ***//
        Integer sumPriority = 0;

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

    //*** 追い出し通知を行うメソッド ***//
    private void notificationEviction(String otherReId) {
        Log.d("call", "call ReserveConfirmActivity.notificationEviction()");
        Util.easyLog("追い出し検知！ ステータス通知発行！");

        //*** 通知で表示する追い出し対象の予約情報を取得する ***//
        db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("select * from v_reserve_member where re_id = ?", new String[]{otherReId});
        Reserve r = new Reserve();
        if (c.moveToNext()) {
            //*** 追い出し対象の予約インスタンスを生成 ***//
            r.setRe_id(otherReId);         //*** 予約ID ***//
            r.setRe_name(c.getString(1));           //*** 概要 ***//
            r.setRe_startDay(c.getString(2));       //*** 開始日時 ***//
            r.setRe_endDay(c.getString(3));         //*** 終了日時 ***//
            r.setRe_startTime(c.getString(4));      //*** 開始時刻 ***//
            r.setRe_endTime(c.getString(5));        //*** 終了時刻 ***/
            r.setRe_switch(c.getString(6));         //*** 社内社外区分 ***//
            r.setRe_fixtures(c.getString(7));       //*** 備品 ***//
            r.setRe_remarks(c.getString(8));        //*** 備考 ***//
            r.setRe_room_id(c.getString(10));       //*** 会議室ID ***//
            r.setRe_purpose_name(c.getString(19));  //*** 会議目的名 ***//
        }
        c.close();

        //*** ステータス通知をタップで、どの処理を行うか設定 ***//
        Intent intent = new Intent(Intent.ACTION_VIEW);       //*** 通知押下で遷移するIntent ***//
        intent.setData(Uri.parse("http://www.google.com/"));  //***  ***//

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                intent,
                0
        );
        //*** ヘッドアップ通知 ***//
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentIntent(pendingIntent);                //*** 通知押下で、遷移するIntent指定 ***//
        builder.setSmallIcon(R.drawable.aaa);                   //*** 通知で使用する画像（小） ***//
        builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        builder.setVibrate(new long[]{100, 0, 100, 0, 100, 0}); //*** ヘッドアップ通知にするためのメソッド ***//
        builder.setPriority(Notification.PRIORITY_HIGH);        //*** ヘッドアップ通知にするためのメソッド ***//
        builder.setContentText(String.format("会議ID: %s 開始時刻:%s:%s", r.getRe_id(), r.getRe_startDay(), r.getRe_startTime()));
        builder.setContentTitle("会議キャンセル");

        //*** 通知関連の情報をセット ***//
        Notification notification = builder.build();
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(123, notification);
    }
}