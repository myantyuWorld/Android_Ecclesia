package com.example.yuichi_oba.ecclesia.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.yuichi_oba.ecclesia.activity.ReserveListActivity;
import com.example.yuichi_oba.ecclesia.model.Reserve;
import com.example.yuichi_oba.ecclesia.tools.DB;
import com.example.yuichi_oba.ecclesia.tools.NameConst;

import java.util.ArrayList;
import java.util.List;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.MAX_WIDTH;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.ROOM_A;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.ROOM_B;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.ROOM_C;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.TOKUBETSU;
import static com.example.yuichi_oba.ecclesia.tools.NameConst.ZERO;

/**
 * Created by Yuichi-Oba on 2017/08/28.
 */

public class TimeTableView extends View implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    //*** 早期退出」オプション選択時の ダイアログフラグメントクラス ***//
//    public static class EarlyOutDialog extends DialogFragment {
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            return new AlertDialog.Builder(getActivity())
//                    .setTitle("早期退出")
//                    .setMessage("早期退出しますか？")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(getActivity(), "早期退出", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                        }
//                    })
//                    .create();
//        }
//
//        @Override
//        public void onPause() {
//            super.onPause();
//            dismiss();
//        }
//    }

    public static class CancelDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("予約のキャンセル")
                    .setMessage("本当にこの予約をキャンセルしますか？")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //*** Bundle で渡された引数を取得する ***//
                            String re_id = savedInstanceState.getString("re_id");
                            Log.d("call", "引数で渡された予約ID : " + re_id);

                            //*** 予約のキャンセル処理を行う ***//
                            SQLiteOpenHelper helper = new DB(getContext());
                            SQLiteDatabase db = helper.getWritableDatabase();

                            //*** 予約レコードの削除を行うSQL実行 ***//
                            int result = db.delete("t_reserve", "re_id = ?", new String[]{re_id});
                            Log.d("call", "処理件数 : " + result);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }


    public static final int Y_HEIGHT = 40;
    public static final int X_WIGDH = 216;

    public static final int RE_ID = 0;
    public static final int RE_OVERVIEW = 1;
    public static final int RE_START_DAY = 2;
    public static final int RE_END_DAY = 3;
    public static final int RE_START_TIME = 4;
    public static final int RE_END_TIME = 5;
    public static final int RE_SWITCH = 6;
    public static final int RE_ROOM_ID = 10;

    //*** Field ***//
    private Paint p;
    private Paint p2;
    private Paint room;
    private Paint tokubetsu;
    private Paint roomA;
    private Paint roomB;
    private Paint roomC;
    private Paint p_txtTime;
    private Paint p_txtConference;
    private Paint p_myConference;           //*** 自分の会議用 ***//
    private Paint p_myConference_waku;
    private Paint p_otherConference;        //*** 他人の会議用 ***//

    public static float x = 0;    // タップしたｘ座標
    public static float y = 0;    // タップしたｙ座標

    GestureDetector detector;

    private float[] timeFloats;
    public boolean thread_flg;
    private List<Reserve> reserveInfo;      //*** 自分の会議記録用リスト ***//
    private List<Reserve> reserveOther;     //*** 他人の会議記録用リスト ***//

    //*** Constractor ***//
    public TimeTableView(Context context) {
        super(context);
        init();
    }
    public TimeTableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public TimeTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    //*** 描画するメソッド ***//
    @Override
    protected void onDraw(Canvas c) {
        Log.d("call", "TimeTableView->onDraw()");

        //*** 特別ABC列の色の描画 ***//
        // l t r b

        c.drawRect(216, 3, 432, 2000, tokubetsu );
        c.drawRect(432, 3, 648, 2000, roomA);
        c.drawRect(648, 3, 864, 2000, roomB);
        c.drawRect(864, 3, 1078, 2000, roomC);



        timeFloats = new float[24];
        for (int i = 0, j = 100; i < timeFloats.length; i++) {
            timeFloats[i] = j;
            j += Y_HEIGHT * 2;
        }
        // 特別会議室 Ａ Ｂ Ｃ 列の描画
        float x = X_WIGDH; // 216
        float room_y = 100;
        c.drawRect(ZERO, 0, MAX_WIDTH, room_y, p);
        c.drawLine(ZERO, 0, ZERO, room_y, p);   // sx sy ex ey
        for (int i = 1; i <= 4; i++) {
            c.drawLine(i * x, 0, i * x, room_y, p);
        }




        // 会議室名の描画
        float y_conference = 60;
        c.drawText("特別", 316, y_conference, p_txtConference);
        c.drawText("A", 532, y_conference, p_txtConference);
        c.drawText("B", 748, y_conference, p_txtConference);
        c.drawText("C", 964, y_conference, p_txtConference);

        // 時間割の枠の描画
        onDrawTimeTable(c);
        // 時間の文字の描画 text x y paint
        for (int i = 0, j = 150; i < 24; i++) {
            String time = String.format("%02d:00", i);
            c.drawText(time, 100, j, p_txtTime);
            j += Y_HEIGHT * 2;
        }

        // アプリを立ち上げた社員の予約情報の描画
        onDrawConference(c);


    }
    //*** 会議を角丸で描画するメソッド ***//
    private void onDrawConference(Canvas c) {
        int cnt = 0;
        //*** 他人の参加会議に対する処理 ***//
        for (Reserve r : reserveOther) {
            String sTime = r.getRe_startTime();
            String eTime = r.getRe_endTime();
            String room_id = r.getRe_room_id();

            RectF rectF = retRectCooperation(sTime, eTime, room_id);
            // 予約会議のざ行情報を記録する
            reserveOther.get(cnt).setCoop(new float[]{rectF.left, rectF.top, rectF.right, rectF.bottom});

            c.drawRoundRect(rectF, 30, 30, p_otherConference);
            c.drawRoundRect(rectF, 30, 30, p_myConference_waku);
            cnt++;
        }
        cnt = 0;
        //*** 自分の参加会議に対する処理 ***//
        for (Reserve r : reserveInfo) {
            String sTime = r.getRe_startTime();
            String eTime = r.getRe_endTime();
            String room_id = r.getRe_room_id();

            RectF rectF = retRectCooperation(sTime, eTime, room_id);
            // 予約会議の座標情報を記録する
            reserveInfo.get(cnt).setCoop(new float[]{rectF.left, rectF.top, rectF.right, rectF.bottom});
//            switch (room_id) {
//                case "0001":
//                    room = tokubetsu;
//                    break;
//                case "0002":
//                    room = roomA;
//                    break;
//                case "0003":
//                    room = roomB;
//                    break;
//                case "0004":
//                    room = roomC;
//                    break;
//            }
            // 予約会議の描画
            c.drawRoundRect(rectF, 30, 30, p_myConference);
            c.drawRoundRect(rectF, 30, 30, p_myConference_waku);
            cnt++;
        }

    }
    //*** 開始終了時刻・会議室を基に、描画すべき座標を返すメソッド ***//
    private RectF retRectCooperation(String sTime, String eTime, String room_id) {
        float sX = 0, eX = 0, sY = 0, eY = 0;
        float x = 216;
        float padding = 2;

        switch (room_id) {
            case TOKUBETSU:
                sX = x + padding;
                eX = 2 * x - padding;
                break;
            case ROOM_A:
                sX = 2 * x + padding;
                eX = 3 * x - padding;
                break;
            case ROOM_B:
                sX = 3 * x + padding;
                eX = 4 * x - padding;
                break;
            case ROOM_C:
                sX = 4 * x + padding;
                eX = 5 * x - padding;
                break;
        }
        int s = Integer.parseInt(sTime.split("：")[0]); // 08:00 -> 8 => 8 - 8 = 0
        sY = timeFloats[s] + padding;
        if (Integer.parseInt(sTime.split("：")[1]) >= 30) {
            sY += Y_HEIGHT + padding;
        }

        int e = Integer.parseInt(eTime.split("：")[0]); // 10:30 -> 10 - 8 = 2
        eY = timeFloats[e] - padding;
        if (Integer.parseInt(eTime.split("：")[1]) >= 30) { // 30 >= 30
            eY += Y_HEIGHT - padding;
        }

        return new RectF(sX, sY, eX, eY);
    }
    //*** 時間割の枠の描画 ***//
    private void onDrawTimeTable(Canvas canvas) {
        float x = 216;
        float y_timetable = 100;
        for (int i = 1; i <= 4; i++) {
            canvas.drawLine(i * x, y_timetable, i * x, 2200, p2);
        }
        // 中の線の線
        float y = Y_HEIGHT;
        for (int i = 1; i < 48; i++) {
            canvas.drawLine(x, y_timetable + i * y, MAX_WIDTH, y_timetable + i * y, p2);
            if (i % 2 == 0) {
                canvas.drawLine(ZERO, y_timetable + i * y, x, y_timetable + i * y, p);
            }
        }
        canvas.drawRect(ZERO, y_timetable, MAX_WIDTH, y_timetable + 48 * y, p);
    }
    //*** Paintクラスの初期化処理メソッド ***//
    private void init() {
        Log.d("call", "call TimeTableView->init()");


        reserveInfo = new ArrayList<>();    //***  ***//
        reserveOther = new ArrayList<>();   //***  ***//

        detector = new GestureDetector(ReserveListActivity.getInstance(), this);

        // 枠線用
        p = new Paint();
        p.setColor(Color.DKGRAY);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);

        p2 = new Paint();
        p2.setStrokeWidth(2.0f);

        // 特別会議室用
        tokubetsu = new Paint();
        tokubetsu.setColor(Color.parseColor("#ffb6c1"));
        tokubetsu.setStyle(Paint.Style.FILL);
        tokubetsu.setStrokeWidth(10);

        // 会議室Ａ用
        roomA = new Paint();
        roomA.setColor(Color.parseColor("#add8e6"));
        roomA.setStyle(Paint.Style.FILL);
        roomA.setStrokeWidth(10);

        // 会議室Ｂ用
        roomB = new Paint();
        roomB.setColor(Color.parseColor("#98fb98"));
        roomB.setStyle(Paint.Style.FILL);
        roomB.setStrokeWidth(10);

        // 会議室Ｃ用
        roomC = new Paint();
        roomC.setColor(Color.parseColor("#fffacd"));
        roomC.setStyle(Paint.Style.FILL);
        roomC.setStrokeWidth(10);

        // テキスト用
        p_txtTime = new Paint();
        p_txtTime.setTypeface(Typeface.MONOSPACE);
        p_txtTime.setTextSize(40);
        p_txtTime.setTextAlign(Paint.Align.CENTER);
        p_txtTime.setColor(Color.BLACK);

        p_txtConference = new Paint();
        p_txtConference.setTypeface(Typeface.MONOSPACE);
        p_txtConference.setTextSize(60);
        p_txtConference.setTextAlign(Paint.Align.CENTER);
        p_txtConference.setColor(Color.BLACK);

        //*** 自分の会議の描画用 ***//
        p_myConference = new Paint();
        p_myConference.setColor(Color.parseColor("#ff6347"));       //*** 再考の余地あり ***//
        p_myConference.setStyle(Paint.Style.FILL);
        p_myConference.setStrokeWidth(10);

        p_myConference_waku = new Paint();
        p_myConference_waku.setColor(Color.parseColor("#000000"));
        p_myConference_waku.setStyle(Paint.Style.STROKE);
        p_myConference_waku.setStrokeWidth(3);

        //*** 他人の会議の描画用 ***//
        p_otherConference = new Paint();
        p_otherConference.setColor(Color.parseColor("#f5f5f5"));    //*** 再考の余地あり ***//
        p_otherConference.setStyle(Paint.Style.FILL);
        p_otherConference.setStrokeWidth(10);
    }
    //*** 画面タッチ時のイベント ***//
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_DOWN:
                Log.d("call", "TimeTableView->onTouchEvent()");
                // タップした座標を取得する
                x = e.getX();
                y = e.getY();
                Log.d("call", e.getX() + " : " + e.getY());
                Log.d("call", String.valueOf(x) + " : " + String.valueOf(y));
                break;
        }
        if (detector.onTouchEvent(e)) return true;

        return true;
    }
    //*** 再描画を行うメソッド ***//
    public void reView(String emp_id, String date) {
        // DO: 2017/09/06 review()コールで、引数の日付をデータベース検索をかけたのち、自身のreserveInfoに格納する-> invalidate() で描画する
        Log.d("call", "TimeTableView->reView()");
        SQLiteOpenHelper helper = new DB(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        //*** 自分の参加会議の検索 ***//
        Cursor c = db.rawQuery("select * from v_reserve_member where mem_id = ? and re_startday = ?", new String[]{emp_id, date});
        reserveInfo.clear();
        while (c.moveToNext()) {
            // 予約情報のインスタンス生成
            // TODO: 2017/09/15 ここで、検索結果のすべてを記録する
            Reserve r = new Reserve();
            r.setRe_id(c.getString(RE_ID));
            r.setRe_name(c.getString(1));
            r.setRe_startDay(c.getString(2));
            r.setRe_endDay(c.getString(3));
            r.setRe_startTime(c.getString(4));
            r.setRe_endTime(c.getString(5));
            r.setRe_switch(c.getString(6));
            r.setRe_fixtures(c.getString(7));
            r.setRe_remarks(c.getString(8));
            r.setRe_room_id(c.getString(10));
            r.setRe_pur_priority(c.getString(18));


            reserveInfo.add(r);
            Log.d("call", c.getString(2) + " : " + c.getString(3));
        }
        c.close();

        //*** 他人の参加会議の検索 ***//
        c = db.rawQuery("select * from v_reserve_member where mem_id <> ? and re_startday = ?", new String[]{emp_id, date});
        while (c.moveToNext()) {
            Reserve r = new Reserve();
            r.setRe_id(c.getString(RE_ID));
            r.setRe_name(c.getString(1));
            r.setRe_startDay(c.getString(2));
            r.setRe_endDay(c.getString(3));
            r.setRe_startTime(c.getString(4));
            r.setRe_endTime(c.getString(5));
            r.setRe_switch(c.getString(6));
            r.setRe_fixtures(c.getString(7));
            r.setRe_remarks(c.getString(8));
            r.setRe_room_id(c.getString(10));
            r.setRe_pur_priority(c.getString(18));

            reserveOther.add(r);
        }
        c.close();
        invalidate();
    }
    //*** タップした会議の予約ＩＤを返すメソッド ***//
    public String getSelectedReserve() {
        Log.d("call", "TimeTableView->getSelectedReserve()");
        //
        String re_id = "";
        Log.d("call", String.valueOf(thread_flg));
//        thread_flg = true;
        while (thread_flg) {
            float wX = 216;
            // タッチされたか
            if (isTouched()) {
                if (x > wX && x < 2 * wX) {
                    Log.d("call", "tokubetu");
                    re_id = TOKUBETSU;
                } else if (x > 2 * wX && x < 3 * wX) {
                    Log.d("call", "roomA");
                    re_id = ROOM_A;
                } else if (x > 3 * wX && x < 4 * wX) {
                    Log.d("call", "roomB");
                    re_id = ROOM_B;
                } else if (x > 4 * wX && x < 5 * wX) {
                    Log.d("call", "roomC");
                    re_id = ROOM_C;
                }
                // re_id と y座標を基に、どの会議がタップされたかを返す
                int cnt = 0;
                for (Reserve r : reserveInfo) {
                    if (r.getCoop() != null && r.getCoop()[1] < y && r.getCoop()[3] > y) {
                        // 特定した
                        if (re_id.equals(r.getRe_room_id())) {
                            Log.d("call", "会議を特定した！  " + r.getRe_room_id());
                            re_id = r.getRe_id();
                            thread_flg = false;
                            return re_id;
                        }
                        cnt++;
                    }
                }
                Log.d("call", "cnt :: " + String.valueOf(cnt));
                Log.d("call", re_id);
                // TODO: 2017/09/06 該当する会議がないー→ 新規会議の予約画面に移行するロジックの実装
                if (cnt > 0) {
                    Log.d("call", "新規会議の登録ロジック開始！");
                    return NameConst.NONE;
                }
//                x = 0;
//                y = 0;
            } else {
//                Log.d("call", "no Touch");
            }
        }
        Log.d("call", "Re_id : " + re_id);
        return re_id;
    }
    //*** x y の値を基に、ユーザがタッチしたのか否かを返すメソッド ***//
    public boolean isTouched() {
//        Log.d("call", "call TimeTableView->isTouched()");
        if (x != 0 && y != 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("call", "onDown");
        return true;
    }
    @Override
    public void onShowPress(MotionEvent e) {

    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d("call", "onSingleTapUp!");
        return true;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }
    @Override
    public void onLongPress(MotionEvent e) {
        Toast.makeText(ReserveListActivity.getInstance(), "この予約をキャンセルしますか？", Toast.LENGTH_SHORT).show();
        Log.d("call", "LongTouch");

        //*** タップした会議の予約IDを求めて代入する ***//
        String re_id = getSelectedReserve();

        //*** キャンセルダイアログの生成 ***//
        CancelDialog cancelDialog = new CancelDialog();
        Bundle bundle = new Bundle();
        bundle.putString("re_id", re_id);       //*** Bundle に予約IDを渡す ***//
        cancelDialog.show(ReserveListActivity.getInstance().getFragmentManager(), "cancel");
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("call", "onFling");
        return false;
    }
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d("call", "onSingleTapConfirmed");
        return false;
    }
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d("call", "onDoubleTap");
        return false;
    }
    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d("call", "onDoubleTapEvent");
        return false;
    }
}
