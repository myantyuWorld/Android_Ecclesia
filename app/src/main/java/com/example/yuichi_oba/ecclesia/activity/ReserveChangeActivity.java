package com.example.yuichi_oba.ecclesia.activity;

import android.content.ContentValues;
import android.content.Intent;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.ReserveInfo;
import com.example.yuichi_oba.ecclesia.tools.DB;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.*;
//import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/
// _/_/ 予約変更を行うアクティビティ
// _/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
public class ReserveChangeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ReserveInfo reserveInfo ;
    Button editBtn;

    EditText overview;
    Spinner purpose;
    TextView startDay;
    TextView endDay;
    TextView startTime;
    TextView endTime;
    Button startDayBtn;
    Button endDayBtn;
    Button startTimeBtn;
    Button endTimeBtn;
    TextView applicant;
    Switch inout;
    Spinner room;
    EditText fixtrues;
    EditText remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_change);

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
                setReserveInfo();
                Intent intent = new Intent(getApplicationContext(), ReserveCheckActivity.class);
                intent.putExtra("Check", reserveInfo);
                startActivity(intent);
            }
        });

        inout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    reserveInfo.setRe_flg(ONE);
                } else {
                    reserveInfo.setRe_flg(ZERO);
                }
            }
        });
    }

    private void init() {
        editBtn = (Button) findViewById(R.id.cre_editbtn);
        overview = (EditText) findViewById(R.id.cre_overview);
        purpose = (Spinner) findViewById(R.id.cre_purpose);
        startDay = (TextView) findViewById(R.id.cre_startDay);
        endDay = (TextView) findViewById(R.id.cre_endDay);
        startTime = (TextView) findViewById(R.id.cre_startTime);
        endTime = (TextView) findViewById(R.id.cre_endTime);
        startDayBtn = (Button) findViewById(R.id.cre_startDayBtn);
        endDayBtn = (Button) findViewById(R.id.cre_endDayBtn);
        startTimeBtn = (Button) findViewById(R.id.cre_startTimeBtn);
        endTimeBtn = (Button) findViewById(R.id.cre_endTimeBtn);
        applicant = (TextView) findViewById(R.id.cre_applicant);
        inout = (Switch) findViewById(R.id.cre_inoutsw);
        room = (Spinner) findViewById(R.id.cre_room);
        fixtrues = (EditText) findViewById(R.id.cre_fixtrues);
        remarks = (EditText) findViewById(R.id.cre_remarks);

        reserveInfo = (ReserveInfo) getIntent().getSerializableExtra(KEYCHANGE);
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

    // いずれ削除するテスト用メソッド
    public void imitationEdit(){

    }

    public void setReserveInfo() {
        reserveInfo.setRe_overview(overview.getText().toString());
        reserveInfo.setRe_startDay(startDay.getText().toString());
        reserveInfo.setRe_startTime(startTime.getText().toString());
        reserveInfo.setRe_endDay(endDay.getText().toString());
        reserveInfo.setRe_endTime(endTime.getText().toString());
        reserveInfo.setRe_conference_room(room.getSelectedItem().toString());
        reserveInfo.setRe_purpose(purpose.getSelectedItem().toString());
        reserveInfo.setFixtrues(fixtrues.getText().toString());
        reserveInfo.setRe_marks(remarks.getText().toString());
    }
}
