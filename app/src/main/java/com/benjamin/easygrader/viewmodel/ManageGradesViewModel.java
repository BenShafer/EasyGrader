package com.benjamin.easygrader.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.benjamin.easygrader.model.Assignment;
import com.benjamin.easygrader.model.AssignmentRepository;
import com.benjamin.easygrader.model.Course;
import com.benjamin.easygrader.model.CourseRepository;
import com.benjamin.easygrader.model.Enrollment;
import com.benjamin.easygrader.model.Grade;
import com.benjamin.easygrader.model.GradeRepository;

import java.util.List;
import java.util.Map;

public class ManageGradesViewModel extends AndroidViewModel {
  private static final String TAG = "ManageGradesViewModel";
  private final AssignmentRepository mAssignmentRepository;
  private final GradeRepository mGradeRepository;
  private final CourseRepository mCourseRepository;

  public ManageGradesViewModel(@NonNull Application application) {
    super(application);
    mAssignmentRepository = AssignmentRepository.getAssignmentRepository(application);
    mGradeRepository = GradeRepository.getGradeRepository(application);
    mCourseRepository = CourseRepository.getCourseRepository(application);
  }

  public LiveData<List<Assignment>> getAssignmentsByCourseId(int courseId) {
    return mAssignmentRepository.getAssignmentsByCourseId(courseId);
  }

  public LiveData<Map<Enrollment, Grade>> getGradesForAssignment(int assignmentId) {
    return mGradeRepository.getGradesForAssignment(assignmentId);
  }

  public void update(Grade grade) {
    mGradeRepository.update(grade);
  }

  public LiveData<List<Course>> getInstructorsUnfinalizedCourses(int instructorId) {
    return mCourseRepository.getInstructorsUnfinalizedCourses(instructorId);
  }

  public LiveData<Integer> getGradedAssignmentsCount(int assignmentId) {
    return mGradeRepository.getGradedAssignmentsCount(assignmentId);
  }

  public LiveData<Integer> getTotalAssignmentsCount(int assignmentId) {
    return mGradeRepository.getTotalAssignmentsCount(assignmentId);
  }

  public LiveData<Integer> getCourseGradedAssignmentsCount(int courseId) {
    return mGradeRepository.getCourseGradedAssignmentsCount(courseId);
  }

  public LiveData<Integer> getCourseTotalAssignmentsCount(int courseId) {
    return mGradeRepository.getCourseTotalAssignmentsCount(courseId);
  }

  public void finalizeGradesForCourse(Course course) {
    mCourseRepository.finalizeGradesForCourse(course);
  }

}
