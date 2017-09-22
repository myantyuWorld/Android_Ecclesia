package com.example.yuichi_oba.ecclesia.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Yuichi-Oba on 2017/09/22.
 */

public class HistorySearch extends View {

    private Paint p_outline;
    private Paint p_text;

    public HistorySearch(Context context) {
        super(context);
        init();
    }

    public HistorySearch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HistorySearch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        //***  ***//
        // l t r b
        c.drawRect(0, 0, 1080, 300, p_outline);
        // sx sy ex ey
        c.drawLine(0, 100, 1080, 100, p_outline);
        c.drawLine(0, 200, 1080, 200, p_outline);
        c.drawLine(540, 0, 540, 100, p_outline);
        c.drawLine(540, 200, 540, 300, p_outline);



    }

    private void init() {
        p_outline = new Paint();
        p_outline.setStrokeWidth(10);
        p_outline.setColor(Color.BLACK);
        p_outline.setStyle(Paint.Style.STROKE);

        p_text = new Paint();
        p_text.setStrokeWidth(10);
        p_text.setColor(Color.BLACK);
    }

}
