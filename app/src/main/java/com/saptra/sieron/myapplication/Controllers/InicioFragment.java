package com.saptra.sieron.myapplication.Controllers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saptra.sieron.myapplication.Models.mUsuarios;
import com.saptra.sieron.myapplication.R;

public class InicioFragment extends Fragment {

    //Controls
    TextView txvTipoFigura, txvNombre, txvEmail;

    //Other
    mUsuarios user;

    public InicioFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        //Instancias
        //*********************************************
        user = new mUsuarios();
        user = getLoggedUser();

        txvTipoFigura = (TextView) view.findViewById(R.id.txvTipoFigura);
        txvNombre = (TextView) view.findViewById(R.id.txvNombre);
        txvEmail = (TextView) view.findViewById(R.id.txvEmail);

        //Iniciar perfil
        txvTipoFigura.setText(user.getTipoFiguras().getDescripcionTipoFigura());
        txvNombre.setText(user.getNombresUsuario()+" "+user.getApellidosUsuario());
        txvEmail.setText(user.getEmailUsuario());

        //*********************************************
        return view;
    }

    //Validar si ya se ha iniciado sesion, buscar flag en SharedPreferences
    private mUsuarios getLoggedUser(){
        mUsuarios user = new mUsuarios();
        String sharedPreferences = getResources().getString(R.string.SATRA_PREFERENCES);
        boolean logged;
        SharedPreferences settings = getActivity()
                .getSharedPreferences(sharedPreferences, getActivity().MODE_PRIVATE);
        logged = settings.getBoolean(getResources().getString(R.string.sp_Logged), false);
        user.setUsuarioId(
                settings.getInt(getResources().getString(R.string.sp_UsuarioId),0)
        );
        user.setNombresUsuario(
                settings.getString(getResources().getString(R.string.sp_NombresUsuario),"")
        );
        user.setApellidosUsuario(
                settings.getString(getResources().getString(R.string.sp_ApellidosUsuario),"")
        );
        user.setEmailUsuario(
                settings.getString(getResources().getString(R.string.sp_EmailUsuario),"")
        );
        user.getTipoFiguras().setDescripcionTipoFigura(
                settings.getString(getResources().getString(R.string.sp_TipoFigura),"")
        );
        return user;
    }

}
