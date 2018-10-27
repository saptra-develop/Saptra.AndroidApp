package com.saptra.sieron.myapplication.Controllers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.rey.material.widget.FloatingActionButton;
import com.saptra.sieron.myapplication.Models.dDetallePlanSemanal;
import com.saptra.sieron.myapplication.R;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class CheckInActivity extends AppCompatActivity {

    Toolbar tlbCheckIn;
    TextInputLayout tilBarCode;
    FloatingActionButton btnScan;
    CircleImageView civPreview;
    FloatingActionButton btnTakePhoto;


    private static final int SCAN_BAR_CODE = 201;
    private static final int CAMERA_REQUEST = 202;
    private static final int CAMERA_PERMISSION_CODE = 100;

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

        btnScan = (FloatingActionButton) findViewById(R.id.btnScan);
        btnTakePhoto = (FloatingActionButton) findViewById(R.id.btnTakePhoto);
        tilBarCode = (TextInputLayout) findViewById(R.id.tilBarCode);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplication(), ReadBarCodeActivity.class),
                        SCAN_BAR_CODE);
            }
        });

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CheckInActivity.this, new String[]{Manifest.permission.CAMERA},
                            0);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode) {
                case SCAN_BAR_CODE :
                    String _barcoderead = data.getStringExtra("BarCodeRead");
                    tilBarCode.getEditText().setText(_barcoderead);
                    break;
                case CAMERA_REQUEST :
                    Bundle extras = data.getExtras();
                    Bitmap photo = (Bitmap) extras.get("data");
                    civPreview.setImageBitmap(photo);
                    break;
            }
        }
    }
}
