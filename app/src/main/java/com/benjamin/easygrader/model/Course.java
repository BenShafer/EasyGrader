package com.benjamin.easygrader.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(tableName = AppDatabase.COURSE_TABLE)
public class Course {

  @PrimaryKey(autoGenerate = true)
  private int mId;

  @ColumnInfo(name="name")
  private String mName;

  @ColumnInfo(name="semester")
  private String mSemester;

  @ColumnInfo(name="semester_end_date")
  private LocalDateTime mSemesterEndDate;

  @ColumnInfo(name="instructor_id")
  private int mInstructorId;

  @ColumnInfo(name="is_finalized")
  private boolean mIsFinalized;

  public Course(String name, String semester, LocalDateTime semesterEndDate, int instructorId) {
    mName = name;
    mSemester = semester;
    mSemesterEndDate = semesterEndDate;
    mInstructorId = instructorId;
    mIsFinalized = false;
  }

  public int getId() {
    return mId;
  }

  public void setId(int id) {
    mId = id;
  }

  public String getName() {
    return mName;
  }

  public void setName(String name) {
    mName = name;
  }

  public String getSemester() {
    return mSemester;
  }

  public void setSemester(String semester) {
    mSemester = semester;
  }

  public LocalDateTime getSemesterEndDate() {
    return mSemesterEndDate;
  }

  public void setSemesterEndDate(LocalDateTime semesterEndDate) {
    mSemesterEndDate = semesterEndDate;
  }

  public void setFinalized(boolean finalized) {
    mIsFinalized = finalized;
  }

  public int getInstructorId() {
    return mInstructorId;
  }

  public void setInstructorId(int instructorId) {
    mInstructorId = instructorId;
  }

  public boolean isFinalized() {
    return mIsFinalized;
  }

  public void setIsFinalized(boolean finalized) {
    mIsFinalized = finalized;
  }

  @Override
  public String toString() {
    return "Course{" +
        "mId=" + mId +
        ", mName='" + mName + '\'' +
        ", mSemester='" + mSemester + '\'' +
        ", mSemesterEndDate=" + mSemesterEndDate +
        ", mInstructorId=" + mInstructorId +
        ", mIsFinalized=" + mIsFinalized +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Course course = (Course) o;
    return mId == course.mId && mInstructorId == course.mInstructorId && mIsFinalized == course.mIsFinalized && Objects.equals(mName, course.mName) && Objects.equals(mSemester, course.mSemester) && Objects.equals(mSemesterEndDate, course.mSemesterEndDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mId, mName, mSemester, mSemesterEndDate, mInstructorId, mIsFinalized);
  }
}
