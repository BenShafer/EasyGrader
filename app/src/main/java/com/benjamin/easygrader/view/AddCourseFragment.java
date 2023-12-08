package com.benjamin.easygrader.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.benjamin.easygrader.R;
import com.benjamin.easygrader.model.User;
import com.benjamin.easygrader.viewmodel.ManageCoursesViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCourseFragment extends Fragment {
  private static final String TAG = "AddCourseFragment";
  private User mSelectedInstructor;
  
  public AddCourseFragment() {
    // Required empty public constructor
  }

  public static AddCourseFragment newInstance() { return new AddCourseFragment(); }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_course, container, false);

    ManageCoursesViewModel mManageCoursesViewModel = new ViewModelProvider(this).get(ManageCoursesViewModel.class);

    TextInputEditText mCourseInput = view.findViewById(R.id.courseInputText);
    TextInputEditText mSemesterInput = view.findViewById(R.id.semesterInputText);
    Spinner mInstructorSpinner = view.findViewById(R.id.instructorSpinner);
    Button mConfirmAddCourseBtn = view.findViewById(R.id.confirmAddCourseBtn);

    mManageCoursesViewModel.getAllInstructors().observe(requireActivity(), instructors -> {
      InstructorSpinnerAdapter mInstructorAdapter = new InstructorSpinnerAdapter(getContext(), instructors);
      mInstructorSpinner.setAdapter(mInstructorAdapter);
      mInstructorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          mSelectedInstructor = (User) parent.getItemAtPosition(position);
          Log.d(TAG, "onItemSelected: selected instructor: " + mSelectedInstructor);
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
          mSelectedInstructor = null;
        }
      });
    });

    mConfirmAddCourseBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String courseName = mCourseInput.getText().toString();
        String semester = mSemesterInput.getText().toString();
        if (courseName.isEmpty() || semester.isEmpty() || mSelectedInstructor == null) {
          Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
        } else {
          mManageCoursesViewModel.addCourse(courseName, semester, mSelectedInstructor.getId());
          Toast.makeText(getContext(), "Course added!", Toast.LENGTH_SHORT).show();
          mCourseInput.setText("");
          mSemesterInput.setText("");
        }
      }
    });

    return view;
  }
}