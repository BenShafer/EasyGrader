package com.benjamin.easygrader.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

  private static volatile UserRepository instance;
  private static final String TAG = "UserRepository";
  private final UserDAO mUserDAO;
  private final CourseDAO mCourseDAO;
  private final GradeDAO mGradeDAO;

  private UserRepository(Application application) {
    AppDatabase db = AppDatabase.getDatabase(application);
    mUserDAO = db.userDAO();
    mCourseDAO = db.courseDAO();
    mGradeDAO = db.gradeDAO();
    AppDatabase.databaseWriteExecutor.execute(() -> {
      int numUsers = mUserDAO.getCount();
      if (numUsers == 0) {
        User testuser1 = new User("testuser1", "password", false);
        User admin2 = new User("admin2", "password", true);
        mUserDAO.insert(testuser1);
        mUserDAO.insert(admin2);
        Log.d(TAG, "UserRepository: inserted testuser1 and admin2");
      }
    });
  }

  // singleton repository instance
  public static UserRepository getUserRepository(final Application application) {
    if (instance == null) {
      synchronized (UserRepository.class) {
        if (instance == null) {
          instance = new UserRepository(application);
        }
      }
    }
    return instance;
  }

  public void login(String username, String password, MutableLiveData<User> loggedInUser, MutableLiveData<Boolean> isLoggingIn) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      User user = mUserDAO.login(username, password);
      if (user != null) {
        Log.d(TAG, "login: user found: " + user);
        loggedInUser.postValue(user);
      } else {
        Log.d(TAG, "login: user not found " + username + " " + password);
      }
      isLoggingIn.postValue(false);
    });
  }

  public void loadLoggedInUser(int userId, MutableLiveData<User> loggedInUser) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      User user = mUserDAO.getUserById(userId);
      if (user != null) {
        Log.d(TAG, "loadLoggedInUser: user found: " + user);
        loggedInUser.postValue(user);
      } else {
        Log.d(TAG, "loadLoggedInUser: user not found " + userId);
      }
    });
  }

  public LiveData<List<User>> getAllUsers() { return mUserDAO.getAllUsers(); }

  public LiveData<List<User>> getAllInstructors() { return mUserDAO.getAllInstructors(); }

  public void addUser(String username, String password, boolean isAdmin, MutableLiveData<Boolean> isAddingUser, MutableLiveData<Boolean> isUsernameTaken) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      User userByUsername = mUserDAO.getUserByUsername(username);
      if (userByUsername != null) {
        isUsernameTaken.postValue(true);
        Log.d(TAG, "addUser: username taken");
        isAddingUser.postValue(false);
      } else {
        isUsernameTaken.postValue(false);
        User user = new User(username, password, isAdmin);
        Log.d(TAG, "addUser: inserting new user: " + user);
        mUserDAO.insert(user);
        isAddingUser.postValue(false);
      }
    });
  }

  public void deleteUser(User user) {
    Log.d(TAG, "deleteUser: " + user);
    AppDatabase.databaseWriteExecutor.execute(() -> {
      mUserDAO.delete(user);
    });
  }

  public void hasActiveCourses(int instructorId, MutableLiveData<Boolean> hasActiveCourses) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      List<Course> instructorCourses = mCourseDAO.getInstructorsUnfinalizedCoursesList(instructorId);
      if (instructorCourses != null) {
        for (Course course : instructorCourses) {
          if (!course.isFinalized()) {
            if (mGradeDAO.getCourseGradedAssignmentsCountInt(course.getId()) > 0) {
              hasActiveCourses.postValue(true);
              return;
            }
          }
        }
      }
      hasActiveCourses.postValue(false);
    });
  }

  public void updateInstructorCourses(int oldInstructorId, int newInstructorId) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      List<Course> instructorCourses = mCourseDAO.getInstructorsUnfinalizedCoursesList(oldInstructorId);
      if (instructorCourses != null) {
        List<Course> updatedCourses = new ArrayList<>();
        for (Course course : instructorCourses) {
          course.setInstructorId(newInstructorId);
          updatedCourses.add(course);
        }
        mCourseDAO.updateAll(updatedCourses);
      }
    });
  }
}
