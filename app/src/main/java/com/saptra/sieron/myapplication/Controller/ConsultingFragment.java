package com.saptra.sieron.myapplication.Controller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saptra.sieron.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConsultingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConsultingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultingFragment extends Fragment {
    public ConsultingFragment() {

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
