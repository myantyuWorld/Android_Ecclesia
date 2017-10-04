package com.example.yuichi_oba.ecclesia.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.Employee;
import com.example.yuichi_oba.ecclesia.model.Reserve;
import com.example.yuichi_oba.ecclesia.tools.DB;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.EX;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.KEYCHANGE;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.KEYEX;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.ZERO;

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/
// _/_/ 予約の詳細・確認を行うアクティビティ
// _/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// DO: 2017/09/19 延長ダイアログの正常動作の実装
// TODO: 2017/09/19 延長ダイアログのレイアウト調整およびデザインの考察 
public class ReserveConfirmActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String exTime = "";

    //***  ***//
//    public static Reserve reserve;
    private Employee employee;
    public static String re_id;
    public static String gamen;
    public static Reserve reserve;

    // 内部クラスからgetApplicationContextするためのやつ(普通にやるとno-staticで怒られる)
    private static ReserveConfirmActivity instance = null;

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
    public static class EarlyOutDialog extends DialogFragment {
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

        @Override
        public void onPause() {
            super.onPause();
            dismiss();
        }
    }

    //*** 延長オプション選択時の ダイアログフラグメントクラス ***//
    public static class ExtentionDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final RelativeLayout layout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.extention_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            return builder.setTitle(EX)
                    .setView(layout)
                    .setPositiveButton(EX, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ContentValues con = new ContentValues();
                            con.put("ex_endtime", exTime);
                            SQLiteOpenHelper helper = new DB(instance.getApplicationContext());
                            SQLiteDatabase db = helper.getWritableDatabase();
                            if (db.update("t_extension", con, "re_id = " + re_id, null) > ZERO) {
//                                Toast.makeText(getActivity(), "延長しました", Toast.LENGTH_SHORT).show();
                                ExtentResultDialog extentResultDialog = new ExtentResultDialog();
                                extentResultDialog.show(getFragmentManager(), "exres");
                            } else {
                                Toast.makeText(getActivity(), "延長失敗", Toast.LENGTH_SHORT).show();
                            }
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

        //*** 前画面からの引数を受け取る ***//
        Intent intent = getIntent();
        gamen = intent.getStringExtra("gamen").contains("0") ? "新規" : "一覧"; //*** 0: 新規  1: 一覧　からの画面遷移 ***//

        // TODO: 2017/10/03 遷移元画面から、Reserveクラスのインスタンスをもらうよう修正する
        Log.d("call", "画面遷移元　" + gamen);
        //*** 画面遷移元によって、処理を分ける ***//
        if (gamen.contains("新規")) {    //*** 「新規」画面からの画面遷移 ***//
            employee = (Employee) intent.getSerializableExtra("emp");        //*** 社員情報の取得 ***//
            reserve = (Reserve) intent.getSerializableExtra("reserve");     //*** 予約情報のインスタンスを取得 ***//

        } else {                         //*** 「一覧」画面からの画面遷移 ***//
            reserve = (Reserve) intent.getSerializableExtra("reserve");     //*** 予約情報のインスタンスを取得 ***//
        }

        instance = this;

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

    // これでどこからでもgetApplicationContextできる
    // 同一Activityから呼び出す際は不要
//    public static ReserveConfirmActivity getInstance() {
//        return instance;
//    }

}
