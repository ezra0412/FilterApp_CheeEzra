package com.example.filterapp.adapter;

import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterapp.R;
import com.example.filterapp.classes.BtLongDoubleItem;

import java.util.List;

public class BtAdapterDouble extends RecyclerView.Adapter<BtAdapterDouble.ViewHolder> {

    private List<BtLongDoubleItem> btLongDoubleItems;
    private BtDoubleListener mBtDoubleLIstener;

    public BtAdapterDouble(List<BtLongDoubleItem> btLongDoubleItems, BtDoubleListener btDoubleListener) {
        this.btLongDoubleItems = btLongDoubleItems;
        this.mBtDoubleLIstener = btDoubleListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.button_long_double, parent, false);

        return new ViewHolder(v, mBtDoubleLIstener);
    }

    @Override
    public void onBindViewHolder(@NonNull BtAdapterDouble.ViewHolder holder, int position) {
        String data1 = btLongDoubleItems.get(position).getItem1();
        String data2 = btLongDoubleItems.get(position).getItem2();

        holder.item1.setText(data1);
        holder.item2.setText(data2);
    }

    @Override
    public int getItemCount() {
        return btLongDoubleItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView item1, item2;
        BtDoubleListener btDoubleListener;

        public ViewHolder(@NonNull View itemView, BtDoubleListener btDoubleListener) {
            super(itemView);
            item1 = itemView.findViewById(R.id.tv_item1_buttonLongDouble);
            item2 = itemView.findViewById(R.id.tv_item2_buttonLongDouble);
            itemView.setOnClickListener(this);
            this.btDoubleListener = btDoubleListener;

        }

        @Override
        public void onClick(View view) {
            btDoubleListener.btDoubleListener(getLayoutPosition());
        }
    }

    public interface BtDoubleListener {
        void btDoubleListener(int position);
    }
}
