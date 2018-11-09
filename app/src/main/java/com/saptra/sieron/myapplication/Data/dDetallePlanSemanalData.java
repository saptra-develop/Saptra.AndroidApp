package com.saptra.sieron.myapplication.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saptra.sieron.myapplication.Models.dDetallePlanSemanal;

import java.util.ArrayList;
import java.util.List;

public class dDetallePlanSemanalData {
    public static dDetallePlanSemanalData instance;
    Context context;
    DatabaseHelper dbh;

    public dDetallePlanSemanalData(Context context){
        this.context = context;
        dbh = new DatabaseHelper(context);
    }

    //Get Instance
    public static synchronized dDetallePlanSemanalData getInstance(Context context) {
        if (instance == null) {
            instance = new dDetallePlanSemanalData(context.getApplicationContext());
        }
        return instance;
    }

    /***************************************************************************
     * CREATE_DETALLE_PLAN SEMANAL
     ***************************************************************************/
    public long createDetallePlanSemanal(dDetallePlanSemanal dp) {
        long IdPlanSemanal= 0;
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbh.DPS_DETALLE_PLAN_ID, dp.getDetallePlanId());
        values.put(dbh.DPS_PLAN_SEMANAL_ID, dp.getPlanSemanal().getPlanSemanalId());
        values.put(dbh.DPS_FECHA_CREACION, dp.getFechaCreacion());
        values.put(dbh.DPS_USUARIO_CREACION_ID, dp.getUsuarioCreacionId());
        values.put(dbh.DPS_ESTATUS_ID, dp.getEstatusId());
        values.put(dbh.DPS_TIPO_ACTIVIDAD_ID, dp.getTipoActividades().getTipoActividadId());
        values.put(dbh.DPS_FECHA_ACTIVIDAD, dp.getFechaActividad());
        values.put(dbh.DPS_HORA_ACTIVIDAD, dp.getHoraActividad());
        values.put(dbh.DPS_CANTIDAD_CHECKIN, dp.getCantidadCheckIn());
        values.put(dbh.DPS_DESCRIPCION_ACTIVIDAD, dp.getDescripcionActividad());
        values.put(dbh.DPS_LUGAR_ACTIVIDAD, dp.getLugarActividad());
        values.put(dbh.DPS_COMENTARIOS_NO_VALIDACION, dp.getComentariosNoValidacion());
        values.put(dbh.DPS_COMENTARIOS_RECHAZO, dp.getComentariosRechazo());
        values.put(dbh.DPS_ACTIVIDAD_RECHAZADA, dp.getActividadRechazada());


        // insert row
        IdPlanSemanal = db.insert(dbh.TBL_DETALLE_PLAN_SEMANAL, null, values);
        db.close();
        Log.e("DBH-createDetPlanSem", "Id:"+IdPlanSemanal);
        return IdPlanSemanal;
    }

    /***************************************************************************
     * DELETE DETALLE PLAN SEMANAL
     ***************************************************************************/
    public void deleteDetallePlanSemanal() {
        try {
            SQLiteDatabase db = dbh.getReadableDatabase();
            int d_rows = db.delete(dbh.TBL_DETALLE_PLAN_SEMANAL, "", null);
            Log.e("deletePlanSemanal", "deleted_rows: " + d_rows);
        }
        catch (Exception ex){
            Log.e("deleteDetPlanSem", "Error: "+ex.getLocalizedMessage());
            throw  ex;
        }
    }

    /***************************************************************************
     * GET LISTA DETALLE PLAN SEMANAL
     ***************************************************************************/
    public List<dDetallePlanSemanal> getDetPlanSemanal(int PeriodoId){
        List<dDetallePlanSemanal> lstDetPlanSem = new ArrayList<dDetallePlanSemanal>();
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor c = db.rawQuery(
                " SELECT A.*, B."+dbh.PLS_PLAN_SEMANAL_ID+", B."+dbh.PLS_DESCRIPCION_PLANEACION+
                        ", C."+dbh.PER_PERIODO_ID+", C."+dbh.PER_DESCRIPCION_PERIODO+
                        ", D."+dbh.TIA_TIPO_ACTIVIDAD_ID+", D."+dbh.TIA_NOMBRE_ACTIVIDAD+
                        ", D."+dbh.TIA_DESCRIPCION_ACTIVIDAD+" AS TIPO_ACTIVIDAD, D."+dbh.TIA_REQUIERE_CHECKIN+
                        ", D."+dbh.TIA_ACTIVIDAD_ESPECIAL+
                " FROM "+dbh.TBL_DETALLE_PLAN_SEMANAL +" A "+
                " JOIN "+ dbh.TBL_PLAN_SEMANAL+" B on A."+dbh.DPS_PLAN_SEMANAL_ID+" = B."+dbh.PLS_PLAN_SEMANAL_ID+
                " JOIN "+dbh.TBL_PERIODOS+" C on B."+dbh.PLS_PERIODO_ID+" = C."+dbh.PER_PERIODO_ID+
                " JOIN "+dbh.TBL_TIPO_ACTIVIDADES+" D on A."+ dbh.DPS_TIPO_ACTIVIDAD_ID+ " = D."+dbh.TIA_TIPO_ACTIVIDAD_ID+
                " WHERE C."+dbh.PER_PERIODO_ID+" = "+PeriodoId
                , null);
        Log.e("getDetPlanSemanal", ""+c.getCount());
        try{
            if(c.moveToFirst()){
                do{
                    dDetallePlanSemanal obj = new dDetallePlanSemanal();
                    obj.setDetallePlanId(c.getInt(c.getColumnIndex(dbh.DPS_DETALLE_PLAN_ID)));
                    obj.getPlanSemanal().setPlanSemanalId(c.getInt(c.getColumnIndex(dbh.PLS_PLAN_SEMANAL_ID)));
                    obj.getPlanSemanal().setDescripcionPlaneacion(c.getString(c.getColumnIndex(dbh.PLS_DESCRIPCION_PLANEACION)));
                    obj.setDescripcionActividad(c.getString(c.getColumnIndex(dbh.DPS_DESCRIPCION_ACTIVIDAD)));
                    obj.setLugarActividad(c.getString(c.getColumnIndex(dbh.DPS_LUGAR_ACTIVIDAD)));
                    obj.setFechaActividad(c.getString(c.getColumnIndex(dbh.DPS_FECHA_ACTIVIDAD)));
                    obj.setHoraActividad(c.getString(c.getColumnIndex(dbh.DPS_HORA_ACTIVIDAD)));
                    obj.setCantidadCheckIn(c.getInt(c.getColumnIndex(dbh.DPS_CANTIDAD_CHECKIN)));
                    obj.getPlanSemanal().getPeriodos().setPeriodoId(c.getInt(c.getColumnIndex(dbh.PER_PERIODO_ID)));
                    obj.getPlanSemanal().getPeriodos().setDecripcionPeriodo(c.getString(c.getColumnIndex(dbh.PER_DESCRIPCION_PERIODO)));
                    obj.getTipoActividades().setTipoActividadId(c.getInt(c.getColumnIndex(dbh.TIA_TIPO_ACTIVIDAD_ID)));
                    obj.getTipoActividades().setNombreActividad(c.getString(c.getColumnIndex("TIPO_ACTIVIDAD")));
                    obj.getTipoActividades().setDescripcionActividad(c.getString(c.getColumnIndex(dbh.TIA_DESCRIPCION_ACTIVIDAD)));
                    obj.getTipoActividades().setRequiereCheckIn(
                            c.getInt(c.getColumnIndex(dbh.TIA_REQUIERE_CHECKIN)) == 1 ? true : false);
                    obj.getTipoActividades().setActividadEspecial(
                            c.getInt(c.getColumnIndex(dbh.TIA_ACTIVIDAD_ESPECIAL)) == 1 ? true : false);
                    lstDetPlanSem.add(obj);
                }
                while(c.moveToNext());
            }
        }
        catch (Exception ex){
            Log.d("getDetPlanSemanal", "Error mientras se intentaba obtener informacion desde la bd");
        }
        finally {
            if(c != null && c.isClosed()){
                c.close();
            }
        }
        return lstDetPlanSem;
    }
}
