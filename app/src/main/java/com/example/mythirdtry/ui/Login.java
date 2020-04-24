package com.example.mythirdtry.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.os.Bundle;
import android.widget.Toast;
import com.example.mythirdtry.R;

public class Login extends AppCompatActivity {

    //All UI references
    EditText LoginUsername, LoginPassword;
    Button LoginButton;
    DatabaseHelper db;

    //used in BookingStep4Fragment
    static Login INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        INSTANCE = this; //Instance of the class
        db = new DatabaseHelper(this); //DatabaseHelper object

        //Assigning each reference to the specific UI component
        LoginUsername = (EditText) findViewById(R.id.et_LoginUsername);
        LoginPassword = (EditText) findViewById(R.id.et_LoginPassword);
        LoginButton = (Button) findViewById(R.id.btn_LoginButton);

        login(); //OnClickListener method
        getUsername(); //method to return the username
    }

    //used in BookingStep4 to put the booking details in SQLite database
    public static Login getActivityInstance()
    {
        return INSTANCE;
    }

    //returns the username from this EditText
    public String getUsername()
    {
        return LoginUsername.getText().toString();
    }


    private void login()
    {
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //variables for the details
                String username = LoginUsername.getText().toString();
                String password = LoginPassword.getText().toString();

                //Method from DatabaseHelper.class to check if the username and password exist
                //and if they are correct
                Boolean CheckUserAndPass = db.checkUsernameAndPassword(username, password);

                //boolean receives the data from MainActivity.class
                boolean logged = getIntent().getExtras().getBoolean("isLogged", false);

                //if the username and password exist OR if they are correct
                if (CheckUserAndPass == true) {
                    //toast message is displayed to inform the user
                    Toast.makeText(getApplicationContext(), "Successful login", Toast.LENGTH_LONG).show();

                    //logged is changed to true
                    logged = true;

                    //a new intent is created to finish the activity and send "logged" to MainActivity
                    //to change the UI
                    Intent ret = new Intent(getApplicationContext(), MainActivity.class);
                    ret.putExtra("ReturnUsername", getUsername());
                    ret.putExtra("Logged", logged);
                    setResult(RESULT_OK, ret);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
