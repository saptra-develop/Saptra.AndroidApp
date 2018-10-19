package com.saptra.sieron.myapplication.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saptra.sieron.myapplication.Models.cTipoFiguras;
import com.saptra.sieron.myapplication.Utils.Globals;

public class cTipoFigurasData {
    public static cTipoFigurasData instance;
    Context context;
    DatabaseHelper dbh;

    public cTipoFigurasData(Context context){
        this.context = context;
        dbh = new DatabaseHelper(context);
    }

    //Get Instance
    public static synchronized cTipoFigurasData getInstance(Context context) {
        if (instance == null) {
            instance = new cTipoFigurasData(context.getApplicationContext());
        }
        return instance;
    }

    /***************************************************************************
     * CREATE_TIPO_FIGURA
     ***************************************************************************/
    public long createTipoFigura(cTipoFiguras tf) {
        long IdTipoFigura= 0;
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbh.TIF_TIPO_FIGURA_ID, tf.getTipoFiguraId());
        values.put(dbh.TIF_FECHA_CREACION, tf.getFechaCreacion());
        values.put(dbh.TIF_USAURIO_CREACION_ID, tf.getUsuarioCreacionId());
        values.put(dbh.TIF_ESTATUS_ID, tf.getEstatusId());
        values.put(dbh.TIF_DESCRIPCION_TIPO_FIGURA, tf.getDescripcionTipoFigura());
        // insert row
        IdTipoFigura = db.insert(dbh.TBL_TIPO_FIGURAS, null, values);
        db.close();
        Log.e("DBH-createTipoFigura", "Id:"+IdTipoFigura);
        return IdTipoFigura;
    }

    /***************************************************************************
     * DELETE TIPO_FIGURA
     ***************************************************************************/
    public void deleteTipoFigura() {
        try {
            SQLiteDatabase db = dbh.getReadableDatabase();
            int d_rows = db.delete(dbh.TBL_TIPO_FIGURAS, "", null);
            Log.e("deleteTipoFigura", "deleted_rows: " + d_rows);
        }
        catch (Exception ex){
            Log.e("deleteTipoFigura", "Error: "+ex.getLocalizedMessage());
            throw  ex;
        }
    }
}
