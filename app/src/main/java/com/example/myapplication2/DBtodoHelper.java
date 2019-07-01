package com.example.myapplication2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SearchView;

import java.util.ArrayList;

public class DBtodoHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "todoManager";

    // Contacts table name
    private static final String TABLE_TODO = "todo";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_CONTENTS = "contents";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";
    private static final String KEY_CHECK = "checked";
    private static final String KEY_SWITCH = "switch";

    public DBtodoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CONTENTS + " TEXT," + KEY_HOUR + " INTEGER," + KEY_MINUTE + " INTEGER," + KEY_CHECK + " TEXT," + KEY_SWITCH + " TEXT" + ")";
        db.execSQL(CREATE_TODO_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);

        // Create tables again
        onCreate(db);
    }

    /**
     * CRUD 함수
     */

    // 새로운 Todo 함수 추가
    public void addTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_CONTENTS, todo.getContents()); // Contact Name
            values.put(KEY_HOUR, todo.getHour());
            values.put(KEY_MINUTE, todo.getMinute());
            values.put(KEY_CHECK, todo.getChecked());
            values.put(KEY_SWITCH, todo.getSwitching());
            // Inserting Row
            db.insert(TABLE_TODO, null, values);

            db.close(); // Closing database connection

    }

    // id 에 해당하는 Contact 객체 가져오기
    public Todo getTodo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TODO, new String[] { KEY_ID,
                        KEY_CONTENTS, KEY_HOUR, KEY_MINUTE, KEY_CHECK, KEY_SWITCH}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Todo todo = new Todo(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5));
        // return contact
        return todo;
    }

    // 모든 Contact 정보 가져오기
    public ArrayList<Todo> getAllTodos() {
        ArrayList<Todo> todoList = new ArrayList<Todo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TODO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_TODO, null, null,
                null, null, null,  null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Todo todo = new Todo();
                todo.setId(Integer.parseInt(cursor.getString(0)));
                todo.setContents(cursor.getString(1));
                todo.setHour(Integer.parseInt(cursor.getString(2)));
                todo.setMinute(Integer.parseInt((cursor.getString(3))));
                todo.setChecked(cursor.getString(4));
                todo.setSwitching(cursor.getString(5));
                // Adding contact to list
                todoList.add(todo);
        } while (cursor.moveToNext());
        }

        // return contact list
        return todoList;
    }

    //Contact 정보 업데이트
    public void updateTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CONTENTS, todo.getContents());
        values.put(KEY_HOUR, todo.getHour());
        values.put(KEY_MINUTE, todo.getMinute());
        values.put(KEY_CHECK, todo.getChecked());
        values.put(KEY_SWITCH, todo.getSwitching());


        // updating row
        db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(todo.getId()) });
    }

    // Contact 정보 삭제하기
    public void deleteTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[] { String.valueOf(todo.getId()) });
        db.close();
    }

    // Contact 정보 숫자
    public int getTodoCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}