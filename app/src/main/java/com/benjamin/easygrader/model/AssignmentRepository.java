package com.benjamin.easygrader.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AssignmentRepository {

  private static volatile AssignmentRepository instance;
  private static final String TAG = "AssignmentRepository";
  private final AssignmentDAO mAssignmentDAO;
  private final GradeDAO mGradeDAO;
  private final EnrollmentDAO mEnrollmentDAO;
  private final CourseDAO mCourseDAO;

  private AssignmentRepository(Application application) {
    AppDatabase db = AppDatabase.getDatabase(application);
    mAssignmentDAO = db.assignmentDAO();
    mGradeDAO = db.gradeDAO();
    mEnrollmentDAO = db.enrollmentDAO();
    mCourseDAO = db.courseDAO();
  }

  public static AssignmentRepository getAssignmentRepository(final Application application) {
    if (instance == null) {
      synchronized (AssignmentRepository.class) {
        if (instance == null) {
          instance = new AssignmentRepository(application);
        }
      }
    }
  return instance;
  }

  public void addAssignment(int courseId, String assignmentName, int points, LocalDateTime dueDate) {
    Assignment assignment = new Assignment(courseId, assignmentName, points, dueDate);
    AppDatabase.databaseWriteExecutor.execute(() -> {
      Log.d(TAG, "addAssignment: " + assignment);
      long assignmentId = mAssignmentDAO.insert(assignment);
      createGradesForAssignment(assignmentId, courseId);
    });
  }

  private void createGradesForAssignment(long assignmentId, int courseId) {
    List<Grade> grades = new ArrayList<>();
    List<Long> enrollmentIds = mEnrollmentDAO.getEnrollmentIdsForCourse(courseId);
    Log.d(TAG, "createGradesForAssignment: making grades for enrollments: " + enrollmentIds + " for assignment: " + assignmentId + " for course: " + courseId);
      for (long enrollmentId : enrollmentIds) {
        Grade grade = new Grade((int) assignmentId, (int) enrollmentId);
        grades.add(grade);
      }
    Log.d(TAG, "createGradesForAssignment: inserting grades: " + grades);
      mGradeDAO.insertAll(grades);
  }

  public LiveData<List<Assignment>> getAssignmentsByCourseId(int courseId) {
    return mAssignmentDAO.getAssignmentsByCourseId(courseId);
  }

  public void deleteAssignment(Assignment assignment) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      mAssignmentDAO.delete(assignment);
    });
  }

  public void update(Assignment assignment) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      mAssignmentDAO.update(assignment);
    });
  }

  public LiveData<LocalDateTime> getSemesterEndDate(int courseId) {
    return mCourseDAO.getSemesterEndDate(courseId);
  }

}
