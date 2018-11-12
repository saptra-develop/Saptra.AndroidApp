package com.saptra.sieron.myapplication.Models;

public class mCheckIn {
    private int RowId;
    private String CheckInId;
    private String FechaCreacion;
    private int UsuarioCreacionId;
    private String Coordenadas;
    private dDetallePlanSemanal dDetallePlanSemanal;
    private String Incidencias;
    private String FotoIncidencia;
    private String ImageData;
    private String State;
    private String UUID;

    public mCheckIn(){
        RowId = 0;
        UsuarioCreacionId = 0;
        dDetallePlanSemanal = new dDetallePlanSemanal();
    }

    public int getRowId() {
        return RowId;
    }

    public void setRowId(int rowId) {
        RowId = rowId;
    }

    public String getCheckInId() {
        return CheckInId;
    }

    public void setCheckInId(String CheckInId) {
        this.CheckInId = CheckInId;
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

    public String getCoordenadas() {
        return Coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        Coordenadas = coordenadas;
    }

    public dDetallePlanSemanal getDetallePlan() {
        return dDetallePlanSemanal;
    }

    public void setDetallePlan(dDetallePlanSemanal dDetallePlanSemanal) {
        this.dDetallePlanSemanal = dDetallePlanSemanal;
    }

    public String getIncidencias() {
        return Incidencias;
    }

    public void setIncidencias(String incidencias) {
        Incidencias = incidencias;
    }

    public String getFotoIncidencia() {
        return FotoIncidencia;
    }

    public void setFotoIncidencia(String fotoIncidencia) {
        FotoIncidencia = fotoIncidencia;
    }

    public String getImageData() { return ImageData; }

    public void setImageData(String ImageData) { this.ImageData = ImageData; }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
}
