package com.saptra.sieron.myapplication.Utils;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Globals {
    //Globals
    public static String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    }; //TODO You can Add multiple permissions here.
    public static final int REQUIRED_PERMISSIONS_CODE = 800;
    private Context context;
    public static Globals instance;

    //Constructor
    public Globals(){}

    static {
        instance = new Globals();
    }

    //Get Instance
    public static Globals getInstance(){
        return Globals.instance;
    }

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    //Valida si disposiivo está conectado a internet
    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
