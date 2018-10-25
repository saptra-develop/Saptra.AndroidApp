package com.saptra.sieron.myapplication.Controllers;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.saptra.sieron.myapplication.Models.dDetallePlanSemanal;
import com.saptra.sieron.myapplication.R;

public class CheckInActivity extends AppCompatActivity {

    Toolbar tlbCheckIn;

    dDetallePlanSemanal model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        String _model = getIntent().getStringExtra("model");
        model = new Gson().fromJson(_model, dDetallePlanSemanal.class);

        tlbCheckIn = (android.support.v7.widget.Toolbar) findViewById(R.id.tlbCheckIn);
        tlbCheckIn.setSubtitle("Check-In");
        setSupportActionBar(tlbCheckIn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
