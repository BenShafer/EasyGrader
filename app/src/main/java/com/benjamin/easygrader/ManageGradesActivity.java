package com.benjamin.easygrader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.benjamin.easygrader.util.Destination;
import com.benjamin.easygrader.util.IntentFactory;
import com.benjamin.easygrader.view.FinalizeGradesFragment;
import com.benjamin.easygrader.view.GradeAssignmentFragment;

public class ManageGradesActivity extends AppCompatActivity {
  private static final String TAG = "ManageGradesActivity";

  private int mCourseId;
  private String mCourseName;
  private int mUserId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_manage_grades);

    Destination destination = (Destination) getIntent().getSerializableExtra(IntentFactory.DESTINATION_EXTRA);
    mCourseId = getIntent().getIntExtra(IntentFactory.COURSE_ID_EXTRA, -1);
    mCourseName = getIntent().getStringExtra(IntentFactory.COURSE_NAME_EXTRA);
    mUserId = getIntent().getIntExtra(IntentFactory.USER_ID_EXTRA, -1);

    TextView header = findViewById(R.id.headerText);

    if (savedInstanceState == null) {
      switch (destination) {
        case GRADE_ASSIGNMENT:
          header.setText("Grade assignments for " + mCourseName);
          openFragment(GradeAssignmentFragment.newInstance(mCourseId));
          break;
        case FINALIZE_GRADES:
          header.setText("Finalize Grades");
          openFragment(FinalizeGradesFragment.newInstance(mUserId));
          break;
      }
    }
  }

  private void openFragment(Fragment fragment) {
    getSupportFragmentManager().beginTransaction()
        .setReorderingAllowed(true)
        .add(R.id.manageGradesFragmentView, fragment, null)
        .commit();
  }
}