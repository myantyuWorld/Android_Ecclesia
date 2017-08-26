package com.example.yuichi_oba.ecclesia.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
        p.setStrokeWidth(LINE_WIDGH);
        p.setStyle(Paint.Style.STROKE);

        Paint p2 = new Paint();
        p2.setColor(Color.BLACK);
        p2.setStrokeWidth(1.0f);

        Paint tokubetu = new Paint();
        tokubetu.setColor(Color.BLUE);
        Paint room_a = new Paint();
        room_a.setColor(Color.RED);
        Paint room_b = new Paint();
        room_a.setColor(Color.CYAN);
        Paint room_c = new Paint();
        room_a.setColor(Color.YELLOW);


//        Paint[] colors = new Paint[4];
//        colors[0].setColor(Color.RED);
//        colors[1].setColor(Color.BLUE);
//        colors[2].setColor(Color.CYAN);
//        colors[3].setColor(Color.YELLOW);


        // ビュー側への描画
        float y = 150;
//        canvas.drawLine(0,0,1080,0,p);
//        canvas.drawLine(0,y,1080,y,p);

//        Rect rect = new Rect(50, 100, 100, 200);
        // l t r b
        canvas.drawRect(5, 5, 1080, y, p);
        canvas.drawRect(5, y, 1080, 1700, p);
        canvas.drawRect(5, y, 1080, y + 200, p);
        canvas.drawRect(5, y, 100, 1700, p);
        float x = 345;
        float x2 = 100;
        for (int i = 0; i < 4; i++) {
            canvas.drawLine(x, y, x, 1700, p2);
            canvas.drawLine(x, y, x, y + 200, p);
//            canvas.drawRect(x - 10,y,x + 245 - 10, y + 200, colors[i]);
            switch (i) {
                case 0:
                    canvas.drawRect(x2 - 10, y, x2 + 245 - 10, y + 200, tokubetu);
                    break;
                case 1:
                    canvas.drawRect(x2 - 10, y, x2 + 245 - 10, y + 200, room_a);
                    break;
                case 2:
                    canvas.drawRect(x2 - 10, y, x2 + 245 - 10, y + 200, room_b);
                    break;
                case 3:
                    canvas.drawRect(x2 - 10, y, x2 + 245 - 10, y + 200, room_c);
                    break;
            }
            x += 245;
            x2 += 245;
        }

        float y2 = 350;
        for (int i = 0; i < 36; i++) {
            canvas.drawLine(5, y2, 1080, y2, p2);
            y2 += 50;
        }


    }
}
