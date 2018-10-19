package com.saptra.sieron.myapplication.Models;

public class dDetallePlanSemanal {
    private int DetallePlanId;
    private int PlanSemanalId;
    private String FechaCreacion;
    private int UsuarioCreacionId;
    private int EstatusId;
    private cTipoActividades TipoActividad;
    private String FechaAcividad;
    private String HoraActividad;
    private int CantidadCheckIn;
    private String DescripcionActividad;
    private String ComentariosNoValidacion;
    private String ComentariosRechazo;
    private boolean ActividadRechazada;

    public dDetallePlanSemanal(){
        DetallePlanId = 0;
        UsuarioCreacionId = 0;
        EstatusId = 0;
        TipoActividad = new cTipoActividades();
        CantidadCheckIn = 0;
        ActividadRechazada = false;
    }

    public int getDetallePlanId() {
        return DetallePlanId;
    }

    public void setDetallePlanId(int detallePlanId) {
        DetallePlanId = detallePlanId;
    }

    public int getPlanSemanalId() {
        return PlanSemanalId;
    }

    public void setPlanSemanalId(int PlanSemanalId) {
        this.PlanSemanalId = PlanSemanalId;
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

    public cTipoActividades getTipoActividad() {
        return TipoActividad;
    }

    public void setTipoActividad(cTipoActividades tipoActividad) {
        TipoActividad = tipoActividad;
    }

    public String getFechaAcividad() {
        return FechaAcividad;
    }

    public void setFechaAcividad(String fechaAcividad) {
        FechaAcividad = fechaAcividad;
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

    public void setDescripcionActividad(String descripcionActividad) {
        DescripcionActividad = descripcionActividad;
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
