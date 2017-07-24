package com.example.yuichi_oba.ecclesia.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.tools.DB;
import com.example.yuichi_oba.ecclesia.tools.Util;

import java.util.ArrayList;
import java.util.List;

public class AddMemberActivity extends AppCompatActivity
        implements View.OnClickListener {

    public static final String SELECT_ADD_HISTORY = "select emp_id,emp_name,emp_tel,emp_mailaddr,com_name,dep_name,po_name,count(*) as count from v_member group by emp_id order by count desc limit 10";

    class Member {
        private String emp_id;
        private String emp_name;
        private String emp_tel;
        private String emp_mailaddr;
        private String com_name;
        private String dep_name;
        private String po_name;
        private int count;

        public Member(String emp_id, String emp_name, String emp_tel, String emp_mailaddr, String com_name, String dep_name, String po_name, int count) {
            this.emp_id = emp_id;
            this.emp_name = emp_name;
            this.emp_tel = emp_tel;
            this.emp_mailaddr = emp_mailaddr;
            this.com_name = com_name;
            this.dep_name = dep_name;
            this.po_name = po_name;
            this.count = count;
        }

        public int getCount() {
            return count;
        }
        public String getEmp_tel() {
            return emp_tel;
        }
        public String getEmp_mailaddr() {
            return emp_mailaddr;
        }
        public String getEmp_id() {
            return emp_id;
        }
        public String getEmp_name() {
            return emp_name;
        }
        public String getCom_name() {
            return com_name;
        }
        public String getDep_name() {
            return dep_name;
        }
        public String getPo_name() {
            return po_name;
        }

        public void setCount(int count) {
            this.count = count;
        }
        public void setEmp_tel(String emp_tel) {
            this.emp_tel = emp_tel;
        }
        public void setEmp_mailaddr(String emp_mailaddr) {
            this.emp_mailaddr = emp_mailaddr;
        }
        public void setEmp_id(String emp_id) {
            this.emp_id = emp_id;
        }
        public void setEmp_name(String emp_name) {
            this.emp_name = emp_name;
        }
        public void setCom_name(String com_name) {
            this.com_name = com_name;
        }
        public void setDep_name(String dep_name) {
            this.dep_name = dep_name;
        }
        public void setPo_name(String po_name) {
            this.po_name = po_name;
        }
    }

    Button bt_cancel;
    Button bt_regist;
    EditText ed_company;
//    EditText ed_depart;
//    EditText ed_position;
    EditText ed_name;
    EditText ed_email;
    EditText ed_tel;
    RadioGroup rbn_group;
    Spinner sp_history;
    Spinner sp_position;
    Spinner sp_depart;
    // 会議に参加したことのあるメンバー情報を格納するメンバークラスのリスト
    List<Member> members = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);


        /***
         * 各種Widgetの初期化処理
         */
        init();
        /***
         * キャンセル・登録ボタンのリスナー登録
         */
        bt_cancel.setOnClickListener(this);
        bt_regist.setOnClickListener(this);
        /***
         * EditTextへの入力監視リスナー登録
         */
        //  「会社」
        ed_company.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Toast.makeText(AddMemberActivity.this, "会社入力し終わった", Toast.LENGTH_SHORT).show();
            }
        });
        //  「氏名」
        ed_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Toast.makeText(AddMemberActivity.this, "氏名入力し終わった", Toast.LENGTH_SHORT).show();

            }
        });
        /***
         * ラジオグループのリスナー登録
         */
        rbn_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton rbn = (RadioButton) radioGroup.findViewById(checkedId);
                Toast.makeText(AddMemberActivity.this,
                        String.format("[%s]が選択されました", rbn.getText()),
                        Toast.LENGTH_SHORT).show();
                switch (checkedId) {
                    case R.id.rbn_add_history:      // 履歴から選択
                        // 各項目入力できないようにする
                        ed_company.setFocusable(false);
                        ed_name.setFocusable(false);
//                        ed_depart.setFocusable(false);
//                        ed_position.setFocusable(false);
                        ed_tel.setFocusable(false);
                        ed_email.setFocusable(false);
                        sp_history.setEnabled(true);
                        break;
                    case R.id.rbn_add_newregist:    // 新規登録
                        // 再度編集可能にするメソッドをcall
                        setAgainEditable();
                        sp_history.setEnabled(false);
                        break;
                }
            }
        });
        /***
         * 初期のボタンの活性/非活性設定
         */
        sp_history.setEnabled(false);
        ed_company.setText("");
        ed_name.setText("");
        ed_email.setText("");
        ed_tel.setText("");
    }

    /***
     * 各ボタン押下時の処理
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.bt_addmem_cancel:
                finish();
                break;
            case R.id.bt_addmem_regist:
                // 選択した参加者をResurveActivityにもっていく
                Toast.makeText(this, "参加者登録", Toast.LENGTH_SHORT).show();
                /***
                 * ここで、新規登録ならば、社外者ファイルへの登録を行う
                 */
                // ラジオボタンをみて、新規登録ラジオボタンなら、入力された情報の重複チェックを行う

                // 社外者ファイルへのインサートを行う
                break;
        }
    }
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // _/
    // _/           自作メソッド
    // _/
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    /***
     * 新規登録を再度選択したとき、再度編集可能にするためのメソッド
     */
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

    /***
     * 各種Widgetの初期化処理メソッド
     */
    private void init() {
        bt_cancel = (Button) findViewById(R.id.bt_addmem_cancel);       //  キャンセルボタン
        bt_regist = (Button) findViewById(R.id.bt_addmem_regist);       //  登録（追加？）ボタン
        ed_company = (EditText) findViewById(R.id.ed_add_company);      //  会社入力項目
        ed_name = (EditText) findViewById(R.id.ed_add_name);            //  氏名入力項目
//        ed_depart = (EditText) findViewById(R.id.ed_add_depart);
//        ed_position = (EditText) findViewById(R.id.ed_add_position);
        ed_email = (EditText) findViewById(R.id.ed_add_email);          //  Email入力項目
        ed_tel = (EditText) findViewById(R.id.ed_add_tel);              //  電話入力項目
        rbn_group = (RadioGroup) findViewById(R.id.rbngroup_addmember); //  ラジオボタングループ
        sp_history = (Spinner) findViewById(R.id.sp_add_history);       //  会社履歴スピナー
        sp_position = (Spinner) findViewById(R.id.sp_add_potision);     //  役職スピナー
        sp_depart = (Spinner) findViewById(R.id.sp_add_depart);         //  部署スピナー
        /***
         * 履歴スピナーの各種設定
         */
        // DB 検索して、予約した人の「会社名 ： 予約者苗字」で出す
        setSpinnerHistory();
        // リスナー登録
        sp_history.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner spinner = (Spinner) adapterView;
                String name = spinner.getSelectedItem().toString().split(":")[0];
//                Toast.makeText(AddMemberActivity.this, name, Toast.LENGTH_SHORT).show();

                for (Member member : members) {
                    if (name.equals(member.getEmp_name()) ) {
                        // 履歴から選択された人間の情報を下の項目群にマッピングする
                        ed_company.setText(member.getCom_name());
                        ed_name.setText(member.getEmp_name());
                        ed_email.setText(member.getEmp_mailaddr());
                        ed_tel.setText(member.getEmp_tel());
                        sp_position.setSelection(Util.setSelection(sp_position, member.getPo_name()));
                        sp_depart.setSelection(Util.setSelection(sp_depart, member.getDep_name()));
                    }
                }
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

    /***
     * 部署スピナーの項目を動的設定するメソッド
     */
    private void setSpinnerDepart() {
        // ＤＢ検索
        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        // 結果をリストにつなぐ
        Cursor c = db.rawQuery("select * from m_department", new String[]{});
        List<String> list = new ArrayList<>();
        while (c.moveToNext()) {
            list.add(c.getString(1));
        }
        //  スピナーに設定する
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        sp_depart.setAdapter(adapter);
    }

    /***
     * 履歴スピナーの項目を動的設定するメソッド
     */
    private void setSpinnerHistory() {
        // DB 検索
        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from v_member", new String[]{});
        Cursor c = db.rawQuery(
                SELECT_ADD_HISTORY,
                new String[]{});

        // メンバークラスのインスタンス生成
        List<String> list = new ArrayList<>();
        while (c.moveToNext()) {
            Member m = new Member(
                    c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getShort(7)
            );
            members.add(m);
            list.add(c.getString(1) +  ":" + c.getString(4));
        }
        // スピナーに設定する
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        sp_history.setAdapter(adapter);
    }
}
