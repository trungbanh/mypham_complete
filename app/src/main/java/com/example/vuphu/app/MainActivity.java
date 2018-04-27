package com.example.vuphu.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vuphu.app.AcsynHttp.AsyncHttpApi;
import com.example.vuphu.app.AcsynHttp.NetworkConst;
import com.example.vuphu.app.admin.AdminCatogoriesFragment;
import com.example.vuphu.app.admin.AdminOrdersFragment;
import com.example.vuphu.app.admin.AdminUserFragment;
import com.example.vuphu.app.login_signUp.LoginActivity;
import com.example.vuphu.app.object.AcountId;
import com.example.vuphu.app.object.listOrder;
import com.example.vuphu.app.user.AddMoneyFragment;
import com.example.vuphu.app.user.ProfileFragment;
import com.example.vuphu.app.user.UserProfileTab.AddMoneyHistoryFragment;
import com.example.vuphu.app.user.UserProfileTab.OrderHistoryFragment;
import com.example.vuphu.app.user.cart.CartList;
import com.example.vuphu.app.user.catogries.CatogriesFragment;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int TIME_INTERVAL = 2000;
    NavigationView navigationView;
    private MaterialSearchView searchView;
    private ImageView cart;
    private SharedPreferences pre;
    private SharedPreferences.Editor edit;
    private long mBackPressed;
    private listOrder order ;
    private AcountId u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        cart = findViewById(R.id.img_cart);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        pre = getSharedPreferences("data", MODE_PRIVATE);
        edit = pre.edit();
        getToken(pre.getString("token",""));
        SearchQuery();
        initView();
        viewCart();

    }
    void getToken (String token) {
        AsyncHttpApi.get(token,"/account",null,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                order = gson.fromJson(response.toString(),listOrder.class);
                u = order.getAccountId();
                int money = order.getBalanced();
                edit.putInt("money",money);
                edit.commit();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("fail",errorResponse.toString());
            }
        });
    }

    private void viewCart() {
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CartList.class);
                startActivity(i);
            }
        });
    }

    private void initView() {

        String user = pre.getString("type_user", "");
        if (user.equals("admin")) {
            navigationView.inflateMenu(R.menu.activity_main_admin);
            setTitle(R.string.danhmucsanpham);
            FragmentTransaction transaction;
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, AdminCatogoriesFragment.newInstance());
            transaction.commit();
        } else {
            navigationView.inflateMenu(R.menu.activity_main_drawer);
            android.support.v4.app.FragmentTransaction transaction;
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, CatogriesFragment.newInstance());
            transaction.commit();
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void SearchQuery() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AsyncHttpApi.get(pre.getString("token", ""), "/products/search/" + query, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        android.support.v4.app.FragmentTransaction transaction;
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content, SearchFragment.newInstance(response)).addToBackStack(null);
                        transaction.commit();
                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                AsyncHttpApi.get(pre.getString("token", ""), "/products/search/" + newText, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        android.support.v4.app.FragmentTransaction transaction;
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content, SearchFragment.newInstance(response)).addToBackStack(null);
                        transaction.commit();
                    }
                });
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
                String user = pre.getString("type_user", "");
                if (user.equals("admin")) {
                    setTitle(R.string.danhmucsanpham);
                    android.support.v4.app.FragmentTransaction transaction;
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, AdminCatogoriesFragment.newInstance());
                    transaction.commit();
                } else {
                    android.support.v4.app.FragmentTransaction transaction;
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, CatogriesFragment.newInstance());
                    transaction.commit();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fm = getSupportFragmentManager();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            applyExit();
        }
    }

    private void applyExit() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            finish();
        } else {
            Toast.makeText(this, "Press Again to exit", Toast.LENGTH_LONG).show();
        }
        mBackPressed = System.currentTimeMillis();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment temp = null;
        switch (id) {

            case R.id.nav_admin_logout:
                edit.clear();
                edit.apply();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                return true;
            case R.id.nav_logout:
                edit.clear();
                edit.apply();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                return true;

            case R.id.nav_profile:
                AcountId u = new AcountId();
                temp = ProfileFragment.newInstance(u);
                setTitle(getString(R.string.thongtintaikhoan));
                break;
            case R.id.nav_cato:
                setTitle(getString(R.string.danhmucsanpham));
                temp = CatogriesFragment.newInstance();
                break;
            case R.id.nav_add_money:
                setTitle(getString(R.string.naptien));
                temp = AddMoneyFragment.Companion.newInstance();
                break;
            case R.id.nav_admin_cato:
                setTitle(R.string.danhmucsanpham);
                temp = AdminCatogoriesFragment.newInstance();
                break;
            case R.id.nav_admin_orders:
                setTitle(getString(R.string.donhang));
                temp = AdminOrdersFragment.newInstance();
                break;
            case R.id.nav_admin_user:
                setTitle(getString(R.string.quanlinguoidung));
                temp = AdminUserFragment.newInstance();
                break;
            case R.id.nav_order_his:
                setTitle(getString(R.string.order_history));
                temp = OrderHistoryFragment.newInstance();
                break;
            case R.id.nav_add_money_his:
                setTitle("Add money History");
                temp = AddMoneyHistoryFragment.Companion.newInstance();
                break;
        }
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, temp);
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
