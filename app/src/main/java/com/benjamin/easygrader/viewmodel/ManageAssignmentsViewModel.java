package com.benjamin.easygrader.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.benjamin.easygrader.model.Assignment;
import com.benjamin.easygrader.model.AssignmentRepository;

import java.time.LocalDateTime;
import java.util.List;

public class ManageAssignmentsViewModel extends AndroidViewModel {
  private static final String TAG = "ManageAssignmentsViewModel";
  private final AssignmentRepository mAssignmentRepository;

  public ManageAssignmentsViewModel(@NonNull Application application) {
    super(application);
    mAssignmentRepository = AssignmentRepository.getAssignmentRepository(application);
  }

  public void addAssignment(int courseId, String assignmentName, int points, LocalDateTime dueDate) {
    mAssignmentRepository.addAssignment(courseId, assignmentName, points, dueDate);
  }

  public LiveData<List<Assignment>> getAssignmentsByCourseId(int courseId) {
   return mAssignmentRepository.getAssignmentsByCourseId(courseId);
  }

  public void deleteAssignment(Assignment assignment) {
    mAssignmentRepository.deleteAssignment(assignment);
  }

  public void update(Assignment assignment) { mAssignmentRepository.update(assignment); }

  public LiveData<LocalDateTime> getSemesterEndDate(int courseId) {
    return mAssignmentRepository.getSemesterEndDate(courseId);
  }
}
