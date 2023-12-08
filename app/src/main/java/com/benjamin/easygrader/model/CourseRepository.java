package com.benjamin.easygrader.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseRepository {

  private static volatile CourseRepository instance;
  private static final String TAG = "CourseRepository";
  private final CourseDAO mCourseDAO;
  private final EnrollmentDAO mEnrollmentDAO;

  private CourseRepository(Application application) {
    AppDatabase db = AppDatabase.getDatabase(application);
    mCourseDAO = db.courseDAO();
    mEnrollmentDAO = db.enrollmentDAO();
  }

  public static CourseRepository getCourseRepository(final Application application) {
    if (instance == null) {
      synchronized (CourseRepository.class) {
        if (instance == null) {
          instance = new CourseRepository(application);
        }
      }
    }
    return instance;
  }

  public LiveData<List<Course>> getAllCourses() { return mCourseDAO.getAllCourses(); }

  public LiveData<Map<Course, User>> getAllCoursesWithInstructor(){
    return mCourseDAO.getCoursesWithInstructor();
  }

  public void addCourse(String courseName, String semester, int instructorID) {
    Course course = new Course(courseName, semester, instructorID);
    AppDatabase.databaseWriteExecutor.execute(() -> {
      mCourseDAO.insert(course);
    });
  }

  public void removeCourse(Course course) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      mCourseDAO.delete(course);
    });
  }

  public LiveData<Course> getCourseById(int courseId) {
    return mCourseDAO.getCourseById(courseId);
  }

  public LiveData<Map<Course, User>> getCoursesByInstructorId(int instructorId) {
    return mCourseDAO.getCoursesByInstructorId(instructorId);
  }

  public void enrollStudents(int courseId, Map<Integer, String> students) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      List<Enrollment> enrollments = new ArrayList<>();
      for (Map.Entry<Integer, String> entry : students.entrySet()) {
        enrollments.add(new Enrollment(courseId, entry.getKey(), entry.getValue()));
      }
      mEnrollmentDAO.insertAll(enrollments.toArray(new Enrollment[0]));
      Log.d(TAG, "enrolled students: " + students.toString());
    });
  }

}
