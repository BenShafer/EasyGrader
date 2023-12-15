package com.benjamin.easygrader.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.benjamin.easygrader.R;
import com.benjamin.easygrader.viewmodel.ManageGradesViewModel;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnterGradesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnterGradesFragment extends Fragment {
  private static final String TAG = "EnterGradesFragment";
  private static final String ARG_ASSIGNMENT_ID = "assignmentId";

  private ManageGradesViewModel mManageGradesViewModel;
  private int mAssignmentId;

  public EnterGradesFragment() {
    // Required empty public constructor
  }

  public static EnterGradesFragment newInstance(int assignmentId) {
    EnterGradesFragment fragment = new EnterGradesFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_ASSIGNMENT_ID, assignmentId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mAssignmentId = getArguments().getInt(ARG_ASSIGNMENT_ID);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_enter_grades, container, false);

    mManageGradesViewModel = new ViewModelProvider(this).get(ManageGradesViewModel.class);

    Button mDoneBtn = view.findViewById(R.id.doneButton);
    RecyclerView mGradesRecyclerView = view.findViewById(R.id.gradesRecyclerView);
    mGradesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    GradeAdapter mGradeAdapter = new GradeAdapter(new HashMap<>(), mManageGradesViewModel);
    mGradesRecyclerView.setAdapter(mGradeAdapter);

    mManageGradesViewModel.getGradesForAssignment(mAssignmentId).observe(getViewLifecycleOwner(), grades -> {
      Log.d(TAG, "getGradeForCourse: grades: " + grades);
      mGradeAdapter.setGradeList(grades);
    });

    mDoneBtn.setOnClickListener(v -> {
      getParentFragmentManager().popBackStack();
    });

    return view;
  }

}