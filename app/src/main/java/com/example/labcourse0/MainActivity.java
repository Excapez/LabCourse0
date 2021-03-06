package com.example.labcourse0;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TodoDialog.TodoDialogListener {

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference todoRef = database.getReference("todos");
    FloatingActionButton fab;
    ArrayList<Todo> todoList = new ArrayList<>();
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
        addFloatingActionButton();
        addSwipeToDelete();
        addChildEventListener();
    }

    private void initRecyclerView()
    {
        recyclerView = findViewById(R.id.recyclerView);
        myAdapter = new MyAdapter(this, todoList);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void addFloatingActionButton(){
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    private void addSwipeToDelete(){
        itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                todoRef.child(todoList.get(viewHolder.getLayoutPosition()).key).removeValue();
            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    private void addChildEventListener()
    {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                /*for(DataSnapshot child : snapshot.getChildren())
                {
                    Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                    Log.d("snapshottest", child.getValue().toString());
                }*/
                Todo newTodo = snapshot.getValue(Todo.class);
                Log.d("snapshottext", newTodo.description);
                Log.d("snapshottext", ""+newTodo.priority);
                newTodo.setKey(snapshot.getKey());
                todoList.add(newTodo);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {



                for(Todo todo : todoList) {
                    Log.d("snapshot key", todo.key);
                    Log.d("snapshot key", snapshot.getKey());
                    if(todo.key.equals(snapshot.getKey())) {
                        Log.d("snapshot key", "found");
                        Todo changedTodo = snapshot.getValue(Todo.class);
                        todo.description = changedTodo.getDescription();
                        todo.priority = changedTodo.getPriority();
                        break;
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                for(Todo todo : todoList) {
                    if(todo.key.equals(snapshot.getKey())) {
                        todoList.remove(todo);
                        break;
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("snapshot", "onChildMoved");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("snapshot", "onCancelled");
            }
        };
        todoRef.addChildEventListener(childEventListener);
    }

    public void openDialog() {
        TodoDialog todoDialog = new TodoDialog();
        todoDialog.show(getSupportFragmentManager(), "todo dialog");
    }

    @Override
    public void applyText(String todoDescription, Integer priority) {
        Toast.makeText(this, ""+priority, Toast.LENGTH_SHORT).show();
        String newKey = todoRef.push().getKey();
        todoRef.child(newKey).child("description").setValue(todoDescription);
        todoRef.child(newKey).child("priority").setValue(priority.toString());

    }
}