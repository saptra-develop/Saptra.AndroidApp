package com.saptra.sieron.myapplication.Utils.Interfaces;

import com.saptra.sieron.myapplication.Models.HttpListResponse;
import com.saptra.sieron.myapplication.Models.HttpObjectResponse;
import com.saptra.sieron.myapplication.Models.cPeriodos;
import com.saptra.sieron.myapplication.Models.cTipoActividades;
import com.saptra.sieron.myapplication.Models.dDetallePlanSemanal;
import com.saptra.sieron.myapplication.Models.mPlanSemanal;
import com.saptra.sieron.myapplication.Models.mUsuarios;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ServiceApi {

    //WebApi
    //public static final String BASE_URL = "http://api.impulsomonterrey.com";

    //virtual device
    //public static final String BASE_URL = "http://192.168.226.2:45455/";

    //physical device
    public static final String BASE_URL = "http://localhost:57680/";

    //mUsuarios
    @POST("Saptra/Usuario/GetLogin")
    Call<HttpObjectResponse<mUsuarios>> GetLogin(@Body mUsuarios usuario);

    //Periodo
    @GET("Saptra/Periodo/GetPeriodo")
    Call<HttpListResponse<cPeriodos>> GetPeriodo();

    //Planeacion
    @GET("Saptra/Planeacion/GetPlaneacionSemanal")
    Call<HttpListResponse<mPlanSemanal>> GetPlaneacionSemanal(
            @Query("PeriodoId")int PeriodoId, @Query("UsuarioId") int UsuarioId);

    @GET("Saptra/Planeacion/GetDetallePlaneacionSemanal")
    Call<HttpListResponse<dDetallePlanSemanal>> GetDetallePlaneacionSemanal(
            @Query("PeriodoId")int PeriodoId,
            @Query("UsuarioId") int UsuarioId);

    //TipoActividad
    @GET("Saptra/TipoActividad/GetTipoActividades")
    Call<HttpListResponse<cTipoActividades>> GetTipoActividades();
}



