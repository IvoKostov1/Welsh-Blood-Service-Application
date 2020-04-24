package com.example.mythirdtry.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.mythirdtry.R;

public class Registration extends AppCompatActivity {

    //All UI references
    EditText editPassword, editEmail, editUsername;
    Button btnRegister;

    //DatabaseHelper object
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Assigning each reference to the specific UI component
        editUsername = (EditText)findViewById(R.id.et_Username);
        editPassword = (EditText)findViewById(R.id.et_Password);
        editEmail = (EditText)findViewById(R.id.et_Email);
        btnRegister = (Button)findViewById(R.id.btn_Register);
        myDb = new DatabaseHelper(this);

        //method for adding the datafrom the user
        //in the database
        AddData();
    }

    public void AddData()
    {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = editUsername.getText().toString(); //username from the EditText

                //If the username EditText is empty or the password EditText is empty or the email EditText is empty,
                if(editUsername.getText().toString().matches("")||editPassword.getText().toString().matches("") ||
                editEmail.getText().toString().matches(""))
                {
                    //display this toast message
                    Toast.makeText(getApplicationContext(), "Fields Empty!", Toast.LENGTH_SHORT).show();
                }

                //else if the information is typed in correctly.
                else
                {
                    //boolean which checks of the username has already been taken
                    Boolean checkUser = myDb.checkUsername(user);

                    //if the boolean returns true, the data is inserted in the database
                    if(checkUser == true)
                    {
                        //another boolean to check if the data has been inserted correctly
                        boolean isInserted = myDb.insertData(editUsername.getText().toString(),
                                editPassword.getText().toString(), editEmail.getText().toString());

                        //if the data has been inserted correctly, the below toast message is displayed
                        //and the user is taken back to MainActivity
                        if(isInserted == true)
                        {
                            Toast.makeText(Registration.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
