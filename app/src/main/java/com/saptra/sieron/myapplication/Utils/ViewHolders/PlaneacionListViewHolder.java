package com.saptra.sieron.myapplication.Utils.ViewHolders;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.widget.FloatingActionButton;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.Interfaces.PlaneacionViewListener;

public class PlaneacionListViewHolder extends RecyclerView.ViewHolder {

    public TextInputLayout tilActividad, tilDescripcion, tilFecha, tilHora;
    public FloatingActionButton btnCheck;
    public View parentView;

    public PlaneacionListViewHolder(View itemView, final PlaneacionViewListener planeacionViewListener) {
        super(itemView);

        tilActividad = (TextInputLayout) itemView.findViewById(R.id.tilActividad);
        tilDescripcion = (TextInputLayout) itemView.findViewById(R.id.tilDescripcion);
        tilFecha = (TextInputLayout) itemView.findViewById(R.id.tilFecha);
        tilHora = (TextInputLayout) itemView.findViewById(R.id.tilHora);
        btnCheck = (FloatingActionButton) itemView.findViewById(R.id.btnCheck);
        parentView = (RelativeLayout) itemView.findViewById(R.id.rlItem);

        parentView = itemView;

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    planeacionViewListener.ButonClick(view, position);
                }
            }
        });

        /*txvDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    planeacionViewListener.DetalleClick(view, position);
                }
            }
        });*/
    }
}