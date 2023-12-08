package com.benjamin.easygrader.util;

import android.content.Context;
import android.content.Intent;

import com.benjamin.easygrader.AdminLandingPageActivity;
import com.benjamin.easygrader.InstructorLandingPageActivity;
import com.benjamin.easygrader.LoginActivity;
import com.benjamin.easygrader.MainActivity;
import com.benjamin.easygrader.SelectCourseActivity;

public class IntentFactory {

//  public static final String MAIN_ACTIVITY = "com.benjamin.easygrader.MainActivity";
//  public static final String LOGIN_ACTIVITY = "com.benjamin.easygrader.LoginActivity";
//  public static final String ADMIN_LANDING_PAGE_ACTIVITY = "com.benjamin.easygrader.AdminLandingPageActivity";
//  public static final String INSTRUCTOR_LANDING_PAGE_ACTIVITY = "com.benjamin.easygrader.InstructorLandingPageActivity";
//  public static final String SELECT_COURSE_ACTIVITY = "com.benjamin.easygrader.SelectCourseActivity";

  public static final String USER_ID_EXTRA = "com.benjamin.easygrader.userId";
  public static final String COURSE_ID_EXTRA = "com.benjamin.easygrader.courseId";

  public static Intent getMainActivityIntent(Context context) {
    return new Intent(context, MainActivity.class);
  }

  public static Intent getLoginActivityIntent(Context context) {
    return new Intent(context, LoginActivity.class);
  }

  public static Intent getAdminLandingPageActivityIntent(Context context, int userId) {
    Intent intent = new Intent(context, AdminLandingPageActivity.class);
    intent.putExtra(USER_ID_EXTRA, userId);
    return intent;
  }

  public static Intent getInstructorLandingPageActivityIntent(Context context, int userId) {
    Intent intent = new Intent(context, InstructorLandingPageActivity.class);
    intent.putExtra(USER_ID_EXTRA, userId);
    return intent;
  }

  public static Intent getSelectCourseActivityIntent(Context context, int userId) {
    Intent intent = new Intent(context, SelectCourseActivity.class);
    intent.putExtra(USER_ID_EXTRA, userId);
    return intent;
  }
}
