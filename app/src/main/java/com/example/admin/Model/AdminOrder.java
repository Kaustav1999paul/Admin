package com.example.admin.Model;

public class AdminOrder {

    private String Email;
    private String Name;
    private String PhoneNo;
    private String Qty;
    private String address;
    private String date;
    private String image;
    private String is_Cancelled;
    private String price;
    private String state;
    private String time;
    private String ID;

    public AdminOrder() {
    }

    public AdminOrder(String email, String name, String phoneNo, String qty, String address, String date, String image, String is_Cancelled, String price, String state, String time, String ID) {
        Email = email;
        Name = name;
        PhoneNo = phoneNo;
        Qty = qty;
        this.address = address;
        this.date = date;
        this.image = image;
        this.is_Cancelled = is_Cancelled;
        this.price = price;
        this.state = state;
        this.time = time;
        this.ID = ID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIs_Cancelled() {
        return is_Cancelled;
    }

    public void setIs_Cancelled(String is_Cancelled) {
        this.is_Cancelled = is_Cancelled;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
