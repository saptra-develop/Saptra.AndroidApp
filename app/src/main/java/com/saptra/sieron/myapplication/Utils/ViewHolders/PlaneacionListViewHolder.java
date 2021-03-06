package com.saptra.sieron.myapplication.Utils.ViewHolders;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.widget.FloatingActionButton;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.Interfaces.PlaneacionViewListener;

import org.w3c.dom.Text;

public class PlaneacionListViewHolder extends RecyclerView.ViewHolder {

    public TextInputLayout tilActividad, tilDescripcion, tilFecha, tilHora, tilCheckIn, tilLugar;
    public TextView txvPeriodo, txvEstatusEnvio;
    public Button btnCertificados;
    public FloatingActionButton btnCheck;
    public View parentView, llyCertificados, llyEstatusEnvio;

    public PlaneacionListViewHolder(View itemView, final PlaneacionViewListener planeacionViewListener) {
        super(itemView);

        txvPeriodo = (TextView) itemView.findViewById(R.id.txvPeriodo);
        tilActividad = (TextInputLayout) itemView.findViewById(R.id.tilActividad);
        tilDescripcion = (TextInputLayout) itemView.findViewById(R.id.tilDescripcion);
        tilFecha = (TextInputLayout) itemView.findViewById(R.id.tilFecha);
        tilHora = (TextInputLayout) itemView.findViewById(R.id.tilHora);
        tilCheckIn = (TextInputLayout) itemView.findViewById(R.id.tilCheckIn);
        tilLugar = (TextInputLayout) itemView.findViewById(R.id.tilLugar);
        btnCheck = (FloatingActionButton) itemView.findViewById(R.id.btnCheck);
        btnCertificados = (Button) itemView.findViewById(R.id.btnCertificados);
        parentView = (RelativeLayout) itemView.findViewById(R.id.rlItem);
        llyCertificados = itemView.findViewById(R.id.llyCertificado);
        llyEstatusEnvio = itemView.findViewById(R.id.llyEstatusenvio);
        txvEstatusEnvio = (TextView) itemView.findViewById(R.id.txvEstatusEnvio);

        llyCertificados.setVisibility(View.GONE);
        parentView = itemView;

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    planeacionViewListener.ButtonClick(view, position);
                }
            }
        });

        btnCertificados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                    planeacionViewListener.CertificadoClick(view, position);
            }
        });
    }
}