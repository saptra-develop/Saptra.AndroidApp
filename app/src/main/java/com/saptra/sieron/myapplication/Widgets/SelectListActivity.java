package com.saptra.sieron.myapplication.Widgets;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import com.rey.material.widget.ProgressView;
import com.saptra.sieron.myapplication.Models.HttpListResponse;
import com.saptra.sieron.myapplication.Models.cPeriodos;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.Adapters.SelectListAdapter;
import com.saptra.sieron.myapplication.Utils.Globals;
import com.saptra.sieron.myapplication.Utils.Interfaces.RecyclerViewClickListener;
import com.saptra.sieron.myapplication.Utils.Interfaces.ServiceApi;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SelectListActivity extends AppCompatActivity {
    //Controls
    private RecyclerView rvwSelectList;
    private Button btnCancelar;
    private ProgressView pvLoading;
    private RelativeLayout rlCancelar;

    //Other
    private Retrofit mRestAdapter;
    private ServiceApi InfoApi;

    ArrayList<cPeriodos> lstPeriodo;
    SelectListAdapter adapterPeriodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_list);

        //android O fix bug orientation
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        //Service
        /**********************************************/
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(ServiceApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InfoApi = mRestAdapter.create(ServiceApi.class);

        rvwSelectList = (RecyclerView) findViewById(R.id.rvwSelectList);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        pvLoading = (ProgressView) findViewById(R.id.pvProgress);
        rlCancelar = (RelativeLayout) findViewById(R.id.rlCancelar);
        ControlBehavior(false);

        lstPeriodo = new ArrayList<cPeriodos>();

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ObenerPeriodosHttp();
    }

    private void ObenerPeriodosHttp(){
        if(!Globals.getInstance().isOnline(getApplicationContext())){
            Toast.makeText(getApplicationContext(),
                    "Sin acceso a internet",
                    Toast.LENGTH_LONG).show();
            finish();
        }
        try{
            Call<HttpListResponse<cPeriodos>> periodos = InfoApi.GetPeriodo();
            periodos.enqueue(new Callback<HttpListResponse<cPeriodos>>() {
                @Override
                public void onResponse(Call<HttpListResponse<cPeriodos>> call, Response<HttpListResponse<cPeriodos>> response) {
                    if(response.isSuccessful()){
                        //Si respuesta correcta, obener datos
                        String data = new Gson().toJson(response.body().getDatos());
                        Log.e("DATA", data);
                        if(response.body().getDatos().size() > 0){
                            ControlBehavior(true);
                            btnCancelar.setVisibility(View.VISIBLE);
                            //Limpiar tabla
                            lstPeriodo = (ArrayList<cPeriodos>) response.body().getDatos();
                            if(lstPeriodo.size() > 0) {
                                rvwSelectList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                adapterPeriodo = new SelectListAdapter(getApplicationContext(), lstPeriodo);
                                adapterPeriodo.setRecyclerViewItemClickListener(new RecyclerViewClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {
                                        Intent returnIntent = new Intent();
                                        int PeriodoId = lstPeriodo.get(position).getPeriodoId();
                                        String Periodo = lstPeriodo.get(position).getDecripcionPeriodo();
                                        returnIntent.putExtra("PeriodoId", PeriodoId);
                                        returnIntent.putExtra("Periodo", Periodo);
                                        returnIntent.putExtra("FechaInicio", lstPeriodo.get(position).getFechaInicio());
                                        returnIntent.putExtra("FechaFin", lstPeriodo.get(position).getFechaFin());
                                        setResult(RESULT_OK, returnIntent);
                                        ((SelectListActivity) view.getContext()).finish();
                                    }
                                });
                                rvwSelectList.setAdapter(adapterPeriodo);
                            }
                        }
                        else{
                            Log.e("PER-onResponse", "Sin información");
                            ControlBehavior(true);
                            Toast.makeText(getApplicationContext(),
                                    "No se obtuvo información",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                    else{
                        Log.e("PER-onResponse", "Sin información");
                        ControlBehavior(true);
                        Toast.makeText(getApplicationContext(),
                                "No se obtuvo información",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                @Override
                public void onFailure(Call<HttpListResponse<cPeriodos>> call, Throwable t) {
                    Log.e("CTL-Failure", "error: "+t.getMessage());
                    ControlBehavior(true);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            Log.e("getCatalogos", ex.getMessage());
            ControlBehavior(true);
        }
    }

    private void ControlBehavior(boolean hidden){
        pvLoading.setVisibility(hidden ? View.GONE : View.VISIBLE);
        rlCancelar.setVisibility(!hidden ? View.GONE : View.VISIBLE);
    }
}
