package com.example.ex1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.Serializable;

public class Restaurant implements Serializable {
    private String name;
    private String address;
    private String phoneNumber;
    private String path;
    transient private Bitmap photo;

    public Restaurant(String name, String address, String phoneNumber, String path, Bitmap photo) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.path = path;
        this.photo = photo;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", path='" + path + '\'' +
                ", photo=" + photo +
                '}';
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException{
        photo.compress(Bitmap.CompressFormat.JPEG, 10, out);

        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
        photo = BitmapFactory.decodeStream(in);

        in.defaultReadObject();
    }
}
