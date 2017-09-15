package com.example.yuichi_oba.ecclesia.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.yuichi_oba.ecclesia.R;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.EX;

/**
 * 延長画面をダイアログにしたらどうなるか.
 */

public class ExtentionDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.extention_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle(EX)
                .setView(layout)
                .setPositiveButton(EX, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
    }

    @Override
    public void onPause() {
        super.onPause();

        dismiss();
    }

    public void dbInsertExtension() {
//        ContentValues con = new ContentValues();
//        con.put("re_id", reserveInfo.getRe_id());
//        con.put("ex_startday", reserveInfo.getRe_startDay());
//        con.put("ex_endday", reserveInfo.getRe_endDay());
//        con.put("ex_starttime", reserveInfo.getRe_startTime());
//        con.put("ex_endtime", reserveInfo.getRe_endTime());
//        SQLiteOpenHelper helper = new DB(getApplicationContext());
//        SQLiteDatabase db = helper.getWritableDatabase();
//        if (db.insert("t_extension", null, con) > ZERO) {
//        }
    }
}
