package com.saptra.sieron.myapplication.Controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rey.material.widget.ImageButton;
import com.saptra.sieron.myapplication.Data.cPeriodosData;
import com.saptra.sieron.myapplication.Data.cTipoActividadesData;
import com.saptra.sieron.myapplication.Data.dDetallePlanSemanalData;
import com.saptra.sieron.myapplication.Data.mPlanSemanalData;
import com.saptra.sieron.myapplication.Models.HttpListResponse;
import com.saptra.sieron.myapplication.Models.cPeriodos;
import com.saptra.sieron.myapplication.Models.cTipoActividades;
import com.saptra.sieron.myapplication.Models.dDetallePlanSemanal;
import com.saptra.sieron.myapplication.Models.mPlanSemanal;
import com.saptra.sieron.myapplication.Models.mUsuarios;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.Globals;
import com.saptra.sieron.myapplication.Utils.Interfaces.ServiceApi;
import com.saptra.sieron.myapplication.Widgets.SelectListActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class DownloadFragment extends Fragment {

    TextInputLayout tilPeriodo;
    ImageButton btnDownload;
    private static final int KEY_SELECT_LIST = 111;
    private cPeriodos periodo;
    private Retrofit mRestAdapter;
    private ServiceApi InfoApi;

    private mUsuarios user;
    private ArrayList<mPlanSemanal> _pSemanal;
    private ArrayList<dDetallePlanSemanal> _dSemanal;

    public DownloadFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        //Instancias
        //*********************************************
        user = getLoggedUser();
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(ServiceApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InfoApi = mRestAdapter.create(ServiceApi.class);
        periodo = new cPeriodos();
        _pSemanal = new ArrayList<mPlanSemanal>();
        _dSemanal = new ArrayList<dDetallePlanSemanal>();
        tilPeriodo = (TextInputLayout) view.findViewById(R.id.tilPeriodo);
        btnDownload = (ImageButton) view.findViewById(R.id.btnDownload);

        //*********************************************

        tilPeriodo.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tilPeriodoClick", "Click");
                Intent intent = new Intent(getActivity(), SelectListActivity.class);
                startActivityForResult(intent, KEY_SELECT_LIST);
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(periodo.getPeriodoId() == 0){
                    Toast.makeText(getContext(),
                            "Seleccione periodo",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if(!Globals.getInstance().isOnline(getContext())){
                    Toast.makeText(getContext(),
                            "Sin acceso a internet",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                new AlertDialog.Builder(getContext())
                        .setTitle("Descargar")
                        .setMessage("Descargar información?")
                        .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ObtenerPlanSemanal();
                            }
                        })
                        .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode) {
                case KEY_SELECT_LIST:
                    periodo.setPeriodoId(data.getIntExtra("PeriodoId", 0));
                    periodo.setDecripcionPeriodo(data.getStringExtra("Periodo"));
                    tilPeriodo.getEditText().setText(periodo.getDecripcionPeriodo());
                    if(periodo.getPeriodoId() > 0){
                        try {
                            cPeriodosData.getInstance(getContext()).deletePeriodo();
                            cPeriodosData.getInstance(getContext()).createPeriodo(periodo);
                        }
                        catch (Exception ex){
                            Log.e("onActResult", "Error al insertar periodo:"+ex.getMessage());
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void ObtenerPlanSemanal(){
        if(!Globals.getInstance().isOnline(getContext())){
            Toast.makeText(getContext(),
                    "Sin acceso a internet",
                    Toast.LENGTH_LONG).show();
            return;
        }
        try{
            Call<HttpListResponse<mPlanSemanal>> psemanal
                    = InfoApi.GetPlaneacionSemanal(periodo.getPeriodoId(), user.getUsuarioId());
            psemanal.enqueue(new Callback<HttpListResponse<mPlanSemanal>>() {
                @Override
                public void onResponse(Call<HttpListResponse<mPlanSemanal>> call, Response<HttpListResponse<mPlanSemanal>> response) {
                    if(response.isSuccessful()){
                        //Si respuesta correcta, obener datos
                        String data = new Gson().toJson(response.body().getDatos());
                        Log.e("DATA", data);
                        if(response.body().getDatos().size() > 0){
                            mPlanSemanalData.getInstance(getContext()).deletePlanSemanal();
                            for(mPlanSemanal obj : response.body().getDatos()){
                                mPlanSemanalData.getInstance(getContext())
                                        .createPlanSemanal(obj);
                            }
                            ObtenerDetallePlanSemanal();
                        }
                        else{
                            Log.e("IF_ObtenerPlanSemanal", "Sin información");
                            Toast.makeText(getContext(),
                                    "Información no disponible",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<HttpListResponse<mPlanSemanal>> call, Throwable t) {
                    Log.e("@_ObtenerPlanSemanal", "error: "+t.getMessage());
                    Toast.makeText(getContext(),
                            "Error al intentar descargar",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            Log.e("ObtenerPlanSemanal", ex.getMessage());
        }
    }

    private void ObtenerDetallePlanSemanal(){
        if(!Globals.getInstance().isOnline(getContext())){
            Toast.makeText(getContext(),
                    "Sin acceso a internet",
                    Toast.LENGTH_LONG).show();
            return;
        }
        try{
            Call<HttpListResponse<dDetallePlanSemanal>> dsemanal
                    = InfoApi.GetDetallePlaneacionSemanal(periodo.getPeriodoId(),user.getUsuarioId());
            dsemanal.enqueue(new Callback<HttpListResponse<dDetallePlanSemanal>>() {
                @Override
                public void onResponse(Call<HttpListResponse<dDetallePlanSemanal>> call, Response<HttpListResponse<dDetallePlanSemanal>> response) {
                    if(response.isSuccessful()){
                        //Si respuesta correcta, obener datos
                        String data = new Gson().toJson(response.body().getDatos());
                        Log.e("DATA", data);
                        if(response.body().getDatos().size() > 0){
                            cTipoActividadesData.getInstance(getContext()).deleteTipoActividad();
                            dDetallePlanSemanalData.getInstance(getContext()).deleteDetallePlanSemanal();
                            for(dDetallePlanSemanal obj : response.body().getDatos()){
                                //Insertar detalle actividad
                                dDetallePlanSemanalData.getInstance(getContext())
                                        .createDetallePlanSemanal(obj);
                                //Insertar Tipo Actividad del detalle
                                cTipoActividadesData.getInstance(getContext())
                                        .createTipoActividad(obj.getTipoActividades());
                            }
                            Toast.makeText(getContext(),
                                    "La descarga finalizó con éxito",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{
                            Log.e("IF_ObtenerDetPlanSem", "Sin información");
                            Toast.makeText(getContext(),
                                    "Información no disponible",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<HttpListResponse<dDetallePlanSemanal>> call, Throwable t) {
                    Log.e("@_ObtenerDetPlanSem", "error: "+t.getMessage());
                    Toast.makeText(getContext(),
                            "Error al intentar descargar, vuelve a intentar.",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            Log.e("ObtenerDetPlanSem", "Error:"+ex.getMessage());
        }
    }

    //Validar si ya se ha iniciado sesion, buscar flag en SharedPreferences
    private mUsuarios getLoggedUser(){
        mUsuarios user = new mUsuarios();
        String sharedPreferences = getResources().getString(R.string.SATRA_PREFERENCES);
        boolean logged;
        SharedPreferences settings = getActivity()
                .getSharedPreferences(sharedPreferences, getActivity().MODE_PRIVATE);
        logged = settings.getBoolean(getResources().getString(R.string.sp_Logged), false);
        user.setUsuarioId(
                settings.getInt(getResources().getString(R.string.sp_UsuarioId),0)
        );
        return user;
    }
}
