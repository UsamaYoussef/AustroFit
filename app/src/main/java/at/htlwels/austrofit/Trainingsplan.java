package at.htlwels.austrofit;

import java.util.ArrayList;

public class Trainingsplan {

    private ArrayList<TrainingsEinheit> einheiten;

    public Trainingsplan() {
        einheiten=new ArrayList<TrainingsEinheit>();

    }

    public void addEinheit(TrainingsEinheit einheit){
        einheiten.add(einheit);
    }

    public void removeUebung(TrainingsEinheit einheit){
        einheiten.remove(einheit);
    }

    public void getbestimmteUebung(TrainingsEinheit einheit){
        einheiten.get(einheiten.indexOf(einheit));
    }

    public ArrayList<TrainingsEinheit> getEinheiten() {
        return einheiten;
    }

    public void setEinheiten(ArrayList<TrainingsEinheit> einheiten) {
        this.einheiten = einheiten;
    }
}
