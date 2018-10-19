package com.saptra.sieron.myapplication.Utils.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saptra.sieron.myapplication.Models.cPeriodos;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.Interfaces.RecyclerViewClickListener;
import com.saptra.sieron.myapplication.Utils.ViewHolders.SelectListViewHolder;

import java.util.List;

public class SelectListAdapter extends RecyclerView.Adapter<SelectListViewHolder> {
    private Context c;
    private List<cPeriodos> lstPeriodos;

    private RecyclerViewClickListener recyclerViewClickListener;

    public void setRecyclerViewItemClickListener(RecyclerViewClickListener recyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener;
    }


    public SelectListAdapter(Context c, List<cPeriodos> lstCliente){
        this.c = c;
        this.lstPeriodos = lstCliente;
    }

    @Override
    public SelectListViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Log.d("TRANCER", "en onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_row, parent, false);
        return new SelectListViewHolder(v, recyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(final SelectListViewHolder holder, final int position){
        Log.d("TRANCER", "en onBindViewHolder**");
        //BIND DATA
        holder.txvRow.setText(lstPeriodos.get(position).getDecripcionPeriodo());
    }

    @Override
    public int getItemCount(){
        Log.d("TRANCER", "en getItemCount");
        if(lstPeriodos != null)
            return lstPeriodos.size();
        else
            return 0;
    }
}
