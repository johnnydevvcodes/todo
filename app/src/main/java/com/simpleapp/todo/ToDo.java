package com.simpleapp.todo;


class ToDo {
    private String mTodo;
    private String mTodoId;
    private String mDoneUndone;

    ToDo(String todo, String todoId, String doneUndone) {
        mTodo = todo;
        mTodoId = todoId;
        mDoneUndone = doneUndone;
    }


    String getTodo() {
        return mTodo;
    }

    String getTodoId() {
        return mTodoId;
    }

    String getDoneUndone() {
        return mDoneUndone;
    }

}
