package com.simpleapp.todo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ToDoActivity extends AppCompatActivity {

    // This is the Adapter being used for listing

    private ListView listView;

    private ToDoAdapter mAdapter;

    private String currentToDo;
    private String currentId;

    private String doneUndone;
    private String mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("QenWQd7lPlUSHRdyIBbznPGUG9LGmZQZVhuSdBcR")
                .clientKey("oflD9Cm8YtWRCWHPh6AiMKdbUl2VzjSTLBDFYpCF")
                .server("https://parseapi.back4app.com/")
                .build()
        );

        listView = (ListView) findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView todoText = (TextView) view.findViewById(R.id.todoTextItem);
                currentToDo = todoText.getText().toString();
                EditText todoEdit = (EditText) findViewById(R.id.todo_edit);
                todoEdit.setText(currentToDo);
                TextView idText = (TextView) view.findViewById(R.id.hiddenId);
                currentId = idText.getText().toString();
                TextView doneUndoneText = (TextView) view.findViewById(R.id.hiddenStats);
                doneUndone = doneUndoneText.getText().toString();

                Toast.makeText(getApplicationContext(), "todo name:  " + currentToDo + "\n" + "id: " + currentId, Toast.LENGTH_SHORT).show();


                showChangeLangDialog();
            }
        });


        retrieveRecord();

        Button b = (Button) findViewById(R.id.add);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecord();
            }
        });

        Button delAll = (Button) findViewById(R.id.delete_all);
        delAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll();
            }
        });

    }


    /**
     * addRecord() will  add a single record to the parse db
     */
    private void addRecord() {
        TextView todotv = (TextView) findViewById(R.id.todo_edit);
        String todo = todotv.getText().toString().trim();
        if (!TextUtils.isEmpty(todo)) {
            final ParseObject todoObject = new ParseObject("ToDo");
            todoObject.put("todo", todo);
            todoObject.put("doneUndone", "undone");
            todoObject.saveInBackground();
            retrieveRecord();
        } else {
            Toast.makeText(getBaseContext(), "Please fill out.", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteRecord(String todoId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ToDo");
        query.whereEqualTo("objectId", todoId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> todolist, ParseException e) {
                if (e == null) {
                    for (ParseObject t : todolist) {
                        t.deleteInBackground();
                        Toast.makeText(getBaseContext(), "Record deleted.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "No todo id captured.", Toast.LENGTH_SHORT).show();
                }
                retrieveRecord();
            }
        });
    }

    private void deleteAll() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ToDo");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ParseObject.deleteAllInBackground(objects);
                    Toast.makeText(getBaseContext(), "Records deleted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "No records.", Toast.LENGTH_SHORT).show();
                }
                retrieveRecord();
            }
        });
    }

    private void retrieveRecord() {
        if (mAdapter != null) {
            mAdapter.clear();
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ToDo");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                StringBuilder sb = new StringBuilder();
                if (e == null) {

                    ArrayList<ToDo> todoList = new ArrayList<ToDo>();

                    for (ParseObject p : objects) {
                        ToDo toDo = new ToDo(p.getString("todo"), p.getObjectId().toString(), p.getString("doneUndone"));
                        todoList.add(toDo);
                    }
                    mAdapter = new ToDoAdapter(getBaseContext(), todoList);
                    listView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getBaseContext(), "No records found.", Toast.LENGTH_SHORT).show();
                }
                EditText todoTextView = (EditText) findViewById(R.id.todo_edit);
                todoTextView.setText("");
            }
        });


    }

    private void showChangeLangDialog() {
        if (doneUndone.equals("done")) {
            mark = "mark as undone";
            doneUndone = "undone";
        } else {
            mark = "mark as done";
            doneUndone = "done";
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        edt.setText(currentToDo);

        dialogBuilder.setMessage("Todo id: " + currentId);
        dialogBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                deleteRecord(currentId);
            }
        });
        dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("ToDo");
                query.whereEqualTo("objectId", currentId);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> todolist, ParseException e) {
                        EditText editText = (EditText) dialogView.findViewById(R.id.edit1);
                        String todo = editText.getText().toString();
                        if (TextUtils.isEmpty(todo)) {
                            Toast.makeText(getBaseContext(), "Please fill out.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (e == null) {
                            for (ParseObject t : todolist) {
                                t.put("todo", todo);
                                t.saveInBackground();
                                retrieveRecord();
                                Toast.makeText(getBaseContext(), "Records updated.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "Record not updated.", Toast.LENGTH_SHORT).show();
                        }
                        retrieveRecord();
                    }
                });
            }
        });

        dialogBuilder.setNeutralButton(mark, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("ToDo");
                query.whereEqualTo("objectId", currentId);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> todolist, ParseException e) {
                        if (e == null) {
                            for (ParseObject t : todolist) {
                                t.put("doneUndone", doneUndone);
                                t.saveInBackground();
                                retrieveRecord();
                                Toast.makeText(getBaseContext(), "Records updated.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "Record not updated.", Toast.LENGTH_SHORT).show();
                        }
                        retrieveRecord();
                    }
                });
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

}