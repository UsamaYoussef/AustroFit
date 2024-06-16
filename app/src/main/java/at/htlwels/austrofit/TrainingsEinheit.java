package at.htlwels.austrofit;

import java.util.ArrayList;

public class TrainingsEinheit {

    private ArrayList<TrainingsUebung> uebungen;

    public TrainingsEinheit() {
        uebungen= new ArrayList<TrainingsUebung>();

    }

    public void addUebung(TrainingsUebung uebung){
        uebungen.add(uebung);
    }

    public void removeUebung(TrainingsUebung uebung){
        uebungen.remove(uebung);
    }

    public void getbestimmteUebung(TrainingsUebung uebung){
        uebungen.get(uebungen.indexOf(uebung));
    }


    public ArrayList<TrainingsUebung> getUebungen() {
        return uebungen;
    }

    public void setUebungen(ArrayList<TrainingsUebung> uebungen) {
        this.uebungen = uebungen;
    }
}
