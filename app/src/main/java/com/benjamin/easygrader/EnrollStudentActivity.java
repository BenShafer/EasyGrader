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
  Button addStudentDialogBtn;
  TextView studentListTextView;
  HashMap<Integer, String> studentList = new HashMap<>();


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

    addStudentFloatingBtn.setOnClickListener(v -> {
      showAddStudentDialog();
    });

    confirmEnrollStudentsBtn.setOnClickListener(v -> {
      Log.d(TAG, "Confirm button clicked: studentList: " + studentList);
      if (studentList.isEmpty()) {
        Toast.makeText(this, "No students entered", Toast.LENGTH_SHORT).show();
      } else {
        mManageCoursesViewModel.enrollStudents(mCourseId, studentList);
        finish();
      }
    });

  }

  private void showAddStudentDialog() {
    View dialogView = LayoutInflater.from(this).inflate(R.layout.enroll_student_dialog, null);

    studentIdInputText = dialogView.findViewById(R.id.studentIdInputText);
    studentNameInputText = dialogView.findViewById(R.id.studentNameInputText);
    addStudentDialogBtn = dialogView.findViewById(R.id.addStudentDialogBtn);

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setView(dialogView);

    builder.setTitle("Add a student");
    builder.setPositiveButton("Add Student", (dialog, which) -> {
      String studentId = studentIdInputText.getText().toString();
      String studentName = studentNameInputText.getText().toString();
      if (studentId.isEmpty() || studentName.isEmpty()) {
        return;
      } else if (studentList.containsKey(Integer.parseInt(studentId))) {
        Toast.makeText(this, "Student already entered", Toast.LENGTH_SHORT).show();
        return;
      } else {
        studentList.put(Integer.parseInt(studentId), studentName);
        StringBuilder sb = new StringBuilder();
        for (Object id : studentList.keySet().toArray()) {
          Log.d(TAG, "showAddStudentDialog: studentInfo: " + id);
          sb.append("ID: ").append(id).append(", Name: ").append(studentList.get(id));
          sb.append("\n");
        }
        studentListTextView.setText(sb.toString());
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

}