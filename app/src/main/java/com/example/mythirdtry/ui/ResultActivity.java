package com.example.mythirdtry.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.mythirdtry.R;

public class  ResultActivity extends AppCompatActivity {

    //All UI references
    Toolbar toolbar;
    TextView txt_result, txt_right_answer;
    TextView btn_filer_right, btn_filer_wrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Result");
        //Set a Toolbar to act as the ActionBar for this Activity window.
        setSupportActionBar(toolbar);

        //gets a reference of this Activitys' Action Bar
        //Sets the <- button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Assigning each reference to the specific UI component
        txt_result = (TextView)findViewById(R.id.txt_result);
        txt_right_answer = (TextView)findViewById(R.id.txt_right_answer);
        btn_filer_right = (TextView) findViewById(R.id.btn_filter_right_answer);
        btn_filer_wrong = (TextView) findViewById(R.id.btn_filter_wrong_answer);

        //Sets the format of the text to be "number/number"
        txt_right_answer.setText(new StringBuilder("").append(Common.right_answer_count).append("/")
        .append(14));

        //Appends the number of the right and wrong answers
        btn_filer_right.setText(new StringBuilder("").append(Common.right_answer_count));
        btn_filer_wrong.setText(new StringBuilder("").append(Common.wrong_answer_count));

        //Calculate result
        int percent = (Common.right_answer_count*100/14);

        //The user needs 100% result to pass the quiz
        if(percent == 100)
        {
            txt_result.setText("We are pleased to say that it looks like you're good to give."
                    + "\n" + "Please contact a clinic for more information");
        }
        else
        {
            txt_result.setText("Seems like you gave a number of answers which may be worrying for you health"
                    + "\n" + "Please contact a clinic for more information");
        }
    }

    //This method goes back to the main Activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            //
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Common.right_answer_count = 0;
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //Delete all activity

                /*A boolean is sent back to MainActivity
                 to keep the user logged in*/
                intent.putExtra("Logged", true);
                startActivity(intent);
                break;
        }
        return true;
    }
}
