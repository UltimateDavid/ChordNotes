package com.example.chordnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NoteDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "chordnotedb2";
    private static final String DATABASE_TABLE = "notetable";

    // column names for database table
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ARTIST = "artist";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_CHORDS = "chords";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";

    NoteDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table nametame(id INT PRIMARY KEY, title TEXT, content TEXT, date TEXT, time TEXT)
        String createDb = "CREATE TABLE "+DATABASE_TABLE+" ("+
                KEY_ID+" INTEGER PRIMARY KEY,"+
                KEY_TITLE+" TEXT,"+
                KEY_ARTIST+" TEXT,"+
                KEY_CONTENT+" TEXT,"+
                KEY_CHORDS+" TEXT,"+
                KEY_DATE+" TEXT,"+
                KEY_TIME+" TEXT"
                +" )";
        db.execSQL(createDb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion >= newVersion)
            return;

        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
        onCreate(db);
    }

    public long addNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_TITLE,note.getTitle());
        v.put(KEY_ARTIST,note.getArtist());
        v.put(KEY_CONTENT,note.getContent());
        v.put(KEY_CHORDS,note.getChordsString());
        v.put(KEY_DATE,note.getDate());
        v.put(KEY_TIME,note.getTime());

        // inserting data into db
        long ID = db.insert(DATABASE_TABLE,null,v);
        return  ID;
    }

    public Note getNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] query = new String[] {KEY_ID,KEY_TITLE,KEY_ARTIST,KEY_CONTENT,KEY_CHORDS,KEY_DATE,KEY_TIME};
        Cursor cursor=  db.query(DATABASE_TABLE,query,KEY_ID+"=?",new String[]{String.valueOf(id)},null,null,null,null);

        if(cursor != null)
            cursor.moveToFirst();

        //Reverse engineer Chordsstring to Chord object
        Chord[] chords = new Chord[0];
        Log.d("GET_NOTE", "chords: -> "+ cursor.getString(4));
        if(cursor.getString(4) !="#") {
            String[] chordsString = cursor.getString(4).split("\\|");
            for (int c = 1; c < chordsString.length; c++) {
                Log.d("GET_NOTE", "chordString: -> "+ chordsString[c]);
                String[] chordinfo = chordsString[c].split("-");
                // Find occurences of "-", to see how many items chordinfo should have
                int count = 1;
                for(int i=0; i < chordsString[c].length(); i++) {
                    if(chordsString[c].charAt(i) == '-')
                        count++;
                }
                // Append empty items if chordinfo ends with a few empty options
                for(int empty = chordinfo.length; empty<count;empty++){
                    String[] chordnew = new String[chordinfo.length+1];
                    System.arraycopy(chordinfo, 0, chordnew, 0, chordinfo.length);
                    //add element to new array
                    chordnew[chordinfo.length] = "";
                    chordinfo = chordnew;
                }
                Chord[] chordsnew = new Chord[chords.length + 1];
                System.arraycopy(chords, 0, chordsnew, 0, chords.length);
                //add element to new array
                chordsnew[chords.length] = new Chord(Long.parseLong(chordinfo[0]), Long.parseLong(chordinfo[1]), chordinfo[2], chordinfo[3], chordinfo[4], chordinfo[5]);
                chords = chordsnew;
            }
        }



        return new Note(
                Long.parseLong(cursor.getString(0)), // id
                cursor.getString(1), // title
                cursor.getString(2), // artist
                cursor.getString(3), // content
                chords, // chords
                cursor.getString(5), // datemodified
                cursor.getString(6));// timemodified);
    }

    public List<Note> getAllNotes(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<Note> allNotes = new ArrayList<>();

        String query = "SELECT * FROM " + DATABASE_TABLE;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setID(Long.parseLong(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setArtist(cursor.getString(2));
                note.setContent(cursor.getString(3));
                note.setDate(cursor.getString(5));
                note.setTime(cursor.getString(6));
                allNotes.add(note);
            }while(cursor.moveToNext());
        }
        return allNotes;
    }

    public int editNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        Log.d("Edited", "Edited Title: -> "+ note.getTitle() + "\n ID -> "+note.getID());
        c.put(KEY_TITLE,note.getTitle());
        c.put(KEY_ARTIST,note.getArtist());
        c.put(KEY_CONTENT,note.getContent());
        c.put(KEY_CHORDS,note.getChordsString());
        c.put(KEY_DATE,note.getDate());
        c.put(KEY_TIME,note.getTime());
        return db.update(DATABASE_TABLE,c,KEY_ID+"=?",new String[]{String.valueOf(note.getID())});
    }

    void deleteNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE,KEY_ID+"=?",new String[]{String.valueOf(id)});
        db.close();
    }
}
