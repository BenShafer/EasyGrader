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
public interface GradeDAO {

  @Insert
  void insert(Grade grade);

  @Insert
  void insertAll(List<Grade> grades);

  @Delete
  void delete(Grade grade);

  @Update
  void update(Grade grade);

  @Query("SELECT * FROM " + AppDatabase.GRADE_TABLE + " g " +
      "JOIN " + AppDatabase.ENROLLMENT_TABLE + " e ON g.enrollment_id = e.mId " +
      "WHERE g.assignment_id = :assignmentId")
  LiveData<Map<Enrollment, Grade>> getGradesForAssignment(int assignmentId);

  @Query("SELECT COUNT(*) FROM " + AppDatabase.GRADE_TABLE + " g " +
      "WHERE g.assignment_id = :assignmentId AND g.score >= 0")
  LiveData<Integer> getGradedAssignmentsCount(int assignmentId);

  @Query("SELECT COUNT(*) FROM " + AppDatabase.GRADE_TABLE + " g " +
      "WHERE g.assignment_id = :assignmentId")
  LiveData<Integer> getTotalAssignmentsCount(int assignmentId);

  @Query("SELECT COUNT(*) FROM (SELECT COUNT(*) 'count' FROM " + AppDatabase.GRADE_TABLE + " g " +
      "JOIN " + AppDatabase.ASSIGNMENT_TABLE + " a ON g.assignment_id = a.mId " +
      "WHERE a.course_id = :courseId AND g.score >= 0 GROUP BY g.assignment_id) AS graded " +
      "WHERE graded.count = (SELECT COUNT(*) FROM " + AppDatabase.ENROLLMENT_TABLE + " e " +
      "WHERE e.course_id = :courseId)")
  LiveData<Integer> getCourseGradedAssignmentsCount(int courseId);

  @Query("SELECT COUNT(*) FROM (SELECT COUNT(*) 'count' FROM " + AppDatabase.GRADE_TABLE + " g " +
      "JOIN " + AppDatabase.ASSIGNMENT_TABLE + " a ON g.assignment_id = a.mId " +
      "WHERE a.course_id = :courseId AND g.score >= 0 GROUP BY g.assignment_id) AS graded " +
      "WHERE graded.count = (SELECT COUNT(*) FROM " + AppDatabase.ENROLLMENT_TABLE + " e " +
      "WHERE e.course_id = :courseId)")
  int getCourseGradedAssignmentsCountInt(int courseId);
}
