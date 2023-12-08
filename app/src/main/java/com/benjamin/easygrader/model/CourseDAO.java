package com.benjamin.easygrader.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.Map;

@Dao
public interface CourseDAO {

  @Insert
  void insert(Course course);

  @Update
  void update(Course course);

  @Delete
  void delete(Course course);

  @Query("SELECT * FROM " + AppDatabase.COURSE_TABLE)
  LiveData<List<Course>> getAllCourses();

  @Query("SELECT * FROM " + AppDatabase.COURSE_TABLE + " WHERE mId = :courseId")
  LiveData<Course> getCourseById(int courseId);

  @Query("SELECT * FROM " + AppDatabase.COURSE_TABLE + " c " +
      "JOIN " + AppDatabase.USER_TABLE + " u ON c.instructor_id = u.mId " +
      " WHERE instructor_id = :instructorId")
  LiveData<Map<Course, User>> getCoursesByInstructorId(int instructorId);

  @Query("SELECT * FROM " + AppDatabase.COURSE_TABLE + " c " +
      "JOIN " + AppDatabase.USER_TABLE + " u ON c.instructor_id = u.mId")
  LiveData<Map<Course, User>> getCoursesWithInstructor();



}
