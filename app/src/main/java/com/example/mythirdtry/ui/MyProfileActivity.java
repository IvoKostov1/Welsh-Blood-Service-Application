package com.example.mythirdtry.ui;

//Libraries
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.mythirdtry.R;
import static com.example.mythirdtry.ui.DatabaseHelper.BOOKING_COL_6;
import static com.example.mythirdtry.ui.DatabaseHelper.BOOKING_DETAILS;
import static com.example.mythirdtry.ui.DatabaseHelper.UPDATED_COL_8;
import static com.example.mythirdtry.ui.DatabaseHelper.UPDATED_USER_DETAILS;

public class MyProfileActivity extends AppCompatActivity {

    //All UI references
    EditText et_name, et_surname, et_age, et_address, et_blood_type, et_mobile_number, et_password;
    CardView btn_update_details, btn_view_details, btn_finish, btn_view_booking_details;
    DatabaseHelper myDb;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        //Assigning each reference to the specific UI component
        et_name = (EditText)findViewById(R.id.et_name);
        et_surname = (EditText)findViewById(R.id.et_surname);
        et_age = (EditText)findViewById(R.id.et_age);
        et_address = (EditText)findViewById(R.id.et_address);
        et_blood_type = (EditText)findViewById(R.id.et_blood);
        et_mobile_number = (EditText)findViewById(R.id.et_phone);
        et_password = (EditText)findViewById(R.id.et_pass);

        btn_update_details = (CardView)findViewById(R.id.btn_updateDetails);
        btn_view_details = (CardView)findViewById(R.id.btn_view_details);
        btn_finish = (CardView) findViewById(R.id.btn_back);
        btn_view_booking_details= (CardView) findViewById(R.id.btn_viewBooking);

        //DatabaseHelper object
        myDb = new DatabaseHelper(this);

        //Intent which receives the username from MainActivity
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        et_password.setText(username);

        //OnClickListeners
        updateDetails();
        viewDetails();
        viewBookingDetails();
        back();
    }


    //onClickListener method which takes the user back to MainActivity
    private void back()
    {
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /*Method of type Cursor
    which presents all the data specified in the query*/
    public Cursor viewData()
    {
        //database instance
        db = myDb.getWritableDatabase();

        //comparison parameter is set equal to the username
        String[] params = new String[] { et_password.getText().toString() };

        //Query
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                UPDATED_USER_DETAILS + " WHERE " + UPDATED_COL_8 + " = " + " ?", params);
        return cursor;
    }

    /*Method of type Cursor
    which presents all the data specified in the query*/
    public Cursor viewBookingData()
    {
        //database instance
        db = myDb.getWritableDatabase();

        //comparison parameter is set equal to the username
        String[] bookingParams = new String[] { et_password.getText().toString() };

        //Query
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                BOOKING_DETAILS + " WHERE " + BOOKING_COL_6 + " = " + " ?", bookingParams);
        return cursor;
    }

    /*OnClickListener method which prints
     the appointment info from the table*/
    private void viewBookingDetails()
    {
        btn_view_booking_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cursor returned from the above method
                Cursor cursor = viewBookingData();

                //If the cursor is equal to 0, something went wrong
                if(cursor.getCount() == 0)
                {
                    //show error AlertDialog message
                    showAlertDialog("Error", "Booking information" +
                            " is empty\n" + "or wrong username!");
                    return;
                }

                //New StringBuffer to write the data to
                StringBuffer buffer = new StringBuffer();

                //While loop which append the data from the table to the StringBuffer
                while(cursor.moveToNext())
                {
                    if(cursor.getString(1) == null)
                    {
                        buffer.append("Empty" + "\n\n");
                    }
                    else
                    {
                        buffer.append("City: " + cursor.getString(1) + "\n\n");
                    }
                    buffer.append("Clinic: " + cursor.getString(2) + "\n\n");
                    buffer.append("Doctor: " + cursor.getString(3) + "\n\n");
                    buffer.append("Date: " + cursor.getString(4) + "\n\n");
                }

                //Sets the title and message of the AlertDialog
                showAlertDialog("Booking Information", buffer.toString());
            }
        });
    }

    /*OnClickListener method which prints
     the user info from the table*/
    private void viewDetails()
    {
        btn_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cursor returned from viewData() method
                Cursor cursor = viewData();

                //If the cursor is equal to 0, something went wrong
                if(cursor.getCount() == 0)
                {
                    //show error AlertDialog message
                    showAlertDialog("Error", "User information is empty\n" + "or wrong username!");
                    return;
                }

                //New StringBuffer to write the data to
                StringBuffer buffer = new StringBuffer();

                //While loop which append the data from the table to the StringBuffer
                while(cursor.moveToNext())
                {

                    if(cursor.getString(1) == null)
                    {
                        buffer.append("Name: Empty" + "\n\n");
                    }
                    else
                    {
                        buffer.append("Name: " + cursor.getString(1) + "\n\n");
                    }

                    if(cursor.getString(2) == null)
                    {
                        buffer.append("Surname: Empty" + "\n\n");
                    }
                    else
                    {
                        buffer.append("Surname: " + cursor.getString(2) + "\n\n");
                    }

                    if(cursor.getString(3) == null)
                    {
                        buffer.append("Age: Empty" + "\n\n");
                    }
                    else
                    {
                        buffer.append("Age: " + cursor.getString(3) + "\n\n");
                    }

                    if(cursor.getString(4) == null)
                    {
                        buffer.append("Address: Empty" + "\n\n");
                    }
                    else
                    {
                        buffer.append("Address: " + cursor.getString(4) + "\n\n");
                    }

                    if(cursor.getString(5) == null)
                    {
                        buffer.append("Blood type: Empty" + "\n\n");
                    }
                    else
                    {
                        buffer.append("Blood type: " + cursor.getString(5) + "\n\n");
                    }

                    if(cursor.getString(6) == null)
                    {
                        buffer.append("Mobile number: Empty" + "\n\n");
                    }
                    else
                    {
                        buffer.append("Mobile number: " + cursor.getString(6) + "\n\n");
                    }
                }

                //Sets the title and message of the AlertDialog
                showAlertDialog("User Information", buffer.toString());

                //All EditText fields are reset
                et_name.getText().clear();
                et_surname.getText().clear();
                et_age.getText().clear();
                et_address.getText().clear();
                et_blood_type.getText().clear();
                et_mobile_number.getText().clear();
            }
        });
    }

    //Method to set up the AlertDialog
    public void showAlertDialog(String title, String dialogMessage)
    {
        //new AlertDialog object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //AlertDialog will be cancelable
        builder.setCancelable(true);

        //Sets the title
        builder.setTitle(title);

        //Sets the text
        builder.setMessage(dialogMessage);

        //Appears on screen
        builder.show();
    }

    //Method to update and save the new details in a new table - UPDATED_USER_DETAILS
    private void updateDetails()
    {
        btn_update_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int age = 0;

                //if any field is empty, print out the toast message
                if(et_name.getText().toString().matches("") || et_surname.getText().toString().matches("") ||
                        et_age.getText().toString().matches("") || et_address.getText().toString().matches("") ||
                        et_blood_type.getText().toString().matches("") || et_mobile_number.getText().toString().matches(""))
                {
                    Toast.makeText(getApplicationContext(), "Empty fields!", Toast.LENGTH_SHORT).show();
                }

                //If the fields are filled up correctly:
                else
                {
                    //The age is parsed to String
                    age = Integer.parseInt(et_age.getText().toString());

                   /*Boolean which is set to true or false. The result depends if the method in DatabaseHelper
                    determines if the username in the EditText field is the same as in the table. This is done as a precaution*/
                    Boolean checkUpdatedUsername = myDb.checkUpdatedUser(et_password.getText().toString());

                    //If the returned value is true, it means that no such username exists, therefore, an error is thrown
                    if (checkUpdatedUsername == true)
                    {
                        showAlertDialog("Error", "Wrong username!");
                        return;
                    }

                    //This method from DatabaseHelper inserts the data in the new Table
                    boolean isUpdated = myDb.updateData(et_name.getText().toString(), et_surname.getText().toString(),
                            age, et_address.getText().toString(), et_blood_type.getText().toString(),
                            et_mobile_number.getText().toString(), et_password.getText().toString());

                    //If the boolean is true, everything is ok
                    if (isUpdated == true)
                    {
                        Toast.makeText(getApplicationContext(), "Details Updated", Toast.LENGTH_SHORT).show();
                    }

                    //Else, error message
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Details Not Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


}
