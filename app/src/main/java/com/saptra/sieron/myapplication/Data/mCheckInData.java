package com.saptra.sieron.myapplication.Data;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.saptra.sieron.myapplication.Models.mCheckIn;
import com.saptra.sieron.myapplication.Models.mLecturaCertificados;
import com.saptra.sieron.myapplication.Utils.Globals;

import java.util.ArrayList;
import java.util.List;

public class mCheckInData {
    public static mCheckInData instance;
    Context context;
    DatabaseHelper dbh;

    public mCheckInData(Context context){
        this.context = context;
        dbh = new DatabaseHelper(context);
    }

    //Get Instance
    public static synchronized mCheckInData getInstance(Context context) {
        if (instance == null) {
            instance = new mCheckInData(context.getApplicationContext());
        }
        return instance;
    }

    /***************************************************************************
     * CREATE
     ***************************************************************************/
    public long createCheckIn(mCheckIn ci) {
        long IdCheckIn= 0;
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbh.CHK_CHECKIN_ID, ci.getCheckInId());
        values.put(dbh.CHK_FECHA_CREACION, Globals.getInstance().getDateTime());
        values.put(dbh.CHK_USUARIO_CREACION_ID, ci.getUsuarioCreacionId());
        values.put(dbh.CHK_COORDENADAS, ci.getCoordenadas());
        values.put(dbh.CHK_DETALLE_PLAN_ID, ci.getDetallePlan().getDetallePlanId());
        values.put(dbh.CHK_INCIDENCIAS, ci.getIncidencias());
        values.put(dbh.CHK_FOTO_INCIDENCIA, ci.getFotoIncidencia());
        values.put(dbh.CHK_IMAGE_DATA, ci.getImageData());
        values.put(dbh.CHK_STATE, ci.getState());
        values.put(dbh.CHK_UUID, ci.getUUID());
        // insert row
        IdCheckIn = db.insert(dbh.TBL_CHECKIN, null, values);
        db.close();
        Log.e("DBH-createCheckIn", "Id:"+IdCheckIn);
        return IdCheckIn;
    }

    /***************************************************************************
     * DELETE
     ***************************************************************************/
    public void deleteCheckIn() {
        try {
            SQLiteDatabase db = dbh.getReadableDatabase();
            String _WHERE = dbh.CHK_STATE+ " = 'S'";
            int d_rows = db.delete(dbh.TBL_CHECKIN, _WHERE, null);
            Log.e("deleteCheckIn", "deleted_rows: " + d_rows);
        }
        catch (Exception ex){
            Log.e("deleteCheckIn", "Error: "+ex.getLocalizedMessage());
            throw  ex;
        }
    }

    /***************************************************************************
     * OBENER CHECK-INS POR ACIVIDAD COUNT
     ***************************************************************************/
    public long getCheckInRealizados(int DetallePlanId) {
        SQLiteDatabase db = dbh.getReadableDatabase();
        long rows = DatabaseUtils.queryNumEntries(db, dbh.TBL_CHECKIN,
                dbh.CHK_DETALLE_PLAN_ID + "=?",
                new String[]{"" + DetallePlanId});
        Log.e("getCheckInRealizados", "rows:" + rows);
        return rows;
    }

    /***************************************************************************
     * GET CHECK-INS POR ACTIVIDAD PLAN SEMANAL
     ***************************************************************************/
    public mCheckIn getCheckInsDetallePlan(int DetallePlanId, String certificado){
        mCheckIn CheckIn = new mCheckIn();
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor c = db.rawQuery(
                " SELECT A.*"+
                        " FROM "+dbh.TBL_CHECKIN +" A "+
                        " JOIN "+ dbh.TBL_DETALLE_PLAN_SEMANAL +" B on A."+dbh.CHK_DETALLE_PLAN_ID+" = B."+dbh.DPS_DETALLE_PLAN_ID+
                        " LEFT JOIN "+ dbh.TBL_LECTURA_CERTIFICADOS +" C on A."+dbh.CHK_CHECKIN_ID+" = C."+dbh.LCR_CHECKIN_ID+
                        " WHERE A."+dbh.CHK_DETALLE_PLAN_ID+" = "+DetallePlanId+" AND "+
                        (certificado.equals("") ? "1 = 1 " : "C."+dbh.LCR_FOLIO_CERTIFICADO+" = '"+certificado.trim()+"' ")
                , null);
        Log.e("getCheckInsDetallePlan", ""+c.getCount());
        try{
            if(c.moveToFirst()){
                mCheckIn obj = new mCheckIn();
                obj.setCheckInId(c.getString(c.getColumnIndex(dbh.CHK_CHECKIN_ID)));
                obj.setRowId(c.getInt(c.getColumnIndex(dbh.CHK_ROW_ID)));
                obj.setImageData(c.getString(c.getColumnIndex(dbh.CHK_IMAGE_DATA)));
                obj.setIncidencias(c.getString(c.getColumnIndex(dbh.CHK_INCIDENCIAS)));
                obj.setFechaCreacion(c.getString(c.getColumnIndex(dbh.CHK_FECHA_CREACION)));
                CheckIn = obj;
            }
        }
        catch (Exception ex){
            Log.d("getCheckInsDetallePlan", "Error mientras se intentaba obtener informacion desde la bd");
        }
        finally {
            if(c != null && c.isClosed()){
                c.close();
            }
        }
        return CheckIn;
    }

    /***************************************************************************
     * GET CHECK-IN NO ENVIADOS
     ***************************************************************************/
    public List<mLecturaCertificados> getCheckInsNoEnviados(){
        List<mLecturaCertificados> noEnviados = new ArrayList<mLecturaCertificados>();
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor c = db.rawQuery(
                " SELECT "+
                        " A."+dbh.CHK_CHECKIN_ID+", A."+dbh.CHK_DETALLE_PLAN_ID+", A."+dbh.CHK_FECHA_CREACION+" CHK_F_C"
                        +", A."+dbh.CHK_USUARIO_CREACION_ID+" CHK_USER, A."+dbh.CHK_IMAGE_DATA+", A."+dbh.CHK_INCIDENCIAS
                        +", A."+dbh.CHK_COORDENADAS+", A."+dbh.CHK_UUID+" CHK_UUID, A."+dbh.CHK_STATE+" CHK_STATE,"+
                        " B."+dbh.LCR_LECTURA_CERTIFICADO_ID+", B."+dbh.LCR_CHECKIN_ID+" LCR_CHECKIN_ID, B."+dbh.LCR_FOLIO_CERTIFICADO
                        +", B."+dbh.LCR_FECHA_CREACION+" LCR_F_C, B."+dbh.LCR_USUARIO_CREACION_ID+" LCR_USER"
                        +", IFNULL(B."+dbh.LCR_UUID+",'') LCR_UUID, B."+dbh.LCR_STATE+" LCR_STATE"+
                        " FROM "+dbh.TBL_CHECKIN +" A "+
                        " LEFT JOIN "+ dbh.TBL_LECTURA_CERTIFICADOS +" B on A."+dbh.CHK_CHECKIN_ID+" = B."+dbh.LCR_CHECKIN_ID+
                        " AND B."+dbh.LCR_STATE+" = 'I'"+
                        " WHERE A."+dbh.CHK_STATE+" = 'I'"
                , null);
        Log.e("getCheckInsNoEnviados", ""+c.getCount());
        try{
            if(c.moveToFirst()){
                do {
                    mLecturaCertificados obj = new mLecturaCertificados();
                    obj.getCheckIn().setCheckInId(c.getString(c.getColumnIndex(dbh.CHK_CHECKIN_ID)));
                    obj.getCheckIn().getDetallePlan().setDetallePlanId(c.getInt(c.getColumnIndex(dbh.CHK_DETALLE_PLAN_ID)));
                    obj.getCheckIn().setFechaCreacion(c.getString(c.getColumnIndex("CHK_F_C")));
                    obj.getCheckIn().setUsuarioCreacionId(c.getInt(c.getColumnIndex("CHK_USER")));
                    obj.getCheckIn().setImageData(c.getString(c.getColumnIndex(dbh.CHK_IMAGE_DATA)));
                    obj.getCheckIn().setIncidencias(c.getString(c.getColumnIndex(dbh.CHK_INCIDENCIAS)));
                    obj.getCheckIn().setCoordenadas(c.getString(c.getColumnIndex(dbh.CHK_COORDENADAS)));
                    obj.getCheckIn().setUUID(c.getString(c.getColumnIndex("CHK_UUID")));
                    obj.getCheckIn().setState(c.getString(c.getColumnIndex("CHK_STATE")));

                    obj.setLecturaCertificadoId(c.getInt(c.getColumnIndex(dbh.LCR_LECTURA_CERTIFICADO_ID)));
                    obj.setFolioCertificado(c.getString(c.getColumnIndex(dbh.LCR_FOLIO_CERTIFICADO)));
                    obj.setFechaCreacion(c.getString(c.getColumnIndex("LCR_F_C")));
                    obj.setUsuarioCreacionId(c.getInt(c.getColumnIndex("LCR_USER")));
                    obj.setUUID(c.getString(c.getColumnIndex("LCR_UUID")));
                    obj.setState(c.getString(c.getColumnIndex("LCR_STATE")));
                    noEnviados.add(obj);
                }
                while(c.moveToNext());
            }
        }
        catch (Exception ex){
            Log.d("getCheckInsNoEnviados", "Error mientras se intentaba obtener informacion desde la bd");
        }
        finally {
            if(c != null && c.isClosed()){
                c.close();
            }
        }
        return noEnviados;
    }

    public int deleteCheckInRow(String CheckInId){
        int remove = 0;
        try{
            SQLiteDatabase db = dbh.getReadableDatabase();
            String _WHERE= dbh.CHK_CHECKIN_ID +" = "+CheckInId;
            //Ejecutar update
            remove = db.delete(dbh.TBL_CHECKIN,  _WHERE, null);
        }
        catch(Exception ex){
            remove = 0;
            Log.e("updateCliente", "Error: "+ex.getMessage());
        }
        return  remove;
    }

    public int updateCheckIn(mCheckIn check){
        int actualizado = 0;
        try{
            SQLiteDatabase db = dbh.getReadableDatabase();
            //Contenedor de valores a modificar
            ContentValues cv = new ContentValues();
            cv.put(dbh.CHK_CHECKIN_ID, check.getCheckInId());
            cv.put(dbh.CHK_STATE, check.getState());
            //Generar where
            String _WHERE= dbh.CHK_UUID +" = '"+ check.getUUID()+"'";
            //Ejecutar update
            actualizado = db.update(dbh.TBL_CHECKIN, cv, _WHERE, null);
        }
        catch(Exception ex){
            actualizado = 0;
            Log.e("updateCheckIn", "Error: "+ex.getMessage());
        }
        return  actualizado;
    }
}
