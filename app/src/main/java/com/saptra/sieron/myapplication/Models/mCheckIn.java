package com.saptra.sieron.myapplication.Models;

public class mCheckIn {
    private int RowId;
    private int CheckInId;
    private String FechaCreacion;
    private int UsuarioCreacionId;
    private String Coordenadas;
    private dDetallePlanSemanal dDetallePlanSemanal;
    private String Incidencias;
    private String FotoIncidencia;
    private String ImageData;

    public mCheckIn(){
        RowId = 0;
        CheckInId = 0;
        UsuarioCreacionId = 0;
        dDetallePlanSemanal = new dDetallePlanSemanal();
    }

    public int getRowId() {
        return RowId;
    }

    public void setRowId(int rowId) {
        RowId = rowId;
    }

    public int getCheckInId() {
        return CheckInId;
    }

    public void setCheckInId(int checkInId) {
        CheckInId = checkInId;
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
}
