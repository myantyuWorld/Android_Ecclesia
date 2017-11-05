package com.example.yuichi_oba.ecclesia.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.Employee;
import com.example.yuichi_oba.ecclesia.model.OutEmployee;
import com.example.yuichi_oba.ecclesia.model.Person;
import com.example.yuichi_oba.ecclesia.model.Reserve;
import com.example.yuichi_oba.ecclesia.tools.MyHelper;
import com.example.yuichi_oba.ecclesia.tools.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import android.app.Dialog;

//import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/
// _/_/ 予約を行うアクティビティ
// _/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
public class ReserveActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  public static final String REGEX = "：";
  public static final String REGEX_1 = "/";
  //*** Filed ***//
  private Employee employee;
  private Map<String, String> mapPurpose; //*** 会議目的の 会議目的ID ： 会議目的名 をもつMap ***//
  private Map<String, String> mapRoom;    //*** 会議室の 会議室ID ： 会議室名 をもつMap ***//
  //*** Widget ***//
  private EditText edOverView;        //*** 概要エディットテキスト ***//
  private EditText edFixture;         //*** 備品エディットテキスト ***//
  private EditText edRemark;          //*** 備考エディットテキスト ***//
  private static Button btStartDay;   //*** 開始日時ボタン ***//
  private static Button btEndDay;     //*** 終了日時ボタン ***//
  private static Button btStartTime;  //*** 開始時刻ボタン ***//
  private static Button btEndTime;    //*** 終了時刻ボタン ***//
  private TextView txtApplicant;      //*** 申請者 ***//
  private Switch swSwitch;            //*** 社内/社外区分 ***//
  private Spinner ar_sp_room;         //*** 会議室スピナー ***//
  private Spinner sp_purpose;         //*** 目的スピナー ***//
  private Spinner sp_member;          //*** 参加者スピナー ***//
  private Button btReConfirm;         //*** 内容確認ボタン ***//

  private boolean switchFlg;          //*** 社内社外区分 ***//
  //    private MyHelper helper = new MyHelper(this);
  public static SQLiteDatabase db;

  //    private ReserveInfo reserveInfo;
//    public static List<Employee> member = new ArrayList<>();
  //*** 社員・社外者の参加者を持つための、ポリモーフィズム使用のための、スーパクラスのリスト ***//
  public static List<Person> member = new ArrayList<>();


  //*** 日付ダイアログ ***//
  public static class MyDateDialog extends DialogFragment {

    public static final String FORMAT_DATE = "%04d/%02d/%02d";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      final Calendar cal = Calendar.getInstance();
      return new DatePickerDialog(
          getActivity(),
          new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
              String date = getArguments().getString("date");
              Log.d("call", date);
              if (date.contains("sDay")) {
                btStartDay.setText(String.format(FORMAT_DATE, year, month + 1, day));
                btEndDay.setText(String.format(FORMAT_DATE, year, month + 1, day));    //*** 終了日時を開始日時でせっていする ***//
              } else {
                btEndDay.setText(String.format(FORMAT_DATE, year, month + 1, day));
              }
            }
          },
          cal.get(Calendar.YEAR),
          cal.get(Calendar.MONTH),
          cal.get(Calendar.DAY_OF_MONTH)
      );
    }
  }

  //*** 時刻ダイアログ ***//
  public static class MyTimeDialog extends DialogFragment {

    public static final String FORMAT_TIME = "%02d：%02d";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      final Calendar cal = Calendar.getInstance();
      return new TimePickerDialog(
          getActivity(),
          new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
              String time = getArguments().getString("time");
              Log.d("call", time);
              if (time.contains("sTime")) {
                btStartTime.setText(String.format(FORMAT_TIME, hourOfDay, minute));
                btEndTime.setText(String.format(FORMAT_TIME, hourOfDay + 1, minute)); //*** 終了日時を開始日時の60分後（暫定）で設定する ***//
              } else {
                btEndTime.setText(String.format(FORMAT_TIME, hourOfDay, minute));
              }
            }
          },
          cal.get(Calendar.HOUR_OF_DAY),
          cal.get(Calendar.MINUTE),
          true
      );
    }
  }

  //*** 予約アクティビティで使用する警告用ダイアログ ***//
  public static class CautionDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      //*** Bundleから値を取り出し、エラー種別を受け取る ***//
      String errorMsg = getArguments().getString("error");

      String message = "";
      switch (errorMsg) {
        case "no_select":
          message = "開始・終了が選択されていません！";
          break;
        case "date":
          message = "開始日時が終了日時より大きい";
          break;
        case "day":
          message = "開始時刻が終了時刻より大きい";
          break;
        case "brank":
          message = "会議概要が空欄です!";
          break;
        case "room":
          message = "参加者が、会議室最大人数より大きい";
          break;
        case "zero":
          message = "参加者が選択されていません！";
          break;

      }
      //*** ダイアログのインスタンスを生成し、返す ***//
      return new AlertDialog.Builder(getActivity())
          .setTitle("警告")
          .setMessage(message)
          .setPositiveButton("OK", null)
          .create();
    }
  }

  //*** onCreate ***//
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Util.easyLog("ReserveActivity->onCreate() 予約画面");

    //*** 前画面からのオブジェクトをもらう（Employeeクラスのインスタンス） ***//
    Intent intent = getIntent();
//        employee = (Employee) intent.getSerializableExtra("emp");   //*** 社員インスタンス ***//
    String emp_id = intent.getStringExtra("emp_id");
    MyHelper helper = new MyHelper(this);
    db = helper.getWritableDatabase();
    Cursor c = db.rawQuery("select * from v_employee where emp_id = ?", new String[]{emp_id});
    if (c.moveToNext()) {
      employee = new Employee(
          emp_id,                     //*** 社員ID ***//
          c.getString(1),      //*** 氏名 ***//
          c.getString(2),       //*** 電話番号 ***//
          c.getString(3), //*** メールアドレス ***//
          c.getString(4),             //*** 部署ID ***//
          c.getString(6)              //*** 役職ID ***//
      );
    }
    c.close();

    String date = intent.getStringExtra("date");                //*** 日付 ***//
    String roomId = intent.getStringExtra("roomId");            //*** 会議室ID ***//


    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reserve);
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
     * フローティングボタン押下時の処理
     *
     * 会議参加者選択画面に遷移する
     */
    FloatingActionButton fbn = (FloatingActionButton) findViewById(R.id.fbn_addMember);
    fbn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getApplication(), AddMemberActivity.class);
        intent.putExtra("emp_id", employee.getEmp_id());
//                startActivity(intent);
        startActivityForResult(intent, 1);  // 結果をもらう画面遷移を行う
      }
    });

    //*** 各ウィジェットの初期化処理（日付、会議室） ***//
    init(date, roomId);

    //*** 参加者をからにする ***//
    Log.d("call", "参加者を殻にします");
    member.clear();

  }

  //*** 開いたアクティビティ(AddMemberActivity)から何かしらの情報を受け取る ***//
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.d("call", "call ReserveActivity->onActivityResult()");
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      //*** --------------------------***//
      //*** AddMemberActivityからの結果 ***//
      //*** --------------------------***//
      case (1):

        //*** OKボタン押下で、戻ってきたときの処理 ***//
        if (resultCode == RESULT_OK) {
          Object o = data.getSerializableExtra("member");
          if (o instanceof Employee) {    //*** インスタンスが、Employeeクラスのインスタンス ***//
            Employee e = (Employee) o;
            //*** AddMemberActivity->405行目くらいで、その処理があります ***//
            Log.d("call", String.format("社内参加者の役職優先度 : %s", e.getPos_priority()));
            member.add(e);
          } else {                        //*** インスタンスが、OutEmployeeクラスのインスタンス ***//
            OutEmployee e = (OutEmployee) o;
            Log.d("call", String.format("社外参加者 : %s", e.toString()));
            member.add(e);
          }

          //*** 参加者を追加する ***//
          final List<String> list = new ArrayList<>();
          member.forEach(p -> {
            if (p instanceof Employee) list.add("社内 : " + p.getName());
            else                        Log.d("call", "-----社外者参加者");
          });
          //*** 参加者スピナーに反映する ***//
          ArrayAdapter<String> adapter_member = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list);
          sp_member.setAdapter(adapter_member);
        }
        break;
      //*** --------------------------------***//
      //*** ReserveConfirmActivityからの結果 ***//
      //*** --------------------------------***//
      case (2):
        if (resultCode == RESULT_OK) {
          finish(); //*** 予約画面、殺す ***//
        }

      default:
        break;
    }
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

  //*** ナビを選択したときの処理 ***//
  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    Intent intent = null;
    switch (id) {
      case R.id.nav_reserve_list:
        intent = new Intent(getApplicationContext(), ReserveListActivity.class);
        break;
      case R.id.nav_rireki:
        intent = new Intent(getApplicationContext(), HistorySearchActivity.class);
        break;
      case R.id.nav_admin_auth:
        AuthDialog authDialog = new AuthDialog();
        authDialog.show(getFragmentManager(), "aaa");
        break;

    }
    if (intent != null) {
      startActivity(intent);
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  /***
   * _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
   * <p>
   * 自作メソッド
   * <p>
   * _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
   *
   * @param date
   * @param roomId
   */
  //*** --- SELF MADE METHOD --- 各ウィジェットの初期化処理メソッド ***//
  private void init(String date, String roomId) {
    mapPurpose = new HashMap<>();
    mapRoom = new HashMap<>();

    edOverView = (EditText) findViewById(R.id.ar_etxt_overview);        //***  ***//
    btStartDay = (Button) findViewById(R.id.ar_btn_start_day);          //***  ***//
    btStartTime = (Button) findViewById(R.id.ar_btn_re_start_time);     //***  ***//
    btEndDay = (Button) findViewById(R.id.ar_btn_end_day);              //***  ***//
    btEndTime = (Button) findViewById(R.id.ar_btn_re_end_time);         //***  ***//
    txtApplicant = (TextView) findViewById(R.id.ar_txt_applicant);      //***  ***//
    swSwitch = (Switch) findViewById(R.id.ar_sw_switch);                //***  ***//
    sp_member = (Spinner) findViewById(R.id.ar_sp_member);              //***  ***//
    sp_purpose = (Spinner) findViewById(R.id.ar_sp_purpose);            //***  ***//
    ar_sp_room = (Spinner) findViewById(R.id.ar_sp_room);               //***  ***//
    edFixture = (EditText) findViewById(R.id.ar_etxt_fixture);          //***  ***//
    edRemark = (EditText) findViewById(R.id.ar_etxt_remark);            //***  ***//
    btReConfirm = (Button) findViewById(R.id.ar_btn_confirm);           //***  ***//

    btStartDay.setText(date);   //*** 開始日時の設定 ***//
    btEndDay.setText(date);     //*** 終了時刻の設定 ***//
//        reserveInfo = new ReserveInfo();
    //*** 申請者の設定 ***//
    TextView txtApplicant = (TextView) findViewById(R.id.ar_txt_applicant);
    txtApplicant.setText(employee.getName());

    //*** 参加者スピナー ***//
    final List<String> list = new ArrayList<>();
//        for (Employee employee : member) {
//            list.add(employee.getCom_name() + " : " + employee.getName());
//        }
    ArrayAdapter<String> adapter_member = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list);
    sp_member.setAdapter(adapter_member);

    //*** 会議目的スピナー ***//
    MyHelper helper = new MyHelper(this);
    SQLiteDatabase db = helper.getReadableDatabase();
    List<String> purpose = new ArrayList<>();
    Cursor c = db.rawQuery("select * from m_purpose", null);
    while (c.moveToNext()) {
      //*** mapPurpose に記録する ***//
      mapPurpose.put(c.getString(0), c.getString(1));
//            purpose.add(c.getString(0) + ":" + c.getString(1));
      purpose.add(c.getString(1));
    }
    c.close();
    ArrayAdapter<String> adapter_purpose = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, purpose);
    sp_purpose.setAdapter(adapter_purpose);

    //*** 会議室スピナー ***//
    c = db.rawQuery("select * from m_room", null);
    List<String> listRoom = new ArrayList<>();
    while (c.moveToNext()) {
      //*** mapRoom に記録する ***//
      mapRoom.put(c.getString(1), c.getString(2));   //*** Mapで、会議室名 と 最大人数を保持する ***//
      listRoom.add(c.getString(1));
    }
    c.close();
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listRoom);
    ar_sp_room.setAdapter(adapter);
    //*** 引数の会議室IDに該当する会議室名でスピナーをセットする ***//
    ar_sp_room.setSelection(Util.setSelection(ar_sp_room, Util.returnRoomName(roomId)));


    //***  ***//
    setWidgetListener();
  }

  //*** --- SELF MADE METHOD --- 各種ウィジェットのリスナーを登録するメソッド ***//
  private void setWidgetListener() {
    //***  ***//
    sp_member.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
    //***  ***//
    sp_purpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
    //***  ***//
    ar_sp_room.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
    //***  ***//
    btStartDay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MyDateDialog myDateDialog = new MyDateDialog();
        Bundle args = new Bundle();
        args.putString("date", "sDay");
        myDateDialog.setArguments(args);
        myDateDialog.show(getFragmentManager(), "sDay");
      }
    });
    //***  ***//
    btStartTime.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MyTimeDialog myTimeDialog = new MyTimeDialog();
        Bundle args = new Bundle();
        args.putString("time", "sTime");
        myTimeDialog.setArguments(args);

        myTimeDialog.show(getFragmentManager(), "sTime");
      }
    });
    //***  ***//
    btEndDay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MyDateDialog myDateDialog = new MyDateDialog();
        Bundle args = new Bundle();
        args.putString("date", "eDay");
        myDateDialog.setArguments(args);

        myDateDialog.show(getFragmentManager(), "eDay");
      }
    });
    //***  ***//
    btEndTime.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MyTimeDialog myTimeDialog = new MyTimeDialog();
        Bundle args = new Bundle();
        args.putString("time", "eTime");
        myTimeDialog.setArguments(args);

        myTimeDialog.show(getFragmentManager(), "eTime");
      }
    });
    //*** 社内/社外区分 スイッチの値の取得 ***//
    swSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {    //*** チェックされている ーー＞ 社内 ***//
          switchFlg = true;   //*** 社内 ***//
        } else {            //*** チェックされていないーー＞ 社外 ***//
          switchFlg = false;  //*** 社外 ***//
        }
      }
    });
  }

  //*** --- SELF MADE METHOD --- 内容確認ボタン押下時の処理 ***//
  public void onClickReConfirm(View view) {
    Log.d("call", "call onClickReConfirm()");

    //*** 参加者の人数が、会議室の最大人数以下か 0人ではないかどうかチェックする ***//
    //*** エディットテキストに空欄があるかチェック ***//
    //*** 開始終了日時・時刻に矛盾がないかチェック ***//
    if (!checkMemberCount() || !isBrankSpace() || !checkStartEnd()) {
      Log.d("call", "予約アクティビティ エラー検出！ 画面遷移不可能");
      return;     //*** 処理を抜ける ***//
    }
    Log.d("call", edOverView.getText().toString());
    //*** 入力されている情報で、予約情報インスタンスを作る ***//
    Reserve reserve = new Reserve();
    reserve.setRe_id(Util.returnMaxReserveId());                        //*** 予約ID ***//
    reserve.setRe_name(edOverView.getText().toString());                //*** 概要 ***//
    reserve.setRe_purpose_name((String) sp_purpose.getSelectedItem());  //*** 会議目的名 ***//
    reserve.setRe_startDay(btStartDay.getText().toString());            //*** 開始日時 ***//
    reserve.setRe_endDay(btEndDay.getText().toString());                //*** 終了日時 ***//
    reserve.setRe_startTime(btStartTime.getText().toString());          //*** 開始時刻 ***//
    reserve.setRe_endTime(btEndTime.getText().toString());              //*** 終了時刻 ***//
    reserve.setRe_applicant(txtApplicant.getText().toString());         //*** 申請者 ***//
    reserve.setRe_member(member);                                       //*** 会議参加者のリスト ***//
    reserve.setRe_switch(switchFlg ? "0" : "1");                        //*** true(社内) : false(社外) ***//
    reserve.setRe_company("");  //*** 現状これで対処 ***//// TODO: 2017/10/03 会社名をどうするべきか考察
    //*** 選択されている会議室名を取得 ***//
    reserve.setRe_room_id(Util.returnRoomId((String) ar_sp_room.getSelectedItem()));   //*** 会議室ＩＤ ***//
    reserve.setRe_room_name((String) ar_sp_room.getSelectedItem());     //*** 会議室名 ***//
    reserve.setRe_fixtures(edFixture.getText().toString());             //*** 備品 ***//
    reserve.setRe_remarks(edRemark.getText().toString());               //*** 備考 ***//

    //*** エラー未検出ならば画面遷移処理を行う ***//
    Log.d("call", "画面遷移開始");
    Intent intent = new Intent(getApplicationContext(), ReserveConfirmActivity.class);
    intent.putExtra("gamen", "0");          //*** 予約確認画面への、「新規」予約での画面遷移 ***//
    intent.putExtra("reserve", reserve);    //*** 予約情報のインスタンス ***//
    intent.putExtra("emp", employee);

    //*** 予約確認画面への遷移、結果も取得する ***//
    startActivityForResult(intent, 2);


//    startActivity(intent);  //*** 予約確認画面への画面遷移 ***//
  }

  //*** --- SELF MADE METHOD --- 参加者の人数が、会議室の最大人数以下かどうかチェックする ***//
  private boolean checkMemberCount() {
    Log.d("call", "call checkMemberCount()");

    Integer memberCount = sp_member.getAdapter().getCount();    //*** 参加者スピナーの長さを取得する ***//
    String ss = (String) ar_sp_room.getSelectedItem();             //*** 選択されている会議室名を取得 ***//
    if (memberCount == 0) {     //*** 参加者が0人 ***//
      Bundle bundle = new Bundle();
      bundle.putString("error", "zero");

      CautionDialog dialog = new CautionDialog();
      dialog.setArguments(bundle);
      dialog.show(getFragmentManager(), "zero");

      return false;   //*** 異常を返す ***//
    }

    //*** 参加者が、選択中の会議室最大人数より大きい-> 異常 ***//
    if (memberCount > Integer.valueOf(mapRoom.get(ss))) {
      Bundle bundle = new Bundle();
      bundle.putString("error", "room");

      CautionDialog dialog = new CautionDialog();
      dialog.setArguments(bundle);
      dialog.show(getFragmentManager(), "room");
      return false;   //*** 異常を返す ***//
    }
    return true;        //*** 正常を返す ***//
  }

  //*** --- SELF MADE METHOD --- 開始終了日時・時刻に矛盾がないかチェックするメソッド ***//
  private boolean checkStartEnd() {
    Log.d("call", "call checkStartEnd()");

    Bundle bundle = new Bundle();
    //*** ”開始日時”など、未選択の状態かチェックする ***//
    if (btStartDay.getText().toString().contains("開始日時") ||
        btEndDay.getText().toString().contains("終了日時") ||
        btStartTime.getText().toString().contains("開始時刻") ||
        btEndTime.getText().toString().contains("終了時刻")) {
      //*** ダイアログに、どのエラー種別か渡す ***//
      bundle.putString("error", "no_select");
      //*** 警告ダイアログの出力 ***//
      CautionDialog dialog = new CautionDialog();
      dialog.setArguments(bundle);
      dialog.show(getFragmentManager(), "no_select");

      return false;   //*** 異常 を返す ***//
    }
    //*** 開始が終了より遅いかなどの矛盾をチェックする ***//
    String sDay = btStartDay.getText().toString().split(REGEX_1)[1] + btStartDay.getText().toString().split(REGEX_1)[2];
    String eDay = btEndDay.getText().toString().split(REGEX_1)[1] + btEndDay.getText().toString().split(REGEX_1)[2];
    Log.d("call", String.format("日時検査 --- %s : %s", sDay, eDay));
    if (Integer.valueOf(sDay) > Integer.valueOf(eDay)) {    //*** 開始日時のほうが大きい -→ 異常 ***//
      Log.d("call", "開始日時のほうが大きい矛盾発生");

      //*** ダイアログに、どのエラー種別か渡す ***//
      bundle.putString("error", "date");
      //*** 警告ダイアログの出力 ***//
      CautionDialog dialog = new CautionDialog();
      dialog.setArguments(bundle);
      dialog.show(getFragmentManager(), "date");
      return false;   //*** 異常 を返す ***//
    }

    //*** 開始が終了より遅いかなどの矛盾をチェックする ***//
    String sTime = btStartTime.getText().toString().split(REGEX)[0] + btStartTime.getText().toString().split(REGEX)[1];
    String eTime = btEndTime.getText().toString().split(REGEX)[0] + btEndTime.getText().toString().split(REGEX)[1];
    Log.d("call", String.format("時刻検査 --- %s : %s", sTime, eTime));
    if (Integer.valueOf(sTime) > Integer.valueOf(eTime)) {  //*** 開始時刻のほうが大きい -→ 異常 ***//
      Log.d("call", "開始時刻のほうが大きい矛盾発生");

      //*** ダイアログに、どのエラー種別か渡す ***//
      bundle.putString("error", "day");
      //*** 警告ダイアログの出力 ***//
      CautionDialog dialog = new CautionDialog();
      dialog.setArguments(bundle);
      dialog.show(getFragmentManager(), "day");
      return false;   //*** 異常 を返す ***//
    }

    return true;    //*** 矛盾なしを返す ***//
  }

  //*** --- SELF MADE METHOD --- ウィジェットに空欄があるかチェックするメソッド ***//
  public boolean isBrankSpace() {
    Log.d("call", "call isBrankSpace()");

    if (edOverView.getText().toString().isEmpty()) {         //*** 会議概要 ***//
      Log.d("call", "空欄あり");
      //*** ダイアログに、どのエラー種別か渡す ***//
      Bundle bundle = new Bundle();
      bundle.putString("error", "brank");
      //*** 警告ダイアログの出力 ***//
      CautionDialog dialog = new CautionDialog();
      dialog.setArguments(bundle);
      dialog.show(getFragmentManager(), "brank");
      return false;
    }
    return true;    //*** ブランク無し（正常）を返す ***//
  }

}
