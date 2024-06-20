package at.htlwels.austrofit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private LinkedList<Food> foodList = new LinkedList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Food food);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.bind(food);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void setFoodList(LinkedList<Food> foodList) {
        this.foodList = foodList;
        notifyDataSetChanged();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewFoodName, textViewCalories, textViewWeight;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFoodName = itemView.findViewById(R.id.textViewFoodName);
            textViewCalories = itemView.findViewById(R.id.textViewCalories);
            textViewWeight = itemView.findViewById(R.id.textViewWeight);
        }

        public void bind(final Food food) {
            textViewFoodName.setText(food.getName());
            textViewCalories.setText("Kalorien: " + food.getCalories());
            textViewWeight.setText("Gewicht: " + food.getWeight() + "g");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(food);
                    }
                }
            });
        }
    }
}