package com.benjamin.easygrader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.benjamin.easygrader.model.Course;
import com.benjamin.easygrader.model.User;
import com.benjamin.easygrader.util.IntentFactory;
import com.benjamin.easygrader.view.CourseAdapter;
import com.benjamin.easygrader.viewmodel.SelectCourseViewModel;

import java.util.HashMap;

public class SelectCourseActivity extends AppCompatActivity {

  private static final String TAG = "SelectCourseActivity";

  private int mUserId;
  private User mUser;
  private Course mSelectedCourse;

  Button mSelectCourseBtn;
  RecyclerView mSelectCourseRecyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_course);

    SelectCourseViewModel mSelectCourseViewModel = new ViewModelProvider(this).get(SelectCourseViewModel.class);
    mUserId = getIntent().getIntExtra(IntentFactory.USER_ID_EXTRA, -1);
    mSelectCourseViewModel.loadCoursesForUser(mUserId);

    TextView header = findViewById(R.id.headerText);
    header.setText("Select a course");
    mSelectCourseRecyclerView = findViewById(R.id.selectCourseRecyclerView);
    mSelectCourseBtn = findViewById(R.id.selectCourseBtn);

    mSelectCourseViewModel.getLoggedInUser().observe(this, user -> {
      Log.d(TAG, "Observed getLoggedInUser: " + user);
      if (user != null) {
        mUser = user;
      }
    });

    mSelectCourseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    CourseAdapter mCourseAdapter = new CourseAdapter(new HashMap<>(), new CourseAdapter.OnCourseSelectedListener() {
      @Override
      public void onCourseSelected(Course course) {
        mSelectedCourse = course;
        Log.d(TAG, "Course selected: " + mSelectedCourse);
      }
    });
    mSelectCourseRecyclerView.setAdapter(mCourseAdapter);

    mSelectCourseViewModel.coursesForUser.observe(this, courses -> {
      Log.d(TAG, "Observed getCoursesForUser: " + mUser);
      if (courses != null) {
        mCourseAdapter.setCourseList(courses);
      }
    });
    mSelectCourseRecyclerView.setAdapter(mCourseAdapter);

    mSelectCourseBtn.setOnClickListener(view -> {
      Log.d(TAG, "Selected course: " + mSelectedCourse);
      if (mSelectedCourse != null) {
        Intent intent = new Intent();
        intent.putExtra(IntentFactory.COURSE_ID_EXTRA, mSelectedCourse.getId());
        intent.putExtra(IntentFactory.COURSE_NAME_EXTRA, mSelectedCourse.getName());
        setResult(RESULT_OK, intent);
        finish();
      } else {
        Toast.makeText(this, "Please select a course above", Toast.LENGTH_SHORT).show();
      }
    });
  }
}