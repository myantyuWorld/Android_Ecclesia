package com.example.yuichi_oba.ecclesia.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.model.Employee;
import com.example.yuichi_oba.ecclesia.model.Person;
import com.example.yuichi_oba.ecclesia.model.Reserve;
import com.example.yuichi_oba.ecclesia.tools.MyHelper;
import com.example.yuichi_oba.ecclesia.tools.Util;

import java.util.ArrayList;
import java.util.List;

//import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;

/*************************************************************************************
 *
 *                                  Hint!
 *
 *  １．サーチビューでなんか打った時(会社名とか）にListView絞り込むやつ　  ｐ１８９
 *  ２．自作アダプタやら自作のレイアウトやらなんやら                       ｐ１６４～ｐ１９２
 *  外部設計の履歴検索にあるまんまをつくる
 *
 **************************************************************************************/

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// _/_/
// _/_/ 利用履歴を検索するアクティビティ
// _/_/
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// TODO: 2017/11/13 かいしゃスピナーの会社、履歴にあるかいしゃのみにするロジックの実装 
// TODO: 2017/11/13 会社名、参加者の文字位置、文字サイズの調整 
// TODO: 2017/11/13 ぎちぎちで見づらいので、レイアウトの調整
// TODO: 2017/11/13 メソッド分割
public class HistorySearchActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  public static final int COM_NAME = 18;
  public static final int PUR_NAME = 26;
  public static final int COM_MEMBER = 18;
  public static final int DAY = 2;
  public static final int GAIYOU = 1;
  public static final int FLAG = 0;
  public static final String Q_SELECT_HISTORY = "select * from  t_reserve x inner join t_member y on x.re_id = y.re_id inner join m_out as a on y.mem_id = a.out_id inner join m_company as b on a.com_id = b.com_id inner join m_purpose as p on p.pur_id = x.pur_id inner join m_room as c on c.room_id = x.room_id where x.emp_id = ?";
  public static final String Q_TEST = "select * from  t_reserve x inner join t_member y on x.re_id = y.re_id inner join t_emp as a on y.mem_id = a.emp_id inner join m_purpose as p on p.pur_id = x.pur_id inner join m_room as c on c.room_id = x.room_id inner join m_company as b on x.com_id = b.com_id where x.emp_id = ? group by x.re_id";
  public static final String Q_COMPANY = "select * from  t_reserve x inner join t_member y on x.re_id = y.re_id inner join t_emp as a on y.mem_id = a.emp_id inner join m_purpose as p on p.pur_id = x.pur_id inner join m_room as c on c.room_id = x.room_id inner join m_company as b on x.com_id = b.com_id where x.emp_id = ? group by x.com_id";
  public static final String Q_SELECT_MEMBER = "select * from t_member where re_id = ?";
  public static final String Q_PURPOSE = "select * from  t_reserve x inner join t_member y on x.re_id = y.re_id inner join t_emp as a on y.mem_id = a.emp_id inner join m_purpose as p on p.pur_id = x.pur_id inner join m_room as c on c.room_id = x.room_id inner join m_company as b on x.com_id = b.com_id where x.emp_id = ? group by x.pur_id";
  public static final String Q_H_SPINNER = "select * from  t_reserve x inner join t_member y on x.re_id = y.re_id inner join t_emp as a on y.mem_id = a.emp_id inner join m_purpose as p on p.pur_id = x.pur_id inner join m_room as c on c.room_id = x.room_id inner join m_company as b on x.com_id = b.com_id where  x.emp_id = ? AND b.com_name = ? AND p.pur_name = ? group by x.re_id";
  public static final String Q_H_SPI_COM = "select * from  t_reserve x inner join t_member y on x.re_id = y.re_id inner join t_emp as a on y.mem_id = a.emp_id inner join m_purpose as p on p.pur_id = x.pur_id inner join m_room as c on c.room_id = x.room_id inner join m_company as b on x.com_id = b.com_id where  x.emp_id = ? AND b.com_name = ? group by x.re_id";
  public static final String Q_H_SPI_PUR = "select * from  t_reserve x inner join t_member y on x.re_id = y.re_id inner join t_emp as a on y.mem_id = a.emp_id inner join m_purpose as p on p.pur_id = x.pur_id inner join m_room as c on c.room_id = x.room_id inner join m_company as b on x.com_id = b.com_id where  x.emp_id = ? AND p.pur_name = ? group by x.re_id";
  public static final int STARTTIME = 4;
  public static final int ENDTIME = 5;
  public static final int APPLICANT = 18;
  public static final int SWITCH = 6;
  public static final int ROOMNAME = 27;
  public static final int FIXTURES = 7;
  public static final int REMARKS = 8;
  public static final int RE_ID = 0;
  public static final int ENDDAY = 3;
  public static final int COMPANY = 30;
  public static final int PURPOSE = 24;
  public static HistorySearchActivity instance = null;

  SearchView searchView;
  ListView listView;
  List<Purpose> purpose;
  List<Company> companiesy;
  ArrayList<Reserve> listItems;
  ArrayList<Reserve> reserves = new ArrayList<>();
  //  MyListAdapter adapter1 = new MyListAdapter(HistorySearchActivity.getInstance());
  MyListAdapter adapter1;

  private MyHelper helper = new MyHelper(this);
  public static SQLiteDatabase db;
  private Employee employee;

  //*** 社員・社外者の参加者を持つための、ポリモーフィズム使用のための、スーパクラスのリスト ***//
  public static List<Person> member = new ArrayList<>();


  //    private class ListItem {
//        private long id;
//        private String purpose;
//        private String date;
//        private String gaiyou;
//        private String company;
//        private String companyMember;
//
//        public long getId() {
//            return id;
//        }
//
//        public String getPurpose() {
//            return purpose;
//        }
//        public String getDate() {
//            return date;
//        }
//        public String getGaiyou() {
//            return gaiyou;
//        }
//        public String getCompany() {
//            return company;
//        }
//        public String getCompanyMember() {
//            return companyMember;
//        }
//
//        public void setId(long id) {
//            this.id = id;
//        }
//        public void setPurpose(String purpose) {
//            this.purpose = purpose;
//        }
//        public void setDate(String date) {
//            this.date = date;
//        }
//        public void setGaiyou(String gaiyou) {
//            this.gaiyou = gaiyou;
//        }
//        public void setCompany(String company) {
//            this.company = company;
//        }
//        public void setCompanyMember(String companyMember) {
//            this.companyMember = companyMember;
//        }
//
//        @Override
//        public String toString() {
//            return this.purpose + " " + this.company;
//        }
//    }
  private class Purpose {
    private String pur_id;
    private String pur_name;

    public String getPur_id() {
      return pur_id;
    }

    public void setPur_id(String pur_id) {
      this.pur_id = pur_id;
    }

    public String getPur_name() {
      return pur_name;
    }

    public void setPur_name(String pur_name) {
      this.pur_name = pur_name;
    }

  }

  private class Company {
    private String com_id;
    private String com_name;

    public String getCom_id() {
      return com_id;
    }

    public void setCom_id(String com_id) {
      this.com_id = com_id;
    }

    public String getCom_name() {
      return com_name;
    }

    public void setCom_name(String com_name) {
      this.com_name = com_name;
    }
  }

  private class MyListAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private ArrayList<Reserve> data = null;
    private int resource = 0;
    private LayoutInflater inflater = null;

    public MyListAdapter(Context context) {
      this.context = context;
      this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItemList(ArrayList<Reserve> data) {
      this.data = data;
    }

    //*** データの個数を取得 ***//
    @Override
    public int getCount() {
      return data.size();
    }

    //*** 指定された項目を取得 ***//
    @Override
    public Object getItem(int position) {
      return data.get(position);
    }

    //*** 指定された項目を識別するためのID値を取得 ***//
    @Override
    public long getItemId(int position) {
      return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      convertView = inflater.inflate(R.layout.list_search_item, parent, false);
      Reserve item = (Reserve) getItem(position);
      ((TextView) convertView.findViewById(R.id.txt_purpose)).setText(item.getRe_purpose_name());
      ((TextView) convertView.findViewById(R.id.txt_date)).setText(item.getRe_startDay());
      ((TextView) convertView.findViewById(R.id.txt_overview)).setText(item.getRe_name());
      ((TextView) convertView.findViewById(R.id.txt_company)).setText(item.getRe_company());
      ((TextView) convertView.findViewById(R.id.txt_member)).setText(String.format("%s,他 %d名", item.getRe_member().get(0).getName(), item.getRe_member().size()));
      return convertView;
    }



    @Override
    public Filter getFilter() {
      Log.d("call", "public Filter getFilter() ");
      return new MyFilter();


    }


    /***  独自フィルター ***/
    public class MyFilter extends Filter {

      @Override
      protected FilterResults performFiltering(CharSequence c) {
        Log.d("call", "protected FilterResults performFiltering(CharSequence c) ");
        List<Reserve> list = new ArrayList<>();
        for (int i = 0, size = getCount(); i < size; i++) {
          Reserve d = (Reserve) getItem(i);
          if (d.getRe_name() != null && d.getRe_name().contains(c)
              || d.getRe_company() != null && d.getRe_company().contains(c)
              || d.getRe_startDay() != null && d.getRe_startDay().contains(c)) {
            list.add(d);
          }
        }
        FilterResults f = new FilterResults();
        f.count = list.size();
        f.values = list;
        Log.d("call", f.values.toString());

        return f;
      }

      @Override
      protected void publishResults(CharSequence charSequence, FilterResults results) {
        Log.d("call", "protected void publishResults(CharSequence charSequence, FilterResults results) ");
        Log.d("call", String.format("result count :: %d", results.count));
//        if (results.count > 0) {
          Log.d("call", String.format("result count :: %d", results.count));
          List<Reserve> list = (List<Reserve>) results.values;
          listItems.clear();
          listItems = (ArrayList<Reserve>) list;

//          reserves.clear();
//          reserves = (ArrayList<Reserve>) list;
//          for (Reserve reserve : reserves) {
//            Log.d("call", reserve.toString());
//          }

          notifyDataSetChanged();
          onResume();
//        }
      }
    }
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d("call", "HistorySearchActivity->onCreate()");
    Intent intent = getIntent();
    employee = (Employee) intent.getSerializableExtra("employee");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_history_search);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();
    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    //*** データベースを準備 ***//
    listItems = new ArrayList<>();
    SQLiteDatabase db_list = helper.getReadableDatabase();
    db = helper.getReadableDatabase();
    //*** SQLで指定したデータを設定 ***//
    Cursor c = db.rawQuery(Q_TEST, new String[]{employee.getEmp_id()});
    //*** 会社用のデータベース ***//
//    ArrayList<Reserve> reserves = new ArrayList<>();
    //*** データベースにある情報だけループを回す ***//
    while (c.moveToNext()) {
      Reserve reserve = new Reserve();
      reserve.setRe_startTime(c.getString(STARTTIME));
      reserve.setRe_endTime(c.getString(ENDTIME));
      reserve.setRe_applicant(c.getString(APPLICANT));
      reserve.setRe_switch(c.getString(SWITCH));
      reserve.setRe_room_name(c.getString(ROOMNAME));
      reserve.setRe_fixtures(c.getString(FIXTURES));
      reserve.setRe_remarks(c.getString(REMARKS));
      reserve.setRe_id(c.getString(RE_ID));
      reserve.setRe_name(c.getString(GAIYOU));
      reserve.setRe_startDay(c.getString(DAY));
      reserve.setRe_endDay(c.getString(ENDDAY));
      reserve.setRe_company(c.getString(COMPANY));
      reserve.setRe_purpose_name(c.getString(PURPOSE));
      //*** reservesにaddする ***//
      reserves.add(reserve);
    }

    c.close();
    for (Reserve r : reserves) {
      r.setRe_member(Util.retHistoryPesonsList(employee.getEmp_id()));
    }
//        reserves.forEach(r -> {
//
//        });


    //*** ListItemとレイアウトとを関連付け ***//
    adapter1 = new MyListAdapter(this);
    listView = (ListView) findViewById(R.id.ahs_lis_history);
    //*** アダプターにアイテムリストをセット ***//
    listItems = reserves;
    adapter1.setItemList(listItems);
    listView.setAdapter(adapter1);


    //*** データベース検索(目的) ***//
    purpose = new ArrayList<>();
    List<String> strings = new ArrayList<>();
    strings.add("未選択");
    c = db.rawQuery(Q_PURPOSE, new String[]{employee.getEmp_id()});
    while (c.moveToNext()) {
      Purpose p = new Purpose();
      strings.add(c.getString(24));
      p.setPur_id(c.getString(13));
      p.setPur_name(c.getString(24));
      Log.d("call", c.getString(0) + " : " + c.getString(24) + " : " + c.getString(13));
      purpose.add(p);
    }
    c.close();
    //*** スピナーを取得(目的) ***//
    Spinner sp_purpose = (Spinner) findViewById(R.id.ahs_sp_purpose);
    //*** スピナーを取得(会社名) ***//
    Spinner sp_company = (Spinner) findViewById(R.id.ahs_sp_company);
    //*** スピナーのリストを設定 ***//
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
        android.R.layout.simple_spinner_dropdown_item, strings);
    //*** 目的スピナーにアダプターをセット ***//
    sp_purpose.setAdapter(adapter);
    //*** 起動時にspinnerが動かないようにfalseを設定 ***//
    sp_purpose.setSelection(FLAG, false);
    //*** スピナーに対してのイベントリスナーを登録 ***//
    sp_purpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner sp = (Spinner) parent;
        //*** 選択した会社名spinnerの文字列を取得 ***//
        String posi = (String) sp_company.getSelectedItem();
        //*** 目的spinnerの文字列を取得 ***//
        String purpose_name = (String) sp_purpose.getSelectedItem();
        Log.d("call2", posi);
        Log.d("call2", purpose_name);
        SQLiteDatabase db_list = helper.getReadableDatabase();
        db = helper.getReadableDatabase();
        //*** SQLで指定(会社名と目的が一致)したデータを設定 ***//
        Cursor c = db.rawQuery(Q_H_SPINNER, new String[]{employee.getEmp_id(), posi, purpose_name});
        //*** SQLで指定した(会社名が一致した)データを設定 ***//
        Cursor c_company = db.rawQuery(Q_H_SPI_COM, new String[]{employee.getEmp_id(), posi});
        //*** SOLで指定した(目的)データ設定 ***//
        Cursor c_pupose = db.rawQuery(Q_H_SPI_PUR, new String[]{employee.getEmp_id(), purpose_name});
        ArrayList<Reserve> list = new ArrayList<>();
        int count = FLAG;
//                if (!posi.equals("未選択") && !purpose_name.equals("未選択")) {

        //*** 会社名と目的一致 ***//
        while (c.moveToNext()) {
          Reserve reserve = new Reserve();
          reserve.setRe_startTime(c.getString(STARTTIME));
          reserve.setRe_endTime(c.getString(ENDTIME));
          reserve.setRe_applicant(c.getString(APPLICANT));
          reserve.setRe_switch(c.getString(SWITCH));
          reserve.setRe_room_name(c.getString(ROOMNAME));
          reserve.setRe_fixtures(c.getString(FIXTURES));
          reserve.setRe_remarks(c.getString(REMARKS));
          reserve.setRe_id(c.getString(RE_ID));
          reserve.setRe_name(c.getString(GAIYOU));
          reserve.setRe_startDay(c.getString(DAY));
          reserve.setRe_endDay(c.getString(ENDDAY));
          reserve.setRe_company(c.getString(COMPANY));
          reserve.setRe_purpose_name(c.getString(PURPOSE));
          //*** reservesにaddする ***//
          list.add(reserve);
          count++;
          Log.d("call", String.valueOf(count));
        }
//                } else if (purpose_name.equals("未選択") && !posi.equals("未選択")) {
//                    //*** 会社名が一致 ***//
//                    while (c_company.moveToNext()) {
//                        Reserve reserve = new Reserve();
//                        reserve.setRe_startTime(c.getString(STARTTIME));
//                        reserve.setRe_endTime(c.getString(ENDTIME));
//                        reserve.setRe_applicant(c.getString(APPLICANT));
//                        reserve.setRe_switch(c.getString(SWITCH));
//                        reserve.setRe_room_name(c.getString(ROOMNAME));
//                        reserve.setRe_fixtures(c.getString(FIXTURES));
//                        reserve.setRe_remarks(c.getString(REMARKS));
//                        reserve.setRe_id(c.getString(RE_ID));
//                        reserve.setRe_name(c.getString(GAIYOU));
//                        reserve.setRe_startDay(c.getString(DAY));
//                        reserve.setRe_endDay(c.getString(ENDDAY));
//                        reserve.setRe_company(c.getString(COMPANY));
//                        reserve.setRe_purpose_name(c.getString(PURPOSE));
//                        //*** reservesにaddする ***//
//                        list.add(reserve);
//                        count++;
//                        Log.d("call", String.valueOf(count));
//                    }
//                } else if (posi.equals("未選択") && !purpose_name.equals("未選択")) {
//                    while (c_pupose.moveToNext()) {
//                        Reserve reserve = new Reserve();
//                        reserve.setRe_startTime(c.getString(STARTTIME));
//                        reserve.setRe_endTime(c.getString(ENDTIME));
//                        reserve.setRe_applicant(c.getString(APPLICANT));
//                        reserve.setRe_switch(c.getString(SWITCH));
//                        reserve.setRe_room_name(c.getString(ROOMNAME));
//                        reserve.setRe_fixtures(c.getString(FIXTURES));
//                        reserve.setRe_remarks(c.getString(REMARKS));
//                        reserve.setRe_id(c.getString(RE_ID));
//                        reserve.setRe_name(c.getString(GAIYOU));
//                        reserve.setRe_startDay(c.getString(DAY));
//                        reserve.setRe_endDay(c.getString(ENDDAY));
//                        reserve.setRe_company(c.getString(COMPANY));
//                        reserve.setRe_purpose_name(c.getString(PURPOSE));
//                        //*** reservesにaddする ***//
//                        list.add(reserve);
//                        count++;
//                        Log.d("call", String.valueOf(count));
//                    }
//                }
        adapter1.setItemList(list);
        c.close();
        c_pupose.close();
        c_company.close();
        for (Reserve r : list) {
          r.setRe_member(Util.retHistoryPesonsList(employee.getEmp_id()));
        }
//                list.forEach(r -> {
//                    r.setRe_member(Util.retHistoryPesonsList(employee.getEmp_id()));
//                });
        //*** アダプターにアイテムリストをセット ***//
        listView.setAdapter(adapter1);
        if (count != FLAG) {
          //選択項目を取得し、その値で検索をする？それとトースト表示
          Toast.makeText(HistorySearchActivity.this, String.format("選択目的 : %s", sp.getSelectedItem()),
              Toast.LENGTH_SHORT).show();
          Log.d("call", "");
        } else if (posi.equals("未選択") && purpose_name.equals("未選択")) {
          //*** アダプターにアイテムリストをセット ***//
          adapter1.setItemList(reserves);
          listView.setAdapter(adapter1);
        } else {
          //選択項目を取得し、その値で検索をする？それとトースト表示
          Toast.makeText(HistorySearchActivity.this, String.format("検索目的該当なし。 : %s", sp.getSelectedItem()),
              Toast.LENGTH_SHORT).show();
          //*** アダプターにアイテムリストをセット ***//
//                    adapter1.setItemList(reserves);
//                    listView.setAdapter(adapter1);
        }
      }

      //項目が選択されなかったときの処理(今は空)
      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
    });

    //データベース検索(会社名)
    companiesy = new ArrayList<>();
    List<String> strings1 = new ArrayList<>();
    //*** リスト会社名の先頭に未選択追加 ***//
    strings1.add("未選択");
    Cursor cursor = db.rawQuery(Q_COMPANY, new String[]{employee.getEmp_id()});
    while (cursor.moveToNext()) {
      strings1.add(cursor.getString(30));
      Log.d("call", cursor.getString(30));
    }


    //*** adapterを宣言 ***//
    ArrayAdapter<String> adapter_com = new ArrayAdapter<>(this,
        android.R.layout.simple_spinner_dropdown_item, strings1);
    //*** アダプターの設定をspinnerカンパニーにセット ***//
    sp_company.setAdapter(adapter_com);
    //*** 起動時にspinnerが動かないようにfalseを設定 ***//
    sp_company.setSelection(FLAG, false);
    Log.d("call", "");
    //*** スピナーに対してのイベントリスナーを登録 ***//
    sp_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner sp = (Spinner) parent;
        //*** 選択した会社名spinnerの文字列を取得 ***//
        String posi = (String) sp_company.getSelectedItem();
        //*** 目的spinnerの文字列を取得 ***//
        String purpose_name = (String) sp_purpose.getSelectedItem();
        Log.d("call2", posi);
        Log.d("call2", purpose_name);
        SQLiteDatabase db_list = helper.getReadableDatabase();
        db = helper.getReadableDatabase();
        //*** SQLで指定(目的と会社名が一致)したデータを設定 ***//
        Cursor c = db.rawQuery(Q_H_SPINNER, new String[]{employee.getEmp_id(), posi, purpose_name});
        ArrayList<Reserve> list = new ArrayList<>();
        int count = FLAG;
//                int count_com = FLAG;
        //*** 会社名が一致したときにループする ***//
//                while (c_company.moveToNext()) {
//                    Reserve reserve = new Reserve();
//                    reserve.setRe_startTime(c.getString());
//                    reserve.setRe_endTime(c.getString(ENDTIME));
//                    reserve.setRe_applicant(c.getString(APPLICANT));
//                    reserve.setRe_switch(c.getString(SWITCH));
//                    reserve.setRe_room_name(c.getString(ROOMNAME));
//                    reserve.setRe_fixtures(c.getString(FIXTURES));
//                    reserve.setRe_remarks(c.getString(REMARKS));
//                    reserve.setRe_id(c.getString(RE_ID));
//                    reserve.setRe_name(c.getString(GAIYOU));
//                    reserve.setRe_startDay(c.getString(DAY));
//                    reserve.setRe_endDay(c.getString(ENDDAY));
//                    reserve.setRe_company(c.getString(COMPANY));
//                    reserve.setRe_purpose_name(c.getString(PURPOSE));
//                    //*** reservesにaddする ***//
//                    list_com.add(reserve);
//                    count_com++;
//                }
        //*** 目的と会社名が一致したときにループする ***//
        while (c.moveToNext()) {
          Reserve reserve = new Reserve();
          reserve.setRe_startTime(c.getString(STARTTIME));
          reserve.setRe_endTime(c.getString(ENDTIME));
          reserve.setRe_applicant(c.getString(APPLICANT));
          reserve.setRe_switch(c.getString(SWITCH));
          reserve.setRe_room_name(c.getString(ROOMNAME));
          reserve.setRe_fixtures(c.getString(FIXTURES));
          reserve.setRe_remarks(c.getString(REMARKS));
          reserve.setRe_id(c.getString(RE_ID));
          reserve.setRe_name(c.getString(GAIYOU));
          reserve.setRe_startDay(c.getString(DAY));
          reserve.setRe_endDay(c.getString(ENDDAY));
          reserve.setRe_company(c.getString(COMPANY));
          reserve.setRe_purpose_name(c.getString(PURPOSE));
          //*** reservesにaddする ***//
          list.add(reserve);
          count++;
        }
        adapter1.setItemList(list);
        c.close();
        for (Reserve r : list) {
          r.setRe_member(Util.retHistoryPesonsList(employee.getEmp_id()));
        }
//                        list.forEach(r -> {
//                            r.setRe_member(Util.retHistoryPesonsList(employee.getEmp_id()));
//                        });
        //*** アダプターにアイテムリストをセット ***//
        listView.setAdapter(adapter1);
        if (count != FLAG) {
          //*** スピナーに対しての処理 ***//
          Toast.makeText(HistorySearchActivity.this, String.format("選択会社名 : %s", sp.getSelectedItem()), Toast.LENGTH_SHORT).show();
        } else if (posi.equals("未選択") && purpose_name.equals("未選択")) {
          //*** アダプターにアイテムリストをセット ***//
          adapter1.setItemList(reserves);
          listView.setAdapter(adapter1);
        } else {
          //*** スピナーに対しての処理 ***//
          Toast.makeText(HistorySearchActivity.this, String.format("検索会社名該当なし。 : %s", sp.getSelectedItem()), Toast.LENGTH_SHORT).show();

        }

      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
    });


    //*** ListItemとレイアウトとを関連付け ***//
//        final MyListAdapter adapter1 = new MyListAdapter(this);
//        listView = (ListView) findViewById(R.id.ahs_lis_history);
//        //*** アダプターにアイテムリストをセット ***//
//        adapter1.setItemList(reserves);
//        listView.setAdapter(adapter1);


    //*** リスト項目をタップした時の処理を定義 ***//
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //*** 中身はまだ考え中 ***//
        Log.d("call", "履歴確認画面への遷移");

        //*** 画面遷移のインテント作成中
        Intent intent = new Intent(getApplicationContext(), ReserveConfirmActivity.class);

        //*** 予約IDを特定 ***//
        intent.putExtra("reserve", (Reserve) adapter1.getItem(position));

        //*** intent.puextra(xxx)する ***//
        intent.putExtra("gamen", "2");
        intent.putExtra("employee", employee);

        //*** 予約確認画面へ画面遷移 ***//
        startActivity(intent);

      }
    });

    //*** フィルタ機能を有効化 ***//
    listView.setTextFilterEnabled(true);

    //*** serchviewの検索ボックスに入力された時の処理 ***//
    searchView = (SearchView) findViewById(R.id.ahs_sea_freeword);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String s) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String text) {
        Log.d("call", "public boolean onQueryTextChange(String text) ");
        if (text == null || text.isEmpty()) {
          listView.clearTextFilter();
        } else {
          //*** 入力された文字が検索結果の日付と概要に一致したら検索結果を再構築する ***//

          //*** DB呼び出し ***//
          //SQLiteDatabase db_data_search = helper.getReadableDatabase();
          Log.d("call", text);
          listView.setFilterText(text);

          // TODO: 2017/12/01 ここで、listviewの再描画をかける
          listView.invalidate();
        }
        return false;
      }
    });

  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  protected void onResume() {
    Log.d("call", "protected void onResume() {");
    super.onResume();

    adapter1 = new MyListAdapter(this);
    listView = (ListView) findViewById(R.id.ahs_lis_history);
    //*** アダプターにアイテムリストをセット ***//
    adapter1.setItemList(listItems);
    listView.setAdapter(adapter1);


  }

  // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
  // _/_/
  // _/_/ ナビを選択したときの処理
  // _/_/
  // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    Intent intent = null;
    switch (id) {
      case R.id.nav_reserve_list:
        intent = new Intent(getApplicationContext(), ReserveListActivity.class);
        break;
      case R.id.nav_rireki:
        intent = new Intent(getApplicationContext(), HistorySearchActivity.class);
        break;
      case R.id.nav_admin_auth:
//                AuthDialog authDialog = new AuthDialog();
//                authDialog.show(getFragmentManager(), "aaa");
//                break;

    }
    if (intent != null) {
      startActivity(intent);
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  public static HistorySearchActivity getInstance(){
    return instance;
  }
}
