package com.benjamin.easygrader.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.benjamin.easygrader.model.User;
import com.benjamin.easygrader.model.UserRepository;

import org.jetbrains.annotations.NotNull;

public class LoginViewModel extends AndroidViewModel {

  private static final String TAG = "LoginViewModel";
  private final UserRepository mUserRepository;
  private final MutableLiveData<Boolean> mIsLoggingIn = new MutableLiveData<>();
  private final MutableLiveData<User> mLoggedInUser = new MutableLiveData<>();

  public LoginViewModel(@NotNull Application application) {
    super(application);
    mUserRepository = UserRepository.getUserRepository(application);
  }

  public void login(String username, String password) {
    mIsLoggingIn.setValue(true);
    mUserRepository.login(username, password, mLoggedInUser, mIsLoggingIn);
  }

  public LiveData<User> getLoggedInUser() { return mLoggedInUser; }
  public LiveData<Boolean> getIsLoggingIn() { return mIsLoggingIn; }

}
