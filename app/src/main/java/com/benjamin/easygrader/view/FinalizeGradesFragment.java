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
import com.benjamin.easygrader.model.Course;
import com.benjamin.easygrader.viewmodel.ManageGradesViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FinalizeGradesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinalizeGradesFragment extends Fragment {
  private static final String TAG = "FinalizeGradesFragment";
  private static final String ARG_USER_ID = "userId";

  private int mUserId;
  private Course mSelectedCourse = null;
  private boolean mIsFinalizable = false;

  public FinalizeGradesFragment() {
    // Required empty public constructor
  }

  public static FinalizeGradesFragment newInstance(int userId) {
    FinalizeGradesFragment fragment = new FinalizeGradesFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_USER_ID, userId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mUserId = getArguments().getInt(ARG_USER_ID);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_finalize_grades, container, false);

    ManageGradesViewModel mManageGradesViewModel = new ViewModelProvider(this).get(ManageGradesViewModel.class);

    Button mFinalizeBtn = view.findViewById(R.id.finalizeButton);
    RecyclerView mCourseGradesRecycler = view.findViewById(R.id.courseGradesRecycler);

    mCourseGradesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    CourseGradesAdapter mCourseGradesAdapter = new CourseGradesAdapter(new ArrayList<>(), mManageGradesViewModel,new CourseGradesAdapter.OnCourseSelectedListener() {
      @Override
      public void onCourseSelected(Course course, boolean isFinalizable) {
        Log.d(TAG, "onCourseSelected: selected course: " + course);
        mSelectedCourse = course;
        mIsFinalizable = isFinalizable;
      }
    });
    mCourseGradesRecycler.setAdapter(mCourseGradesAdapter);

    mManageGradesViewModel.getInstructorsUnfinalizedCourses(mUserId).observe(getViewLifecycleOwner(), unfinalizedCourses -> {
      Log.d(TAG, "onCreateView: unfinalized courses: " + unfinalizedCourses);
      mCourseGradesAdapter.setCourseGradesList(unfinalizedCourses);
    });

    mFinalizeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mSelectedCourse != null) {
          if (mIsFinalizable) {
            Log.d(TAG, "finalize grades for course: " + mSelectedCourse);
            mManageGradesViewModel.finalizeGradesForCourse(mSelectedCourse);
            mSelectedCourse = null;
          } else {
            Toast.makeText(getContext(), "Unable to finalize. Ungraded assignments exist", Toast.LENGTH_LONG).show();
          }
        } else {
          Toast.makeText(getContext(), "No courses selected", Toast.LENGTH_SHORT).show();
        }
      }
    });

    return view;
  }
}