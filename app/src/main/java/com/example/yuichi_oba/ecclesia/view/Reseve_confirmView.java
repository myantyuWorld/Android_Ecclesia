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
 * Created by yuki on 2017/09/08.
 */

public class Reseve_confirmView extends View {
    private Paint p_line;
    private Paint p_out_line;
    private Paint p_text;

    public Reseve_confirmView(Context context) {
        super(context);
        init();
    }

    public Reseve_confirmView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Reseve_confirmView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 枠線用
        p_line = new Paint();
        p_line.setColor(Color.DKGRAY);
        p_line.setStyle(Paint.Style.STROKE);
        p_line.setStrokeWidth(10);

        p_out_line = new Paint();
        p_out_line.setStrokeWidth(2.0f);


        // テキスト用
        p_text = new Paint();
        p_text.setTypeface(Typeface.MONOSPACE);
        p_text.setTextSize(40);
        p_text.setTextAlign(Paint.Align.CENTER);
        p_text.setColor(Color.BLACK);
    }
    private String[] name = {"概要","目的","開始時間","終了時間","申請者","参加者","社内/社外","会社名","希望会議室","備品","その他"};

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        // l t r b
        c.drawRect(0,1080,0,1920,p_line);
        float room = 120;
        // sx = 左上 sy　=　左下 ex　=　幅 ey　=　右下
        c.drawLine(0,room,1080,room,p_out_line);
        for (int i = 1 ; i <=11 ; i++) {
            c.drawLine(0 ,room * i,1080,room*i,p_out_line);
            c.drawText(name[i-1],0,60,p_text);
        }
    }
}
