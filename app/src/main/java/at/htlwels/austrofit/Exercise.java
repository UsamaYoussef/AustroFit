package at.htlwels.austrofit;

import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable {
    private String name;
    private int reps;
    private int sets;
    private String day;

    // Konstruktor
    public Exercise(String name, int reps, int sets, String day) {
        this.name = name;
        this.reps = reps;
        this.sets = sets;
        this.day = day;
    }

    // Parcelable Konstruktor
    protected Exercise(Parcel in) {
        name = in.readString();
        reps = in.readInt();
        sets = in.readInt();
        day = in.readString();
    }

    // Parcelable Creator
    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    // Getter-Methoden
    public String getName() {
        return name;
    }

    public int getReps() {
        return reps;
    }

    public int getSets() {
        return sets;
    }

    public String getDay() {
        return day;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(reps);
        parcel.writeInt(sets);
        parcel.writeString(day);
    }
}