package com.example.chordnotes;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewNote extends AppCompatActivity {
    long noteID;
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get info about which note to display
        Intent i = getIntent();
        noteID = i.getLongExtra("ID", 0);
        NoteDatabase db = new NoteDatabase(this);
        Note note = db.getNote(noteID);
        getSupportActionBar().setTitle(note.getTitle());
        TextView content = findViewById(R.id.noteContent);

        String noteContent = note.getContent();
        content.setText(noteContent);
        content.setMovementMethod(new ScrollingMovementMethod());

        findChords(content);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Edit note
                Intent i = new Intent(ViewNote.this, EditNote.class);
                i.putExtra("ID",noteID);
                startActivity(i);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            // Delete Note
            NoteDatabase db = new NoteDatabase(getApplicationContext());
            db.deleteNote(noteID);
            Toast.makeText(getApplicationContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
            goToMain();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Edit note
        Intent i = new Intent(ViewNote.this, EditNote.class);
        i.putExtra("ID",noteID);
        startActivity(i);
    }

    private void goToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    private void findChords(TextView content){
        //String[] noteContentString = noteContent.getText().toString().split("\n");
        String noteContentString = content.getText().toString();
        for(int i = 0; i < noteContentString.length(); i++){
            if(noteContentString.charAt(i) == '{'){
                for (int j = i; j < noteContentString.length(); j++){
                    if(noteContentString.charAt(j) == '}'){
                        String chord = noteContentString.substring(i+1,j);

                        content.setText(styledChords(noteContentString, chord, i, j));
                        i = j;
                        break;
                    }
                }
            }
        }
        Log.d("Chord5 ","eind...");
    }
    private SpannableString styledChords(String lyrics, String chord, int i, int j){
        SpannableString ss = new SpannableString(lyrics);
        ss.setSpan(new StyleSpan(Typeface.BOLD), i+1, j, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

}