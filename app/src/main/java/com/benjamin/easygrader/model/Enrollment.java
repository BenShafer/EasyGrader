package com.benjamin.easygrader.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = AppDatabase.ENROLLMENT_TABLE)
public class Enrollment {

  @PrimaryKey(autoGenerate = true)
  private int mId;

  @ColumnInfo(name="student_id")
  private int mStudentId;

  @ColumnInfo(name="course_id")
  private int mCourseId;

  @ColumnInfo(name="student_name")
  private String mStudentName;

  public Enrollment(int courseId, int studentId, String studentName) {
    mCourseId = courseId;
    mStudentId = studentId;
    mStudentName = studentName;
  }

  public int getId() {
    return mId;
  }

  public void setId(int id) {
    mId = id;
  }

  public int getStudentId() {
    return mStudentId;
  }

  public void setStudentId(int studentId) {
    mStudentId = studentId;
  }

  public int getCourseId() {
    return mCourseId;
  }

  public void setCourseId(int courseId) {
    mCourseId = courseId;
  }

  public String getStudentName() {
    return mStudentName;
  }

  public void setStudentName(String studentName) {
    mStudentName = studentName;
  }

  @Override
  public String toString() {
    return "Enrollment{" +
        "mId=" + mId +
        ", mStudentId=" + mStudentId +
        ", mCourseId=" + mCourseId +
        ", mStudentName='" + mStudentName + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Enrollment that = (Enrollment) o;
    return mId == that.mId && mStudentId == that.mStudentId && mCourseId == that.mCourseId && Objects.equals(mStudentName, that.mStudentName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mId, mStudentId, mCourseId, mStudentName);
  }
}
