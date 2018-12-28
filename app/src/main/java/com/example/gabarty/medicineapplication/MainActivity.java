package com.example.gabarty.medicineapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gabarty.medicineapplication.fragments.ClientsFragment;
import com.example.gabarty.medicineapplication.fragments.DashboardFragment;
import com.example.gabarty.medicineapplication.fragments.InventoryFragment;
import com.example.gabarty.medicineapplication.fragments.OrdersFragment;
import com.example.gabarty.medicineapplication.fragments.SuppliersFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Handler mHandler;
    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_DASHBOARD = "dashboard";
    private static final String TAG_CLIENTS = "clients";
    private static final String TAG_SUPPLIERS = "suppliers";
    private static final String TAG_ORDERS = "orders";
    private static final String TAG_INVENTORY = "inventory";
    public static String CURRENT_TAG = TAG_DASHBOARD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler=new Handler();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

        if (id == R.id.nav_dashboard) {
            CURRENT_TAG=TAG_DASHBOARD;
            navItemIndex=0;
            getSupportActionBar().setTitle("Dashboard");
        } else if (id == R.id.nav_clients) {
            CURRENT_TAG=TAG_CLIENTS;
            navItemIndex=1;
            getSupportActionBar().setTitle("Clients");
        } else if (id == R.id.nav_suppliers) {
            CURRENT_TAG=TAG_SUPPLIERS;
            navItemIndex=2;
            getSupportActionBar().setTitle("Suppliers");
        } else if (id == R.id.nav_orders) {
            CURRENT_TAG=TAG_ORDERS;
            navItemIndex=3;
            getSupportActionBar().setTitle("Orders");
        } else if (id == R.id.nav_inventory) {
            CURRENT_TAG=TAG_INVENTORY;
            navItemIndex=4;
            getSupportActionBar().setTitle("Inventory");
        }

        loadSelectedFragment();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadSelectedFragment() {

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                DashboardFragment df = new DashboardFragment();
                return df;
            case 1:
                ClientsFragment cf = new ClientsFragment();
                return cf;
            case 2:
                SuppliersFragment sf = new SuppliersFragment();
                return sf;
            case 3:
                OrdersFragment of = new OrdersFragment();
                return of;
            case 4:
                InventoryFragment invf = new InventoryFragment();
                return invf;
            default:
                return new DashboardFragment();
        }
    }
}
