package com.example.myapplication2;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;

public class RecyclerviewTodoAdapter extends RecyclerView.Adapter<RecyclerviewTodoAdapter.ViewHolder> {

    private ArrayList<Todo> arrayListOfTodo;
    final private Context mcontext;
    private Activity activity;
    private Fragment fragment;




    //Adapter 초기화 및 생성자로 받은 데이터기반으로 Adapter 내 데이터 세팅
    public RecyclerviewTodoAdapter(Activity activity, Context context, Fragment fragment, ArrayList<Todo> arrayListOfTodo){
        this.mcontext = context;
        this.arrayListOfTodo = arrayListOfTodo;
        this.activity = activity;
        this.fragment = fragment;
    }

    //ViewHolder가 초기화 될 때 혹은 ViewHolder를 초기화 할 때 실행되는 메서드
    @Override
    public RecyclerviewTodoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Context를 부모로 부터 받아와서
        Context context = parent.getContext() ;

        //받은 Context를 기반으로 LayoutInflater를 생성
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //생성된 LayoutInflater로 어떤 Layout을 가져와서 어떻게 View를 그릴지 결정
        View todoView = layoutInflater.inflate(R.layout.recyclerview_todo, parent, false);

        //View 생성 후, 이 View를 관리하기위한 ViewHolder를 생성
        RecyclerviewTodoAdapter.ViewHolder viewHolder = new RecyclerviewTodoAdapter.ViewHolder(todoView);

        //생성된 ViewHolder를 OnBindViewHolder로 넘겨줌
        return viewHolder;
    }

    //RecyclerView의 Row 하나하나를 구현하기위해 Bind(묶이다) 될 때
    @Override
    public void onBindViewHolder(final RecyclerviewTodoAdapter.ViewHolder holder, int position) {


        //RecyclerView에 들어갈 Data(Student로 이루어진 ArrayList 배열인 arrayListOfStudent)를 기반으로 Row를 생성할 때
        //해당 row의 위치에 해당하는 Student를 가져와서
        final Todo todo = arrayListOfTodo.get(position);

        //넘겨받은 ViewHolder의 Layout에 있는 View들을 어떻게 다룰지 설정
        //ex. TextView의 text를 어떻게 설정할지, Button을 어떻게 설정할지 등등...
        final CheckBox txtContents = holder.txtContents;
        txtContents.setText(todo.getContents());

        final Switch alarm = holder.alarm;

        final TextView txtClock = holder.clock;
        Integer hour = todo.getHour();

        if(arrayListOfTodo.get(position).getChecked().equals("true")){
            txtContents.setChecked(true);
            txtContents.setPaintFlags(txtContents.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            txtContents.setTextColor(Color.parseColor("#808080"));
        }else{
            txtContents.setChecked(false);
            txtContents.setPaintFlags(0);
            txtContents.setTextColor(Color.parseColor("#000000"));
        }

        if(arrayListOfTodo.get(position).getSwitching().equals("true")){
            alarm.setChecked(true);
            String h = String.valueOf(todo.getHour());
            String m = String.valueOf(todo.getMinute());
            if(String.valueOf(todo.getMinute()).length()==1){
                m = "0"+m;
            }
            txtClock.setText(h+":"+m);
        }else{
            alarm.setChecked(false);
        }



    }



    //요 메서드가 arrayListOfStudent에 들어있는 Student 개수만큼 카운트해줌
    //요녀석이 있어야 arrayListOfStudent에 넣어준 Student의 데이터를 모두 그릴수 있음
    @Override
    public int getItemCount() {
        return arrayListOfTodo.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public CheckBox txtContents;
        public Switch alarm;
        public TextView clock;

        //ViewHolder 생성
        public ViewHolder(View itemView) {
            super(itemView);

            //View를 넘겨받아서 ViewHolder를 완성
            txtContents = (CheckBox) itemView.findViewById(R.id.todo);
            alarm = (Switch) itemView.findViewById(R.id.alarm);
            clock = (TextView) itemView.findViewById(R.id.clock);


            txtContents.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   if (arrayListOfTodo.get(getAdapterPosition()).getChecked().equals("true")) {
                                                       DBtodoHelper db = new DBtodoHelper(mcontext);
                                                       Todo todo = new Todo(arrayListOfTodo.get(getAdapterPosition()).getId(), arrayListOfTodo.get(getAdapterPosition()).getContents(), arrayListOfTodo.get(getAdapterPosition()).getHour(), arrayListOfTodo.get(getAdapterPosition()).getMinute(), "false", arrayListOfTodo.get(getAdapterPosition()).getSwitching());
                                                       db.updateTodo(todo);
                                                       FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                                                       ft.detach(fragment).attach(fragment).commit();
                                                   } else {
                                                       DBtodoHelper db = new DBtodoHelper(mcontext);
                                                       Todo todo = new Todo(arrayListOfTodo.get(getAdapterPosition()).getId(), arrayListOfTodo.get(getAdapterPosition()).getContents(), arrayListOfTodo.get(getAdapterPosition()).getHour(), arrayListOfTodo.get(getAdapterPosition()).getMinute(), "true", arrayListOfTodo.get(getAdapterPosition()).getSwitching());
                                                       db.updateTodo(todo);
                                                       FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                                                       ft.detach(fragment).attach(fragment).commit();
                                                   }
                                               }
                                           });
            txtContents.setOnCreateContextMenuListener(this);


            alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(arrayListOfTodo.get(getAdapterPosition()).getSwitching().equals("true")) {
                        DBtodoHelper db = new DBtodoHelper(mcontext);
                        Todo todo = new Todo(arrayListOfTodo.get(getAdapterPosition()).getId(), arrayListOfTodo.get(getAdapterPosition()).getContents(), 100,100, arrayListOfTodo.get(getAdapterPosition()).getChecked(), "false");
                        db.updateTodo(todo);
                        FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                        ft.detach(fragment).attach(fragment).commit();

                        AlarmManager alarm_manager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
                        Intent intent = new Intent(mcontext, Allimmaker.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(mcontext,todo.getId(),intent ,PendingIntent.FLAG_UPDATE_CURRENT);
                        if (pendingIntent != null)
                        { alarm_manager.cancel(pendingIntent);
                            pendingIntent.cancel();
                        }
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                        View view = LayoutInflater.from(mcontext)
                                .inflate(R.layout.timepicker, null, false);
                        builder.setView(view);
                        final AlarmManager alarm_manager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);

                        final Calendar calendar = Calendar.getInstance();

                        final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_update);

                        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.edittext_dialog_timepicker);

                        final AlertDialog dialog = builder.create();
                        ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                                calendar.set(Calendar.MINUTE, timePicker.getMinute());

                                DBtodoHelper db = new DBtodoHelper(mcontext);
                                Todo todo = new Todo(arrayListOfTodo.get(getAdapterPosition()).getId(), arrayListOfTodo.get(getAdapterPosition()).getContents(), timePicker.getHour(),timePicker.getMinute(), arrayListOfTodo.get(getAdapterPosition()).getChecked(), "true");
                                db.updateTodo(todo);
                                dialog.dismiss();

                                FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                                ft.detach(fragment).attach(fragment).commit();

                                Intent intent = new Intent(mcontext, Allimmaker.class);
                                intent.putExtra("id", todo.getId());
                                PendingIntent pendingIntent = PendingIntent.getActivity(mcontext,todo.getId(),intent ,PendingIntent.FLAG_CANCEL_CURRENT);
                                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            }
                        });

                        dialog.show();


                    }

                }
            });

        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "Edit");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "Delete");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);

        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                final DBtodoHelper db = new DBtodoHelper(mcontext);

                switch (item.getItemId()) {

                    case 1001:
                        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                        View view = LayoutInflater.from(mcontext)
                                .inflate(R.layout.todo_edit_box, null, false);
                        builder.setView(view);
                        final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_update);
                        final EditText editTextName = (EditText) view.findViewById(R.id.edittext_dialog_contents);

                        editTextName.setText(arrayListOfTodo.get(getAdapterPosition()).getContents());


                        final AlertDialog dialog = builder.create();
                        ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String strContents = editTextName.getText().toString();

                                Todo todo = new Todo(arrayListOfTodo.get(getAdapterPosition()).getId(), strContents);
                                db.updateTodo(todo);
                                arrayListOfTodo=db.getAllTodos();
                                dialog.dismiss();

                                FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                                ft.detach(fragment).attach(fragment).commit();
                            }
                        });

                        dialog.show();

                        break;


                    case 1002:
                        AlarmManager alarm_manager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
                        Intent intent = new Intent(mcontext, Allimmaker.class);
                        Todo todo = arrayListOfTodo.get(getAdapterPosition());
                        PendingIntent pendingIntent = PendingIntent.getActivity(mcontext,todo.getId(),intent ,PendingIntent.FLAG_UPDATE_CURRENT);
                        if (pendingIntent != null)
                        { alarm_manager.cancel(pendingIntent);
                            pendingIntent.cancel();
                        }

                        db.deleteTodo(arrayListOfTodo.get(getAdapterPosition()));
                        arrayListOfTodo=db.getAllTodos();
                        FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                        ft.detach(fragment).attach(fragment).commit();


                        break;
                }


                return true;
            }
        };
    }
}