package com.benjamin.easygrader.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = AppDatabase.USER_TABLE)
public class User {

  @PrimaryKey(autoGenerate = true)
  private int mId;

  @ColumnInfo(name="username")
  private String mUsername;

  @ColumnInfo(name="password")
  private String mPassword;

  @ColumnInfo(name="is_admin")
  private boolean mIsAdmin;

  public int getId() {
    return mId;
  }

  public User(String username, String password, boolean isAdmin) {
    mUsername = username;
    mPassword = password;
    mIsAdmin = isAdmin;
  }

  public void setId(int id) {
    mId = id;
  }

  public String getUsername() {
    return mUsername;
  }

  public void setUsername(String username) {
    mUsername = username;
  }

  public String getPassword() {
    return mPassword;
  }

  public void setPassword(String password) {
    mPassword = password;
  }

  public boolean isAdmin() {
    return mIsAdmin;
  }

  public void setAdmin(boolean admin) {
    mIsAdmin = admin;
  }

  @Override
  public String toString() {
    return "User{" +
        "mId=" + mId +
        ", mUsername='" + mUsername + '\'' +
        ", mPassword='" + mPassword + '\'' +
        ", mIsAdmin=" + mIsAdmin +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return mId == user.mId && mIsAdmin == user.mIsAdmin && mUsername.equals(user.mUsername) && mPassword.equals(user.mPassword);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mId, mUsername, mPassword, mIsAdmin);
  }
}
