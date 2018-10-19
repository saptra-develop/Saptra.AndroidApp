package com.saptra.sieron.myapplication.Controller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saptra.sieron.myapplication.Data.dDetallePlanSemanalData;
import com.saptra.sieron.myapplication.Models.dDetallePlanSemanal;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.Adapters.PlaneacionListAdapter;
import com.saptra.sieron.myapplication.Utils.Interfaces.PlaneacionViewListener;

import java.util.ArrayList;


public class PlaningFragment extends Fragment {

    RecyclerView rvwDetPlanSem;

    ArrayList<dDetallePlanSemanal> lstDetPlanSem;
    PlaneacionListAdapter planeacionListAdapter;

    public PlaningFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_planing, container, false);
        //Instancias
        //*********************************************
        lstDetPlanSem = new ArrayList<dDetallePlanSemanal>();
        rvwDetPlanSem =(RecyclerView) view.findViewById(R.id.rvwDetPlanSem);
        //*********************************************
        lstDetPlanSem = (ArrayList<dDetallePlanSemanal>)
                dDetallePlanSemanalData.getInstance(getContext()).getDetPlanSemanal(0);
        if(lstDetPlanSem.size() > 0) {
            rvwDetPlanSem.setLayoutManager(new LinearLayoutManager(getContext()));
            planeacionListAdapter = new PlaneacionListAdapter(getContext(), lstDetPlanSem);
            planeacionListAdapter.setPlaneacionViewListener(new PlaneacionViewListener() {
                @Override
                public void ButonClick(View view, int position) {

                }

                @Override
                public void DetalleClick(View view, int position) {

                }
            });
            rvwDetPlanSem.setAdapter(planeacionListAdapter);
        }
        return view;
    }
}
