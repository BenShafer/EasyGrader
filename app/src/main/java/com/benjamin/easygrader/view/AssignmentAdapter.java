package com.benjamin.easygrader.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.benjamin.easygrader.R;
import com.benjamin.easygrader.model.Assignment;

import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder> {
  private static final String TAG = "AssignmentAdapter";

  private OnAssignmentSelectedListener mListener;
  int mSelectedPosition = -1;
  private List<Assignment> mAssignmentList;

  public AssignmentAdapter(List<Assignment> assignmentList, OnAssignmentSelectedListener listener) {
    mAssignmentList = assignmentList;
    mListener = listener;
  }
  public void setAssignmentList(List<Assignment> assignmentList) {
    mAssignmentList = assignmentList;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View assignmentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assignment, parent, false);
    return new AssignmentViewHolder(assignmentView);
  }

  @Override
  public void onBindViewHolder(@NonNull AssignmentViewHolder holder, int position) {
    Assignment assignment = mAssignmentList.get(position);
    holder.bind(assignment);
    holder.mAssignmentNameRadioBtn.setChecked(position == mSelectedPosition);
  }

  @Override
  public int getItemCount() {
    if (mAssignmentList != null) {
      return mAssignmentList.size();
    }
    return 0;
  }


  public class AssignmentViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "AssignmentViewHolder";

    private final RadioButton mAssignmentNameRadioBtn;
    private final TextView mPointsEditText;
    private final TextView mDueDateEditText;

    public AssignmentViewHolder(View itemView) {
      super(itemView);
      mAssignmentNameRadioBtn = itemView.findViewById(R.id.assignmentNameRadioButton);
      mPointsEditText = itemView.findViewById(R.id.pointsItemText);
      mDueDateEditText = itemView.findViewById(R.id.dueDateItemText);

      mAssignmentNameRadioBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
        if (isChecked) {
          if (mSelectedPosition != getAdapterPosition()) {
            notifyItemChanged(mSelectedPosition);
            mSelectedPosition = getAdapterPosition();
            mListener.onAssignmentSelected(mAssignmentList.get(mSelectedPosition));
          }
        }
      });
    }

    public void bind(Assignment assignment) {
      mAssignmentNameRadioBtn.setText(assignment.getName());
      mPointsEditText.setText(String.valueOf(assignment.getPoints()));
      mDueDateEditText.setText(String.valueOf(assignment.getDueDate()));
    }
  }

  public interface OnAssignmentSelectedListener {
    void onAssignmentSelected(Assignment assignment);
  }


}
