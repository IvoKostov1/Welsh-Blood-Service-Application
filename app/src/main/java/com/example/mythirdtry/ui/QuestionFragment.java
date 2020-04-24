package com.example.mythirdtry.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mythirdtry.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView txt_question_text, txt_end_yes, txt_end_no;
    CheckBox ckbYes, ckbNo;
    Question question;
    int questionIndex = -1;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(String param1, String param2) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_question, container, false);

        ckbYes = (CheckBox)itemView.findViewById(R.id.chkYes);
        ckbNo = (CheckBox)itemView.findViewById(R.id.chkNo);
        txt_end_yes = (TextView)itemView.findViewById(R.id.tv_end_yes);
        txt_end_no = (TextView)itemView.findViewById(R.id.tv_end_no);

        //Get question
        questionIndex = getArguments().getInt("index", -1);
        question = Common.questionList.get(questionIndex);

        if (question != null) {

            if(question == Common.questionList.get(14))
            {
                txt_end_yes.setText(question.getAnswerA());
                txt_end_yes.setVisibility(View.VISIBLE);

                txt_end_no.setText(question.getAnswerB());
                txt_end_no.setVisibility(View.VISIBLE);

                ckbYes.setVisibility(View.GONE);
                ckbNo.setVisibility(View.GONE);
            }

            //View
            txt_question_text = (TextView)itemView.findViewById(R.id.txt_question_text);
            txt_question_text.setText(question.getQuestionText());
            ckbYes.setText(question.getAnswerA());

            ckbYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        ckbNo.setChecked(false);
                    }

                }
            });
            ckbNo.setText(question.getAnswerB());
            ckbNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        ckbYes.setChecked(false);
                    }
                }
            });
        }

        return itemView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //This function returns wrong or right answer
    public CurrentQuestion getSelectedAnswer() {

        CurrentQuestion currentQuestion = new CurrentQuestion(questionIndex, Common.ANSWER_TYPE.NO_ANSWER);
        StringBuilder result = new StringBuilder();

        if(ckbYes.isChecked() == true)
        {
            Common.selected_values.add(ckbYes.getText().toString());
        }

        else if (ckbNo.isChecked() == true)
        {
            Common.selected_values.add(ckbNo.getText().toString());
        }

        if (Common.selected_values.size() == 1)
        {
            Object[] arrayAnswer = Common.selected_values.toArray();
            result.append((String)arrayAnswer[0]);
        }

        if(question != null)
        {
            if(!TextUtils.isEmpty(result))
            {
                if(result.toString().equals(question.getCorrectAnswer()))
                {
                    currentQuestion.setType(Common.ANSWER_TYPE.RIGHT_ANSWER);
                }

                else
                {
                    currentQuestion.setType(Common.ANSWER_TYPE.WRONG_ANSWER);
                }
            }

            else
            {
                currentQuestion.setType(Common.ANSWER_TYPE.NO_ANSWER);
            }
        }
        else
        {
            Toast.makeText(getContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
            currentQuestion.setType(Common.ANSWER_TYPE.NO_ANSWER);
        }
        Common.selected_values.clear();

        return currentQuestion;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
