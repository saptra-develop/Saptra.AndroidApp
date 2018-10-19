package com.saptra.sieron.myapplication.Utils.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.widget.FloatingActionButton;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.Interfaces.PlaneacionViewListener;

public class PlaneacionListViewHolder extends RecyclerView.ViewHolder {

    public TextView txvPeriodo, txvActividad, txvDescripcion, txvFecha, txvHora;
    public FloatingActionButton btnCheck;
    public View parentView;

    public PlaneacionListViewHolder(View itemView, final PlaneacionViewListener planeacionViewListener) {
        super(itemView);

        txvPeriodo = (TextView) itemView.findViewById(R.id.txvPeriodo);
        txvActividad = (TextView) itemView.findViewById(R.id.txvActividad);
        txvDescripcion = (TextView) itemView.findViewById(R.id.txvDescripcion);
        txvFecha = (TextView) itemView.findViewById(R.id.txvFecha);
        txvHora = (TextView) itemView.findViewById(R.id.txvHora);
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

        txvDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    planeacionViewListener.DetalleClick(view, position);
                }
            }
        });
    }
}