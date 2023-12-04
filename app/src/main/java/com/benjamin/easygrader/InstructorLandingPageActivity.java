package com.benjamin.easygrader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.benjamin.easygrader.util.IntentFactory;
import com.benjamin.easygrader.viewmodel.LandingViewModel;

public class InstructorLandingPageActivity extends AppCompatActivity {

  private static final String TAG = "InstructorLandingPageActivity";
  private LandingViewModel mLandingViewModel;

  private int mUserId;
  private int mCourseId;

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



  }
}