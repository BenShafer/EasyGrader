package com.benjamin.easygrader.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = AppDatabase.ASSIGNMENT_TABLE)
public class Assignment {

  @PrimaryKey(autoGenerate = true)
  private int mId;

  @ColumnInfo(name="course_id")
  private int mCourseId;

  @ColumnInfo(name="name")
  private String mName;

  @ColumnInfo(name="points")
  private int mPoints;

  @ColumnInfo(name="due_date")
  private Date mDueDate;

  public Assignment(int courseId, String name, int points, Date dueDate) {
    mCourseId = courseId;
    mName = name;
    mPoints = points;
    mDueDate = dueDate;
  }

  public int getId() {
    return mId;
  }

  public void setId(int id) {
    mId = id;
  }

  public int getCourseId() {
    return mCourseId;
  }

  public void setCourseId(int courseId) {
    mCourseId = courseId;
  }

  public String getName() {
    return mName;
  }

  public void setName(String name) {
    mName = name;
  }

  public int getPoints() {
    return mPoints;
  }

  public void setPoints(int points) {
    mPoints = points;
  }

  public Date getDueDate() {
    return mDueDate;
  }

  public void setDueDate(Date dueDate) {
    mDueDate = dueDate;
  }

  @Override
  public String toString() {
    return "Assignment{" +
        "mId=" + mId +
        ", mCourseId=" + mCourseId +
        ", mName='" + mName + '\'' +
        ", mPoints=" + mPoints +
        ", mDueDate=" + mDueDate +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Assignment that = (Assignment) o;
    return mId == that.mId && mCourseId == that.mCourseId && mPoints == that.mPoints && Objects.equals(mName, that.mName) && Objects.equals(mDueDate, that.mDueDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mId, mCourseId, mName, mPoints, mDueDate);
  }
}
