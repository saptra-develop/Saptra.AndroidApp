package com.saptra.sieron.myapplication.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saptra.sieron.myapplication.Models.mPlanSemanal;

public class mPlanSemanalData {
    public static mPlanSemanalData instance;
    Context context;
    DatabaseHelper dbh;

    public mPlanSemanalData(Context context){
        this.context = context;
        dbh = new DatabaseHelper(context);
    }

    //Get Instance
    public static synchronized mPlanSemanalData getInstance(Context context) {
        if (instance == null) {
            instance = new mPlanSemanalData(context.getApplicationContext());
        }
        return instance;
    }

    /***************************************************************************
     * CREATE_PLAN SEMANAL
     ***************************************************************************/
    public long createPlanSemanal(mPlanSemanal ps) {
        long IdPlanSemanal= 0;
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbh.PLS_PLAN_SEMANAL_ID, ps.getPlanSemanalId());
        values.put(dbh.PLS_FECHA_CREACION, ps.getFechaCreacion());
        values.put(dbh.PLS_USUARIO_CREACION_ID, ps.getUsuarioCreacionId());
        values.put(dbh.PLS_ESTATUS_ID, ps.getEstatusId());
        values.put(dbh.PLS_DESCRIPCION_PLANEACION, ps.getDescripcionPlaneacion());
        values.put(dbh.PLS_PERIODO_ID, ps.getPeriodos().getPeriodoId());
        // insert row
        IdPlanSemanal = db.insert(dbh.TBL_PLAN_SEMANAL, null, values);
        db.close();
        Log.e("DBH-createPlanSemanal", "Id:"+IdPlanSemanal);
        return IdPlanSemanal;
    }

    /***************************************************************************
     * DELETE PLAN SEMANAL
     ***************************************************************************/
    public void deletePlanSemanal() {
        try {
            SQLiteDatabase db = dbh.getReadableDatabase();
            int d_rows = db.delete(dbh.TBL_PLAN_SEMANAL, "", null);
            Log.e("deletePlanSemanal", "deleted_rows: " + d_rows);
        }
        catch (Exception ex){
            Log.e("deletePlanSemanal", "Error: "+ex.getLocalizedMessage());
            throw  ex;
        }
    }
}
