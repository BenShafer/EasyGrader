package com.benjamin.easygrader.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.benjamin.easygrader.model.User;
import com.benjamin.easygrader.model.UserRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ManageUsersViewModel extends AndroidViewModel {

  private static final String TAG = "ManageUsersViewModel";
  private final UserRepository mUserRepository;
  private final MutableLiveData<Boolean> mIsAddingUser = new MutableLiveData<>();
  private final MutableLiveData<Boolean> mIsUsernameTaken = new MutableLiveData<>();

  public ManageUsersViewModel(@NotNull Application application) {
    super(application);
    Log.d(TAG, "ManageUsersViewModel: initialized");
    mUserRepository = UserRepository.getUserRepository(application);
  }

  public LiveData<List<User>> getAllUsers() { return mUserRepository.getAllUsers(); }

  public void deleteUser(User user) { mUserRepository.deleteUser(user); }

  public void addUser(String username, String password, boolean isAdmin) {
    Log.d(TAG, "addUser: called");
    mIsAddingUser.setValue(true);
    mIsUsernameTaken.setValue(false);
    mUserRepository.addUser(username, password, isAdmin, mIsAddingUser, mIsUsernameTaken);
  }
  public LiveData<Boolean> getIsAddingUser() {
    Log.d(TAG, "getIsAddingUser: called");
    return mIsAddingUser; }
  public LiveData<Boolean> getIsUsernameTaken() {
    Log.d(TAG, "getIsUsernameTaken: called");
    return mIsUsernameTaken; }

}
