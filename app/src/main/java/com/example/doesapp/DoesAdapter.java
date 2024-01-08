package com.example.doesapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DoesAdapter extends RecyclerView.Adapter<DoesAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<MyDoes> myDoes;

    public DoesAdapter(Context context, ArrayList<MyDoes> myDoes) {
        this.context = context;
        this.myDoes = myDoes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_does, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        MyDoes currentDoes = myDoes.get(i);

        myViewHolder.titledoes.setText(currentDoes.getTitledoes());
        myViewHolder.descdoes.setText(currentDoes.getDescdoes());
        myViewHolder.datedoes.setText(currentDoes.getDatedoes());

        final String getTitleDoes = currentDoes.getTitledoes();
        final String getDescDoes = currentDoes.getDescdoes();
        final String getDateDoes = currentDoes.getDatedoes();
        final String getKeydoes = currentDoes.getKeydoes();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aa = new Intent(context, EditTaskDesk.class);
                aa.putExtra("titledoes", getTitleDoes);
                aa.putExtra("descdoes", getDescDoes);
                aa.putExtra("datedoes", getDateDoes);
                aa.putExtra("keydoes", getKeydoes);
                context.startActivity(aa);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myDoes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titledoes, descdoes, datedoes, keydoes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titledoes = itemView.findViewById(R.id.titledoes);
            descdoes = itemView.findViewById(R.id.descdoes);
            datedoes = itemView.findViewById(R.id.datedoes);
        }
    }
}
