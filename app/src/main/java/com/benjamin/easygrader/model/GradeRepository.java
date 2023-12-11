package com.benjamin.easygrader.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Map;

public class GradeRepository {

  private static volatile GradeRepository instance;
  private static final String TAG = "GradeRepository";
  private final GradeDAO mGradeDAO;
  private final AssignmentDAO mAssignmentDAO;

  private GradeRepository(Application application) {
    AppDatabase db = AppDatabase.getDatabase(application);
    mGradeDAO = db.gradeDAO();
    mAssignmentDAO = db.assignmentDAO();
  }

  public static GradeRepository getGradeRepository(final Application application) {
    if (instance == null) {
      synchronized (GradeRepository.class) {
        if (instance == null) {
          instance = new GradeRepository(application);
        }
      }
    }
    return instance;
  }

  public void update(Grade grade) {
    AppDatabase.databaseWriteExecutor.execute(() -> mGradeDAO.update(grade));
  }

  public LiveData<Map<Enrollment, Grade>> getGradesForAssignment(int assignmentId) {
    return mGradeDAO.getGradesForAssignment(assignmentId);
  }

  public LiveData<Integer> getGradedAssignmentsCount(int assignmentId) {
    return mGradeDAO.getGradedAssignmentsCount(assignmentId);
  }

  public LiveData<Integer> getTotalAssignmentsCount(int assignmentId) {
    return mGradeDAO.getTotalAssignmentsCount(assignmentId);
  }

  public LiveData<Integer> getCourseGradedAssignmentsCount(int courseId) {
    return mGradeDAO.getCourseGradedAssignmentsCount(courseId);
  }

  public LiveData<Integer> getCourseTotalAssignmentsCount(int courseId) {
    return mAssignmentDAO.getCourseTotalAssignmentsCount(courseId);
  }
}
