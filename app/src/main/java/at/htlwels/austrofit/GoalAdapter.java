package at.htlwels.austrofit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {

    private List<Goal> goalList;
    private OnGoalListener onGoalListener;

    public GoalAdapter(List<Goal> goalList, OnGoalListener onGoalListener) {
        this.goalList = goalList;
        this.onGoalListener = onGoalListener;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_item, parent, false);
        return new GoalViewHolder(view, onGoalListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        Goal goal = goalList.get(position);
        holder.goalCheckBox.setChecked(goal.isCompleted());
        holder.goalTextView.setText(goal.getName());
    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

    public static class GoalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox goalCheckBox;
        TextView goalTextView;
        OnGoalListener onGoalListener;

        public GoalViewHolder(@NonNull View itemView, OnGoalListener onGoalListener) {
            super(itemView);
            goalCheckBox = itemView.findViewById(R.id.checkbox_goal);
            goalTextView = itemView.findViewById(R.id.textview_goal);
            this.onGoalListener = onGoalListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onGoalListener.onGoalClick(getAdapterPosition());
        }
    }

    public interface OnGoalListener {
        void onGoalClick(int position);
    }
}
