package com.example.sch_agro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.sch_agro.Configuration.ApiClient;
import com.example.sch_agro.Configuration.DatabaseInstance;
import com.example.sch_agro.DAO.ActivityDAO;
import com.example.sch_agro.DAO.TaskGebaDAO;
import com.example.sch_agro.DAO.TaskSanDAO;
import com.example.sch_agro.DAO.TrabalhadoresDAO;
import com.example.sch_agro.DAO.UserDAO;
import com.example.sch_agro.R;
import com.example.sch_agro.Services.ApiService;
import com.example.sch_agro.Services.DataSyncManager;
import com.example.sch_agro.Services.NetworkMonitor;
import com.example.sch_agro.ui.fragment.AddActFragment;
import com.example.sch_agro.ui.fragment.AddUserFragment;
import com.example.sch_agro.ui.fragment.DashboardFragment;
import com.example.sch_agro.ui.fragment.RelatoriosFragment;
import com.example.sch_agro.ui.fragment.ViewFarmerFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Handler handler = new Handler();
    private Runnable syncRunnable;
    private static final long SYNC_INTERVAL = 15 * 60 * 1000; // 15 minutos em milissegundos

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

        // Inicialização do monitor de rede
        NetworkMonitor networkMonitor = new NetworkMonitor(this);

        // Instância dos DAOs
        UserDAO userDao = DatabaseInstance.getInstance(this).userDao();
        ActivityDAO activityDao = DatabaseInstance.getInstance(this).activityDao();
        TrabalhadoresDAO trabalhadorDao = DatabaseInstance.getInstance(this).trabalhadorDao();
        TaskGebaDAO taskGebaDao = DatabaseInstance.getInstance(this).taskGebaDao();
        TaskSanDAO taskSanDao = DatabaseInstance.getInstance(this).taskSanDao();

        // Instância da ApiService
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Instância do DataSyncManager com os DAOs e ApiService
        DataSyncManager syncManager = new DataSyncManager(userDao, activityDao, trabalhadorDao, taskGebaDao, taskSanDao, apiService);

        // Iniciar o monitoramento da rede e executar sincronização quando conectado
        networkMonitor.startMonitoring(syncManager::syncData);

        // Agendar a sincronização a cada 15 minutos
        syncRunnable = new Runnable() {
            @Override
            public void run() {
                networkMonitor.startMonitoring(syncManager::syncData);
                handler.postDelayed(this, SYNC_INTERVAL);
            }
        };

        // Iniciar a primeira sincronização
        handler.post(syncRunnable);
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