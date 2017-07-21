package com.example.yuichi_oba.ecclesia.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.dialog.ExtensionDialog;

public class ExtentionActivity extends AppCompatActivity
    implements View.OnClickListener{

    Button bt_extension;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extention);

        bt_extension = (Button) findViewById(R.id.bt_ex_extention);
        bt_extension.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        ExtensionDialog dialog = new ExtensionDialog();
        dialog.show(getFragmentManager(), "bbb");
    }
}
