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
import android.widget.Toast;

import com.benjamin.easygrader.R;
import com.benjamin.easygrader.model.Assignment;
import com.benjamin.easygrader.viewmodel.ManageGradesViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GradeAssignmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GradeAssignmentFragment extends Fragment {
  private static final String TAG = "GradeAssignmentFragment";
  private static final String ARG_COURSE_ID = "courseId";

  private ManageGradesViewModel mManageGradesViewModel;

  private int mCourseId;
  private Assignment mSelectedAssignment;
  private AssignmentAdapter mAssignmentAdapter;


  public GradeAssignmentFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param courseId int representing the id of the course to add an assignment to.
   * @return A new instance of fragment AddAssignmentFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static GradeAssignmentFragment newInstance(int courseId) {
    GradeAssignmentFragment fragment = new GradeAssignmentFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_COURSE_ID, courseId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mCourseId = getArguments().getInt(ARG_COURSE_ID);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_grade_assignment, container, false);

    mManageGradesViewModel = new ViewModelProvider(this).get(ManageGradesViewModel.class);

    RecyclerView mGradesRecyclerView = view.findViewById(R.id.gradeAssignmentsRecycler);
    Button mGradeAssignmentButton = view.findViewById(R.id.gradeAssignmentBtn);

    mGradesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mAssignmentAdapter = new AssignmentAdapter(new ArrayList<>(), mManageGradesViewModel, assignments -> {
      mSelectedAssignment = assignments;
    });
    mGradesRecyclerView.setAdapter(mAssignmentAdapter);

    mManageGradesViewModel.getAssignmentsByCourseId(mCourseId).observe(getViewLifecycleOwner(), assignments -> {
      mAssignmentAdapter.setAssignmentList(assignments);
    });

    mGradeAssignmentButton.setOnClickListener(v -> {
      if (mSelectedAssignment != null) {
        Log.d(TAG, "Grading assignment: " + mSelectedAssignment);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.manageGradesFragmentView, EnterGradesFragment.newInstance(mSelectedAssignment.getId()))
                .addToBackStack(null)
                .commit();
      } else {
        Toast.makeText(requireContext(), "Please select an assignment", Toast.LENGTH_SHORT).show();
      }
    });

    return view;
  }
}