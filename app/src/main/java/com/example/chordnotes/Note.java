package com.example.chordnotes;

import android.util.Log;

public class Note {
    private long ID;
    private String title;
    private String artist;
    private String content;
    private Chord[] chords;
    private String date;
    private String time;

    Note(){}
    Note(String title, String artist, String content, Chord[] chords, String date, String time){
        this.title = title;
        this.artist = artist;
        this.content = content;
        this.chords = chords;
        this.date = date;
        this.time = time;
    }
    Note(long id, String title, String artist, String content, Chord[] chords, String date, String time){
        this.ID = id;
        this.title = title;
        this.artist = artist;
        this.content = content;
        this.chords = chords;
        this.date = date;
        this.time = time;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Chord[] getChords() {
        return chords;
    }

    public String getChordsString() {
        String chordsString = "#";
        Log.d("Chords: ", chords.toString());
        for(int c = 0; c < chords.length;c++){
            chordsString = chordsString + "|" +
                    chords[c].getID() + "-" +
                    chords[c].getPosition() + "-" +
                    chords[c].getTonic() + "-" +
                    chords[c].getMajmin() + "-" +
                    chords[c].getAdditions() + "-" +
                    chords[c].getBass();
        }
        Log.d("ChordString:", chordsString);
        return chordsString;
    }

    public void setChords(Chord[]  chords) {
        this.chords= chords;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
