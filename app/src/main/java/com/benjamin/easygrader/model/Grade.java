package com.benjamin.easygrader.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = AppDatabase.GRADE_TABLE)
public class Grade {

  @PrimaryKey(autoGenerate = true)
  private int mId;

  @ColumnInfo(name = "assignment_id")
  private int mAssignmentId;

  @ColumnInfo(name = "enrollment_id")
  private int mEnrollmentId;

  @ColumnInfo(name = "date_submitted")
  private Date mDateSubmitted;

  @ColumnInfo(name = "score")
  private int mScore;

  @ColumnInfo(name = "letter_grade")
  private char mLetterGrade;

  public Grade(int assignmentId, int enrollmentId) {
    mAssignmentId = assignmentId;
    mEnrollmentId = enrollmentId;
    mScore = -1;
    mDateSubmitted = new Date(0);
  }

  public int getId() {
    return mId;
  }

  public void setId(int id) {
    mId = id;
  }

  public int getAssignmentId() {
    return mAssignmentId;
  }

  public void setAssignmentId(int assignmentId) {
    mAssignmentId = assignmentId;
  }

  public int getEnrollmentId() {
    return mEnrollmentId;
  }

  public void setEnrollmentId(int enrollmentId) {
    mEnrollmentId = enrollmentId;
  }

  public Date getDateSubmitted() {
    return mDateSubmitted;
  }

  public void setDateSubmitted(Date dateSubmitted) {
    mDateSubmitted = dateSubmitted;
  }

  public int getScore() {
    return mScore;
  }

  public void setScore(int score) {
    mScore = score;
  }

  public char getLetterGrade() {
    return mLetterGrade;
  }

  public void setLetterGrade(char letterGrade) {
    mLetterGrade = letterGrade;
  }

  @Override
  public String toString() {
    return "Grade{" +
        "mId=" + mId +
        ", mAssignmentId=" + mAssignmentId +
        ", mEnrollmentId=" + mEnrollmentId +
        ", mDateSubmitted=" + mDateSubmitted +
        ", mScore=" + mScore +
        ", mLetterGrade=" + mLetterGrade +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Grade grade = (Grade) o;
    return mId == grade.mId && mAssignmentId == grade.mAssignmentId && mEnrollmentId == grade.mEnrollmentId && mScore == grade.mScore && mLetterGrade == grade.mLetterGrade && Objects.equals(mDateSubmitted, grade.mDateSubmitted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mId, mAssignmentId, mEnrollmentId, mDateSubmitted, mScore, mLetterGrade);
  }
}
