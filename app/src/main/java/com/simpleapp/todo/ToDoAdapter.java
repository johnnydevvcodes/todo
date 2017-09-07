package com.simpleapp.todo;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class ToDoAdapter extends ArrayAdapter<ToDo> {

    ToDoAdapter(Context context, List<ToDo> toDoList) {
        super(context, 0, toDoList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }

        //Get the position of current todo
        final ToDo todo = getItem(position);

        TextView todoTextView = (TextView) listItemView.findViewById(R.id.todoTextItem);
        todoTextView.setText(todo.getTodo().toString().trim());


        TextView hiddenId = (TextView) listItemView.findViewById(R.id.hiddenId);
        hiddenId.setText(todo.getTodoId().toString().trim());

        TextView hiddenStats = (TextView) listItemView.findViewById(R.id.hiddenStats);
        String stats= todo.getDoneUndone().toString().trim();
        hiddenStats.setText(stats);


        if (stats.equals("done")) {
            todoTextView.setPaintFlags(todoTextView.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG );
        } else {
            todoTextView.setPaintFlags(todoTextView.getPaintFlags()& (~Paint.STRIKE_THRU_TEXT_FLAG ));
        }

        return listItemView;
    }
}
