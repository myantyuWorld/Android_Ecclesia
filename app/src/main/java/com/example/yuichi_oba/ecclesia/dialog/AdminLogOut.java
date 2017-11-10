package com.example.yuichi_oba.ecclesia.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.yuichi_oba.ecclesia.activity.ReserveListActivity;

import static com.example.yuichi_oba.ecclesia.activity.ReserveListActivity.authFlg;

/**
 * Created by Yuichi-Oba on 2017/11/10.
 */

public class AdminLogOut extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle("確認")
                .setMessage("ログアウトしますか？")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //*** OK押されたら、管理者認証フラグを”０”（オフにする） ***//
                        authFlg = "0";
                        //*** 予約一覧画面に遷移させる（テーマカラーを変えるために、onCreate()呼ぶ必要があるから） ***//
                        Intent intent = new Intent(ReserveListActivity.getInstance().getApplicationContext(), ReserveListActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;   //*** 何もしない ***//
                    }
                })
                .create();
    }

}
