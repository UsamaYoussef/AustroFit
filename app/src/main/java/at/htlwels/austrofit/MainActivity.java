package at.htlwels.austrofit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoalAdapter.OnGoalListener {

    private RecyclerView recyclerView;
    private GoalAdapter goalAdapter;
    private List<Goal> goalList;
    private int selectedGoalPosition = -1;
    private EditText newGoalEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zielsetzer);

        goalList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        goalAdapter = new GoalAdapter(goalList, this);
        recyclerView.setAdapter(goalAdapter);

        Button addButton = findViewById(R.id.button_add);
        Button deleteButton = findViewById(R.id.button_delete);
        Button editButton = findViewById(R.id.button_edit);
        newGoalEditText = findViewById(R.id.new_goal);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goalName = newGoalEditText.getText().toString();
                if (!goalName.isEmpty()) {
                    goalList.add(new Goal(goalName, false));
                    goalAdapter.notifyItemInserted(goalList.size() - 1);
                    newGoalEditText.setText("");
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedGoalPosition >= 0) {
                    goalList.remove(selectedGoalPosition);
                    goalAdapter.notifyItemRemoved(selectedGoalPosition);
                    selectedGoalPosition = -1;
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedGoalPosition >= 0) {
                    showEditGoalDialog();
                }
            }
        });
    }

    private void showEditGoalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Goal");

        final EditText input = new EditText(this);
        input.setText(goalList.get(selectedGoalPosition).getName());
        builder.setView(input);

        builder.setPositiveButton("Edit", (dialog, which) -> {
            String goalName = input.getText().toString();
            goalList.get(selectedGoalPosition).setName(goalName);
            goalAdapter.notifyItemChanged(selectedGoalPosition);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void onGoalClick(int position) {
        selectedGoalPosition = position;
    }
}