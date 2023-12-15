package com.benjamin.easygrader;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.benjamin.easygrader.model.Enrollment;
import com.benjamin.easygrader.util.IntentFactory;
import com.benjamin.easygrader.viewmodel.ManageCoursesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

public class EnrollStudentActivity extends AppCompatActivity {
  private static final String TAG = "EnrollStudentActivity";
  private int mCourseId;
  TextView noStudentsTextView;
  FloatingActionButton addStudentFloatingBtn;
  Button confirmEnrollStudentsBtn;
  TextInputEditText studentIdInputText;
  TextInputEditText studentNameInputText;
  TextView studentListTextView;
  HashMap<Integer, String> mNewStudentList = new HashMap<>();
  HashMap<Integer, String> mExistingEnrollments = new HashMap<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_enroll_student);

    ManageCoursesViewModel mManageCoursesViewModel = new ViewModelProvider(this).get(ManageCoursesViewModel.class);
    noStudentsTextView = findViewById(R.id.noStudentsTextView);
    addStudentFloatingBtn = findViewById(R.id.addStudentFloatingBtn);
    confirmEnrollStudentsBtn = findViewById(R.id.confirmEnrollStudentsBtn);
    studentListTextView = findViewById(R.id.studentListTextView);

    mCourseId = getIntent().getIntExtra(IntentFactory.COURSE_ID_EXTRA, -1);

    mManageCoursesViewModel.getCourseById(mCourseId).observe(this, course -> {
      if (course != null) {
        TextView header = findViewById(R.id.headerText);
        header.setText(String.format("Enroll students in %s", course.getName()));
      }
    });

    mManageCoursesViewModel.getEnrollmentsForCourse(mCourseId).observe(this, enrollments -> {
      if (enrollments != null && enrollments.size() > 0) {
        noStudentsTextView.setVisibility(View.INVISIBLE);
        for (Enrollment enrollment : enrollments) {
          mExistingEnrollments.put(enrollment.getId(), enrollment.getStudentName());
          appendEnrollmentList(enrollment.getId(), enrollment.getStudentName());
        }
      }
    });

    addStudentFloatingBtn.setOnClickListener(v -> {
      showAddStudentDialog();
    });

    confirmEnrollStudentsBtn.setOnClickListener(v -> {
      Log.d(TAG, "Confirm button clicked: studentList: " + mNewStudentList);
      if (mNewStudentList.isEmpty()) {
        Toast.makeText(this, "No students entered", Toast.LENGTH_SHORT).show();
      } else {
        mManageCoursesViewModel.enrollStudents(mCourseId, mNewStudentList);
        Toast.makeText(this, "Enrolled students successfully", Toast.LENGTH_SHORT).show();
        finish();
      }
    });
  }

  private void showAddStudentDialog() {
    View dialogView = LayoutInflater.from(this).inflate(R.layout.enroll_student_dialog, null);

    studentIdInputText = dialogView.findViewById(R.id.studentIdInputText);
    studentNameInputText = dialogView.findViewById(R.id.studentNameInputText);

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setView(dialogView);

    builder.setTitle("Add a student");
    builder.setPositiveButton("Add Student", (dialog, which) -> {
      String studentId = studentIdInputText.getText().toString();
      String studentName = studentNameInputText.getText().toString();
      if (studentId.isEmpty() || studentName.isEmpty()) {
        return;
      } else if (mNewStudentList.containsKey(Integer.parseInt(studentId)) || mExistingEnrollments.containsKey(Integer.parseInt(studentId))) {
        Toast.makeText(this, "Student already entered", Toast.LENGTH_SHORT).show();
        return;
      } else {
        mNewStudentList.put(Integer.parseInt(studentId), studentName);
        appendEnrollmentList(Integer.parseInt(studentId), studentName);
        studentIdInputText.setText("");
        studentNameInputText.setText("");
        noStudentsTextView.setVisibility(View.INVISIBLE);
      }
      Log.d(TAG, "Dialog add student: studentId: " + studentId + ", studentName: " + studentName);
      dialog.dismiss();
    });
    builder.setNegativeButton("Cancel", (dialog, which) -> {
      dialog.dismiss();
    });
  builder.create().show();
  }

  private void appendEnrollmentList(int studentId, String studentName) {
    StringBuilder sb = new StringBuilder();
    sb.append("ID: ").append(studentId).append(", Name: ").append(studentName);
    sb.append("\n");
    studentListTextView.append(sb.toString());
  }
}