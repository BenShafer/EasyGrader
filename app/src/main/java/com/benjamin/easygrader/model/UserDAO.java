package com.benjamin.easygrader.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDAO {

  @Query("SELECT * FROM " + AppDatabase.USER_TABLE)
  List<User> getAll();

  @Query("SELECT COUNT(*) FROM " + AppDatabase.USER_TABLE)
  int getCount();

  @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE username = :username AND password = :password")
  User login(String username, String password);

  @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mId = :userId")
  User getUserById(int userId);

  @Insert
  void insert(User user);

  @Update
  void update(User user);

  @Delete
  void delete(User user);
}
