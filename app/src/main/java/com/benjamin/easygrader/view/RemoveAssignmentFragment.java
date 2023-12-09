package com.benjamin.easygrader.view;

import android.app.AlertDialog;
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
import com.benjamin.easygrader.viewmodel.ManageAssignmentsViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemoveAssignmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemoveAssignmentFragment extends Fragment {
  private static final String TAG = "RemoveAssignmentFragment";
  private static final String ARG_COURSE_ID = "courseId";

  private int mCourseId;
  private Assignment mSelectedAssignment = null;

  public RemoveAssignmentFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param courseId int representing the id of the course to add an assignment to.
   * @return A new instance of fragment AddAssignmentFragment.
   */
  public static RemoveAssignmentFragment newInstance(int courseId) {
    RemoveAssignmentFragment fragment = new RemoveAssignmentFragment();
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
    View view = inflater.inflate(R.layout.fragment_remove_assignment, container, false);

    ManageAssignmentsViewModel mManageAssignmentsViewModel = new ViewModelProvider(this).get(ManageAssignmentsViewModel.class);

    Button mRemoveAssignmentButton = view.findViewById(R.id.removeAssignmentBtn);
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    RecyclerView mAssignmentsRecyclerView = view.findViewById(R.id.removeAssignmentsRecycler);

    mAssignmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    AssignmentAdapter mAssignmentAdapter = new AssignmentAdapter(new ArrayList<>(), new AssignmentAdapter.OnAssignmentSelectedListener() {
      @Override
      public void onAssignmentSelected(Assignment assignment) {
        mSelectedAssignment = assignment;
        Log.d(TAG, "onAssignmentSelected: selected " + mSelectedAssignment);
      }
    });
    mAssignmentsRecyclerView.setAdapter(mAssignmentAdapter);

    mManageAssignmentsViewModel.getAssignmentsByCourseId(mCourseId).observe(requireActivity(), assignments -> {
      mAssignmentAdapter.setAssignmentList(assignments);
    });

    mRemoveAssignmentButton.setOnClickListener(v -> {
      if (mSelectedAssignment != null) {

        builder.setTitle("Remove Assignment").setMessage("Are you sure you want to remove " + mSelectedAssignment.getName() + "?")
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, (dialog, id) -> {
                  Log.d(TAG, "Dialog: removing assignment " + mSelectedAssignment);
                  mManageAssignmentsViewModel.deleteAssignment(mSelectedAssignment);
                  mSelectedAssignment = null;
                  mAssignmentAdapter.notifyItemRemoved(mAssignmentAdapter.mSelectedPosition);
                  mAssignmentAdapter.mSelectedPosition = -1;
                  dialog.dismiss();
                  Toast.makeText(requireContext(), "Assignment removed", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                  Log.d(TAG, "dialog remove assignment cancelled");
                  dialog.dismiss();
                });
        builder.create().show();
      }
    });

    return view;
  }
}