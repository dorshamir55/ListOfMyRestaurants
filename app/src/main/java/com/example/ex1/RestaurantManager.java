package com.example.ex1;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RestaurantManager {
    public static RestaurantManager instance;
    private Context context;
    private static ArrayList<Restaurant> restaurants = new ArrayList<>();

    static final String FILE_NAME = "restaurants.dat";

    private RestaurantManager(Context context){
        this.context=context;

        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            restaurants = (ArrayList<Restaurant>)ois.readObject();

            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static RestaurantManager getInstance(Context context){
        if(instance==null){
            instance = new RestaurantManager(context);
        }
        return  instance;
    }

    public Restaurant getRestaurant(int position){
        if (position<restaurants.size()){
            return restaurants.get(position);
        }
        return null;
    }

    public ArrayList<Restaurant> getRestaurants(){
        return restaurants;
    }

    public void addRestaurant(Restaurant restaurant){
        restaurants.add(restaurant);
        saveRestaurants();
    }

    public void removeRestaurant(int position){
        if(position<restaurants.size()){
            removePictureFromExternalStorage(position);
            restaurants.remove(position);
        }
        saveRestaurants();
    }

    public void removePictureFromExternalStorage(int position){
        File file = new File(restaurants.get(position).getPath());
        file.delete();
    }

    public void saveRestaurants(){
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_APPEND);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(restaurants);

            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateRestaurants(){
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(restaurants);

            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
