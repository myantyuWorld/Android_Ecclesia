package com.example.yuichi_oba.ecclesia.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Yuichi-Oba on 2017/09/23.
 */

public class HistorySearchView extends View {

    //*** Field ***//
    private Paint p_outline;
    private Paint p_text;
    private Paint p_title;
    private Paint p_title2;
    //*** Constractor ***//
    public HistorySearchView(Context context) {
        super(context);
        init();
    }
    public HistorySearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public HistorySearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //*** Paintクラスの初期化処理メソッド ***//
    private void init() {
        p_outline = new Paint();
        p_outline.setStrokeWidth(5);
        p_outline.setStyle(Paint.Style.STROKE);
        p_outline.setColor(Color.BLACK);

        p_text = new Paint();
        p_text.setTypeface(Typeface.MONOSPACE);
        p_text.setTextSize(40);
        p_text.setColor(Color.BLACK);

        p_title = new Paint();
        p_title.setStyle(Paint.Style.FILL);
        p_title.setColor(Color.LTGRAY);

        p_title2 = new Paint();
        p_title2.setStyle(Paint.Style.FILL);
        p_title2.setColor(Color.parseColor("#4169e1"));

    }
    //*** 描画メソッド ***//
    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        float padding = 5;
        // l t r b
        c.drawRect(0, 0 + padding, 200, 550 - padding, p_title);
        c.drawRect(0, 0 + padding, 1080 - padding, 70, p_title2);
        c.drawRect(0 + padding, 480, 1080 - padding, 550, p_title2);
        c.drawRect(0 + padding, 0 + padding, 1080 - padding, 550 - padding, p_outline);

        c.drawLine(200, 70, 200, 480, p_outline);

        c.drawLine(0, 70, 1080, 70, p_outline);
        c.drawLine(0, 175, 1080, 175, p_outline);
        c.drawLine(0, 280, 1080, 280, p_outline);
        c.drawLine(0, 480, 1080, 480, p_outline);

        c.drawText("検索条件", 470, 50, p_text);
        c.drawText("会社名", 20, 130, p_text);
        c.drawText("会議目的", 20, 235, p_text);
        c.drawText("フリー", 20, 360, p_text);
        c.drawText("ワード", 20, 410, p_text);
        c.drawText("検索結果", 470, 525, p_text);


    }
}
