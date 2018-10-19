package com.saptra.sieron.myapplication.Controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rey.material.widget.Button;
import com.rey.material.widget.SnackBar;
import com.saptra.sieron.myapplication.Models.HttpObjectResponse;
import com.saptra.sieron.myapplication.Models.mUsuarios;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.Globals;
import com.saptra.sieron.myapplication.Utils.Interfaces.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout tilUsuario;
    TextInputLayout tilContrasena;
    Button btnLogin;
    CoordinatorLayout clMensajes;

    private Retrofit mRestAdapter;
    private ServiceApi ServicioApi;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRestAdapter = new Retrofit.Builder()
                .baseUrl(ServiceApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServicioApi = mRestAdapter.create(ServiceApi.class);

        tilUsuario = (TextInputLayout) findViewById(R.id.tilUsuario);
        tilContrasena = (TextInputLayout) findViewById(R.id.tilContrasena);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        clMensajes = (CoordinatorLayout) findViewById(R.id.clMensajes);
        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        pbLoading.setVisibility(View.GONE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplication(), HomeActivity.class));
                if(!Globals.getInstance().isOnline(getApplication())){
                    Snackbar.make(clMensajes,
                            "Sin conexión a internet",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }
                ResetearCampos();
                if(!ValidarLogin()) {
                    pbLoading.setVisibility(View.GONE);
                    return;
                }
                //Validar conexion a internet
                if(!Globals.getInstance().isOnline(getApplicationContext())){
                    pbLoading.setVisibility(View.GONE);
                    Snackbar.make(clMensajes,
                            "Sin conexión a internet",
                            Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }


                ValidarSesion();
            }
        });
    }

    //Obtener sesion de usuario
    private void ValidarSesion() {
        String usuario = tilUsuario.getEditText().getText().toString();
        String contrasena = tilContrasena.getEditText().getText().toString();
        mUsuarios loginUsuario = new mUsuarios();
        loginUsuario.setLoginUsuario(usuario);
        loginUsuario.setPasswordUsuario(contrasena);
        //************************************************************
        try {
            Call<HttpObjectResponse<mUsuarios>> _loginUsuario = ServicioApi.GetLogin(loginUsuario);
            _loginUsuario.enqueue(new Callback<HttpObjectResponse<mUsuarios>>() {
                @Override
                public void onResponse(Call<HttpObjectResponse<mUsuarios>> call, Response<HttpObjectResponse<mUsuarios>> response) {
                    //progressDialog.dismiss();
                    pbLoading.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        //Si respuesta correcta, obener datos
                        if (response.body().getDatos().getUsuarioId() > 0) {
                            //Guardar datos de cobrador en SharedPreferences
                            SaveSharedPreferencesUsuario(response.body().getDatos());
                            Toast.makeText(getApplication(),
                                    "Bienvenido " + response.body().getDatos().getNombresUsuario(),
                                    Toast.LENGTH_LONG).show();
                            //Mostrar Pantalla Principal
                            Intent intent = new Intent(getApplicationContext(),
                                    HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Snackbar.make(clMensajes, "Usuario y/o constraseña incorrectos"
                                    , Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        String msj = "";
                        switch (response.code()) {
                            case 400:
                                msj = "Error, Servicio no encontrado.";
                                break;
                            case 500:
                                msj = "Error, El servidor no responde, por favor vuelva a intentarlo.";
                                break;
                            default:
                                msj = "Error, servicio no disponible.";
                                break;
                        }
                        Snackbar.make(clMensajes, msj
                                , Snackbar.LENGTH_INDEFINITE)
                                .setAction("ACEPTAR", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                }).show();
                    }
                }

                @Override
                public void onFailure(Call<HttpObjectResponse<mUsuarios>> call, Throwable t) {
                    pbLoading.setVisibility(View.GONE);
                    Snackbar.make(clMensajes, "Error de conexión, por favor vuelva a intentarlo."
                            , Snackbar.LENGTH_INDEFINITE)
                            .setAction("ACEPTAR", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            }).show();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Error login", ex.getMessage());
        }
    }

    private void ResetearCampos(){
        tilUsuario.setErrorEnabled(false);
        tilContrasena.setErrorEnabled(false);
    }

    private boolean ValidarLogin(){
        boolean validacion = true;
        String usuario, contrasena;
        usuario = tilUsuario.getEditText().getText().toString();
        contrasena = tilContrasena.getEditText().getText().toString();

        if(usuario.equals("")){
            tilUsuario.setErrorEnabled(true);
            tilUsuario.setError("Ingrese usuario");
            validacion = false;
        }
        if(contrasena.equals("")){
            tilContrasena.setErrorEnabled(true);
            tilContrasena.setError("Ingrese Contraseña");
            validacion = false;
        }
        return validacion;
    }

    private void SaveSharedPreferencesUsuario(mUsuarios usuario){
        String sharedPref = getString(R.string.SATRA_PREFERENCES);
        SharedPreferences settings = getApplication().getSharedPreferences(sharedPref,
                getApplication().MODE_PRIVATE);
        settings.edit().clear().commit();
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(getString(R.string.sp_UsuarioId), usuario.getUsuarioId());
        editor.putString(getString(R.string.sp_ApellidosUsuario), usuario.getNombresUsuario());
        editor.putString(getString(R.string.sp_EmailUsuario), usuario.getEmailUsuario());
        editor.putString(getString(R.string.sp_RolUsuario), usuario.getRoles().getNombreRol());
        editor.putBoolean(getString(R.string.sp_Logged), true);
        editor.commit();
    }
}
