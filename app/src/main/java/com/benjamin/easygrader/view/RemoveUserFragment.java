package com.benjamin.easygrader.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.benjamin.easygrader.R;
import com.benjamin.easygrader.model.User;
import com.benjamin.easygrader.viewmodel.ManageUsersViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemoveUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemoveUserFragment extends Fragment {
  private static final String TAG = "RemoveUserFragment";
  ManageUsersViewModel mManageUsersViewModel;
  UserAdapter mUserAdapter;
  private User mSelectedUser = null;
  private User mSelectedInstructor;
  private boolean mHasActiveCourses = false;

  public RemoveUserFragment() {
    // Required empty public constructor
  }

  public static RemoveUserFragment newInstance() {
    return new RemoveUserFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_remove_user, container, false);

    mManageUsersViewModel = new ViewModelProvider(this).get(ManageUsersViewModel.class);

    RecyclerView mUserRecyclerView = view.findViewById(R.id.userRecyclerView);
    Button mRemoveUserButton = view.findViewById(R.id.removeUserButton);
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

    mUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mUserAdapter = new UserAdapter(new ArrayList<>(), new UserAdapter.OnUserSelectedListener() {
      @Override
      public void onUserSelected(User user) {
        mSelectedUser = user;
        Log.d(TAG, "onUserSelected: " + mSelectedUser);
        if (!mSelectedUser.isAdmin()) {
          Log.d(TAG, "onClick: checking courses: " + mSelectedUser);
          mManageUsersViewModel.hasActiveCourses(mSelectedUser.getId());
        }
      }
    });
    mUserRecyclerView.setAdapter(mUserAdapter);

    mManageUsersViewModel.getAllUsers().observe(requireActivity(), users -> {
      mUserAdapter.setUserList(users);
    });

    mManageUsersViewModel.getHasActiveCourses().observe(requireActivity(), hasActiveCourses -> {
      Log.d(TAG, "hasActiveCourses: " + hasActiveCourses);
      mHasActiveCourses = hasActiveCourses;
    });

    mRemoveUserButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.d(TAG, "onClick: " + mSelectedUser);
        if (mSelectedUser != null) {
          builder.setTitle("Remove User").setMessage("Are you sure you wish to remove " + mSelectedUser.getUsername() + "?")
              .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                  if (mHasActiveCourses) {
                    selectInstructorDialog();
                    dialog.dismiss();
                  } else {
                    Log.d(TAG, "dialog: " + mSelectedUser.getUsername() + " being removed");
                    mManageUsersViewModel.deleteUser(mSelectedUser);
                    mSelectedUser = null;
                    mUserAdapter.notifyItemRemoved(mUserAdapter.mSelectedPosition);
                    mUserAdapter.mSelectedPosition = -1;
                    dialog.dismiss();
                    Toast.makeText(requireContext(), "User removed", Toast.LENGTH_SHORT).show();
                  }
                }
              })
              .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                  Log.d(TAG, "dialog: cancelled remove user");
                  dialog.dismiss();
                }
              });
          builder.create().show();
        }
      }
    });

    return view;
  }
  private void selectInstructorDialog() {
    View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_choose_instructor, null);

    Spinner mInstructorSpinner = dialogView.findViewById(R.id.selectInstructor);

    mManageUsersViewModel.getAllInstructors().observe(requireActivity(), instructors -> {
      instructors.remove(mSelectedUser);
      InstructorSpinnerAdapter mInstructorAdapter = new InstructorSpinnerAdapter(getContext(), instructors);
      mInstructorSpinner.setAdapter(mInstructorAdapter);
      mInstructorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          mSelectedInstructor = (User) parent.getItemAtPosition(position);
          Log.d(TAG, "onItemSelected: selected instructor: " + mSelectedInstructor);
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
          mSelectedInstructor = null;
        }
      });
    });

    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setView(dialogView);
    builder.setTitle("Select Instructor").setMessage("You must select an instructor to replace to cover the courses of the instructor you are removing.")
        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int id) {
            Log.d(TAG, "dialog: " + mSelectedInstructor.getUsername() + " selected");
            if (mSelectedInstructor != null) {
              Log.d(TAG, "dialog: " + mSelectedUser.getUsername() + " being removed");
              mManageUsersViewModel.updateInstructorCourses(mSelectedUser.getId(), mSelectedInstructor.getId());
              mManageUsersViewModel.deleteUser(mSelectedUser);
              mSelectedUser = null;
              mSelectedInstructor = null;
              mUserAdapter.notifyItemRemoved(mUserAdapter.mSelectedPosition);
              mUserAdapter.mSelectedPosition = -1;
              dialog.dismiss();
              Toast.makeText(requireContext(), "User removed", Toast.LENGTH_SHORT).show();
            } else {
              Toast.makeText(requireContext(), "You must select an instructor", Toast.LENGTH_SHORT).show();
              dialog.dismiss();
            }
          }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int id) {
            Log.d(TAG, "dialog: cancelled select user");
            Toast.makeText(requireContext(), "You must select an instructor", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
          }
        });
    builder.create().show();
  }
}