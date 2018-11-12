package com.saptra.sieron.myapplication.Controllers;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.saptra.sieron.myapplication.Models.mUsuarios;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.ExceptionHandler;
import com.saptra.sieron.myapplication.Utils.Globals;
import com.saptra.sieron.myapplication.Widgets.SynchronizerService;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    //Controls
    private DrawerLayout drlHome;
    private Toolbar tlbHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        }

        try {

            startService(new Intent(this, SynchronizerService.class));
            Calendar cal = Calendar.getInstance();
            Intent intent = new Intent(this, SynchronizerService.class);
            PendingIntent pintent = PendingIntent
                    .getService(this, 0, intent, 0);
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            // Start service every hour
            alarm.setInexactRepeating(AlarmManager.RTC, cal.getTimeInMillis(),
                    (1000 * 60 * 1), pintent);
        }
        catch (Exception e){
            Toast.makeText(getApplication(),
                    "Home-Serc Error:"+ e.getMessage(), Toast.LENGTH_LONG).show();
        }

        /*if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }*/
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            boolean allPermissionsGranted = true;
            ArrayList<String> toRequestPermissions = new ArrayList<>();

            for(String permission : Globals.REQUIRED_PERMISSIONS){
                if(ContextCompat.checkSelfPermission(this, permission) !=
                        PackageManager.PERMISSION_GRANTED){
                    toRequestPermissions.add(permission);
                    allPermissionsGranted = false;
                }
            }
            if(allPermissionsGranted) {
                Toast.makeText(this,
                        "Todos los permisos asignados",
                        Toast.LENGTH_LONG).show();
            }
            else {
                ActivityCompat.requestPermissions(this,
                        toRequestPermissions.toArray(new String[toRequestPermissions.size()]),
                        Globals.REQUIRED_PERMISSIONS_CODE);
            }
        }

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Globals.REQUIRED_PERMISSIONS_CODE) {
            boolean allPermGranted = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                            this,
                            "Permissions not granted: " + permissions[i],
                            Toast.LENGTH_LONG).show();
                    allPermGranted = false;
                    finish();
                    break;
                }
            }
            /*if (allPermGranted)
                handleWriteSettingsPermission();*/
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                        case R.id.itmSalir:
                            CloseSession();
                            break;
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

    private void CloseSession(){
        new AlertDialog.Builder(this)
                .setTitle("AVISO")
                .setMessage("Se cerrará la sesión actual. Continuar?")
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sharedPreferences = getResources().getString(R.string.SATRA_PREFERENCES);
                        SharedPreferences settings = getApplication()
                                .getSharedPreferences(sharedPreferences, getApplication().MODE_PRIVATE);
                        settings.edit().clear().commit();
                        Intent intent = new Intent(getApplication(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}
