package com.benjamin.easygrader.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Dao
public interface CourseDAO {

  @Insert
  void insert(Course course);

  @Update
  void update(Course course);

  @Update
  void updateAll(List<Course> courses);

  @Delete
  void delete(Course course);

  @Query("SELECT * FROM " + AppDatabase.COURSE_TABLE + " WHERE mId = :courseId")
  LiveData<Course> getCourseById(int courseId);

  @Query("SELECT * FROM " + AppDatabase.COURSE_TABLE + " c " +
      "JOIN " + AppDatabase.USER_TABLE + " u ON c.instructor_id = u.mId " +
      " WHERE instructor_id = :instructorId")
  LiveData<Map<Course, User>> getCoursesByInstructorId(int instructorId);

  @Query("SELECT * FROM " + AppDatabase.COURSE_TABLE + " c " +
      "JOIN " + AppDatabase.USER_TABLE + " u ON c.instructor_id = u.mId")
  LiveData<Map<Course, User>> getCoursesWithInstructor();

  @Query("SELECT * FROM " + AppDatabase.COURSE_TABLE + " c " +
      "WHERE c.is_finalized = 0 AND c.instructor_id = :instructorId")
  LiveData<List<Course>> getInstructorsUnfinalizedCourses(int instructorId);

  @Query("SELECT * FROM " + AppDatabase.COURSE_TABLE + " c " +
      "JOIN " + AppDatabase.USER_TABLE + " u ON c.instructor_id = u.mId " +
      "WHERE c.is_finalized = 0 AND c.instructor_id = :instructorId")
  LiveData<Map<Course, User>> getInstructorWithUnfinalizedCourses(int instructorId);

  @Query("SELECT * FROM " + AppDatabase.COURSE_TABLE + " c " +
      "WHERE c.is_finalized = 0 AND c.instructor_id = :instructorId")
  List<Course> getInstructorsUnfinalizedCoursesList(int instructorId);

  @Query("SELECT c.semester_end_date FROM " + AppDatabase.COURSE_TABLE + " c " +
      "WHERE c.mId = :courseId")
  LiveData<LocalDateTime> getSemesterEndDate(int courseId);
}
