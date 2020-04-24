package com.example.mythirdtry.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.mythirdtry.R;
import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb; //DatabaseHelper object
    SQLiteDatabase db; //SQLiteDatabase object query writing

    //All UI references
    CardView tvRegister;
    CardView loginButton;
    CardView MyAccountButton;
    CardView QuestionsButton;
    CardView Booking;
    CardView gps;
    LinearLayout bookingLayout;
    TextView booking_desc;

    //Used to receive the username from Login.class
    String user;

    //This code goes to Login.class to request data back
    final int REQUEST_MESSAGE_CODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assigning each reference to the specific UI component
        myDb = new DatabaseHelper(this);
        tvRegister = (CardView)findViewById(R.id.tv_Register);
        loginButton = (CardView)findViewById(R.id.btn_Login);
        QuestionsButton = (CardView)findViewById(R.id.btn_quiz);
        gps = (CardView)findViewById(R.id.btn_GetLoc);
        MyAccountButton = (CardView)findViewById(R.id.btn_MyAccount);
        Booking = (CardView) findViewById(R.id.btn_book);
        bookingLayout =  (LinearLayout) findViewById(R.id.booking_id);
        booking_desc = (TextView) findViewById(R.id.booking_description);

        //OnClickListeners
        GoToRegister();
        GoToLogin();
        GoToQuestionActivity();
        GoToMyAccount();
        GoToBooking();
        GoToGps();
    }

    //This functions takes the user to gps.class
    private void GoToGps()
    {
         gps.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(MainActivity.this, gps.class); //Intent to go from MainActivity.class to gps.class
                 startActivity(intent); //Start the intent
             }
         });
    }

    //This functions takes the user to BookingActivity.class
    private void GoToBooking()
    {
        Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookingActivity.class); //Intent to go from MainActivity.class to BookingActivity.class
                startActivity(intent); //Start the Intent
            }
        });
    }

    //This functions takes the user to MyProfileActivity.class
    private void GoToMyAccount()
    {
        MyAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyProfileActivity.class); //Intent to go from MainActivity.class to MyProfileActivity.class
                intent.putExtra("username", user);
                startActivity(intent); //Start the intent
            }
        });
    }

    //This functions takes the user to QuestionActivity.class
    public void GoToQuestionActivity()
    {
        QuestionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class); //Intent to go from MainActivity.class to QuestionActivity.class
                startActivity(intent); //Start the intent
            }
        });
    }

    //This functions takes the user to Registration.class
    public void GoToRegister()
    {
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registration.class); //Intent to go from MainActivity.class to Registration.class
                startActivity(intent);
            }
        });
    }

    //This functions takes the user to Login.class
    public void GoToLogin()
    {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Login.class); //Intent to go from MainActivity.class to Login.class
                Bundle bundle = new Bundle(); //Creates a bundle to store boolean which will indicate if the user is logged in
                bundle.putBoolean("isLogged", false); //boolean for log in indication
                i.putExtras(bundle); //Puts the boolean in the bundle
                startActivityForResult(i, REQUEST_MESSAGE_CODE); //Starts the intent
            }
        });
    }

    /*When the user returns from Login.class, this method is called to check for result
    It takes 3 arguments:
    1) The requestCode which was passed from MainActivity.class  to Login.class
    2) The resultCode which is either RESULT_OK or RESULT_CANCELED, depending what Login.class has sent
    3) An intent which carries the data between the activities*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        //If the resultCode from Login.class is RESULT_OK
        if (resultCode == RESULT_OK) {
            user = data.getStringExtra("ReturnUsername"); //user is equal to the data carried in "ReturnUsername"
            boolean isLogged = data.getBooleanExtra("Logged", false); //isLogged is equal to the data caried in "Logged"

            if(isLogged == true)
            {
                //-------------------------------------------
                //All necessary changes to the UI are made
                loginButton.setVisibility(GONE);
                MyAccountButton.setVisibility(View.VISIBLE);
                Booking.setVisibility(View.VISIBLE);
                tvRegister.setVisibility(GONE);
                QuestionsButton.setVisibility(View.VISIBLE);
                //-------------------------------------------
            }
        }
    }

    /*When the user goes back to the activity(no matter which was the previous one),
    The Activity Lifecycle calls onResume.
    In this case, onResume is overwritten to check if any booking have been made*/
    @Override
    protected void onResume() {
        super.onResume();

        //used to prevent crashing
        if(user == null)
        {
            user = "No User!";
        }

        //Parameter to use in the raqQuery
        String[] userParams = new String[] { user };

        db = myDb.getReadableDatabase();// Open a readable database

        /*SQL query which selects all data from BOOKING_DETAILS where the city is null and username is equal to the currently logged in one
        This is done in order to avoid confusing the application.
        For example, if one user logs in and makes a booking,
        then another one loges in and does not make a booking,
        If it is not explicitly said that the username in the table must be equal to the currently logged in one,
        The application will always execute the first condition*/
        Cursor cursor = db.rawQuery("SELECT * FROM " + myDb.BOOKING_DETAILS + " WHERE " +
                myDb.BOOKING_COL_2 + " IS NULL AND " + myDb.BOOKING_COL_6 + " = " + " ?", userParams);

        /*If the cursor has returned a result,
        It means that there are no booking in this users' account.
        The necessary changes in color and text are made.*/
        if(cursor.getCount() >= 1)
        {
            Booking.setEnabled(true);
            bookingLayout.setBackgroundColor(getResources().getColor(R.color.colorLighterBooking, getTheme()));
            booking_desc.setTextColor(getResources().getColor(android.R.color.darker_gray, getTheme()));
            booking_desc.setText("Book an appointment");
        }

        /*If the cursor does not return a result,
        It means that there is a booking and the CardView should not be clickable.
        The necessary changes in color and text are made.*/
        else
        {
            Booking.setEnabled(false);
            bookingLayout.setBackgroundColor(getResources().getColor(android.R.color.darker_gray, getTheme()));
            booking_desc.setTextColor(getResources().getColor(android.R.color.white, getTheme()));
            booking_desc.setText("You already have an appointment");
        }
    }
}

