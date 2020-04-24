package com.example.mythirdtry.ui;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import java.util.ArrayList;
import java.util.List;

//This class deals with the database for Multiple Choice Quiz
public class DBHelper extends SQLiteAssetHelper {

    private static final String DB_NAME = "EDMTQuiz2019.db";
    private static final int DB_VER = 1;

    private static DBHelper instance;

    //This method creates an instance of the class
    public static synchronized DBHelper getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new DBHelper(context);
        }
        return instance;
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    //This method gets the question from the table
    public List<Question>getQuestions()
    {
        SQLiteDatabase db = instance.getWritableDatabase();

        //The cursor gets all the questions from the table
        Cursor cursor = db.rawQuery("SELECT * FROM Question;", null);

        //A list which stores the question
        List<Question> questions = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            while(!cursor.isAfterLast())
            {
                Question question = new Question(cursor.getInt(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex("QuestionText")),
                        cursor.getString(cursor.getColumnIndex("AnswerA")),
                        cursor.getString(cursor.getColumnIndex("AnswerB")),
                        cursor.getString(cursor.getColumnIndex("CorrectAnswer")));
                questions.add(question);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return questions;
    }
}
