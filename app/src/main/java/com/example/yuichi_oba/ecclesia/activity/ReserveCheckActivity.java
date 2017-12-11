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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.AdminLogOut;
import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;
import com.example.yuichi_oba.ecclesia.model.Employee;
import com.example.yuichi_oba.ecclesia.model.OutEmployee;
import com.example.yuichi_oba.ecclesia.model.Person;
import com.example.yuichi_oba.ecclesia.model.Reserve;
import com.example.yuichi_oba.ecclesia.tools.MyHelper;
import com.example.yuichi_oba.ecclesia.tools.Util;

import java.util.ArrayList;
import java.util.List;

import static com.example.yuichi_oba.ecclesia.activity.ReserveListActivity.authFlg;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.COMPLETE;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.FALSE;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.KEYCHANGE;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.KEYCHECK;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.KEYEVITARGET;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.OK;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.RESERVECHANGE;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.RUNMESSAGE;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.TRUE;

public class ReserveCheckActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener{
    private static ReserveCheckActivity instance = null;

    //*** 追い出しが発生している場合に、追い出す会議IDのIntentを受け取る。なければ"false" ***//
    String eviTarget;

    //*** 変更情報を入力した後の予約インスタンス ***//
    Reserve checkRes;
    //*** 確定ボタン ***//
    Button button;
    private MyHelper helper = new MyHelper(this);
    public static SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //*** 管理者認証済みだったら、テーマを変更する ***//
        if (Util.isAuthAdmin(authFlg)) {
            setTheme(R.style.SecondTheme);
        }
        setContentView(R.layout.activity_reserve_check);

        instance = this;

        //*** 変更箇所入力後の予約インスタンスを受け取る ***//
        checkRes = (Reserve) getIntent().getSerializableExtra(KEYCHECK);
        //*** 時間重複チェックの結果を受け取る ***//
        eviTarget = getIntent().getStringExtra(KEYEVITARGET);

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
                //*** 追い出しがあった場合はここで追い出す ***//
                //*** 優先度による予約不可メッセージはChangeで ***//
                if (!eviTarget.equals(FALSE) && !eviTarget.equals(TRUE)) {
                    checkRes.eviction(eviTarget);
                }
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
            //*** 「管理者ログアウト」が選択されたとき ***//
            case R.id.nav_admin_logout:
                AdminLogOut adminLogOut = new AdminLogOut();
                adminLogOut.show(getFragmentManager(), "adminLogOut");

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
        ChangeResultDialog changeResultDialog = new ChangeResultDialog();
        changeResultDialog.show(getFragmentManager(), KEYCHANGE);

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
            return new AlertDialog.Builder(getActivity()).setTitle(RESERVECHANGE + COMPLETE)
                    .setMessage(RESERVECHANGE + RUNMESSAGE).setPositiveButton(OK, new DialogInterface.OnClickListener() {
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

    private float setReserveDetail() {
        //*** 会議目的IDをセットする ***//
        String purName = checkRes.getRe_purpose_name();  //***  ***//
        String purId = Util.returnPurposeId(purName);   //*** 会議目的名を ***//
        checkRes.setRe_purpose_id(purId);


        //*** 会議参加者の優先度を計算する ***//
        Integer sumPriority = 0;

        //*** 参加者の優先度の合計を算出する ***//
        for (Person p : checkRes.getRe_member()) {
            if (p instanceof Employee) {                //*** 社員クラス ***//
                //***  ***//
                sumPriority += Integer.valueOf(((Employee) p).getPos_priority());
            } else if (p instanceof OutEmployee) {      //*** 社外者クラス ***//
                //***  ***//
                sumPriority += Integer.valueOf(((OutEmployee) p).getPos_priority());
            }
        }
        //*** 参加者の優先度合計の平均を算出してその値を返す ***//
        return sumPriority / checkRes.getRe_member().size();
    }
}
