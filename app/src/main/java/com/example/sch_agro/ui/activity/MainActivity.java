package com.example.sch_agro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.sch_agro.R;
import com.example.sch_agro.ui.fragment.AddActFragment;
import com.example.sch_agro.ui.fragment.AddUserFragment;
import com.example.sch_agro.ui.fragment.DashboardFragment;
import com.example.sch_agro.ui.fragment.RelatoriosFragment;
import com.example.sch_agro.ui.fragment.ViewFarmerFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_nav,
                R.string.close_nav
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new DashboardFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_dashboard);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.nav_dashboard){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new DashboardFragment())
                    .commit();

        } else if (itemId == R.id.nav_verprodutores) {

           // startActivity(new Intent(MainActivity.this, DisplayData.class));

            ViewFarmerFragment settingsFragment = new ViewFarmerFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, settingsFragment);
            transaction.commit();


        } else if (itemId == R.id.nav_actividades) {
            //Intent intent = new Intent(getApplicationContext(), AddActivity.class);
           //startActivity(intent);
            AddActFragment addFragment = new AddActFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, addFragment);
            transaction.commit();

        } else if (itemId == R.id.nav_addprodutores) {
            //Intent intent = new Intent(getApplicationContext(), UserEditGeba.class);
           // startActivity(intent);
            AddUserFragment adduserFragment = new AddUserFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, adduserFragment);
            transaction.commit();

        } else if (itemId == R.id.nav_logout) {

            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finishAffinity(); // to clear the backstack, so that user cannot go back after logout
        }else if (itemId == R.id.nav_addUser) {

            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        }else if (itemId == R.id.nav_report) {


            RelatoriosFragment aboutfragment = new RelatoriosFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, aboutfragment);
            transaction.commit();




/*
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_report, new AboutFragment())
                    .commit();

 */

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}