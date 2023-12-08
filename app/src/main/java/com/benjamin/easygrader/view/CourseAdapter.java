package com.benjamin.easygrader.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.benjamin.easygrader.R;
import com.benjamin.easygrader.model.Course;
import com.benjamin.easygrader.model.User;

import java.util.Map;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

  private static final String TAG = "CourseAdapter";
  private Map<Course, User> mCourseList;
  int mSelectedPosition = -1;
  private OnCourseSelectedListener mListener;

  public CourseAdapter(Map<Course, User> courseList, OnCourseSelectedListener listener) {
    mCourseList = courseList;
    mListener = listener;
  }
  public void setCourseList(Map<Course, User> courseList) {
    mCourseList = courseList;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View courseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
    return new CourseViewHolder(courseView);
  }

  @Override
  public void onBindViewHolder(CourseViewHolder holder, int position) {
    Course course = (Course) mCourseList.keySet().toArray()[position];
    User instructor = mCourseList.get(course);
    holder.bind(course, instructor);
    holder.mCourseIdRadioBtn.setChecked(position == mSelectedPosition);
  }

  @Override
  public int getItemCount() {
    if (mCourseList != null) {
      return mCourseList.size();
    }
    return 0;
  }


  public class CourseViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "CourseViewHolder";

    private final RadioButton mCourseIdRadioBtn;
    private final TextView mCourseNameItemText;
    private final TextView mCourseSemesterItemText;
    private final TextView mCourseInstructorItemText;

    public CourseViewHolder(View itemView) {
      super(itemView);
      mCourseIdRadioBtn = itemView.findViewById(R.id.courseIdRadioButton);
      mCourseNameItemText = itemView.findViewById(R.id.courseNameItemText);
      mCourseSemesterItemText = itemView.findViewById(R.id.courseSemesterItemText);
      mCourseInstructorItemText = itemView.findViewById(R.id.courseInstructorItemText);

      mCourseIdRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
        if (isChecked) {
          Log.d(TAG, "radio button checked for " + mCourseNameItemText.getText() + " at position " + getAdapterPosition());
          if (mSelectedPosition != getAdapterPosition()) {
            notifyItemChanged(mSelectedPosition);
            mSelectedPosition = getAdapterPosition();
            mListener.onCourseSelected((Course) mCourseList.keySet().toArray()[mSelectedPosition]);
          }
        }
      });
    }

    public void bind(Course course, User instructor) {
      mCourseIdRadioBtn.setText(String.valueOf(course.getId()));
      mCourseNameItemText.setText(course.getName());
      mCourseSemesterItemText.setText(course.getSemester());
      mCourseInstructorItemText.setText(instructor.getUsername());
    }
  }

  public interface OnCourseSelectedListener {
    void onCourseSelected(Course course);
  }

}

