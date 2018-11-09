package com.saptra.sieron.myapplication.Controllers;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import java.text.SimpleDateFormat;

import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.rey.material.widget.Button;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.ProgressView;
import com.saptra.sieron.myapplication.BuildConfig;
import com.saptra.sieron.myapplication.Data.mCheckInData;
import com.saptra.sieron.myapplication.Data.mLecturaCertificadosData;
import com.saptra.sieron.myapplication.Models.dDetallePlanSemanal;
import com.saptra.sieron.myapplication.Models.mCheckIn;
import com.saptra.sieron.myapplication.Models.mLecturaCertificados;
import com.saptra.sieron.myapplication.Models.mUsuarios;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.Globals;
import com.saptra.sieron.myapplication.Utils.Interfaces.ServiceApi;
import com.saptra.sieron.myapplication.Widgets.PhotoPreview;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckInActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //Controls
    Toolbar tlbCheckIn;
    TextView txvActividad, txvPeriodo, txvFecha, txvHora, txvChecks;
    TextInputLayout tilBarCode, tilIncidencia;
    FloatingActionButton btnScan;
    CircleImageView civPreview;
    FloatingActionButton btnTakePhoto;
    Button btnCheckIn;
    View llyCertificado;
    private ProgressView pvProgress;

    //Others
    private Retrofit mRestAdapter;
    private ServiceApi ServicioApi;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath = "";
    private static final int SCAN_BAR_CODE = 201;
    private static final int CAMERA_REQUEST = 202;
    private static final int REQUEST_LOCATION = 203;
    private mUsuarios user;
    private dDetallePlanSemanal model;
    private boolean LeerCertificado = false;
    private mCheckIn checkIn;

    //Geolocalization
    String lattitude ="", longitude="";
    private Location mLocation = null;
    private GoogleApiClient mGoogleApiClient = null;
    private LocationManager mLocationManager = null;
    private LocationRequest mLocationRequest = null;
    private final String TAG = "CheckInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        /********************************************
         *      PARAMETROS DE SERVICIO WEB
         *******************************************/
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(ServiceApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServicioApi = mRestAdapter.create(ServiceApi.class);

        /********************************************
         *      INSTANCIAS DE CONTROLES UI
         *******************************************/
        pvProgress = (ProgressView) findViewById(R.id.pvProgress);
        tlbCheckIn = (android.support.v7.widget.Toolbar) findViewById(R.id.tlbCheckIn);
        tlbCheckIn.setSubtitle("Check-In");
        setSupportActionBar(tlbCheckIn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txvChecks = (TextView) findViewById(R.id.txvChecks);
        txvActividad = (TextView) findViewById(R.id.txvActividad);
        txvPeriodo = (TextView) findViewById(R.id.txvPeriodo);
        txvFecha = (TextView) findViewById(R.id.txvFecha);
        txvHora = (TextView) findViewById(R.id.txvHora);
        civPreview = (CircleImageView) findViewById(R.id.civPreview);
        btnScan = (FloatingActionButton) findViewById(R.id.btnScan);
        btnTakePhoto = (FloatingActionButton) findViewById(R.id.btnTakePhoto);
        tilBarCode = (TextInputLayout) findViewById(R.id.tilBarCode);
        tilIncidencia = (TextInputLayout) findViewById(R.id.tilIncidencia);
        tilBarCode.getEditText().setKeyListener(null);
        btnCheckIn = (Button) findViewById(R.id.btnCheckIn);
        llyCertificado = findViewById(R.id.llyCertificado);

        pvProgress.setVisibility(View.GONE);

        /***********************************************************
                GEOLIZALICACION
         ************************************************************/
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        checkLocation();
        /***********************************************************/

        /********************************************
         *      ACTIVIDAD A MOSTRAR
         *******************************************/
        try {
            user = new mUsuarios();
            checkIn = new mCheckIn();
            user = getLoggedUser();
            String _model = getIntent().getStringExtra("model");
            model = new Gson().fromJson(_model, dDetallePlanSemanal.class);
            int DetallePlanId = model.getDetallePlanId();
            long checks = mCheckInData.getInstance(getApplication()).getCheckInRealizados(DetallePlanId);
            int TotalChecks = model.getCantidadCheckIn();

            if (model != null) {

                txvActividad.setText(model.getTipoActividades().getNombreActividad());
                txvPeriodo.setText(model.getPlanSemanal().getPeriodos().getDecripcionPeriodo());
                txvFecha.setText(model.getFechaActividad().substring(0, 10));
                txvHora.setText(model.getHoraActividad().substring(0, 5));

                //Ocultar / Mostrar Lector de Certificados
                //if (RequiereCertificado(model)) {
                    llyCertificado.setVisibility(View.VISIBLE);
                //} else {
                    //llyCertificado.setVisibility(View.GONE);
                //}

                txvChecks.setText(checks + "/" + TotalChecks);
                if (checks == TotalChecks) {
                    checkIn = mCheckInData.getInstance(getApplication()).getCheckInsDetallePlan(DetallePlanId);
                    if (checkIn.getCheckInId() > 0) {
                        //Ocultar botones
                        btnCheckIn.setVisibility(View.GONE);
                        btnTakePhoto.setVisibility(View.GONE);
                        btnScan.setVisibility(View.GONE);
                        //mostrar indicencia registrada
                        tilIncidencia.setFocusable(false);
                        tilIncidencia.setEnabled(false);
                        tilIncidencia.getEditText().setText(checkIn.getIncidencias());
                        Bitmap img = Globals.getInstance().Base64ToBitmap(checkIn.getImageData());
                        if (img != null) {
                            civPreview.setImageBitmap(img);
                        }
                    }
                }
            }
        }
        catch(Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        /********************************************
         *      EVENTOS CLICK
         *******************************************/
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
                        Toast.makeText(getApplication(),
                                "createImgFile-Error: "+ex.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                    if (photoFile != null) {
                        try {
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getUri(photoFile));
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                        catch(Exception ex){
                            Toast.makeText(getApplication(),
                                    "CAMERA_INTENT-Error: "+ex.getMessage(),
                                    Toast.LENGTH_SHORT).show();
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
                        .setMessage("Se realizará check-in a la actividad actual, continuar?")
                        .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                GenerateCheckIn();
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
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Error de conexión. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null){
            mGoogleApiClient.connect();
            //getCurrentLocation();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (location != null)
        {
            lattitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            //Toast.makeText(this, location.getLatitude()+" "+location.getLongitude(), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "location is null", Toast.LENGTH_SHORT).show();
            lattitude = "";
            longitude = "";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length <= 0) {
                Log.i("onRequestPermission", "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
            } else {
                // Permission denied.
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
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
                        //mImageBitmap = reduceBitmapSize(mCurrentPhotoPath);
                        //civPreview.setImageBitmap(mImageBitmap);
                        Picasso.with(this).load(Uri.parse(mCurrentPhotoPath))
                                .placeholder(getResources().getDrawable(R.drawable.ic_preview))
                                .fit()
                                .centerCrop()
                                .into(civPreview);
                        civPreview.setBorderColorResource(R.color.azul_gto);
                    } catch (Exception e) {
                        Log.e("mImageBitmap", e.getMessage());
                    }
                    break;
            }
        }
    }

    /*******************************************************************************
            METHODS
     /******************************************************************************/

    private void GenerateCheckIn(){
        pvProgress.setVisibility(View.VISIBLE);
        byte[] bFile;
        String base64File = "";
        mLecturaCertificados certificado = new mLecturaCertificados();
        long newCheckInID = 0, newCertificado = 0;

        if(!Validaciones()) {
            pvProgress.setVisibility(View.GONE);
            return;
        }

        try {
            if(mImageBitmap != null) {
                //bFile = Globals.getInstance().bitmapToByte(mImageBitmap);
                //base64File = Globals.getInstance().ByteArrayToB64Sring(bFile);
            }

            checkIn.getDetallePlan().setDetallePlanId(model.getDetallePlanId());
            checkIn.setImageData(base64File);
            checkIn.setUsuarioCreacionId(user.getUsuarioId());
            checkIn.setCoordenadas(lattitude+","+longitude);
            checkIn.setIncidencias(tilIncidencia.getEditText().getText().toString());
            if(RequiereCertificado(model)){
                certificado.setFolioCertificado(tilBarCode.getEditText().getText().toString());
                certificado.setUsuarioCreacionId(user.getUsuarioId());
            }
            newCheckInID = mCheckInData.getInstance(this).createCheckIn(checkIn);
            if(newCheckInID > 0){
                int NewID = (int) newCheckInID;
                certificado.getCheckIn().setCheckInId(NewID);
                newCertificado = mLecturaCertificadosData.getInstance(this).createLecturaCerificado(certificado);
                pvProgress.setVisibility(View.GONE);
                showToastMessage("CheckIn correcto!!");
                DisabledControls();
            }
        }catch (Exception ex){
            pvProgress.setVisibility(View.GONE);
            showToastMessage("GenerateCheckIn-ERROR:"+ex.getMessage());
            if(newCheckInID > 0){
                int CheckInId = (int) newCheckInID;
                int CertificadoId = (int) newCertificado;
                mCheckInData.getInstance(this).deleteCheckInRow(CheckInId);
                mLecturaCertificadosData.getInstance(this).deleteLectCerRow(CertificadoId);
            }
        }
    }

    private boolean Validaciones(){

        boolean validacion = false;

        //VALIDAR ACTIVIDAD
        if(model.getDetallePlanId() == 0){
            showToastMessage("La actividad no es válida");
            validacion = false;
        }
        else
            validacion = true;
        //VALIDAR UBICACION
        /*getCurrentLocation();
        if(lattitude.equals("") || longitude.equals("")) {
            showToastMessage("No se puede obtener su ubicación, verifique su gps");
            validacion = false;
        }
        else
            validacion = true;*/
        //VALIDAR LECTURA DE CERTIFICADO
        if(RequiereCertificado(model)){
            if(tilBarCode.getEditText().getText().equals("")){
                tilBarCode.setErrorEnabled(true);
                showToastMessage("No se han leído ningún código");
                tilBarCode.setError("No se han leído ningún código");
                validacion = false;
            }
            else {
                tilBarCode.setErrorEnabled(false);
                validacion = true;
            }
        }
        return validacion;
    }

    protected void startLocationUpdates() {
        // Create the location request
        /***************************************
         * LOCALIZATION INIT
         * **************************************/
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(7000);
        mLocationRequest.setSmallestDisplacement(1);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    private boolean isLocationEnabled() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            AlertMessageNoGPS();
        return isLocationEnabled();
    }

    private void getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {
            lattitude = String.valueOf(mLocation.getLatitude());
            longitude = String.valueOf(mLocation.getLongitude());
            Toast.makeText(getApplication(), "Lat:"+lattitude+"  long:"+longitude, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Sin poder detectar geolocalización", Toast.LENGTH_SHORT).show();
            lattitude = ""; longitude = "";
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

    private void PostCheckIn(mCheckIn checkIn){
        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), checkIn.getImageData());
        Call<String> call = ServicioApi.PostCheckIn(checkIn);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("ERROR", response.errorBody().toString());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ERROR_CHECK",t.getLocalizedMessage());
            }
        });
    }

    private void showToastMessage(String message){
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
    }

    private boolean RequiereCertificado(dDetallePlanSemanal actividad){
        boolean requerido = false;
        if(actividad.getTipoActividades().getActividadEspecial() &&
                actividad.getCantidadCheckIn() > 1){
            requerido = true;
        }
        return requerido;
    }

    private mUsuarios getLoggedUser(){
        mUsuarios user = new mUsuarios();
        String sharedPreferences = getResources().getString(R.string.SATRA_PREFERENCES);
        boolean logged;
        SharedPreferences settings = getApplication()
                .getSharedPreferences(sharedPreferences, getApplication().MODE_PRIVATE);
        logged = settings.getBoolean(getResources().getString(R.string.sp_Logged), false);
        user.setUsuarioId(
                settings.getInt(getResources().getString(R.string.sp_UsuarioId),0)
        );
        return user;
    }

    private Bitmap reduceBitmapSize(String  filepath)
    {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, bmOptions);
        bmOptions.inSampleSize = calculateInSampleSize(bmOptions);
        bmOptions.inJustDecodeBounds = false;
        Bitmap bitmap =BitmapFactory.decodeFile(filepath,bmOptions);
        return bitmap;
    }
    // This method is used to calculate largest inSampleSize
//which is used to decode bitmap in required bitmap.
    private int calculateInSampleSize(BitmapFactory.Options bmOptions)
    {
        // Raw height and width of image
        final int photoWidth = bmOptions.outWidth;
        final int photoHeight = bmOptions.outHeight;
        int scaleFactor = 1;
        if (photoWidth > 640
                || photoHeight > 960)
        {
            final int halfPhotoWidth = photoWidth / 2;
            final int halfPhotoHeight = photoHeight / 2;

            // Calculate the largest inSampleSize value that is a power of 2
            //and keeps both height and width larger than the requested height and width.
            while ((halfPhotoWidth / scaleFactor) >= 640
                    && (halfPhotoHeight / scaleFactor) >= 960)
            {
                scaleFactor *= 2;
            }
        }
        return scaleFactor;
    }

    private void DisabledControls(){
        btnScan.setVisibility(View.GONE);
        btnTakePhoto.setVisibility(View.GONE);
        btnCheckIn.setVisibility(View.GONE);
        tilIncidencia.setFocusable(false);
        tilIncidencia.setEnabled(false);
        tilBarCode.setFocusable(false);
        tilBarCode.setEnabled(false);
        //Actualizar indicador CheckIn´s
        int DetallePlanId = model.getDetallePlanId();
        long checks = mCheckInData.getInstance(getApplication()).getCheckInRealizados(DetallePlanId);
        int TotalChecks = model.getCantidadCheckIn();
        txvChecks.setText(checks + "/" + TotalChecks);
    }
}
