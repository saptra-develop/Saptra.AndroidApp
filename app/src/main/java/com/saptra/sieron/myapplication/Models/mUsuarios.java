package com.saptra.sieron.myapplication.Models;

public class mUsuarios {

    private int UsuarioId;
    private String FechaCreacion;
    private int EstatusId;
    private String LoginUsuario;
    private String PasswordUsuario;
    private String NombresUsuario;
    private String ApellidosUsuario;
    private cTipoFiguras cTipoFiguras1;
    private String EmailUsuario;
    private boolean LoggedUsuario;

    public mUsuarios(){
        UsuarioId = 0;
        EstatusId = 0;
        cTipoFiguras1 = new cTipoFiguras();
        LoggedUsuario = false;
    }

    public int getUsuarioId() {
        return UsuarioId;
    }

    public void setUsuarioId(int UsuarioId) {
        this.UsuarioId = UsuarioId;
    }

    public String getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(String FechaCreacion) {
        this.FechaCreacion = FechaCreacion;
    }

    public int getEstatusId() {
        return EstatusId;
    }

    public void setEstatusId(int EstatusId) {
        this.EstatusId = EstatusId;
    }

    public String getLoginUsuario() {
        return LoginUsuario;
    }

    public void setLoginUsuario(String LoginUsuario) {
        this.LoginUsuario = LoginUsuario;
    }

    public String getPasswordUsuario() {
        return PasswordUsuario;
    }

    public void setPasswordUsuario(String PasswordUsuario) {
        this.PasswordUsuario = PasswordUsuario;
    }

    public String getNombresUsuario() {
        return NombresUsuario;
    }

    public void setNombresUsuario(String NombresUsuario) {
        this.NombresUsuario = NombresUsuario;
    }

    public String getApellidosUsuario() {
        return ApellidosUsuario;
    }

    public void setApellidosUsuario(String ApellidosUsuario) {
        this.ApellidosUsuario = ApellidosUsuario;
    }

    public String getEmailUsuario() {
        return EmailUsuario;
    }

    public void setEmailUsuario(String EmailUsuario) {
        this.EmailUsuario = EmailUsuario;
    }

    public cTipoFiguras getTipoFiguras() {
        return cTipoFiguras1;
    }

    public void setTipoFiguras(cTipoFiguras cTipoFiguras1) {
        this.cTipoFiguras1 = cTipoFiguras1;
    }

    public boolean getLoggedUsuario() {
        return LoggedUsuario;
    }

    public void setLoggedUsuario(boolean loggedUsuario) {
        LoggedUsuario = loggedUsuario;
    }
}
