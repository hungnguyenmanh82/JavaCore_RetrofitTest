package com.hung.myapplication.user;

/**
 * Created by hungnm2 on 12/28/2017.
 */

public class User {

    /**
     * id : 1
     * name : nguyen manh hung
     * address : Hanoi
     * phone : 123455
     */

    private int id;
    private String name;
    private String address;
    private int phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
}
