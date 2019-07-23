package com.saptra.sieron.myapplication.Controllers;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.appolica.flubber.Flubber;
import com.saptra.sieron.myapplication.Models.mUsuarios;
import com.saptra.sieron.myapplication.R;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    //Controls
    private View imvLogo;
    //Others
    private static final int SPLASH_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mUsuarios user = new mUsuarios();
        user = getLoggedUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imvLogo =  findViewById(R.id.imvLogo);

        Flubber.with()
                .animation(Flubber.AnimationPreset.ZOOM_IN)
                .interpolator(Flubber.Curve.SPRING)
                .repeatCount(1)
                .duration(4000)
                .createFor(imvLogo)
                .start();

        final Intent intent = new Intent(this,
                user.getLoggedUsuario() ? HomeActivity.class : LoginActivity.class);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //Efecto a imagen
                startActivity(intent);
                finish();
            }
        };
        new Timer().schedule(task, SPLASH_DELAY);
    }

    //Validar si ya se ha iniciado sesion, buscar flag en SharedPreferences
    private mUsuarios getLoggedUser(){
        mUsuarios user = new mUsuarios();
        String sharedPreferences = getResources().getString(R.string.SATRA_PREFERENCES);
        boolean logged;
        SharedPreferences settings = getApplication()
                .getSharedPreferences(sharedPreferences, getApplication().MODE_PRIVATE);
        user.setLoggedUsuario(settings.getBoolean(getResources().getString(R.string.sp_Logged), false));
        user.setUsuarioId(
                settings.getInt(getResources().getString(R.string.sp_UsuarioId),0)
        );
        return user;
    }
}
