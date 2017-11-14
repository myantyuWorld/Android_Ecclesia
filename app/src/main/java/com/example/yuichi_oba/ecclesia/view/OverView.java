package com.example.yuichi_oba.ecclesia.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yuichi_develop on 2017/11/14.
 */

public class OverView extends View {

  private Paint p_line;
  private Paint p_text;
  private Paint p_my;
  private Paint p_other;
  private Paint p_outline;

  public OverView(Context context) {
    super(context);
    init();
  }

  public OverView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public OverView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @Override
  protected void onDraw(Canvas c) {

    //*** l t r b ***//
    c.drawRect(0, 0, 1078, 200, p_line);

    int x = 30;
    int round = 10;
    c.drawRoundRect(new RectF(40 + x, 20, 100 + x, 80), round, round, p_my);
    c.drawRoundRect(new RectF(540 + x, 20, 600 + x, 80), round, round, p_other);

    c.drawRoundRect(new RectF(40 + x, 20, 100 + x, 80), round, round, p_outline);
    c.drawRoundRect(new RectF(540 + x, 20, 600 + x, 80), round, round, p_outline);

    c.drawText("自分の参加会議", 150 + x, 60, p_text);
    c.drawText("他人の参加会議", 650 + x, 60, p_text);

  }

  private void init() {
    p_line = new Paint();
    p_line.setColor(Color.parseColor("#303F9F"));
    p_line.setStrokeWidth(5);
    p_line.setStyle(Paint.Style.FILL);

    p_text = new Paint();
    p_text.setTypeface(Typeface.MONOSPACE);
    p_text.setTextSize(40);
    p_text.setColor(Color.WHITE);

    //*** 自分の凡例 ***//
    p_my = new Paint();
    p_my.setColor(Color.parseColor("#ff6347"));
    p_my.setStyle(Paint.Style.FILL);

    //*** 他人の凡例 ***//
    p_other = new Paint();
    p_other.setColor(Color.parseColor("#f5f5f5"));
    p_other.setStyle(Paint.Style.FILL);

    p_outline = new Paint();
    p_outline.setStyle(Paint.Style.STROKE);
    p_outline.setColor(Color.BLACK);
    p_outline.setStrokeWidth(5);

  }
}
