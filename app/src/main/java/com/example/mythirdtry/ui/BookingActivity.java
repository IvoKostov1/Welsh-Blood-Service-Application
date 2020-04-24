package com.example.mythirdtry.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.example.mythirdtry.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class BookingActivity extends AppCompatActivity {

    //Used to receive broadcasts from the fragments
    LocalBroadcastManager localBroadcastManager;

    //Loading window
    AlertDialog dialog;

    //Used to load doctors data from Firebase
    CollectionReference doctorRef;

    //Butterknife library - automatically finds and cast
    //corresponding views in layout
    @BindView(R.id.step_view)
    StepView stepView;

    @BindView(R.id.view_pager)
    NotSwiperForBookingPager viewPager;

    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;

    @BindView(R.id.btn_next_step)
    Button btn_next_step;

    @BindView(R.id.booking_activity)
    RelativeLayout booking_activity;

    //OnClick method for the "Next" button
    @OnClick(R.id.btn_next_step)
    void nextClick()
    {
        if(Common.step < 3 || Common.step == 0)
        {
            Common.step++;

            if (Common.step == 1) //step == 1 when the user is choosing a clinic
            {
                if(Common.currentClinic != null)
                {
                    loadDoctorsByClinic(Common.currentClinic.getClinicId());
                }
            }

            else if (Common.step == 2) //step == 2 after choosing a doctor
            {
                if(Common.currentDoctor != null)
                {
                    loadDoctorTimes(Common.currentDoctor.getDoctor_id());
                }
            }

            else if (Common.step == 3) //step == 3 after choosing an appointment time
            {
                if(Common.currentAppointment != 1)
                {
                    confirmAppointment();
                }
            }

            viewPager.setCurrentItem(Common.step);
        }
    }

    private void confirmAppointment()
    {
        //Sending local broadcast to BookingStep4Fragment
        Intent intent = new Intent(Common.KEY_CONFIRM_BOOKING);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadDoctorTimes(String doctor_id)
    {
        //Sending local broadcast to BookingStep3Fragment
        Intent intent = new Intent(Common.KEY_DISPLAY_DOCTOR_TIMES);
        localBroadcastManager.sendBroadcast(intent);
    }

    @OnClick(R.id.btn_previous_step)
    void previousStep()
    {
        if(Common.step == 3 || Common.step > 0)
        {
            Common.step--;
            viewPager.setCurrentItem(Common.step);

            if(Common.step < 3)
            {
                btn_next_step.setEnabled(true);
                colorButton();
            }
        }
    }

    private void loadDoctorsByClinic(String clinicId)
    {
        //Load doctor information from the Firestore

        //Loading window appears
         dialog.show();


        if(!TextUtils.isEmpty(Common.city))
        {
            /*Firestore query - gets the collection reference in collection "Clinics,
            * in the selected city, in collection "moreClinics", in the currently selected clinic,
            * in collection "Doctor*/
            doctorRef = FirebaseFirestore.getInstance()
                    .collection("Clinics")
                    .document(Common.city)
                    .collection("moreClinics")
                    .document(clinicId)
                    .collection("Doctor");

            doctorRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    ArrayList<Doctor> doctors = new ArrayList<>();

                    for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                    {
                        //This doctor object is equal to the doctor data from the document in Firestore
                        Doctor doctor = documentSnapshot.toObject(Doctor.class);
                        doctor.setDoctor_id(documentSnapshot.getId());

                        doctors.add(doctor);
                    }
                    //Sending broadcast to BookingStep2 to load
                    Intent intent = new Intent(Common.KEY_DOCTOR_LOAD_DONE);
                    intent.putParcelableArrayListExtra(Common.KEY_DOCTOR_LOAD_DONE, doctors);
                    localBroadcastManager.sendBroadcast(intent);

                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    dialog.dismiss();
                }
            });
        }
    }

    //Broadcast receiver receiving information from MyClinicAdapter
    //so that btn_next_step could be enabled and colored
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int step = intent.getIntExtra(Common.KEY_STEP, 0);
            if(step ==1)
            {
                Common.currentClinic = intent.getParcelableExtra(Common.KEY_CLINIC_STORAGE);
            }

            else if (step == 2)
            {
                Common.currentDoctor = intent.getParcelableExtra(Common.KEY_DOCTOR_SELECTED);
            }

            else if (step == 3)
            {
                Common.currentAppointment = intent.getIntExtra(Common.KEY_APPOINTMENT, -1);
            }


            btn_next_step.setEnabled(true);
            colorButton();
        }
    };

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        ButterKnife.bind(BookingActivity.this);

        dialog = new SpotsDialog.Builder().setContext(BookingActivity.this).build();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));

        setUpStepView();
        colorButton();

                                                                                 //Indicates that only the current fragment is in Resume state
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        viewPager.setOffscreenPageLimit(4); //Because there are 4 fragments, the state of this pages should be set to 4
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //Show the step the user is on
                stepView.go(position, true);

                if (position == 0) {
                    btn_previous_step.setEnabled(false);
                }

                else {
                    btn_previous_step.setEnabled(true);
                }

                if (position == 3) {
                    stepView.setBackgroundColor(getResources().getColor(R.color.colorStepView, getTheme()));
                    stepView.getState().doneCircleColor(getResources().getColor(R.color.colorWhite, getTheme()));
                    booking_activity.setBackgroundColor(getResources().getColor(R.color.colorStepView, getTheme()));
                    btn_next_step.setEnabled(false);
                }

                else {
                    booking_activity.setBackgroundColor(getResources().getColor(R.color.colorBooking, getTheme()));
                    stepView.setBackgroundColor(getResources().getColor(R.color.colorBooking, getTheme()));
                    btn_next_step.setEnabled(true);
                }

                btn_next_step.setEnabled(false);
                colorButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void colorButton()
    {
        if(btn_previous_step.isEnabled())
        {
            btn_previous_step.setBackgroundColor(getResources().getColor(R.color.colorButton, getTheme()));
            btn_previous_step.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
        }

        else
        {
            btn_previous_step.setBackgroundColor(getResources().getColor(android.R.color.darker_gray, getTheme()));
            btn_previous_step.setTextColor(getResources().getColor(android.R.color.black, getTheme()));
        }

        if(btn_next_step.isEnabled())
        {
            btn_next_step.setBackgroundColor(getResources().getColor(R.color.colorButton, getTheme()));
            btn_next_step.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
        }

        else
        {
            btn_next_step.setBackgroundColor(getResources().getColor(android.R.color.darker_gray, getTheme()));
            btn_next_step.setTextColor(getResources().getColor(android.R.color.black, getTheme()));
        }
    }


    private void setUpStepView()
    {
        List<String> stepList = new ArrayList<>();
        stepList.add("City");
        stepList.add("Clinic");
        stepList.add("Time");
        stepList.add("Confirm");

        stepView.setSteps(stepList);

    }
}
