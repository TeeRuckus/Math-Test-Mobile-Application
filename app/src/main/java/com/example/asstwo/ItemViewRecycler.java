package com.example.asstwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemViewRecycler#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemViewRecycler extends Fragment {

    private static final String TAG = "ItemViewRecycler";
    //the places where we're going to store the data which we have read into the programme
    private ArrayList<Graph.Vertex> students;
    private ArrayList<String> phoneNumbers;
    private ArrayList<String> emailAddress;
    private String currUser;
    private Graph mathTestGraph;
    private ArrayList<Graph.Vertex> tempStudents;

    private RecyclerView rv;
    private ItemAdapter adapter;
    private LinearLayoutManager rvLayout;

    private Spinner sortOrder;
    private EditText searchUsers;

    public enum state {
        users,
        addresses,
        numbers
    }

    private static state currMode;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ItemViewRecycler() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemViewRecycler.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemViewRecycler newInstance(String param1, String param2) {
        ItemViewRecycler fragment = new ItemViewRecycler();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mathTestGraph.save(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        mathTestGraph.save(getActivity());
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        students = new ArrayList<>();

        mathTestGraph = new Graph();
        //TODO: you will need to double check if this is the correct way to get the current context
        //of the application
        mathTestGraph = mathTestGraph.load(getActivity());
        //TODO: you will need to make different states for laoding different things depending on
        //where you're in the application

        switch(currMode)
        {
            case users:
                if (!mathTestGraph.isEmpty())
                {
                    students = mathTestGraph.adminStudentLoad();
                }

                break;
            case addresses:
                //TODO: you will need to get the students name here so you can view teh addresses
                //which they will have saved under them
                currUser = Details.getName();

                if(currUser != null)
                {
                    Graph.Vertex currVert = mathTestGraph.getVertex(currUser);
                    emailAddress = currVert.getValue().getEmails();
                    break;
                }
                else
                {
                    Log.e(TAG, "can't load student contact information at the moment");
                }
                break;
            case numbers:
                currUser = Details.getName();
                if(currUser != null)
                {
                    Graph.Vertex currVert = mathTestGraph.getVertex(currUser);
                    phoneNumbers = currVert.getValue().phoneNumbers;
                    break;
                }
                else
                {
                    Log.e(TAG, "can't load student contact information at the moment");
                }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_view_recycler, container, false);
        rv = (RecyclerView) view.findViewById(R.id.studentList);

        //setting up the recycler view
        adapter = new ItemAdapter();
        rvLayout = new LinearLayoutManager(getActivity());
        rv.setAdapter(adapter);
        rv.setLayoutManager(rvLayout);
        searchUsers = (EditText) view.findViewById(R.id.searchStudent);
        sortOrder = (Spinner) view.findViewById(R.id.sortOrderSpinner);

        //TODO: When you have time you should add the code for searching for the students here aswellS
        switch(currMode)
        {
            case users:
                //attaching the sorting methods which are available on the spinner
                ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(
                        getContext(), R.array.sort_options, android.R.layout.simple_spinner_item);
                sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sortOrder.setAdapter(sortAdapter);

                break;

            case addresses: case numbers:
                //hiding the stuff which I don't need which is going to be the whole entire linear
                //layout including the spinner
                LinearLayout topBanner = view.findViewById(R.id.llOneRecycler);
                topBanner.setVisibility(View.INVISIBLE);
                searchUsers.setMaxHeight(0);
                sortOrder.setMinimumHeight(0);
                sortOrder.setVisibility(View.INVISIBLE);
        }


        return view;
    }

    public static void usersViewing()
    {
        currMode = state.users;
    }

    public static void addressesViewing()
    {
        currMode = state.addresses;
    }

    public static void numbersViewing()
    {
        currMode = state.numbers;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder
    {
        private static final String TAG = "ItemViewHolder";
        private ImageView studentAvatar;
        private EditText studentName;
        private TextView studentScore;
        private ImageButton viewStudent;
        private ImageButton deleteStudent;
        private LinearLayout currLayout;
        private Graph.Vertex vert;


        public ItemViewHolder(LayoutInflater li, ViewGroup parent) {
            super(li.inflate(R.layout.row_student, parent, false));

            //getting reference to all the major UI components in each row of the created list
            studentAvatar = itemView.findViewById(R.id.studentAvatarList);
            studentName = itemView.findViewById(R.id.studentNameList);
            studentScore = itemView.findViewById(R.id.studentScoreList);
            viewStudent = itemView.findViewById(R.id.viewStudentRow);
            deleteStudent = itemView.findViewById(R.id.deleteStudentRow);

            switch(currMode)
            {
                case users:
                    viewStudent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), Details.class);
                            intent.putExtra("name", studentName.getText().toString());
                            startActivity(intent);
                        }
                    });

                    deleteStudent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mathTestGraph.delVertex(studentName.getText().toString());
                            mathTestGraph.save(getActivity());
                            getActivity().recreate();
                        }
                    });
                    break;

                case addresses:
                    viewStudent.setVisibility(View.INVISIBLE);
                    viewStudent.setClickable(false);
                    viewStudent.setEnabled(false);
                    viewStudent.setMaxWidth(0);

                    //TODO: you will have to make this toggle between email addressed and phone
                    //numbers in your programme. COME BACK TO THIS AND FIX IT
                    studentName.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                    deleteStudent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //delete that whole entire email address of phone number from the programme
                        }
                    });
                    //hiding the buttons from the view
                    break;

                case numbers:
                    viewStudent.setVisibility(View.INVISIBLE);
                    viewStudent.setClickable(false);
                    viewStudent.setEnabled(false);
                    viewStudent.setMaxWidth(0);

                    //TODO: you will have to make this toggle between email addressed and phone
                    //numbers in your programme. COME BACK TO THIS AND FIX IT
                    studentName.setInputType(InputType.TYPE_CLASS_PHONE);

                    deleteStudent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //this will remove teh whole current phone number from the user
                        }
                    });
                    //hiding the buttons from the view
                    break;

            }


            //TODO: add the functionality to view students detail page, and to delete the current
            //student as well

            //TODO: when you're in the email or the phone number order I should put a text watcher here
        }

        public void bind(Graph.Vertex inVert)
        {
            this.vert = inVert;
            // we must update teh displayed names, and scores. However, for each one we have
            // to temporarily disable the corresponding event handler, or else the event
            // handler would assume the *user* has edited the informatio of the current edit
            // text box which we're viewing

            studentName.setText(inVert.getValue().getName());
            studentAvatar.setImageBitmap(inVert.getValue().getAvatar().getImage());
            studentName.setEnabled(false);
        }
        //TODO: you will have to create a bind method for the emails and the phone numbers here as well

        public void bindPhoneNumbers(String inPhoneNumber)
        {
            studentName.setHint(inPhoneNumber);
            studentAvatar.setVisibility(View.GONE);
            studentScore.setVisibility(View.GONE);
        }

        public void bindEmailAddresses(String inEmail)
        {
            studentName.setHint(inEmail);
            studentAvatar.setVisibility(View.GONE);
            studentScore.setVisibility(View.GONE);
            studentAvatar.setMaxWidth(0);
        }
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder>
    {
        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
        {
            return new ItemViewHolder(LayoutInflater.from(getActivity()), viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            switch (currMode)
            {
                case users:
                    holder.bind(students.get(position));
                    break;

                case addresses:
                    holder.bindEmailAddresses(emailAddress.get(position));
                    //this is going to be the same view, so we need to tell it that the data has changed
                    //notifyDataSetChanged();
                    break;

                case numbers:
                    holder.bindPhoneNumbers(phoneNumbers.get(position));

            }

        }

        @Override
        public int getItemCount() {
            int retSize = 0;
            switch(currMode)
            {
                case users:
                    retSize = students.size();
                    break;
                case addresses:
                    retSize = emailAddress.size();
                    break;
                case numbers:
                    retSize = phoneNumbers.size();
            }

            return retSize;
        }
    }
}