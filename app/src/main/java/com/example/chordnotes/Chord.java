package com.example.chordnotes;

public class Chord {
    private long ID;
    private long position;
    private String tonic;
    private String majmin;
    private String additions;
    private String bass;


    Chord(long position, String tonic, String majmin, String additions, String bass){
        this.position = position;
        this.tonic = tonic;
        this.majmin = majmin;
        this.additions = additions;
        this.bass = bass;
    }
    Chord(long ID, long position, String tonic, String majmin, String additions, String bass){
        this.ID = ID;
        this.position = position;
        this.tonic = tonic;
        this.majmin = majmin;
        this.additions = additions;
        this.bass = bass;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getTonic() {
        return tonic;
    }

    public void setTonic(String tonic) {
        this.tonic = tonic;
    }

    public String getMajmin() {
        return majmin;
    }

    public void setMajmin(String majmin) {
        this.majmin = majmin;
    }

    public String getAdditions() {
        return additions;
    }

    public void setAdditions(String additions) {
        this.additions = additions;
    }

    public String getBass() {
        return bass;
    }

    public void setBass(String bass) {
        this.bass = bass;
    }
}
