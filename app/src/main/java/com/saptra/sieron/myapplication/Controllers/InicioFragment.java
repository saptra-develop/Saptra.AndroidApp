package com.saptra.sieron.myapplication.Controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saptra.sieron.myapplication.R;

public class InicioFragment extends Fragment {

    public InicioFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        //Instancias
        //*********************************************
        //*********************************************
        return view;
    }

}
