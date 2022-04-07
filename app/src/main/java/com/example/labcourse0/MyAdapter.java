package com.example.labcourse0;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<Todo> todoList;
    Context context;

    public MyAdapter(Context context, ArrayList<Todo> todoList)
    {
        this.context = context;
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.todoText.setText(todoList.get(position).description);
        holder.priorityText.setText("Priority: " + todoList.get(position).priority);
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView todoText;
        TextView priorityText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            todoText = itemView.findViewById(R.id.todoTextView);
            priorityText = itemView.findViewById(R.id.priorityTextView);
        }
    }
}
