package com.benjamin.easygrader.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EnrollmentDAO {

  @Insert
  void insert(Enrollment enrollment);

  @Insert
  List<Long> insertAll(List<Enrollment> enrollments);

  @Update
  void update(Enrollment enrollment);

  @Delete
  void delete(Enrollment enrollment);

  @Query("SELECT * FROM " + AppDatabase.ENROLLMENT_TABLE + " WHERE course_id = :courseId")
  LiveData<List<Enrollment>> getEnrollmentsForCourse(int courseId);

  @Query("SELECT e.mId FROM " + AppDatabase.ENROLLMENT_TABLE + " e WHERE course_id = :courseId")
  List<Long> getEnrollmentIdsForCourse(int courseId);
}
