package com.benjamin.easygrader.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.benjamin.easygrader.model.Assignment;
import com.benjamin.easygrader.model.AssignmentRepository;
import com.benjamin.easygrader.model.CourseRepository;

import java.util.Date;
import java.util.List;

public class ManageAssignmentsViewModel extends AndroidViewModel {

  private static final String TAG = "ManageAssignmentsViewModel";
  private final AssignmentRepository mAssignmentRepository;
//  private final CourseRepository mCourseRepository;


  public ManageAssignmentsViewModel(@NonNull Application application) {
    super(application);
    mAssignmentRepository = AssignmentRepository.getAssignmentRepository(application);
//    mCourseRepository = CourseRepository.getCourseRepository(application);
  }

  public void addAssignment(int courseId, String assignmentName, int points, Date dueDate) {
    mAssignmentRepository.addAssignment(courseId, assignmentName, points, dueDate);
  }

  public LiveData<List<Assignment>> getAssignmentsByCourseId(int courseId) {
   return mAssignmentRepository.getAssignmentsByCourseId(courseId);
  }

  public void deleteAssignment(Assignment assignment) {
    mAssignmentRepository.deleteAssignment(assignment);
  }

  public void update(Assignment assignment) { mAssignmentRepository.update(assignment); }

}
