package com.example.chordnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity{
    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView noItemText;
    NoteAdapter noteAdapter;
    List<Note> notes;
    NoteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);


        db = new NoteDatabase(this);
        List<Note> allNotes = db.getAllNotes();
        recyclerView = findViewById(R.id.allNotesList);
        displayList(allNotes);


//        noItemText = findViewById(R.id.noItemText);
//
//
//        if(allNotes.isEmpty()){
//            noItemText.setVisibility(View.VISIBLE);
//        }else {
//            noItemText.setVisibility(View.GONE);
//            displayList(allNotes);
//        }
    }
    private void displayList(List<Note> allNotes) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(this,allNotes);
        recyclerView.setAdapter(noteAdapter);
    }

    // // MENU HOME SCREEN
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Add note
        if(item.getItemId() == R.id.add){
            Toast.makeText(this, "Add New Note", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, EditNote.class);
            i.putExtra("ID",0);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


}



