package com.example.yuichi_oba.ecclesia.tools;

/**
 * Created by 5151021 on 2017/08/23.
 */

public class NameConst {
    public static final int MAX_HEIGHT = 1700;
    public static final int MAX_WIDTH = 1080;
    public static final int LINE_WIDGH = 20;

    //*** 汎用 ***//
    public static final int MINUSONE = -1;
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final int TEN = 10;
    public static final int EREVEN = 11;
    public static final int M_THIRTY = -30;
    public static final String NONE = "0";

    public static final int REQUEST_CODE = 1;

    public static final String TRUE = "1";
    public static final String FALSE = "false";

    public static final String CALL = "call";
    public static final String BTNDAYFORMAT = "%04d/%02d/%02d";

    public static final String EX = "延長";
    public static final String EARLY = "早期退出";
    public static final String RESERVECHANGE = "予約変更";
    public static final String CONFIRM = "確認";
    public static final String COMPLETE = "完了";
    public static final String RUNQUESTION = "を実行しますか？";
    public static final String RUNMESSAGE = "が完了しました";
    public static final String OK = "OK";
    public static final String IN = "社内";
    public static final String OUT = "社外";
    public static final String CANCEL = "キャンセル";
    public static final String MEMBERYETADD = "参加者未追加";

    public static final String KEYEX = "EX"; // 受け渡し用キー
    public static final String KEYRESULT = "result";
    public static final String KEYEAR = "ear";
    public static final String KEYSMALLEX = "ex";
    public static final String KEYCHECK = "Check";
    public static final String KEYCHANGE = "Change";
    public static final String KEYOUT = "out";
    public static final String YYYY_MM_DD_HH_MM = "yyyy/MM/dd HH：mm";
    public static final String HH_MM = "HH：mm";
    public static final String SPACE = " ";
    public static final String FULLSPACE = "　";
    public static final String EMPTY = "";
    public static final String YYYY_MM_DD = "yyyy/MM/dd";

    // yuichi
    public static final String TOKUBETSU = "0001";
    public static final String ROOM_A = "0002";
    public static final String ROOM_B = "0003";
    public static final String ROOM_C = "0004";

    // SQL
    public static final String SQL_ALREADY_EXTENSION_CHECK = "select * from t_extension where re_id = ?";
    public static final String SQL_EARLY_OUT_EXTENSION = "update t_extension set ex_endtime = ? where re_id = ?";
    public static final String SQL_EARLY_OUT_RESERVE = "update t_reserve set re_endtime = ? where re_id = ?";
    public static final String SQL_EXTENSION_INSERT = "insert into t_extension values(?,?,?,?,?)";
    public static final String SQL_MEMBER_REPLACE = "replace into t_member values(?, ?)";
    public static final String SQL_RESERVE_UPDATE =
            "update t_reserve set re_overview = ? , re_startday = ?, re_endday = ?, re_starttime = ?, re_endtime = ?," +
            " re_switch = ?, re_fixture = ?, re_remarks = ?, re_priority = ?, room_id = ?, pur_id = ?" +
            " where re_id = ? ";
    public static final String SQL_ROOM_CAPACITY = "select room_capacity from m_room where room_id = ?";
}
