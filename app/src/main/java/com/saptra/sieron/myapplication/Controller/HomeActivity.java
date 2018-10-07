package com.saptra.sieron.myapplication.Controller;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.saptra.sieron.myapplication.R;

public class HomeActivity extends AppCompatActivity {

    //Controls
    private DrawerLayout drlHome;
    private Toolbar tlbHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Instancias
        //*********************************************
        drlHome = (DrawerLayout) findViewById(R.id.drlHome);
        tlbHome = (Toolbar) findViewById(R.id.tlbHome);
        setSupportActionBar(tlbHome);
        getSupportActionBar().setHomeButtonEnabled(true);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drlHome,
                tlbHome,
                R.string.drawer_open,
                R.string.drawer_close
                ){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drlHome.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView nvwHome = (NavigationView) findViewById(R.id.nvwHome);
        if(nvwHome != null){
            setupNavigationDrawerContent(nvwHome);
        }
        setFragment(0);
        tlbHome.setSubtitle(R.string.str_home);
        //*********************************************
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int Id = item.getItemId();
        switch (Id){
            case R.id.home:
                drlHome.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.itmHome:
                            menuItem.setChecked(true);
                            setFragment(0);
                            tlbHome.setSubtitle(R.string.str_home);
                            drlHome.closeDrawer(GravityCompat.START);
                            return  true;
                        case R.id.itmDonwload:
                            menuItem.setChecked(true);
                            setFragment(1);
                            tlbHome.setSubtitle(R.string.str_descargar);
                            drlHome.closeDrawer(GravityCompat.START);
                            return  true;
                        case R.id.itmPlaneacion:
                            menuItem.setChecked(true);
                            setFragment(2);
                            tlbHome.setSubtitle(R.string.str_planeacion);
                            drlHome.closeDrawer(GravityCompat.START);
                            return  true;
                        case R.id.itmConsultar:
                            menuItem.setChecked(true);
                            setFragment(3);
                            tlbHome.setSubtitle(R.string.str_consultar);
                            drlHome.closeDrawer(GravityCompat.START);
                            return  true;
                    }
                    return false;
                }
        });
    }

    private void setFragment(int position){
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (position){
            case 0:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                InicioFragment inicioFragment = new InicioFragment();
                fragmentTransaction.replace(R.id.principalContent, inicioFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                DownloadFragment downloadFragment = new DownloadFragment();
                fragmentTransaction.replace(R.id.principalContent, downloadFragment);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                PlaningFragment planingFragment = new PlaningFragment();
                fragmentTransaction.replace(R.id.principalContent, planingFragment);
                fragmentTransaction.commit();
                break;
            case 3:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                ConsultingFragment consultingFragment = new ConsultingFragment();
                fragmentTransaction.replace(R.id.principalContent, consultingFragment);
                fragmentTransaction.commit();
                break;
        }
    }

    private void CloseApp(){

    }
}
