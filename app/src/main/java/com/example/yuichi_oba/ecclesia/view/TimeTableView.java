package com.example.yuichi_oba.ecclesia.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.yuichi_oba.ecclesia.model.ReserveInfo;

import static com.example.yuichi_oba.ecclesia.activity.ReserveListActivity.reserveInfo;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.MAX_HEIGHT;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.MAX_WIDTH;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.ZERO;

/**
 * Created by Yuichi-Oba on 2017/08/28.
 */

public class TimeTableView extends View {

    private Paint p;
    private Paint p2;
    private Paint timetable;
    private float[] timeFloats = {300,500,700,900,1100,1300,1500};

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
        // 時間割の枠の描画
        onDrawTimeTable(c);

        // アプリを立ち上げた社員の予約情報の描画
        for (ReserveInfo r : reserveInfo) {
            String sTime = r.getRe_startTime();
            String eTime = r.getRe_endTime();
            String room_id = r.getRe_roomId();

            RectF rectF = retRectCooperation(sTime, eTime, room_id);
            c.drawRoundRect(rectF, 30, 30, timetable);
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
            case "0001":
                sX = x;
                eX = 2 * x;
                break;
            case "0002":
                sX = 2 * x;
                eX = 3 * x;
                break;
            case "0003":
                sX = 3 * x;
                eX = 4 * x;
                break;
            case "0004":
                sX = 4 * x;
                eX = 5 * x;
                break;
        }
        int s = Integer.parseInt(sTime.split("：")[0]) - 8; // 08:00 -> 8 => 8 - 8 = 0
        sY = timeFloats[s];
        if (Integer.parseInt(sTime.split("：")[1]) >= 30) {
            sY += 100;
        }

        int e = Integer.parseInt(eTime.split("：")[0]) - 8; // 10:30 -> 10 - 8 = 2
        eY = timeFloats[e];
        if (Integer.parseInt(eTime.split("：")[1]) >= 30) { // 30 >= 30
            eY += 100;
        }

        return new RectF(sX, sY, eX, eY);
    }

    /***
     * 時間割の枠の描画
     * @param canvas
     */
    private void onDrawTimeTable(Canvas canvas) {
        float x = 216;
        float y_timetable = 300;
        for (int i = 1; i <= 4; i++) {
            canvas.drawLine(i * x, y_timetable, i * x, MAX_HEIGHT, p2);
        }
        float y = 100;
        for (int i = 4; i < 17; i++) {
            canvas.drawLine(x, i * y, MAX_WIDTH, i * y, p2);
            if (i % 2 == 1) {
                canvas.drawLine(ZERO, i * y, x, i * y, p);
            }
        }
        canvas.drawRect(ZERO, y_timetable, MAX_WIDTH, MAX_HEIGHT, p);
        canvas.drawLine(x, y_timetable, x, MAX_HEIGHT, p);
    }

    /***
     * Paintクラスの初期化処理メソッド
     */
    private void init() {
        Log.d("call", "call TimeTableView->init()");
        p = new Paint();
        p.setColor(Color.DKGRAY);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);

        p2 = new Paint();
        p2.setStrokeWidth(2.0f);

        timetable = new Paint();
        timetable.setColor(Color.RED);
        timetable.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(10);
//        for (ReserveInfo r : reserveInfo){
//            Log.d("call", r.getRe_id() + " : " + r.getRe_startTime() + "(" + r.getRe_endTime() + ") room_id : " + r.getRe_roomId());
//        }
    }


}
