package com.saptra.sieron.myapplication.Controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import java.text.SimpleDateFormat;

import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.gson.Gson;
import com.rey.material.widget.Button;
import com.rey.material.widget.FloatingActionButton;
import com.saptra.sieron.myapplication.BuildConfig;
import com.saptra.sieron.myapplication.Models.dDetallePlanSemanal;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.Services.AppLocationService;
import com.saptra.sieron.myapplication.Widgets.PhotoPreview;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class CheckInActivity extends AppCompatActivity {

    //Controls
    Toolbar tlbCheckIn;
    TextInputLayout tilBarCode;
    FloatingActionButton btnScan;
    CircleImageView civPreview;
    FloatingActionButton btnTakePhoto;
    Button btnCheckIn;

    //Others
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath = "";
    private static final int SCAN_BAR_CODE = 201;
    private static final int CAMERA_REQUEST = 202;
    AppLocationService appLocationService;

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

        civPreview = (CircleImageView) findViewById(R.id.civPreview);
        //civPreview = (ImageView) findViewById(R.id.civPreview);
        btnScan = (FloatingActionButton) findViewById(R.id.btnScan);
        btnTakePhoto = (FloatingActionButton) findViewById(R.id.btnTakePhoto);
        tilBarCode = (TextInputLayout) findViewById(R.id.tilBarCode);
        tilBarCode.getEditText().setKeyListener(null);
        btnCheckIn = (Button) findViewById(R.id.btnCheckIn);

        appLocationService = new AppLocationService(this);

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
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(cameraIntent.resolveActivity(getPackageManager()) != null){
                    File photoFile = null;
                    try{
                        photoFile = createImageFile();
                    }
                    catch (IOException ex){
                        Log.e("photoFile-block", ex.getMessage());
                    }
                    if (photoFile != null) {
                        try {

                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getUri(photoFile));
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                        catch(Exception ex){
                            Log.e("CAMERA_INTENT", ex.getMessage());
                        }
                    }
                }
            }
        });

        civPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentPhotoPath != "") {
                    Intent intent = new Intent(CheckInActivity.this, PhotoPreview.class);
                    intent.putExtra("CurrentPhotoPath", mCurrentPhotoPath);
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplication(),
                            "Vista previa no disponible",
                            Toast.LENGTH_LONG).show();
            }
        });

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActividadCheckIn();
                /*new AlertDialog.Builder(getApplication())
                        .setTitle("AVISO")
                        .setMessage("Se cerrará la sesión actual. Continuar?")
                        .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();*/
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do whatever
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
                    try {
                        mImageBitmap = MediaStore.Images.Media
                                .getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                        //civPreview.setImageBitmap(mImageBitmap);
                        Picasso.with(this).load(Uri.parse(mCurrentPhotoPath))
                                .placeholder(getResources().getDrawable(R.drawable.ic_preview))
                                .fit()
                                .centerCrop()
                                .into(civPreview);
                        civPreview.setBorderColorResource(R.color.azul_gto);
                    } catch (IOException e) {
                        Log.e("mImageBitmap", e.getMessage());
                    }
                    break;
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private Uri getUri(File file){
        Uri uri = null;
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                uri = FileProvider.getUriForFile(
                        this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());
            }
            else uri = Uri.fromFile(file);
        }
        catch (Exception ex){
            Log.e("Method-getUri", ex.getMessage());
        }
        return  uri;
    }

    private void ActividadCheckIn(){
        getCurrentLocation();
    }

    private void getCurrentLocation(){
        Location gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
        if(gpsLocation != null){
            double latitude = gpsLocation.getLatitude();
            double longitude = gpsLocation.getLongitude();
            Toast.makeText(getApplication(),
                    "Localizaion móvil (GPS): \nLatitude: " + latitude
                            + "\nLongitude: " + longitude,
                    Toast.LENGTH_LONG).show();
        }
        else{
            showSettingsAlert("GPS");
        }
    }

    public void showSettingsAlert(String provider) {
        new AlertDialog.Builder(getApplication())
        .setTitle("CONFIGURACIÓN "+provider)
        .setMessage(provider + " no disponible, activar?")
        .setPositiveButton("ACTIVAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        })
        .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
}
