package com.example.yuichi_oba.ecclesia.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.yuichi_oba.ecclesia.model.Reserve;

import static com.example.yuichi_oba.ecclesia.activity.ReserveChangeActivity.changes;
import static com.example.yuichi_oba.ecclesia.activity.ReserveConfirmActivity.re_id;

public class ReseveCheckView extends View {

    private Paint p_line;
    private Paint p_out_line;
    private Paint p_text;
    private Paint p_rect;
    private Paint p_change;

    private String[] name = {"概要", "目的", "開始時間", "終了時間", "申請者", "参加者", "社内/社外", "会社名", "希望会議室", "備品", "その他"};
    private String[] before;

    Reserve reserve;

    public ReseveCheckView(Context context) {
        super(context);
        init();
    }

    public ReseveCheckView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReseveCheckView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        // 色違い用
        p_rect = new Paint();
        p_rect.setColor(Color.LTGRAY);
        p_rect.setStyle(Paint.Style.FILL);

        p_change = new Paint();
        p_change.setColor(Color.RED);
        p_change.setStyle(Paint.Style.FILL);

        reserve = new Reserve();
    }
    //*** 描画メソッド ***//
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //*** 基本の枠の描画 ***//
        onDrawBasic(canvas);
        //*** 予約情報のDB検索 ***//
        reserve = Reserve.retReserveConfirm(re_id);
        before = new String[]{reserve.getRe_name(), reserve.getRe_purpose_name(), reserve.getRe_startDay() + " " + reserve.getRe_startTime(), reserve.getRe_endDay() + " " + reserve.getRe_endTime(),
                reserve.getRe_switch(), reserve.getRe_room_name(), reserve.getRe_fixtures(), reserve.getRe_remarks()};
        //*** 予約情報の描画 ***//
        onDrawReserveInfo(canvas);
    }
    //*** 予約情報の描画メソッド ***//
    private void onDrawReserveInfo(Canvas c) {
        float y_name = 70;
        float y_purpose = 190;
        float y_start = 310;
        float y_end = 430;
        float y_applicant = 550;
        float y_member = 670;
        float y_switch = 790;
        float y_company = 910;
        float y_room = 1030;
        float y_fixture = 1150;
        float y_remark = 1270;

        c.drawText(reserve.getRe_name(), 500, y_name, p_text);
        c.drawText(reserve.getRe_purpose_name(), 500, y_purpose, p_text);
        c.drawText(reserve.getRe_startDay() + " " + reserve.getRe_startTime(), 500, y_start, p_text);
        c.drawText(reserve.getRe_endDay() + " " + reserve.getRe_endTime(), 500, y_end, p_text);
        c.drawText("", 500, y_applicant, p_text);
        c.drawText("", 500, y_member, p_text);
        c.drawText(reserve.getRe_switch().contains("0") ? "社内" : "社外", 500, y_switch, p_text);
        c.drawText(reserve.getRe_room_name(), 500, y_room, p_text);
        c.drawText(reserve.getRe_fixtures(), 500, y_fixture, p_text);
        c.drawText(reserve.getRe_remarks(), 500, y_remark, p_text);

    }

    private void onDrawBasic(Canvas c) {
        c.drawRect(0, 1080, 0, 1920, p_line);
        float room = 120;
        float room_y = 70;
        p_text.setTextAlign(Paint.Align.LEFT);
        // sx = 左上 sy　=　左下 ex　=　幅 ey　=　右下
        c.drawLine(0, room, 1080, room, p_out_line);

        float padding = 3;
        for (int i = 1; i < 13; i += 2) {
            c.drawRect(0 + padding, room * i - room + padding, 1080 - padding, room * i - padding, p_rect);
        }

        for (int i = 1; i <= 11; i++) {
//            if (changes[i - 1].equals(before[i - 1])){
//
//            }
            c.drawLine(0, room * i, 1080, room * i, p_out_line);
            c.drawText(name[i - 1], 100, room_y, p_text);
            room_y += room;
        }
    }
}
