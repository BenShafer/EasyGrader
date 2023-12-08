package com.benjamin.easygrader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.benjamin.easygrader.util.Destination;
import com.benjamin.easygrader.util.IntentFactory;
import com.benjamin.easygrader.view.CreateUserFragment;
import com.benjamin.easygrader.view.RemoveUserFragment;

public class ManageUsersActivity extends AppCompatActivity {

  private static final String TAG = "ManageUsersActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_manage_users);

    Destination destination = (Destination) getIntent().getSerializableExtra(IntentFactory.DESTINATION_EXTRA);
    TextView header = findViewById(R.id.headerText);

    if (savedInstanceState == null) {
      switch(destination) {
        case CREATE_USER:
          header.setText("Create a new user");
          getSupportFragmentManager().beginTransaction()
              .setReorderingAllowed(true)
              .add(R.id.manageUsersFragmentView, CreateUserFragment.class, null)
              .commit();
          break;
        case REMOVE_USER:
          header.setText("Remove a user");
          getSupportFragmentManager().beginTransaction()
              .setReorderingAllowed(true)
              .add(R.id.manageUsersFragmentView, RemoveUserFragment.class, null)
              .commit();
          break;
      }
    }

  }
}