package com.example.yuichi_oba.ecclesia.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.yuichi_oba.ecclesia.R;

public class AddMemberActivity extends AppCompatActivity
    implements View.OnClickListener{

    Button bt_cancel;
    Button bt_regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        bt_cancel = (Button) findViewById(R.id.bt_addmem_cancel);
        bt_cancel.setOnClickListener(this);
        bt_regist = (Button) findViewById(R.id.bt_addmem_regist);
        bt_regist.setOnClickListener(this);
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
                break;
        }
    }
}
