package com.example.yuichi_oba.ecclesia.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yuichi_oba.ecclesia.R;

public class AddMemberActivity extends AppCompatActivity
        implements View.OnClickListener {

    Button bt_cancel;
    Button bt_regist;
    EditText ed_company;
    EditText ed_depart;
    EditText ed_position;
    EditText ed_name;
    EditText ed_email;
    EditText ed_tel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        bt_cancel = (Button) findViewById(R.id.bt_addmem_cancel);
        bt_cancel.setOnClickListener(this);
        bt_regist = (Button) findViewById(R.id.bt_addmem_regist);
        bt_regist.setOnClickListener(this);

        /***
         * EditTextへの入力監視リスナー登録
         */
        //
        //  「会社」
        //
        ed_company = (EditText) findViewById(R.id.ed_add_company);
        ed_company.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            // 入力し終わった最後にこのメソッドがcall
            @Override
            public void afterTextChanged(Editable editable) {
                Toast.makeText(AddMemberActivity.this, "会社入力し終わった", Toast.LENGTH_SHORT).show();
            }
        });
        //
        //  「氏名」
        //
        ed_name = (EditText) findViewById(R.id.ed_add_name);
        ed_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            // 入力し終わった最後にこのメソッドがcall
            @Override
            public void afterTextChanged(Editable editable) {
                Toast.makeText(AddMemberActivity.this, "氏名が入力し終わった", Toast.LENGTH_SHORT).show();
            }
        });
        //
        //  「部署」
        //
        ed_depart = (EditText) findViewById(R.id.ed_add_depart);

        //
        //  「役職」
        //
        ed_position = (EditText) findViewById(R.id.ed_add_position);


        //
        //  「Email」
        //
        ed_email = (EditText) findViewById(R.id.ed_add_email);

        //
        //  「TEL」
        //


    }

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
                break;
        }
    }
}
