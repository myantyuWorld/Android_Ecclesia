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
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.model.Employee;
import com.example.yuichi_oba.ecclesia.model.OutEmployee;
import com.example.yuichi_oba.ecclesia.model.Person;
import com.example.yuichi_oba.ecclesia.tools.DB;
import com.example.yuichi_oba.ecclesia.tools.MyInterface;
import com.example.yuichi_oba.ecclesia.tools.Util;

import java.util.ArrayList;
import java.util.List;

public class AddMemberActivity extends AppCompatActivity
        implements MyInterface {

    public static class CautionDialog extends DialogFragment {
        String[] str = new String[]{"氏名", "Email", "電話番号"};

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            StringBuilder sb = new StringBuilder();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            if (ed_name.getText().toString().isEmpty()) sb.append(str[0]);
            if (ed_tel.getText().toString().isEmpty()) sb.append(" " + str[1]);
            if (ed_email.getText().toString().isEmpty()) sb.append(" " + str[2]);

            builder.setTitle("警告！").
                    setMessage(String.format("[%s] が 空欄です", sb.toString()))
                    .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                            Log.d("call", "CautionDialog onclick!");
                        }
                    });

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    //*** あんまやりたくないけど、ＳＱＬ書きまくるのたいぎいので！ ***//
    public static class Position {
        public String posId;        //***  ***//
        public String posName;      //***  ***//
        public String posPriority;  //***  ***//
    }

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
    public static final String M_OUT_EMP = "m_out_emp"; //*** 社外者テーブル ***//

    //*** ここまで ***//

    //    EditText ed_depart;
    //    EditText ed_position;
    static EditText ed_name;
    static EditText aam_etxt_company;
    static EditText ed_email;
    static EditText ed_tel;
    Button bt_cancel;
    Button bt_regist;
    RadioGroup rbn_group;
    Spinner sp_history;
    Spinner sp_position;
    Spinner sp_depart;
    // 会議に参加したことのあるメンバー情報を格納するメンバークラスのリスト
    //*** ポリモーフィズム使用 ***//
    List<Person> members = new ArrayList<>();
    private String emp_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Util.easyLog("AddMemberActivity->onCreate() 参加者の追加を行う画面");
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

    /***_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
     *
     *          自作メソッド
     *
     * _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/*/
    //*** --- SELF MADE METHOD --- 新規登録ラジオボタンを再度選択したとき、再度編集可能にするメソッド ***//
    private void setAgainEditable() {
        // 全Edittextに対して、再編集可能にする
        aam_etxt_company.setFocusable(true);
        aam_etxt_company.setEnabled(true);
        aam_etxt_company.setFocusableInTouchMode(true);
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

    //*** --- SELF MADE METHOD --- 各ウィジェットの初期化処理メソッド ***//
    public void init() {
        bt_cancel = (Button) findViewById(R.id.aam_btn_cancel);          //  キャンセルボタン
        bt_regist = (Button) findViewById(R.id.aam_btn_regist);          //  登録（追加？）ボタン
        aam_etxt_company = (EditText) findViewById(R.id.aam_etxt_company);          //  会社入力項目
        ed_name = (EditText) findViewById(R.id.aam_ed_name);            //  氏名入力項目
        ed_email = (EditText) findViewById(R.id.aam_ed_mailaddr);       //  Email入力項目
        ed_tel = (EditText) findViewById(R.id.aam_ed_tel);              //  電話入力項目
        rbn_group = (RadioGroup) findViewById(R.id.aam_rbg_add_member); //  ラジオボタングループ
        sp_history = (Spinner) findViewById(R.id.aam_sp_history);       //  会社履歴スピナー
        sp_position = (Spinner) findViewById(R.id.aam_sp_position);     //  役職スピナー
        sp_depart = (Spinner) findViewById(R.id.aam_sp_depart);         //  部署スピナー
        //*** 履歴スピナーの各種設定 ***//
        setSpinnerHistory();
        //*** 部署スピナーの各種設定 ***//
        setSpinnerDepart();
        //*** 役職スピナーの各種設定 ***//
        setSpinnerPosition();
    }

    //*** --- SELF MADE METHOD --- 各ウィジェットのリスナー登録メソッド ***//
    @Override
    public void setWidgetListener() {
        //*** 履歴スピナーのリスナー ***//
        sp_history.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //*** 履歴スピナー選択時の処理 ***//
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner spinner = (Spinner) adapterView;
                //*** 「会社名」:「氏名」を分割して、氏名のみ取得する ***//
                String name = spinner.getSelectedItem().toString().split(" : ")[1];
                Log.d("call", name);

                //*** 履歴リストから、選択された人間と等しいインスタンスを探す ***//
                for (Person person : members) {
                    if (person.getName().contains(name)) {  //*** 名前が等しい ***//
                        //*** 特定したインスタンス情報で、各ウィジェットにマップする ***//
                        mappingWidget(person);
                        break; //*** これ以上ループする必要がないので、ループを抜ける ***//
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {            }
        });

        //*** キャンセルボタン押下時の処
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("call", "AddMemberActivity->finish()");
                finish();
            }
        });
        //*** ラジオボタングループのリスナー ***//
        rbn_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                switch (radioButton.getId()) {
                    // 履歴検索
                    case R.id.aam_rbt_history:

                        break;
                    // 新規登録
                    case R.id.aam_rbt_new_regist:
                        break;
                }
            }
        });
        //*** 部署スピナーのリスナー ***//
        sp_depart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Spinner s = (Spinner) parent;
//                String depId = Util.returnDepartId(s.getSelectedItem().toString());
//                Log.d("call", String.format("部署ＩＤ : %s", depId));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //*** 役職スピナーのリスナー ***//
        sp_position.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Spinner s = (Spinner) parent;
//                String posId = Util.returnPositionId(s.getSelectedItem().toString());
//                Log.d("call", String.format("役職ＩＤ : %s", posId));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    //*** --- SELF MADE METHOD --- 履歴から選択されたインスタンス情報を、各ウィジェットにマップするメソッド ***//
    private void mappingWidget(Person p) {
        //*** 引数のクラスの型に応じた処理を行う ***//
        if (p instanceof Employee) {            //*** 社員クラスのインスタンスの場合 ***//
            Log.d("call", p.toString());
            aam_etxt_company.setText("社内");         //*** 会社名 ***//
            ed_name.setText(p.getName());       //*** 氏名 ***//
            ed_email.setText(p.getMailaddr());  //*** メールアドレス ***//
            ed_tel.setText(p.getTel());         //*** 電話番号 ***//
            //*** インスタンスの部署ＩＤから、部署名を解決 ***//
            //*** ↓ ***//
            //*** のち、部署名から、スピナーの添え字を解決して、選択する ***//
            sp_depart.setSelection(Util.setSelection(sp_depart, Util.returnDepartName(((Employee) p).getDep_id())));
            //*** インスタンスの役職ＩＤから、役職名を解決 ***//
            //*** ↓ ***//
            //*** のち、役職名から、スピナーの添え字を解決して、選択する ***//
            sp_position.setSelection(Util.setSelection(sp_position, Util.returnPostionName(((Employee) p).getPos_id()).posName));
        } else if (p instanceof OutEmployee) {  //*** 社外者クラスのインスタンスの場合 ***//
            aam_etxt_company.setText(((OutEmployee) p).getCom_name());    //*** 会社名 ***//
            ed_name.setText(p.getName());                           //*** 氏名 ***//
            ed_email.setText(p.getMailaddr());                      //*** メールアドレス ***//
            ed_tel.setText(p.getTel());                             //*** 電話番号 ***//
            //*** 部署名から、スピナーの添え字を解決して、選択する ***//
            sp_depart.setSelection(Util.setSelection(sp_depart, ((OutEmployee) p).getDep_name()));
            //*** 役職名から、スピナーの添え字を解決して、選択する ***//
            sp_position.setSelection(Util.setSelection(sp_position, ((OutEmployee) p).getPos_name()));
        }
    }

    //*** --- SELF MADE METHOD --- 部署スピナーの項目をDB検索して設定するメソッド ***//
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

    //*** --- SELF MADE METHOD --- 役職スピナーの項目をDB検索して設定するメソッド ***//
    private void setSpinnerPosition() {
        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from m_position", null);

        List<String> list = new ArrayList<>();
        while (c.moveToNext()) {
            list.add(c.getString(1));
        }
        c.close();
        // スピナーに設定する
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        sp_position.setAdapter(adapter);
    }

    //*** --- SELF MADE METHOD --- 履歴スピナーの項目を動的設定するメソッド ***//
    private void setSpinnerHistory() {
        // DB 検索
        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from v_member", new String[]{});
        // 自分が参加した会議に参加したことのある人間を検索(社内)
        String sqlArgs = "select *, count(*) as cnt from v_reserve_member x " +
                "inner join m_depart y on x.dep_name = y.dep_name " +
                " where re_id in (select re_id from t_member where mem_id = ?) group by mem_id order by cnt desc limit 10 ";
        Cursor c = db.rawQuery(sqlArgs, new String[]{emp_id});
        // メンバークラスのインスタンス生成
        List<String> list = new ArrayList<>();
        while (c.moveToNext()) {
            //*** [社員]クラスのインスタンスを生成 ***//
            Employee e = new Employee();
            e.setEmp_id(c.getString(11));           // ID
            e.setName(c.getString(12));         // 氏名
            e.setTel(c.getString(13));          // 電話番号
            e.setMailaddr(c.getString(14));     // メールアドレス
            e.setDep_id(c.getString(22));       //*** 部署ＩＤ ***//
            e.setPos_name(c.getString(16));     // 役職名
            e.setPos_priority(c.getString(17)); // 役職の優先度

            Log.d("call", String.format("社員情報 : %s", e.toString()));    //***  ***//

            members.add(e);
//            list.add(e.getCom_name() + " : " + e.getName());
            list.add("社内" + " : " + e.getName());

        }
        c.close();
        // 自分が参加した会議に参加したことのある人間を検索(社外)
        c = db.rawQuery("select * from v_reserve_out_member where re_id in (select re_id from t_member where mem_id = ?)", new String[]{emp_id});
        while (c.moveToNext()) {
//            Employee e = new Employee();
//            e.setId(c.getString(10));           // ID
//            e.setName(c.getString(11));         // 氏名
//            e.setTel(c.getString(12));          // 電話番号
//            e.setMailaddr(c.getString(13));     // メールアドレス
//            e.setDep_name(c.getString(14));     // 部署名
//            e.setPos_name(c.getString(15));     // 役職名
//            e.setPos_priority(c.getString(16)); // 役職の優先度
//            e.setCom_name(c.getString(18));     // 会社名

            //*** [社外者]クラスのインスタンスを生成 ***//
            OutEmployee e = new OutEmployee(
                    c.getString(10),            //*** ID ***//
                    c.getString(11),            //*** 氏名 ***//
                    c.getString(12),            //*** 電話番号 ***//
                    c.getString(13),            //*** メールアドレス ***//
                    c.getString(14),            //*** 部署名 ***//
                    c.getString(15),            //*** 役職名 ***//
                    c.getString(16),            //*** 役職優先度 ***//
                    c.getString(18)             //*** 会社名 ***//
            );

            members.add(e);
            list.add(e.getCom_name() + " : " + e.getName());
        }
        // スピナーに設定する
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        sp_history.setAdapter(adapter);
    }

    //*** --- SELF MADE METHOD --- 登録ボタン押下時の処理 ***//
    public void onClickRegist(View view) {
        Log.d("call", "call onClickRegist()");
        //*** ひとつでもエラーが検出されたら、画面遷移させない ***//
        // TODO: 2017/10/04 同じ人間を追加することを防ぐロジックの実装
        if (!isBrankSpace()) {
            Log.d("call", "参加者追加アクティビティ エラー検出！ 登録はしません！");
            CautionDialog cautionDialog = new CautionDialog();
            cautionDialog.show(getFragmentManager(), "aaaaa");
            return; //*** 処理を抜ける ***//
        }


        //*** 各ウィジェットの情報を基に、参加者のインスタンスを生成 ***//
        Intent intent = new Intent();
        if (aam_etxt_company.getText().toString().contains("")) {     //*** 社内の場合 ***//
            Employee e = new Employee(
                    returnMaxId("t_emp"),                                              //*** 社員テーブルのIDの最大値＋１を代入 ***//
                    ed_name.getText().toString(),                                       //*** 氏名 ***//
                    ed_tel.getText().toString(),                                        //*** 電話番号 ***//
                    ed_email.getText().toString(),                                      //*** メールアドレス ***//
                    Util.returnDepartId(sp_depart.getSelectedItem().toString()),        //*** 部署ID ***//
                    Util.returnPositionId(sp_position.getSelectedItem().toString()).posId     //*** 役職ID ***//
            );

            Log.d("call", e.toString());
            e.setDep_id(sp_depart.getSelectedItem().toString());
            e.setPos_id(sp_position.getSelectedItem().toString());

            intent.putExtra("member", e);   //*** intent に セットする ***//
        } else {                                                //*** 社外者の場合 ***//
            OutEmployee e = new OutEmployee(
                    returnMaxId(M_OUT_EMP),                     //*** 社外者テーブルのIDの最大値＋１を代入 ***//
                    ed_name.getText().toString(),               //*** 氏名 ***//
                    ed_tel.getText().toString(),                //*** 電話番号 ***//
                    ed_email.getText().toString(),              //*** メールアドレス ***//
                    sp_depart.getSelectedItem().toString(),     //*** 部署名 ***//
                    sp_position.getSelectedItem().toString(),   //*** 役職名 ***//
                    "",                                         //*** 役職優先度 ***//
                    aam_etxt_company.getText().toString()             //*** 会社名 ***//
            );
            Log.d("call", e.toString());

            intent.putExtra("member", e);   //*** intent に セットする ***//
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    //*** --- SELF MADE METHOD --- 指定されたテーブルのIDの最大値＋１を返すメソッド ***//
    private String returnMaxId(String tblName) {
        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        String maxId = "";
        Cursor c = db.rawQuery(String.format("SELECT * FROM %s", tblName), null);
        if (c.moveToNext()) {
            maxId = c.getString(0);
        }
        c.close();

        return maxId;
    }

    //*** --- SELF MADE METHOD --- ウィジェットに空欄があるかチェックするメソッド ***//
    private boolean isBrankSpace() {
        //*** 各ウィジェットの空欄を検査 ※ 会社は、空欄の場合は、社内とするので、OKとする（暫定）***//
        if (ed_email.getText().toString().isEmpty() ||      //*** Email ***//
                ed_name.getText().toString().isEmpty() ||   //*** 氏名 ***//
                ed_tel.getText().toString().isEmpty()) {    //*** 電話番号 ***//
            return false;   //*** 異常を返す ***//
        }
        return true;    //*** ブランク無し（正常）を返す ***//
    }
}
