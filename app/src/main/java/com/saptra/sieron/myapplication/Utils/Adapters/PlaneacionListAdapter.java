package com.saptra.sieron.myapplication.Utils.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.instantapps.ActivityCompat;
import com.saptra.sieron.myapplication.Data.mCheckInData;
import com.saptra.sieron.myapplication.Data.mLecturaCertificadosData;
import com.saptra.sieron.myapplication.Models.dDetallePlanSemanal;
import com.saptra.sieron.myapplication.Models.mCheckIn;
import com.saptra.sieron.myapplication.Models.mLecturaCertificados;
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
        int DetallePlanId = lstDetPlanSem.get(position).getDetallePlanId();
        Long Checks = new Long(mCheckInData.getInstance(c).getCheckInRealizados(DetallePlanId)+"");
        Long TotalChecks = new Long(lstDetPlanSem.get(position).getCantidadCheckIn()+"");
        boolean bloquearChecking = false;
        boolean IsEnviado = mCheckInData.getInstance(c).IsCheckingEnviado(DetallePlanId);
        Long CertificadosEnviados = new Long(mLecturaCertificadosData.getInstance(c)
                .getCertificadosEnviadosCount(DetallePlanId));
        long certificados = mLecturaCertificadosData.getInstance(c)
                .getCertificadosCount(DetallePlanId);

        holder.txvPeriodo.setText(lstDetPlanSem.get(position).getPlanSemanal().getPeriodos().getDecripcionPeriodo());
        holder.tilActividad.getEditText().setText(lstDetPlanSem.get(position).getTipoActividades().getNombreActividad());
        holder.tilDescripcion.getEditText().setText(lstDetPlanSem.get(position).getDescripcionActividad());
        holder.tilFecha.getEditText().setText(lstDetPlanSem.get(position).getFechaActividad().substring(0,10));
        holder.tilHora.getEditText().setText(lstDetPlanSem.get(position).getHoraActividad().substring(0,5));
        holder.tilLugar.getEditText().setText(lstDetPlanSem.get(position).getLugarActividad());
        holder.tilCheckIn.getEditText().setText(Checks+" / "+TotalChecks);

        //Validar si la actividad contiene sus respectivos checkings (solo especiales)
        if(lstDetPlanSem.get(position).getTipoActividades().getActividadEspecial()) {
            bloquearChecking = Checks >= TotalChecks;
        }
        //Para actividades que no sean especiales
        else {
            bloquearChecking = mCheckInData.getInstance(c).getCheckingRealizado(DetallePlanId);
        }

        //Validar si ya tiene check-in
        if(bloquearChecking){
            holder.btnCheck.setBackgroundColor(c.getResources().getColor(R.color.magenta_gto));
            holder.btnCheck.setIcon(c.getResources().getDrawable(R.drawable.ic_check_white), false);
        }
        else{
            holder.btnCheck.setBackgroundColor(c.getResources().getColor(R.color.colorAccent));
            holder.btnCheck.setIcon(c.getResources().getDrawable(R.drawable.ic_arrow_forward), false);
        }

        /*---------------------------------------------------------
                Mostrar mensaje para notificar si checking
                de actividad ya fue enviado
         ---------------------------------------------------------*/

        holder.txvEstatusEnvio.setText("Actividad sincronizada: "+
                (IsEnviado ? "Si" : "No"));
        //Cambiar color de estatus de envio de checking
        holder.llyEstatusEnvio.setBackgroundResource(
                IsEnviado ? R.color.md_green_200 : R.color.md_grey_200);

        //Si actividad es especial y contiene certificados leidos, mostrar layout
        if(lstDetPlanSem.get(position).getTipoActividades().getActividadEspecial() &&
                lstDetPlanSem.get(position).getCantidadCheckIn() > 0){
            if(certificados > 0)
                //Mostrar layout para leer certificado
                holder.llyCertificados.setVisibility(View.VISIBLE);

            //Mostrar cantidad de certificados enviados
            holder.txvEstatusEnvio.setText("Actividades sincronizadas: "+CertificadosEnviados+ "/" +TotalChecks);
            //Cambiar color de estatus de envio de checking
            holder.llyEstatusEnvio.setBackgroundResource(
                    ((CertificadosEnviados.equals(TotalChecks))) ? R.color.md_green_200 :
                            (CertificadosEnviados > 0 && CertificadosEnviados < TotalChecks ? R.color.md_yellow_200 :
                                    R.color.md_grey_200));
        }
        else
            //Ocultar layout para leer certificado
            holder.llyCertificados.setVisibility(View.GONE);
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
