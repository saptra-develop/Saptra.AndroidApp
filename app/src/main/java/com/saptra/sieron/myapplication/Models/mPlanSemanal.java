package com.saptra.sieron.myapplication.Models;

public class mPlanSemanal {
    private int PlanSemanalId;
    private String FechaCreacion;
    private int UsuarioCreacionId;
    private int EstatusId;
    private String DescripcionPlaneacion;
    private cPeriodos cPeriodos;

    public mPlanSemanal(){
        PlanSemanalId = 0;
        UsuarioCreacionId = 0;
        EstatusId = 0;
        cPeriodos = new cPeriodos();
    }

    public int getPlanSemanalId() {
        return PlanSemanalId;
    }

    public void setPlanSemanalId(int planSemanalId) {
        PlanSemanalId = planSemanalId;
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

    public String getDescripcionPlaneacion() {
        return DescripcionPlaneacion;
    }

    public void setDescripcionPlaneacion(String descripcionPlaneacion) {
        DescripcionPlaneacion = descripcionPlaneacion;
    }

    public cPeriodos getPeriodos() {
        return cPeriodos;
    }

    public void setPeriodos(cPeriodos cPeriodos) {
        this.cPeriodos = cPeriodos;
    }
}
