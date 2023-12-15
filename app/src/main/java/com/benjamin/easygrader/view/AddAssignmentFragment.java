package com.benjamin.easygrader.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.benjamin.easygrader.R;
import com.benjamin.easygrader.viewmodel.ManageAssignmentsViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAssignmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAssignmentFragment extends Fragment {
  private static final String TAG = "AddAssignmentFragment";
  private static final String ARG_COURSE_ID = "courseId";

  private int mCourseId;
  private Date mDueDate;

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

    mDueDateInput.setOnClickListener(v -> {
      DialogDatePicker dialogDatePicker = new DialogDatePicker();
      dialogDatePicker.show(getChildFragmentManager(), new DialogDatePicker.OnDateSetListener() {
        @Override
        public void onDateSetListener(Date date) {
          mDueDate = date;
          mDueDateInput.setText(mDueDate.toString());
        }
      });
    });

    mConfirmAddAssignmentBtn.setOnClickListener(v -> {
      String assignmentName = mAssignmentNameInput.getText().toString();
      String totalPoints = mTotalPointsInput.getText().toString();
      if (assignmentName.isEmpty() || totalPoints.isEmpty() || mDueDate == null) {
        Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
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

  public static class DialogDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "DialogDatePicker";
    OnDateSetListener mListener;

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

    public void show(FragmentManager manager, OnDateSetListener listener) {
      super.show(manager, TAG);
      mListener = listener;
    }

    public interface OnDateSetListener {
      void onDateSetListener(Date date);
    }
  }

}