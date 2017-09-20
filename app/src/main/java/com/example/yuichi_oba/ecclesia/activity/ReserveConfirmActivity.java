package com.example.yuichi_oba.ecclesia.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.Employee;
import com.example.yuichi_oba.ecclesia.tools.DB;

import java.util.ArrayList;
import java.util.List;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.*;

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/
// _/_/ 予約の詳細・確認を行うアクティビティ
// _/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
public class ReserveConfirmActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //***  ***//
//    public static Reserve reserve;
    private Employee employee;
    public static String re_id;

    // デバッグ用
    private static final String TAG = ReserveConfirmActivity.class.getSimpleName();

    //*** 会議参加者をリスト形式で出す、ダイアログフラグメントクラス ***//
    public static class MemberConfirmDialog extends DialogFragment {
        // ダイアログを生成するメソッド
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // 会議参加者データ
//            CharSequence[] items = reserveInfo.getRe_member().toArray(new CharSequence[reserveInfo.getRe_member().size()]);
//
//            return new AlertDialog.Builder(getActivity())
//                    .setTitle("会議参加者一覧")
//                    .setItems(items, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                        }
//                    })
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                        }
//                    })
//                    .create();
//        }

        // ダイアログを破棄するメソッドーー＞HCP不要
        @Override
        public void onPause() {
            super.onPause();
            dismiss();
        }
    }
    //*** 早期退出」オプション選択時の ダイアログフラグメントクラス ***//
    public static class EarlyOutDialog extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("早期退出")
                    .setMessage("早期退出しますか？")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getActivity(), "早期退出", Toast.LENGTH_SHORT).show();
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
        }

    }
    //*** 延長オプション選択時の ダイアログフラグメントクラス ***//
    public static class ExtentionDialog extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.extention_dialog, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            return builder.setTitle(EX)
                    .setView(layout)
                    .setPositiveButton(EX, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            ContentValues con = new ContentValues();
//                            con.put("ex_endtime", reserveInfo.getRe_endTime());
//                            SQLiteOpenHelper helper = new DB(getApplicationContext());
//                            SQLiteDatabase db = helper.getWritableDatabase();
//                            if (db.update("t_extension", con, "re_id = " + reserveInfo.getRe_id(), null) > ZERO) {
//
//                            }
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

    //*** onCreate ***//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("call", "ReserveConfirmActivity->onCreate()");

        //*** 前画面からの引数を受け取る（re_id） ***//
        Intent intent = getIntent();
        re_id = intent.getStringExtra("re_id");
        Log.d("call", re_id);

        employee = (Employee) intent.getSerializableExtra("emp");



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
        // idによって処理を分ける
        switch (id) {
            // 「早期退出」が選択された
            case R.id.option_earlyOut:
                EarlyOutDialog earlyOutDialog = new EarlyOutDialog();
                earlyOutDialog.show(getFragmentManager(), "out");
                break;
            // 「予約変更」が選択された
            case R.id.option_reserveChange:
                intent = new Intent(getApplicationContext(), ReserveChangeActivity.class);
                intent.putExtra(KEYCHANGE, re_id);
                intent.putExtra("emp", employee);
                startActivity(intent);
//                Toast.makeText(this, "予約変更", Toast.LENGTH_SHORT).show();
                // 予約情報インスタンスを次の画面にオブジェクト渡しする
                break;
            // 「延長」が選択された
            case R.id.option_extention:
//                intent = new Intent(getApplicationContext(), ExtentionActivity.class);
//                intent.putExtra(KEYEX, reserveInfo);
//                startActivity(intent);

                ExtentionDialog extentionDialog = new ExtentionDialog();
                extentionDialog.show(getFragmentManager(), KEYEX);
                break;
        }
        // 選択された結果（項目）を返す
        return super.onOptionsItemSelected(item);
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

    //*** SelfMadeMethod ***//
    //*** インスタンスと、画面情報をマッピングするメソッド ***//
    private void setWidgetInfo() {
//        txt_overview.setText(reserveInfo.getRe_overview());                 // 項目「概要」に予約情報の概要を設定する(以下同様
//        txt_purpose.setText(reserveInfo.getRe_purpose());                   // 目的
//        txt_startDay.setText(reserveInfo.getRe_startDay());                 // 開始日
//        txt_startTime.setText(reserveInfo.getRe_startTime());               // 開始時刻
//        txt_endday.setText(reserveInfo.getRe_endDay());                     // 終了日
//        txt_endTime.setText(reserveInfo.getRe_endTime());                   // 終了時刻
//        txt_applicant.setText(reserveInfo.getRe_rePerson());                // 予約者
//        txt_conferenceRoom.setText(reserveInfo.getRe_conference_room());    // 会議室
    }
    //*** 予約情報クラスのインスタンスに、ＤＢ検索した結果をセットする ***//
    private void setReserveInfo(Cursor c) {
//        reserveInfo.setRe_overview("aaaaaa");               // 予約情報クラスのインスタンスに、概要をセットする(以下同様
//        reserveInfo.setRe_purpose(c.getString(1));          // 会議目的名
////        reserveInfo.setRe_startDay();                     開始日は現在ビューにないので後に追記か、代替案が必要
////        reserveInfo.setRe_endDay();                       同上
//        reserveInfo.setRe_startTime(c.getString(4));        // 開始時刻
//        reserveInfo.setRe_endTime(c.getString(5));          // 終了時刻
//        reserveInfo.setRe_rePerson(c.getString(TWO));         // 予約者
//        reserveInfo.setRe_conference_room(c.getString(12)); // 会議室名
//    }
    }
    //*** 画面の各ウィジェットの初期化処理メソッド ***//
    private void init() {

//        txt_overview = (TextView) findViewById(R.id.txt_rd_overView);       // 「概要」テキストビューを取得(以下同様
//        txt_purpose = (TextView) findViewById(R.id.txt_rd_purpose);         // 会議目的名
//        txt_startDay = (TextView) findViewById(R.id.cre_startDay);          // 開始日
//        txt_startTime = (TextView) findViewById(R.id.txt_rd_startTime);     // 開始時刻
//        txt_endday = (TextView) findViewById(R.id.txt_rd_endDay);           // 終了日
//        txt_endTime = (TextView) findViewById(R.id.txt_rd_endTime);         // 終了時刻
//        txt_applicant = (TextView) findViewById(R.id.txt_rd_applicant);     // 予約者
//        txt_inOutHouse = (TextView) findViewById(R.id.txt_rd_inOutHouse);   // 社内社外区分
//        txt_conferenceRoom = (TextView) findViewById(R.id.txt_rd_room);     // 会議室名
//        txt_fixtures = (TextView) findViewById(R.id.txt_rd_fixtures);       // 備品
//        txt_remarks = (TextView) findViewById(R.id.txt_rd_remarks);         // 備考
//        txt_member = (TextView) findViewById(R.id.txt_member);              // 参加者
        // 「参加者」テキストにリスナー登録
//        txt_member.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(ReserveConfirmActivity.this, "test", Toast.LENGTH_SHORT).show();
//                // 会議参加者ダイアログを生成する
//                MemberConfirmDialog memberConfirmDialog = new MemberConfirmDialog();
//                memberConfirmDialog.show(getFragmentManager(), "ccc");
//            }
//        });

    }
    //*** 予約詳細をDB検索して、画面へ反映させるメソッド ***//
    private void dbSearchReserveConfirm() {
        Log.d("call", "ReserveConfirmActivity->dbSearchReserveConfirm()");
        // 予約情報クラスのインスタンスから、予約情報詳細をＤＢ検索して、画面にマッピングする
        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from v_reserve_member where re_id = ?", new String[]{re_id});
        List<String> list = new ArrayList<>();
        // 検索結果が存在する
        if (c.moveToNext()) {
            // 予約情報クラスのインスタンスに、ＤＢ検索した結果をセットする
            setReserveInfo(c);
            // インスタンスと、画面情報をマッピングする
            setWidgetInfo();
            // 会議参加者を追加する 15 16
            list.add(c.getString(15) + " : " + c.getString(16));
        }
        c.close();

        // 次に、会議参加者をDB検索する、予約情報クラスのインスタンスに会議参加者情報をセットする
//        reserveInfo.setRe_member(list);
    }



}
