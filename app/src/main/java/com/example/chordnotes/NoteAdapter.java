package com.example.chordnotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.Viewholder> {
    private LayoutInflater inflater;
    private List<com.example.chordnotes.Note> notes;

    NoteAdapter(Context context, List<com.example.chordnotes.Note> notes){
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_view,viewGroup,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.Viewholder viewHolder, int i) {
        String title = notes.get(i).getTitle();
        String artist = notes.get(i).getArtist();
        String date = notes.get(i).getDate();
        String time = notes.get(i).getTime();

        viewHolder.sampleTitle.setText(title);
        viewHolder.sampleArtist.setText(artist);
        viewHolder.sampleDate.setText(date);
        viewHolder.sampleTime.setText(time);
//        viewHolder.sampleID.setText(String.valueOf(notes.get(i).getID()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView sampleTitle,sampleArtist,sampleDate,sampleTime,sampleID;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            sampleTitle = itemView.findViewById(R.id.sampleTitle);
            sampleArtist = itemView.findViewById(R.id.sampleArtist);
            sampleDate = itemView.findViewById(R.id.sampleDate);
            sampleTime = itemView.findViewById(R.id.sampleTime);
            //sampleID = itemView.findViewById(R.id.listID);

            itemView.setOnClickListener(v -> {
                // Edit note
                Intent i = new Intent(v.getContext(), EditNote.class);
                i.putExtra("ID",notes.get(getAdapterPosition()).getID());
                v.getContext().startActivity(i);
            });
        }
    }
}
