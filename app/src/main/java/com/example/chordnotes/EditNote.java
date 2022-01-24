package com.example.chordnotes;

import static java.lang.String.valueOf;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EditNote extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTitle, noteArtist, noteContent;
    Chord[] noteChords;
    ChordAdapter chordAdapter;
    String oldTitle;
    Calendar c;
    String dateModified;
    String timeModified;
    FloatingActionButton addChord;
    long noteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Setup top toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Setup chord editing toolbar
        addChord = findViewById(R.id.btnAddChord);
        addChord.setVisibility(View.INVISIBLE);
        final ConstraintLayout editToolbar = findViewById(R.id.editToolbar);
        editToolbar.setVisibility(View.INVISIBLE);

        // Get info from last activity about what to do
        Intent i = getIntent();
        noteID = i.getLongExtra("ID", 0);
        Log.d("NOTEID", "Note id " + noteID);
        NoteDatabase db = new NoteDatabase(this);

        // Find the EditText objects
        noteTitle = findViewById(R.id.noteTitle);
        noteArtist = findViewById(R.id.noteArtist);
        noteContent = findViewById(R.id.noteContent);

        // Initialize variables (from database)
        if (noteID == 0) { // it is a new note
            getSupportActionBar().setTitle("New Note");
            noteChords = new Chord[0];
        } else { // it is a note from database
            Note note = db.getNote(noteID);
            Log.d("DATE", "Date: " + dateModified);
            final String title = note.getTitle();
            oldTitle = title;
            String artist = note.getArtist();
            String content = note.getContent();
            noteChords = note.getChords();

            //change title in toolbar and add all elements from database
            getSupportActionBar().setTitle(title);
            noteTitle.setText(title);
            noteArtist.setText(artist);
            noteContent.setText(content);
        }

        if(noteChords.length>0) {
            List<Chord> allChords = Arrays.asList(noteChords);
            RecyclerView recyclerView = findViewById(R.id.chordView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            chordAdapter = new ChordAdapter(this, allChords);
            recyclerView.setAdapter(chordAdapter);
        }


        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (noteID == 0) {
                        getSupportActionBar().setTitle("New Note");
                    }
                    getSupportActionBar().setTitle(s);
                } else {
                    if (noteID == 0) {
                        getSupportActionBar().setTitle("New Note");
                    }
                }
            }
            @Override public void afterTextChanged(Editable s) { }
        });

        // When button for addng chords is clicked, open up the popup
        addChord.setOnClickListener(view -> popupChord());

        // When content is in focus, show the chord editing toolbar
        noteContent.setOnFocusChangeListener((v, hasFocus) -> {
            Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
            Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

            if (hasFocus){
                if(addChord.getVisibility()==View.INVISIBLE) {
                    Log.d("EDIT_ACTIVITY", "AddChord-button initiated");
                    addChord.startAnimation(slideUp);
                    addChord.setVisibility(View.VISIBLE);
                }
            } else {
                if(addChord.getVisibility()==View.VISIBLE) {
                    Log.d("EDIT_ACTIVITY", "AddChord-button hidden");
                    addChord.startAnimation(slideDown);
                    addChord.setVisibility(View.INVISIBLE);
                }
            }
        });



        Log.d("NOTEID", "Note id " + noteID);

        // set current date and time
        c = Calendar.getInstance();
        dateModified = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        Log.d("DATE", "Date: " + dateModified);
        timeModified = pad(c.get(Calendar.HOUR)) + ":" + pad(c.get(Calendar.MINUTE));
        Log.d("TIME", "Time: " + timeModified);
    }

    private String pad(int time) {
        if (time < 10)
            return "0" + time;
        return valueOf(time);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Save the note
        if (item.getItemId() == R.id.save) {
            if (noteID == 0) {
                if (noteTitle.getText().length() == 0) {
                    //give error message about not having an empty title!
                    Toast.makeText(this, "Empty Title", Toast.LENGTH_SHORT).show();
                } else {
                    newNote();
                }
            } else {
                saveNote();
            }
            goToMain();
        }
        // Delete the note
        if (item.getItemId() == R.id.delete) {
            new AlertDialog.Builder(this)
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    NoteDatabase db = new NoteDatabase(getApplicationContext());
                    db.deleteNote(noteID);
                    Toast.makeText(getApplicationContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                    goToMain();
                })
                .setNegativeButton("Cancel", (dialog, which) -> { })
                .show();
        }
        // View the note
        if (item.getItemId() == R.id.view) {
            Intent i = new Intent(this, ViewNote.class);
            i.putExtra("ID", noteID);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // new note --> title cannot be black
        // edited note --> use old title if blank
        if (noteID == 0) {
            if (noteTitle.getText().length() == 0) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            } else {
                newNote();
            }
        } else {
            if (noteTitle.getText().length() != 0) {
                saveNote();
            }
            // if title is empty, use old title
            noteTitle.setText(oldTitle);
        }
        goToMain();
    }

    private void goToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    private void newNote(){
        Note note = new Note( //String title, String artist, String content, Chord[] chords, String date, String time
                noteTitle.getText().toString(),
                noteArtist.getText().toString(),
                noteContent.getText().toString(),
                noteChords,
                dateModified,
                timeModified);
        NoteDatabase db = new NoteDatabase(this);
        long id = db.addNote(note);
        Note check = db.getNote(id);
        Log.d("inserted", "Note: " + id + " -> Title:" + check.getTitle() + " Date: " + check.getDate());
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }
    private void saveNote(){
        Note note = new Note( //long ID, String title, String artist, String content, Chord[] chords, String date, String time
                noteID,
                noteTitle.getText().toString(),
                noteArtist.getText().toString(),
                noteContent.getText().toString(),
                noteChords,
                dateModified,
                timeModified);
        Log.d("EDITED", "content-> " + noteContent.getText().toString());
        NoteDatabase db = new NoteDatabase(getApplicationContext());
        long id = db.editNote(note);
        Log.d("EDITED", "EDIT: id " + id);
        Toast.makeText(this, "Note Saved.", Toast.LENGTH_SHORT).show();
    }

    private void popupChord(){
        final View popupChord = getLayoutInflater().inflate(R.layout.popup_chord, null);

        // Initialize all dropdown selection boxes
        Spinner spinTonic = popupChord.findViewById(R.id.spin_tonic);
        ArrayAdapter<CharSequence> tonicAdapter = ArrayAdapter.createFromResource(this, R.array.tonic, android.R.layout.simple_spinner_item);
        tonicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTonic.setAdapter(tonicAdapter);
        spinTonic.setSelection(0);

        Spinner spinMajMin = popupChord.findViewById(R.id.spin_majmin);
        ArrayAdapter<CharSequence> majminAdapter = ArrayAdapter.createFromResource(this, R.array.majmin, android.R.layout.simple_spinner_item);
        majminAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMajMin.setAdapter(majminAdapter);
        spinMajMin.setSelection(0);

        Spinner spinAdditions = popupChord.findViewById(R.id.spin_additions);
        ArrayAdapter<CharSequence>additionsAdapter= ArrayAdapter.createFromResource(this, R.array.additions, android.R.layout.simple_spinner_item);
        additionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAdditions.setAdapter(additionsAdapter);
        spinAdditions.setSelection(0);

        Spinner spinBass = popupChord.findViewById(R.id.spin_bass);
        ArrayAdapter<CharSequence>bassAdapter= ArrayAdapter.createFromResource(this, R.array.bass, android.R.layout.simple_spinner_item);
        bassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinBass.setAdapter(bassAdapter);
        spinBass.setSelection(0);

        //Make popup for Chord creation
        new AlertDialog.Builder(this)
            .setTitle("New Chord")
            .setView(popupChord)
            .setPositiveButton("Confirm", (dialog, which) -> {
                Chord chord = new Chord(noteContent.getSelectionStart(),
                        spinTonic.getSelectedItem().toString(),
                        spinMajMin.getSelectedItem().toString(),
                        spinAdditions.getSelectedItem().toString(),
                        spinBass.getSelectedItem().toString()
                );
                Chord[] chordsnew = new Chord[noteChords.length + 1];
                System.arraycopy(noteChords, 0, chordsnew, 0, noteChords.length);
                //add element to new array
                chordsnew[noteChords.length] = chord;
                noteChords = chordsnew;
            })
            .setNegativeButton("Cancel", (dialog, which) -> { })
            .create()
            .show();
    }
}