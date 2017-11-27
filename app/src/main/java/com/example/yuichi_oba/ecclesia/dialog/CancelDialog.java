package com.example.yuichi_oba.ecclesia.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.example.yuichi_oba.ecclesia.tools.Util;

/**
 * Created by 5151021 on 2017/11/27.
 */

public class CancelDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Util.easyLog("call CancelDialog->onCreateDialog()");
//        return super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle("予約のキャンセル")
                // メッセージの設定 .setMessage("")
                .setMessage("本当にこの予約をキャンセルしますか？")
                // 肯定的ボタンの設定
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("call", "cancel dialog ok selected!");
                        String re_id = getArguments().getString("re_id");
                        Log.d("call", re_id);


                    }

                })
                .create();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
