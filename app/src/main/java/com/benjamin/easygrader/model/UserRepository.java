package com.benjamin.easygrader.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class UserRepository {

  private static volatile UserRepository instance;
  private static final String TAG = "UserRepository";
  private final UserDAO mUserDAO;
  private User mLoggedInUser;


  private UserRepository(Application application) {
    AppDatabase db = AppDatabase.getDatabase(application);
    mUserDAO = db.userDAO();
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
    if (mLoggedInUser == null) {
      Log.d(TAG, "UserRepository: mLoggedInUser is null");
    } else {
      Log.d(TAG, "UserRepository: mLoggedInUser is not null " + mLoggedInUser);
    }
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

//  public void getLoggedInUser(MutableLiveData<User> loggedInUser) {
//    Log.d(TAG, "getLoggedInUser: " + mLoggedInUser.toString());
//    loggedInUser.postValue(mLoggedInUser);
//  }
//  public User getLoggedInUser() {
//    if (mLoggedInUser != null) {
//      Log.d(TAG, "getLoggedInUser: " + mLoggedInUser);
//      return mLoggedInUser;
//    } else {
//      Log.d(TAG, "getLoggedInUser: null");
//      return null;
//    }
//  }
  public User getUserById(int id) {
    return mUserDAO.getUserById(id);
  }







}
