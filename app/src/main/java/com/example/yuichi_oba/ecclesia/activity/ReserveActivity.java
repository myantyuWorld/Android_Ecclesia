package com.example.yuichi_oba.ecclesia.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.Employee;
import com.example.yuichi_oba.ecclesia.tools.DB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import android.app.Dialog;

//import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/
// _/_/ 予約を行うアクティビティ
// _/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
public class ReserveActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //***  ***//
    private Employee employee;
    private Spinner sp_room;
    private Spinner sp_purpose;
    private Spinner sp_member;
    private static Button btStartDay;
    private static Button btEndDay;
    private static Button btStartTime;
    private static Button btEndTime;
    private Button btReConfirm;
    //    private ReserveInfo reserveInfo;
    public static List<Employee> member = new ArrayList<>();

    //*** 日付ダイアログ ***//
    public static class MyDateDialog extends DialogFragment {

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
                                btStartDay.setText(String.format("%04d/%02d/%02d", year, month + 1, day));
                            } else {
                                btEndDay.setText(String.format("%04d/%02d/%02d", year, month + 1, day));
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
                                btStartTime.setText(String.format("%02d : %02d", hourOfDay, minute));
                            } else {
                                btEndTime.setText(String.format("%02d : %02d", hourOfDay, minute));
                            }
                        }
                    },
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
            );
        }
    }

    //*** onCreate ***//
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //*** 前画面からのオブジェクトをもらう（Employeeクラスのインスタンス） ***//
        Intent intent = getIntent();
        employee = (Employee) intent.getSerializableExtra("emp");


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
                intent.putExtra("emp_id", employee.getId());
                startActivity(intent);
            }
        });

        init();


    }
    //*** 各ウィジェットの初期化処理メソッド ***//
    private void init() {

//        reserveInfo = new ReserveInfo();
        //*** 申請者の設定 ***//
        TextView txtApplicant = (TextView) findViewById(R.id.txt_re_shinseisya);
        txtApplicant.setText(employee.getName());

        //*** 参加者スピナー ***//
        sp_member = (Spinner) findViewById(R.id.sp_re_member);
        final List<String> list = new ArrayList<>();
        for (Employee employee : member) {
            list.add(employee.getCom_name() + " : " + employee.getName());
        }
        ArrayAdapter<String> adapter_member = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list);
        sp_member.setAdapter(adapter_member);

        //*** 会議目的スピナー ***//
        sp_purpose = (Spinner) findViewById(R.id.sp_re_purpose);
        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        List<String> purpose = new ArrayList<>();
        Cursor c = db.rawQuery("select * from m_purpose", null);
        while (c.moveToNext()) {
            purpose.add(c.getString(0) + ":" + c.getString(1));
        }
        c.close();
        ArrayAdapter<String> adapter_purpose = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, purpose);
        sp_purpose.setAdapter(adapter_purpose);

        //*** 会議室スピナー ***//
        sp_room = (Spinner) findViewById(R.id.sp_room);
        c = db.rawQuery("select * from m_room", null);
        List<String> listRoom = new ArrayList<>();
        while (c.moveToNext()) {
            listRoom.add(c.getString(0) + " : " + c.getString(1));
        }
        c.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listRoom);
        sp_room.setAdapter(adapter);


        btStartDay = (Button) findViewById(R.id.bt_re_sDay);
        btStartTime = (Button) findViewById(R.id.bt_re_sTime);
        btEndDay = (Button) findViewById(R.id.bt_re_eDay);
        btEndTime = (Button) findViewById(R.id.bt_re_eTime);

        btReConfirm = (Button) findViewById(R.id.bt_re_confirm);
        

        //***  ***//
        setWidgetListener();
    }
    //*** 各種ウィジェットのリスナーを登録するメソッド ***//
    private void setWidgetListener() {
        sp_member.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_purpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_room.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        //*** 内容確認ボタン押下時の処理 ***//
        btReConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("call", "内容確認ボタン押下");


            }
        });
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
}
