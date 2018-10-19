package com.saptra.sieron.myapplication.Models;

import java.util.List;

public class HttpListResponse<T> {
    private int Status;
    private List<T> Datos;
    private String Error;
    private String StackTrace;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public List<T> getDatos() {
        return Datos;
    }

    public void setDatos(List<T>  data) {
        Datos = data;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public String getStackTrace() {
        return StackTrace;
    }

    public void setStackTrace(String stackTrace) {
        StackTrace = stackTrace;
    }
}