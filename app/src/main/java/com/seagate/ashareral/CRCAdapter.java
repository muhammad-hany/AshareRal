package com.seagate.ashareral;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CRCAdapter extends RecyclerView.Adapter<CRCAdapter.CRCViewHolder> {


    private ArrayList<CRC> crcs;
    View.OnClickListener listener;
    public CRCAdapter(ArrayList<CRC> crcs, View.OnClickListener listener) {
        this.crcs=crcs;
        this.listener=listener;
    }

    @NonNull
    @Override
    public CRCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.crc_list_item,parent
                ,false);

        CRCAdapter.CRCViewHolder holder=new CRCViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CRCViewHolder holder, int position) {
        holder.title.setText(crcs.get(position).getTitle());
        Picasso.get().load(crcs.get(position).getCoverimageDownloadLink()).placeholder(R.drawable.placeholder).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return crcs.size();
    }

    public class CRCViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title;
        public CRCViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.coverImage);
            title=itemView.findViewById(R.id.title);
            itemView.setTag(this);
            itemView.setOnClickListener(listener);
        }
    }

}
