package com.saptra.sieron.myapplication.Utils.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.saptra.sieron.myapplication.Models.dDetallePlanSemanal;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.Interfaces.PlaneacionViewListener;
import com.saptra.sieron.myapplication.Utils.ViewHolders.PlaneacionListViewHolder;

import java.util.List;

public class PlaneacionListAdapter extends RecyclerView.Adapter<PlaneacionListViewHolder> {
    private Context c;
    private List<dDetallePlanSemanal> lstDetPlanSem;

    private PlaneacionViewListener planeacionViewListener;

    public void setPlaneacionViewListener(PlaneacionViewListener planeacionViewListener) {
        this.planeacionViewListener = planeacionViewListener;
    }


    public PlaneacionListAdapter(Context c, List<dDetallePlanSemanal> lstDetPlanSem){
        this.c = c;
        this.lstDetPlanSem = lstDetPlanSem;
    }

    @Override
    public PlaneacionListViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Log.d("TRANCER", "en onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_planeacion, parent, false);
        return new PlaneacionListViewHolder(v, planeacionViewListener);
    }

    @Override
    public void onBindViewHolder(final PlaneacionListViewHolder holder, final int position){
        Log.d("TRANCER", "en onBindViewHolder**");
        //BIND DATA
        holder.tilActividad.getEditText().setText(lstDetPlanSem.get(position).getDescripcionActividad()+"TEST");
        holder.tilDescripcion.getEditText().setText(lstDetPlanSem.get(position).getDescripcionActividad());
        holder.tilFecha.getEditText().setText(lstDetPlanSem.get(position).getFechaAcividad());
        holder.tilHora.getEditText().setText(lstDetPlanSem.get(position).getHoraActividad());
    }

    @Override
    public int getItemCount(){
        Log.d("TRANCER", "en getItemCount");
        if(lstDetPlanSem != null)
            return lstDetPlanSem.size();
        else
            return 0;
    }
}
