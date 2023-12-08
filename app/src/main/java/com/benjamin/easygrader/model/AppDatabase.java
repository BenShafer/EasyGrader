package com.benjamin.easygrader.model;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.benjamin.easygrader.util.DataTypeConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Course.class, Enrollment.class}, version = 3, exportSchema = false)
@TypeConverters(DataTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {

  private static volatile AppDatabase INSTANCE;
  private static final int NUMBER_OF_THREADS = 4;
  static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
  public static final String dbName="app_db";
  public static final String ASSIGNMENT_TABLE = "assignment";
  public static final String COURSE_TABLE = "course";
  public static final String ENROLLMENT_TABLE = "enrollment";
  public static final String GRADE_TABLE = "grade";
  public static final String USER_TABLE = "user";
  public abstract UserDAO userDAO();
  public abstract CourseDAO courseDAO();
  public abstract EnrollmentDAO enrollmentDAO();

  public static AppDatabase getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (RoomDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                  AppDatabase.class, dbName)
              .fallbackToDestructiveMigration()
              .build();
        }
      }
    }
    return INSTANCE;
  }
}
