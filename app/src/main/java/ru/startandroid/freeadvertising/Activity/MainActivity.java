package ru.startandroid.freeadvertising.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.startandroid.freeadvertising.Adapter.ProductlistAdapter;
import ru.startandroid.freeadvertising.R;
import ru.startandroid.freeadvertising.http.Config;
import ru.startandroid.freeadvertising.http.HttpHandler;
import ru.startandroid.freeadvertising.model.Product;
import ru.startandroid.freeadvertising.model.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean all = true;
    private RecyclerView recyclerView;
    private List<Product> productList = new ArrayList<>();
    private static LayoutInflater inflate;
    private static LinearLayout parentLayout;
    private ProductlistAdapter adapter;
    private User user = new User("", "", "", "", "");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAuth();
        toolDrawerfab();
        inflate = getLayoutInflater();
        parentLayout = (LinearLayout) findViewById(R.id.layout_list_reklam);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        parentLayout.setLayoutParams(params);
        initRecyclerview();
        GetReklam my = new GetReklam();
        my.execute();
    }
    public void checkAuth() {
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        user.setLogin(sp.getString("login", ""));
        if(!user.getLogin().equals("")) {
            GetUser gt = new GetUser();
            gt.execute();
        }
    }
    class GetUser extends AsyncTask<Void,Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpHandler handler = new HttpHandler();
            String jsonstr = handler.ReadHttpResponsewithHeader(Config.getAccount(), "login", user.getLogin());
            if (jsonstr != null) {
                try {
                    JSONArray productarray = new JSONArray(jsonstr);
                    for (int i = 0; i < productarray.length(); i++) {
                        user.setFirstname(productarray.getJSONObject(i).getString("firstname"));
                        user.setLastname(productarray.getJSONObject(i).getString("lastname"));
                        user.setTelefon(productarray.getJSONObject(i).getString("telefon"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        public void onPostExecute(Boolean ok) {
            super.onPostExecute(ok);
            TextView ismvafam = (TextView) findViewById(R.id.ismvafam);
            TextView teluser = (TextView) findViewById(R.id.telefon_user);
            Log.d("mylogs", user.getFirstname());
            Log.d("mylogs", user.getLastname());
            Log.d("mylogs", user.getTelefon());
            ismvafam.setText(user.getFirstname() + " " + user.getLastname());
            teluser.setText(user.getTelefon());
        }
    }

    class GetReklam extends AsyncTask<Void,Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpHandler handler = new HttpHandler();
            String jsonstr = null;
            productList = new ArrayList<>();
            if(all) {
                jsonstr = handler.ReadHttpResponsewithHeader(Config.getProduct(), "login", "");
            }
            else {
                jsonstr = handler.ReadHttpResponsewithHeader(Config.getProduct(), "login", user.getLogin());
            }
            Log.d("mylogs", "" + jsonstr);
            if (jsonstr != null) {
                try {
                    JSONArray productarray = new JSONArray(jsonstr);
                    for (int i = 0; i < productarray.length(); i++) {
                        Product product = new Product(
                                productarray.getJSONObject(i).getString("name"),
                                productarray.getJSONObject(i).getString("score"),
                                productarray.getJSONObject(i).getString("comment"),
                                productarray.getJSONObject(i).getString("imagepath"),
                                productarray.getJSONObject(i).getLong("type"),
                                productarray.getJSONObject(i).getString("userlogin")
                        );
                        product.setId(productarray.getJSONObject(i).getInt("id"));
                        productList.add(product);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        public void onPostExecute(Boolean ok) {
            super.onPostExecute(ok);
            adapter = new ProductlistAdapter(MainActivity.this, productList);
            recyclerView.setAdapter(adapter);
        }
    }
    private void  initRecyclerview() {
        recyclerView = (RecyclerView) findViewById(R.id.list_item_reklam);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
    }
    public void toolDrawerfab() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        toolbar.setTitle("elon");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!user.getLogin().equals("")) {
                    startActivity(new Intent(MainActivity.this, AddNewReklam.class));
                    finish();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.logout);
        MenuItem itemadd = menu.findItem(R.id.addnewreklam);
        MenuItem myrek = menu.findItem(R.id.my_reklam);
        if(user.getLogin().equals("")) {
            item.setTitle(R.string.kirish);
            myrek.setVisible(false);
            itemadd.setVisible(false);
        }
        else {
            item.setTitle(R.string.chiqish);
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.my_reklam) {
            all = false;
            GetReklam gt = new GetReklam();
            gt.execute();
        }
        if(id == R.id.all_reklam) {
            all = true;
            GetReklam gt = new GetReklam();
            gt.execute();
        }
        if (id == R.id.addnewreklam) {
            if(!user.getLogin().equals("")) {
                startActivity(new Intent(MainActivity.this, AddNewReklam.class));
                finish();
            }

        } else if (id == R.id.logout) {
            if(user.getLogin().equals("")) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
            else
            {
                SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor ed =  sp.edit();
                ed.putString("login", "");
                ed.putString("parol", "");
                ed.commit();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
