package com.example.rural_healthy_mom_to_be.Model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rural_healthy_mom_to_be.R;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {
    private Hospital[] listdata;

    // RecyclerView recyclerView;
    public HospitalAdapter(Hospital[] listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.recycler_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Hospital myListData = listdata[position];
        holder.name.setText(listdata[position].getHospitalName());
        holder.address.setText("Address: "+listdata[position].getHosAddress());

        holder.distance.setText("Distance from current location: "+String.format("%.1f",listdata[position].getDistanceFromCur())+"");
//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView address;
        public TextView distance;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.address = (TextView) itemView.findViewById(R.id.address);
            this.distance =(TextView) itemView.findViewById(R.id.distance);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.recycler_layout);
        }
    }
}
