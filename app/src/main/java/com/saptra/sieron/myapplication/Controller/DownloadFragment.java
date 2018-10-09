package com.saptra.sieron.myapplication.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dd.CircularProgressButton;
import com.saptra.sieron.myapplication.R;

public class DownloadFragment extends Fragment {

    CircularProgressButton btnDownload;
    public DownloadFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        //Instancias
        //*********************************************
        //btnDownload = (CircularProgressButton) view.findViewById(R.id.btnDownload);
        /*btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDownload.setIndeterminateProgressMode(true);
            }
        });*/
        //*********************************************
        return view;
    }
}
