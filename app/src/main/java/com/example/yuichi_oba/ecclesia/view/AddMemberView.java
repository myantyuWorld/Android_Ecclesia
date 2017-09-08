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
 * Created by Yuichi-Oba on 2017/09/08.
 */

public class AddMemberView extends View {

    private Paint p;
    private Paint p2;
    private Paint p_txt;
    private Paint p_daimei;

    public AddMemberView(Context context) {
        super(context);
        init();
    }

    public AddMemberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AddMemberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //*** ペイントの初期化処理メソッド ***//
    private void init() {
        p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);

        p2 = new Paint();
        p2.setColor(Color.LTGRAY);
        p2.setStyle(Paint.Style.FILL);

        p_txt = new Paint();
        p_txt.setTypeface(Typeface.MONOSPACE);
        p_txt.setTextSize(60);
        p_txt.setTextAlign(Paint.Align.LEFT);
        p_txt.setColor(Color.BLACK);

        p_daimei = new Paint();
        p_daimei.setColor(Color.GREEN);
        p_daimei.setStyle(Paint.Style.FILL);
        p_daimei.setStrokeWidth(10);

    }

    //*** 描画メソッド ***//
    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        // 枠の描画
        // l t r b
        c.drawRect(0, 0, 1080, 1920, p);

        float y = 100;
        // sx sy ex ey
        c.drawLine(0, y, 1080, y, p);
        float padding = 3;
        c.drawRect(0 + padding, 0 + padding, 1080 - padding, y - padding, p_daimei);
        c.drawLine(0, 280, 1080, 280, p);
        c.drawRect(0 + padding, 280 + padding, 1080 - padding, 380 - padding, p_daimei);
        c.drawLine(0, 280 + y, 1080, 280 + y, p);
        c.drawLine(0, 1500, 1080, 1500, p);

        float y2 = 190;
        for (int i = 1; i <= 5; i++) {
            c.drawLine(0, i * y2 + 380, 1080, i * y2 + 380, p);
        }
        for (int i = 1; i < 7; i+=2) {
            c.drawRect(0 + padding, i * y2 - y2 + 380 + padding, 1080 - padding, i * y2 + 380 - padding, p2);
        }
        c.drawLine(300, 380, 300, 1500, p);

        // 各項目の描画
        String[] str = new String[]{"会社", "部署", "役職", "氏名", "Email", "TEL"};
        float y_txt = 105;
        for (int i = 0; i <= 5; i++) {
            c.drawText(str[i], 30, y_txt + 380, p_txt);
            y_txt += y2;
        }



    }
}
