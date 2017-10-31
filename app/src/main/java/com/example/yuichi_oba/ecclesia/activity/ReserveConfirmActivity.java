package com.example.yuichi_oba.ecclesia.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import com.example.yuichi_oba.ecclesia.tools.MyHelper;
import com.example.yuichi_oba.ecclesia.tools.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.EX;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.HH_MM;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.KEYCHANGE;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.KEYEX;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.SPACE;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.YYYY_MM_DD_HH_MM;

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/
// _/_/ 予約の詳細・確認を行うアクティビティ
// _/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// DO: 2017/09/19 延長ダイアログの正常動作の実装
// TODO: 2017/09/19 延長ダイアログのレイアウト調整およびデザインの考察 
public class ReserveConfirmActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  private MyHelper helper = new MyHelper(this);
  private static SQLiteDatabase db;

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

  //*** 早期退出オプション選択時の ダイアログフラグメントクラス ***//
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
//                            SQLiteOpenHelper helper = new DB(instanceApplicationContext());
              MyHelper helper = new MyHelper(instance.getApplicationContext());
              SQLiteDatabase db = helper.getWritableDatabase();
              //*** トランザクション開始 ***//
//                            db.beginTransaction();
              //*** DBへ更新をかけるために用意 ***//
//                            ContentValues con = new ContentValues();
              //*** 現在時刻取得 ***//
              Date ealDate = new Date();
              //*** フォ－マットを用意 ***//
              SimpleDateFormat ealFor = new SimpleDateFormat(HH_MM);
              //*** 現在時刻をフォーマットにかけてStringへ変換 ***//
              String ealTime = ealFor.format(ealDate);
              //*** 早期退出による終了時刻をセット ***//
//                            con.put("re_endTime", ealTime);
              Log.d("ealTIme", ealTime);
              reserve.earlyExit();
              AlertDialog.Builder result = new AlertDialog.Builder(instance.getApplicationContext());
//                            result.setTitle("早期退出完了")
//                                    .setMessage("早期退出が完了しました").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                }
//                            }).create();
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
//                                SQLiteOpenHelper helper = new DB(instance.getApplicationContext());
              MyHelper helper = new MyHelper(instance.getApplicationContext());
              SQLiteDatabase db = helper.getWritableDatabase();
              //*** トランザクション開始 ***//
//                                db.beginTransaction();
              //*** 延長情報をDBへ投げるために用意 ***//
//                                ContentValues con = new ContentValues();
              //*** 延長による終了時刻を計算 ***//
              SimpleDateFormat endFor = new SimpleDateFormat(HH_MM);
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
              //*** コミットみたいな感じ ***//
//                                db.setTransactionSuccessful();
              //*** トランザクション終了 ***//
//                                db.endTransaction();

//                            db.execSQL("insert into t_extension values(?,?,?,?,?)",
//                                    new Object[]{reserve.getRe_id(),
//                                                reserve.getRe_startDay(),
//                                                reserve.getRe_startTime(),
//                                                reserve.getRe_endDay(),
//                                                reserve.getRe_endTime()});

              reserve.endTimeExtention(exTime);

              //*** 延長結果ダイアログを表示 ***//
              ExtentResultDialog extentResultDialog = new ExtentResultDialog();
              extentResultDialog.show(getFragmentManager(), KEYEX);

              AlertDialog.Builder result = new AlertDialog.Builder(instance.getApplicationContext());
              result.setTitle("延長完了")
                  .setMessage("延長が完了しました").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
              }).create();
              db.close();
              helper.close();
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

//        helper = new DB(getApplicationContext());

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

//    intent.getIntExtra("gamen", 1);
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

    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

    //*** 現在時刻取得 ***//
    Calendar cal = Calendar.getInstance();
    //*** 比較用Calender ***//
    Calendar cmp = Calendar.getInstance();
    //*** フォーマット用意 ***//
    SimpleDateFormat timeFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM);
    // idによって処理を分ける
    switch (id) {
      // 「早期退出」が選択された
      case R.id.option_earlyOut:
        try {
          //*** Calenderにセット ***//
          cmp.setTime(timeFormat.parse(reserve.getRe_endDay() + SPACE + reserve.getRe_endTime()));
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
//                    builder.setTitle("早期退出不可能").setMessage("早期退出できる会議ではありません").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {}
//                    }).create().show();
          Toast.makeText(this, "早期退出できる会議ではありません", Toast.LENGTH_SHORT).show();
          //*** 試験的に、ダメでも出来るようにしておく（いずれ削除） ***//
//                    EarlyOutDialog earlyOutDialog = new EarlyOutDialog();
//                    earlyOutDialog.show(getFragmentManager(), "out");
//                    builder.setTitle("早期退出完了")
//                            .setMessage("早期退出が完了しました").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                }
//                            }).create().show();
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
//                    intent.putExtra(KEYCHANGE, re_id);
          startActivity(intent);
        } else {
//                    builder.setTitle("変更不可能").setMessage("変更できる会議ではありません").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {}
//                    }).create().show();
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
//                    builder.setTitle("延長不可能").setMessage("延長できる会議ではありません").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {}
//                    }).create().show();
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

  @Override
  protected void onDestroy() {
    super.onDestroy();
    helper.close();
    db.close();
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
  @RequiresApi(api = Build.VERSION_CODES.N)
  public void onClickKakutei(View view) {
    Log.d("call", "call onClickKakutei");

    //*** 申請者の氏名－＞ 社員IDに変換して、予約インスタンスにセットする ***//
    reserve.setRe_applicant(Util.returnEmpId(reserve.getRe_applicant()));


    //***  ***//
    float priorityAverage = setReserveDetail();         //***  ***//

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

    //*** 予約テーブルへのインサート ***//
    db = helper.getWritableDatabase();
    db.beginTransaction();
    try {
      try (SQLiteStatement st = db.compileStatement("insert into t_reserve values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
        st.bindString(1, reserve.getRe_id());
        st.bindString(2, reserve.getRe_name());
        st.bindString(3, reserve.getRe_startDay());
        st.bindString(4, reserve.getRe_endDay());
        st.bindString(5, reserve.getRe_startTime());
        st.bindString(6, reserve.getRe_endTime());
        st.bindString(7, reserve.getRe_switch());
        st.bindString(8, reserve.getRe_fixtures());
        st.bindString(9, reserve.getRe_remarks());
        st.bindString(10, String.valueOf(priorityAverage));
        st.bindString(11, "company_name");
        st.bindString(12, employee.getEmp_id());
        st.bindString(13, reserve.getRe_room_id());
        st.bindString(14, reserve.getRe_purpose_id());
//                    st.bindString(14, "0001");

        st.bindString(15, reserve.getRe_applicant());
        st.executeInsert();
      }
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }

    //*** 参加者テーブルへのインサート ***//
    insertMemberTable(reserve.getRe_id(), reserve.getRe_member());

    //*** 画面を殺す ***//
    finish();
//        db = helper.getWritableDatabase();                      //***  ***//
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


  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  private void insertMemberTable(String re_id, List<Person> members) {
    db = helper.getWritableDatabase();
    db.beginTransaction();
    SQLiteStatement st = db.compileStatement("insert into t_member values (?, ?)");
    for (Person m : members) {
      st.bindString(1, re_id);
      String mem_id = null;
      //***  ***//
      if (m instanceof Employee) {
        mem_id = ((Employee) m).getEmp_id();
      }
      //***  ***//
      else if (m instanceof OutEmployee) {
        mem_id = ((OutEmployee) m).getOut_id();
      }
      st.bindString(2, mem_id);
      st.executeInsert();
    }
    db.setTransactionSuccessful();
    db.endTransaction();
  }

  //*** --- SELF MADE METHOD --- 予約インスタンスの情報を、DBに書き込める形にまで設定するメソッド ***//
  private float setReserveDetail() {
    //*** 会議目的IDをセットする ***//
    String purName = reserve.getRe_purpose_name();  //***  ***//
    String purId = Util.returnPurposeId(purName);   //*** 会議目的名を ***//
    reserve.setRe_purpose_id(purId);


    //*** 会議参加者の優先度を計算する ***//
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
