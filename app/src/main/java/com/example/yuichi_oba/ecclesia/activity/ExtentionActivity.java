package com.example.yuichi_oba.ecclesia.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yuichi_oba.ecclesia.R;

public class ExtentionActivity extends AppCompatActivity
    implements View.OnClickListener{

    /***
     *  延長アクティビティで使用するダイアログクラス
     */
    public static class ExtensionDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("延長確認")
                    .setMessage("延長を実行しますか？")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getActivity(), "延長の実行！", Toast.LENGTH_SHORT).show();
                            /***
                             * 延長情報をDBに書き込む
                             */

                            /***
                             * 画面をころして、予約一覧画面に遷移する
                             */
                            Intent intent = new Intent(getActivity(), ReserveListActivity.class);
                            startActivity(intent);
                            dismiss();
                        }
                    })
                    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getActivity(), "キャンセル", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    })
                    .create();
        }
    }

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


//        finish();
//        Intent intent = new Intent(getApplicationContext(), ReserveListActivity.class);
//        startActivity(intent);
    }
}
