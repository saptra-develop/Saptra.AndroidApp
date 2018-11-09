package com.saptra.sieron.myapplication.Data;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.saptra.sieron.myapplication.Models.mCheckIn;
import com.saptra.sieron.myapplication.Utils.Globals;

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
        //values.put(dbh.CHK_CHECKIN_ID, ci.getCheckInId());
        values.put(dbh.CHK_FECHA_CREACION, Globals.getInstance().getDateTime());
        values.put(dbh.CHK_USUARIO_CREACION_ID, ci.getUsuarioCreacionId());
        values.put(dbh.CHK_COORDENADAS, ci.getCoordenadas());
        values.put(dbh.CHK_DETALLE_PLAN_ID, ci.getDetallePlan().getDetallePlanId());
        values.put(dbh.CHK_INCIDENCIAS, ci.getIncidencias());
        values.put(dbh.CHK_FOTO_INCIDENCIA, ci.getFotoIncidencia());
        values.put(dbh.CHK_IMAGE_DATA, ci.getImageData());
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
            int d_rows = db.delete(dbh.TBL_CHECKIN, "", null);
            Log.e("deleteCheckIn", "deleted_rows: " + d_rows);
        }
        catch (Exception ex){
            Log.e("deleteCheckIn", "Error: "+ex.getLocalizedMessage());
            throw  ex;
        }
    }

    /***************************************************************************
     * OBENER CHECK-INS POR DETALLE PLAN
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
    public mCheckIn getCheckInsDetallePlan(int DetallePlanId){
        mCheckIn CheckIn = new mCheckIn();
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor c = db.rawQuery(
                " SELECT A.*"+//dbh.CHK_ROW_ID+", "+dbh.CHK_IMAGE_DATA+", "+dbh.CHK_INCIDENCIAS+", "+dbh.CHK_FECHA_CREACION+
                        " FROM "+dbh.TBL_CHECKIN +" A "+
                        " JOIN "+ dbh.TBL_DETALLE_PLAN_SEMANAL +" B on A."+dbh.CHK_DETALLE_PLAN_ID+" = B."+dbh.DPS_DETALLE_PLAN_ID+
                        " WHERE A."+dbh.CHK_DETALLE_PLAN_ID+" = "+DetallePlanId
                , null);
        Log.e("getCheckInsDetallePlan", ""+c.getCount());
        try{
            if(c.moveToFirst()){
                mCheckIn obj = new mCheckIn();
                obj.setCheckInId(c.getInt(c.getColumnIndex(dbh.CHK_CHECKIN_ID)));
                obj.setRowId(c.getInt(c.getColumnIndex(dbh.CHK_ROW_ID)));
                byte [] img = (c.getBlob(c.getColumnIndex(dbh.CHK_IMAGE_DATA)));
                obj.setImageData(Globals.getInstance().ByteArrayToB64Sring(img));
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

    public int deleteCheckInRow(int CheckInId){
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
}
