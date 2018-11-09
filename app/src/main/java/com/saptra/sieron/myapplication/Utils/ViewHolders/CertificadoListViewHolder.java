package com.saptra.sieron.myapplication.Utils.ViewHolders;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.saptra.sieron.myapplication.R;
import com.saptra.sieron.myapplication.Utils.Interfaces.RecyclerViewClickListener;

public class CertificadoListViewHolder extends RecyclerView.ViewHolder {

    public TextView txvRow;
    public View parentView;

    public CertificadoListViewHolder(View itemView, final RecyclerViewClickListener recyclerViewClickListener){
        super(itemView);

        txvRow = (TextView) itemView.findViewById(R.id.txvRow);
        parentView = (RelativeLayout) itemView.findViewById(R.id.rlItem);

        parentView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    recyclerViewClickListener.onClick(view, position);
                }

            }
        });
    }
}
