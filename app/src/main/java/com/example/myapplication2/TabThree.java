package com.example.myapplication2;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;

public class TabThree extends Fragment {
    private ArrayList<Todo> ArrayListOfEdit = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        final Fragment fragment = this;

        final DBtodoHelper db = new DBtodoHelper(getContext());
        ArrayListOfEdit= db.getAllTodos();
        RecyclerviewTodoAdapter recyclerTodoAdapter = new RecyclerviewTodoAdapter(getActivity(),getContext(),fragment, ArrayListOfEdit);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_tab3);
        recyclerView.setAdapter(recyclerTodoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Button allDel = (Button) view.findViewById(R.id.alldel);
        allDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayListOfEdit = db.getAllTodos();
                for(int i =0; i<ArrayListOfEdit.size(); i++){
                    db.deleteTodo(ArrayListOfEdit.get(i));
                    AlarmManager alarm_manager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent(getContext(), Allimmaker.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getContext(),ArrayListOfEdit.get(i).getId(),intent ,PendingIntent.FLAG_UPDATE_CURRENT);
                    if (pendingIntent != null)
                    { alarm_manager.cancel(pendingIntent);
                        pendingIntent.cancel();
                    }

                }
                FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                ft.detach(fragment).attach(fragment).commit();

            }
        });


        Button buttonInsert = (Button) view.findViewById(R.id.addTodo);
        buttonInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext()).inflate(R.layout.todo_edit_box, null, false);
                builder.setView(view);

                final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_update);
                final EditText editTextContents = (EditText) view.findViewById(R.id.edittext_dialog_contents);

                ButtonSubmit.setText("Update");

                final AlertDialog dialog = builder.create();

                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strContents = editTextContents.getText().toString();

                        Todo todo = new Todo(strContents);
                        db.addTodo(todo);
                        ArrayListOfEdit=db.getAllTodos();
                        dialog.dismiss();
                        RecyclerviewTodoAdapter recyclerTodoAdapter = new RecyclerviewTodoAdapter(getActivity(),getContext(),fragment, ArrayListOfEdit);
                        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView_tab3);
                        recyclerView.setAdapter(recyclerTodoAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                });

                dialog.show();
            }
        });

        return view;
    }
}