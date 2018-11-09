package com.saptra.sieron.myapplication.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saptra.sieron.myapplication.Models.mLecturaCertificados;
import com.saptra.sieron.myapplication.Utils.Globals;

import java.util.ArrayList;
import java.util.List;

public class mLecturaCertificadosData {
    public static mLecturaCertificadosData instance;
    Context context;
    DatabaseHelper dbh;

    public mLecturaCertificadosData(Context context){
        this.context = context;
        dbh = new DatabaseHelper(context);
    }

    //Get Instance
    public static synchronized mLecturaCertificadosData getInstance(Context context) {
        if (instance == null) {
            instance = new mLecturaCertificadosData(context.getApplicationContext());
        }
        return instance;
    }

    /***************************************************************************
     * CREATE
     ***************************************************************************/
    public long createLecturaCerificado(mLecturaCertificados lc) {
        long IdLecturaCertificado= 0;
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbh.LCR_LECTURA_CERTIFICADO_ID, lc.getLecturaCertificadoId());
        values.put(dbh.LCR_FECHA_CREACION, lc.getFechaCreacion());
        values.put(dbh.LCR_USUARIO_CREACION_ID, lc.getUsuarioCreacionId());
        values.put(dbh.LCR_FOLIO_CERTIFICADO, lc.getFolioCertificado());
        values.put(dbh.LCR_ESTATUS_ID, lc.getEstatusId());
        values.put(dbh.LCR_CHECKIN_ID, lc.getCheckIn().getCheckInId());
        // insert row
        IdLecturaCertificado = db.insert(dbh.TBL_LECTURA_CERTIFICADOS, null, values);
        db.close();
        Log.e("DBH-createLecCer", "Id:"+IdLecturaCertificado);
        return IdLecturaCertificado;
    }

    /***************************************************************************
     * GET CERTIFICADOS POR ACTIVIDAD
     ***************************************************************************/
    public ArrayList<mLecturaCertificados> getCertificadosPorActividad (int DetallePlanId){
        ArrayList<mLecturaCertificados> lstCertificado = new ArrayList<mLecturaCertificados>();
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor c = db.rawQuery(
                " SELECT A.*"+dbh.CHK_CHECKIN_ID+ //, "+dbh.CHK_IMAGE_DATA+", "+dbh.CHK_INCIDENCIAS+", "+dbh.CHK_FECHA_CREACION+
                        " FROM "+dbh.TBL_LECTURA_CERTIFICADOS +" A "+
                        " JOIN "+ dbh.TBL_CHECKIN +" B on A."+dbh.LCR_CHECKIN_ID+" = B."+dbh.CHK_CHECKIN_ID+
                        " JOIN "+dbh.TBL_DETALLE_PLAN_SEMANAL+" C on C"+dbh.DPS_DETALLE_PLAN_ID +" = B."+dbh.CHK_DETALLE_PLAN_ID+
                        " WHERE A."+dbh.DPS_DETALLE_PLAN_ID+" = "+DetallePlanId
                , null);
        Log.e("getCheckInsDetallePlan", ""+c.getCount());
        try{
            if(c.moveToFirst()){
                do {
                    mLecturaCertificados obj = new mLecturaCertificados();
                    obj.setLecturaCertificadoId(c.getInt(c.getColumnIndex(dbh.LCR_LECTURA_CERTIFICADO_ID)));
                    obj.setFolioCertificado(c.getString(c.getColumnIndex(dbh.LCR_FOLIO_CERTIFICADO)));
                    obj.getCheckIn().setCheckInId(c.getInt(c.getColumnIndex(dbh.CHK_CHECKIN_ID)));
                    lstCertificado.add(obj);
                }
                while(c.moveToNext());
            }
        }
        catch (Exception ex){
            Log.d("getCertPorActividad", "Error mientras se intentaba obtener informacion desde la bd");
        }
        finally {
            if(c != null && c.isClosed()){
                c.close();
            }
        }
        return lstCertificado;
    }

    /***************************************************************************
     * DELETE ALL
     ***************************************************************************/
    public void deleteLecuraCertificado() {
        try {
            SQLiteDatabase db = dbh.getReadableDatabase();
            int d_rows = db.delete(dbh.TBL_LECTURA_CERTIFICADOS, "", null);
            Log.e("deleteLecCer", "deleted_rows: " + d_rows);
        }
        catch (Exception ex){
            Log.e("deleteLecCer", "Error: "+ex.getLocalizedMessage());
            throw  ex;
        }
    }

    /***************************************************************************
     * DELETE ROW
     ***************************************************************************/
    public int deleteLectCerRow(int Certificado){
        int remove = 0;
        try{
            SQLiteDatabase db = dbh.getReadableDatabase();
            String _WHERE= dbh.LCR_LECTURA_CERTIFICADO_ID +" = "+Certificado;
            //Ejecutar update
            remove = db.delete(dbh.TBL_LECTURA_CERTIFICADOS,  _WHERE, null);
        }
        catch(Exception ex){
            remove = 0;
            Log.e("updateCliente", "Error: "+ex.getMessage());
        }
        return  remove;
    }
}
