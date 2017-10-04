package com.example.yuichi_oba.ecclesia.activity;

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
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
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

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.Employee;
import com.example.yuichi_oba.ecclesia.model.Reserve;
import com.example.yuichi_oba.ecclesia.tools.DB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.KEYCHANGE;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.KEYCHECK;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.ONE;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.ZERO;
import static com.example.yuichi_oba.ecclesia.activity.ReserveConfirmActivity.re_id;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.*;

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
    public static Employee appEmp;

    EditText overview;
    Spinner sp_purpose;
    TextView startDay;
    TextView endDay;
    TextView startTime;
    TextView endTime;
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
        appEmp = (Employee) intent.getSerializableExtra("emp");

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
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                setReserveInfo();
                changes = new String[]{changeRes.getRe_name(), changeRes.getRe_purpose_name(), changeRes.getRe_startDay() + " " + changeRes.getRe_startTime(), changeRes.getRe_endDay() + " " + changeRes.getRe_endTime(),
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

    public static class CheckView extends View {
        private Paint p_line;
        private Paint p_out_line;
        private Paint p_text;
        private Paint p_rect;
        private Paint p_change;

        private String[] name = {"概要", "目的", "開始時間", "終了時間", "申請者", "参加者", "社内/社外", "会社名", "希望会議室", "備品", "その他"};
        private String[] before;

        Reserve reserve;

        public CheckView(Context context) {
            super(context);
            if (isInEditMode()){
                return;
            }
            init();
        }

        public CheckView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            if (isInEditMode()) {
                return;
            }
            init();
        }

        public CheckView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            if (isInEditMode()) {
                return;
            }
            init();
        }

        private void init() {
            // 枠線用
            p_line = new Paint();
            p_line.setColor(Color.DKGRAY);
            p_line.setStyle(Paint.Style.STROKE);
            p_line.setStrokeWidth(10);

            p_out_line = new Paint();
            p_out_line.setStrokeWidth(2.0f);


            // テキスト用
            p_text = new Paint();
            p_text.setTypeface(Typeface.MONOSPACE);
            p_text.setTextSize(40);
            p_text.setTextAlign(Paint.Align.CENTER);
            p_text.setColor(Color.BLACK);

            // 色違い用
            p_rect = new Paint();
            p_rect.setColor(Color.LTGRAY);
            p_rect.setStyle(Paint.Style.FILL);

            // 変更箇所の色
            p_change = new Paint();
            p_change.setColor(Color.parseColor("#87cefa"));
            p_change.setStyle(Paint.Style.FILL);

            reserve = new Reserve();
            reserve = Reserve.retReserveConfirm(ReserveConfirmActivity.re_id);
            Log.d("room", reserve.getRe_room_name());
            before = new String[]{reserve.getRe_name(), reserve.getRe_purpose_name(), reserve.getRe_startDay() + " " + reserve.getRe_startTime(), reserve.getRe_endDay() + " " + reserve.getRe_endTime(),
                   reserve.getRe_applicant(), "", reserve.getRe_switch(), "", reserve.getRe_room_name(), reserve.getRe_fixtures(), reserve.getRe_remarks()};
        }

        //*** 描画メソッド ***//
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //*** 基本の枠の描画 ***//
            onDrawBasic(canvas);
            //*** 予約情報の描画 ***//
            onDrawReserveInfo(canvas);
        }

        //*** 予約情報の描画メソッド ***//
        private void onDrawReserveInfo(Canvas c) {
            float y_name = 70;
            float y_purpose = 190;
            float y_start = 310;
            float y_end = 430;
            float y_applicant = 550;
            float y_member = 670;
            float y_switch = 790;
            float y_company = 910;
            float y_room = 1030;
            float y_fixture = 1150;
            float y_remark = 1270;

            c.drawText(changes[ZERO], 500, y_name, p_text);
            c.drawText(changes[ONE], 500, y_purpose, p_text);
            c.drawText(changes[TWO], 500, y_start, p_text);
            c.drawText(changes[THREE], 500, y_end, p_text);
            c.drawText(changes[FOUR], 500, y_applicant, p_text);
            c.drawText(changes[FIVE], 500, y_member, p_text);
            c.drawText(changes[SIX].contains("0") ? "社内" : "社外", 500, y_switch, p_text);
//            c.drawText(changes[SEVEN], 500, y_company, p_text);
            c.drawText(changes[EIGHT], 500, y_room, p_text);
            c.drawText(changes[NINE], 500, y_fixture, p_text);
            c.drawText(changes[TEN], 500, y_remark, p_text);

        }

        private void onDrawBasic(Canvas c) {
            c.drawRect(0, 1080, 0, 1920, p_line);
            float room = 120;
            float room_y = 70;
            p_text.setTextAlign(Paint.Align.LEFT);
            // sx = 左上 sy　=　左下 ex　=　幅 ey　=　右下
            c.drawLine(0, room, 1080, room, p_out_line);

            float padding = 3;
            for (int i = 1; i < 13; i += 2) {
                c.drawRect(0 + padding, room * i - room + padding, 1080 - padding, room * i - padding, p_rect);
            }

            for (int i = 1; i <= 11; i++) {
                if (!changes[i - 1].equals(before[i - 1])) {
                    c.drawRect(0 + padding, room * i - room + padding, 1080 - padding, room * i - padding, p_change);
                }
                c.drawLine(0, room * i, 1080, room * i, p_out_line);
                c.drawText(name[i - 1], 100, room_y, p_text);
                room_y += room;
            }
        }
    }

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
