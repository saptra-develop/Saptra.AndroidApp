package com.saptra.sieron.myapplication.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saptra.sieron.myapplication.Models.cTipoActividades;

public class cTipoActividadesData {
    public static cTipoActividadesData instance;
    Context context;
    DatabaseHelper dbh;

    public cTipoActividadesData(Context context){
        this.context = context;
        dbh = new DatabaseHelper(context);
    }

    //Get Instance
    public static synchronized cTipoActividadesData getInstance(Context context) {
        if (instance == null) {
            instance = new cTipoActividadesData(context.getApplicationContext());
        }
        return instance;
    }

    /***************************************************************************
     * CREATE_TIPO_ACTIVIDAD
     ***************************************************************************/
    public long createTipoActividad(cTipoActividades ta) {
        long IdTipoActividad= 0;
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbh.TIA_TIPO_ACTIVIDAD_ID, ta.getTipoActividadId());
        values.put(dbh.TIA_FECHA_CREACION, ta.getFechaCreacion());
        values.put(dbh.TIA_USUARIO_CREACION_ID, ta.getUsuarioCreacion());
        values.put(dbh.TIA_ESTATUS_ID, ta.getEstatusId());
        values.put(dbh.TIA_NOMBRE_ACTIVIDAD, ta.getNombreActividad());
        values.put(dbh.TIA_DESCRIPCION_ACTIVIDAD, ta.getDescripcionActividad());
        values.put(dbh.TIA_REQUIERE_CHECKIN, ta.getRequiereCheckIn());
        values.put(dbh.TIA_ACTIVIDAD_ESPECIAL, ta.getActividadEspecial());
        values.put(dbh.TIA_MENSAJE, ta.getMensaje());
        // insert row
        IdTipoActividad = db.insert(dbh.TBL_TIPO_ACTIVIDADES, null, values);
        db.close();
        Log.e("DBH-createTipoActividad", "Id:"+IdTipoActividad);
        return IdTipoActividad;
    }

    /***************************************************************************
     * DELETE TIPO_ACTIVIDAD
     ***************************************************************************/
    public void deleteTipoActividad() {
        try {
            SQLiteDatabase db = dbh.getReadableDatabase();
            int d_rows = db.delete(dbh.TBL_TIPO_ACTIVIDADES, "", null);
            Log.e("deleteTipoActividad", "deleted_rows: " + d_rows);
        }
        catch (Exception ex){
            Log.e("deleteTipoActividad", "Error: "+ex.getLocalizedMessage());
            throw  ex;
        }
    }
}
