package com.example.mythirdtry.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

//This class includes all data which is used across the Application
public class Common {

    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_CLINIC_STORAGE = "CLINIC_STORAGE";
    public static final String KEY_DOCTOR_LOAD_DONE = "DOCTOR_LOAD_DONE";
    public static final String KEY_DISPLAY_DOCTOR_TIMES = "DISPLAY_DOCTOR_TIMES";
    public static final String KEY_STEP = "KEY_STEP";
    public static final String KEY_DOCTOR_SELECTED = "DOCTOR_SELECTED";

    //Let's say each clinic has the same Open times (From 9am to 7pm)
    //and each procedure takes 30 minutes
    //In that case, each clinic has 10 horus of open time
    //So 10 hours multiplied by 2(each hour has 2 procedure times) = 20
    public static final int APPOINTMENT_TOTAL = 20;
    public static final Object DISABLE_TAG = "DISABLE_TAG";
    public static final String KEY_APPOINTMENT = "KEY_APPOINTMENT";
    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";

    public static Addresses currentClinic;
    public static int step = 0;
    public static String city = "";
    public static Doctor currentDoctor;
    public static int currentAppointment=-1;
    public static Calendar bookingDate = Calendar.getInstance();
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");

    //Global data for the Quiz
    public enum ANSWER_TYPE
    {
        RIGHT_ANSWER,
        NO_ANSWER,
        WRONG_ANSWER

    }

    public static List<Question> questionList = new ArrayList<>();
    public static List<CurrentQuestion> answerSheetList = new ArrayList<>();
    public static ArrayList<QuestionFragment> fragmentsList = new ArrayList<>();
    public static TreeSet<String> selected_values = new TreeSet<>();
    public static int right_answer_count = 0;
    public static int wrong_answer_count = 0;

    public static String convertTimeSlotToString(int time_slot)
    {
        switch(time_slot)
        {
            case 0:
                return "9:00 - 9:30";
            case 1:
                return "9:30 - 10:00";
            case 2:
                return "10:00 - 10:30";
            case 3:
                return "10:30 - 11:00";
            case 4:
                return "11:00 - 11:30";
            case 5:
                return "11:30 - 12:00";
            case 6:
                return "12:00 - 12:30";
            case 7:
                return "12:30 - 13:00";
            case 8:
                return "13:00 - 13:30";
            case 9:
                return "13:30 - 14:00";
            case 10:
                return "14:00 - 14:30";
            case 11:
                return "14:30 - 15:00";
            case 12:
                return "15:00 - 15:30";
            case 13:
                return "15:30 - 16:00";
            case 14:
                return "16:00 - 16:30";
            case 15:
                return "16:30 - 17:00";
            case 16:
                return "17:00 - 17:30";
            case 17:
                return "17:30 - 18:00";
            case 18:
                return "18:00 - 18:30";
            case 19:
                return "18:30 - 19:00";
                default:
                    return "Closed";
        }

    }
}
