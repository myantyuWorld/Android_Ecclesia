package com.example.yuichi_oba.ecclesia.view;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.yuichi_oba.ecclesia.model.ReserveInfo;
import com.example.yuichi_oba.ecclesia.tools.DB;
import com.example.yuichi_oba.ecclesia.tools.NameConst;

import java.util.ArrayList;
import java.util.List;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.MAX_WIDTH;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.ROOM_A;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.ROOM_B;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.ROOM_C;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.TOKUBETSU;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.ZERO;

/**
 * Created by Yuichi-Oba on 2017/08/28.
 */

public class TimeTableView extends View {

    public static final int Y_HEIGHT = 40;
    public static final int X_WIGDH = 216;
    public static final int RE_ID = 0;
    public static final int RE_OVERVIEW = 1;
    public static final int RE_START_DAY = 2;
    public static final int RE_END_DAY = 3;
    public static final int RE_START_TIME = 4;
    public static final int RE_END_TIME = 5;
    public static final int RE_SWITCH = 6;
    public static final int RE_ROOM_ID = 10;
    private Paint p;
    private Paint p2;
    private Paint room;
    private Paint tokubetsu;
    private Paint roomA;
    private Paint roomB;
    private Paint roomC;
    private Paint p_txtTime;
    private Paint p_txtConference;
    public float x = 0;    // タップしたｘ座標
    public float y = 0;    // タップしたｙ座標

    private float[] timeFloats;
    public boolean thread_flg;
    private List<ReserveInfo> reserveInfo;

    public TimeTableView(Context context) {
        super(context);
        init();
    }

    public TimeTableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /***
     * 描画するメソッド
     * @param c
     */
    @Override
    protected void onDraw(Canvas c) {
        Log.d("call", "TimeTableView->onDraw()");

        timeFloats = new float[24];
        for (int i = 0, j = 100; i < timeFloats.length; i++) {
            timeFloats[i] = j;
            j += Y_HEIGHT * 2;
        }
        // 特別会議室 Ａ Ｂ Ｃ 列の描画
        float x = X_WIGDH;
        float room_y = 100;
        c.drawRect(ZERO, 0, MAX_WIDTH, room_y, p);
        c.drawLine(ZERO, 0, ZERO, room_y, p);   // sx sy ex ey
        for (int i = 1; i <= 4; i++) {
            c.drawLine(i * x, 0, i * x, room_y, p);
        }

        // 会議室名の描画
        float y_conference = 60;
        c.drawText("特別", 316, y_conference, p_txtConference);
        c.drawText("A", 532, y_conference, p_txtConference);
        c.drawText("B", 748, y_conference, p_txtConference);
        c.drawText("C", 964, y_conference, p_txtConference);

        // 時間割の枠の描画
        onDrawTimeTable(c);
        // 時間の文字の描画 text x y paint
        for (int i = 0, j = 150; i < 24; i++) {
            String time = String.format("%02d:00", i);
            c.drawText(time, 100, j, p_txtTime);
            j += Y_HEIGHT * 2;
        }

        // アプリを立ち上げた社員の予約情報の描画
        onDrawConference(c);


    }

    private void onDrawConference(Canvas c) {
        int cnt = 0;
        for (ReserveInfo r : this.reserveInfo) {
            String sTime = r.getRe_startTime();
            String eTime = r.getRe_endTime();
            String room_id = r.getRe_roomId();

            RectF rectF = retRectCooperation(sTime, eTime, room_id);
            // 予約会議の座標情報を記録する
            reserveInfo.get(cnt).setCoop(new float[]{rectF.left, rectF.top, rectF.right, rectF.bottom});
            switch (room_id) {
                case "0001":
                    room = tokubetsu;
                    break;
                case "0002":
                    room = roomA;
                    break;
                case "0003":
                    room = roomB;
                    break;
                case "0004":
                    room = roomC;
                    break;
            }
            // 予約会議の描画
            c.drawRoundRect(rectF, 30, 30, room);
            cnt++;
        }
    }

    /***
     * 開始終了時刻・会議室を基に、描画すべき座標を返すメソッド
     * @param sTime
     * @param eTime
     * @param room_id
     */
    private RectF retRectCooperation(String sTime, String eTime, String room_id) {
        float sX = 0, eX = 0, sY = 0, eY = 0;
        float x = 216;
        switch (room_id) {
            case TOKUBETSU:
                sX = x;
                eX = 2 * x;
                break;
            case ROOM_A:
                sX = 2 * x;
                eX = 3 * x;
                break;
            case ROOM_B:
                sX = 3 * x;
                eX = 4 * x;
                break;
            case ROOM_C:
                sX = 4 * x;
                eX = 5 * x;
                break;
        }
        int s = Integer.parseInt(sTime.split("：")[0]); // 08:00 -> 8 => 8 - 8 = 0
        sY = timeFloats[s];
        if (Integer.parseInt(sTime.split("：")[1]) >= 30) {
            sY += Y_HEIGHT;
        }

        int e = Integer.parseInt(eTime.split("：")[0]); // 10:30 -> 10 - 8 = 2
        eY = timeFloats[e];
        if (Integer.parseInt(eTime.split("：")[1]) >= 30) { // 30 >= 30
            eY += Y_HEIGHT;
        }

        return new RectF(sX, sY, eX, eY);
    }

    /***
     * 時間割の枠の描画
     * @param canvas
     */
    private void onDrawTimeTable(Canvas canvas) {
        float x = 216;
        float y_timetable = 100;
        for (int i = 1; i <= 4; i++) {
            canvas.drawLine(i * x, y_timetable, i * x, 2200, p2);
        }
        // 中の線の線
        float y = Y_HEIGHT;
        for (int i = 1; i < 48; i++) {
            canvas.drawLine(x, y_timetable + i * y, MAX_WIDTH, y_timetable + i * y, p2);
            if (i % 2 == 0) {
                canvas.drawLine(ZERO, y_timetable + i * y, x, y_timetable + i * y, p);
            }
        }
        canvas.drawRect(ZERO, y_timetable, MAX_WIDTH, y_timetable + 48 * y, p);
    }

    /***
     * Paintクラスの初期化処理メソッド
     */
    private void init() {
        Log.d("call", "call TimeTableView->init()");
        reserveInfo = new ArrayList<>();

        // 枠線用
        p = new Paint();
        p.setColor(Color.DKGRAY);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);

        p2 = new Paint();
        p2.setStrokeWidth(2.0f);

        // 特別会議室用
        tokubetsu = new Paint();
        tokubetsu.setColor(Color.parseColor("#E91E63"));
        tokubetsu.setStyle(Paint.Style.FILL);
        tokubetsu.setStrokeWidth(10);

        // 会議室Ａ用
        roomA = new Paint();
        roomA.setColor(Color.parseColor("#536DFE"));
        roomA.setStyle(Paint.Style.FILL);
        roomA.setStrokeWidth(10);

        // 会議室Ｂ用
        roomB = new Paint();
        roomB.setColor(Color.parseColor("#4CAF50"));
        roomB.setStyle(Paint.Style.FILL);
        roomB.setStrokeWidth(10);

        // 会議室Ｃ用
        roomC = new Paint();
        roomC.setColor(Color.parseColor("#FFC107"));
        roomC.setStyle(Paint.Style.FILL);
        roomC.setStrokeWidth(10);

        // テキスト用
        p_txtTime = new Paint();
        p_txtTime.setTypeface(Typeface.MONOSPACE);
        p_txtTime.setTextSize(40);
        p_txtTime.setTextAlign(Paint.Align.CENTER);
        p_txtTime.setColor(Color.BLACK);

        p_txtConference = new Paint();
        p_txtConference.setTypeface(Typeface.MONOSPACE);
        p_txtConference.setTextSize(60);
        p_txtConference.setTextAlign(Paint.Align.CENTER);
        p_txtConference.setColor(Color.BLACK);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        Log.d("call", "TimeTableView->onTouchEvent()");
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                // タップした座標を取得する
                x = e.getX();
                y = e.getY();
                Log.d("call", e.getX() + " : " + e.getY());
                break;
        }

        return true;
    }

    /***
     *  再描画メソッド
     * @param emp_id
     * @param date
     */
    public void reView(String emp_id, String date) {
        // DO: 2017/09/06 review()コールで、引数の日付をデータベース検索をかけたのち、自身のreserveInfoに格納する-> invalidate() で描画する
        Log.d("call", "TimeTableView->reView()");
        SQLiteOpenHelper helper = new DB(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from v_reserve_member where mem_id = ? and re_startday = ?", new String[]{emp_id, date});
        reserveInfo.clear();
        while (c.moveToNext()) {
            // 予約情報のインスタンス生成 :: 暫定的に下記引数です
            ReserveInfo r = new ReserveInfo(
                    c.getString(RE_ID),
                    c.getString(RE_OVERVIEW),
                    c.getString(RE_START_DAY),
                    c.getString(RE_END_DAY),
                    c.getString(RE_START_TIME),
                    c.getString(RE_END_TIME),
                    c.getString(RE_SWITCH),
                    c.getString(RE_ROOM_ID)
            );
            reserveInfo.add(r);
            Log.d("call", c.getString(2) + " : " + c.getString(3));
        }

        invalidate();
    }

    /***
     * タップした会議の予約ＩＤを返すメソッド
     * @return 会議予約ＩＤ
     */
    public String getSelectedReserve() {
        Log.d("call", "TimeTableView->getSelectedReserve()");
        //
        String re_id = "";
        while (thread_flg) {
            float wX = 216;
            // タッチされたか
            if (isTouched()) {
                if (x > wX && x < 2 * wX) {
                    Log.d("call", "tokubetu");
                    re_id = TOKUBETSU;
                } else if (x > 2 * wX && x < 3 * wX) {
                    Log.d("call", "roomA");
                    re_id = ROOM_A;
                } else if (x > 3 * wX && x < 4 * wX) {
                    Log.d("call", "roomB");
                    re_id = ROOM_B;
                } else if (x > 4 * wX && x < 5 * wX) {
                    Log.d("call", "roomC");
                    re_id = ROOM_C;
                }
                // re_id と y座標を基に、どの会議がタップされたかを返す
                int cnt = 0;
                for (ReserveInfo r : this.reserveInfo) {
                    if (r.getCoop() != null && r.getCoop()[1] < y && r.getCoop()[3] > y) {
                        // 特定した
                        if (re_id.equals(r.getRe_roomId())) {
                            Log.d("call", "会議を特定した！  " + r.getRe_roomId());
                            re_id = r.getRe_id();
                            thread_flg = false;
                        }
                        cnt++;
                    }
                }
                Log.d("call", "cnt :: " + String.valueOf(cnt));
                // TODO: 2017/09/06 該当する会議がないー→ 新規会議の予約画面に移行するロジックの実装
                if (cnt > 0) {
                    Log.d("call", "新規会議の登録ロジック開始！");
                    return NameConst.NONE;
                }
                x = 0;
                y = 0;
            }
        }
        Log.d("call", "Re_id : " + re_id);
        return re_id;
    }

    /***
     * 予約情報リストのディープコピーを行うメソッド
     * @param reserveInfo_timetable
     */
    private void deepCopyReserveInfo(List<ReserveInfo> reserveInfo_timetable) {
        // 予約情報を保持するリストのディープコピー
    }

    public boolean isTouched() {
        if (x != 0 && y != 0) {
            return true;
        }
        return false;
    }
}
