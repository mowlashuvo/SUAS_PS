package com.example.suas_ps;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    DatabaseReference databaseReference;
    List<ServiceRequestModel> list = new ArrayList<>();


    AVLoadingIndicatorView avi;

    RecyclerView recyclerView;
    ServiceRequestAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView noData;
    private ProgressDialog progressDialog;


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        recyclerView = view.findViewById(R.id.recycler_view);
        noData = view.findViewById(R.id.noData);
        avi = view.findViewById(R.id.aviView);
        adapter = new ServiceRequestAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
//        getData();
        return view;
    }



}