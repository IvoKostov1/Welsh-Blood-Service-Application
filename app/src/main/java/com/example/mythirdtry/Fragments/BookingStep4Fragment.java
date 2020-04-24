package com.example.mythirdtry.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.mythirdtry.R;
import com.example.mythirdtry.ui.BookingInformation;
import com.example.mythirdtry.ui.Common;
import com.example.mythirdtry.ui.DatabaseHelper;
import com.example.mythirdtry.ui.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep4Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;
    AlertDialog dialog;
    DatabaseHelper db;
    SQLiteDatabase mDatabase;
    String user;

    @BindView(R.id.txt_appointment_doctor)
    TextView txt_appointment_doctor;

    @BindView(R.id.txt_appointment_time)
    TextView txt_appointment_time;

    @BindView(R.id.txt_appointment_address)
    TextView txt_appointment_address;

    @BindView(R.id.txt_appointment_clinic_name)
    TextView txt_appointment_clinic_name;

    @BindView(R.id.txt_appointment_open_hours)
    TextView txt_appointment_open_hours;

    @BindView(R.id.txt_appointment_phone)
    TextView txt_appointment_phone;

    @BindView(R.id.txt_appointment_website)
    TextView txt_appointment_website;

    @OnClick(R.id.btn_confirm)
    void confirmBooking()
    {
        dialog.show();
        mDatabase = db.getWritableDatabase();

        BookingInformation bookingInformation = new BookingInformation();

        bookingInformation.setDoctorId(Common.currentDoctor.getDoctor_id());
        bookingInformation.setDoctorName(Common.currentDoctor.getName());
        bookingInformation.setClinicId(Common.currentClinic.getClinicId());
        bookingInformation.setClinicName(Common.currentClinic.getName());
        bookingInformation.setClinicAddress(Common.currentClinic.getAddress());
        bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentAppointment))
                .append(" on ").append(simpleDateFormat.format(Common.bookingDate.getTime())).toString());
        bookingInformation.setSlot(String.valueOf(Common.currentAppointment));

        //submit to clinic document
        DocumentReference appointmentDate = FirebaseFirestore.getInstance()
            .collection("Clinics")
            .document(Common.city)
            .collection("moreClinics")
            .document(Common.currentClinic.getClinicId())
            .collection("Doctor")
            .document(Common.currentDoctor.getDoctor_id())
            .collection(Common.simpleDateFormat.format(Common.bookingDate.getTime()))
            .document(String.valueOf(Common.currentAppointment));

        //user = username - foreign key in booking details table
        db.insertBookingDetails(Common.city, Common.currentClinic.getName(),
                Common.currentDoctor.getName(),
                Common.simpleDateFormat.format(Common.bookingDate.getTime()), user);

        appointmentDate.set(bookingInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismiss();

                //reseting all static data for future use
                resetStaticData();

                //going back to Main Activity
                getActivity().finish();
                Toast.makeText(getContext(), "Booking Successful!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //resetting all static data in Common class
    private void resetStaticData()
    {
        Common.step = 0;
        Common.currentAppointment = -1;
        Common.currentClinic = null;
        Common.currentDoctor = null;
        Common.bookingDate.add(Calendar.DATE, 0);
    }

    //Received from Booking Activity to display all data
    BroadcastReceiver confirmAppointmentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData()
    {
        txt_appointment_doctor.setText(Common.currentDoctor.getName());
        txt_appointment_time.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentAppointment))
        .append(" on ").append(simpleDateFormat.format(Common.bookingDate.getTime())));
        txt_appointment_address.setText(Common.currentClinic.getAddress());
    }

    static BookingStep4Fragment instance;

    public static BookingStep4Fragment getInstance()
    {
        if(instance == null)
        {
            instance = new BookingStep4Fragment();
        }
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmAppointmentReceiver, new IntentFilter(Common.KEY_CONFIRM_BOOKING));

        user = Login.getActivityInstance().getUsername();

        db = new DatabaseHelper(getContext());
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmAppointmentReceiver);

        super.onDestroy();
    }

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_four, container, false);
        unbinder = ButterKnife.bind(this, itemView);

        return itemView;
    }
}
