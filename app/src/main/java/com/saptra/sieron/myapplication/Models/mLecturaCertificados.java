package com.saptra.sieron.myapplication.Models;

public class mLecturaCertificados {
    private int LecturaCertificadoId;
    private String FechaCreacion;
    private int UsuarioCreacionId;
    private String FolioCertificado;
    private int EstatusId;
    private mCheckIn CheckInId;

    public mLecturaCertificados(){
        LecturaCertificadoId = 0;
        UsuarioCreacionId = 0;
        CheckInId = new mCheckIn();
    }

    public int getLecturaCertificadoId() {
        return LecturaCertificadoId;
    }

    public void setLecturaCertificadoId(int lecturaCertificadoId) {
        LecturaCertificadoId = lecturaCertificadoId;
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

    public String getFolioCertificado() {
        return FolioCertificado;
    }

    public void setFolioCertificado(String folioCertificado) {
        FolioCertificado = folioCertificado;
    }

    public int getEstatusId() {
        return EstatusId;
    }

    public void setEstatusId(int estatusId) {
        EstatusId = estatusId;
    }

    public mCheckIn getCheckIn() {
        return CheckInId;
    }

    public void setCheckIn(mCheckIn checkInId) {
        CheckInId = checkInId;
    }
}
