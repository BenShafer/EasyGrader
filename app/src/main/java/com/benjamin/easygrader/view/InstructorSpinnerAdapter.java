package com.benjamin.easygrader.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.benjamin.easygrader.R;
import com.benjamin.easygrader.model.User;

import java.util.List;

public class InstructorSpinnerAdapter extends ArrayAdapter<User> {

  private static final String TAG = "InstructorSpinnerAdapter";
  public InstructorSpinnerAdapter(Context context,List<User> instructors) {
    super(context, R.layout.spinner_item, R.id.spinnerItemText, instructors);
  }

  @Override
  public View getView(int position, View convertView, @NonNull ViewGroup parent) {
    View view = super.getView(position, null, parent);
    TextView instructorText = view.findViewById(R.id.spinnerItemText);
    instructorText.setText(getItem(position).getUsername());
    return view;
  }

  public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
    View view = super.getDropDownView(position, convertView, parent);
    TextView instructorText = view.findViewById(R.id.spinnerItemText);
    instructorText.setText(getItem(position).getUsername());
    return view;
  }
}
