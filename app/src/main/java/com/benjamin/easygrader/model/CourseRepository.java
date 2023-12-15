package com.benjamin.easygrader.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CourseRepository {

  private static volatile CourseRepository instance;
  private static final String TAG = "CourseRepository";
  private final CourseDAO mCourseDAO;
  private final EnrollmentDAO mEnrollmentDAO;
  private final AssignmentDAO mAssignmentDAO;
  private final GradeDAO mGradeDAO;

  private CourseRepository(Application application) {
    AppDatabase db = AppDatabase.getDatabase(application);
    mCourseDAO = db.courseDAO();
    mEnrollmentDAO = db.enrollmentDAO();
    mAssignmentDAO = db.assignmentDAO();
    mGradeDAO = db.gradeDAO();
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

  public LiveData<Map<Course, User>> getAllCoursesWithInstructor(){
    return mCourseDAO.getCoursesWithInstructor();
  }

  public LiveData<List<Course>> getInstructorsUnfinalizedCourses(int instructorId) {
    return mCourseDAO.getInstructorsUnfinalizedCourses(instructorId);
  }

  public void addCourse(String courseName, String semester, int instructorID) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      Course course = new Course(courseName, semester, instructorID);
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

  public LiveData<Map<Course, User>> getCoursesByInstructorId(int instructorId) { return mCourseDAO.getCoursesByInstructorId(instructorId); }

  public void enrollStudents(int courseId, Map<Integer, String> students) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      List<Enrollment> enrollments = new ArrayList<>();
      for (Map.Entry<Integer, String> entry : students.entrySet()) {
        enrollments.add(new Enrollment(courseId, entry.getKey(), entry.getValue()));
      }
      List<Long> enrollmentIds = mEnrollmentDAO.insertAll(enrollments);
      Log.d(TAG, "enrolled students: " + enrollments);
      createGradesForEnrollments(courseId, enrollmentIds);
    });
  }

  private void createGradesForEnrollments(int courseId, List<Long> enrollmentIds) {
    List<Grade> grades = new ArrayList<>();
    int[] assignmentIds = mAssignmentDAO.getAssignmentIdsForCourse(courseId);
    Log.d(TAG, "createGradesForEnrollments: making grades for enrollments: " + enrollmentIds + " for assignments: " + Arrays.toString(assignmentIds) + " for course: " + courseId);
    for (int assignmentId : assignmentIds) {
      for (long enrollmentId : enrollmentIds) {
        Grade grade = new Grade(assignmentId, (int) enrollmentId);
        grades.add(grade);
      }
    }
    Log.d(TAG, "createGradesForEnrollments: inserting grades: " + grades);
    mGradeDAO.insertAll(grades);
  }

  public void finalizeGradesForCourse(Course course) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      course.setIsFinalized(true);
      mCourseDAO.update(course);
    });
  }
}
