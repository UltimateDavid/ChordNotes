package com.example.chordnotes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChordAdapter extends RecyclerView.Adapter<ChordAdapter.Viewholder> {
    private LayoutInflater inflater;
    private List<Chord> chords;

    ChordAdapter(Context context, List<com.example.chordnotes.Chord> chords){
        this.inflater = LayoutInflater.from(context);
        this.chords = chords;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.chord,viewGroup,false);
        return new ChordAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChordAdapter.Viewholder viewHolder, int i) {
        long position = chords.get(i).getPosition();
        String tonic = chords.get(i).getTonic();
        String majmin = chords.get(i).getMajmin();
        String additions = chords.get(i).getAdditions();
        String bass = chords.get(i).getBass();

        Log.d("Chord: ",tonic + majmin + additions + bass);
        viewHolder.sampleChord.setText(tonic);
        viewHolder.sampleChord.setText(tonic + majmin + additions + bass);
//        viewHolder.sampleID.setText(String.valueOf(notes.get(i).getID()));

    }

    @Override
    public int getItemCount() { return chords.size(); }


    public class Viewholder extends RecyclerView.ViewHolder {
        TextView sampleChord;
        RelativeLayout layoutChord;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            sampleChord = itemView.findViewById(R.id.sampleChord);
            layoutChord = itemView.findViewById(R.id.chordBackground);

            itemView.setOnClickListener(v -> { // Click on Chord
                layoutChord.requestFocus();
                Log.d("CHORD FIND FOCUS:", String.valueOf(itemView.findFocus()));
                // Edit Chord
            });
            layoutChord.setOnFocusChangeListener((v, hasFocus) -> {
                ConstraintLayout editToolbar = v.getRootView().findViewById(R.id.editToolbar);
                
                Animation slideUp = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_up);
                Animation slideDown = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_down);


                if (hasFocus){
                    itemView.findViewById(R.id.chordBackground).setBackgroundColor(Color.parseColor("#FF0000"));
                    if(editToolbar.getVisibility()==View.INVISIBLE) {

                        Log.d("EDIT_ACTIVITY", "Toolbar initiated");
                        editToolbar.startAnimation(slideUp);
                        editToolbar.setVisibility(View.VISIBLE);
                    }
                } else {
                    itemView.findViewById(R.id.chordBackground).setBackgroundColor(Color.parseColor("#FFFF00"));
                    if(editToolbar.getVisibility()==View.VISIBLE) {
                        Log.d("EDIT_ACTIVITY", "Toolbar hidden");
                        editToolbar.startAnimation(slideDown);
                        editToolbar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }
}
