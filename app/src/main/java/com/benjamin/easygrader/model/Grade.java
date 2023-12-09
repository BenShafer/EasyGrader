package com.benjamin.easygrader.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = AppDatabase.GRADE_TABLE)
public class Grade {

  @PrimaryKey(autoGenerate = true)
  private int mId;

  @Embedded
  private AssignmentWithEnrollmentKey mAssignmentWithEnrollmentKey;

  public static class AssignmentWithEnrollmentKey {
    private int mAssignmentId;
    private int mEnrollmentId;
  }

}
