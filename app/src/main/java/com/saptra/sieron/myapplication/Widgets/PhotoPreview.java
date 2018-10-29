package com.saptra.sieron.myapplication.Widgets;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.saptra.sieron.myapplication.R;
import com.squareup.picasso.Picasso;

public class PhotoPreview extends AppCompatActivity {

    Toolbar tlbPreview;
    ImageView imvPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);

        imvPreview = (ImageView) findViewById(R.id.imvPreview);
        tlbPreview = (android.support.v7.widget.Toolbar) findViewById(R.id.tlbPreview);
        tlbPreview.setTitle("Viste Previa");
        setSupportActionBar(tlbPreview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String mCurrentPhotoPath = getIntent().getStringExtra("CurrentPhotoPath");
        if(mCurrentPhotoPath != ""){
            Picasso.with(this).load(Uri.parse(mCurrentPhotoPath))
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
