package com.example.yuichi_oba.ecclesia.tools;

public class Hint {
    /***
     * _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
     * _/_/_/                Spinner値の動的設定
     * _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
     *      ①   リスト作成（スピナーに設定したい型でーー＞ま、ふつうList<String> ）
     *      ②   ArrayAdapter<String> adapter = new ...(this, android.R.layout.simple..., list);
     *      ③   spinner.setAdapter(adapter)で OK!
     *
     *
     * _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
     * _/_/_/                    DB検索
     * _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
     *      ①   SQLiteOpenHelper helper = new DB(getApplicationContext());
     *      ②   SQLiteDatabase db = helper.getReadableDatabase();   // 読み取り専用
     *                                                               // 書き込みたいなら、getWritable~~~();
     *      ③   Cursor c = rawQuery("SQL文", ?に該当するやつ（new String[]{});
     *      ④   if (c.moveToNext()){...} or while (c.moveToNext()){...}
     *
     *
     * _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
     * _/_/_/           Intentでの値渡し ＋ オブジェクト渡し
     * _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
     *      ①   Inten in = new Intent(getApplicationContext(), 次の画面のクラス名.class);
     *      ②   intent.putExtra("key", object);
     *
     *      ③   遷移後のアクティビティでのオブジェクト受け取り
     *      ④   Intent in = getIntent();
     *      ⑤   ex) StoreData s = (StoreData) intent.getSerializableExtra(key)
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */
}
