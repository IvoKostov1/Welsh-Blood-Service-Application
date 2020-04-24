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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder> {

    Context context;
    List<Appointment> appointmentList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;


    public AppointmentAdapter(Context context, List<Appointment> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        this.cardViewList = new ArrayList<>();
    }

    public AppointmentAdapter(Context context) {
        this.context = context;
        this.appointmentList = new ArrayList<>();
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        this.cardViewList = new ArrayList<>();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_time_slot, txt_availability;
        CardView card_appointment;
        RecyclerItemSelectedListener recyclerItemSelectedListener;

        public void setRecyclerItemSelectedListener(RecyclerItemSelectedListener recyclerItemSelectedListener) {
            this.recyclerItemSelectedListener = recyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_appointment = (CardView)itemView.findViewById(R.id.card_appointment);
            txt_time_slot = (TextView) itemView.findViewById(R.id.txt_time_slot);
            txt_availability = (TextView)itemView.findViewById(R.id.txt_appointment_availability);

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
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_appointment_time, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.txt_time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(position)));

        //If all times are available, the whole list is shown
        if(appointmentList.size() == 0)
        {
            holder.card_appointment.setTag(Common.DISABLE_TAG);

            holder.card_appointment.setCardBackgroundColor(context.getResources().getColor(android.R.color.white, context.getTheme()));
            holder.txt_availability.setText("Available");
            holder.txt_availability.setTextColor(context.getResources().getColor(android.R.color.black, context.getTheme()));
            holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black, context.getTheme()));
        }

        //If position are full, loop through all time slots and set different color
        else
        {
            for(Appointment appointmentValue: appointmentList)
            {
                int slot = Integer.parseInt(appointmentValue.getSlot());

                //if the slot is equal to the current adapter position:
                if(slot == position)
                {
                    holder.card_appointment.setCardBackgroundColor(context.getResources().getColor(android.R.color.darker_gray, context.getTheme()));
                    holder.txt_availability.setText("Full");
                    holder.txt_availability.setTextColor(context.getResources().getColor(android.R.color.white, context.getTheme()));
                    holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.white, context.getTheme()));
                }
            }
        }

        //Adding all appointment times to cardViewList
        if(!cardViewList.contains(holder.card_appointment))
        {
            cardViewList.add(holder.card_appointment);
        }

        //Checking if appointment time is available
        holder.setRecyclerItemSelectedListener(new RecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //Loop through all cards in the card list to set white color
                for(CardView cardView : cardViewList)
                {
                    cardView.setBackgroundColor(context.getResources().getColor(android.R.color.white, context.getTheme()));
                }

                //The selected card will be with a different color
                holder.card_appointment.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark, context.getTheme()));

                //Now, a broadcast is send to enable button "Next"
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_APPOINTMENT, pos);
                intent.putExtra(Common.KEY_STEP,3 );
                localBroadcastManager.sendBroadcast(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Common.APPOINTMENT_TOTAL;
    }
}
