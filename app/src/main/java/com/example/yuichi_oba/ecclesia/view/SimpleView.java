package com.example.yuichi_oba.ecclesia.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.*;

/**
 * Created by 5151021 on 2017/08/23.
 */

public class SimpleView extends View {



    /***
     * Constractor
     */
    public SimpleView(Context context) {
        super(context);
    }

    public SimpleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /***
     * 描画のコア
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 描画のためのスタイルの設定
        Paint p = new Paint();
        p.setColor(Color.DKGRAY);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);

        // 特別会議室 Ａ Ｂ Ｃ 列の描画
        onDrawRoom(p, canvas);
        // 時間割の描画
        onDrawTimeTable(p, canvas);

    }

    /***
     * 時間割の枠を描画するメソッド
     * @param p
     * @param canvas
     */
    private void onDrawTimeTable(Paint p, Canvas canvas) {
        float x = 216;
        Paint p2 = new Paint();
        p2.setStrokeWidth(2.0f);
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
     * 会議室の枠を描画するメソッド
     * @param p
     * @param canvas
     */
    private void onDrawRoom(Paint p, Canvas canvas) {
        float x = 216;
        float room_y = 150;
        canvas.drawRect(ZERO, room_y, MAX_WIDTH, 2 * room_y, p);
        canvas.drawLine(ZERO, room_y, ZERO, 2 * room_y, p);   // sx sy ex ey
        for (int i = 1; i <= 4; i++) {
            canvas.drawLine(i * x, room_y, i * x, 2 * room_y, p);
        }

    }
}
