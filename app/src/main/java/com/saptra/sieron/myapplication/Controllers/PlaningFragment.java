package com.saptra.sieron.myapplication.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.saptra.sieron.myapplication.Data.dDetallePlanSemanalData;
import com.saptra.sieron.myapplication.Models.dDetallePlanSemanal;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.Adapters.PlaneacionListAdapter;
import com.saptra.sieron.myapplication.Utils.Interfaces.PlaneacionViewListener;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class PlaningFragment extends Fragment {

    RecyclerView rvwDetPlanSem;

    ArrayList<dDetallePlanSemanal> lstDetPlanSem;
    PlaneacionListAdapter planeacionListAdapter;

    private static final int INTENT_CHECKIN = 112;

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
                public void ButtonClick(View view, int position) {
                    Intent intent = new Intent(getActivity(), CheckInActivity.class);
                    String model = new Gson().toJson(lstDetPlanSem.get(position));
                    Log.e("ButtonClick", model);
                    intent.putExtra("model", model);
                    startActivityForResult(intent, INTENT_CHECKIN);
                }

                @Override
                public void DetalleClick(View view, int position) {

                }
            });
            rvwDetPlanSem.setAdapter(planeacionListAdapter);
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
                                    .getInstance(getContext()).getDetPlanSemanal(0);
                    planeacionListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
