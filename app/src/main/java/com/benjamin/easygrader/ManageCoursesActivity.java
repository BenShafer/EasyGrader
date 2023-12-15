package com.benjamin.easygrader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.benjamin.easygrader.util.Destination;
import com.benjamin.easygrader.util.IntentFactory;
import com.benjamin.easygrader.view.AddCourseFragment;
import com.benjamin.easygrader.view.RemoveCourseFragment;

public class ManageCoursesActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_manage_courses);

    Destination destination = (Destination) getIntent().getSerializableExtra(IntentFactory.DESTINATION_EXTRA);
    TextView header = findViewById(R.id.headerText);

    if (savedInstanceState == null) {
      switch(destination) {
        case ADD_COURSE:
          header.setText("Add a new course");
          openFragment(AddCourseFragment.newInstance());
          break;
        case REMOVE_COURSE:
          header.setText("Remove a course");
          openFragment(RemoveCourseFragment.newInstance());
          break;
      }
    }

  }

  private void openFragment(Fragment fragment) {
    getSupportFragmentManager().beginTransaction()
        .setReorderingAllowed(true)
        .add(R.id.manageCoursesFragmentView, fragment, null)
        .commit();
  }

}