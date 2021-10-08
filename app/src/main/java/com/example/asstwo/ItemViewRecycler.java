package com.example.asstwo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
    private ArrayList<String> contactInformation;
    private Graph mathTestGraph;
    private ArrayList<Graph.Vertex> tempStudents;

    private RecyclerView rv;
    private ItemAdapter adapter;
    private LinearLayoutManager rvLayout;

    private Spinner sortOrder;
    private EditText searchUsers;


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        students = new ArrayList<>();
        contactInformation = new ArrayList<>();

        mathTestGraph = new Graph();
        //TODO: you will need to double check if this is the correct way to get the current context
        //of the application
        mathTestGraph = mathTestGraph.load(getContext());
        //TODO: you will need to make different states for laoding different things depending on
        //where you're in the application
        students = mathTestGraph.adminStudentLoad();

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

        //TODO: When you have time you should add the code for searching for the students here aswell

        //attaching the sorting methods which are available on the spinner
        sortOrder = (Spinner) view.findViewById(R.id.sortOrderSpinner);
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.sort_options, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortOrder.setAdapter(sortAdapter);

        return view;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder
    {
        private static final String TAG = "ItemViewHolder";
        private ImageView studentAvatar;
        private EditText studentName;
        private TextView studentScore;
        private LinearLayout currLayout;
        private Graph.Vertex vert;


        public ItemViewHolder(LayoutInflater li, ViewGroup parent) {
            super(li.inflate(R.layout.row_student, parent, false));

            //getting reference to all the major UI components in each row of the created list
            studentAvatar = itemView.findViewById(R.id.studentAvatarList);
            studentName = itemView.findViewById(R.id.studentNameList);
            studentScore = itemView.findViewById(R.id.studentScoreList);

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
            holder.bind(students.get(position));

        }

        @Override
        public int getItemCount() {
            return students.size();
        }
    }
}