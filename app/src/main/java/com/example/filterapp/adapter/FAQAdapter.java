package com.example.filterapp.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterapp.R;
import com.example.filterapp.classes.FAQitem;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

import java.util.List;


class FAQAdapterWith_child extends RecyclerView.ViewHolder {

    public TextView question, answer;
    public ImageView button;
    public ExpandableLinearLayout expandableLayout;

    public FAQAdapterWith_child(View itemView) {
        super(itemView);
        question = itemView.findViewById(R.id.tv_question_faqItem);
        answer = itemView.findViewById(R.id.tv_answer_faqItem);
        button = itemView.findViewById(R.id.img_arrow_faqItem);

        expandableLayout = itemView.findViewById(R.id.expandable_layout_faqItem);

    }
}


public class FAQAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<FAQitem> faqItems;
    Context context;
    SparseBooleanArray expandState = new SparseBooleanArray();

    public FAQAdapter(List<FAQitem> faqItems) {
        this.faqItems = faqItems;
        for (int i = 0; i < getItemCount(); i++) {
            expandState.append(i, false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (faqItems.get(position).isExpandable()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.faq_item, parent, false);
        return new FAQAdapterWith_child(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final FAQAdapterWith_child viewHolder = (FAQAdapterWith_child) holder;
        FAQitem item = faqItems.get(position);
        viewHolder.setIsRecyclable(false);
        viewHolder.question.setText(item.getQuestion());
        viewHolder.expandableLayout.setInRecyclerView(true);
        viewHolder.expandableLayout.setExpanded(expandState.get(position));
        viewHolder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {


            @Override
            public void onPreClose() {
                changeRotate(viewHolder.button, 0f, 180f).start();
                expandState.put(position, false);
            }


            @Override
            public void onPreOpen() {
                changeRotate(viewHolder.button, 180f, 0f).start();
                expandState.put(position, true);
            }

        });

        viewHolder.button.setRotation(expandState.get(position) ? 180f : 0f);

        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.expandableLayout.toggle();
            }
        });

        viewHolder.answer.setText(faqItems.get(position).getAnswer());
    }

    private ObjectAnimator changeRotate(ImageView button, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "rotation", to, from);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    @Override
    public int getItemCount() {
        return faqItems.size();
    }
}
