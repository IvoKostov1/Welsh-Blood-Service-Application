package com.example.mythirdtry.Interface;

import com.example.mythirdtry.ui.Appointment;

import java.util.List;

public interface AppointmentLoadListener {

    void onAppointmentSuccess(List<Appointment> appointmentList);
    void onAppointmentFailed(String message);
    void onAppointmentEmpty();

}
