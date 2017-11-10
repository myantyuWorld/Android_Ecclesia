package com.example.yuichi_oba.ecclesia.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.tools.MyHelper;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.*;

/**
 * Created by Yuichi-Oba on 2017/07/21.
 */

public class AuthDialog extends DialogFragment {

    /***
     * 管理者認証用ダイアログ
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_auth, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle("")
                .setView(layout)
                .setPositiveButton("認証", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(CALL, "認証");
                        EditText id = (EditText) layout.findViewById(R.id.dia_empId);
                        EditText pass = (EditText) layout.findViewById(R.id.dia_empPass);

                        Log.d(CALL, id.getText().toString() + " : " + pass.getText().toString());
                        /***
                         * ここで、管理者認証を行い、良ければ、管理者画面に遷移するといっても、管理者画面はなし・・・
                         */
                        SQLiteOpenHelper helper = new MyHelper(getContext());
                        SQLiteDatabase db = helper.getReadableDatabase();
                        Cursor c = db.rawQuery("select * from m_admin where admin_id = ? and admin_pass = ?",
                                new String[]{id.getText().toString(), pass.getText().toString()});
                        if (c.moveToNext()) {
                            // ログイン成功
                            Log.d(CALL, "ログイン成功");
                            // TODO: 2017/10/04 ログイン成功で、アプリのテーマを変更するロジックの実装  基本は青だから、逆の赤系？
                        }
                        // ログイン失敗
                        else {
                            ;   // TODO: 2017/10/04 ログイン失敗を教える何らかのメッセージ を出力する
                        }
                        c.close();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
    }

    /***
     * ダイアログを閉じる際の処理
     */
    @Override
    public void onPause() {
        super.onPause();

        dismiss();
    }
}
