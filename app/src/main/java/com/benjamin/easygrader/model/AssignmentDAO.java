package com.benjamin.easygrader.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AssignmentDAO {

  @Insert
  void insert(Assignment assignment);

  @Delete
  void delete(Assignment assignment);

  @Update
  void update(Assignment assignment);

  @Query("SELECT * FROM " + AppDatabase.ASSIGNMENT_TABLE + " WHERE course_id = :courseId")
  LiveData<List<Assignment>> getAssignmentsByCourseId(int courseId);

}
