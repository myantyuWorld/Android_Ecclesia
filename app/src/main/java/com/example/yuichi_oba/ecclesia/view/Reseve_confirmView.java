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

import static com.example.yuichi_oba.ecclesia.activity.ReserveConfirmActivity.re_id;

/**
 * Created by yuki on 2017/09/08.
 */

public class Reseve_confirmView extends View {

    //*** Field ***//
    private Paint p_line;
    private Paint p_out_line;
    private Paint p_text;
    private Paint p_rect;       // 交互に色違いにする用

    private String[] name = {"概要", "目的", "開始時間", "終了時間", "申請者", "参加者", "社内/社外", "会社名", "希望会議室", "備品", "その他"};
//    private String[] content = {"システム開発の組み合わせ", "打合せ", "2017/01/18 09:00", "2017/01/18 11:00", "石山大樹", "大馬祐一 : 管理部", "社内", "株式会社Ostraca", "会議室A", "プロジェクタ", "無し"};

    Reserve reserve;

    //*** Constractor ***//
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

    //*** Paintクラスの初期化処理メソッド ***//
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
        // DO: 2017/09/16 会議目的カラムを、DBのテーブルに追加する
        c.drawText(reserve.getRe_purpose_name(), 500, y_purpose, p_text);
        c.drawText(reserve.getRe_startDay() + " " + reserve.getRe_startTime(), 500, y_start, p_text);
        c.drawText(reserve.getRe_endDay() + " " + reserve.getRe_endTime(), 500, y_end, p_text);
        // TODO: 2017/09/16 申請者カラムを、DBのテーブルに追加する
        c.drawText(reserve.getRe_applicant(), 500, y_applicant, p_text);
        // DO: 2017/09/16 参加者を検索するロジックの実装
        // TODO: 2017/09/16 折り畳み式ビュー検討：ExpandableListView
        c.drawText("", 500, y_member, p_text);
        // DO: 2017/09/16 0 =>社内 1=> 社外と表示する
        c.drawText(reserve.getRe_switch().contains("0") ? "社内" : "社外", 500, y_switch, p_text);
        // TODO: 2017/09/16 社外者がいない場合、nullでアプリが落ちるため、対処を考える
//        c.drawText(reserve.getRe_company(), 500, y_company, p_text);
        c.drawText("", 500, y_company, p_text); //*** 現状これで対処します  ***//
        c.drawText(reserve.getRe_room_name(), 500, y_room, p_text);
        c.drawText(reserve.getRe_fixtures(), 500, y_fixture, p_text);
        c.drawText(reserve.getRe_remarks(), 500, y_remark, p_text);

    }
    //*** 基本の枠の描画メソッド ***//
    private void onDrawBasic(Canvas c) {
        // l t r b
        c.drawRect(0, 1080, 0, 1920, p_line);
        float room = 120;
        float room_y = 70;
        p_text.setTextAlign(Paint.Align.LEFT);
        // sx = 左上 sy　=　左下 ex　=　幅 ey　=　右下
        c.drawLine(0, room, 1080, room, p_out_line);
        // 各項目を交互に、色違いにするロジック
        float padding = 3;
        for (int i = 1; i < 13; i += 2) {
            c.drawRect(0 + padding, room * i - room + padding, 1080 - padding, room * i - padding, p_rect);
        }

        for (int i = 1; i <= 11; i++) {
            c.drawLine(0, room * i, 1080, room * i, p_out_line);
            c.drawText(name[i - 1], 100, room_y, p_text);       // 項目の描画
//            c.drawText(content[i - 1], 500, room_y, p_text);    // 予約情報の描画
            room_y += room;
        }
    }
}
