package com.benjamin.easygrader.view;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.benjamin.easygrader.R;
import com.benjamin.easygrader.viewmodel.ManageAssignmentsViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAssignmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAssignmentFragment extends Fragment {
  private static final String TAG = "AddAssignmentFragment";
  private static final String ARG_COURSE_ID = "courseId";

  private int mCourseId;
  private LocalDateTime mDueDate;
  private LocalDateTime mSemesterEndDate;
  private final List<String> mAssignmentNames = new ArrayList<>();

  public AddAssignmentFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param courseId int representing the id of the course to add an assignment to.
   * @return A new instance of fragment AddAssignmentFragment.
   */
  public static AddAssignmentFragment newInstance(int courseId) {
    AddAssignmentFragment fragment = new AddAssignmentFragment();
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
    View view = inflater.inflate(R.layout.fragment_add_assignment, container, false);

    ManageAssignmentsViewModel mManageAssignmentsViewModel = new ViewModelProvider(this).get(ManageAssignmentsViewModel.class);

    TextInputEditText mAssignmentNameInput = view.findViewById(R.id.assignmentNameInputText);
    TextInputEditText mTotalPointsInput = view.findViewById(R.id.totalPointsInputText);
    Button mConfirmAddAssignmentBtn = view.findViewById(R.id.confirmAddAssignmentBtn);
    TextInputEditText mDueDateInput = view.findViewById(R.id.dueDateInputText);

    mManageAssignmentsViewModel.getSemesterEndDate(mCourseId).observe(getViewLifecycleOwner(), date -> {
      mSemesterEndDate = date;
    });

    mManageAssignmentsViewModel.getAssignmentsByCourseId(mCourseId).observe(getViewLifecycleOwner(), assignments -> {
      for (int i = 0; i < assignments.size(); i++) {
        mAssignmentNames.add(assignments.get(i).getName());
      }
    });

    mDueDateInput.setOnClickListener(v -> {
      DatePickerDialog datePicker = new DatePickerDialog(getContext());
      datePicker.setOnDateSetListener((view1, year, month, day) -> {
        mDueDate = LocalDateTime.of(year, month+1, day, 23, 59, 59);
        mDueDateInput.setText(mDueDate.toString());
        Log.d(TAG, "onCreateView: mDueDate: " + mDueDate.toString());
      });
      datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
      datePicker.getDatePicker().setMaxDate(mSemesterEndDate.atZone(ZoneId.of("PST")).toEpochSecond() * 1000);
      datePicker.show();
    });

    mConfirmAddAssignmentBtn.setOnClickListener(v -> {
      String assignmentName = mAssignmentNameInput.getText().toString();
      String totalPoints = mTotalPointsInput.getText().toString();
      mAssignmentNameInput.setError(null);
      if (assignmentName.isEmpty() || totalPoints.isEmpty() || mDueDate == null) {
        Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
      } else if (mAssignmentNames.contains(assignmentName)){
        mAssignmentNameInput.setError("Assignment name already exists");
        Toast.makeText(getContext(), "Choose a different name. An assignment with that name already exists", Toast.LENGTH_LONG).show();
      } else {
        mManageAssignmentsViewModel.addAssignment(mCourseId, assignmentName, Integer.parseInt(totalPoints), mDueDate);
        Toast.makeText(getContext(), "Assignment added", Toast.LENGTH_SHORT).show();
        mAssignmentNameInput.setText("");
        mTotalPointsInput.setText("");
        mDueDateInput.setText("");
      }
    });

    return view;
  }
}