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
import com.saptra.sieron.myapplication.Models.cPeriodos;
import com.saptra.sieron.myapplication.Models.dDetallePlanSemanal;
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
                    if(lstDetPlanSem.get(position).getTipoActividades().getActividadEspecial() &&
                            lstDetPlanSem.get(position).getCantidadCheckIn() > 1){
                        //Si actividad especial, preguntar...
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Actividad Especial")
                                .setPositiveButton("CHECK-IN", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(getActivity(), CheckInActivity.class);
                                        String model = new Gson().toJson(lstDetPlanSem.get(position));
                                        Log.e("ButtonClick", model);
                                        intent.putExtra("model", model);
                                        startActivityForResult(intent, INTENT_CHECKIN);
                                        dialogInterface.dismiss();

                                    }
                                })
                                .setNeutralButton("VER CERTIFICADOS", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(getActivity(), CertificadoListActivity.class);
                                        currentActividad = lstDetPlanSem.get(position);
                                        intent.putExtra("DetallePlanId", lstDetPlanSem.get(position).getDetallePlanId());
                                        startActivityForResult(intent, INTENT_CERTIFICADOS);
                                        dialogInterface.dismiss();
                                    }
                                }).show();
                    }
                    else{
                        Intent intent = new Intent(getActivity(), CheckInActivity.class);
                        String model = new Gson().toJson(lstDetPlanSem.get(position));
                        Log.e("ButtonClick", model);
                        intent.putExtra("model", model);
                        startActivityForResult(intent, INTENT_CHECKIN);
                    }

                }

                @Override
                public void DetalleClick(View view, int position) {

                }
            });
            rvwDetPlanSem.setAdapter(planeacionListAdapter);
        }
        else{
            Toast.makeText(getContext(), "No se obuvo información", Toast.LENGTH_LONG).show();
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
