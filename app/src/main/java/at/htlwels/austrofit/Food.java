package at.htlwels.austrofit;

public class Food {
    private int id;
    private String name;
    private int weight;
    private int calories;

    public Food(int id, String name, int weight, int calories) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.calories = calories;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public int getCalories() {
        return calories;
    }
}
