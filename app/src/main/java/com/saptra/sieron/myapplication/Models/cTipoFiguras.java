package com.saptra.sieron.myapplication.Models;

public class cTipoFiguras {
    private int TipoFiguraId;
    private String FechaCreacion;
    private int UsuarioCreacionId;
    private int EstatusId;
    private String DescripcionTipoFigura;

    cTipoFiguras(){

    }

    public int getTipoFiguraId() {
        return TipoFiguraId;
    }

    public void setTipoFiguraId(int tipoFiguraId) {
        TipoFiguraId = tipoFiguraId;
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

    public String getDescripcionTipoFigura() {
        return DescripcionTipoFigura;
    }

    public void setDescripcionTipoFigura(String descripcionTipoFigura) {
        DescripcionTipoFigura = descripcionTipoFigura;
    }
}
