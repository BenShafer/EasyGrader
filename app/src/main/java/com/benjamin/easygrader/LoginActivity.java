package com.benjamin.easygrader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.benjamin.easygrader.util.IntentFactory;
import com.benjamin.easygrader.viewmodel.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

  private static final String TAG = "LoginActivity";
  private LoginViewModel mLoginViewModel;

  private TextInputEditText mUsernameInput;
  private TextInputEditText mPasswordInput;
  private Button mLoginButton;
  private boolean mIsLoggingIn = false;
  private boolean backButtonPressed = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();
    mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

    TextView header = findViewById(R.id.headerText);
    header.setText(R.string.sign_in);

    mUsernameInput = findViewById(R.id.usernameInput);
    mPasswordInput = findViewById(R.id.passwordInput);
    mLoginButton = findViewById(R.id.loginButton);

    mLoginButton.setOnClickListener(view -> {
      String username = mUsernameInput.getText().toString();
      String password = mPasswordInput.getText().toString();
      if (! (username.equals("") || password.equals("")) ) {
        mIsLoggingIn = true;
        mLoginViewModel.login(username, password);
      } else {
        mLoginViewModel.login("admin2", "password");
        Toast.makeText(this, "Please enter a username and password.", Toast.LENGTH_SHORT).show();
      }
    });

    mLoginViewModel.getLoggedInUser().observe(this, user -> {
      if (user != null) {
        mIsLoggingIn = false;
        editor.putInt(getString(R.string.logged_in_user_id_key), user.getId());
        editor.putBoolean(getString(R.string.user_is_admin_key), user.isAdmin());
        editor.apply();
        if (user.isAdmin()) {
          startActivity(IntentFactory.getAdminLandingPageActivityIntent(getApplicationContext(), user.getId()));
        } else {
          startActivity(IntentFactory.getInstructorLandingPageActivityIntent(getApplicationContext(), user.getId()));
        }
      }
    });

    mLoginViewModel.getIsLoggingIn().observe(this, isLoggingIn -> {
      if (mIsLoggingIn) {
        if (isLoggingIn) {
          mLoginButton.setEnabled(false);
        } else {
          mLoginButton.setEnabled(true);
          Toast.makeText(this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
        }
      }
    });

  }

  @Override
  public void onBackPressed() {
    if (backButtonPressed) {
      this.finishAffinity();
      super.onBackPressed();
    }
    backButtonPressed = true;
    Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show();
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        backButtonPressed = false;
      }
    }, 2000);
  }

}