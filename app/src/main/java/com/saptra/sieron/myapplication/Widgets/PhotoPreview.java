package com.saptra.sieron.myapplication.Widgets;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.saptra.sieron.myapplication.Data.mCheckInData;
import com.saptra.sieron.myapplication.Models.mCheckIn;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.Globals;
import com.squareup.picasso.Picasso;

public class PhotoPreview extends AppCompatActivity {

    //Controls
    private Toolbar tlbPreview;
    private ImageView imvPreview;

    //Others
    private int DetallePlanId = 0;
    private String mCurrentPhotoPath = "", certificado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);

        //android O fix bug orientation
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        imvPreview = (ImageView) findViewById(R.id.imvPreview);
        tlbPreview = (android.support.v7.widget.Toolbar) findViewById(R.id.tlbPreview);
        tlbPreview.setTitle("Vista Previa");
        setSupportActionBar(tlbPreview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mCurrentPhotoPath = getIntent().getStringExtra("CurrentPhotoPath");
        DetallePlanId = getIntent().getIntExtra("DetallePlanId", 0);
        certificado = getIntent().getStringExtra("certificado");

        mCheckIn checkIn = mCheckInData.getInstance(this).getCheckInsDetallePlan(DetallePlanId, certificado);

        if(!mCurrentPhotoPath.equals("")){
            Picasso.with(this).load(Uri.parse(mCurrentPhotoPath))
                    .placeholder(getResources().getDrawable(R.drawable.ic_preview))
                    .fit()
                    .centerCrop()
                    .into(imvPreview);
        }
        else if(!checkIn.getFotoIncidencia().equals("")){
            Picasso.with(this).load(Uri.parse(checkIn.getFotoIncidencia()))
                    .placeholder(getResources().getDrawable(R.drawable.ic_preview))
                    .fit()
                    .centerCrop()
                    .into(imvPreview);
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
}
