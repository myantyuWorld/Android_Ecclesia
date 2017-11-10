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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuichi_oba.ecclesia.R;
import com.example.yuichi_oba.ecclesia.model.Person;
import com.example.yuichi_oba.ecclesia.model.Reserve;
import com.example.yuichi_oba.ecclesia.tools.MyHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.*;

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
public class HistorySearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int COM_NAME = 18;
    public static final int PUR_NAME = 26;
    public static final int COM_MEMBER = 18;
    public static final int DAY = 2;
    public static final int GAIYOU = 1;
    public static final int ID = 0;

    SearchView searchView;
    ListView listView;
    List<Purpose> purpose;
    List<Company> companiesy;
    ArrayList<Reserve> listItems;
    private MyHelper helper = new MyHelper(this);
    public static SQLiteDatabase db;

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

    private class MyListAdapter extends BaseAdapter{
        private Context context;
        private ArrayList<Reserve> data = null;
        private int resource = 0;
        private LayoutInflater inflater = null;

//        public MyListAdapter(Context context, ArrayList<ListItem> listdata, int resource) {
//            this.context = context;
//            this.data = listdata;
//            this.resource = resource;
//            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }


        public MyListAdapter(Context context) {
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setItemList(ArrayList<Reserve> data) {
            this.data = data;
        }
        //データの個数を取得
        @Override
        public int getCount() {
            return data.size();
        }

        //指定された項目を取得
        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        //指定された項目を識別するためのID値を取得
        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.list_search_item, parent, false);

//            Activity activity = (Activity) context;
            Reserve item = (Reserve) getItem(position);
            //*** 会社メンバーセットのクラス呼び出し(エラーが発生しているのでコメント) ***//
//            Person p_item = (Person) getItem(position);

//************************************************************************************************
//            初回かどうか確認
//            if (convertView == null) {
//                //Layoutを取得
//                convertView = activity.getLayoutInflater().inflate(resource, null);
//            }
//            ((RelativeLayout)convertView).findViewById(R.id.customview).invalidate();
//************************************************************************************************

            ((TextView) convertView.findViewById(R.id.txt_purpose)).setText(item.getRe_purpose_name());
            ((TextView) convertView.findViewById(R.id.txt_date)).setText(item.getRe_startDay());
            ((TextView) convertView.findViewById(R.id.txt_overview)).setText(item.getRe_name());
            ((TextView) convertView.findViewById(R.id.txt_company)).setText(item.getRe_company());
//           ((TextView) convertView.findViewById(R.id.txt_member)).setText(p_item.getName());
            return convertView;
        }

        /*** 大馬コーディング 独自フィルター ***/
//        public class MyFilter extends Filter {
//
//            @Override
//            protected FilterResults performFiltering(CharSequence c) {
//                List<ListItem> list = new ArrayList<>();
//                for (int i = 0, size = getCount(); i < size; i++){
//                    ListItem d = (ListItem) getItem(i);
//                    if (d.gaiyou != null && d.gaiyou.contains(c)
//                            || d.company != null && d.company.contains(c)
//                            || d.date != null && d.date.contains(c)){
//                        list.add(d);
//                    }
//                }
//                FilterResults f = new FilterResults();
//                f.count = list.size();
//                f.values = list;
//
//                return f;
//            }
//
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults results) {
//                if (results.count > 0){
//                    List<ListItem> list = (List<ListItem>) results.values;
//
//
//
//                }
//            }
//        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(CALL, "HistorySearchActivity->onCreate()");
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
        //データベースを準備
        listItems = new ArrayList<>();
        SQLiteDatabase db_list = helper.getReadableDatabase();
        db = helper.getReadableDatabase();

        Cursor c_list = db.rawQuery("select * from  t_reserve x" +
                " inner join t_member y on x.re_id = y.re_id" +
                " inner join m_out as a on y.mem_id = a.out_id" +
                " inner join m_company as b on a.com_id = b.com_id" +
                " inner join m_purpose as p on p.pur_id = x.pur_id"+
                " inner join m_room as c on c.room_id = x.room_id"+
                " where x.re_id = ?",new String[]{"0003"});

        //会社用のデータベース
        while (c_list.moveToNext()) {
            Reserve li = new Reserve();
            Person p = new Person();

            //*** 今後必要になるためコメントアウト ***//
//            li.setId(c_list.getLong(ID));
            li.setRe_name(c_list.getString(GAIYOU));
            li.setRe_startDay(c_list.getString(DAY));
            //*** 社員名をセット(まだ実行してないので未確認。) ***//
            p.setName(c_list.getString(COM_MEMBER));
            li.setRe_company(c_list.getString(26));
            li.setRe_purpose_name(c_list.getString(28));
            Log.d(CALL, (c_list.getString(GAIYOU)) + " : " + c_list.getString(DAY) + " : " + c_list.getString(26) + " : " + c_list.getString(28) + " : " + c_list.getString(COM_MEMBER));
            // addするメソッドを書く
            listItems.add(li);
        }
        c_list.close();

        //リストに表示するデータを準備
//        String pupose[] = {"定例会","商談"};
//        String date[] = {"2017/02/20","2018/01/31"};
//        String gaiyou[] = {"内定懇談会","Ecclesiaの売り込み"};
//        String company[] = {"株式会社ostraca","株式会社トミー"};
//        String companyMember[] = {"xxxx様","yyyy様"};


        //配列の内容をListItemオブジェクトに詰め替え
//        final ArrayList<ListItem> list = new ArrayList<>();
//        for (int i = 0; i < pupose.length; i++)
//        {
//            ListItem item = new ListItem();
//            item.setId((new Random()).nextLong());
//            item.setPurpose(pupose[i]);
//            item.setDate(date[i]);
//            item.setGaiyou(gaiyou[i]);
//            item.setCompany(company[i]);
//            item.setCompanyMember(companyMember[i]);
//            list.add(item);
//        }


        //データベース検索
        purpose= new ArrayList<>();
        List<String> strings = new ArrayList<>();
        Cursor c = db.rawQuery("select * from m_purpose", new String[]{});
        while (c.moveToNext()) {
            strings.add(c.getString(1));
            Purpose p = new Purpose();
            p.setPur_id(c.getString(0));
            p.setPur_name(c.getString(1));
            Log.d(CALL, c.getString(0) + " : " + c.getString(1));
            purpose.add(p);
        }
        c.close();

        for(String s:strings){
            Log.d(CALL,s);
        }
        //スピナーを取得
        Spinner sp = (Spinner) findViewById(R.id.ahs_sp_purpose);
        //
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,strings);
        sp.setAdapter(adapter);
        //スピナーに対してのイベントリスナーを登録
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner sp = (Spinner) parent;
                //選択項目を取得し、その値で検索をする？それとトースト表示

                Toast.makeText(HistorySearchActivity.this,String.format("選択目的 : %s",sp.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
                Log.d(CALL,"");
            }
            //項目が選択されなかったときの処理(今は空)
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //データベース検索(会社名)
        companiesy = new ArrayList<>();
        List<String> strings1 = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from m_company", new String[]{});
        while (cursor.moveToNext()) {
            strings1.add(cursor.getString(1));
            Log.d(CALL,cursor.getString(1));
        }

        //スピナーを取得
        Spinner sp_company = (Spinner) findViewById(R.id.ahs_sp_company);
        //adapterを宣言
        ArrayAdapter<String> adapter_com = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,strings1);
        sp_company.setAdapter(adapter_com);
        Log.d(CALL, "");
        //スピナーに対してのイベントリスナーを登録
        sp_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner sp = (Spinner) parent;
                //スピナーに対しての処理
                Toast.makeText(HistorySearchActivity.this,String.format("選択会社名 : %s",sp.getSelectedItem()),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        //*** ListItemとレイアウトとを関連付け ***//

        final MyListAdapter adapter1 = new MyListAdapter(this);
        listView = (ListView) findViewById(R.id.ahs_lis_history);
        adapter1.setItemList(listItems);
        listView.setAdapter(adapter1);


        //*** リスト項目をタップした時の処理を定義 ***//
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //*** 中身はまだ考え中 ***//
                Log.d(CALL,"履歴確認画面への遷移");

        //        Reserve reserve = Reserve.retReserveConfirm(re_id);
                CharSequence msg = ((TextView) view).getText();
                Toast.makeText(HistorySearchActivity.this,
                        String.format("選択したのは%s",msg.toString()),Toast.LENGTH_SHORT).show();
                //*** 予約IDを特定 ***//

                //*** 予約のインスタンスを生成 ***//

                //*** intent.puextra(xxx)する ***//

                //*** 画面遷移のインテント作成中
                Intent intent = new Intent(getApplicationContext(),ReserveConfirmActivity.class);
                intent.putExtra("gamen",1);           //*** どの画面からの遷移か(履歴検索)***//
                //*** アクティビティを起動 ***//
                startActivity(intent);
            }
        });

        //フィルタ機能を有効化
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(HistorySearchActivity.this, adapter1.getItem(position).toString(), Toast.LENGTH_SHORT).show();
                Log.d(CALL, adapter1.getItem(position).toString());


            }
        });

        //serchviewの検索ボックスに入力された時の処理
        searchView  = (SearchView) findViewById(R.id.ahs_sea_freeword);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                if (text == null || text.isEmpty()) {
                    listView.clearTextFilter();
                } else {
                    listView.setFilterText(text);
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
}
