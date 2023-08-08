package com.example.easy_studying.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easy_studying.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class WordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private final Context context;
    private ArrayList<String> word_id, word, translation;

    private String module_name, module_creation;

    private static final int MODULE_INFO_POS = 0;
    private static final int WORDS_POS = 1;

    public WordAdapter(Context context, ArrayList<String> word_id,  ArrayList<String> word,  ArrayList<String> translation, String module_name, String module_creation) {
        this.context = context;
        this.word_id = word_id;
        this.word = word;
        this.translation = translation;
        this.module_name = module_name;
        long seconds = Long.parseLong(module_creation);
        DateFormat df = new SimpleDateFormat("dd.MM.yy");
        this.module_creation = df.format(seconds);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View itemLayoutView;
        switch (viewType)
        {
            case MODULE_INFO_POS:
                itemLayoutView = LayoutInflater.from(context).inflate(
                        R.layout.module_index_top,
                        parent,
                        false
                );
                vh = new ModuleInfoViewHolder(itemLayoutView);
                break;
            default:
                itemLayoutView = LayoutInflater.from(context).inflate(
                        R.layout.word_row,
                        parent,
                        false
                );
                vh = new WordViewHolder(itemLayoutView);
                break;
        }
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == MODULE_INFO_POS)
        {
            return MODULE_INFO_POS;
        }
        return WORDS_POS;
    }

//    TODO: https://ru.stackoverflow.com/questions/510879/Как-добавить-в-recyclerview-разные-элемент

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (this.getItemViewType(position))
        {
            case MODULE_INFO_POS:
                ModuleInfoViewHolder module_info_holder = (ModuleInfoViewHolder) holder;
                module_info_holder.count_words_txt.setText(Integer.toString(word_id.size()));
                module_info_holder.date_creation_txt.setText(module_creation);
                module_info_holder.module_name_txt.setText(module_name);
                break;
            case WORDS_POS:
                WordViewHolder word_holder = (WordViewHolder) holder;
                word_holder.translation_word_txt.setText(
                        translation.get(position - 1) + " - " + word.get(position - 1)
                ); // Счет позиции начинается с header страницы
                break;
        }
    }

    @Override
    public int getItemCount() {
        return word_id.size() + 1;
    }

    public ArrayList<String> getData() {
        return word_id;
    }

    public void removeWord(int module_pos) {
        word_id.remove(module_pos);
        word.remove(module_pos);
        translation.remove(module_pos);
        notifyItemRemoved(module_pos);
    }

    public static class ModuleInfoViewHolder extends RecyclerView.ViewHolder
    {
        TextView module_name_txt;
        TextView count_words_txt;
        TextView date_creation_txt;

        public ModuleInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            module_name_txt = itemView.findViewById(R.id.module_name_txt);
            count_words_txt = itemView.findViewById(R.id.count_words_txt);
            date_creation_txt = itemView.findViewById(R.id.date_creation_txt);
        }
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder
    {
        TextView translation_word_txt;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            translation_word_txt = itemView.findViewById(R.id.translation_word_txt);
        }
    }
}
