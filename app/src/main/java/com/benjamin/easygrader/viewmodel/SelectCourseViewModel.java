package com.benjamin.easygrader.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.benjamin.easygrader.model.Course;
import com.benjamin.easygrader.model.CourseRepository;
import com.benjamin.easygrader.model.User;
import com.benjamin.easygrader.model.UserRepository;

import java.util.Map;

public class SelectCourseViewModel extends AndroidViewModel {
  private static final String TAG = "SelectCourseViewModel";
  private final UserRepository mUserRepository;
  private CourseRepository mCourseRepository;
  private final MutableLiveData<User> mLoggedInUser = new MutableLiveData<>();
  public LiveData<Map<Course, User>> coursesForUser = Transformations.switchMap(mLoggedInUser, (user) -> {
    Log.d(TAG, "coursesForUser: " + user);
    if (user != null) {
      if (user.isAdmin()) {
        return mCourseRepository.getAllCoursesWithInstructor();
      } else {
        return mCourseRepository.getCoursesByInstructorId(user.getId());
      }
    }
    return mCourseRepository.getCoursesByInstructorId(-1);
  });

  public SelectCourseViewModel(@NonNull Application application) {
    super(application);
    mUserRepository = UserRepository.getUserRepository(application);
    mCourseRepository = CourseRepository.getCourseRepository(application);
  }

  public void loadCoursesForUser(int userId) {
    mUserRepository.loadLoggedInUser(userId, mLoggedInUser);
  }

  public LiveData<User> getLoggedInUser() { return mLoggedInUser; }


}
