package com.saptra.sieron.myapplication.Models;

public class cPeriodos {
    private int PeriodoId;
    private String FechaCreacion;
    private int UsuarioCreacionId;
    private int EstatusId;
    private String FechaInicio;
    private String FechaFin;
    private String DecripcionPeriodo;

    public cPeriodos(){
        //PeriodoId = 0;
        //UsuarioCreacionId = 0;
        //EstatusId = 0;
    }

    public int getPeriodoId() {
        return PeriodoId;
    }

    public void setPeriodoId(int periodoId) {
        PeriodoId = periodoId;
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

    public String getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        FechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return FechaFin;
    }

    public void setFechaFin(String fechaFin) {
        FechaFin = fechaFin;
    }

    public String getDecripcionPeriodo() {
        return DecripcionPeriodo;
    }

    public void setDecripcionPeriodo(String decripcionPeriodo) {
        DecripcionPeriodo = decripcionPeriodo;
    }
}
