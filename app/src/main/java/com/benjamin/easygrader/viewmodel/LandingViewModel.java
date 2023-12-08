package com.benjamin.easygrader.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.benjamin.easygrader.model.User;
import com.benjamin.easygrader.model.UserRepository;

public class LandingViewModel extends AndroidViewModel {

  private static final String TAG = "LandingViewModel";
  private final UserRepository mUserRepository;
  private final MutableLiveData<User> mLoggedInUser = new MutableLiveData<>();

  public LandingViewModel(@NonNull Application application) {
    super(application);
    mUserRepository = UserRepository.getUserRepository(application);
  }

  public LiveData<User> getLoggedInUser() { return mLoggedInUser; }

  public void loadLoggedInUser(int userId) {
    mUserRepository.loadLoggedInUser(userId, mLoggedInUser);
  }



}
