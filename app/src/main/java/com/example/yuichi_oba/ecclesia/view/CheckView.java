package com.example.yuichi_oba.ecclesia.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.yuichi_oba.ecclesia.activity.ReserveConfirmActivity;
import com.example.yuichi_oba.ecclesia.model.Reserve;

import static com.example.yuichi_oba.ecclesia.activity.ReserveChangeActivity.changes;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.*;

/**
 * Created by 5151002 on 2017/10/17.
 */

public class CheckView extends View {
    //*** 描画情報 ***//
    private Paint p_line;
    private Paint p_out_line;
    private Paint p_text;
    private Paint p_rect;
    private Paint p_change;

    //*** 項目名 ***//
    private String[] name = {"概要", "目的", "開始時間", "終了時間", "申請者", "参加者", "社内/社外", "会社名", "希望会議室", "備品", "その他"};
    //*** 変更前情報を保持 ***//
    private String[] before;

    Reserve reserve;

    //*** コンストラクター ***//
    public CheckView(Context context) {
        super(context);
        if (isInEditMode()){
            return;
        }
        init();
    }

    public CheckView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        init();
    }

    public CheckView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            return;
        }
        init();
    }

    //*** SelfMadeMethod ***//
    //*** 描画情報セット ***//
    private void init() {
        //*** 枠線用 ***//
        p_line = new Paint();
        p_line.setColor(Color.DKGRAY);
        p_line.setStyle(Paint.Style.STROKE);
        p_line.setStrokeWidth(10);

        p_out_line = new Paint();
        p_out_line.setStrokeWidth(2.0f);


        //*** テキスト用 ***//
        p_text = new Paint();
        p_text.setTypeface(Typeface.MONOSPACE);
        p_text.setTextSize(40);
        p_text.setTextAlign(Paint.Align.CENTER);
        p_text.setColor(Color.BLACK);

        //*** 交互に枠内の色を変えるためのもの ***//
        p_rect = new Paint();
        p_rect.setColor(Color.LTGRAY);
        p_rect.setStyle(Paint.Style.FILL);

        //*** 変更箇所を表示する用の色 ***//
        p_change = new Paint();
        p_change.setColor(Color.parseColor("#87cefa"));
        p_change.setStyle(Paint.Style.FILL);

        //*** 確認画面のstaticな予約インスタンスを引っ張ってくる ***//
        reserve = new Reserve();
        reserve = Reserve.retReserveConfirm(ReserveConfirmActivity.re_id);
        Log.d("room", reserve.getRe_room_name());

        //*** 変更前情報を保持する配列に挿入 ***//
        //*** applicantの次…member  switchの次…company ***//
        before = new String[]{reserve.getRe_name(), reserve.getRe_purpose_name(), reserve.getRe_startDay() + " " + reserve.getRe_startTime(), reserve.getRe_endDay() + " " + reserve.getRe_endTime(),
                reserve.getRe_applicant(), "", reserve.getRe_switch(), "会社名", reserve.getRe_room_name(), reserve.getRe_fixtures(), reserve.getRe_remarks()};
    }

    //*** 描画メソッド ***//
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //*** 基本の枠の描画 ***//
        onDrawBasic(canvas);
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

        c.drawText(changes[ZERO], 500, y_name, p_text);
        c.drawText(changes[ONE], 500, y_purpose, p_text);
        c.drawText(changes[TWO], 500, y_start, p_text);
        c.drawText(changes[THREE], 500, y_end, p_text);
        c.drawText(changes[FOUR], 500, y_applicant, p_text);
        c.drawText(changes[FIVE], 500, y_member, p_text);
        c.drawText(changes[SIX].contains("0") ? "社内" : "社外", 500, y_switch, p_text);
        c.drawText(changes[SEVEN], 500, y_company, p_text);
        c.drawText(changes[EIGHT], 500, y_room, p_text);
        c.drawText(changes[NINE], 500, y_fixture, p_text);
        c.drawText(changes[TEN], 500, y_remark, p_text);

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
            Log.d("Be", before[i - 1]);
            Log.d("Af", changes[i - 1]);
            if (!changes[i - 1].equals(before[i - 1])) {
                c.drawRect(0 + padding, room * i - room + padding, 1080 - padding, room * i - padding, p_change);
            }
            c.drawLine(0, room * i, 1080, room * i, p_out_line);
            c.drawText(name[i - 1], 100, room_y, p_text);
            room_y += room;
        }
    }
}
