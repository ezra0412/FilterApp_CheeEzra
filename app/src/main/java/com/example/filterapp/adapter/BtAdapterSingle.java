package com.example.filterapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterapp.R;

import java.util.List;

public class BtAdapterSingle extends RecyclerView.Adapter<BtAdapterSingle.ViewHolder> {

    private List<String> listOfData;
    private BtSingleListener mBtSingleListener;

    public BtAdapterSingle(List<String> listOfData, BtSingleListener btSingleListener) {
        this.listOfData = listOfData;
        this.mBtSingleListener = btSingleListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.button_long_single, parent, false);

        return new ViewHolder(v, mBtSingleListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BtAdapterSingle.ViewHolder holder, int position) {
        String data = listOfData.get(position);

        holder.displayItem.setText(data);
    }

    @Override
    public int getItemCount() {
        return listOfData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView displayItem;
        BtSingleListener btSingleListener;

        public ViewHolder(@NonNull View itemView, BtSingleListener btSingleListener) {
            super(itemView);
            displayItem = itemView.findViewById(R.id.tv_char_btLongSingle);
            itemView.setOnClickListener(this);
            this.btSingleListener = btSingleListener;
        }

        @Override
        public void onClick(View view) {
            btSingleListener.btSingleListener(getAdapterPosition());
        }
    }

    public interface BtSingleListener {
        void btSingleListener(int position);
    }


}
