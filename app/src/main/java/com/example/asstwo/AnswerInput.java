package com.example.asstwo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnswerInput#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnswerInput extends Fragment {

    //my own defined values for the fragment
    private EditText inputtedAnswer;
    private Button answer;
    private AnswerInputListener listener;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AnswerInput() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnswerInput.
     */
    // TODO: Rename and change types and number of parameters
    public static AnswerInput newInstance(String param1, String param2) {
        AnswerInput fragment = new AnswerInput();
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

    public interface AnswerInputListener {
        void onClickAnswer(CharSequence input);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_answer_input, container, false);
        inputtedAnswer = v.findViewById(R.id.enterAnswerTest);
        answer = v.findViewById(R.id.answerBttnTest);

        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence userInput = inputtedAnswer.getText();
                listener.onClickAnswer(userInput);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //checking if the current activity which we're attaching ourselves too has implemented the
        //necessary interfaces
        if(context instanceof AnswerInputListener){
            listener = (AnswerInputListener) context;
        }
        else
        {
            throw  new RuntimeException(context.toString()
            + " must implement AnswerInputListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //removing the listener when it has being detached from the main activity
        listener = null;
    }
}