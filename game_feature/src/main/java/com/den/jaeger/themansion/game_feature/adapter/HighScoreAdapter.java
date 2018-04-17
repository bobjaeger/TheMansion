package com.den.jaeger.themansion.game_feature.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.den.jaeger.themansion.game_feature.R;
import com.den.jaeger.themansion.game_feature.model.HighScoreModel;

import java.util.List;

public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.ViewHolder> {

    private List<HighScoreModel> highScoreModelList;

    public HighScoreAdapter(List<HighScoreModel> highScoreModelList) {
        this.highScoreModelList = highScoreModelList;
    }

    @NonNull
    @Override
    public HighScoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_high_score, parent, false);
        return new HighScoreAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HighScoreAdapter.ViewHolder holder, int position) {
        holder.bindData(highScoreModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return highScoreModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textTimeFinished;

        ViewHolder(View v) {
            super(v);
            textName = v.findViewById(R.id.textName);
            textTimeFinished = v.findViewById(R.id.textTimeFinished);
        }

        void bindData(HighScoreModel highScoreModel){
            textName.setText(highScoreModel.getName());
            textTimeFinished.setText(String.valueOf(highScoreModel.getTimeFinished()));
        }
    }
}
