package com.example.yuichi_oba.ecclesia.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.ReserveInfo;
import com.example.yuichi_oba.ecclesia.tools.DB;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.*;

public class ReserveCheckActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener{
    TextView txt_overview;              // 概要
    TextView txt_purpose;               // 会議目的
    TextView txt_startDay;              // 開始日
    TextView txt_endday;                // 終了日
    TextView txt_startTime;             // 開始時刻
    TextView txt_endTime;               // -終了時刻
    TextView txt_applicant;             // 予約者
    TextView txt_inOutHouse;            // 社外社内区分
    TextView txt_conferenceRoom;        // 使用会議室
    TextView txt_fixtures;              // 備品
    TextView txt_remarks;               // 備考
    TextView txt_member;                // 会議参加者

    Button button;

    static ReserveInfo reserveInfo;     // 予約情報クラスの変数

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_check);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
    }

    private void init() {
        txt_overview = (TextView) findViewById(R.id.check_overView);
        txt_purpose = (TextView) findViewById(R.id.check_purpose);
        txt_startDay = (TextView) findViewById(R.id.check_startDay);
        txt_endday = (TextView) findViewById(R.id.check_endDay);
        txt_startTime = (TextView) findViewById(R.id.check_startTime);
        txt_endTime = (TextView) findViewById(R.id.check_endTime);
        txt_applicant = (TextView) findViewById(R.id.check_applicant);
        txt_inOutHouse = (TextView) findViewById(R.id.check_inOutHouse);
        txt_conferenceRoom = (TextView) findViewById(R.id.check_room);
        txt_fixtures = (TextView) findViewById(R.id.check_fixtures);
        txt_remarks = (TextView) findViewById(R.id.check_remarks);
        txt_member = (TextView) findViewById(R.id.check_member);
        button = (Button) findViewById(R.id.check_btn);

        reserveInfo = (ReserveInfo) getIntent().getSerializableExtra(KEYCHECK);

        txt_overview.setText(reserveInfo.getRe_overview());
        txt_purpose.setText(reserveInfo.getRe_purpose());
        txt_startDay.setText(reserveInfo.getRe_startDay());
        txt_endday.setText(reserveInfo.getRe_endDay());
        txt_startTime.setText(reserveInfo.getRe_startTime());
        txt_endTime.setText(reserveInfo.getRe_endTime());
//        txt_applicant.setText(reserveInfo.get);
        if (reserveInfo.getRe_flg() == ZERO) {
            txt_inOutHouse.setText(IN);
        } else {
            txt_inOutHouse.setText(OUT);
        }
        txt_conferenceRoom.setText(reserveInfo.getRe_roomId());
        txt_remarks.setText(reserveInfo.getRe_marks());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserveChange();
                Intent intent = new Intent(getApplicationContext(), ReserveListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

    public void reserveChange(){
        ContentValues con = new ContentValues();
        con.put("re_overview", reserveInfo.getRe_overview());
        con.put("re_startday", reserveInfo.getRe_startDay());
        con.put("re_endday", reserveInfo.getRe_endDay());
        con.put("re_starttime", reserveInfo.getRe_startTime());
        con.put("re_endtime", reserveInfo.getRe_endTime());
        con.put("re_switch", reserveInfo.getRe_flg());
        con.put("re_fixtrue", reserveInfo.getFixtrues());
        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.update("t_reserve", con, null, null) > ZERO) {

        }
    }
}
