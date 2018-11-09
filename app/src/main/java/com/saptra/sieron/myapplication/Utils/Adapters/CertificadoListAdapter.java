package com.saptra.sieron.myapplication.Utils.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.saptra.sieron.myapplication.Models.mLecturaCertificados;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.Interfaces.RecyclerViewClickListener;
import com.saptra.sieron.myapplication.Utils.ViewHolders.CertificadoListViewHolder;

import java.util.List;

public class CertificadoListAdapter extends RecyclerView.Adapter<CertificadoListViewHolder> {
    private Context c;
    private List<mLecturaCertificados> lstCertificados;

    private RecyclerViewClickListener recyclerViewClickListener;

    public void setRecyclerViewItemClickListener(RecyclerViewClickListener recyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    public CertificadoListAdapter(Context c, List<mLecturaCertificados> lstCertificados){
        this.c = c;
        this.lstCertificados = lstCertificados;
    }

    @Override
    public CertificadoListViewHolder  onCreateViewHolder(ViewGroup parent, int viewType){
        Log.d("TRANCER", "en onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_row, parent, false);
        return new CertificadoListViewHolder(v, recyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(final CertificadoListViewHolder holder, final int position){
        Log.d("TRANCER", "en onBindViewHolder**");
        //BIND DATA
        holder.txvRow.setText(lstCertificados.get(position).getFolioCertificado());
    }

    @Override
    public int getItemCount(){
        Log.d("TRANCER", "en getItemCount");
        if(lstCertificados != null)
            return lstCertificados.size();
        else
            return 0;
    }
}