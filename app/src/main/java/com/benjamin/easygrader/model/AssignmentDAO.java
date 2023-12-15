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
  long insert(Assignment assignment);

  @Delete
  void delete(Assignment assignment);

  @Update
  void update(Assignment assignment);

  @Query("SELECT * FROM " + AppDatabase.ASSIGNMENT_TABLE + " WHERE course_id = :courseId")
  LiveData<List<Assignment>> getAssignmentsByCourseId(int courseId);

  @Query("SELECT a.mId FROM " + AppDatabase.ASSIGNMENT_TABLE + " a WHERE course_id = :courseId")
  int[] getAssignmentIdsForCourse(int courseId);

  @Query("SELECT COUNT(*) FROM " + AppDatabase.ASSIGNMENT_TABLE + " a " +
      "WHERE a.course_id = :courseId")
  LiveData<Integer> getCourseTotalAssignmentsCount(int courseId);

}
