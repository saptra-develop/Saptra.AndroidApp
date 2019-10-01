package com.saptra.sieron.myapplication.Controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.saptra.sieron.myapplication.Data.cPeriodosData;
import com.saptra.sieron.myapplication.Data.dDetallePlanSemanalData;
import com.saptra.sieron.myapplication.Data.mLecturaCertificadosData;
import com.saptra.sieron.myapplication.Models.cPeriodos;
import com.saptra.sieron.myapplication.Models.dDetallePlanSemanal;
import com.saptra.sieron.myapplication.Models.mLecturaCertificados;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.Adapters.PlaneacionListAdapter;
import com.saptra.sieron.myapplication.Utils.Interfaces.PlaneacionViewListener;
import com.saptra.sieron.myapplication.Widgets.CertificadoListActivity;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class PlaningFragment extends Fragment {

    //Controls
    RecyclerView rvwDetPlanSem;
    //Others
    private ArrayList<dDetallePlanSemanal> lstDetPlanSem;
    private PlaneacionListAdapter planeacionListAdapter;
    private cPeriodos periodo;
    private static final int INTENT_CHECKIN = 112;
    private static final int INTENT_CERTIFICADOS = 113;
    private dDetallePlanSemanal currentActividad;

    public PlaningFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_planing, container, false);
        //Instancias
        //*********************************************
        currentActividad = new dDetallePlanSemanal();
        lstDetPlanSem = new ArrayList<dDetallePlanSemanal>();
        rvwDetPlanSem =(RecyclerView) view.findViewById(R.id.rvwDetPlanSem);
        //*********************************************
        periodo = (cPeriodos) cPeriodosData.getInstance(getContext()).getPeridoSemanal();
        if(periodo != null) {

            lstDetPlanSem = (ArrayList<dDetallePlanSemanal>)
                    dDetallePlanSemanalData.getInstance(getContext())
                            .getDetPlanSemanal(periodo.getPeriodoId());
        }
        if(lstDetPlanSem.size() > 0) {
            rvwDetPlanSem.setLayoutManager(new LinearLayoutManager(getContext()));
            planeacionListAdapter = new PlaneacionListAdapter(getContext(), lstDetPlanSem);
            planeacionListAdapter.setPlaneacionViewListener(new PlaneacionViewListener() {
                @Override
                public void ButtonClick(View view, final int position) {
                    Intent intent = new Intent(getActivity(), CheckInActivity.class);
                    String model = new Gson().toJson(lstDetPlanSem.get(position));
                    intent.putExtra("model", model);
                    startActivityForResult(intent, INTENT_CHECKIN);
                }

                @Override
                public void CertificadoClick(View view, int position) {
                    int DetallePlanId = lstDetPlanSem.get(position).getDetallePlanId();
                    long certificados = mLecturaCertificadosData.getInstance(getActivity())
                            .getCertificadosCount(DetallePlanId);
                    if(certificados > 0) {
                        Intent intent = new Intent(getActivity(), CertificadoListActivity.class);
                        String model = new Gson()
                                .toJson(lstDetPlanSem.get(position), dDetallePlanSemanal.class);
                        intent.putExtra("model", model);
                        startActivityForResult(intent, INTENT_CHECKIN);
                    }
                }
            });
            rvwDetPlanSem.setAdapter(planeacionListAdapter);
        }
        else{
            Toast.makeText(getContext(), "No se obtuvo información", Toast.LENGTH_LONG).show();
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode) {
                case INTENT_CHECKIN:
                    lstDetPlanSem = (ArrayList<dDetallePlanSemanal>)
                            dDetallePlanSemanalData
                                    .getInstance(getContext()).getDetPlanSemanal(periodo.getPeriodoId());
                    planeacionListAdapter.notifyDataSetChanged();
                    break;
                case INTENT_CERTIFICADOS:
                    Intent intent = new Intent(getActivity(), CheckInActivity.class);
                    String model = new Gson().toJson(currentActividad);
                    intent.putExtra("model", model);
                    startActivityForResult(intent, INTENT_CHECKIN);
                    break;
            }
        }
    }
}
