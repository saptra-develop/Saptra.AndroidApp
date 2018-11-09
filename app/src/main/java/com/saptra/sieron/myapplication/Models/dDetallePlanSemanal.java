package com.saptra.sieron.myapplication.Models;

public class dDetallePlanSemanal {
    private int DetallePlanId;
    private mPlanSemanal mPlanSemanal;
    private String FechaCreacion;
    private int UsuarioCreacionId;
    private int EstatusId;
    private cTipoActividades cTipoActividades;
    private String FechaActividad;
    private String HoraActividad;
    private int CantidadCheckIn;
    private String DescripcionActividad;
    private String LugarActividad;
    private String ComentariosNoValidacion;
    private String ComentariosRechazo;
    private boolean ActividadRechazada;

    public dDetallePlanSemanal(){
        DetallePlanId = 0;
        mPlanSemanal =  new mPlanSemanal();
        UsuarioCreacionId = 0;
        EstatusId = 0;
        cTipoActividades = new cTipoActividades();
        CantidadCheckIn = 0;
        ActividadRechazada = false;
    }

    public int getDetallePlanId() {
        return DetallePlanId;
    }

    public void setDetallePlanId(int detallePlanId) {
        DetallePlanId = detallePlanId;
    }

    public mPlanSemanal getPlanSemanal() {
        return mPlanSemanal;
    }

    public void setPlanSemanal(int PlanSemanalId) {
        this.mPlanSemanal = mPlanSemanal;
    }

    public String getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        FechaCreacion = fechaCreacion;
    }

    public int getUsuarioCreacionId() {
        return UsuarioCreacionId;
    }

    public void setUsuarioCreacionId(int usuarioCreacionId) {
        UsuarioCreacionId = usuarioCreacionId;
    }

    public int getEstatusId() {
        return EstatusId;
    }

    public void setEstatusId(int estatusId) {
        EstatusId = estatusId;
    }

    public cTipoActividades getTipoActividades() {
        return cTipoActividades;
    }

    public void setTipoActividades(cTipoActividades cTipoActividades) {
        cTipoActividades = cTipoActividades;
    }

    public String getFechaActividad() {
        return FechaActividad;
    }

    public void setFechaActividad(String FechaActividad) {
        this.FechaActividad = FechaActividad;
    }

    public String getHoraActividad() {
        return HoraActividad;
    }

    public void setHoraActividad(String horaActividad) {
        HoraActividad = horaActividad;
    }

    public int getCantidadCheckIn() {
        return CantidadCheckIn;
    }

    public void setCantidadCheckIn(int cantidadCheckIn) {
        CantidadCheckIn = cantidadCheckIn;
    }

    public String getDescripcionActividad() {
        return DescripcionActividad;
    }

    public void setDescripcionActividad(String DescripcionActividad) {
        this.DescripcionActividad = DescripcionActividad;
    }

    public String getLugarActividad() {
        return LugarActividad;
    }

    public void setLugarActividad(String lugarActividad) {
        this.LugarActividad = lugarActividad;
    }

    public String getComentariosNoValidacion() {
        return ComentariosNoValidacion;
    }

    public void setComentariosNoValidacion(String comentariosNoValidacion) {
        ComentariosNoValidacion = comentariosNoValidacion;
    }

    public String getComentariosRechazo() {
        return ComentariosRechazo;
    }

    public void setComentariosRechazo(String comentariosRechazo) {
        ComentariosRechazo = comentariosRechazo;
    }

    public boolean getActividadRechazada() {
        return ActividadRechazada;
    }

    public void setActividadRechazada(boolean actividadRechazada) {
        ActividadRechazada = actividadRechazada;
    }
}
