package com.example.myapplication2;

public class Contact {

    private int id;
    private String name;
    private String phone_number;
    private String address;
    private String email;
    private String birthday;
    private String memo;


    public Contact(){
        name = null;
        phone_number=null;
        address=null;
        email=null;
        birthday=null;
        memo=null;

    }
    public Contact(int id, String name, String phone_number, String address, String email, String birthday, String memo){
        this.id = id;
        this.name = name;
        this.phone_number = phone_number;
        this.address = address;
        this.email = email;
        this.birthday = birthday;
        this.memo = memo;
    }

    public Contact(String name, String phone_number, String address, String email, String birthday, String memo){
        this.name = name;
        this.phone_number = phone_number;
        this.address = address;
        this.email = email;
        this.birthday = birthday;
        this.memo = memo;
    }


    public int getID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPhoneNumber(){
        return this.phone_number;
    }

    public void setPhoneNumber(String phone_number){
        this.phone_number = phone_number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return this.email;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public String getAddress() {
        return this.address;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
