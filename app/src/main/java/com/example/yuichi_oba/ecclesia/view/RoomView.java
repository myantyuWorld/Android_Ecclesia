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

public class RoomView extends View {

    private Paint p;

    /***
     * Constractor
     */
    public RoomView(Context context) {
        super(context);
        init();
    }
    public RoomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public RoomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /***
     * 描画のコア
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 特別会議室 Ａ Ｂ Ｃ 列の描画
        float x = 216;
        float room_y = 150;
        canvas.drawRect(ZERO, room_y, MAX_WIDTH, 2 * room_y, p);
        canvas.drawLine(ZERO, room_y, ZERO, 2 * room_y, p);   // sx sy ex ey
        for (int i = 1; i <= 4; i++) {
            canvas.drawLine(i * x, room_y, i * x, 2 * room_y, p);
        }
    }

    private void init() {
        p = new Paint();
        p.setColor(Color.DKGRAY);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);
    }
}
