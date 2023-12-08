package com.benjamin.easygrader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.benjamin.easygrader.util.IntentFactory;

public class SelectCourseActivity extends AppCompatActivity {

  private static final String TAG = "SelectCourseActivity";

  private int mUserId;
  private Button mCourse1Button;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_course);

    TextView header = findViewById(R.id.headerText);
    header.setText("Select a course");

    mUserId = getIntent().getIntExtra(IntentFactory.USER_ID_EXTRA, -1);


    mCourse1Button = findViewById(R.id.course1Button);
    mCourse1Button.setOnClickListener(view -> {
      Log.d(TAG, "Course 1 button selected");
      Intent intent = new Intent();
      intent.putExtra(IntentFactory.COURSE_ID_EXTRA, 1);
      setResult(RESULT_OK, intent);
      finish();
    });
  }
}