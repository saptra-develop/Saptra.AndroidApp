package com.saptra.sieron.myapplication.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saptra.sieron.myapplication.Models.cPeriodos;

public class cPeriodosData {
    public static cPeriodosData instance;
    Context context;
    DatabaseHelper dbh;

    public cPeriodosData(Context context){
        this.context = context;
        dbh = new DatabaseHelper(context);
    }

    //Get Instance
    public static synchronized cPeriodosData getInstance(Context context) {
        if (instance == null) {
            instance = new cPeriodosData(context.getApplicationContext());
        }
        return instance;
    }

    /***************************************************************************
     * CREATE_PERIODO
     ***************************************************************************/
    public long createPeriodo(cPeriodos pe) {
        long IdPeriodo= 0;
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbh.PER_PERIODO_ID, pe.getPeriodoId());
        values.put(dbh.PER_FECHA_CREACION, pe.getFechaCreacion());
        values.put(dbh.PER_USUARIO_CREACION_ID, pe.getUsuarioCreacionId());
        values.put(dbh.PER_ESTATUS_ID, pe.getEstatusId());
        values.put(dbh.PER_FECHA_INICIO, pe.getFechaInicio());
        values.put(dbh.PER_FECHA_FIN, pe.getFechaFin());
        values.put(dbh.PER_DESCRIPCION_PERIODO, pe.getDecripcionPeriodo());
        // insert row
        IdPeriodo = db.insert(dbh.TBL_PERIODOS, null, values);
        db.close();
        Log.e("DBH-createPeriodo", "Id:"+IdPeriodo);
        return IdPeriodo;
    }

    /***************************************************************************
     * DELETE TIPO_ACTIVIDAD
     ***************************************************************************/
    public void deletePeriodo() {
        try {
            SQLiteDatabase db = dbh.getReadableDatabase();
            int d_rows = db.delete(dbh.TBL_PERIODOS, "", null);
            Log.e("deletePeriodo", "deleted_rows: " + d_rows);
        }
        catch (Exception ex){
            Log.e("deletePeriodo", "Error: "+ex.getLocalizedMessage());
            throw  ex;
        }
    }

}
