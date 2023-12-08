package com.benjamin.easygrader.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benjamin.easygrader.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemoveCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemoveCourseFragment extends Fragment {

  public RemoveCourseFragment() {
    // Required empty public constructor
  }

  public static RemoveCourseFragment newInstance() {
    return new RemoveCourseFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_remove_course, container, false);
  }
}