package com.example.myapplication2;

public class Todo {

    private int id;
    private String contents;
    private int hour;
    private int minute;
    private String checked;
    private String switching;


    public Todo(){
        this.contents = "";
        this.hour = 100;
        this.minute = 100;
        this.checked = "false";
        this.switching = "false";
    }

    public Todo(String contents){
        this.contents = contents;
        this.hour = 100;
        this.minute = 100;
        this.checked = "false";
        this.switching = "false";
    }

    public Todo(int id, String contents){
        this.id = id;
        this.contents = contents;
        this.hour = 100;
        this.minute = 100;
        this.checked = "false";
        this.switching = "false";

    }

    public Todo(int id, String contents, int hour, int minute, String checked, String switching){
        this.id = id;
        this.contents = contents;
        this.hour = hour;
        this.minute = minute;
        this.hour = hour;
        this.minute = minute;
        this.checked = checked;
        this.switching = switching;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getChecked() {
        return checked;
    }

    public String getSwitching() {
        return switching;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public void setSwitching(String switching) {
        this.switching = switching;
    }
}
