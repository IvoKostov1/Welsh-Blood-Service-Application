package com.example.mythirdtry.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mythirdtry.Interface.AppointmentLoadListener;
import com.example.mythirdtry.R;
import com.example.mythirdtry.SpacesItemDecoration;
import com.example.mythirdtry.ui.Appointment;
import com.example.mythirdtry.ui.AppointmentAdapter;
import com.example.mythirdtry.ui.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;

public class BookingStep3Fragment extends Fragment implements AppointmentLoadListener {

    static BookingStep3Fragment instance;

    DocumentReference doctorRef;
    AppointmentLoadListener appointmentLoadListener;
    AlertDialog dialog;
    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_appointment)
    RecyclerView recycler_appointment;

    @BindView(R.id.calendar_view)
    HorizontalCalendarView calendarView;

    //This class allows date parsing into text
    SimpleDateFormat simpleDateFormat;

    //Received from Booking Activity
    BroadcastReceiver displayAppointment = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE, 0); //Adds current date
            loadAvailableAppointmentWithDoctor(Common.currentDoctor.getDoctor_id(),
                    simpleDateFormat.format(date.getTime()));
        }
    };

    public static BookingStep3Fragment getInstance()
    {
        if(instance == null)
        {
            instance = new BookingStep3Fragment();
        }
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appointmentLoadListener = this;

        //Setting up broadcast manager
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayAppointment,
                new IntentFilter(Common.KEY_DISPLAY_DOCTOR_TIMES));

        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
    }

    @Override
    public void onDestroy() {

        localBroadcastManager.unregisterReceiver(displayAppointment);

        super.onDestroy();
    }

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView =  inflater.inflate(R.layout.fragment_booking_step_three, container, false);

        unbinder = ButterKnife.bind(this, itemView);

        initialize(itemView);

        return itemView;
    }

    private void initialize(View itemView)
    {
        //Setting up recycler view for all available times
        recycler_appointment.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recycler_appointment.setLayoutManager(gridLayoutManager);
        recycler_appointment.addItemDecoration(new SpacesItemDecoration(8));

        //Start date
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.YEAR,  -1);

        //End date
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR, 1);

        //Setting up the calendar
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(itemView, R.id.calendar_view)
                .range(startDate, endDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                //This condition prevents the code running again if a new day with the same number is selected
                if(Common.bookingDate.getTimeInMillis() != date.getTimeInMillis())
                {
                    Common.bookingDate = date;
                    //Bookdate in the method below is equal to the second parameter
                    loadAvailableAppointmentWithDoctor(Common.currentDoctor.getDoctor_id(), simpleDateFormat.format(date.getTime()));
                }
            }
        });
    }

    private void loadAvailableAppointmentWithDoctor(String doctor_id, final String Bookdate)
    {
        dialog.show();

        doctorRef = FirebaseFirestore.getInstance()
                .collection("Clinics")
                .document(Common.city)
                .collection("moreClinics")
                .document(Common.currentClinic.getClinicId())
                .collection("Doctor")
                .document(Common.currentDoctor.getDoctor_id());

        doctorRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        CollectionReference date = FirebaseFirestore.getInstance()
                                .collection("Clinics")
                                .document(Common.city)
                                .collection("moreClinics")
                                .document(Common.currentClinic.getClinicId())
                                .collection("Doctor")
                                .document(Common.currentDoctor.getDoctor_id())
                                .collection(Bookdate);

                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();

                                    if (querySnapshot.isEmpty()) {
                                        appointmentLoadListener.onAppointmentEmpty();
                                    } else {
                                        List<Appointment> appointments = new ArrayList<>();

                                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                            appointments.add(queryDocumentSnapshot.toObject(Appointment.class));
                                        }
                                        appointmentLoadListener.onAppointmentSuccess(appointments);
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                appointmentLoadListener.onAppointmentFailed(e.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onAppointmentSuccess(List<Appointment> appointmentList) {

        AppointmentAdapter adapter = new AppointmentAdapter(getContext(), appointmentList);
        recycler_appointment.setAdapter(adapter);

        dialog.dismiss();

    }

    @Override
    public void onAppointmentFailed(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();

    }

    @Override
    public void onAppointmentEmpty() {

        AppointmentAdapter adapter = new AppointmentAdapter(getContext());
        recycler_appointment.setAdapter(adapter);

        dialog.dismiss();

    }
}
