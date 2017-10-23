package com.example.yuichi_oba.ecclesia.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.Reserve;
import com.example.yuichi_oba.ecclesia.tools.DB;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.KEYCHECK;

public class ReserveCheckActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener{
    private static ReserveCheckActivity instance = null;

    //*** 変更情報を入力した後の予約インスタンス ***//
    Reserve checkRes;

    //*** 確定ボタン ***//
    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_check);

        instance = this;

        //*** 変更箇所入力後の予約インスタンスを受け取る ***//
        checkRes = (Reserve) getIntent().getSerializableExtra(KEYCHECK);

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

    //*** SelfMadeMethod ***//
    private void init() {
        //*** ボタンにID割り当て＆リスナーセット ***//
        button = (Button) findViewById(R.id.accheck_btn_correct);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserveChange();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //*** ナビゲーション処理 ***//
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

    //*** SelfMadeMethod ***//
    //*** 実際にDBの予約情報を書き換える(現在エラー中) ***//
    public void reserveChange() {
        //*** 必要なインスタンスを用意 ***//
        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        //*** トランザクション開始 ***//
//        db.beginTransaction();
        //*** DBに変更をかけるためのクラス ***//
//        ContentValues con = new ContentValues();
        //*** 書き換えるカラム、書き換える情報の指定 ***//
//        con.put("re_overview", checkRes.getRe_name());
//        con.put("re_startday", checkRes.getRe_startDay());
//        con.put("re_endday", checkRes.getRe_endDay());
//        con.put("re_starttime", checkRes.getRe_startTime());
//        con.put("re_endtime", checkRes.getRe_endTime());
//        con.put("re_switch", checkRes.getRe_switch());
//        con.put("re_fixtrue", checkRes.getRe_fixtures());
//        con.put("re_remarks", checkRes.getRe_remarks());
//        con.put("com_id", checkRes.getRe_company());
//        con.put("emp_id", checkRes.getRe_applicant());
//        con.put("por_id", checkRes.getRe_purpose_id());
//        con.put("room_id", checkRes.getRe_room_id());
        //*** where句を用意 ***//
//        String where = "re_id = ?";
        //*** ?に入れるものを指定する ***//
//        String whereArgs[] = new String[ONE];
//        whereArgs[ZERO] = checkRes.getRe_id();
        //*** アップデートを掛けに行く ***//
//        db.update("t_extension", con, where, whereArgs);
        //*** コミットをかける ***//
//        db.setTransactionSuccessful();
        //*** トランザクション終了 ***//
//        db.endTransaction();

        //*** SQLでアップデートかける ***//
        db.execSQL("update t_reserve set re_overview = ? , re_startday = ?, re_endday = ?, re_starttime = ?, re_endtime = ?," +
                " re_switch = ?, re_fixture = ?, re_remarks = ?, re_priority = ?, room_id = ?, pur_id = ?" +
                " where re_id = ? ", new Object[]{checkRes.getRe_name(), checkRes.getRe_startDay(), checkRes.getRe_endDay(), checkRes.getRe_startTime(),
                checkRes.getRe_endTime(), checkRes.getRe_switch(), checkRes.getRe_fixtures(), checkRes.getRe_remarks(), "会議優先度", checkRes.getRe_room_id()
                , checkRes.getRe_purpose_id(), checkRes.getRe_id()});

        //*** 変更成功通知ダイアログを表示する ***//
        ChangeResultDialog changeResultDialog = new ChangeResultDialog();
        changeResultDialog.show(getFragmentManager(), "change");

        //*** 予約一覧へ画面遷移を行う ***//
        Intent intent = new Intent(getApplicationContext(), ReserveListActivity.class);
        startActivity(intent);
    }

    public static ReserveCheckActivity getInstance() {
        return instance;
    }

    //*** 変更成功通知ダイアログ ***//
    public static class ChangeResultDialog extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity()).setTitle("変更完了")
                    .setMessage("変更が完了しました").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { }
                    }).create();
        }

        @Override
        public void onPause() {
            super.onPause();
            dismiss();
        }
    }
}
