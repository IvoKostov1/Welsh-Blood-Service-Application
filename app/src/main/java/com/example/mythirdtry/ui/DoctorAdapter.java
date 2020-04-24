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

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.MyViewHolder> {

    Context context;
    List<Doctor> doctorList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public DoctorAdapter(Context context, List<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_doctor, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txt_doctor_name.setText(doctorList.get(position).getName());

        if(!cardViewList.contains(holder.card_doctor))
        {
            cardViewList.add(holder.card_doctor);
        }

        holder.setRecyclerItemSelectedListener(new RecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {

                //Setting the background color for all choices which are not selected
                for(CardView cardView : cardViewList)
                {
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white, context.getTheme()));
                }

                //Setting background color for the choice which is selected
                holder.card_doctor.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark, context.getTheme()));

                //Sending local broadcast to enable "Next" button
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_DOCTOR_SELECTED, doctorList.get(pos));
                intent.putExtra(Common.KEY_STEP, 2);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView txt_doctor_name;
        CardView card_doctor;

        RecyclerItemSelectedListener recyclerItemSelectedListener;

        public void setRecyclerItemSelectedListener(RecyclerItemSelectedListener recyclerItemSelectedListener) {
            this.recyclerItemSelectedListener = recyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_doctor_name = (TextView)itemView.findViewById(R.id.txt_doctor_name);
            card_doctor = (CardView)itemView.findViewById(R.id.card_doctor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
                }
            });
        }
    }
}
