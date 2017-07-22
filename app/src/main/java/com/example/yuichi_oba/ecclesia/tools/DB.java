package com.example.yuichi_oba.ecclesia.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Yuichi-Oba on 2017/07/22.
 */

public class DB extends SQLiteOpenHelper {
    private static final int    DB_VERSION      = 1;
    private static final String DB_FILE_NAME    = "ecclesia.db";
    private static final String DB_NAME         = "ecclesia";


    public static SQLiteDatabase db = null;

    private boolean createDatabase = true;
    private Context context;
    private File dbPath;

    // Constructor
    public DB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        this.dbPath = context.getDatabasePath(DB_NAME);
    }




    /***
     * DB の読み書き
     * @return this 自身のオブジェクト
     */
    public DB openDB(){
        db = this.getWritableDatabase();
        return this;
    }

    /***
     * DB の読み込み
     * @return this 自身のオブジェクト
     */
    public DB readDB(){
        db = this.getReadableDatabase();
        return this;
    }

    /***
     * DB を閉じる
     * closeDB()
     */
    public void closeDB(){
        db.close();
        db = null;
    }

    /***
     * 指定テーブル の全データ（*での検索)
     * getDB()
     * @param   tblName DBのテーブル名
     * @param   columns DBの列名
     * @return  指定テーブル の全データ（*での検索)
     */
    public Cursor getDB(String tblName, String[] columns){
        // queryメソッド DBのデータを取得
        // 第1引数：DBのテーブル名
        // 第2引数：取得するカラム名
        // 第3引数：選択条件(WHERE句)
        // 第4引数：第3引数のWHERE句において?を使用した場合に使用
        // 第5引数：集計条件(GROUP BY句)
        // 第6引数：選択条件(HAVING句)
        // 第7引数：ソート条件(ODERBY句)
        return db.query(tblName, columns, null, null, null, null, null);
    }

    /*
        スレッドの排他制御（synchronized）
        下記のように synchronized を用いることで、指定したオブジェクト
        に対してロック権を取得した単一のスレッドのみが 下記処理を行えるようにする
     */
    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase sqLiteDatabase = super.getReadableDatabase();
        if (createDatabase){
            try {
                sqLiteDatabase = copyDatabase(sqLiteDatabase);
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        return super.getReadableDatabase();
        return sqLiteDatabase;
    }



    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase sqLiteDatabase = super.getWritableDatabase();
        if (createDatabase){
            try {
                sqLiteDatabase = copyDatabase(sqLiteDatabase);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        return super.getWritableDatabase();
        return sqLiteDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onOpen(db);
        // getWritableDatabase()したときによばれてくるので
        // 初期化する必要があることを保存する
        this.createDatabase = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // My Method
    private SQLiteDatabase copyDatabase(SQLiteDatabase sqLiteDatabase) throws IOException {
        // db が 開放中なので書き換えできるように閉じる
        sqLiteDatabase.close();

        // コピー （いまassetにある最新のDBファイルをコピーする）
        InputStream in = context.getAssets().open(DB_FILE_NAME);
        OutputStream out = new FileOutputStream(this.dbPath);
        copy(in, out);

        // コピーしたので新しくDB作る必要ないので、フラグを false にする
        createDatabase = false;

        // db を閉じたので、また開いて返す
        return super.getWritableDatabase();
//        return sqLiteDatabase;
    }

    private int copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024 * 4];
        int cnt = 0, n = 0;

        while (-1 != (n = in.read(buffer))){
            out.write(buffer, 0, n);
            cnt += n;
        }
        return cnt;
    }

}
