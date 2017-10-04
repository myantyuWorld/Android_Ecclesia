package com.example.yuichi_oba.ecclesia.tools;

public class Hint {
    /***
     * _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
     * _/_/_/
     * _/_/_/         Spinner値の動的設定
     * _/_/_/
     * _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
     *      ①   リスト作成（スピナーに設定したい型でーー＞ま、ふつうList<String> ）
     *      ②   ArrayAdapter<String> adapter = new ...(this, android.R.layout.simple..., list);
     *      ③   spinner.setAdapter(adapter)で OK!
     *
     *      cf) リスナー setOnItemSelectedListener
     *                  Spinner spinner = (Spinner) adapterView;
     *                  String name = spinner.getSelectedItem().toString();
     *
     *
     * _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
     * _/_/_/
     * _/_/_/          DB検索
     * _/_/_/
     * _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
     *      ①   SQLiteOpenHelper helper = new DB(getApplicationContext());
     *      ②   SQLiteDatabase db = helper.getReadableDatabase();   // 読み取り専用
     *                                                               // 書き込みたいなら、getWritable~~~();
     *      ③   Cursor c = rawQuery("SQL文", ?に該当するやつ（new String[]{});     // Cursorはでgetする場合は0オリジン
     *      ④   if (c.moveToNext()){...} or while (c.moveToNext()){...}
     *
     *
     * _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
     * _/_/_/
     * _/_/_/           Intentでの値渡し ＋ オブジェクト渡し
     * _/_/_/
     * _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
     *      ①   Inten in = new Intent(getApplicationContext(), 次の画面のクラス名.class);
     *      ②   intent.putExtra("key", object);
     *
     *      ③   遷移後のアクティビティでのオブジェクト受け取り
     *      ④   Intent in = getIntent();;
     *      ⑤   ex)     StoreData s = (StoreData) intent.getSerializableExtra(key)
     *                   このとき、StoreDataは "implements Serializable" する
     *                   Bundle も同じ！
     *
     *
     *  //*** StartForActivityResult の 値渡し ***
     *                   Intent intent = new Intent();
     //                intent.putExtra("member", e);
     //                setResult(RESULT_OK, intent);   リクエストコード（任意）, intentのインスタンス
     //                finish();

          からの
          呼び出しもとで、onActivityResultをオーバーライドする
     *
     * _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
     * _/_/_/
     * _/_/_/           ダイアログ
     * _/_/_/
     * _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
     *      ①   MyDialog extends DialogFragment {...}
     *      ②   onCreateDialog を　オーバーライド
     *      ③   基本形の例
     @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
     return new AlertDialog.Builder(getActivity())
     .setTitle("タイトル")
     .setMessage("メッセージ")
     .create();
     }

     @Override public void onPause() {
     super.onPause();

     // onPause でダイアログを閉じる場合
     dismiss();
     }

     ④   使用する際は、
     MyDialog d = new MyDialog();
     d.show(getFragmentManager(), "key");

     ⑤   ダイアログにリストを出す
     .setItems(items, new DialogInterface.OnClickLister(){...}
     CharSequence[] items =
     reserveInfo.getRe_member().toArray(new CharSequence[reserveInfo.getRe_member().size()]);

     *
     *
     */

    /***
     * リスト1.8: 点を描画
     mPaint.setStrokeWidth(1.0f);
     for (int i = 0; i &lt; 300; i++) {
     if (i % 5 != 0) {
     continue;
     }

     canvas.drawPoint(100, 100 + i, mPaint);
     }
     */

    //*** HCP 画面担当一覧 ***//
     //*** ReserveChangeActivity        しゃま***//
     //*** ReserveListActivity          オレ***//
     //*** HistorySearchActivity        ぐちお ***//
     //*** ReserveCheckActivity         くに***//
     //*** ReserveConfirmActivity       くに***//
     //*** AddMemberActivity            ヨッシー ***//
     //*** ReserveActivity              ヨッシー ***//

     // 今更命名規則

          // 画面名（略称。アンダーバーごとに頭文字だけ）_部品名_その内容

               // content_reserve の確定ボタンなら cr_btn_correct みたいな感じ
               // 略して同じになる画面は最後の部分を頭文字ではなくフルで書く
               // (content_reserve_chack と content_reserve_change は両方crcになるので、crchange と crcheckという具合)

               // 部品名（基本三文字か二文字に略す）
               // ボタン btn     テキストビュー txt    エディットテキスト etxt      スピナー sp
               // スイッチ sw     リストビュー lis    サーチビュー sea
}
