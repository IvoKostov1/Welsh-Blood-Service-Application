package com.example.mythirdtry.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mythirdtry.R;
import com.example.mythirdtry.SpacesItemDecoration;
import com.example.mythirdtry.ui.Common;
import com.example.mythirdtry.ui.Doctor;
import com.example.mythirdtry.ui.DoctorAdapter;
import java.util.ArrayList;
import javax.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep2Fragment extends Fragment {

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_doctor)
    RecyclerView recycler_doctor;

    //This broadcast is received from Booking Activity.
    //It is sent when all doctor info has been received from Firestore
    //It is displayed in this fragment with a recycler view
    private BroadcastReceiver doctorDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Doctor> doctorsArrayList = intent.getParcelableArrayListExtra(Common.KEY_DOCTOR_LOAD_DONE);

            //Adapter
            DoctorAdapter adapter = new DoctorAdapter(getContext(), doctorsArrayList);
            recycler_doctor.setAdapter(adapter);
        }
    };

    static BookingStep2Fragment instance;

    public static BookingStep2Fragment getInstance()
    {
        if(instance == null)
        {
            instance = new BookingStep2Fragment();
        }
        return instance;
    }

    //Setting up the broadcast manager
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(doctorDoneReceiver, new IntentFilter(Common.KEY_DOCTOR_LOAD_DONE));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(doctorDoneReceiver);
        super.onDestroy();
    }

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @androidx.annotation.Nullable ViewGroup container,
                             @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //Setting the view to be UI of step 2
        View itemView = inflater.inflate(R.layout.fragment_booking_step_two, container, false);

        //Binds the view
        unbinder = ButterKnife.bind(this, itemView);

        //The recycler view is initialized
        intView();

        return itemView;
    }

    private void intView()
    {
        recycler_doctor.setHasFixedSize(true);
        recycler_doctor.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_doctor.addItemDecoration(new SpacesItemDecoration(4));
    }
}
