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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.ReserveInfo;
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

    // デバッグ用
    private static final String TAG = ReserveConfirmActivity.class.getSimpleName();

    TextView txt_overview;              // 概要
    TextView txt_purpose;               // 会議目的
//    TextView txt_startDay;              // 開始日
//    TextView txt_endday;                // 終了日
    TextView txt_startTime;             // 開始時刻
    TextView txt_endTime;               // 終了時刻
    TextView txt_applicant;             // 予約者
    TextView txt_inOutHouse;            // 社外社内区分
    TextView txt_conferenceRoom;        // 使用会議室
    TextView txt_fixtures;              // 備品？
    TextView txt_remarks;               // 備考
    TextView txt_member;                // 会議参加者を表示するスピナー // DO: 2017/07/26 これは、ダイアログでいい？？

    static ReserveInfo reserveInfo;     // 予約情報クラスの変数

    /***
     * 会議参加者をリスト形式で出す、ダイアログフラグメントクラス
     */
    private static class MemberConfirmDialog extends DialogFragment {
        // ダイアログを生成するメソッド
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // 会議参加者データ
            CharSequence[] items = reserveInfo.getRe_member().toArray(new CharSequence[reserveInfo.getRe_member().size()]);

            return new AlertDialog.Builder(getActivity())
                    .setTitle("会議参加者一覧")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .create();
        }

        // ダイアログを破棄するメソッドーー＞HCP不要
        @Override
        public void onPause() {
            super.onPause();
            dismiss();
        }
    }

    /***
     * 「早期退出」オプション選択時の ダイアログフラグメントクラス
     */
    private static class EarlyOutDialog extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("早期退出")
                    .setMessage("この会議を早期退出しますか？")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // OK選択で、早期退出ロジックスタート！
                            Toast.makeText(getActivity(), "早期退出", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 特に何もしない
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        // 各ウィジェットの初期化処理メソッド
        init();
        // 遷移前の画面から、オブジェクトを受け取る
        reserveInfo = (ReserveInfo) getIntent().getSerializableExtra("reserve_info");
        // 予約詳細をDB検索して、画面にマッピングするメソッド
        dbSearchReserveConfirm();
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
        // 選択されたオプションのIDを取得し、代入する
        int id = item.getItemId();

        Intent intent;
        // idによって処理を分ける
        switch (id) {
            // 「早期退出」が選択された
            case R.id.option_earlyOut:
//                Toast.makeText(this, "早期退出", Toast.LENGTH_SHORT).show();
                EarlyOutDialog earlyOutDialog = new EarlyOutDialog();
                earlyOutDialog.show(getFragmentManager(), "ddd");
                break;
            // 「予約変更」が選択された
            case R.id.option_reserveChange:
                intent = new Intent(getApplicationContect(), ReserveConfirmActivity.class);
                intent.putExtra("Change", reserveInfo);
                startActiviti(intent);
//                Toast.makeText(this, "予約変更", Toast.LENGTH_SHORT).show();
                // 予約情報インスタンスを次の画面にオブジェクト渡しする
                break;
            // 「延長」が選択された
            case R.id.option_extention:
                intent = new Intent(getApplicationContext(), ExtentionActivity.class);
                intent.putExtra("EX", reserveInfo);
                startActivity(intent);
                break;
        }
        // 選択された結果（項目）を返す
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

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // _/_/
    // _/_/ SELF MADE METHOD
    // _/_/
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/

    /***
     * // インスタンスと、画面情報をマッピングする
     */
    private void setWidgetInfo() {
        txt_overview.setText(reserveInfo.getRe_overview());                 // 項目「概要」に予約情報の概要を設定する(以下同様
        txt_purpose.setText(reserveInfo.getRe_purpose());                   // 目的
//        txt_startDay.setText(reserveInfo.getRe_startDay());                 // 開始日
        txt_startTime.setText(reserveInfo.getRe_startTime());               // 開始時刻
//        txt_endday.setText(reserveInfo.getRe_endDay());                     // 終了日
        txt_endTime.setText(reserveInfo.getRe_endTime());                   // 終了時刻
        txt_applicant.setText(reserveInfo.getRe_rePerson());                // 予約者
        txt_conferenceRoom.setText(reserveInfo.getRe_conference_room());    // 会議室
    }

    /***
     * // 予約情報クラスのインスタンスに、ＤＢ検索した結果をセットする
     *
     * @param c
     */
    private void setReserveInfo(Cursor c) {
        reserveInfo.setRe_overview("aaaaaa");               // 予約情報クラスのインスタンスに、概要をセットする(以下同様
        reserveInfo.setRe_purpose(c.getString(NINE));          // 会議目的名
//        reserveInfo.setRe_startDay();                     開始日は現在ビューにないので後に追記か、代替案が必要
//        reserveInfo.setRe_endDay();                       同上
        reserveInfo.setRe_startTime(c.getString(SIX));        // 開始時刻
        reserveInfo.setRe_endTime(c.getString(SEVEN));          // 終了時刻
        reserveInfo.setRe_rePerson(c.getString(TWO));         // 予約者
        reserveInfo.setRe_conference_room(c.getString(EREVEN)); // 会議室名
    }

    /***
     * 画面の各ウィジェットの初期化処理
     */
    private void init() {
        txt_overview = (TextView) findViewById(R.id.txt_rd_overView);       // 「概要」テキストビューを取得(以下同様
        txt_purpose = (TextView) findViewById(R.id.txt_rd_purpose);         // 会議目的名
//        txt_startDay = (TextView) findViewById(R.id.cre_startDay);          // 開始日
        txt_startTime = (TextView) findViewById(R.id.txt_rd_startTime);     // 開始時刻
//        txt_endday = (TextView) findViewById(R.id.txt_rd_endDay);           // 終了日
        txt_endTime = (TextView) findViewById(R.id.txt_rd_endTime);         // 終了時刻
        txt_applicant = (TextView) findViewById(R.id.txt_rd_applicant);     // 予約者
        txt_inOutHouse = (TextView) findViewById(R.id.txt_rd_inOutHouse);   // 社内社外区分
        txt_conferenceRoom = (TextView) findViewById(R.id.txt_rd_room);     // 会議室名
        txt_fixtures = (TextView) findViewById(R.id.txt_rd_fixtures);       // 備品
        txt_remarks = (TextView) findViewById(R.id.txt_rd_remarks);         // 備考
        txt_member = (TextView) findViewById(R.id.txt_member);              // 参加者
        // 「参加者」テキストにリスナー登録
        txt_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(ReserveConfirmActivity.this, "test", Toast.LENGTH_SHORT).show();
                // 会議参加者ダイアログを生成する
                MemberConfirmDialog memberConfirmDialog = new MemberConfirmDialog();
                memberConfirmDialog.show(getFragmentManager(), "ccc");
            }
        });

    }

    /***
     * 予約詳細をDB検索して、画面へ反映させるメソッド
     */
    private void dbSearchReserveConfirm() {
        // 予約情報クラスのインスタンスから、予約情報詳細をＤＢ検索して、画面にマッピングする
        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from v_reserve where re_id = ?", new String[]{reserveInfo.getRe_id()});
        // 検索結果が存在する
        if (c.moveToNext()) {
            // 予約情報クラスのインスタンスに、ＤＢ検索した結果をセットする
            setReserveInfo(c);
            // インスタンスと、画面情報をマッピングする
            setWidgetInfo();
        }

        c = db.rawQuery("select * from v_member where re_id = ?", new String[]{reserveInfo.getRe_id()});
        List<String> list = new ArrayList<>();
        // 検索結果の最後まで繰り返す
        while (c.moveToNext()) {
            // HCP不要
//            Log.d(TAG, "setReserveInfo: " + c.getString(2));
//            String member = String.format("%s : %s", c.getString(2), c.getString(6));
            // フォーマットをかけた文字列を生成し、リストに追加する
            list.add(String.format("%s : %s", c.getString(2), c.getString(6)));
        }
        // 次に、会議参加者をDB検索する、予約情報クラスのインスタンスに会議参加者情報をセットする
        reserveInfo.setRe_member(list);
    }


}
