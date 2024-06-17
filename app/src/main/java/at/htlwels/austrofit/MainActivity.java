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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zielsetzer);

        goalList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        goalAdapter = new GoalAdapter(goalList, this);
        recyclerView.setAdapter(goalAdapter);

        Button addButton = findViewById(R.id.button_add);
        Button deleteButton = findViewById(R.id.button_delete);
        Button editButton = findViewById(R.id.button_edit);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGoalDialog(false);
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
                    showGoalDialog(true);
                }
            }
        });
    }

    private void showGoalDialog(final boolean isEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isEdit ? "Edit Goal" : "Add Goal");

        final EditText input = new EditText(this);
        builder.setView(input);

        if (isEdit && selectedGoalPosition >= 0) {
            input.setText(goalList.get(selectedGoalPosition).getName());
        }

        builder.setPositiveButton(isEdit ? "Edit" : "Add", (dialog, which) -> {
            String goalName = input.getText().toString();
            if (isEdit && selectedGoalPosition >= 0) {
                goalList.get(selectedGoalPosition).setName(goalName);
                goalAdapter.notifyItemChanged(selectedGoalPosition);
            } else {
                goalList.add(new Goal(goalName, false));
                goalAdapter.notifyItemInserted(goalList.size() - 1);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void onGoalClick(int position) {
        selectedGoalPosition = position;
    }
}