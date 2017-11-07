package com.example.yuichi_oba.ecclesia.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.Employee;
import com.example.yuichi_oba.ecclesia.model.OutEmployee;
import com.example.yuichi_oba.ecclesia.model.Person;
import com.example.yuichi_oba.ecclesia.model.Reserve;
import com.example.yuichi_oba.ecclesia.tools.MyHelper;
import com.example.yuichi_oba.ecclesia.tools.Util;

import java.util.ArrayList;
import java.util.List;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.KEYCHECK;

public class ReserveCheckActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener{
    private static ReserveCheckActivity instance = null;

    //*** 変更情報を入力した後の予約インスタンス ***//
    Reserve checkRes;
    //*** 確定ボタン ***//
    Button button;
    private MyHelper helper = new MyHelper(this);
    public static SQLiteDatabase db;

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
                //*** 時間帯の被りをチェック ***//
//                checkRes.timeDuplicationCheck();
//                MyHelper helper = new MyHelper(getApplicationContext());
//                SQLiteDatabase db = helper.getWritableDatabase();
//                Cursor cursor = db.rawQuery("select * from t_reserve where room_id = ? and re_starttime", new String[]{checkRes.getRe_room_id()});
//                while (cursor.moveToNext()) {
//
//
//                }
                //*** 優先度チェック ***//
//                    checkRes.priorityCheck();
                //*** 追い出し ***//
//                checkRes.eviction();
                //*** 追い出し（DBから削除） ***//
//                db.rawQuery("delete from t_reserve where re_id = ?", new String[]{checkRes.getRe_id()});

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
    //*** 実際にDBの予約情報を書き換える ***//
    public void reserveChange() {

        int sum = 0;
        //*** 参加者の優先度の合計を算出する ***//
        for (Person person : checkRes.getRe_member()) {
            if (person instanceof Employee) {
                //*** 社員クラスであれば社員でキャストし優先度を取得 ***//
                sum += Integer.valueOf(((Employee) person).getPos_priority());
            } else if (person instanceof OutEmployee) {
                //*** 社外者クラスであれば社外者でキャストし優先度を取得 ***//
                sum += Integer.valueOf(((OutEmployee) person).getPos_priority());
            }
        }
        //*** 参加者の優先度の平均をセッターでセット ***//
        checkRes.setRe_mem_priority(sum / checkRes.getRe_member().size());


        //*** 必要なインスタンスを用意 ***//
        SQLiteDatabase db = helper.getWritableDatabase();
        //*** トランザクション開始 ***//
//        db.beginTransaction();

        //*** SQLでアップデートかける ***//
        db.execSQL("update t_reserve set re_overview = ? , re_startday = ?, re_endday = ?, re_starttime = ?, re_endtime = ?," +
                " re_switch = ?, re_fixture = ?, re_remarks = ?, re_priority = ?, room_id = ?, pur_id = ?" +
                " where re_id = ? ", new Object[]{checkRes.getRe_name(), checkRes.getRe_startDay(), checkRes.getRe_endDay(), checkRes.getRe_startTime(),
                checkRes.getRe_endTime(), checkRes.getRe_switch(), checkRes.getRe_fixtures(), checkRes.getRe_remarks(), checkRes.getRe_mem_priority(), checkRes.getRe_room_id()
                , checkRes.getRe_purpose_id(), checkRes.getRe_id()});

        checkRes.getRe_member().forEach(person -> {
            if (person instanceof Employee) {
                db.execSQL("replace into t_member values(?, ?) ", new Object[]{checkRes.getRe_id(), Util.returnEmpId(person.getName())});
            } else {
                db.execSQL("replace into t_member values(?, ?)", new Object[]{checkRes.getRe_id(), Util.returnOutEmpId(person.getName())});
            }
        });
        //*** コミットをかける ***//
//        db.setTransactionSuccessful();
        //*** トランザクション終了 ***//
//        db.endTransaction();

        //*** 変更完了ダイアログ ***//
//        ReserveConfirmActivity.ResultDialog resultDialog = new ReserveConfirmActivity.ResultDialog();
//        Bundle bundle = new Bundle();
//        bundle.putString("result", "change");
//        resultDialog.setArguments(bundle);
//        resultDialog.show(getFragmentManager(), "change");
        ChangeResultDialog changeResultDialog = new ChangeResultDialog();
        changeResultDialog.show(getFragmentManager(), "change");

        //*** 予約一覧へ画面遷移を行う ***//
        Intent intent = new Intent(getApplicationContext(), ReserveListActivity.class);
        startActivity(intent);
    }

    public static ReserveCheckActivity getInstance() {
        return instance;
    }

    public void onClickMemConfirm(View view) {
        //*** 参加者一覧ダイアログを表示する ***//
//        ReserveConfirmActivity.MemberConfirmDialog dialog = new ReserveConfirmActivity.MemberConfirmDialog();
//        dialog.show(getFragmentManager(), "confirm_a");
        Bundle bundle = new Bundle();
        bundle.putSerializable("checkRes", checkRes);
        ChangeMemberConfirmDialog dialog = new ChangeMemberConfirmDialog();
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "member");
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

    public static class ChangeMemberConfirmDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            CharSequence[] items;
            List<String> list = new ArrayList<>();

            //*** 本処理から渡された予約クラスを取得 ***//
            Reserve memberRes = (Reserve) getArguments().getSerializable("checkRes");

            //*** メンバーのループ ***//
            for (Person person : memberRes.getRe_member()) {
                if (person instanceof Employee) {
                    list.add(String.format("社内 : %s", person.getName()));
                } else if (person instanceof OutEmployee) {
                    list.add(String.format("%s : %s", ((OutEmployee) person).getCom_name(), person.getName()));     //***  ***//
                }
            }
            items = (CharSequence[]) list.toArray(new CharSequence[list.size()]);
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
    }
}
