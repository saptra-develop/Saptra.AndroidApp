package com.saptra.sieron.myapplication.Widgets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.google.gson.Gson;
import com.rey.material.widget.ProgressView;
import com.saptra.sieron.myapplication.Data.mLecturaCertificadosData;
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
    ArrayList<mLecturaCertificados> lstCertificados;
    CertificadoListAdapter adapterCertificado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificado_list);

        int DetallePlanId = getIntent().getIntExtra("DetallePlanId", 0);

        rvwSelectList = (RecyclerView) findViewById(R.id.rvwSelectList);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        pvLoading = (ProgressView) findViewById(R.id.pvProgress);
        rlCancelar = (RelativeLayout) findViewById(R.id.rlCancelar);

        ControlBehavior(false);

        lstCertificados = new ArrayList<mLecturaCertificados>();
        lstCertificados = mLecturaCertificadosData.getInstance(this).getCertificadosPorActividad(DetallePlanId);

        if(lstCertificados.size() > 0){
            ControlBehavior(true);
            rvwSelectList.setLayoutManager(new LinearLayoutManager(this));
            adapterCertificado = new CertificadoListAdapter(this, lstCertificados);
            adapterCertificado.setRecyclerViewItemClickListener(new RecyclerViewClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent returnIntent = new Intent();
                    String certificado = new Gson().toJson(lstCertificados.get(position), mLecturaCertificados.class);
                    returnIntent.putExtra("Certificado", certificado);
                    setResult(RESULT_OK, returnIntent);
                    ((CertificadoListActivity) view.getContext()).finish();
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
