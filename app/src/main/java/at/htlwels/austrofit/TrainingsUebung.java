package at.htlwels.austrofit;

public class TrainingsUebung {

    private String nameUebung;
    private String muskel;

    private double gewicht;

    private int saetze;
    private int wiederholungen;


    public TrainingsUebung(String nameUebung, String muskel, int saetze, int wiederholungen) {
        this.nameUebung = nameUebung;
        this.muskel = muskel;
        this.saetze = saetze;
        this.wiederholungen = wiederholungen;
    }

    public TrainingsUebung(String nameUebung, String muskel, double gewicht, int saetze, int wiederholungen) {
        this.nameUebung = nameUebung;
        this.muskel = muskel;
        this.gewicht = gewicht;
        this.saetze = saetze;
        this.wiederholungen = wiederholungen;
    }

    public String getNameUebung() {
        return nameUebung;
    }

    public void setNameUebung(String nameUebung) {
        this.nameUebung = nameUebung;
    }

    public String getMuskel() {
        return muskel;
    }

    public void setMuskel(String muskel) {
        this.muskel = muskel;
    }

    public int getReps() {
        return wiederholungen;
    }

    public void setReps(int reps) {
        this.wiederholungen = reps;
    }

    public double getGewicht() {
        return gewicht;
    }

    public void setGewicht(double gewicht) {
        this.gewicht = gewicht;
    }

    public int getSaetze() {
        return saetze;
    }

    public void setSaetze(int saetze) {
        this.saetze = saetze;
    }

    public int getWiederholungen() {
        return wiederholungen;
    }

    public void setWiederholungen(int wiederholungen) {
        this.wiederholungen = wiederholungen;
    }
}
