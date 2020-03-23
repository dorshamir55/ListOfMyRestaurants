package com.example.ex1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.Serializable;

public class Restaurant implements Serializable {
    private String name;
    private String address;
    private String type;
    transient private Bitmap photo;
    transient private Bitmap qualityPhoto;

    public Restaurant(String name, String address, String type, Bitmap photo, Bitmap qualityBitmap) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.photo = photo;
        this.qualityPhoto = qualityBitmap;
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

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", type='" + type + '\'' +
                ", photo=" + photo +
                ", qualityPhoto=" + qualityPhoto +
                '}';
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public Bitmap getQualityPhoto() {
        return qualityPhoto;
    }

    public void setQualityPhoto(Bitmap photo) {
        this.qualityPhoto = qualityPhoto;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException{
        photo.compress(Bitmap.CompressFormat.JPEG, 50, out);
        //qualityPhoto.compress(Bitmap.CompressFormat.JPEG, 100, out);
        qualityPhoto = photo;

        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
        photo = BitmapFactory.decodeStream(in);
        //qualityPhoto = BitmapFactory.decodeStream(in);
        qualityPhoto = photo;

        in.defaultReadObject();
    }
}
