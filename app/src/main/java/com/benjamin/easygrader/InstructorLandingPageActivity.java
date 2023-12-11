package com.benjamin.easygrader;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.benjamin.easygrader.util.Destination;
import com.benjamin.easygrader.util.IntentFactory;
import com.benjamin.easygrader.viewmodel.LandingViewModel;

public class InstructorLandingPageActivity extends AppCompatActivity {

  private static final String TAG = "InstructorLandingPageActivity";
  private LandingViewModel mLandingViewModel;

  private Destination mDestination;
  private int mUserId;
  private int mCourseId;
  private String mCourseName;

  Button mAddAssignmentButton;
  Button mRemoveAssignmentButton;
  Button mModifyAssignmentButton;
  Button mGradeAssignmentsButton;
  Button mFinalizeGradesButton;
  Button mLogoutButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_instructor_landing_page);

    mLandingViewModel = new ViewModelProvider(this).get(LandingViewModel.class);
    mAddAssignmentButton = findViewById(R.id.addAssignmentButton);
    mRemoveAssignmentButton = findViewById(R.id.removeAssignmentButton);
    mModifyAssignmentButton = findViewById(R.id.modifyAssignmentButton);
    mGradeAssignmentsButton = findViewById(R.id.gradeAssignmentsButton);
    mFinalizeGradesButton = findViewById(R.id.finalizeGradesButton);
    mLogoutButton = findViewById(R.id.logoutButton);

    mUserId = getIntent().getIntExtra(IntentFactory.USER_ID_EXTRA, -1);
    mLandingViewModel.loadLoggedInUser(mUserId);

    mLandingViewModel.getLoggedInUser().observe(this, user -> {
      if (user != null) {
        TextView header = findViewById(R.id.headerText);
        header.setText(String.format("Welcome, %s!", user.getUsername()));
      }
    });

    mLogoutButton.setOnClickListener(view -> {
      SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = sharedPref.edit();
      editor.putInt(getString(R.string.logged_in_user_id_key), -1);
      editor.putBoolean(getString(R.string.user_is_admin_key), false);
      editor.commit();
      startActivity(IntentFactory.getMainActivityIntent(getApplicationContext()));
    });

    ActivityResultLauncher<Intent> startSelectCourseActivityForId = registerForActivityResult(
      new ActivityResultContracts.StartActivityForResult(),
      result -> {
        if (result.getResultCode() == RESULT_OK) {
          Intent data = result.getData();
          if (data != null) {
            mCourseId = data.getIntExtra(IntentFactory.COURSE_ID_EXTRA, -1);
            mCourseName = data.getStringExtra(IntentFactory.COURSE_NAME_EXTRA);
            Log.d(TAG, "result callback: courseId = " + mCourseId + ", destination = " + mDestination);
            if (mDestination == Destination.GRADE_ASSIGNMENT)
              startActivity(IntentFactory.getManageGradesActivityIntent(getApplicationContext(), mCourseId, mCourseName, mUserId, mDestination));
            else {
              startActivity(IntentFactory.getManageAssignmentsActivityIntent(getApplicationContext(), mCourseId, mCourseName, mDestination));
            }
          }
        }
    });


    mAddAssignmentButton.setOnClickListener(view -> {
      startSelectCourseActivityForId.launch(IntentFactory.getSelectCourseActivityIntent(getApplicationContext(), mUserId));
      mDestination = Destination.ADD_ASSIGNMENT;
    });

    mRemoveAssignmentButton.setOnClickListener(view -> {
      startSelectCourseActivityForId.launch(IntentFactory.getSelectCourseActivityIntent(getApplicationContext(), mUserId));
      mDestination = Destination.REMOVE_ASSIGNMENT;
    });

    mModifyAssignmentButton.setOnClickListener(view -> {
      startSelectCourseActivityForId.launch(IntentFactory.getSelectCourseActivityIntent(getApplicationContext(), mUserId));
      mDestination = Destination.MODIFY_ASSIGNMENT;
    });

    mGradeAssignmentsButton.setOnClickListener(view -> {
      startSelectCourseActivityForId.launch(IntentFactory.getSelectCourseActivityIntent(getApplicationContext(), mUserId));
      mDestination = Destination.GRADE_ASSIGNMENT;
    });

    mFinalizeGradesButton.setOnClickListener(view -> {
      startActivity(IntentFactory.getManageGradesActivityIntent(getApplicationContext(), -1, null, mUserId, Destination.FINALIZE_GRADES));
    });
  }
}