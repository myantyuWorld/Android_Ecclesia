package com.example.yuichi_oba.ecclesia;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;

import java.util.ArrayList;
import java.util.List;

//
// へろーぐちおさん
//

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

    private class MyListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }

    SearchView searchView;
    ListView listView;
    List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
        {
            list.add("test");
        }
        listView = (ListView) findViewById(R.id.list_history);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);

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
//            case R.id.nav_rireki:
//                intent = new Intent(getApplicationContext(), HistorySearchActivity.class);
//                break;
            case R.id.nav_admin_auth:
                AuthDialog authDialog = new AuthDialog();
                authDialog.show(getFragmentManager(), "aaa");
                break;

        }
        if (intent != null) {
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
