package com.example.mythirdtry.ui;

//This class stores all the question data from the table
public class Question {
    private int id;
    private String questionText, answerA, answerB, correctAnswer;

    public Question(int id, String questionText, String answerA, String answerB, String correctAnswer)
    {
        this.id = id;
        this.questionText = questionText;
        this.answerA = answerA;
        this.answerB = answerB;
        this.correctAnswer = correctAnswer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getAnswerA() {
        return answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
