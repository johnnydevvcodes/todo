package com.simpleapp.todo;

/**
 * Created by sarge.andee on 06/09/2017.
 */

public class ToDo {
    private String mTodo;
    private String mTodoId;
    private String mDoneUndone;

    public ToDo(String todo, String todoId,String doneUndone) {
        mTodo = todo;
        mTodoId = todoId;
        mDoneUndone = doneUndone;
    }

    public String getTodo() {
        return mTodo;
    }

    public String getTodoId() { return mTodoId; }

    public String getDoneUndone() { return mDoneUndone; }

}
