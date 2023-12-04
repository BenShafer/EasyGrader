package com.benjamin.easygrader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.benjamin.easygrader.util.IntentFactory;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    int userId = sharedPref.getInt(getString(R.string.logged_in_user_id_key), -1);
    if (userId < 0) {
      startActivity(IntentFactory.getLoginActivityIntent(getApplicationContext()));
    } else {
      if (sharedPref.getBoolean(getString(R.string.user_is_admin_key), false)) {
        startActivity(IntentFactory.getAdminLandingPageActivityIntent(getApplicationContext(), userId));
      } else {
        startActivity(IntentFactory.getInstructorLandingPageActivityIntent(getApplicationContext(), userId));
      }
    }
  }
}