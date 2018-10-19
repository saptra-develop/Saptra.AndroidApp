package com.saptra.sieron.myapplication.Models;

public class HttpObjectResponse<T> {
    private int Status;
    private T Datos;
    private String Error;
    private String StackTrace;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public T getDatos() {
        return Datos;
    }

    public void setDatos(T data) {
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