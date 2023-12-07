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

  public void addUser(String username, String password, boolean isAdmin, MutableLiveData<Boolean> isAddingUser, MutableLiveData<Boolean> isUsernameTaken) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      User userByUsername = mUserDAO.getUserByUsername(username);
      if (userByUsername != null) {
        Log.d(TAG, "addUser: username taken");
        isUsernameTaken.postValue(true);
        isAddingUser.postValue(false);
      } else {
        User user = new User(username, password, isAdmin);
        Log.d(TAG, "addUser: inserting new user: " + user);
        mUserDAO.insert(user);
        isAddingUser.postValue(false);
      }
    });
  }

  public User getUserById(int id) {
    return mUserDAO.getUserById(id);
  }

  public void deleteUser(User user) {
    Log.d(TAG, "deleteUser: " + user);
    AppDatabase.databaseWriteExecutor.execute(() -> {
      mUserDAO.delete(user);
    });
  }
}
