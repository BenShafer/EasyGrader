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

public class AdminLandingPageActivity extends AppCompatActivity {

  private static final String TAG = "AdminLandingPageActivity";
  private LandingViewModel mLandingViewModel;

  private int mUserId;
  private int mCourseId;

  Button mAddUserButton;
  Button mDeleteUserButton;
  Button mAddCourseButton;
  Button mRemoveCourseButton;
  Button mEnrollStudentsButton;
  Button mLogoutButton;
  Button mAdminButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_landing_page);

    mLandingViewModel = new ViewModelProvider(this).get(LandingViewModel.class);
    mAddUserButton = findViewById(R.id.addUserButton);
    mDeleteUserButton = findViewById(R.id.deleteUserButton);
    mAddCourseButton = findViewById(R.id.addCourseButton);
    mRemoveCourseButton = findViewById(R.id.removeCourseButton);
    mEnrollStudentsButton = findViewById(R.id.enrollStudentsButton);
    mLogoutButton = findViewById(R.id.logoutButton);
    mAdminButton = findViewById(R.id.adminButton);

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
              Log.d(TAG, "result callback: courseId = " + mCourseId);
              startActivity(IntentFactory.getEnrollStudentActivityIntent(getApplicationContext(), mCourseId));
            }
          }
        });

    mAddUserButton.setOnClickListener(view -> {
      startActivity(IntentFactory.getManageUsersActivityIntent(getApplicationContext(), Destination.CREATE_USER));
    });

    mDeleteUserButton.setOnClickListener(view -> {
      startActivity(IntentFactory.getManageUsersActivityIntent(getApplicationContext(), Destination.REMOVE_USER));
    });

    mAddCourseButton.setOnClickListener(view -> {
      startActivity(IntentFactory.getManageCoursesActivityIntent(getApplicationContext(), Destination.ADD_COURSE));
    });

    mRemoveCourseButton.setOnClickListener(view -> {
      startActivity(IntentFactory.getManageCoursesActivityIntent(getApplicationContext(), Destination.REMOVE_COURSE));
    });

    mEnrollStudentsButton.setOnClickListener(view -> {
      startSelectCourseActivityForId.launch(IntentFactory.getSelectCourseActivityIntent(getApplicationContext(), mUserId));
    });

  }
}