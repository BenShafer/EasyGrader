package com.benjamin.easygrader.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.benjamin.easygrader.R;
import com.benjamin.easygrader.model.Assignment;
import com.benjamin.easygrader.viewmodel.ManageAssignmentsViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModifyAssignmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyAssignmentFragment extends Fragment {
  private static final String TAG = "ModifyAssignmentFragmen";

  private static final String ARG_COURSE_ID = "courseId";

  private ManageAssignmentsViewModel mManageAssignmentsViewModel;

  private int mCourseId;
  private Date mDueDate;
  private Assignment mSelectedAssignment;
  private AssignmentAdapter mAssignmentAdapter;

  public ModifyAssignmentFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param courseId int representing the id of the course to add an assignment to.
   * @return A new instance of fragment AddAssignmentFragment.
   */
  public static ModifyAssignmentFragment newInstance(int courseId) {
    ModifyAssignmentFragment fragment = new ModifyAssignmentFragment();
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
    View view = inflater.inflate(R.layout.fragment_modify_assignment, container, false);

    mManageAssignmentsViewModel = new ViewModelProvider(this).get(ManageAssignmentsViewModel.class);

    Button mModifyAssignmentButton = view.findViewById(R.id.modifyAssignmentBtn);
    RecyclerView mAssignmentsRecyclerView = view.findViewById(R.id.modifyAssignmentsRecycler);

    mAssignmentsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

    mAssignmentAdapter = new AssignmentAdapter(new ArrayList<>(), assignment -> {
      mSelectedAssignment = assignment;
      Log.d(TAG, "onAssignmentSelected: selected " + mSelectedAssignment);
    });
    mAssignmentsRecyclerView.setAdapter(mAssignmentAdapter);

    mManageAssignmentsViewModel.getAssignmentsByCourseId(mCourseId).observe(getViewLifecycleOwner(), assignments -> {
      mAssignmentAdapter.setAssignmentList(assignments);
//      mAssignmentAdapter.notifyDataSetChanged();
    });

    mModifyAssignmentButton.setOnClickListener(v -> {
      if (mSelectedAssignment != null) {
        showModifyAssignmentDialog();
      } else {
        Toast.makeText(requireContext(), "Please select an assignment", Toast.LENGTH_SHORT).show();
      }
    });


    return view;
  }

  private void showModifyAssignmentDialog() {
    View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_modify_assignment, null);

    TextInputEditText mAssignmentNameText = dialogView.findViewById(R.id.modifyAssignmentNameInputText);
    TextInputEditText mPointsInputText = dialogView.findViewById(R.id.modifyPointsInputText);
    TextInputEditText mModifyDueDateInputText = dialogView.findViewById(R.id.modifyDueDateInputText);

    mAssignmentNameText.setText(mSelectedAssignment.getName());
    mPointsInputText.setText(String.valueOf(mSelectedAssignment.getPoints()));
    mDueDate = mSelectedAssignment.getDueDate();
    mModifyDueDateInputText.setText(mDueDate.toString());

    mModifyDueDateInputText.setOnClickListener(v -> {
      AddAssignmentFragment.DialogDatePicker dialogDatePicker = new AddAssignmentFragment.DialogDatePicker();
      dialogDatePicker.show(getChildFragmentManager(), new AddAssignmentFragment.DialogDatePicker.OnDateSetListener() {
        @Override
        public void onDateSetListener(Date date) {
          mDueDate = date;
          mModifyDueDateInputText.setText(mDueDate.toString());
        }
      });
    });

    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
    builder.setView(dialogView)
            .setTitle("Modify Assignment")
            .setPositiveButton("Update", (dialog, which) -> {
              String assignmentName = mAssignmentNameText.getText().toString();
              String points = mPointsInputText.getText().toString();
              String dueDate = mModifyDueDateInputText.getText().toString();

              if (assignmentName.isEmpty() || points.isEmpty() || dueDate.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
              } else {
                mSelectedAssignment.setName(assignmentName);
                mSelectedAssignment.setPoints(Integer.parseInt(points));
                mSelectedAssignment.setDueDate(mDueDate);
                mManageAssignmentsViewModel.update(mSelectedAssignment);
                dialog.dismiss();
              }
            })
            .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
            .create()
            .show();
  }

  public static class DialogDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "DialogDatePicker";
    AddAssignmentFragment.DialogDatePicker.OnDateSetListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      // Use the current date as the default date in the picker
      final Calendar c = Calendar.getInstance();
      int year = c.get(Calendar.YEAR);
      int month = c.get(Calendar.MONTH);
      int day = c.get(Calendar.DAY_OF_MONTH);
      // Create a new instance of DatePickerDialog and return it
      return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
      Date dueDate = new Date(year, month, day, 23, 59, 59);
      mListener.onDateSetListener(dueDate);
    }

    public void show(FragmentManager manager, AddAssignmentFragment.DialogDatePicker.OnDateSetListener listener) {
      super.show(manager, TAG);
      mListener = listener;
    }

    public interface OnDateSetListener {
      void onDateSetListener(Date date);
    }
  }

}