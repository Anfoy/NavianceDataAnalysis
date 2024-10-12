package me.antcode;

import java.util.ArrayList;

public class StudentProfile {

    private final int iD;

    private final double wGpa;

    private final int usedSAT;

    private final int actComposite;

    private final int satComposite;

    private final ArrayList<Application> applications;

    public StudentProfile(int id, double wGpa, int usedSAT, int actComposite, int satComposite) {
        this.iD = id;
        this.wGpa = wGpa;
        this.usedSAT = usedSAT;
        this.actComposite = actComposite;
        this.satComposite = satComposite;
        applications = new ArrayList<>();
    }

    public int getiD() {
        return iD;
    }

    public double getwGpa() {
        return wGpa;
    }

    public int getHighestSAT() {
        return usedSAT;
    }

    public int getActComposite() {
        return actComposite;
    }

    public int getSatComposite() {
        return satComposite;
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }
}
