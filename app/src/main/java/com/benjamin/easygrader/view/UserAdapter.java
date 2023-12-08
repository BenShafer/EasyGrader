package com.benjamin.easygrader.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.benjamin.easygrader.R;
import com.benjamin.easygrader.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

  private static final String TAG = "UserAdapter";
  private List<User> mUserList;
  int mSelectedPosition = -1;
  private OnUserSelectedListener mListener;

  public UserAdapter(List<User> userList, OnUserSelectedListener listener) {
    mUserList = userList;
    mListener = listener;
  }
  public void setUserList(List<User> userList) {
    mUserList = userList;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View userView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
    return new UserViewHolder(userView);
  }

  @Override
  public void onBindViewHolder(UserViewHolder holder, int position) {
    User user = mUserList.get(position);
    holder.bind(user);
    holder.mUserIdRadioBtn.setChecked(position == mSelectedPosition);
  }

  @Override
  public int getItemCount() {
    if (mUserList != null) {
      return mUserList.size();
    }
    return 0;
  }

  public class UserViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "UserViewHolder";

    private final RadioButton mUserIdRadioBtn;
    private final TextView mUsernameItemText;
    private final TextView mUserTypeText;

    public UserViewHolder(View itemView) {
      super(itemView);
      mUserIdRadioBtn = itemView.findViewById(R.id.userIdRadioButton);
      mUsernameItemText = itemView.findViewById(R.id.usernameItemText);
      mUserTypeText = itemView.findViewById(R.id.userTypeItemText);

      mUserIdRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
        if (isChecked) {
          Log.d(TAG, "UserViewHolder: radio button checked for " + mUsernameItemText.getText() + " at position " + getAdapterPosition());
          if (mSelectedPosition != getAdapterPosition()) {
            notifyItemChanged(mSelectedPosition);
            mSelectedPosition = getAdapterPosition();
            mListener.onUserSelected(mUserList.get(mSelectedPosition));
          }
        }
      });
    }

    public void bind(User user) {
      mUserIdRadioBtn.setText(String.valueOf(user.getId()));
      mUsernameItemText.setText(user.getUsername());
      if (user.isAdmin()) {
        mUserTypeText.setText("Administrator");
      } else {
        mUserTypeText.setText("Instructor");
      }
    }
  }

  public interface OnUserSelectedListener {
    void onUserSelected(User user);
  }

}
