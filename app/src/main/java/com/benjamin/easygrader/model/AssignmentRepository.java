package com.benjamin.easygrader.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

public class AssignmentRepository {

  private static volatile AssignmentRepository instance;
  private static final String TAG = "AssignmentRepository";
  private final AssignmentDAO mAssignmentDAO;

  private AssignmentRepository(Application application) {
    AppDatabase db = AppDatabase.getDatabase(application);
    mAssignmentDAO = db.assignmentDAO();
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

  public void addAssignment(int courseId, String assignmentName, int points, Date dueDate) {
    Assignment assignment = new Assignment(courseId, assignmentName, points, dueDate);
    AppDatabase.databaseWriteExecutor.execute(() -> {
      Log.d(TAG, "addAssignment: " + assignment);
      mAssignmentDAO.insert(assignment);
    });
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

}
