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
        p.setStrokeWidth(10);

    }
}
