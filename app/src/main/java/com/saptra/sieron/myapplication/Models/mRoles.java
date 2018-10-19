package com.saptra.sieron.myapplication.Models;

public class mRoles {
    private int RolId;
    private String FechaCreacion;
    private int EstatusId;
    private String NombreRol;

    public mRoles(){
        RolId = 0;
        EstatusId = 0;
    }

    public int getRolId() {
        return RolId;
    }

    public void setRolId(int rolId) {
        RolId = rolId;
    }

    public String getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        FechaCreacion = fechaCreacion;
    }

    public int getEstatusId() {
        return EstatusId;
    }

    public void setEstatusId(int estatusId) {
        EstatusId = estatusId;
    }

    public String getNombreRol() {
        return NombreRol;
    }

    public void setNombreRol(String nombreRol) {
        NombreRol = nombreRol;
    }
}
