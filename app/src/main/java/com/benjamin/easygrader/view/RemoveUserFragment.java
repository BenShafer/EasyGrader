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
import android.widget.Button;

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
  private User mSelectedUser = null;

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

    ManageUsersViewModel mManageUsersViewModel = new ViewModelProvider(this).get(ManageUsersViewModel.class);

    RecyclerView mUserRecyclerView = view.findViewById(R.id.userRecyclerView);
    Button mRemoveUserButton = view.findViewById(R.id.removeUserButton);
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


    mUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    UserAdapter mUserAdapter = new UserAdapter(new ArrayList<>(), new UserAdapter.OnUserSelectedListener() {
      @Override
      public void onUserSelected(User user) {
        mSelectedUser = user;
        Log.d(TAG, "onUserSelected: " + mSelectedUser);
      }
    });
    mUserRecyclerView.setAdapter(mUserAdapter);

    mManageUsersViewModel.getAllUsers().observe(requireActivity(), users -> {
      mUserAdapter.setUserList(users);
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
                  Log.d(TAG, "dialog: " + mSelectedUser.getUsername() + " being removed");
                  mManageUsersViewModel.deleteUser(mSelectedUser);
                  mSelectedUser = null;
                  mUserAdapter.notifyItemRemoved(mUserAdapter.mSelectedPosition);
                  mUserAdapter.mSelectedPosition = -1;
                  dialog.dismiss();
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
}