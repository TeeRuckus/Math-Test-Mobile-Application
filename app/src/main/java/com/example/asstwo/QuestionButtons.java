package com.example.asstwo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionButtons#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionButtons extends Fragment {

    private static final String TAG = "QuestionButtons.";
    private Button optionOne;
    private Button optionTwo;
    private Button optionThree;
    private Button optionFour;

    private enum buttonNumbers {
        twoButtons,
        threeButtons,
        allButtons
    }

    private static buttonNumbers currState;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String receivedOptionOne;
    private String receivedOptionTwo;
    private String receivedOptionThree;
    private String receivedOptionFour;


    public QuestionButtons() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionButtons.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionButtons newInstance(String param1, String param2) {
        QuestionButtons fragment = new QuestionButtons();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receivedOptionOne = "";
        receivedOptionTwo = "";
        receivedOptionThree = "";
        receivedOptionFour = "";

        if (this.getArguments() != null) {
            receivedOptionOne = this.getArguments().getString("optionOne", "");
            receivedOptionTwo = this.getArguments().getString("optionTwo", "");
            receivedOptionThree = this.getArguments().getString("optionThree", "");
            receivedOptionFour = this.getArguments().getString("optionFour", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_question_buttons, container, false);
        optionOne = v.findViewById(R.id.optionOne);
        optionTwo = v.findViewById(R.id.optionTwo);
        optionThree = v.findViewById(R.id.optionThree);
        optionFour = v.findViewById(R.id.optionFour);

        //depending what state the programme is going to be in will determine how many buttons are going to
        //to be displayed to on the screen

        if(receivedOptionOne.equals(""))
        {
            optionOne.setVisibility(View.INVISIBLE);
            optionOne.setClickable(false);
        }
        else
        {
            optionOne.setText(receivedOptionOne);
        }


        if(receivedOptionTwo.equals(""))
        {
            optionTwo.setVisibility(View.INVISIBLE);
            optionTwo.setClickable(false);
        }
        else
        {
            optionTwo.setText(receivedOptionTwo);
        }

        if(receivedOptionThree.equals(""))
        {
            optionThree.setVisibility(View.INVISIBLE);
            optionThree.setClickable(false);

        }
        else
        {
            optionThree.setText(receivedOptionThree);
        }


        if(receivedOptionFour.equals(""))
        {
            optionFour.setVisibility(View.INVISIBLE);
            optionFour.setClickable(false);
        }
        else
        {
            optionFour.setText(receivedOptionFour);
        }

        //setting the current views from the  options which were recieved in the programme

        return v;
    }

    public static void setTwoButtons()
    {
        currState = buttonNumbers.twoButtons;
    }

    public static void setThreeButtons()
    {
        currState = buttonNumbers.threeButtons;
    }

    public static void setFullButtons()
    {
        currState = buttonNumbers.allButtons;
    }
}