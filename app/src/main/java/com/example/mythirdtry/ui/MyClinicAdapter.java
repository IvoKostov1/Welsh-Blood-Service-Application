package com.example.mythirdtry.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mythirdtry.Interface.RecyclerItemSelectedListener;
import com.example.mythirdtry.R;

import java.util.ArrayList;
import java.util.List;

public class MyClinicAdapter extends RecyclerView.Adapter<MyClinicAdapter.MyViewHolder> {

    Context context;
    List<Addresses> clinicList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyClinicAdapter(Context context, List<Addresses> clinicList) {
        this.context = context;
        this.clinicList = clinicList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_clinic_name;
        CardView card_clinics;

        RecyclerItemSelectedListener recyclerItemSelectedListener;

        public void setRecyclerItemSelectedListener(RecyclerItemSelectedListener recyclerItemSelectedListener) {
            this.recyclerItemSelectedListener = recyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_clinic_name = (TextView)itemView.findViewById(R.id.txt_clinic_name);
            card_clinics = (CardView)itemView.findViewById(R.id.card_clinic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate
                (R.layout.layout_address, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.txt_clinic_name.setText(clinicList.get(position).getName());

        //cardViewList will be filled up with CardViews of the clinics
        //card_clinics contains the names of the clinics
        if(!cardViewList.contains(holder.card_clinics))
        {
            cardViewList.add(holder.card_clinics);
        }

        //This method is executed when the user clicks on a CardView
        holder.setRecyclerItemSelectedListener(new RecyclerItemSelectedListener() {
            @Override
            //set the background color of each cardview to white
            public void onItemSelectedListener(View view, int pos) {
                for(CardView cardView:cardViewList)
                {
                    cardView.setCardBackgroundColor(context.getResources().getColor
                            (android.R.color.white, view.getContext().getTheme()));
                }

                //Then change the color of the selected cardview the orange
                holder.card_clinics.setCardBackgroundColor(context.getResources().
                        getColor(android.R.color.holo_orange_dark, view.getContext().getTheme()));

                //Sends a broadcast to Booking Activity
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_CLINIC_STORAGE, clinicList.get(pos));
                intent.putExtra(Common.KEY_STEP, 1);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clinicList.size();
    }
}
