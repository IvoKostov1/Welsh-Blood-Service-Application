package com.example.mythirdtry.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mythirdtry.R;
import java.util.List;

public class AnswerSheetAdapter extends RecyclerView.Adapter<AnswerSheetAdapter.MyViewHolder> {

    //Context is global information about an application
    Context context;

    //List of the current questions being answered
    List<CurrentQuestion> currentQuestionList;

    //Constructor
    public AnswerSheetAdapter(Context context, List<CurrentQuestion> currentQuestionList) {
        this.context = context;
        this.currentQuestionList = currentQuestionList;
    }

    /*This method inflates layout_grid_answer_sheet
     at first with only blue squares but the
     colour changes when the user selects and answer*/
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_grid_answer_sheet,parent, false );
        return new MyViewHolder(itemView);
    }

    //This method changes the colour of the square based on the answer
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(currentQuestionList.get(position).getType() == Common.ANSWER_TYPE.RIGHT_ANSWER)
        {
            holder.question_item.setBackgroundResource(R.drawable.grid_question_right_answer);
        }

        else if(currentQuestionList.get(position).getType() == Common.ANSWER_TYPE.WRONG_ANSWER)
        {
            holder.question_item.setBackgroundResource(R.drawable.grid_question_wrong_answer);
        }

        else
        {
            holder.question_item.setBackgroundResource(R.drawable.grid_question_no_answer);
        }

    }

    @Override
    public int getItemCount() {
        //Hardcoded number of squares because of the confirmation fragment
        return 14;
    }

    //This assigns the item of the layout which is used for inflation
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        View question_item;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            question_item = itemView.findViewById(R.id.question_item);
        }
    }
}
