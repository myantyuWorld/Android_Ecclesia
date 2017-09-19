package com.example.yuichi_oba.ecclesia.activity;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.Reserve;
import com.example.yuichi_oba.ecclesia.tools.DB;

import java.util.ArrayList;
import java.util.List;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.KEYCHANGE;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.KEYCHECK;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.ONE;
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
    TextView startDay;
    TextView endDay;
    TextView startTime;
    TextView endTime;
    Button startDayBtn;
    Button endDayBtn;
    Button startTimeBtn;
    Button endTimeBtn;
    Switch inout;
    Spinner room;
    Spinner members;
    EditText fixtrues;
    EditText remarks;
    TextView sinseisya;
    TextView comp;

    List<String> member = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_change);

        Intent intent = getIntent();
        re_id = intent.getStringExtra(KEYCHANGE);

        // {"概要", "目的", "開始時間", "終了時間", "申請者", "参加者", "社内/社外", "会社名", "希望会議室", "備品", "その他"};
        changes = new String[]{"", "", "", "", "", "", "", "", "", "", ""};

        changeRes = new Reserve();

        init();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FloatingActionButton fbn = (FloatingActionButton) findViewById(R.id.fbn_addMember);
        fbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ここで、参加者
                Intent intent = new Intent(getApplication(), AddMemberActivity.class);
                startActivity(intent);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                setReserveInfo();
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
    }

    private void init() {
        overview = (EditText) findViewById(R.id.change_gaiyou);
        fixtrues = (EditText) findViewById(R.id.change_fix);
        remarks = (EditText) findViewById(R.id.change_remark);
        startDayBtn = (Button) findViewById(R.id.change_sDaybtn);
        startTimeBtn = (Button) findViewById(R.id.change_sTimebtn);
        endDayBtn = (Button) findViewById(R.id.change_eDaybtn);
        endTimeBtn = (Button) findViewById(R.id.change_eTimebtn);
        sp_purpose = (Spinner) findViewById(R.id.change_sppurpose);
        sinseisya = (TextView) findViewById(R.id.change_sinseisya);
        members = (Spinner) findViewById(R.id.change_spmember);
        room = (Spinner) findViewById(R.id.change_room);
        comp = (TextView) findViewById(R.id.change_company);

        editBtn = (Button) findViewById(R.id.change_confirm);
        inout = (Switch) findViewById(R.id.change_inout);

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
            rooms.add(c.getString(0) + " : " + c.getString(1));
        }
        c.close();
        ArrayAdapter<String> roomadapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, rooms);
        room.setAdapter(roomadapter);

        changeRes = Reserve.retReserveConfirm(re_id);

        overview.setText(changeRes.getRe_name());
        fixtrues.setText(changeRes.getRe_fixtures());
        remarks.setText(changeRes.getRe_remarks());
        sinseisya.setText(changeRes.getRe_applicant());
        startDayBtn.setText(changeRes.getRe_startDay());
        endDayBtn.setText(changeRes.getRe_endDay());
        startTimeBtn.setText(changeRes.getRe_startTime());
        endTimeBtn.setText(changeRes.getRe_endTime());

        // テキストが変わったとき　http://gupuru.hatenablog.jp/entry/2014/04/07/202334
        overview.addTextChangedListener(new TextWatcher() {
            // テキスト変更前
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            // テキスト変更中
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            // テキスト変更後
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
            public void afterTextChanged(Editable s) {
                changeRes.setRe_endTime(s.toString());
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


    public void setReserveInfo() {
//        reserveInfo.setRe_name(overview.getText().toString());
//        reserveInfo.setRe_startDay(startDayBtn.getText().toString());
//        reserveInfo.setRe_startTime(startTimeBtn.getText().toString());
//        reserveInfo.setRe_endDay(endDayBtn.getText().toString());
//        reserveInfo.setRe_endTime(endTimeBtn.getText().toString());
//        reserveInfo.setRe_room_name(room.getSelectedItem().toString());
//        reserveInfo.setRe_purpose_name(sp_purpose.getSelectedItem().toString());
//        reserveInfo.setRe_fixtures(fixtrues.getText().toString());
//        reserveInfo.setRe_remarks(remarks.getText().toString());
//        reserveInfo.setRe_company(comp.getText().toString());
    }
}
