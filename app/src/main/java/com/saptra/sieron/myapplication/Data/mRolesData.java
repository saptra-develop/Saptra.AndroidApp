package com.saptra.sieron.myapplication.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saptra.sieron.myapplication.Models.mRoles;
import com.saptra.sieron.myapplication.Utils.Globals;

import java.util.ArrayList;
import java.util.List;

public class mRolesData {
    public static mRolesData instance;
    Context context;
    DatabaseHelper dbh;

    public mRolesData(Context context){
        this.context = context;
        dbh = new DatabaseHelper(context);
    }

    //Get Instance
    public static synchronized mRolesData getInstance(Context context) {
        if (instance == null) {
            instance = new mRolesData(context.getApplicationContext());
        }
        return instance;
    }

    /***************************************************************************
     * CREATE_ROL
     ***************************************************************************/
    public long createRol(mRoles rol) {
        long IdRol = 0;
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbh.ROL_ID, rol.getRolId());
        values.put(dbh.ROL_FECHA_CREACION, Globals.getInstance().getDateTime());
        values.put(dbh.ROL_ESTATUS_ID, rol.getEstatusId());
        values.put(dbh.ROL_NOMBRE_ROL, rol.getNombreRol());
        // insert row
        IdRol = db.insert(dbh.TBL_ROLES, null, values);
        db.close();
        Log.e("DBH-createRol", "Id:"+IdRol);
        return IdRol;
    }

    /***************************************************************************
     * DELETE ROL
     ***************************************************************************/
    public void deleteRol() {
        try {
            SQLiteDatabase db = dbh.getReadableDatabase();
            int d_rows = db.delete(dbh.TBL_ROLES, "", null);
            Log.e("deleteRol", "deleted_rows: " + d_rows);
        }
        catch (Exception ex){
            Log.e("deleteRol", "Error: "+ex.getLocalizedMessage());
            throw  ex;
        }
    }
}
