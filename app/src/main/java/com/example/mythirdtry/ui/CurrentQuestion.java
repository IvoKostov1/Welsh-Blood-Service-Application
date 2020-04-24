package com.example.mythirdtry.ui;

/*This class represents the current question
  which the user is currently on*/
public class CurrentQuestion
{
    private int questionIndex;
    private Common.ANSWER_TYPE type;

    //constructor
    public CurrentQuestion(int questionIndex, Common.ANSWER_TYPE type)
    {
        this.questionIndex = questionIndex;
        this.type = type;
    }

    //getters and setters
    public Common.ANSWER_TYPE getType() {
        return type;
    }

    public void setType(Common.ANSWER_TYPE type) {
        this.type = type;
    }
}

