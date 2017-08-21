package com.example.yuichi_oba.ecclesia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.ReserveInfo;

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/
// _/_/ 予約状況(リストで視覚的にわかりやすい）を表示するアクティビティ
// _/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
public class ReserveListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    /***
     * デバッグ用
     */
    private static final String TAG = ReserveListActivity.class.getSimpleName();
    public static final String RESERVE_INFO = "reserve_info";
    /***
     * ここまで
     */

    Button bt_reserve;          // 予約ボタン
    Spinner spinner;            // 予約IDのスピナー
    ReserveInfo reserveInfo;    // 予約情報記録クラスの変数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /***
         * レイアウト情報をマッピングする
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        // 予約情報クラスのインスタンス生成
        reserveInfo = new ReserveInfo();

        // 各ウィジェットの初期化処理
        bt_reserve = (Button) findViewById(R.id.bt_reserve);
        spinner = (Spinner) findViewById(R.id.spinner2);
        // リスナー登録
        bt_reserve.setOnClickListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // スピナーのアイテムを選択したときの処理
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(ReserveListActivity.this, "test", Toast.LENGTH_SHORT).show();
                Spinner s = (Spinner) adapterView;
                String re_id = s.getSelectedItem().toString();
                Log.d(TAG, re_id);
                reserveInfo.setRe_id(re_id);

                Intent intent = new Intent(getApplicationContext(), ReserveConfirmActivity.class);
                intent.putExtra(RESERVE_INFO, reserveInfo);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // なにもしない
            }
        });


    }

    // ようわからん(笑) ＝＝＞ HCPには書かんでいいよ
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
        // 選択したナビのIDを取得し、idに代入する
        int id = item.getItemId();

        // Intentクラスの変数を宣言し、nullで初期化
        Intent intent = null;
        // idで処理を分ける
        switch (id) {
            // 「履歴検索」が選択されたとき
            case R.id.nav_rireki:
                // Intentクラスのインスタンス生成し、画面遷移させる
                intent = new Intent(getApplicationContext(), HistorySearchActivity.class);
                startActivity(intent);
                break;
            // 「管理者認証」が選択されたとき
            case R.id.nav_admin_auth:
                // 管理者認証ダイアログを生成する
                AuthDialog authDialog = new AuthDialog();
                authDialog.show(getFragmentManager(), "aaa");
                break;

        }

        // ようわからん(笑) ＝＝＞ HCPには書かんでいいよ
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /***
     * ボタンクリック時の処理
     * @param view
     */
    @Override
    public void onClick(View view) {
        // ビューから、どのボタンが押されたかIDを取得する
        int id = view.getId();
        Intent intent;
        // idで処理を分ける
        switch (id) {
            // 「予約」ボタン押下時
            case R.id.bt_reserve:
                // Intentクラスのインスタンス生成し、画面遷移させる
                intent = new Intent(getApplicationContext(), ReserveActivity.class);
                startActivity(intent);
                break;
        }
    }
}
