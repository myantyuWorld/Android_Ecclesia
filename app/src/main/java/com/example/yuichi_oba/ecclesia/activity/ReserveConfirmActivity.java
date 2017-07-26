package com.example.yuichi_oba.ecclesia.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.ReserveInfo;
import com.example.yuichi_oba.ecclesia.tools.DB;

import java.util.ArrayList;
import java.util.List;

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/
// _/_/ 予約の詳細・確認を行うアクティビティ
// _/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
public class ReserveConfirmActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = ReserveConfirmActivity.class.getSimpleName();

    TextView txt_overview;
    TextView txt_purpose;
    TextView txt_startTime;
    TextView txt_endTime;
    TextView txt_applicant;
    TextView txt_inOutHouse;
    TextView txt_conferenceRoom;
    TextView txt_fixtures;
    TextView txt_remarks;
    Spinner sp_member;

    ReserveInfo reserveInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        init();
        // 遷移前の画面から、オブジェクトを受け取る
        reserveInfo = (ReserveInfo) getIntent().getSerializableExtra("reserve_info");
        // 予約詳細をDB検索して、画面にマッピングするメソッド
        dbSearchReserveConfirm();


    }

    private void dbSearchReserveConfirm() {
        // 予約情報クラスのインスタンスから、予約情報詳細をＤＢ検索して、画面にマッピングする
        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from v_reserve where re_id = ?", new String[]{reserveInfo.getRe_id()});

        if (c.moveToNext()) {
            // 予約情報クラスのインスタンスに、ＤＢ検索した結果をセットする
            // インスタンスと、画面情報をマッピングする
            setReserveInfo(c);
            setWidgetInfo();
        }
        // 次に、会議参加者をDB検索して、スピナーに設定する
        c = db.rawQuery("select * from v_member where re_id = ?", new String[]{reserveInfo.getRe_id()});
        List<String> list = new ArrayList<>();
        while (c.moveToNext()) {
            Log.d(TAG, "setReserveInfo: " + c.getString(2));
            String member = String.format("%s : %s", c.getString(2), c.getString(6));
            Log.d(TAG, member);
            list.add(String.format("%s : %s", c.getString(2), c.getString(6)));
        }
        // 予約情報と、画面情報のマッピング
        reserveInfo.setRe_member(list);
    }

    /***
     * アクティビティのライフサイクルとして、別の画面にいってまた帰ってきたとき、コールされる
     */
    @Override
    protected void onStart() {
        super.onStart();
        dbSearchReserveConfirm();
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
    // _/_/ オプション画面を作成するメソッド
    // _/_/
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reserve_confirm, menu);
        return true;
    }

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // _/_/
    // _/_/ オプションを選択したときの処理
    // _/_/
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent;
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.option_earlyOut:
                Toast.makeText(this, "早期退出", Toast.LENGTH_SHORT).show();
                break;
            case R.id.option_reserveChange:
                Toast.makeText(this, "予約変更", Toast.LENGTH_SHORT).show();
                // 予約情報インスタンスを次の画面にオブジェクト渡しする

                break;
            case R.id.option_extention:
                intent = new Intent(getApplicationContext(), ExtentionActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // _/_/
    // _/_/ ナビを選択したときの処理
    // _/_/
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent = null;
        int id = item.getItemId();
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

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // _/_/
    // _/_/ SELF MADE METHOD
    // _/_/
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/

    /***
     * // インスタンスと、画面情報をマッピングする
     */
    private void setWidgetInfo() {
        txt_overview.setText(reserveInfo.getRe_overview());
        txt_purpose.setText(reserveInfo.getRe_purpose());
        txt_startTime.setText(reserveInfo.getRe_startTime());
        txt_endTime.setText(reserveInfo.getRe_endTime());
        txt_applicant.setText(reserveInfo.getRe_rePerson());
        txt_conferenceRoom.setText(reserveInfo.getRe_conference_room());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, reserveInfo.getRe_member());
        sp_member.setAdapter(adapter);

    }

    /***
     * // 予約情報クラスのインスタンスに、ＤＢ検索した結果をセットする
     *
     * @param c
     */
    private void setReserveInfo(Cursor c) {
        reserveInfo.setRe_overview("aaaaaa");
        reserveInfo.setRe_purpose(c.getString(9));
        reserveInfo.setRe_startTime(c.getString(6));
        reserveInfo.setRe_endTime(c.getString(7));
        reserveInfo.setRe_rePerson(c.getString(2));
        reserveInfo.setRe_conference_room(c.getString(11));
    }

    /***
     * 画面の各ウィジェットの初期化処理
     */
    private void init() {
        txt_overview = (TextView) findViewById(R.id.txt_rd_overView);
        txt_purpose = (TextView) findViewById(R.id.txt_rd_purpose);
        txt_startTime = (TextView) findViewById(R.id.txt_rd_startTime);
        txt_endTime = (TextView) findViewById(R.id.txt_rd_endTime);
        txt_applicant = (TextView) findViewById(R.id.txt_rd_applicant);
        txt_inOutHouse = (TextView) findViewById(R.id.txt_rd_inOutHouse);
        txt_conferenceRoom = (TextView) findViewById(R.id.txt_rd_room);
        txt_fixtures = (TextView) findViewById(R.id.txt_rd_fixtures);
        txt_remarks = (TextView) findViewById(R.id.txt_rd_remarks);
        sp_member = (Spinner) findViewById(R.id.sp_rd_member);
    }


}
