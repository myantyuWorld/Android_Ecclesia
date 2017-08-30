//package com.example.yuichi_oba.ecclesia.dialog;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.DialogFragment;
//import android.content.DialogInterface;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import com.example.yuichi_oba.ecclesia.R;
//import com.example.yuichi_oba.ecclesia.tools.DB;
//
///**
// * Created by Yuichi-Oba on 2017/07/21.
// */
//
//public class AuthDialog extends DialogFragment {
//
//    /***
//     * 管理者認証用ダイアログ
//     * @param savedInstanceState
//     * @return
//     */
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_auth, null);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        return builder.setTitle("")
//                .setView(layout)
//                .setPositiveButton("認証", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getActivity(), "認証", Toast.LENGTH_SHORT).show();
//                        /***
//                         * ここで、管理者認証を行い、良ければ、管理者画面に遷移するといっても、管理者画面はなし・・・
//                         */
//                        SQLiteOpenHelper helper = new DB()
//
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .create();
//    }
//
//    /***
//     * ダイアログを閉じる際の処理
//     */
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        dismiss();
//    }
//}
