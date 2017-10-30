package com.example.yuichi_oba.ecclesia.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.Reserve;
import com.example.yuichi_oba.ecclesia.tools.DB;
import com.example.yuichi_oba.ecclesia.tools.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.KEYCHANGE;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.KEYCHECK;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.ONE;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.SPACE;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.YYYY_MM_DD_HH_MM;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.ZERO;

//import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/
// _/_/ 予約変更を行うアクティビティ
// _/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// TODO: 2017/09/19 予約の内容を変更した箇所を、色変える処理の実装
// TODO: 2017/09/19 参加者をさらに追加しようとした際のロジックの実装
public class ReserveChangeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String re_id;
    Reserve changeRes ;
    Button editBtn;
    public static String[] changes ;

    EditText overview;
    Spinner sp_purpose;
    private static Button startDayBtn;
    private static Button endDayBtn;
    private static Button startTimeBtn;
    private static Button endTimeBtn;
    Switch inout;
    Spinner room;
    Spinner members;
    EditText fixtrues;
    EditText remarks;
    TextView sinseisya;
    TextView comp;
    FloatingActionButton fbn;

    List<String> member = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_change);

        Intent intent = getIntent();
        changeRes = (Reserve) intent.getSerializableExtra(KEYCHANGE);

        init();
        setListener();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }



    private void init() {
        fbn = (FloatingActionButton) findViewById(R.id.fbn_addMember);
        overview = (EditText) findViewById(R.id.acchange_etxt_gaiyou);
        fixtrues = (EditText) findViewById(R.id.acchange_txt_fixtures);
        remarks = (EditText) findViewById(R.id.acchange_txt_remarks);
        startDayBtn = (Button) findViewById(R.id.acchange_btn_sDay);
        startTimeBtn = (Button) findViewById(R.id.acchange_btn_sTime);
        endDayBtn = (Button) findViewById(R.id.acchange_btn_eDay);
        endTimeBtn = (Button) findViewById(R.id.acchange_btn_eTime);
        sp_purpose = (Spinner) findViewById(R.id.acchange_sp_purpose);
        sinseisya = (TextView) findViewById(R.id.acchange_txt_sinseisya);
        members = (Spinner) findViewById(R.id.acchange_sp_member);
        room = (Spinner) findViewById(R.id.acchange_sp_room);
        comp = (TextView) findViewById(R.id.acchange_txt_company);

        editBtn = (Button) findViewById(R.id.acchange_btn_confirm);
        inout = (Switch) findViewById(R.id.acchange_sw_inout);

        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        List<String> purpose = new ArrayList<>();
        Cursor c = db.rawQuery("select * from m_purpose", null);
        while (c.moveToNext()) {
            purpose.add(c.getString(ZERO) + ":" + c.getString(ONE));
        }
        c.close();
        ArrayAdapter<String> adapter_purpose = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, purpose);
        sp_purpose.setAdapter(adapter_purpose);

        ArrayAdapter<String> memberdap = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, member);
        members.setAdapter(memberdap);

        c = db.rawQuery("select * from m_room", null);
        List<String> rooms = new ArrayList<>();
        while (c.moveToNext()) {
            rooms.add(c.getString(1));
        }
        c.close();
        ArrayAdapter<String> roomadapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, rooms);
        room.setAdapter(roomadapter);

//        changeRes = Reserve.retReserveConfirm(re_id);

        overview.setText(changeRes.getRe_name());
        fixtrues.setText(changeRes.getRe_fixtures());
        remarks.setText(changeRes.getRe_remarks());
        sinseisya.setText(changeRes.getRe_applicant());
        startDayBtn.setText(changeRes.getRe_startDay());
        endDayBtn.setText(changeRes.getRe_endDay());
        startTimeBtn.setText(changeRes.getRe_startTime());
        endTimeBtn.setText(changeRes.getRe_endTime());
    }

    private void setListener() {
        fbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ここで、参加者
                Intent intent = new Intent(getApplication(), AddMemberActivity.class);
                startActivity(intent);
            }
        });

        // テキストが変わったとき　http://gupuru.hatenablog.jp/entry/2014/04/07/202334
        overview.addTextChangedListener(new TextWatcher() {
            // テキスト変更前
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            // テキスト変更中
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            // テキスト変更後(普通はここに書けばいい)
            @Override
            public void afterTextChanged(Editable s) {
                changeRes.setRe_name(s.toString());
                changeBtnEnable();
            }
        });

        fixtrues.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                changeRes.setRe_fixtures(s.toString());
            }
        });

        remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                changeRes.setRe_remarks(s.toString());
            }
        });

        startDayBtn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                changeRes.setRe_startDay(s.toString());
            }
        });

        startTimeBtn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                changeRes.setRe_startTime(s.toString());
            }
        });

        endDayBtn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                changeRes.setRe_endDay(s.toString());
            }
        });

        endTimeBtn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) { changeRes.setRe_endTime(s.toString()); }
        });

        room.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                changeRes.setRe_room_name(room.getSelectedItem().toString());
                changeRes.setRe_room_id(Util.returnRoomId(changeRes.getRe_room_name()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                setReserveInfo();
                //*** 変更後の予約情報をViewで扱う配列に格納 ***//
                changes = new String[]{changeRes.getRe_name(), changeRes.getRe_purpose_name(), changeRes.getRe_startDay() + SPACE + changeRes.getRe_startTime(), changeRes.getRe_endDay() + SPACE + changeRes.getRe_endTime(),
                        changeRes.getRe_applicant(), "", changeRes.getRe_switch(), changeRes.getRe_room_name(), "", changeRes.getRe_fixtures(), changeRes.getRe_remarks()};


                Intent intent = new Intent(getApplicationContext(), ReserveCheckActivity.class);
                intent.putExtra(KEYCHECK, changeRes);
                startActivity(intent);
            }
        });

        inout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    changeRes.setRe_switch(String.valueOf(ONE));
                } else {
                    changeRes.setRe_switch(String.valueOf(ZERO));
                }
            }
        });

        startDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeDateDialog changeDateDialog = new ChangeDateDialog();
                Bundle bundle = new Bundle();
                bundle.putString("date", "startDay");
                changeDateDialog.setArguments(bundle);
                changeDateDialog.show(getFragmentManager(), "startDay");
            }
        });

        startTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeTimeDialog changeTimeDialog = new ChangeTimeDialog();
                Bundle bundle = new Bundle();
                bundle.putString("time", "startTime");
                changeTimeDialog.setArguments(bundle);
                changeTimeDialog.show(getFragmentManager(), "startTime");
            }
        });

        endDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeDateDialog changeDateDialog = new ChangeDateDialog();
                Bundle bundle = new Bundle();
                bundle.putString("date", "endDay");
                changeDateDialog.setArguments(bundle);
                changeDateDialog.show(getFragmentManager(), "endDay");
            }
        });

        endTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeTimeDialog changeTimeDialog = new ChangeTimeDialog();
                Bundle bundle = new Bundle();
                bundle.putString("time", "endTime");
                changeTimeDialog.setArguments(bundle);
                changeTimeDialog.show(getFragmentManager(), "endTime");
            }
        });
    }


    //*** SelfMadeMethod ***//
    //*** 概要入力チェックメソッド ***//
    private boolean overViewCheck() {
        boolean res = true;
        //*** 未入力だったらfalseを返すようにする ***//
        if (overview.getText().toString().isEmpty()) {
            Toast.makeText(this, "概要を入力してください", Toast.LENGTH_SHORT).show();
            res = false;
        }
        return res;
    }

    //*** SelfMadeMethod ***//
    //*** 時刻チェックメソッド ***//
    private boolean timeCheck() {
        boolean res = false;
        //*** 時刻比較カレンダー ***//
        Calendar cal = Calendar.getInstance();
        Calendar cmp = Calendar.getInstance();
        //*** フォーマット用意 ***//
        SimpleDateFormat timeFor = new SimpleDateFormat(YYYY_MM_DD_HH_MM);
        try {
            //*** 開始時刻終了時刻をセット ***//
            cal.setTime(timeFor.parse(startDayBtn.getText() + SPACE + startTimeBtn.getText()));
            cmp.setTime(timeFor.parse(endDayBtn.getText() + SPACE + endTimeBtn.getText()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //*** 時間に矛盾がないか ***//
        if ((cal.get(Calendar.YEAR) <= cmp.get(Calendar.YEAR)) && (cal.get(Calendar.MONTH) <= cmp.get(Calendar.MONTH)) && (cal.get(Calendar.DAY_OF_MONTH) <= cmp.get(Calendar.DAY_OF_MONTH))
                && (cal.get(Calendar.HOUR_OF_DAY) <= cmp.get(Calendar.HOUR_OF_DAY)) && (cal.get(Calendar.MINUTE) < cmp.get(Calendar.MINUTE))) {
            res = true;
        } else {
            Toast.makeText(this, "開始日時より終了日時のほうが早くなっています", Toast.LENGTH_SHORT).show();
        }
        return res;
    }

    //*** SelfMadeMethod ***//
    //*** 入力項目が満足な場合のみボタンを押下可能にする ***//
    private void changeBtnEnable() {
        if (overViewCheck() && timeCheck()) {
            editBtn.setEnabled(true);
        } else {
            editBtn.setEnabled(false);
        }
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


    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // _/_/
    // _/_/ ナビを選択したときの処理
    // _/_/
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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

    //*** 変更情報確認画面で用いるカスタムビュー ***//
    //*** Viewパッケージに移行できたので内部クラス化は中止 ***//

    //*** 日付変更のダイアログ ***//
    public static class ChangeDateDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            return new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            String date = getArguments().getString("date");
                            if (date.contains("startDay")) {
                                startDayBtn.setText(String.format("%04d/%02d/%02d", year, month + 1, day));
                            } else {
                                endDayBtn.setText(String.format("%04d/%02d/%02d", year, month + 1, day));
                            }
                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
        }
    }

    //*** 時刻変更のダイアログ ***//
    public static class ChangeTimeDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            return new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            String date = getArguments().getString("time");
                            if (date.contains("startTime")) {
                                startTimeBtn.setText(String.format("%04d/%02d/%02d", year, month + 1, day));
                            } else {
                                endTimeBtn.setText(String.format("%04d/%02d/%02d", year, month + 1, day));
                            }
                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
        }
    }
}
