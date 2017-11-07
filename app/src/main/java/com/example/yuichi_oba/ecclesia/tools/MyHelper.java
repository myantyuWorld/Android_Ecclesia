package com.example.yuichi_oba.ecclesia.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by yuichi_develop on 2017/10/28.
 */

//*** 新しいDBヘルパー//
public class MyHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String ECCLESIA_DB = "ecclesia.db";

    public static String Q_CREATE_M_COMPANY = "CREATE TABLE m_company ( `com_id` TEXT, `com_name` TEXT, PRIMARY KEY(`com_id`) )";
    public static String Q_CREATE_M_DEPART = "CREATE TABLE  m_depart  ( `dep_id` TEXT, `dep_name` TEXT, PRIMARY KEY(`dep_id`) )";
    public static String Q_CREATE_M_POSITION = "CREATE TABLE `m_position` ( `pos_id` TEXT, `pos_name` TEXT, `pos_priority` INTEGER, PRIMARY KEY(`pos_id`) )";
    public static String Q_CREATE_M_PURPOSE = "CREATE TABLE `m_purpose` ( `pur_id` TEXT, `pur_name` TEXT, `pur_priority` INTEGER, PRIMARY KEY(`pur_id`) )";
    public static String Q_CREATE_M_ROOM = "CREATE TABLE `m_room` ( `room_id` TEXT, `room_name` TEXT, `room_capacity` INTEGER, PRIMARY KEY(`room_id`) )";
    public static String Q_CREATE_M_EMP = "CREATE TABLE  t_emp  ( `emp_id` TEXT, `emp_name` TEXT, `emp_tel` TEXT, `emp_mailaddr` TEXT, `dep_id` TEXT, `pos_id` TEXT, PRIMARY KEY(`emp_id`), FOREIGN KEY(`pos_id`) REFERENCES m_position(pos_id) )";
    public static String Q_CREATE_M_OUT = "CREATE TABLE  m_out  ( `out_id` TEXT, `out_name` TEXT, `out_tel` TEXT, `out_mailaddr` TEXT, `dep_name` TEXT, `pos_name` TEXT, `outemp_priority` INTEGER, `com_id` TEXT, PRIMARY KEY(`out_id`,`outemp_priority`), FOREIGN KEY(`com_id`) REFERENCES `m_company`(`com_id`) )";
    public static String Q_CREATE_M_ADMIN = "CREATE TABLE  m_admin  ( `admin_id` TEXT, `admin_pass` TEXT, `emp_id` TEXT, PRIMARY KEY(`admin_id`), FOREIGN KEY(`emp_id`) REFERENCES t_emp(emp_id) )";
    public static String Q_CREATE_M_TERMINAL = "CREATE TABLE  m_terminal  ( `ter_id` TEXT, `emp_id` TEXT, PRIMARY KEY(`ter_id`), FOREIGN KEY(`emp_id`) REFERENCES  t_emp (`emp_id`) )";
    public static String Q_CREATE_T_RESERVE = "CREATE TABLE  t_reserve  ( `re_id` TEXT, `re_overview` TEXT, `re_startday` TEXT, `re_endday` TEXT, `re_starttime` BLOB, `re_endtime` TEXT, `re_switch` INTEGER, `re_fixture` TEXT, `re_remarks` TEXT, `re_priority` INTEGER, `com_id` TEXT, `emp_id` TEXT, `room_id` BLOB, `pur_id` TEXT, `re_applicant` TEXT, PRIMARY KEY(`re_id`), FOREIGN KEY(`com_id`) REFERENCES `m_company`(`com_id`), FOREIGN KEY(`pur_id`) REFERENCES `m_purpose`(`pur_id`), FOREIGN KEY(`re_applicant`) REFERENCES t_emp(emp_id) )";
    public static String Q_CREATE_T_MEMBER = "CREATE TABLE  t_member  ( `re_id` TEXT, `mem_id` TEXT, PRIMARY KEY(`re_id`,`mem_id`) )";
    public static String Q_CREATE_T_EXTENSION = "CREATE TABLE `t_extension` ( `re_id` TEXT, `ex_startday` TEXT, `ex_endday` TEXT, `ex_starttime` TEXT, `ex_endtime` TEXT, PRIMARY KEY(`re_id`), FOREIGN KEY(`re_id`) REFERENCES reserve(re_id) )";

    public static String Q_VIEW_V_EMPLOYEE = "CREATE VIEW v_employee as select e.emp_id, e.emp_name, e.emp_tel, e.emp_mailaddr, d.dep_id, d.dep_name, p.pos_id, p.pos_name, p.pos_priority from t_emp as e inner join m_depart as d on e.dep_id = d.dep_id inner join m_position as p on e.pos_id = p.pos_id";
    public static String Q_VIEW_V_OUT_EMP = "CREATE VIEW v_out_emp as select x.re_id, x.mem_id, y.outemp_name, y.outemp_tel, y.outemp_mailaddr, y.dep_name, y.pos_name, y.outemp_priority, z.com_id, z.com_name from (t_member x inner join m_out_emp y on x.mem_id = y.outemp_id) inner join m_company z on y.com_id = z.com_id";
    public static String Q_VIEW_V_RESERVE_MEMBER = "CREATE VIEW v_reserve_member as select x.re_id, x.re_overview, x.re_startday, x.re_endday, x.re_starttime, x.re_endtime, x.re_switch, x.re_fixture, x.re_remarks, x.re_priority, x.room_id, y.mem_id, z.emp_name, z.emp_tel, z.emp_mailaddr, a.dep_name, b.pos_name, b.pos_priority, p.pur_id, p.pur_name,p.pur_priority, x.re_applicant from t_reserve x inner join t_member y on x.re_id = y.re_id inner join t_emp z on y.mem_id = z.emp_id inner join m_depart as a on z.dep_id = a.dep_id inner join m_position as b on z.pos_id = b.pos_id inner join m_purpose as p on x.pur_id = p.pur_id";
    public static String Q_VIEW_V_RESERVE_OUT_MEM = "CREATE VIEW v_reserve_out_member as select x.re_id, x.re_overview, x.re_startday, x.re_endday, x.re_starttime, x.re_endtime, x.re_switch, x.re_fixture, x.re_remarks, x.re_priority, y.mem_id, a.out_name, a.out_tel, a.out_mailaddr, a.dep_name, a.pos_name, a.outemp_priority, a.com_id , b.com_name from t_reserve x inner join t_member y on x.re_id = y.re_id inner join m_out as a on y.mem_id = a.out_id inner join m_company as b on a.com_id = b.com_id";

    public static String Q_INSERT_COMPANY = "INSERT INTO `m_company` (com_id,com_name) VALUES ('0001','株式会社Ostraca'),  " +
            " ('0002','株式会社Ecclesia'),  " +
            " ('0003','株式会社REI'),  " +
            " ('0004','エイダエンジニアリング'),  " +
            " ('0005','アタカ工業'),  " +
            " ('0006','イーグル友田'),  " +
            " ('0007','アマノ'),  " +
            " ('0008','エス・イー・エス'),  " +
            " ('0009','エヌピーシー'),  " +
            " ('0010','エンシュウ'),  " +
            " ('0011','オイエス工業'),  " +
            " ('0012','株式会社ネクストビジョン'),  " +
            " ('0013','オプティマ'),  " +
            " ('0014','イワキ'),  " +
            " ('0015','ＮＴＮ'),  " +
            " ('0000','社内利用');";
    public static String Q_INSERT_DEPART = "INSERT INTO `m_depart` (dep_id,dep_name) VALUES ('0001','広報部'),  " +
            " ('0002','開発部'),  " +
            " ('0003','総務部'),  " +
            " ('0004','企画部'),  " +
            " ('0005','生活部'),  " +
            " ('0006','環境部'),  " +
            " ('0007','税務部'),  " +
            " ('0008','福祉部'),  " +
            " ('0009','建設部'),  " +
            " ('0010','経理部');";
    public static String Q_INSERT_POSITION = "INSERT INTO `m_position` (pos_id,pos_name,pos_priority) VALUES ('0001','取締役会長',9),  " +
            " ('0002','代表取締役',8),  " +
            " ('0003','取締役',7),  " +
            " ('0004','執行役員',6),  " +
            " ('0005','部長',5),  " +
            " ('0006','課長',4),  " +
            " ('0007','係長',3),  " +
            " ('0008','主任',2),  " +
            " ('0009','一般社員',1);";
    public static String Q_INSERT_PURPOSE = "INSERT INTO `m_purpose` (pur_id,pur_name,pur_priority) VALUES ('0001','事業方針',7),  " +
            " ('0002','プロジェクト報告',6),  " +
            " ('0003','決算報告',5),  " +
            " ('0004','契約',4),  " +
            " ('0005','商談',3),  " +
            " ('0006','定例会',2),  " +
            " ('0007','打合せ',1);";
    public static String Q_INSERT_ROOM = "INSERT INTO `m_room` (room_id,room_name,room_capacity) VALUES ('0001','特別会議室',50),  " +
            " ('0002','会議室Ａ',20),  " +
            " ('0003','会議室Ｂ',10),  " +
            " ('0004','会議室Ｃ',5);";
    public static String Q_INSERT_EMP = "INSERT INTO `t_emp` (emp_id,emp_name,emp_tel,emp_mailaddr,dep_id,pos_id) VALUES ('0001','石山大樹','0823123456','5151002@st.hsc.ac.jp','0001','0004'),  " +
            " ('0002','大馬裕一','0823234567','5151021@st.hsc.ac.jp','0002','0005'),  " +
            " ('0003','國貞仁貴','0823345678','5151013@st.hsc.ac.jp','0002','0006'),  " +
            " ('0004','築山大輝','08234567891','5151234@st.hsc.ac.jp','0004','0007'),  " +
            " ('0005','西川浩平','08235678912','5152345@st.hsc.ac.jp','0005','0008'),  " +
            " ('0006','森口裕基','08236789123','5153456@st.hsc.ac.jp','0006','0009'),  " +
            " ('0007','吉本直人','08237891234','5154567@st.hsc.ac.jp','0007','0003'),  " +
            " ('0030','森本慎也','08238912345','morimoto@hsc.ac.jp','0002','0001'),  " +
            " ('0008','矢田太郎','08235678912','test@test.com','0004','0006'),  " +
            " ('0009','山田花子','08236789123','test@test.com','0001','0007'),  " +
            " ('0010','吉川芳樹','08237891234','test@test.com','0004','0008'),  " +
            " ('0011','加藤時雄','08235678912','test@test.com','0005','0009'),  " +
            " ('0012','定森守男','08236789123','test@test.com','0006','0003'),  " +
            " ('0013','吉木好','08237891234','test@test.com','0007','0005'),  " +
            " ('0014','政野昭','08238912345','test@test.com','0002','0006'),  " +
            " ('0015','佐藤隆','08235678912','test@test.com','0006','0005'),  " +
            " ('0016','加藤貴子','08236789123','test@test.com','0007','0006'),  " +
            " ('0017','伊藤喜朗','08237891234','test@test.com','0002','0007'),  " +
            " ('0018','加賀武見','08238912345','test@test.com','0004','0008'),  " +
            " ('0019','吉木りさ','08235678912','test@test.com','0001','0009'),  " +
            " ('0020','森本武','08235678912','test@test.com','0006','0003'),  " +
            " ('0021','井上正和','08236789123','test@test.com','0007','0005'),  " +
            " ('0022','山中剛','08237891234','test@test.com','0006','0006'),  " +
            " ('0023','吉田拓郎','08238912345','test@test.com','0007','0007'),  " +
            " ('0024','野田かおり','08235678912','test@test.com','0002','0008'),  " +
            " ('0025','野村健介','08236789123','test@test.com','0004','0009'),  " +
            " ('0026','佐々木圭佑','08237891234','test@test.com','0001','0003');";
    public static String Q_INSERT_OUT = "INSERT INTO `m_out` (out_id,out_name,out_tel,out_mailaddr,dep_name,pos_name,outemp_priority,com_id) VALUES ('1001','小俣 大輔 ','0435782511','test@test.com','開発部','係長',3,'0015'),  " +
            " ('1002','植田 康平 ','0852255135','test@test.com','総務部','係長',3,'0014'),  " +
            " ('1003','二ノ宮 拓哉 ','0582024656','test@test.com','経理部','執行役員',6,'0013'),  " +
            " ('1004','桂田 真司 ','05823577','test@test.com','開発部','執行役員',6,'0012'),  " +
            " ('1005','濱川 進一 ','0588215454','test@test.com','企画部','一般社員',1,'0012'),  " +
            " ('1006','高嶺 健夫','069821444','test@test.com','経理部','一般社員',1,'0007'),  " +
            " ('1007','佐貫 祐次 ','0823655456','test@test.com','生活部','一般社員',1,'0008'),  " +
            " ('1008','羽生 輝久 ','0824452444','test@test.com','環境部','一般社員',1,'0004'),  " +
            " ('1009','矢本 一広 ','0822456446','test@test.com','経理部','取締役会長',8,'0005'),  " +
            " ('1010','新 英紀 ','0823568576','test@test.com','建設部','代表取締役',9,'0001');";
    public static String Q_INSERT_ADMIN = "INSERT INTO `m_admin` (admin_id,admin_pass,emp_id) VALUES ('123456','123456','9999');";
    public static String Q_INSERT_TERMINAL = "INSERT INTO `m_terminal` (ter_id,emp_id) VALUES ('0','0002'),  " +
            " ('1','9999'),  " +
            " ('353608065049143','0001');";
    public static String Q_INSERT_RESERVE = "INSERT INTO `t_reserve` (re_id,re_overview,re_startday,re_endday,re_starttime,re_endtime,re_switch,re_fixture,re_remarks,re_priority,com_id,emp_id,room_id,pur_id,re_applicant) VALUES ('0001','新規プロジェクト報告','2018/01/17','2018/01/17','08：00','09：00',1,'プロジェクタ','なし',7,'0001','0002','0001','0001','0002'),  " +
            " ('0002','進捗の会議','2018/01/17','2018/01/17','08：00','10：30',1,'プロジェクタ','なし',5,'0002','0002','0002','0002','0002'),  " +
            " ('0003','パンフレット説明','2018/01/17','2018/01/17','11：00','13：30',1,'プロジェクタ','なし',4,'0003','0001','0003','0003','0001'),  " +
            " ('0004','売り込み','2018/01/17','2018/01/17','08：00','09：00',0,'プロジェクタ','なし',6,'0000','0002','0004','0003','0002'),  " +
            " ('0005','決算報告','2018/01/17','2018/01/17','10：00','11：30',0,'プロジェクタ','なし',10,'0000','0002','0001','0002','0002'),  " +
            " ('0006','打合せ','2018/01/18','2018/01/18','09：30','10：30',1,'プロジェクタ','なし',4,'0012','0001','0002','0001','0001'),  " +
            " ('0007','打合せ','2018/01/18','2018/01/18','10：00','12：00',0,'プロジェクタ','なし',4,'0000','0003','0003','0004','0003'),  " +
            " ('0008','打合せ','2018/01/18','2018/01/18','10：00','12：00',1,'プロジェクタ','なし',5,'0002','0004','0004','0003','0004'),  " +
            " ('0009','打合せ','2018/01/18','2018/01/18','08：00','09：30',1,'プロジェクタ','なし',6,'0004','0005','0001','0004','0005'),  " +
            " ('0010','打合せ','2018/01/18','2018/01/18','10：00','12：00',1,'プロジェクタ','なし',4,'0005','0002','0002','0001','0002'),  " +
            " ('0011','売り込み','2018/01/19','2018/01/19','12：30','13：30',1,'プロジェクタ','なし',10,'0002','0001','0003','0003','0001'),  " +
            " ('0012','進捗の会議','2018/01/19','2018/01/19','10：00','12：00',1,'プロジェクタ','なし',4,'0001','0002','0004','0002','0002'),  " +
            " ('0013','新規プロジェクト報告','2018/01/19','2018/01/19','09：30','10：30',1,'プロジェクタ','なし',4,'0002','0005','0001','0004','0005'),  " +
            " ('0014','進捗の会議','2018/01/19','2018/01/19','10：00','12：00',1,'プロジェクタ','なし',4,'0004','0007','0001','0005','0007'),  " +
            " ('0015','パンフレット説明','2018/01/19','2018/01/19','10：00','12：00',1,'プロジェクタ','なし',5,'0011','0001','0002','0001','0001'),  " +
            " ('0016','売り込み','2018/01/19','2018/01/19','08：00','09：30',1,'プロジェクタ','なし',6,'0002','0002','0003','0002','0002'),  " +
            " ('0017','決算報告','2018/01/19','2018/01/19','10：00','12：00',1,'プロジェクタ','なし',4,'0001','0001','0004','0003','0001');";
    public static String Q_INSERT_MEMBER = "INSERT INTO `t_member` (re_id,mem_id) VALUES ('0001','0001'),  " +
            " ('0001','0002'),  " +
            " ('0001','0003'),  " +
            " ('0001','0004'),  " +
            " ('0001','1000'),  " +
            " ('0002','0001'),  " +
            " ('0002','0003'),  " +
            " ('0002','0004'),  " +
            " ('0003','0001'),  " +
            " ('0003','0002'),  " +
            " ('0003','0003'),  " +
            " ('0003','0004'),  " +
            " ('0003','0005'),  " +
            " ('0003','0006'),  " +
            " ('0003','1002'),  " +
            " ('0004','0001'),  " +
            " ('0004','0002'),  " +
            " ('0004','0003'),  " +
            " ('0004','0004'),  " +
            " ('0004','0005'),  " +
            " ('0004','0006'),  " +
            " ('0004','0007'),  " +
            " ('0005','0002'),  " +
            " ('0005','0004'),  " +
            " ('0005','0005'),  " +
            " ('0005','0006'),  " +
            " ('0005','0007'),  " +
            " ('0006','0002'),  " +
            " ('0006','0003'),  " +
            " ('0006','0004'),  " +
            " ('0006','1003'),  " +
            " ('0006','0007'),  " +
            " ('0006','0010'),  " +
            " ('0006','0012'),  " +
            " ('0006','0015'),  " +
            " ('0007','0002'),  " +
            " ('0007','0001'),  " +
            " ('0007','0003'),  " +
            " ('0007','0006'),  " +
            " ('0007','0009'),  " +
            " ('0007','0012'),  " +
            " ('0007','0020'),  " +
            " ('0008','0001'),  " +
            " ('0008','0003'),  " +
            " ('0008','0005'),  " +
            " ('0008','0002'),  " +
            " ('0008','0011'),  " +
            " ('0008','0020'),  " +
            " ('0008','0025'),  " +
            " ('0009','0001'),  " +
            " ('0009','0002'),  " +
            " ('0009','0005'),  " +
            " ('0009','0012'),  " +
            " ('0009','0020'),  " +
            " ('0009','0021');";
    public static String Q_INSERT_EXTENSION = "";


    //***  ***//

    //***  ***//
    public MyHelper(Context context) {
        super(context, ECCLESIA_DB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //*** CREATE TABLE ***//
        db.execSQL(Q_CREATE_M_COMPANY);
        db.execSQL(Q_CREATE_M_DEPART);
        db.execSQL(Q_CREATE_M_POSITION);
        db.execSQL(Q_CREATE_M_PURPOSE);
        db.execSQL(Q_CREATE_M_ROOM);
        db.execSQL(Q_CREATE_M_EMP);
        db.execSQL(Q_CREATE_M_OUT);
        db.execSQL(Q_CREATE_M_ADMIN);
        db.execSQL(Q_CREATE_M_TERMINAL);
        db.execSQL(Q_CREATE_T_RESERVE);
        db.execSQL(Q_CREATE_T_MEMBER);
        db.execSQL(Q_CREATE_T_EXTENSION);

        db.execSQL(Q_VIEW_V_EMPLOYEE);
        db.execSQL(Q_VIEW_V_OUT_EMP);
        db.execSQL(Q_VIEW_V_RESERVE_MEMBER);
        db.execSQL(Q_VIEW_V_RESERVE_OUT_MEM);


        db.execSQL(Q_INSERT_COMPANY);
        db.execSQL(Q_INSERT_DEPART);
        db.execSQL(Q_INSERT_POSITION);
        db.execSQL(Q_INSERT_PURPOSE);
        db.execSQL(Q_INSERT_ROOM);
        db.execSQL(Q_INSERT_EMP);
        db.execSQL(Q_INSERT_OUT);
        db.execSQL(Q_INSERT_ADMIN);
        db.execSQL(Q_INSERT_TERMINAL);
        db.execSQL(Q_INSERT_RESERVE);
        db.execSQL(Q_INSERT_MEMBER);
//        db.execSQL(Q_INSERT_EXTENSION);


        Cursor c = db.rawQuery("select * from m_admin", null);
        if (c.moveToNext()) {
            Log.d("call", c.getString(0));
        }
        c.close();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
