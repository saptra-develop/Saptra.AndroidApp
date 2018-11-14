package com.saptra.sieron.myapplication.Widgets;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.saptra.sieron.myapplication.Data.mCheckInData;
import com.saptra.sieron.myapplication.Data.mLecturaCertificadosData;
import com.saptra.sieron.myapplication.Models.HttpListResponse;
import com.saptra.sieron.myapplication.Models.mCheckIn;
import com.saptra.sieron.myapplication.Models.mLecturaCertificados;
import com.saptra.sieron.myapplication.Utils.Globals;
import com.saptra.sieron.myapplication.Utils.Interfaces.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SynchronizerService extends Service  {

    private Retrofit mRestAdapter;
    private ServiceApi InfoApi;

    public SynchronizerService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(this, "Servicio creado", Toast.LENGTH_LONG).show();
        Log.e("onCreate", "Servicio creado");
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(ServiceApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InfoApi = mRestAdapter.create(ServiceApi.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("onCreate", "Servicio iniciado");
        if(!Globals.getInstance().isOnline(this)){
            Log.e("Service-onStart", "Sin conexión a inernet");
            return START_STICKY;
        }
        SincronizarCheckIns();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "Servicio destruido", Toast.LENGTH_LONG).show();
        Log.e("onCreate", "Servicio destruido");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void SincronizarCheckIns(){
        List<mLecturaCertificados> checks = mCheckInData.getInstance(getApplicationContext())
                .getCheckInsNoEnviados();
        if(checks.size() > 0){
            try{
                Call<HttpListResponse<mLecturaCertificados>> _checks = InfoApi.PostCheckIns(checks);
                _checks.enqueue(new Callback<HttpListResponse<mLecturaCertificados>>() {
                    @Override
                    public void onResponse(Call<HttpListResponse<mLecturaCertificados>> call,
                                           Response<HttpListResponse<mLecturaCertificados>> response) {
                        if(response.isSuccessful()){
                            if(response.body().getDatos().size() > 0){
                                for(mLecturaCertificados certificado : response.body().getDatos()){
                                    mCheckIn check = certificado.getCheckIn();
                                    if(check != null){
                                        mCheckInData.getInstance(getApplicationContext())
                                                .updateCheckIn(check);
                                    }
                                    if(certificado.getLecturaCertificadoId() > 0) {
                                        mLecturaCertificadosData.getInstance(getApplicationContext())
                                                .updateCertificado(certificado);
                                    }
                                }
                            }
                        }
                        else {
                            Log.e("PostCheckIns", "Sin información");
                        }
                    }

                    @Override
                    public void onFailure(Call<HttpListResponse<mLecturaCertificados>> call, Throwable t) {
                        Log.e("PostCheckIns", "Error: " + t.getMessage());
                    }
                });
            }
            catch (Exception ex){
                Log.e("SincronizarCheckIns", "Error: "+ex.getLocalizedMessage());
            }
        }

    }
}
