package com.example.yuichi_oba.ecclesia.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

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


    }

    private void init() {
        p = new Paint();
        p.setColor(Color.DKGRAY);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);
    }
}
