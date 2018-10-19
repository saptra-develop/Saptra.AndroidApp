package com.saptra.sieron.myapplication.Models;

public class mUsuarios {

    private int UsuarioId;
    private String FechaCreacion;
    private int EstatusId;
    private String LoginUsuario;
    private String PasswordUsuario;
    private String NombresUsuario;
    private String ApellidosUsuario;
    private mRoles Roles;
    private String EmailUsuario;
    private cTipoFiguras cTipoFiguras1;

    public mUsuarios(){
        Roles = new mRoles();
        cTipoFiguras1 = new cTipoFiguras();
    }

    public int getUsuarioId() {
        return UsuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        UsuarioId = usuarioId;
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

    public String getLoginUsuario() {
        return LoginUsuario;
    }

    public void setLoginUsuario(String loginUsuario) {
        LoginUsuario = loginUsuario;
    }

    public String getPasswordUsuario() {
        return PasswordUsuario;
    }

    public void setPasswordUsuario(String passwordUsuario) {
        PasswordUsuario = passwordUsuario;
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

    public void setApellidosUsuario(String apellidosUsuario) {
        ApellidosUsuario = apellidosUsuario;
    }

    public mRoles getRoles() {
        return Roles;
    }

    public void setRoles(mRoles mRoles) {
        Roles = Roles;
    }

    public String getEmailUsuario() {
        return EmailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        EmailUsuario = emailUsuario;
    }

    public cTipoFiguras getTipoFiguras() {
        return cTipoFiguras1;
    }

    public void setTipoFiguras(cTipoFiguras cTipoFiguras1) {
        this.cTipoFiguras1 = cTipoFiguras1;
    }
}
