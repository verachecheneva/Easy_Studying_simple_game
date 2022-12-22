package com.example.easy_studying.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easy_studying.R;

import java.util.ArrayList;

public class WordAdapter  extends RecyclerView.Adapter<WordAdapter.ViewHolder>{

    private final Context context;
    private  ArrayList<String> word_id, word, translation;

    public WordAdapter(Context context, ArrayList<String> word_id,  ArrayList<String> word,  ArrayList<String> translation) {
        this.context = context;
        this.word_id = word_id;
        this.word = word;
        this.translation = translation;
    }

    @NonNull
    @Override
    public WordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.word_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordAdapter.ViewHolder holder, int position) {

        holder.translation_word_txt.setText(String.valueOf(
                translation.get(position) + " - " + word.get(position)
        ));
    }

    @Override
    public int getItemCount() {
        return word_id.size();
    }

    public ArrayList<String> getData() {
        return word_id;
    }

    public void removeModule(int module_pos) {
        word_id.remove(module_pos);
        word.remove(module_pos);
        translation.remove(module_pos);
        notifyItemRemoved(module_pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView translation_word_txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            translation_word_txt = itemView.findViewById(R.id.translation_word_txt);
        }
    }
}
