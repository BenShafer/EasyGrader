package com.benjamin.easygrader.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.benjamin.easygrader.model.Course;
import com.benjamin.easygrader.model.CourseRepository;
import com.benjamin.easygrader.model.User;
import com.benjamin.easygrader.model.UserRepository;

import java.util.List;
import java.util.Map;

public class ManageCoursesViewModel extends AndroidViewModel {

  private static final String TAG = "ManageCoursesViewModel";
  private final CourseRepository mCourseRepository;
  private final UserRepository mUserRepository;

  public ManageCoursesViewModel(@NonNull Application application) {
    super(application);
    mCourseRepository = CourseRepository.getCourseRepository(application);
    mUserRepository = UserRepository.getUserRepository(application);
  }

  public LiveData<List<User>> getAllInstructors() { return mUserRepository.getAllInstructors(); }

  public LiveData<Map<Course, User>> getAllCoursesWithInstructor(){
    return mCourseRepository.getAllCoursesWithInstructor();
  }

  public void addCourse(String courseName, String semester, int instructorID) {
    mCourseRepository.addCourse(courseName, semester, instructorID);
  }

  public void removeCourse(Course course) {
    mCourseRepository.removeCourse(course);
  }

  public LiveData<Course> getCourseById(int courseId) {
    return mCourseRepository.getCourseById(courseId);
  }

  public void enrollStudents(int courseId, Map<Integer, String> students) {
    mCourseRepository.enrollStudents(courseId, students);
  }

}
