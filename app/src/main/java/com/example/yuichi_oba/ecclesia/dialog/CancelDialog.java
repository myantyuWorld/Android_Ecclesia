package com.example.yuichi_oba.ecclesia.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import com.example.yuichi_oba.ecclesia.activity.ReserveListActivity;
import com.example.yuichi_oba.ecclesia.tools.MyHelper;
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

                        SQLiteOpenHelper helper = new MyHelper(getContext());
                        SQLiteDatabase db = helper.getWritableDatabase();
                        //*** 該当データの、予約テーブルの削除 ***//
//                        db.execSQL("delete from t_reserve where re_id = ?", new Object[]{re_id});
                        int result = db.delete("t_reserve", "re_id = ?", new String[]{re_id});
                        Log.d("call", "削除件数 : " + result);
                        //*** 該当データの、参加者テーブルの削除 ***//
                        // TODO: 2017/11/27 予約キャンセル後の、参加者テーブルの削除の実装


                        //*** 画面の再描画を行う ***//
                        ReserveListActivity.arl_view_timetableView.reView(ReserveListActivity.employee.getEmp_id(), ReserveListActivity.arl_txt_date.getText().toString());

                    }

                })
                .create();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
