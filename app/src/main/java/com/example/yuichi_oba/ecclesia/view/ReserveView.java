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

public class ReserveView extends View {

    private Paint p;
    private Paint p2;
    private Paint p_koumoku;

    public ReserveView(Context context) {
        super(context);
        init();
    }

    public ReserveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReserveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // *** ペイントの初期化処理メソッド *** //
    private void init() {
        p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);

        p2 = new Paint();
        p2.setColor(Color.LTGRAY);
        p2.setStyle(Paint.Style.FILL);

        p_koumoku = new Paint();
        p_koumoku.setColor(Color.BLACK);
        p_koumoku.setTypeface(Typeface.MONOSPACE);
        p_koumoku.setTextSize(40);

    }
    // *** ペイントの描画メソッド *** //
    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        // l t r b

        // 外枠の描画
        c.drawRect(0, 0, 1080, 1700, p);
        float y = 200;
        for (int i = 0; i < 11; i++) {
            c.drawLine(0, i * y, 1080, i * y, p);
        }
        // 各項目を交互に、色違いにするロジック
        float padding = 3;
        for (int i = 1; i < 13; i += 2) {
            c.drawRect(0 + padding, i * y + padding - y, 1080 - padding, i * y - padding, p2);
        }
        c.drawLine(250, 0, 250, 2200, p);

        float y_txt = 100;
        float x_koumoku = 30;
        String[] koumoku = new String[]{"概要", "目的", "開始", "終了", "申請者", "参加者", "社内/社外", "会社名", "会議室", "備品", "備考"};
        for (int i = 0; i < koumoku.length; i++) {
            c.drawText(koumoku[i], x_koumoku, y_txt, p_koumoku);
            y_txt += y;
        }

    }
}
