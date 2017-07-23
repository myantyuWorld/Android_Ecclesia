package com.example.yuichi_oba.ecclesia.dialog;

/**
 * Created by Yuichi-Oba on 2017/07/21.
 */

//public class ExtensionDialog extends DialogFragment {
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return new AlertDialog.Builder(getActivity())
//                .setTitle("延長確認")
//                .setMessage("延長を実行しますか？")
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getActivity(), "延長の実行！", Toast.LENGTH_SHORT).show();
//                        /***
//                         * 延長情報をDBに書き込んで画面殺しておわり
//                         */
//                        Intent intent = new Intent(ExtentionActivity.this, ReserveListActivity.class);
//                        startActivity(intent);
//                    }
//                })
//                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getActivity(), "キャンセル", Toast.LENGTH_SHORT).show();
//                        dismiss();
//                    }
//                })
//                .create();
//    }
//}
