package com.example.yuichi_oba.ecclesia.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
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

    public static final String TOKUBETSU = "0001";
    public static final String ROOM_A = "0002";
    public static final String ROOM_B = "0003";
    public static final String ROOM_C = "0004";
    private Paint p;
    private Paint p2;
    private Paint room;
    private Paint tokubetsu;
    private Paint roomA;
    private Paint roomB;
    private Paint roomC;

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
        int cnt = 0;
        for (ReserveInfo r : reserveInfo) {
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
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // タップした座標を取得する
                float x = e.getX();
                float y = e.getY();
                Log.d("call", e.getX() + " : " + e.getY());
                // x座標を基に、どの会議室か特定する
                String room_id = "";
                float wX = 216;
                if (x > wX && x < 2 * wX) {
                    Log.d("call", "tokubetu");
                    room_id = TOKUBETSU;
                } else if (x > 2 * wX && x < 3 * wX) {
                    Log.d("call", "roomA");
                    room_id = ROOM_A;
                } else if (x > 3 * wX && x < 4 * wX) {
                    Log.d("call", "roomB");
                    room_id = ROOM_B;
                } else if (x > 4 * wX && x < 5 * wX) {
                    Log.d("call", "roomC");
                    room_id = ROOM_C;
                }
                // y座標を基に、どの時間帯がタップされたかをチェックし、予約情報を返す
                for (ReserveInfo r : reserveInfo) {
                    if (r.getCoop()[1] < y && r.getCoop()[3] > y) {
                        if (room_id.equals(r.getRe_roomId())) {
                            Log.d("call", r.getRe_id());
                        }
                    }
                }
                // その予約情報をもって、予約確認（ReserveConfirmActivity）に飛ぶ
                break;
        }
        return true;
    }

}
