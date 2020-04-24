package com.example.mythirdtry.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mythirdtry.R;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends AppCompatActivity implements
        QuestionFragment.OnFragmentInteractionListener {

    //All UI references
    RecyclerView answer_sheet_view;
    AnswerSheetAdapter answerSheetAdapter;
    TextView txt_right_answer, txt_wrong_answer;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //Action bar set up
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Eligibility quiz");
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        /*This method inflates the little squares which indicate if
      the question is unanswered, correct or wrong*/
        takeQuestion();

        //If questionList is not empty:
        if (Common.questionList.size() > 0) {

            //Assigning each reference to the specific UI component
            txt_right_answer = (TextView) findViewById(R.id.txt_question_right);
            txt_right_answer.setVisibility(View.VISIBLE);

            //sets the format of txt_right_answer
            txt_right_answer.setText(new StringBuilder(String.format("%d/%d", Common.right_answer_count, 14)));

            answer_sheet_view = (RecyclerView) findViewById(R.id.grid_answer);
            answer_sheet_view.setHasFixedSize(true);

            /*This check ensures the squares are split in two lines
              in order to not take up too much space on the screen*/
            if (Common.questionList.size() > 14) {
                answer_sheet_view.setLayoutManager(new GridLayoutManager
                        (this, Common.questionList.size() / 2));
            }

            //Setting the adapter
            answerSheetAdapter = new AnswerSheetAdapter(this, Common.answerSheetList);
            answer_sheet_view.setAdapter(answerSheetAdapter);

            //This function generates the question fragments
            genFragmentList();

            /*Instance of QuestionFragmentAdapter which
              populates the TabLayout and also the viewPager*/
            QuestionFragmentAdapter questionFragmentAdapter = new QuestionFragmentAdapter(getSupportFragmentManager(),
                    this, Common.fragmentsList);

            //Sets the adapter
            viewPager.setAdapter(questionFragmentAdapter);

            //Sets the TabLayout with the viewPager
            tabLayout.setupWithViewPager(viewPager);

            //This listens for page changes
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                //Each number represents left, right or undetermined
                int SCROLLING_RIGHT = 0;
                int SCROLLING_LEFT = 1;
                int SCROLLING_UNDETERMINED = 2;

                //At the first the scrolling is undetermined
                int currentScrollingDirection = 2;

                //Sets the scrolling position
                private void setScrollingDirection(float positionOffset) {
                    if ((1 - positionOffset) >= 0.5) {
                        this.currentScrollingDirection = SCROLLING_RIGHT;
                    } else if ((1 - positionOffset) <= 0.5) {
                        this.currentScrollingDirection = SCROLLING_LEFT;
                    }
                }

                //Sets the current scrolling direction
                private boolean isScrollDirectionUndetermined() {
                    return currentScrollingDirection == SCROLLING_UNDETERMINED;
                }

                private boolean isScrollDirectionRight() {
                    return currentScrollingDirection == SCROLLING_RIGHT;
                }

                private boolean isScrollDirectionLeft() {
                    return currentScrollingDirection == SCROLLING_LEFT;
                }


                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    if (isScrollDirectionUndetermined()) {
                        setScrollingDirection(positionOffset);
                    }

                }

                //This triggers when the user changes pages
                @Override
                public void onPageSelected(int position) {

                    QuestionFragment questionFragment;

                    /*If the position is not 0,
                      the app checks if the user scrolled left or right*/
                    if (position > 0) {

                        //If the user scrolls right, the previous question is displayed
                        if (isScrollDirectionRight()) {
                            questionFragment = Common.fragmentsList.get(position - 1);
                            CurrentQuestion question_state = questionFragment.getSelectedAnswer();

                            //The previous square is selected and waiting for an answer
                            Common.answerSheetList.set(position - 1, question_state);

                        }
                        //If the user scrolls left, the next question is displayed
                        else if (isScrollDirectionLeft()) {
                            questionFragment = Common.fragmentsList.get(position + 1);
                            CurrentQuestion question_state = questionFragment.getSelectedAnswer();

                            //The previous square is selected and waiting for an answer
                            Common.answerSheetList.set(position + 1, question_state);
                        }
                    }

                    //The answerSheet is notified for any data changes
                    answerSheetAdapter.notifyDataSetChanged();

                    //This method counts the correct answers
                    countCorrectAnswer();

                    //This sets the format of the text in the TextView
                    txt_right_answer.setText(new StringBuilder(String.format("%d", Common.right_answer_count)).append("/")
                            .append(String.format("%d", 14)).toString());

                    txt_wrong_answer.setText(String.valueOf(Common.wrong_answer_count));

                }
                /*The scrolling direction is set to undetermined when
                the page is scrolled*/
                @Override
                public void onPageScrollStateChanged(int state) {

                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        this.currentScrollingDirection = SCROLLING_UNDETERMINED;
                    }

                }
            });
        }
    }

    //This method is triggered when the user is done wit the quiz
    private void finishQuiz() {

        //The user is taken to the Result Activity
        Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);

        Common.answerSheetList.clear();
        Common.fragmentsList.clear();

        //closes the activity
        finish();

        //starts Result Activity
        startActivity(intent);
    }

    //This method counts the correct answers
    public void countCorrectAnswer() {

        //At the start both are equal to 0
        Common.right_answer_count = Common.wrong_answer_count = 0;

        /*Every question represents one square on the answer sheet,
          so for every question, depending if right or wrong, 1 is added*/
        for (CurrentQuestion item : Common.answerSheetList) {
            if (item.getType() == Common.ANSWER_TYPE.RIGHT_ANSWER) {
                Common.right_answer_count++;
            } else if (item.getType() == Common.ANSWER_TYPE.WRONG_ANSWER) {
                Common.wrong_answer_count++;
            }
        }
    }

    //This function generates the question fragments
    private void genFragmentList() {
        for (int i = 0; i < Common.questionList.size(); i++) {
            Bundle bundle = new Bundle();

            //i represents every single question data(text, answers, number)
            bundle.putInt("index", i);

            //fragment is an instance of QuestionFragment
            QuestionFragment fragment = new QuestionFragment();

            //The fragment is set for each question
            fragment.setArguments(bundle);

            //each fragment is added in fragmentsList which stores them
            Common.fragmentsList.add(fragment);
        }
    }

    /*This made populates questionList with all question data.
      It also inflates the little squares which indicate if
      the question is unanswered, correct or wrong*/
    private void takeQuestion() {

        //questionList is equal to the function in DBhelper which gets the questions from the table
        Common.questionList = DBHelper.getInstance(this).getQuestions();
        if (Common.questionList.size() == 0) {
            Toast.makeText(getApplicationContext(), "No questions", Toast.LENGTH_SHORT).show();

            //If questionList is not empty, the squares are populated
        } else {
            for (int i = 0; i < Common.questionList.size(); i++) {
                Common.answerSheetList.add(new CurrentQuestion(i, Common.ANSWER_TYPE.NO_ANSWER));
            }
        }
    }

    //This method creates the red wrong answer counter and
    // tick button in the Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //If the id of the clicked item is the same as the Tick Button
        if (id == R.id.menu_finish_quiz)
        {
            //A window poops up to ask if the user if they are ready with the Quiz
            new MaterialStyledDialog.Builder(this).setTitle("Finish?")
                    .setIcon(R.drawable.ic_mood_black_24dp)
                    .setDescription("Ready?")
                    .setNegativeText("No")
                    //If the user click "No", the dialog is dismissed
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    //If the user clicks "Yes", the user is taken to ResultActivity
                    .setPositiveText("Yes")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            finishQuiz();
                        }
                    }).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //This method creates the red square where the number of wrong question is
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_wrong_answer);
        LinearLayout linearLayout = (LinearLayout)item.getActionView();
        txt_wrong_answer = (TextView)linearLayout.findViewById(R.id.txt_wrong_answer);
        txt_wrong_answer.setText(String.valueOf(0));

        return true;
    }
}
