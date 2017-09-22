package com.example.yuichi_oba.ecclesia.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.model.Employee;
import com.example.yuichi_oba.ecclesia.tools.DB;
import com.example.yuichi_oba.ecclesia.tools.MyInterface;

import java.util.ArrayList;
import java.util.List;

import static com.example.yuichi_oba.ecclesia.activity.ReserveActivity.member;

public class AddMemberActivity extends AppCompatActivity
        implements View.OnClickListener, MyInterface {

    public static final String SELECT_ADD_HISTORY = "";
    //*** NameConst には、移動しないこと！ ***//
    public static final int OUTEMP_ID = 10;
    public static final int OUTEMP_NAME = 11;
    public static final int OUTEMP_TEL = 12;
    public static final int OUTEMP_MAILADDR = 13;
    public static final int OUTEMP_COM_NAME = 18;
    public static final int OUTEMP_DEP_NAME = 14;
    public static final int OUTEMP_POS_NAME = 15;
    public static final int EMP_ID = 11;
    public static final int EMP_NAME = 12;
    public static final int EMP_TEL = 13;
    public static final int EMP_MAILADDR = 14;
    public static final int EMP_DEP_NAME = 15;
    public static final int EMP_POS_NAME = 16;
    //*** ここまで ***//

    //    EditText ed_depart;
    //    EditText ed_position;
    EditText ed_name;
    Button bt_cancel;
    Button bt_regist;
    EditText ed_company;
    EditText ed_email;
    EditText ed_tel;
    RadioGroup rbn_group;
    Spinner sp_history;
    Spinner sp_position;
    Spinner sp_depart;
    // 会議に参加したことのあるメンバー情報を格納するメンバークラスのリスト
//    List<Member> members = new ArrayList<>();
    private String emp_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("call", "AddMemberActivity->onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        Intent in = getIntent();
        emp_id = in.getStringExtra("emp_id");
        Log.d("call", "emp_id " + emp_id);
        /***
         * 各種Widgetの初期化処理
         */
        init();
        setWidgetListener();

    }
    //*** ラジオボタンでどちらか選択したときの処理を決めるメソッド ***//
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
//            case R.id.bt_addmem_cancel:
//                finish();
//                break;
//            case R.id.bt_addmem_regist:
//                // 選択した参加者をResurveActivityにもっていく
//                Toast.makeText(this, "参加者登録", Toast.LENGTH_SHORT).show();
//                /***
//                 * ここで、新規登録ならば、社外者ファイルへの登録を行う
//                 */
//                // ラジオボタンをみて、新規登録ラジオボタンなら、入力された情報の重複チェックを行う
//
//                // 社外者ファイルへのインサートを行う
//                break;
        }
    }
    //*** 新規登録ラジオボタンを再度選択したとき、再度編集可能にするメソッド ***//
    private void setAgainEditable() {
        // 全Edittextに対して、再編集可能にする
        ed_company.setFocusable(true);
        ed_company.setEnabled(true);
        ed_company.setFocusableInTouchMode(true);
        ed_name.setFocusable(true);
        ed_name.setEnabled(true);
        ed_name.setFocusableInTouchMode(true);
//        ed_depart.setFocusable(true);
//        ed_depart.setEnabled(true);
//        ed_depart.setFocusableInTouchMode(true);
//        ed_position.setFocusable(true);
//        ed_position.setEnabled(true);
//        ed_position.setFocusableInTouchMode(true);
        ed_tel.setFocusable(true);
        ed_tel.setEnabled(true);
        ed_tel.setFocusableInTouchMode(true);
        ed_email.setFocusable(true);
        ed_email.setEnabled(true);
        ed_email.setFocusableInTouchMode(true);
    }
    //*** 各ウィジェットの初期化処理メソッド ***//
    public void init() {
        bt_cancel = (Button) findViewById(R.id.bt_add_cancel);       //  キャンセルボタン
        bt_regist = (Button) findViewById(R.id.bt_add_regist);       //  登録（追加？）ボタン

        ed_company = (EditText) findViewById(R.id.ed_company);      //  会社入力項目
        ed_name = (EditText) findViewById(R.id.ed_add_name);            //  氏名入力項目
        ed_email = (EditText) findViewById(R.id.ed_add_mailaddr);          //  Email入力項目
        ed_tel = (EditText) findViewById(R.id.ed_add_tel);              //  電話入力項目
//        rbn_group = (RadioGroup) findViewById(R.id.rbngroup_addmember); //  ラジオボタングループ
        sp_history = (Spinner) findViewById(R.id.sp_add_history);       //  会社履歴スピナー
        sp_position = (Spinner) findViewById(R.id.sp_add_position);     //  役職スピナー
        sp_depart = (Spinner) findViewById(R.id.sp_add_depart);         //  部署スピナー
        /***
         * 履歴スピナーの各種設定
         */
        // DB 検索して、予約した人の「会社名 ： 参加者苗字」で出す
        // DO: 2017/09/09 参加回数を求めて、上位１０人ずつを出すSQLの実装
        setSpinnerHistory();
        // リスナー登録
        sp_history.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner spinner = (Spinner) adapterView;
                String name = spinner.getSelectedItem().toString().split(":")[1];
                Log.d("call", name);

//                Toast.makeText(AddMemberActivity.this, name, Toast.LENGTH_SHORT).show();

//                for (Member member : members) {
//                    if (name.equals(member.getEmp_name())) {
//                        // 履歴から選択された人間の情報を下の項目群にマッピングする
//                        ed_company.setText(member.getCom_name());
//                        ed_name.setText(member.getEmp_name());
//                        ed_email.setText(member.getEmp_mailaddr());
//                        ed_tel.setText(member.getEmp_tel());
//                        sp_position.setSelection(Util.setSelection(sp_position, member.getPo_name()));
//                        sp_depart.setSelection(Util.setSelection(sp_depart, member.getDep_name()));
//                    }
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        /***
         * 部署スピナーの各種設定
         */
        setSpinnerDepart();


    }
    //*** 各ウィジェットのリスナー登録メソッド ***//
    @Override
    public void setWidgetListener() {
        // TODO: 2017/09/19  登録ボタン押下で、参加者リストを追加するロジックの実装
        //*** 登録ボタン押下時の処理 ***//
        bt_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("call", "add regist");
                //*** 各ウィジェットの情報を基に、参加者のインスタンスを生成 ***//
                Employee e = new Employee();
                e.setId("11111");   // TODO: 2017/09/22 AddMemberのIDをどうするか・・・
                e.setName(ed_name.getText().toString());
                e.setMailaddr(ed_email.getText().toString());
                e.setCom_name(ed_company.getText().toString());
                e.setDep_name(sp_depart.getSelectedItem().toString());
                e.setPos_name(sp_position.getSelectedItem().toString());
                // TODO: 2017/09/22 役職の優先度をどうするのか

                //*** ReserveActivityの参加者リスト(member)にaddする ***//
                member.add(e);
            }
        });

        //*** キャンセルボタン押下時の処理 ***//
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("call", "AddMemberActivity->finish()");
                finish();
            }
        });
    }
    //*** 部署スピナーの項目を動的設定するメソッド ***//
    private void setSpinnerDepart() {
        // ＤＢ検索
        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        // 結果をリストにつなぐ
        Cursor c = db.rawQuery("select * from m_depart", new String[]{});
        List<String> list = new ArrayList<>();
        while (c.moveToNext()) {
            list.add(c.getString(1));
        }
        c.close();
        //  スピナーに設定する
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        sp_depart.setAdapter(adapter);
    }
    //*** 履歴スピナーの項目を動的設定するメソッド ***//
    private void setSpinnerHistory() {
        // DB 検索
        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from v_member", new String[]{});
        // 自分が参加した会議に参加したことのある人間を検索(社内)
        Cursor c = db.rawQuery("select *, count(*) as cnt from v_reserve_member where re_id in (select re_id from t_member where mem_id = ?) " +
                " group by mem_id order by cnt desc limit 10", new String[]{emp_id});
        // メンバークラスのインスタンス生成
        List<String> list = new ArrayList<>();
        while (c.moveToNext()) {
            //*** 社員？（人間）クラスのインスタンスを生成 ***//
            Employee e = new Employee();
            e.setId(c.getString(11));           // ID
            e.setName(c.getString(12));         // 氏名
            e.setTel(c.getString(13));          // 電話番号
            e.setMailaddr(c.getString(14));     // メールアドレス
            e.setCom_name("社内");
            e.setDep_name(c.getString(15));     // 部署名
            e.setPos_name(c.getString(16));     // 役職名
            e.setPos_priority(c.getString(17)); // 役職の優先度

            list.add(e.getCom_name() + " : " + e.getName());
        }
        c.close();
        // 自分が参加した会議に参加したことのある人間を検索(社外)
        c = db.rawQuery("select * from v_reserve_out_member where re_id in (select re_id from t_member where mem_id = ?)", new String[]{emp_id});
        while (c.moveToNext()) {
            //*** 社員(社外者）クラスのインスタンスを生成 ***//
            Employee e = new Employee();
            e.setId(c.getString(10));           // ID
            e.setName(c.getString(11));         // 氏名
            e.setTel(c.getString(12));          // 電話番号
            e.setMailaddr(c.getString(13));     // メールアドレス
            e.setDep_name(c.getString(14));     // 部署名
            e.setPos_name(c.getString(15));     // 役職名
            e.setPos_priority(c.getString(16)); // 役職の優先度
            e.setCom_name(c.getString(18));     // 会社名

            list.add(e.getCom_name() + " : " + e.getName());
        }
        // スピナーに設定する
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        sp_history.setAdapter(adapter);
    }
}
