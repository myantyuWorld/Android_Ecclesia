package com.example.yuichi_oba.ecclesia.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.yuichi_oba.ecclesia.R;
//import com.example.yuichi_oba.ecclesia.dialog.AuthDialog;

public class EditActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_reserve);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        FloatingActionButton fbn = (FloatingActionButton) findViewById(R.id.fbn_addMember);
//        fbn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplication(), AddMemberActivity.class);
//                startActivity(intent);
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        Intent intent = null;
//        switch (id) {
//            case R.id.nav_reserve_list:
//                intent = new Intent(getApplicationContext(), ReserveListActivity.class);
//                break;
//            case R.id.nav_rireki:
//                intent = new Intent(getApplicationContext(), HistorySearchActivity.class);
//                break;
//            case R.id.nav_admin_auth:
//                AuthDialog authDialog = new AuthDialog();
//                authDialog.show(getFragmentManager(), "aaa");
//                break;
//
//        }
//        if (intent != null) {
//            startActivity(intent);
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
