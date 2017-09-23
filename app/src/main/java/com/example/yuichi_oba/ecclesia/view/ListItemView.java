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
 * Created by Yuichi-Oba on 2017/09/22.
 */
//*** リストビューの子要素のCanvas描画クラス ***//
public class ListItemView extends View {
    //***  ***//
    private Paint p_outline;
    private Paint p_text;
    private Paint p_title;
    //***  ***//
    public ListItemView(Context context) {
        super(context);
        init();
    }
    public ListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public ListItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    //*** 描画メソッド ***//
    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        //*** 基本枠の描画 ***//
        onDrawBasic(c);
        //*** テキストの描画 ***//
        onDrawText(c);




    }
    //*** テキストの描画メソッド ***//
    private void onDrawText(Canvas c) {
        String[] strings = {"目的", "日付", "概要", "会社名", "参加者"};
        c.drawText(strings[0], 20, 50, p_text);
        c.drawText(strings[1], 570, 50, p_text);
        c.drawText(strings[2], 20, 160, p_text);
        c.drawText(strings[3], 10, 275, p_text);
        c.drawText(strings[4], 550, 275, p_text);
    }
    //*** 枠の描画メソッド ***//
    private void onDrawBasic(Canvas c) {
        float padding = 3;
        float p2 = 6;
        float x = 150;

        c.drawRect(0 + p2, 0 + p2, 150, 70 - padding, p_title);
        c.drawRect(540 + padding, 0 + p2, 540 + x - padding, 70 - padding, p_title);
        c.drawRect(0 + padding, 70 + padding, 150 - padding, 230 - padding, p_title);
        c.drawRect(0 + padding, 230 + padding, 150 - padding, 300 - padding, p_title);
        c.drawRect(540 + padding, 230 + padding, 540 + x - padding, 300 - padding, p_title);
        // l t r b
        c.drawRect(0 + padding, 0 + padding, 1080 - padding, 300 - padding, p_outline);
        // sx sy ex ey
        c.drawLine(0 + padding, 70, 1080 - padding, 70, p_outline);
        c.drawLine(0 + padding, 230, 1080 - padding, 230, p_outline);
        c.drawLine(540, 0 + padding, 540, 70, p_outline);
        c.drawLine(540, 230 + padding, 540, 300 - padding, p_outline);

        c.drawLine(x, 0 + padding, x, 70, p_outline);
        c.drawLine(x, 230 + padding, x, 300, p_outline);
        c.drawLine(x, 70 + padding, x, 230, p_outline);
        c.drawLine(540 + x, 0 + padding, 540 + x, 70, p_outline);
        c.drawLine(540 + x, 230 + padding, 540 + x, 300, p_outline);
    }
    //*** 初期化処理メソッド ***//
    private void init() {
        p_outline = new Paint();
        p_outline.setStrokeWidth(5);
        p_outline.setColor(Color.BLACK);
        p_outline.setStyle(Paint.Style.STROKE);

        p_text = new Paint();
        p_text.setStrokeWidth(10);
        p_text.setColor(Color.BLACK);
        p_text.setTypeface(Typeface.MONOSPACE);
        p_text.setTextSize(40);

        p_title = new Paint();
        p_title.setStyle(Paint.Style.FILL);
        p_title.setStrokeWidth(10);
        p_title.setColor(Color.LTGRAY);
    }

}
