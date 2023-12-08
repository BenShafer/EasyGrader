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
import com.benjamin.easygrader.model.Course;
import com.benjamin.easygrader.viewmodel.ManageCoursesViewModel;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemoveCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemoveCourseFragment extends Fragment {

  private static final String TAG = "RemoveCourseFragment";
  private Course mSelectedCourse = null;

  public RemoveCourseFragment() {
    // Required empty public constructor
  }

  public static RemoveCourseFragment newInstance() {
    return new RemoveCourseFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_remove_course, container, false);

    ManageCoursesViewModel mManageCoursesViewModel = new ViewModelProvider(this).get(ManageCoursesViewModel.class);

    RecyclerView mCourseRecyclerView = view.findViewById(R.id.courseRecyclerView);
    Button mRemoveCourseBtn = view.findViewById(R.id.removeCourseBtn);
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

    mCourseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    CourseAdapter mCourseAdapter = new CourseAdapter(new HashMap<>(), new CourseAdapter.OnCourseSelectedListener() {
      @Override
      public void onCourseSelected(Course course) {
        mSelectedCourse = course;
        Log.d(TAG, "onCourseSelected: selected course: " + mSelectedCourse);
      }
    });
    mCourseRecyclerView.setAdapter(mCourseAdapter);

    mManageCoursesViewModel.getAllCoursesWithInstructor().observe(getViewLifecycleOwner(), courses -> {
      mCourseAdapter.setCourseList(courses);
    });

    mRemoveCourseBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mSelectedCourse != null) {
          builder.setTitle("Remove course").setMessage("Are you sure you want to remove " + mSelectedCourse.getName() + "?")
                  .setCancelable(false)
                  .setPositiveButton(R.string.confirm, (dialog, id) -> {
                    Log.d(TAG, "dialog: removing course: " + mSelectedCourse);
                    mManageCoursesViewModel.removeCourse(mSelectedCourse);
                    mSelectedCourse = null;
                    mCourseAdapter.notifyItemRemoved(mCourseAdapter.mSelectedPosition);
                    mCourseAdapter.mSelectedPosition = -1;
                    dialog.dismiss();
                    Toast.makeText(requireContext(), "Course removed", Toast.LENGTH_SHORT).show();
                  })
                  .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    Log.d(TAG, "dialog: canceling remove course");
                    dialog.dismiss();
                  });
          builder.create().show();
        }
      }
    });

    return view;
  }
}