package com.saptra.sieron.myapplication.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saptra.sieron.myapplication.Models.cPeriodos;
import com.saptra.sieron.myapplication.Models.mUsuarios;
import com.saptra.sieron.myapplication.Utils.Globals;

import java.util.ArrayList;
import java.util.List;

public class mUsuariosData {
    public static mUsuariosData instance;
    Context context;
    DatabaseHelper dbh;

    public mUsuariosData(Context context){
        this.context = context;
        dbh = new DatabaseHelper(context);
    }

    //Get Instance
    public static synchronized mUsuariosData getInstance(Context context) {
        if (instance == null) {
            instance = new mUsuariosData(context.getApplicationContext());
        }
        return instance;
    }

    /***************************************************************************
     * CREATE_PERIODO
     ***************************************************************************/
    /*public long createUsuario(mUsuarios usuario) {
        long IdUsuario = 0;
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbh.USR_USUARIO_ID, usuario.getUsuarioId());
        values.put(dbh.USR_FECHA_CREACION, Globals.getInstance().getDateTime());
        values.put(dbh.USR_ESTATUS_ID, usuario.getUsuarioId());
        values.put(dbh.USR_LOGIN_USUARIO, usuario.getLoginUsuario());
        values.put(dbh.USR_NOMBRES_USUARIO, usuario.getNombresUsuario());
        values.put(dbh.USR_APELLIDOS_USUARIO, usuario.getApellidosUsuario());
        values.put(dbh.USR_ROL_ID, usuario.getTipoFiguras().());
        values.put(dbh.USR_ESTATUS_ID, usuario.getUsuarioId());
        //values.put(dbh.USR_IMAGEN_USUARIO, usuario.getimagen);
        values.put(dbh.USR_EMAIL_USUARIO, usuario.getEmailUsuario());
        values.put(dbh.USR_TIPO_FIGURA_ID, usuario.getTipoFiguras().getTipoFiguraId());
        // insert row
        IdUsuario = db.insert(dbh.TBL_USUARIOS, null, values);
        db.close();
        Log.e("DBH-createUsuario", "Id:"+IdUsuario);
        return IdUsuario;
    }*/

    /***************************************************************************
     * DELETE USUARIO
     ***************************************************************************/
    public void deleteUsuario() {
        try {
            SQLiteDatabase db = dbh.getReadableDatabase();
            int d_rows = db.delete(dbh.TBL_USUARIOS, "", null);
            Log.e("deleteUsuarios", "deleted_rows: " + d_rows);
        }
        catch (Exception ex){
            Log.e("deleteUsuarios", "Error: "+ex.getLocalizedMessage());
            throw  ex;
        }
    }

    /***************************************************************************
     * GET_ALL_MOTIVOS_NO_PAGO
     ***************************************************************************/
    public List<mUsuarios> getAllUsuarios(){
        List<mUsuarios> lstUsuarios= new ArrayList<mUsuarios>();
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM "+dbh.TBL_USUARIOS +" A " +
                "JOIN "+dbh.TBL_TIPO_FIGURAS+" B on "+
                "A."+dbh.USR_TIPO_FIGURA_ID +" = B."+dbh.TIF_TIPO_FIGURA_ID , null);
        Log.e("getAllUsuarios-Cursor", ""+c.getCount());
        try{
            if(c.moveToFirst()){
                do{
                    mUsuarios obj = new mUsuarios();
                    obj.setUsuarioId(c.getInt(c.getColumnIndex(dbh.USR_USUARIO_ID)));
                    obj.setNombresUsuario(c.getString(c.getColumnIndex(dbh.USR_NOMBRES_USUARIO)));
                    obj.setApellidosUsuario(c.getString(c.getColumnIndex(dbh.USR_APELLIDOS_USUARIO)));
                    obj.setEmailUsuario(c.getString(c.getColumnIndex(dbh.USR_EMAIL_USUARIO)));
                    obj.setEmailUsuario(c.getString(c.getColumnIndex(dbh.USR_NOMBRES_USUARIO)));
                    obj.getTipoFiguras().setTipoFiguraId(c.getInt(c.getColumnIndex(dbh.USR_TIPO_FIGURA_ID)));
                    obj.getTipoFiguras().setDescripcionTipoFigura(c.getString(c.getColumnIndex(dbh.TIF_DESCRIPCION_TIPO_FIGURA)));
                    lstUsuarios.add(obj);
                }
                while(c.moveToNext());
            }
        }
        catch (Exception ex){
            Log.d("getAllUsuarios", "Error mientras se intentaba obtener informacion desde la bd");
        }
        finally {
            if(c != null && c.isClosed()){
                c.close();
            }
        }
        return lstUsuarios;
    }
}
