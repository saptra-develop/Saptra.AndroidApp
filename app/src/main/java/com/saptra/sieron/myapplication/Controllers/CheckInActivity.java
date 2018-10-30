package com.saptra.sieron.myapplication.Controllers;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import java.text.SimpleDateFormat;

import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.rey.material.widget.Button;
import com.rey.material.widget.FloatingActionButton;
import com.saptra.sieron.myapplication.BuildConfig;
import com.saptra.sieron.myapplication.Models.dDetallePlanSemanal;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Widgets.PhotoPreview;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CheckInActivity extends AppCompatActivity {

    //Controls
    Toolbar tlbCheckIn;
    TextInputLayout tilBarCode, tilIncidencia;
    FloatingActionButton btnScan;
    CircleImageView civPreview;
    FloatingActionButton btnTakePhoto;
    Button btnCheckIn;

    //Others
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath = "";
    private static final int SCAN_BAR_CODE = 201;
    private static final int CAMERA_REQUEST = 202;
    private static final int REQUEST_LOCATION = 203;
    LocationManager locationManager;
    String lattitude, longitude;
    FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;

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
        tilIncidencia = (TextInputLayout) findViewById(R.id.tilIncidencia);
        tilBarCode.getEditText().setKeyListener(null);
        btnCheckIn = (Button) findViewById(R.id.btnCheckIn);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


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
                try {
                new AlertDialog.Builder(CheckInActivity.this)
                        .setTitle("AVISO")
                        .setMessage("Se cerrará la sesión actual. Continuar?")
                        .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                                    AlertMessageNoGPS();
                                }
                                else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                                    getLastLocation();
                                }
                            }
                        })
                        .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setCancelable(false)
                        .show();
                }
                catch (Exception ex){
                    Log.e("btnCheckIn", ex.getMessage());
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i("onRequestPermission", "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar("Warn", "action",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    private void showSnackbar(final String mainTextStringId, final String actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(R.id.clMensajes),
                mainTextStringId,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(actionStringId, listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(CheckInActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i("requestPermissions", "Displaying permission rationale to provide additional context.");

            showSnackbar("Warn", "Ok",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i("*****", "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(CheckInActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(CheckInActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    CheckInActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);

        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLastLocation = task.getResult();

                                String lat = String.format(Locale.ENGLISH, "%s: %f",
                                        lattitude,
                                        mLastLocation.getLatitude());
                                String lon = String.format(Locale.ENGLISH, "%s: %f",
                                        longitude,
                                        mLastLocation.getLongitude());
                                Toast.makeText(getApplication(), "Your current location is" + "\n" + "Lattitude = " + lat
                                        + "\n" + "Longitude = " + lon, Toast.LENGTH_LONG).show();
                            } else {
                                Log.w("getLastLocation", "getLastLocation:exception", task.getException());

                            }
                        }
                    });
        }
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

    /*private void getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(CheckInActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(CheckInActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    CheckInActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Toast.makeText(getApplication(), "Your current location is"+ "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude, Toast.LENGTH_LONG).show();

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Toast.makeText(getApplication(), "Your current location is"+ "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude, Toast.LENGTH_LONG).show();


            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Toast.makeText(getApplication(), "Your current location is"+ "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude, Toast.LENGTH_LONG).show();

            }else{

                Toast.makeText(this,"Incapaz de rastrear su localización" ,Toast.LENGTH_SHORT).show();

            }
        }
    }*/

    protected void AlertMessageNoGPS() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(CheckInActivity.this);
        builder.setMessage("Es necesario activar su GPS, activar?")
                .setCancelable(false)
                .setPositiveButton("ACTIVAR", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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
    }


}
