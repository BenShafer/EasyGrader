package com.benjamin.easygrader.viewmodel;

import android.app.Application;

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
  private final MutableLiveData<Boolean> mHasActiveCourses = new MutableLiveData<>();

  public ManageUsersViewModel(@NotNull Application application) {
    super(application);
    mUserRepository = UserRepository.getUserRepository(application);
  }

  public LiveData<List<User>> getAllUsers() { return mUserRepository.getAllUsers(); }

  public void deleteUser(User user) { mUserRepository.deleteUser(user); }

  public void addUser(String username, String password, boolean isAdmin) {
    mIsUsernameTaken.setValue(false);
    mIsAddingUser.setValue(true);
    mUserRepository.addUser(username, password, isAdmin, mIsAddingUser, mIsUsernameTaken);
  }

  public LiveData<Boolean> getIsAddingUser() {
    return mIsAddingUser; }

  public LiveData<Boolean> getIsUsernameTaken() {
    return mIsUsernameTaken; }

  public LiveData<List<User>> getAllInstructors() { return mUserRepository.getAllInstructors(); }

  public LiveData<Boolean> getHasActiveCourses() { return mHasActiveCourses; }

  public void hasActiveCourses(int instructorId) {
    mUserRepository.hasActiveCourses(instructorId, mHasActiveCourses);
  }

  public void updateInstructorCourses(int oldInstructorId, int newInstructorId) {
    mUserRepository.updateInstructorCourses(oldInstructorId, newInstructorId);
  }
}
