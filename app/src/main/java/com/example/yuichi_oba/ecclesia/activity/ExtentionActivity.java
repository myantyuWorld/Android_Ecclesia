package com.example.yuichi_oba.ecclesia.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;


import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.model.Reserve;
import com.example.yuichi_oba.ecclesia.tools.DB;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.*;

public class ExtentionActivity extends AppCompatActivity
    implements View.OnClickListener{
    Button bt_extension;
    Spinner sp_time;
    Reserve reserveInfo;

//    /***
//     *  延長アクティビティで使用するダイアログクラス
//     */
//    public class ExtensionDialog extends DialogFragment {
//
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            return new AlertDialog.Builder(getActivity())
//                    .setTitle(EX + CONF)
//                    .setMessage(EX + JIKKOUQUE)
//                    .setPositiveButton(OK, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(getActivity(), EX + JIKKOU, Toast.LENGTH_SHORT).show();
//                            /***
//                             * 延長情報をDBに書き込む
//                             */
//                            dbInsertExtension();
//
//                            /***
//                             * 画面をころして、予約一覧画面に遷移する
//                             */
//                            Intent intent = new Intent(getActivity(), ReserveListActivity.class);
//                            startActivity(intent);
//                            dismiss();
//                        }
//                    })
//                    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(getActivity(), "キャンセル", Toast.LENGTH_SHORT).show();
//                            dismiss();
//                        }
//                    })
//                    .create();
//        }
//    }

//    public class ExtensionResult extends DialogFragment {
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            return new AlertDialog.Builder(getActivity()).setTitle();
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extention);

        reserveInfo = (Reserve) getIntent().getSerializableExtra("EX");

        bt_extension = (Button) findViewById(R.id.bt_ex_extention);
        sp_time = (Spinner) findViewById(R.id.sp_endTime);
        bt_extension.setOnClickListener(this);
        bt_extension.setEnabled(false);



//        sp_time.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                bt_extension.setEnabled(true);
//            }
//        });
    }

    @Override
    public void onClick(View view) {


//        ExtensionDialog dialog = new ExtensionDialog();
//        dialog.show(getFragmentManager(), "bbb");


//        finish();
//        Intent intent = new Intent(getApplicationContext(), ReserveListActivity.class);
//        startActivity(intent);
    }

    public void dbInsertExtension() {
        ContentValues con = new ContentValues();
        con.put("re_id", reserveInfo.getRe_id());
        con.put("ex_startday", reserveInfo.getRe_startDay());
        con.put("ex_endday", reserveInfo.getRe_endDay());
        con.put("ex_starttime", reserveInfo.getRe_startTime());
        con.put("ex_endtime", reserveInfo.getRe_endTime());
        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.insert("t_extension", null, con) > ZERO) {

        }
    }
}
