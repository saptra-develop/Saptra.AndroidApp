package com.saptra.sieron.myapplication.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;

    private static final int DATABASE_VERSION = 1;

    //DB name
    private static final String DATABASE_NAME = "DB_SAPTRA";

    //DB Table names
    public static final String TBL_USUARIOS = "UsuarioId";
    public static final String TBL_TIPO_ACTIVIDADES= "cTipoActividades";
    public static final String TBL_TIPO_FIGURAS= "cTipoFiguras";
    public static final String TBL_DETALLE_PLAN_SEMANAL = "dDetallePlanSemanal";
    public static final String TBL_PLAN_SEMANAL = "mPlanSemanal";
    public static final String TBL_ROLES = "mRoles";
    public static final String TBL_PERIODOS = "cPeriodos";
    public static final String TBL_CHECKIN = "mCheckIn";
    public static final String TBL_LECTURA_CERTIFICADOS = "mLecturaCertificados";


    //USUARIOS
    public static final String USR_USUARIO_ID = "UsuarioId";
    public static final String USR_FECHA_CREACION = "FechaCreacion";
    public static final String USR_ESTATUS_ID = "EstatusId";
    public static final String USR_LOGIN_USUARIO = "LoginUsuario";
    public static final String USR_PASSWORD_USUARIO = "PasswordUsuario";
    public static final String USR_NOMBRES_USUARIO = "NombresUsuario";
    public static final String USR_APELLIDOS_USUARIO = "ApelildosUsuario";
    public static final String USR_ROL_ID= "RolId";
    public static final String USR_IMAGEN_USUARIO = "ImagenUsuario";
    public static final String USR_EMAIL_USUARIO = "EmailUsuario";
    public static final String USR_TIPO_FIGURA_ID = "TipoFiguraId";

    //ROLES
    public static final String ROL_ID = "RolId";
    public static final String ROL_FECHA_CREACION = "FechaCreacion";
    public static final String ROL_ESTATUS_ID = "EstatusId";
    public static final String ROL_NOMBRE_ROL = "NombreRol";

    //PLAN SEMANAL
    public static final String PLS_PLAN_SEMANAL_ID = "PlanSemanalId";
    public static final String PLS_FECHA_CREACION= "FechaCreacion";
    public static final String PLS_USUARIO_CREACION_ID = "UsuarioCreacionId";
    public static final String PLS_ESTATUS_ID = "EstatusId";
    public static final String PLS_DESCRIPCION_PLANEACION = "DescripcionPlaneacion";
    public static final String PLS_PERIODO_ID = "PeriodoId";

    //DETALLE PLAN SEMANAL
    public static final String DPS_DETALLE_PLAN_ID = "DetallePlanId";
    public static final String DPS_PLAN_SEMANAL_ID = "PlanSemanalId";
    public static final String DPS_FECHA_CREACION = "FechaCreacion";
    public static final String DPS_USUARIO_CREACION_ID = "UsuarioCreacionId";
    public static final String DPS_ESTATUS_ID = "EstatusId";
    public static final String DPS_TIPO_ACTIVIDAD_ID = "TipoActividadId";
    public static final String DPS_FECHA_ACTIVIDAD = "FechaActividad";
    public static final String DPS_HORA_ACTIVIDAD = "HoraActividad";
    public static final String DPS_CANTIDAD_CHECKIN = "CantidadCheckIn";
    public static final String DPS_DESCRIPCION_ACTIVIDAD = "DescripcionActividad";
    public static final String DPS_LUGAR_ACTIVIDAD = "LugarActividad";
    public static final String DPS_COMENTARIOS_NO_VALIDACION = "ComentariosNoValidacion";
    public static final String DPS_COMENTARIOS_RECHAZO = "ComentariosRechazo";
    public static final String DPS_ACTIVIDAD_RECHAZADA = "ActividadRechazada";

    //TIPO FIGURAS
    public static final String TIF_TIPO_FIGURA_ID = "TipoFiguraId";
    public static final String TIF_FECHA_CREACION = "FechaCreacion";
    public static final String TIF_USAURIO_CREACION_ID = "UsuarioCreacionId";
    public static final String TIF_ESTATUS_ID = "EstatusId";
    public static final String TIF_DESCRIPCION_TIPO_FIGURA = "DescripcionTipoFigura";

    //TIPO ACTIVIDADES
    public static final String TIA_TIPO_ACTIVIDAD_ID = "TipoActividadId";
    public static final String TIA_FECHA_CREACION = "FechaCreacion";
    public static final String TIA_USUARIO_CREACION_ID = "UsuarioCreacionId";
    public static final String TIA_ESTATUS_ID = "EstatusId";
    public static final String TIA_NOMBRE_ACTIVIDAD = "NombreActividad";
    public static final String TIA_DESCRIPCION_ACTIVIDAD = "DescripcionActividad";
    public static final String TIA_REQUIERE_CHECKIN= "RequiereCheckIn";
    public static final String TIA_ACTIVIDAD_ESPECIAL = "ActividadEspecial";
    public static final String TIA_MENSAJE = "Mensaje";

    //PERIODOS
    public static final String PER_PERIODO_ID = "PeriodoId";
    public static final String PER_FECHA_CREACION = "FechaCreacion";
    public static final String PER_USUARIO_CREACION_ID = "UsuarioCreacionId";
    public static final String PER_ESTATUS_ID = "EstatusId";
    public static final String PER_FECHA_INICIO = "FechaInicio";
    public static final String PER_FECHA_FIN = "FechaFin";
    public static final String PER_DESCRIPCION_PERIODO = "DecripcionPeriodo";

    //CheckIn
    public static final String CHK_ROW_ID = "RowId";
    public static final String CHK_CHECKIN_ID = "CheckInId";
    public static final String CHK_FECHA_CREACION = "FechaCreacion";
    public static final String CHK_USUARIO_CREACION_ID = "UsuarioCreacionId";
    public static final String CHK_COORDENADAS = "Coordenadas";
    public static final String CHK_DETALLE_PLAN_ID= "DetallePlanId";
    public static final String CHK_INCIDENCIAS = "Incidencias";
    public static final String CHK_IMAGE_DATA = "ImageData";
    public static final String CHK_FOTO_INCIDENCIA = "FotoIncidencia";
    public static final String CHK_STATE = "State";
    public static final String CHK_UUID = "UUID";

    //LecturaCertificados
    public static final String LCR_ROW_ID = "RowId";
    public static final String LCR_LECTURA_CERTIFICADO_ID = "LecturaCertificadoId";
    public static final String LCR_FECHA_CREACION = "FechaCreacion";
    public static final String LCR_USUARIO_CREACION_ID = "UsuarioCreacionId";
    public static final String LCR_FOLIO_CERTIFICADO = "FolioCertificado";
    public static final String LCR_ESTATUS_ID = "EstatusId";
    public static final String LCR_CHECKIN_ID = "CheckInId";
    public static final String LCR_STATE = "State";
    public static final String LCR_UUID = "UUID";

    //TABLA PLAN SEMANAL
    private static final String CREATE_TABLE_PERIODOS =
            "CREATE TABLE " +TBL_PERIODOS+" ("+
                    PER_PERIODO_ID+ " INTEGER PRIMARY KEY, "+
                    PER_FECHA_CREACION+ " TEXT, "+
                    PER_USUARIO_CREACION_ID+ " INTEGER, "+
                    PER_ESTATUS_ID+ " INTEGER, "+
                    PER_FECHA_INICIO+ " TEXT, "+
                    PER_FECHA_FIN+ " TEXT, "+
                    PER_DESCRIPCION_PERIODO+ " TEXT )";

    //TABLA USUARIOS
    private static final String CREATE_TABLE_USUARIOS =
            "CREATE TABLE " +TBL_USUARIOS+" ("+
                    USR_USUARIO_ID+ " INTEGER PRIMARY KEY, "+
                    USR_FECHA_CREACION+ " TEXT, "+
                    USR_ESTATUS_ID+ " INTEGER, "+
                    USR_LOGIN_USUARIO+ " TEXT, "+
                    USR_PASSWORD_USUARIO+ " TEXT, "+
                    USR_NOMBRES_USUARIO+ " TEXT, "+
                    USR_APELLIDOS_USUARIO+ " TEXT, "+
                    USR_ROL_ID+ " INTEGER, "+
                    USR_IMAGEN_USUARIO+ " TEXT, "+
                    USR_EMAIL_USUARIO+ " TEXT, "+
                    USR_TIPO_FIGURA_ID+ " INTEGER )";

    //TABLA ROLES
    private static final String CREATE_TABLE_ROLES =
            "CREATE TABLE " +TBL_ROLES+" ("+
                    ROL_ID+ " INTEGER PRIMARY KEY, "+
                    ROL_FECHA_CREACION+ " TEXT, "+
                    ROL_ESTATUS_ID+ " INTEGER, "+
                    ROL_NOMBRE_ROL+ " TEXT )";

    //TABLA PLAN SEMANAL
    private static final String CREATE_TABLE_PLAN_SEMANAL =
            "CREATE TABLE " +TBL_PLAN_SEMANAL+" ("+
                    PLS_PLAN_SEMANAL_ID+ " INTEGER PRIMARY KEY, "+
                    PLS_FECHA_CREACION+ " TEXT, "+
                    PLS_USUARIO_CREACION_ID+ " INTEGER, "+
                    PLS_ESTATUS_ID+ " INTEGER, "+
                    PLS_DESCRIPCION_PLANEACION+ " TEXT, "+
                    PLS_PERIODO_ID+ " INTEGER )";

    //TABLA DETALLE PLAN SEMANAL
    private static final String CREATE_TABLE_DETALLE_PLAN_SEMANAL =
            "CREATE TABLE " +TBL_DETALLE_PLAN_SEMANAL+" ("+
                    DPS_DETALLE_PLAN_ID+ " INTEGER PRIMARY KEY, "+
                    DPS_PLAN_SEMANAL_ID+ " INTEGER,  "+
                    DPS_FECHA_CREACION+ " TEXT, "+
                    DPS_USUARIO_CREACION_ID+ " INTEGER, "+
                    DPS_ESTATUS_ID+ " INTEGER, "+
                    DPS_TIPO_ACTIVIDAD_ID+ " INTEGER, "+
                    DPS_FECHA_ACTIVIDAD+ " TEXT, "+
                    DPS_HORA_ACTIVIDAD+ " TEXT, "+
                    DPS_CANTIDAD_CHECKIN+ " INTEGER, " +
                    DPS_DESCRIPCION_ACTIVIDAD+ " TEXT, "+
                    DPS_LUGAR_ACTIVIDAD+ " TEXT, "+
                    DPS_COMENTARIOS_NO_VALIDACION+ " TEXT, "+
                    DPS_COMENTARIOS_RECHAZO+ " TEXT, "+
                    DPS_ACTIVIDAD_RECHAZADA+ " INTEGER )";

    //TABLA TIPO FIGURAS
    private static final String CREATE_TABLE_TIPO_FIGURAS =
            "CREATE TABLE " +TBL_TIPO_FIGURAS+" ("+
                    TIF_TIPO_FIGURA_ID+ " INTEGER PRIMARY KEY, "+
                    TIF_FECHA_CREACION+ " TEXT, "+
                    TIF_USAURIO_CREACION_ID+ " INTEGER, "+
                    TIF_ESTATUS_ID+ " INTEGER, "+
                    TIF_DESCRIPCION_TIPO_FIGURA+ " TEXT )";

    //TABLA TIPO ACTIVIDADES
    private static final String CREATE_TABLE_TIPO_ACTIVIDADES =
            "CREATE TABLE " +TBL_TIPO_ACTIVIDADES+" ("+
                    TIA_TIPO_ACTIVIDAD_ID+ " INTEGER PRIMARY KEY, "+
                    TIA_FECHA_CREACION+ " TEXT, "+
                    TIA_USUARIO_CREACION_ID+ " INTEGER, "+
                    TIA_ESTATUS_ID+ " INTEGER, "+
                    TIA_NOMBRE_ACTIVIDAD+ " TEXT, "+
                    TIA_DESCRIPCION_ACTIVIDAD+ " TEXT, "+
                    TIA_REQUIERE_CHECKIN+ " INTEGER, "+
                    TIA_ACTIVIDAD_ESPECIAL+ " INTEGER, "+
                    TIA_MENSAJE+ " TEXT )";

    //TABLA CHECK_IN
    private static final String CREATE_TABLE_CHECKIN =
            "CREATE TABLE " +TBL_CHECKIN+" ("+
                    CHK_ROW_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    CHK_CHECKIN_ID+ " TEXT , "+
                    CHK_FECHA_CREACION+ " TEXT, "+
                    CHK_USUARIO_CREACION_ID+ " INTEGER, "+
                    CHK_COORDENADAS+ " TEXT, "+
                    CHK_DETALLE_PLAN_ID+ " INTEGER, "+
                    CHK_INCIDENCIAS+ " TEXT, "+
                    CHK_IMAGE_DATA+ " TEXT, "+
                    CHK_FOTO_INCIDENCIA+ " TEXT, "+
                    CHK_STATE+ " TEXT, "+
                    CHK_UUID+ " TEXT )";

    //TABLA LECTURA_CERTIFICADOS
    private static final String CREATE_TABLE_LECTURA_CERFITICADOS =
            "CREATE TABLE " +TBL_LECTURA_CERTIFICADOS+" ("+
                    LCR_ROW_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    LCR_LECTURA_CERTIFICADO_ID+ " INTEGER, "+
                    LCR_FECHA_CREACION+ " TEXT, "+
                    LCR_USUARIO_CREACION_ID+ " INTEGER, "+
                    LCR_FOLIO_CERTIFICADO+ " TEXT, "+
                    LCR_ESTATUS_ID+ " INTEGER, "+
                    LCR_CHECKIN_ID+ " TEXT, "+
                    CHK_STATE+ " TEXT, "+
                    CHK_UUID+ " TEXT )";


    public static synchronized DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    //Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // creating required tables
        db.execSQL(CREATE_TABLE_USUARIOS);
        db.execSQL(CREATE_TABLE_ROLES);
        db.execSQL(CREATE_TABLE_PLAN_SEMANAL);
        db.execSQL(CREATE_TABLE_DETALLE_PLAN_SEMANAL);
        db.execSQL(CREATE_TABLE_TIPO_FIGURAS);
        db.execSQL(CREATE_TABLE_TIPO_ACTIVIDADES);
        db.execSQL(CREATE_TABLE_PERIODOS);
        db.execSQL(CREATE_TABLE_CHECKIN);
        db.execSQL(CREATE_TABLE_LECTURA_CERFITICADOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ROLES);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_PLAN_SEMANAL);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_DETALLE_PLAN_SEMANAL);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TIPO_FIGURAS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TIPO_ACTIVIDADES);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_PERIODOS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_CHECKIN);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_LECTURA_CERFITICADOS);

        // create new tables
        onCreate(db);
    }
}
