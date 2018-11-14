package com.saptra.sieron.myapplication.Widgets;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.google.gson.Gson;
import com.rey.material.widget.ProgressView;
import com.saptra.sieron.myapplication.Controllers.CheckInActivity;
import com.saptra.sieron.myapplication.Data.mLecturaCertificadosData;
import com.saptra.sieron.myapplication.Models.dDetallePlanSemanal;
import com.saptra.sieron.myapplication.Models.mLecturaCertificados;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.Adapters.CertificadoListAdapter;
import com.saptra.sieron.myapplication.Utils.Interfaces.RecyclerViewClickListener;

import java.util.ArrayList;

public class CertificadoListActivity extends AppCompatActivity {

    //Controls
    private RecyclerView rvwSelectList;
    private Button btnCancelar;
    private ProgressView pvLoading;
    private RelativeLayout rlCancelar;

    //Other
    private static final int INTENT_CHECKIN = 112;
    private ArrayList<mLecturaCertificados> lstCertificados;
    private CertificadoListAdapter adapterCertificado;
    private dDetallePlanSemanal model;
    private String _model = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificado_list);

        //android O fix bug orientation
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        _model = getIntent().getStringExtra("model");
        model = new Gson().fromJson(_model, dDetallePlanSemanal.class);

        rvwSelectList = (RecyclerView) findViewById(R.id.rvwSelectList);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        pvLoading = (ProgressView) findViewById(R.id.pvProgress);
        rlCancelar = (RelativeLayout) findViewById(R.id.rlCancelar);

        ControlBehavior(false);

        if(model != null) {
            lstCertificados = new ArrayList<mLecturaCertificados>();
            lstCertificados = mLecturaCertificadosData.getInstance(this)
                    .getCertificadosLeidos(model.getDetallePlanId());
        }

        if(lstCertificados.size() > 0){
            ControlBehavior(true);
            rvwSelectList.setLayoutManager(new LinearLayoutManager(this));
            adapterCertificado = new CertificadoListAdapter(this, lstCertificados);
            adapterCertificado.setRecyclerViewItemClickListener(new RecyclerViewClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(getApplication(), CheckInActivity.class);
                    intent.putExtra("model", _model);
                    intent.putExtra("certificado", lstCertificados.get(position)
                            .getFolioCertificado());
                    startActivityForResult(intent, INTENT_CHECKIN);
                    finish();
                }
            });
            rvwSelectList.setAdapter(adapterCertificado);
        }

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void ControlBehavior(boolean hidden){
        pvLoading.setVisibility(hidden ? View.GONE : View.VISIBLE);
        rlCancelar.setVisibility(!hidden ? View.GONE : View.VISIBLE);
    }
}
