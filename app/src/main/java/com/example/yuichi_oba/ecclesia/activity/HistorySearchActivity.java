package com.example.yuichi_oba.ecclesia.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.example.yuichi_oba.ecclesia.tools.DB;

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
public class HistorySearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int COM_NAME = 18;
    public static final int PUR_NAME = 26;
    public static final int COM_MEMBER = 18;
    public static final int DAY = 2;
    public static final int GAIYOU = 1;
    public static final int ID = 0;

    private class ListItem {
        private long id;
        private String purpose;
        private String date;
        private String gaiyou;
        private String company;
        private String companyMember;

        public long getId() {
            return id;
        }

        public String getPurpose() {
            return purpose;
        }
        public String getDate() {
            return date;
        }
        public String getGaiyou() {
            return gaiyou;
        }
        public String getCompany() {
            return company;
        }
        public String getCompanyMember() {
            return companyMember;
        }

        public void setId(long id) {
            this.id = id;
        }
        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }
        public void setDate(String date) {
            this.date = date;
        }
        public void setGaiyou(String gaiyou) {
            this.gaiyou = gaiyou;
        }
        public void setCompany(String company) {
            this.company = company;
        }
        public void setCompanyMember(String companyMember) {
            this.companyMember = companyMember;
        }
    }
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
        private Context context = null;
        private ArrayList<ListItem> data = null;
        private int resource = 0;

        public MyListAdapter(Context context, ArrayList<ListItem> listdata, int resource) {
            this.context = context;
            this.data = listdata;
            this.resource = resource;
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
            Activity activity = (Activity) context;
            ListItem item = (ListItem) getItem(position);
            //初回かどうか確認
            if (convertView == null) {
                //Layoutを取得
                convertView = activity.getLayoutInflater().inflate(resource, null);
            }
            ((TextView) convertView.findViewById(R.id.purpose)).setText(item.getPurpose());
            ((TextView) convertView.findViewById(R.id.date)).setText(item.getDate());
            ((TextView) convertView.findViewById(R.id.gaiyou)).setText(item.getGaiyou());
            ((TextView) convertView.findViewById(R.id.company)).setText(item.getCompany());
            ((TextView) convertView.findViewById(R.id.companyMember)).setText(item.getCompanyMember());
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

    SearchView searchView;
    ListView listView;
    List<Purpose> purpose;
    List<Company> companiesy;
    ArrayList<ListItem> listItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("call", "HistorySearchActivity->onCreate()");
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
        SQLiteOpenHelper listdeta = new DB(getApplicationContext());
        SQLiteDatabase db_list = listdeta.getReadableDatabase();
        Cursor c_list = db_list.rawQuery("select * from t_reserve x" +
                " inner join t_member y on x.re_id = y.re_id" +
                " inner join m_out_emp as a on y.mem_id = a.outemp_id" +
                " inner join m_company as b on a.com_id = b.com_id" +
                " inner join m_purpose as p on p.pur_id = x.pur_id", new String[]{});
        //会社用のデータベース
        while (c_list.moveToNext()) {
            ListItem li = new ListItem();
            li.setId(c_list.getLong(ID));
            li.setGaiyou(c_list.getString(GAIYOU));
            li.setDate(c_list.getString(DAY));
//            li.setCompanyMember(c_list.getString(COM_MEMBER));
            li.setCompanyMember(c_list.getString(17));
            li.setCompany(c_list.getString(25));
            li.setPurpose(c_list.getString(27));
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
        SQLiteOpenHelper helper = new DB(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from m_purpose", new String[]{});
        while (c.moveToNext()) {
            strings.add(c.getString(1));
            Purpose p = new Purpose();
            p.setPur_id(c.getString(0));
            p.setPur_name(c.getString(1));
            Log.d("call", c.getString(0) + " : " + c.getString(1));
            purpose.add(p);
        }
        c.close();

        for(String s:strings){
            Log.d("call",s);
        }
       //スピナーを取得
        Spinner sp = (Spinner) findViewById(R.id.spinner_mokuteki);
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
                        Log.d("call","");
            }
            //項目が選択されなかったときの処理(今は空)
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //データベース検索(会社名)
        companiesy = new ArrayList<>();
        List<String> strings1 = new ArrayList<>();
        SQLiteOpenHelper helper2 = new DB(getApplicationContext());
        SQLiteDatabase db2 = helper2.getReadableDatabase();
        Cursor cursor = db2.rawQuery("select * from m_company", new String[]{});
        while (cursor.moveToNext()) {
            strings1.add(cursor.getString(1));
            Log.d("call",cursor.getString(1));
        }

        //スピナーを取得
        Spinner sp_company = (Spinner) findViewById(R.id.spinner_company);
        //adapterを宣言
        ArrayAdapter<String> adapter_com = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,strings1);
        sp_company.setAdapter(adapter_com);
        Log.d("call", "");
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

        //ListItemとレイアウトとを関連付け
        MyListAdapter adapter1 = new MyListAdapter(this, listItems,R.layout.list_search_item);
        listView = (ListView) findViewById(R.id.list_history);
        listView.setAdapter(adapter1);

        //フィルタ機能を有効化
        listView.setTextFilterEnabled(true);

        //serchviewの検索ボックスに入力された時の処理
        searchView  = (SearchView) findViewById(R.id.search);
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
