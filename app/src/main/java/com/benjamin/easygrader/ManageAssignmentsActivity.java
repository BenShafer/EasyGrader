package com.benjamin.easygrader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.benjamin.easygrader.util.Destination;
import com.benjamin.easygrader.util.IntentFactory;
import com.benjamin.easygrader.view.AddAssignmentFragment;
import com.benjamin.easygrader.view.ModifyAssignmentFragment;
import com.benjamin.easygrader.view.RemoveAssignmentFragment;

public class ManageAssignmentsActivity extends AppCompatActivity {
  private static final String TAG = "ManageAssignmentsActivity";

  private int mCourseId;
  private String mCourseName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_manage_assignments);

    Destination destination = (Destination) getIntent().getSerializableExtra(IntentFactory.DESTINATION_EXTRA);
    mCourseId = getIntent().getIntExtra(IntentFactory.COURSE_ID_EXTRA, -1);
    mCourseName = getIntent().getStringExtra(IntentFactory.COURSE_NAME_EXTRA);

    TextView header = findViewById(R.id.headerText);

    if (savedInstanceState == null) {
      switch (destination) {
        case ADD_ASSIGNMENT:
          header.setText("Add a new assignment for " + mCourseName);
          openFragment(AddAssignmentFragment.newInstance(mCourseId));
          break;
        case REMOVE_ASSIGNMENT:
          header.setText("Remove an assignment from " + mCourseName);
          openFragment(RemoveAssignmentFragment.newInstance(mCourseId));
        case MODIFY_ASSIGNMENT:
          header.setText("Modify an assignment in " + mCourseName);
          openFragment(ModifyAssignmentFragment.newInstance(mCourseId));
          break;
        case GRADE_ASSIGNMENT:
          header.setText("Grade assignments for " + mCourseName);
//          openFragment(new GradeAssignmentFragment());
          break;
      }
    }

  }

  private void openFragment(Fragment fragment) {
    getSupportFragmentManager().beginTransaction()
        .setReorderingAllowed(true)
        .add(R.id.manageAssignmentsFragmentView, fragment, null)
        .commit();
  }

}