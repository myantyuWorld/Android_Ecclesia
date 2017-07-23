package com.example.yuichi_oba.ecclesia.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yuichi_oba.ecclesia.R;

public class AddMemberActivity extends AppCompatActivity
        implements View.OnClickListener {

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
        bt_cancel = (Button) findViewById(R.id.bt_addmem_cancel);
        bt_regist = (Button) findViewById(R.id.bt_addmem_regist);
        ed_company = (EditText) findViewById(R.id.ed_add_company);
        ed_name = (EditText) findViewById(R.id.ed_add_name);
//        ed_depart = (EditText) findViewById(R.id.ed_add_depart);
//        ed_position = (EditText) findViewById(R.id.ed_add_position);
        ed_email = (EditText) findViewById(R.id.ed_add_email);
        ed_tel = (EditText) findViewById(R.id.ed_add_tel);
        rbn_group = (RadioGroup) findViewById(R.id.rbngroup_addmember);
        sp_history = (Spinner) findViewById(R.id.sp_add_history);
    }
}
