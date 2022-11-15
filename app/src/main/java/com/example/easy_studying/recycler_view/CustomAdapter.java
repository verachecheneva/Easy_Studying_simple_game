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
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Context context;
    private ArrayList module_id, module_name, module_count_words;

    public CustomAdapter(Context context, ArrayList module_id, ArrayList module_name, ArrayList module_count_words) {
        this.context = context;
        this.module_name = module_name;
        this.module_count_words = module_count_words;
        this.module_id = module_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.module_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.module_name_txt.setText(String.valueOf(module_name.get(position)));
        holder.count_words_txt.setText(String.valueOf(module_count_words.get(position)));
    }

    @Override
    public int getItemCount() {
        return module_name.size();
    }

    public ArrayList<String> getData() {
        return module_id;
    }

    public void removeModule(int module_pos) {
        module_id.remove(module_pos);
        module_name.remove(module_pos);
        module_count_words.remove(module_pos);
        notifyItemRemoved(module_pos);
//        https://zatackcoder.com/swipe-to-delete-and-undo-feature-in-android-recyclerview/
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView module_name_txt, count_words_txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            module_name_txt = itemView.findViewById(R.id.module_name_txt);
            count_words_txt = itemView.findViewById(R.id.count_words_txt);
        }
    }
}
