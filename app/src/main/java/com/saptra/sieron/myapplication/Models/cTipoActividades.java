package com.saptra.sieron.myapplication.Models;

public class cTipoActividades {
    private int TipoActividadId;
    private String FechaCreacion;
    private int UsuarioCreacion;
    private int EstatusId;
    private String NombreActividad;
    private String DescripcionActividad;
    private boolean RequiereCheckIn;
    private boolean ActividadEspecial;
    private String Mensaje;

    cTipoActividades(){

    }

    public int getTipoActividadId() {
        return TipoActividadId;
    }

    public void setTipoActividadId(int tipoActividadId) {
        TipoActividadId = tipoActividadId;
    }

    public String getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        FechaCreacion = fechaCreacion;
    }

    public int getUsuarioCreacion() {
        return UsuarioCreacion;
    }

    public void setUsuarioCreacion(int usuarioCreacion) {
        UsuarioCreacion = usuarioCreacion;
    }

    public int getEstatusId() {
        return EstatusId;
    }

    public void setEstatusId(int estatusId) {
        EstatusId = estatusId;
    }

    public String getNombreActividad() {
        return NombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        NombreActividad = nombreActividad;
    }

    public String getDescripcionActividad() {
        return DescripcionActividad;
    }

    public void setDescripcionActividad(String descripcionActividad) {
        DescripcionActividad = descripcionActividad;
    }

    public boolean getRequiereCheckIn() {
        return RequiereCheckIn;
    }

    public void setRequiereCheckIn(boolean requiereCheckIn) {
        RequiereCheckIn = requiereCheckIn;
    }

    public boolean getActividadEspecial() {
        return ActividadEspecial;
    }

    public void setActividadEspecial(boolean actividadEspecial) {
        ActividadEspecial = actividadEspecial;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }
}
