package com.example.asstwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

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
    private ArrayList<TestHistory> currTests;

    private RecyclerView rv;
    private ItemAdapter adapter;
    private LinearLayoutManager rvLayout;

    private Spinner sortOrder;
    private EditText searchUsers;

    public enum state {
        users,
        addresses,
        numbers,
        testViews
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
        currUser = Details.getName();

        switch(currMode)
        {
            case users:
                if (!mathTestGraph.isEmpty())
                {
                    students = mathTestGraph.adminStudentLoad();
                }

                break;
            case addresses: case numbers:
                //TODO: you will need to get the students name here so you can view teh addresses
                //which they will have saved under them

                //addresses and numbers are going to be loaded at teh same time as the oncreate
                //method is only created once

                if(currUser != null)
                {
                    Graph.Vertex currVert = mathTestGraph.getVertex(currUser);
                    emailAddress = currVert.getValue().getEmails();
                    phoneNumbers = currVert.getValue().getNumbers();
                }
                else
                {
                    Log.e(TAG, "can't load student contact emails at the moment");
                }
                break;

            case testViews:
                currTests = mathTestGraph.getVertex(currUser).getValue().getHistory();
                Log.e(TAG, "The amount of tests which were taken by the user: " + currTests.size());
                break;
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
        LinearLayout topBanner = view.findViewById(R.id.llOneRecycler);

        //TODO: When you have time you should add the code for searching for the students here aswellS
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.sort_options, android.R.layout.simple_spinner_item);
        switch(currMode)
        {
            case users:
                //attaching the sorting methods which are available on the spinner
                sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sortOrder.setAdapter(sortAdapter);

                break;

            case addresses: case numbers:
                //hiding the stuff which I don't need which is going to be the whole entire linear
                //layout including the spinner
                topBanner.setVisibility(View.GONE);
                //searchUsers.setMaxHeight(0);
                //sortOrder.setMinimumHeight(0);
                sortOrder.setVisibility(View.GONE);
                break;

            case testViews:
                //attaching the sorting methods which are available on the spinner
                sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sortOrder.setAdapter(sortAdapter);

                sortOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedItem = adapterView.getItemAtPosition(i).toString();
                        if (selectedItem.equals("Lowest"))
                        {
                            Collections.sort(currTests, Collections.reverseOrder());
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Collections.sort(currTests);
                            adapter.notifyDataSetChanged();

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        //do nothing in here for the time being
                    }
                });

                //seeing if we can attach an on click listener to the spinner object
                break;
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

    public static void testViewing()
    {
        currMode = state.testViews;
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
                    break;

                case addresses:
                    viewStudent.setVisibility(View.GONE);
                    studentName.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    break;

                case numbers:
                    viewStudent.setVisibility(View.GONE);
                    studentName.setInputType(InputType.TYPE_CLASS_PHONE);
                    break;

                case testViews:
                    //they should not be an option to delete students
                    deleteStudent.setVisibility(View.GONE);
                    break;

            }
        }

        public void bind(Graph.Vertex inVert)
        {
            this.vert = inVert;
            studentName.setText(inVert.getValue().getName());
            studentAvatar.setImageBitmap(inVert.getValue().getAvatar().getImage());
            studentName.setEnabled(false);
        }
        //TODO: you will have to create a bind method for the emails and the phone numbers here as well

        public void bindPhoneNumbers(String inPhoneNumber)
        {
            // we must update teh displayed names, and scores. However, for each one we have
            // to temporarily disable the corresponding event handler, or else the event
            // handler would assume the *user* has edited the informatio of the current edit
            // text box which we're viewing
            studentName.setHint(inPhoneNumber);
            studentAvatar.setVisibility(View.GONE);
            studentScore.setVisibility(View.GONE);
        }

        public void bindEmailAddresses(String inEmail)
        {
            // we must update teh displayed names, and scores. However, for each one we have
            // to temporarily disable the corresponding event handler, or else the event
            // handler would assume the *user* has edited the informatio of the current edit
            // text box which we're viewing

            studentName.setHint(inEmail);
            studentAvatar.setVisibility(View.GONE);
            studentScore.setVisibility(View.GONE);
            studentAvatar.setMaxWidth(0);
        }

        public void bindTestHistory(TestHistory inHistory)
        {
            studentAvatar.setVisibility(View.GONE);
            studentName.setEnabled(false);
            studentName.setText(inHistory.getTestTitle());
            Log.e(TAG, "The last score of the test which was taken: " + inHistory.getScore());
            //TODO: for some reason this is not working at the current moment
            studentScore.setText(Integer.toString(inHistory.getScore()));
        }

        public void bindTestResults(TestHistory inHistory)
        {

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

                    holder.deleteStudent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mathTestGraph.delVertex(holder.studentName.getText().toString());
                            mathTestGraph.save(getActivity());
                            students = mathTestGraph.adminStudentLoad();
                            notifyDataSetChanged();
                        }
                    });
                    break;

                case addresses:
                    holder.bindEmailAddresses(emailAddress.get(position));

                    holder.deleteStudent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String name = Details.getName();
                            Graph.Vertex currVert = mathTestGraph.getVertex(name);
                            ArrayList<String> currAddresses = currVert.getValue().getEmails();
                            currAddresses.remove(holder.studentName.getHint().toString());
                            emailAddress = currAddresses;
                            notifyDataSetChanged();
                        }
                    });
                    break;

                case numbers:
                    holder.bindPhoneNumbers(phoneNumbers.get(position));

                    holder.deleteStudent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String name = Details.getName();
                            Graph.Vertex currVert = mathTestGraph.getVertex(name);
                            ArrayList<String> currNumbers = currVert.getValue().getNumbers();
                            currNumbers.remove(holder.studentName.getHint().toString());
                            phoneNumbers = currNumbers;
                            notifyDataSetChanged();
                        }
                    });
                    break;

                case testViews:
                    holder.bindTestHistory(currTests.get(position));


                    //TODO: you might need to add a delete method here for deleting your student
                    break;

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
                    break;

                case testViews:
                    retSize = currTests.size();
                    break;
            }

            return retSize;
        }
    }
}