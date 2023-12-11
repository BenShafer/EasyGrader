package com.benjamin.easygrader.view;

import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.benjamin.easygrader.R;
import com.benjamin.easygrader.model.Enrollment;
import com.benjamin.easygrader.model.Grade;
import com.benjamin.easygrader.viewmodel.ManageGradesViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Map;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {

  private static final String TAG = "GradeAdapter";
  private Map<Enrollment, Grade> mGradeList;
  private final ManageGradesViewModel mManageGradesViewModel;
  private Grade mSelectedGrade;

  public GradeAdapter(Map<Enrollment, Grade> gradeList, ManageGradesViewModel manageGradesViewModel) {
    mGradeList = gradeList;
    mManageGradesViewModel = manageGradesViewModel;
  }
  public void setGradeList(Map<Enrollment, Grade> gradeList) {
    Log.d(TAG, "setGradeList: gradeList: " + gradeList);
    mGradeList = gradeList;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View gradeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grade, parent, false);
    return new GradeViewHolder(gradeView);
  }

  @Override
  public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
    Enrollment enrollment = (Enrollment) mGradeList.keySet().toArray()[position];
    Grade grade = mGradeList.get(enrollment);
    holder.bind(enrollment, grade);
  }

  @Override
  public int getItemCount() {
    if (mGradeList != null) {
      return mGradeList.size();
    }
    return 0;
  }


  public class GradeViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "GradeViewHolder";

    private final TextView mStudentNameText;
    private final TextInputEditText mScoreInputText;

    public GradeViewHolder(View itemView) {
      super(itemView);
      mStudentNameText = itemView.findViewById(R.id.studentItemTextView);
      mScoreInputText = itemView.findViewById(R.id.scoreItemInputText);

      mScoreInputText.setOnClickListener(view -> {
        int position = getAdapterPosition();
        mSelectedGrade = (Grade) mGradeList.values().toArray()[position];
        Log.d(TAG, "GradeViewHolder: selected grade: " + mSelectedGrade + " at position: " + position);
        showSetScoreDialog(view);
      });
    }

    public void bind(Enrollment enrollment, Grade grade) {
      mStudentNameText.setText(enrollment.getStudentName());
      if (grade.getScore() >= 0) {
        mScoreInputText.setText(String.valueOf(grade.getScore()));
      }
    }
  }

  private void showSetScoreDialog(View view) {
    View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_enter_grade, null);

    TextInputEditText mScoreInputText = dialogView.findViewById(R.id.scoreInputText);
    if (mSelectedGrade.getScore() >= 0) {
      mScoreInputText.setText(String.valueOf(mSelectedGrade.getScore()));
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
    builder.setView(dialogView)
        .setTitle("Grade Assignment")
        .setPositiveButton("Update", (dialog, which) -> {
          String score = mScoreInputText.getText().toString();
          if (score.isEmpty()) {
            Toast.makeText(view.getContext(), "No score entered", Toast.LENGTH_SHORT).show();
          } else {
            Log.d(TAG, "showSetScoreDialog: score set to: " + score);
            mSelectedGrade.setScore(Integer.parseInt(score));
            mManageGradesViewModel.update(mSelectedGrade);
            dialog.dismiss();
          }
        })
        .setNegativeButton("Cancel", (dialog, which) -> {
          Log.d(TAG, "showGradesDialog: ");
          dialog.dismiss();
        })
        .create().show();
    mScoreInputText.requestFocus();
  }
}
