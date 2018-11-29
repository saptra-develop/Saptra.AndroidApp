package com.saptra.sieron.myapplication.Controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.gson.Gson;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.ProgressView;
import com.saptra.sieron.myapplication.Data.cPeriodosData;
import com.saptra.sieron.myapplication.Data.cTipoActividadesData;
import com.saptra.sieron.myapplication.Data.dDetallePlanSemanalData;
import com.saptra.sieron.myapplication.Data.mCheckInData;
import com.saptra.sieron.myapplication.Data.mLecturaCertificadosData;
import com.saptra.sieron.myapplication.Data.mPlanSemanalData;
import com.saptra.sieron.myapplication.Models.HttpListResponse;
import com.saptra.sieron.myapplication.Models.cPeriodos;
import com.saptra.sieron.myapplication.Models.dDetallePlanSemanal;
import com.saptra.sieron.myapplication.Models.mCheckIn;
import com.saptra.sieron.myapplication.Models.mLecturaCertificados;
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

    //Controls
    private TextInputLayout tilPeriodo;
    private ImageButton btnDownload;
    private static final int KEY_SELECT_LIST = 111;
    private cPeriodos periodo;
    private Retrofit mRestAdapter;
    private ServiceApi InfoApi;
    private ProgressView pvProgress;
    private CoordinatorLayout clMensajes;

    //Others
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
        pvProgress = (ProgressView) view.findViewById(R.id.pvProgress);
        clMensajes = (CoordinatorLayout) view.findViewById(R.id.clMensajes);

        //Iniciar
        pvProgress.setVisibility(View.GONE);

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
                                pvProgress.setVisibility(View.VISIBLE);
                                HabilitarControles(false);
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
                    periodo.setFechaInicio(data.getStringExtra("FechaInicio"));
                    periodo.setFechaFin(data.getStringExtra("FechaFin"));
                    tilPeriodo.getEditText().setText(periodo.getDecripcionPeriodo());
                    if(periodo.getPeriodoId() > 0){
                        try {
                            cPeriodos localPeriodo = getLocalPeriodo();
                            if(periodo.getPeriodoId() != localPeriodo.getPeriodoId()){
                                cPeriodosData.getInstance(getContext()).deletePeriodo();
                                cPeriodosData.getInstance(getContext()).createPeriodo(periodo);
                                SaveLocalPeriodo(periodo);
                                //Borrar fotos del periodo anterior
                                Globals.getInstance().DropAllFile(getActivity());
                            }

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
                            pvProgress.setProgress(33);
                            ObtenerDetallePlanSemanal();
                        }
                        else{
                            HabilitarControles(true);
                            pvProgress.setVisibility(View.GONE);
                            Toast.makeText(getContext(),
                                    "Información no disponible",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        HabilitarControles(true);
                        pvProgress.setVisibility(View.GONE);
                        Toast.makeText(getContext(),
                                "Información plan_semanal no disponible",
                                Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<HttpListResponse<mPlanSemanal>> call, Throwable t) {
                    HabilitarControles(true);
                    pvProgress.setVisibility(View.GONE);
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
                            ObtenerCheckIns();
                        }
                        else{
                            HabilitarControles(true);
                            pvProgress.setVisibility(View.GONE);
                            Toast.makeText(getContext(),
                                    "Información no disponible",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        HabilitarControles(true);
                        pvProgress.setVisibility(View.GONE);
                        Toast.makeText(getContext(),
                                "Información detalle_plan no disponible",
                                Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<HttpListResponse<dDetallePlanSemanal>> call, Throwable t) {
                    HabilitarControles(true);
                    pvProgress.setVisibility(View.GONE);
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

    public void ObtenerCheckIns(){
        try{
            Call<HttpListResponse<mLecturaCertificados>> _checks =
                    InfoApi.GetCheckInsRealizados(periodo.getPeriodoId(), user.getUsuarioId());
            _checks.enqueue(new Callback<HttpListResponse<mLecturaCertificados>>() {
                @Override
                public void onResponse(Call<HttpListResponse<mLecturaCertificados>> call,
                                       Response<HttpListResponse<mLecturaCertificados>> response) {
                    if(response.isSuccessful()){

                        //Limpiar tabla para obtener los datos más actualizados
                        mCheckInData.getInstance(getActivity())
                                .deleteCheckIn();
                        mLecturaCertificadosData.getInstance(getActivity())
                                .deleteLecuraCertificado();

                        if(response.body().getDatos().size() > 0){

                            for(mLecturaCertificados certificado : response.body().getDatos()){
                                mCheckIn check = certificado.getCheckIn();
                                if(check != null){
                                    mCheckInData.getInstance(getActivity())
                                            .createCheckIn(check);
                                }
                                if(certificado.getLecturaCertificadoId() > 0) {
                                    mLecturaCertificadosData.getInstance(getActivity())
                                            .createLecturaCerificado(certificado);
                                }
                            }
                            HabilitarControles(true);
                            pvProgress.setVisibility(View.GONE);
                            Snackbar.make(clMensajes, "La descarga finalizó con éxito."
                                    , Snackbar.LENGTH_INDEFINITE)
                                    .setAction("ACEPTAR", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                        }
                                    }).show();
                        }
                        else{
                            HabilitarControles(true);
                            pvProgress.setVisibility(View.GONE);
                            Snackbar.make(clMensajes, "La descarga finalizó con éxito."
                                    , Snackbar.LENGTH_INDEFINITE)
                                    .setAction("ACEPTAR", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                        }
                                    }).show();
                        }
                    }
                    else {
                        HabilitarControles(true);
                        pvProgress.setVisibility(View.GONE);
                        Toast.makeText(getContext(),
                                "Información checkins no disponible",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<HttpListResponse<mLecturaCertificados>> call, Throwable t) {
                    HabilitarControles(true);
                    pvProgress.setVisibility(View.GONE);
                    Log.e("ObtenerCheckIns", "Error: " + t.getMessage());
                }
            });
        }
        catch (Exception ex){
            Log.e("ObtenerCheckIns", "Error: "+ex.getLocalizedMessage());
        }
    }

    //Validar si ya se ha iniciado sesion, buscar flag en SharedPreferences
    private mUsuarios getLoggedUser(){
        mUsuarios user = new mUsuarios();
        String sharedPreferences = getResources().getString(R.string.SATRA_PREFERENCES);
        boolean logged;
        SharedPreferences settings = getActivity()
                .getSharedPreferences(sharedPreferences, getActivity().MODE_PRIVATE);
        user.setLoggedUsuario(settings.getBoolean(getResources().getString(R.string.sp_Logged), false));
        user.setUsuarioId(
                settings.getInt(getResources().getString(R.string.sp_UsuarioId),0)
        );
        return user;
    }

    public void HabilitarControles(boolean accion){
        tilPeriodo.setEnabled(accion);
        tilPeriodo.setClickable(accion);
        btnDownload.setEnabled(accion);
        btnDownload.setClickable(accion);
    }

    private void SaveLocalPeriodo(cPeriodos periodo){
        String sharedPref = getString(R.string.SATRA_PREFERENCES);
        SharedPreferences settings = getActivity().getSharedPreferences(sharedPref,
                getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(getString(R.string.sp_PeriodoId), periodo.getPeriodoId());
        editor.putString("FechaInicio", periodo.getFechaInicio());
        editor.putString("FechaFin", periodo.getFechaFin());
        editor.commit();
    }

    //Obtener Periodo de SharedPreferences
    private cPeriodos getLocalPeriodo(){
        cPeriodos per = new cPeriodos();
        String sharedPreferences = getResources().getString(R.string.SATRA_PREFERENCES);
        SharedPreferences settings = getActivity()
                .getSharedPreferences(sharedPreferences, getActivity().MODE_PRIVATE);
        per.setPeriodoId(settings.getInt(getResources().getString(R.string.sp_PeriodoId), 0));
        per.setFechaInicio(settings.getString("FechaInicio", ""));
        per.setFechaFin(settings.getString("FechaFin", ""));
        return per;
    }
}
