package com.benjamin.easygrader.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.benjamin.easygrader.R;
import com.benjamin.easygrader.model.Course;
import com.benjamin.easygrader.viewmodel.ManageGradesViewModel;

import java.util.List;

public class CourseGradesAdapter extends RecyclerView.Adapter<CourseGradesAdapter.CourseGradesViewHolder> {
  private static final String TAG = "CourseGradesAdapter";

  private final ManageGradesViewModel mManageGradesViewModel;
  private List<Course> mCourseGradesList;
  private final OnCourseSelectedListener mListener;
  private int mSelectedPosition = -1;

  public CourseGradesAdapter(List<Course> courseGradesList, ManageGradesViewModel ViewModel, OnCourseSelectedListener listener) {
    mCourseGradesList = courseGradesList;
    mManageGradesViewModel = ViewModel;
    mListener = listener;
  }
  public void setCourseGradesList(List<Course> courseGradesList) {
    mCourseGradesList = courseGradesList;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public CourseGradesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View courseGradesView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_grades, parent, false);
    return new CourseGradesViewHolder(courseGradesView);
  }

  @Override
  public void onBindViewHolder(@NonNull CourseGradesViewHolder holder, int position) {
    Course course = mCourseGradesList.get(position);
    holder.bind(course);
    holder.mCourseItemRadioBtn.setChecked(position == mSelectedPosition);
  }

  @Override
  public int getItemCount() {
    if (mCourseGradesList != null)
      return mCourseGradesList.size();
    return 0;
  }


  public class CourseGradesViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "CourseGradesViewHolder";

    private final RadioButton mCourseItemRadioBtn;
    private final TextView mGradedAssignmentsCountText;
    private final TextView mTotalAssignmentsCountText;

    public CourseGradesViewHolder(View itemView) {
      super(itemView);
      mCourseItemRadioBtn = itemView.findViewById(R.id.courseItemRadioButton);
      mGradedAssignmentsCountText = itemView.findViewById(R.id.gradedAssignmentsCountText);
      mTotalAssignmentsCountText = itemView.findViewById(R.id.totalAssignmentsCountText);

      mCourseItemRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
        if (isChecked) {
          if (mSelectedPosition != getAdapterPosition()) {
            notifyItemChanged(mSelectedPosition);
            mSelectedPosition = getAdapterPosition();
            if (mGradedAssignmentsCountText.getText().toString().equals(mTotalAssignmentsCountText.getText().toString()))
              mListener.onCourseSelected(mCourseGradesList.get(mSelectedPosition), true);
            else
              mListener.onCourseSelected(mCourseGradesList.get(mSelectedPosition), false);
          }
        }
      });
    }

    public void bind(Course course) {
      mCourseItemRadioBtn.setText(course.getName());

      mManageGradesViewModel.getCourseGradedAssignmentsCount(course.getId()).observe((LifecycleOwner) itemView.getContext(), gradedAssignmentsCount -> {
        Log.d(TAG, "bind: observed  gradedAssignmentsCount: " + gradedAssignmentsCount + " for course: " + course.getName());
        mGradedAssignmentsCountText.setText(String.valueOf(gradedAssignmentsCount));

      });
      mManageGradesViewModel.getCourseTotalAssignmentsCount(course.getId()).observe((LifecycleOwner) itemView.getContext(), totalAssignmentsCount -> {
        Log.d(TAG, "bind: observed  gradedAssignmentsCount: " + totalAssignmentsCount + " for course: " + course.getName());
        mTotalAssignmentsCountText.setText(String.valueOf(totalAssignmentsCount));
      });
    }
  }

  public interface OnCourseSelectedListener {
    void onCourseSelected(Course course, boolean isFinalizable);
  }
}
