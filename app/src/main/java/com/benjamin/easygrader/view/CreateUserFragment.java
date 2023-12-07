package com.benjamin.easygrader.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.benjamin.easygrader.R;
import com.benjamin.easygrader.viewmodel.ManageUsersViewModel;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateUserFragment extends Fragment {
  private static final String TAG = "CreateUserFragment";
  private boolean mIsAddingUser = false;

  public CreateUserFragment() {
    // Required empty public constructor
  }

  public static CreateUserFragment newInstance() {
    return new CreateUserFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_create_user, container, false);

    ManageUsersViewModel mManageUsersViewModel = new ViewModelProvider(this).get(ManageUsersViewModel.class);

    TextInputEditText mUsername = view.findViewById(R.id.createUsernameInput);
    TextInputEditText mPassword = view.findViewById(R.id.createPasswordInput);
    TextInputEditText mConfirmPassword = view.findViewById(R.id.createConfirmPasswordInput);
    RadioButton mAdminRadioBtn = view.findViewById(R.id.adminRadioButton);
    RadioButton mInstructorRadioBtn = view.findViewById(R.id.instructorRadioButton);
    Button mConfirmAddUserBtn = view.findViewById(R.id.confirmAddUserButton);

    mAdminRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        mInstructorRadioBtn.setChecked(false);
      }
    });

    mInstructorRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        mAdminRadioBtn.setChecked(false);
      }
    });

    mConfirmAddUserBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();
        boolean isAdmin = false;
        if (mAdminRadioBtn.isChecked()) {
          isAdmin = true;
        }
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || (!mAdminRadioBtn.isChecked() && !mInstructorRadioBtn.isChecked())) {
          Toast.makeText(requireActivity(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
          Toast.makeText(requireActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
        }  else {
          mIsAddingUser = true;
          mManageUsersViewModel.addUser(username, password, isAdmin);
        }
      }
    });

    mManageUsersViewModel.getIsAddingUser().observe(requireActivity(), isAddingUser -> {
      if (mIsAddingUser) {
        if (isAddingUser) {
          mConfirmAddUserBtn.setEnabled(false);

        } else {
          mConfirmAddUserBtn.setEnabled(true);
          mIsAddingUser = false;
        }

      }
    });

    mManageUsersViewModel.getIsUsernameTaken().observe(requireActivity(), isUsernameTaken -> {
        Log.d(TAG, "observed getIsUsernameTaken: mIsAddingUser: " + mIsAddingUser);
      if (mIsAddingUser) {
        Log.d(TAG, "observed getIsUsernameTaken: isUsernameTaken: " + isUsernameTaken);
        if (isUsernameTaken) {
          Toast.makeText(requireActivity(), "Username already taken", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(requireActivity(), "Successfully added user!", Toast.LENGTH_SHORT).show();
          mUsername.setText("");
          mPassword.setText("");
          mConfirmPassword.setText("");
          mAdminRadioBtn.setChecked(false);
          mInstructorRadioBtn.setChecked(false);
        }
      }
    });

    return view;
  }
}