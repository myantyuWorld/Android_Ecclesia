package com.example.yuichi_oba.ecclesia.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.yuichi_oba.ecclesia.dialog.AdminLogOut;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.Employee;
import com.example.yuichi_oba.ecclesia.model.Reserve;
import com.example.yuichi_oba.ecclesia.tools.AlarmReceiver;
import com.example.yuichi_oba.ecclesia.tools.MyHelper;
import com.example.yuichi_oba.ecclesia.tools.Util;
import com.example.yuichi_oba.ecclesia.view.TimeTableView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.*;

//import static com.example.yuichi_oba.ecclesia.activity.MyDialog.employee;

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/
// _/_/ 予約状況(リストで視覚的にわかりやすい）を表示するアクティビティ
// _/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// TODO: 2017/09/19  長押し対応は無理か？ 一覧での、タップは反応するが、長押しには反応しない・・・
//*** オブジェクト渡しのはいし OK ***//
// TODO: 2017/11/13 新規登録した会議をタップすると、purName がnullで落ちる
// TODO: 2017/11/13 赤は、自分、白は他人を説明するようなCanvasの追加？わかりにくい
// TODO: 2017/11/13 会議優先度がないと、わかりにくい
public class ReserveListActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  public static final String Q_SELECT_TODAY_CONFERENCE = "select * from t_reserve where emp_id = ? and re_startday = ?";
  @SuppressLint("StaticFieldLeak")
  static TextView arl_txt_date;
  static TimeTableView arl_view_timetableView;
  public static Employee employee;
  private int thCnt = 0;
  public static ReserveListActivity instance = null;
  //    private DB helper;
  private MyHelper helper;
  public static SQLiteDatabase db;
  public static String authFlg = "0";      //*** 管理者認証フラグ ***//

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
      Log.d(CALL, "getTerminalImei()");
      TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//        return manager.getDeviceId();
      // TODO: 2017/09/15 ハードコーディング!
//            this.setImeiNumber("352272080218786");   // 大馬 の 端末ＩＭＥＩ
      this.setImeiNumber("0");   // 大馬 の 端末ＩＭＥＩ
    }

    //*** 社員を認証するメソッド ***//
    public String authEmployee() {
      Log.d(CALL, "ReserveListActivity->authEmployee()");
      String emp_id = "";
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
      Log.d(CALL, TAG + "->getEmployeeInfo()");
      String emp_id = authEmployee();
      if (!emp_id.isEmpty()) {
        // 社員ＩＤが空またはＮＵＬＬでなければ次のロジックを実行する
        Cursor c = db.rawQuery("select * from v_employee where emp_id = ?",
            new String[]{emp_id});
        if (c.moveToNext()) {
          // 社員ＩＤから社員情報を検索して、設定する
//                    Employee e = new Employee();
//                    e.setId(emp_id);
//                    e.setName(c.getString(EMP_NAME));
//                    e.setTel(c.getString(EMP_TEL));
//                    e.setMailaddr(c.getString(EMP_MAIL_ADDR));
//                    e.setDep_name(c.getString(DEP_NAME));
//                    e.setPos_name(c.getString(POS_NAME));
//                    e.setPos_priority(c.getString(POS_PRIORITY));

          //*** 「社員」クラスのインスタンスを生成 ***//
          Employee e = new Employee(
              emp_id,                     //*** 社員ID ***//
              c.getString(EMP_NAME),      //*** 氏名 ***//
              c.getString(EMP_TEL),       //*** 電話番号 ***//
              c.getString(EMP_MAIL_ADDR), //*** メールアドレス ***//
              c.getString(4),             //*** 部署ID ***//
              c.getString(6)              //*** 役職ID ***//
          );

          return e;   //*** インスタンスを返す ***//
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
              arl_txt_date.setText(String.format("%04d/%02d/%02d", year, month + 1, day));
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


//    public static List<Reserve> reserveInfo;    // 予約情報記録クラスの変数   非同期エラーが起きるため使用禁止する！


  @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    instance = this;
    Util.easyLog("ReserveListActivity->onCreate() 予約一覧画面");
    //*** DB関連 ***//
//        helper = new DB(getApplicationContext());
    helper = new MyHelper(this);
    db = helper.getWritableDatabase();

    /*** 各ウィジェットの初期化処理 && 社員情報の取得 ***/
    init();


    super.onCreate(savedInstanceState);
    //*** 管理者認証済みだったら、テーマを変更する ***//
    if (Util.isAuthAdmin(authFlg)) {
      setTheme(R.style.SecondTheme);
    }
    setContentView(R.layout.activity_main);
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
    arl_txt_date = (TextView) findViewById(R.id.arl_txt_date);
    final Calendar c = Calendar.getInstance();
    arl_txt_date.setText(String.format(Locale.JAPAN, "%04d/%02d/%02d", c.get(Calendar.YEAR) + 1, 1, 17));

    //*** コンテキストメニューの発生元ビューを取得 ***//
    registerForContextMenu(findViewById(R.id.content_main));

    /*** ここまで ***/


    // 予約情報の設定
//        getReserveInfo();
//        for (ReserveInfo r : reserveInfo) {
//            Log.d(TAG, r.getRe_id() + " : " + r.getRe_startTime() + "(" + r.getRe_endTime() + ") room_id : " + r.getRe_roomId());
//        }
    /*** 社員ID と アプリ起動時の日付を渡して、描画する ***/
    arl_view_timetableView = (TimeTableView) this.findViewById(R.id.arl_view_timetable);
    Log.d(CALL, employee.getEmp_id());
    Log.d(CALL, arl_txt_date.getText().toString());
    arl_view_timetableView.reView(employee.getEmp_id(), arl_txt_date.getText().toString());

    arl_txt_date.setOnClickListener(view -> {
      Log.d(TAG, "arl_txt_date click!");
      MyDialog d = new MyDialog();
      d.show(getFragmentManager(), "dateDialog");
    });

    /*** 予約情報リストの同期エラーがでるため、コメアウトします ***/
//        Button btPrev = (Button) findViewById(R.id.bt_prev);
//        btPrev.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(ReserveListActivity.this, "Prev", Toast.LENGTH_SHORT).show();
//                getReserveInfo();
//                arl_view_timetableView.reView(reserveInfo);
//            }
//        });
//        Button btNext = (Button) findViewById(R.id.bt_next);
//        btNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(ReserveListActivity.this, "Next", Toast.LENGTH_SHORT).show();
//                getReserveInfo();
//                arl_view_timetableView.reView(reserveInfo);
//            }
//        });
    Button arl_btn_search = (Button) findViewById(R.id.arl_btn_search);
    arl_btn_search.setOnClickListener(v -> {
      Toast.makeText(ReserveListActivity.this, arl_txt_date.getText().toString(), Toast.LENGTH_SHORT).show();
//         DO: 2017/10/04 自分の予約情報をもっているリストを一回クリアしないと、前の情報も描画されてしまう
      arl_view_timetableView.reView(employee.getEmp_id(), arl_txt_date.getText().toString());
    });

    /***
     *
     * 実験用：ステータス通知を出す隠し機能
     * は、OK
     * 次は、ボタン押下で、AlarmManagerで指定した日時にNotificationを送る実験
     *

     ***/
    arl_btn_search.setOnLongClickListener(v -> {
      Log.d(CALL, "call ReserveListActivity.onLongClick()");
//        Toast.makeText(ReserveListActivity.this, "aaaaaaaaa", Toast.LENGTH_SHORT).show();
//
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("http://www.google.com/"));
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(
//            ReserveListActivity.getInstance().getApplicationContext(),
//            0,
//            intent,
//            0
//        );
//
//        Notification notification = new Notification.Builder(ReserveListActivity.getInstance().getApplicationContext())
//            .setContentTitle("タイトル！")
//            .setContentText("お知らせぜよ～～")
//            .addAction(R.drawable.aaa, "決まりて：押し出し", pendingIntent)
//            .setContentIntent(pendingIntent)
//            .setSmallIcon(R.drawable.aaa)
//            .setAutoCancel(true)
//            .build();
//
//        NotificationManager nm = (NotificationManager)
//            getSystemService(Context.NOTIFICATION_SERVICE);
//
//        nm.notify(1000, notification);

      //*** AlarmManagerで指定した日時にNotificationを送る実験 ***//
      //*** 作品展の実演用に残しておく ***//
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(System.currentTimeMillis());
      calendar.add(Calendar.SECOND, 1);

      Intent notificationIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
      notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, 1);
      notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_CONTENT, "会議開始10分前となりました");
      PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

      AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
      alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
      return false;
    });


  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    db.close();
    helper.close();

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
        intent.putExtra("employee", employee);
        startActivity(intent);
        break;
      // 「管理者認証」が選択されたとき
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
    return true;
  }

  //*** 画面が表示・再表示されたらコールされる (画面遷移はここ！)***//
  @Override
  protected void onResume() {
    Util.easyLog("call ReserveListActivity->onResume()");
    super.onResume();
    arl_view_timetableView.reView(employee.getEmp_id(), arl_txt_date.getText().toString());
    arl_view_timetableView.thread_flg = true;

    Thread thread = new Thread(() -> {
      if (thCnt != 0) {
        arl_view_timetableView.thread_flg = true;
        arl_view_timetableView.x = 0;
        arl_view_timetableView.y = 0;
        thCnt = 0;
      }
      Log.d(CALL, "Thread");
      String[] info = arl_view_timetableView.getSelectedReserve();
      Log.d(CALL, "info :: " + Arrays.toString(info));
      //*** 新規予約登録画面への遷移 ***//
      if (info[0].equals(NONE)) {
        Log.d(CALL, "新規予約登録画面への遷移");
        Intent intent = new Intent(getApplicationContext(), ReserveActivity.class);
        //*** オブジェクト渡しがエラーのため、コメアウト ***//
        //                    intent.putExtra("emp", employee);                           //*** 社員インスタンスをインテント渡し ***//
        intent.putExtra("emp_id", employee.getEmp_id());
        intent.putExtra("date", arl_txt_date.getText().toString()); //*** 選択されている日付をインテント渡し ***//
        intent.putExtra("roomId", info[1]);                         //*** 会議室IDを渡す ***//

        startActivity(intent);  //*** 新規予約登録画面 ***//
      } else {
        Log.d(CALL, "予約確認画面への遷移");
        Reserve reserve = Reserve.retReserveConfirm(info[0]); //*** 特定した予約IDを基に、予約情報を検索 ***//
        Log.d(CALL, reserve.toString());

        //                    intent.putExtra("emp", employee); //*** 不要？ ***//
        Intent intent = new Intent(getApplicationContext(), ReserveConfirmActivity.class);
        intent.putExtra("gamen", "1");          //*** どの画面からの遷移か ***//
        intent.putExtra("reserve", reserve);    //*** 予約情報のインスタンス ***//
        intent.putExtra("employee", employee);

        startActivity(intent);  //*** 予約確認画面への画面遷移 ***//
      }
      thCnt++;
      try {
        Thread.sleep(20);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    thread.start();
    //*** アプリを立ち上げた日付の、会議をAlarmManagerに登録する ***//
    Log.d(CALL, "---------------------------------------------------------");
    Log.d(CALL, String.format("アプリを立ち上げた[%s] の日付の会議をAlarmManagerに登録していくよー", arl_txt_date.getText().toString()));
    //*** onResume()は何度も呼ばれるので、最終的には、新規で、追加されたアプリ起動時の日付の会議”だけ”を ***//
    //*** AlarmManager に  追加するロジックにしよう！***//
    //*** AlarmManager登録メソッド ***//
    registAlarmManager(arl_txt_date.getText().toString());
    Log.d(CALL, "---------------------------------------------------------");
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
      Log.d(CALL, employee.toString());
    }
  }

  //*** この画面のインスタンスを返すメソッド（非アクティビティクラスで、DB検索する際に使用する） ***//
  public static ReserveListActivity getInstance() {
    return instance;
  }

  //*** アプリ起動時の会議を、AlarmManagerに登録するメソッド ***//
  private void registAlarmManager(String today) {
    //*** アプリ起動時の会議の件数、予約情報を取得する ***//
    List<Reserve> reserves = new ArrayList<>();
    Cursor c = db.rawQuery(Q_SELECT_TODAY_CONFERENCE, new String[]{employee.getEmp_id(), today});
    while (c.moveToNext()) {
      Reserve r = new Reserve();
      r.setRe_id(c.getString(0));                                     //*** 予約ID ***//
      r.setRe_name(c.getString(1));                                   //*** 概要 ***//
      r.setRe_startDay(c.getString(2));                               //*** 開始日時 ***//
      r.setRe_endDay(c.getString(3));                                 //*** 終了日時 ***//
      r.setRe_startTime(c.getString(4));                              //*** 開始時刻 ***//
      r.setRe_endTime(c.getString(5));                                //*** 終了時刻 ***//
      r.setRe_switch(c.getString(6).contains("0") ? "社内" : "社外");   //*** 社内社外区分 ***//
      r.setRe_room_id(c.getString(12));                               //*** 会議室ID ***//
//      r.setRe_room_name(Util.returnRoomName(c.getString(12)));        //*** 会議室名 ***//
      reserves.add(r);  //*** 会議インスタンス追加 ***//
    }
    c.close();

    //*** 取得した会議リスト全件ループ ***//
//    reserves.forEach(r -> {
//      //*** AlarmManagerの登録 ***//
//
//    });


  }

  public void onReturnValue(String authFlg) {
    this.authFlg = authFlg;
  }

}

