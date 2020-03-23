package com.example.ex1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.ex1.RestaurantManager.FILE_NAME;


public class ListOfRestaurantsActivity extends AppCompatActivity {
    List<Restaurant> restaurants = new ArrayList<>();
    TextView closeConfirmRemove;
    Button yesButton;
    Button noButton;
    Dialog confirmRemoveDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_restaurants);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        confirmRemoveDialog = new Dialog(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean registered = prefs.getBoolean("registered", true);
        if(registered){
            firstRegister();
        }*/

        //restaurants.addAll(load());
        //Toast.makeText(this ,restaurants.get(0).getName(), Toast.LENGTH_SHORT).show();
        //ListView listView = (ListView)findViewById(R.id.restaurant_list);
        final RestaurantManager manager = RestaurantManager.getInstance(this);
        final RestaurantAdapter restaurantAdapter = new RestaurantAdapter(manager.getRestaurants());
        manager.updateRestaurants();
        restaurantAdapter.setListener(new RestaurantAdapter.MyRestaurantListener() {
            @Override
            public void onRestaurantClicked(int position, View view) {
                Intent intent = new Intent(ListOfRestaurantsActivity.this, ShowRestaurantActivity.class);
                Bundle extras = new Bundle();
                /*extras.putString("PhotoPath", restaurants.get(position).getQualityPhoto());
                extras.putString("Name", restaurants.get(position).getName());
                extras.putString("Address", restaurants.get(position).getAddress());
                extras.putString("FoodType", restaurants.get(position).getType());*/
                extras.putInt("Position", position);
                intent.putExtras(extras);
                startActivity(intent);
            }

            @Override
            public void onRestaurantLongClicked(int position, View view) {

            }
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.START|ItemTouchHelper.END|ItemTouchHelper.UP|ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Toast.makeText(ListOfRestaurantsActivity.this, manager.getRestaurants().size(), Toast.LENGTH_SHORT).show();
                Collections.swap(restaurants, fromPosition, toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                manager.updateRestaurants();

                return false;
            };

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                confirmRemoveDialog.setContentView(R.layout.confirm_remove);
                closeConfirmRemove = (TextView) (confirmRemoveDialog.findViewById(R.id.txt_close));
                closeConfirmRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmRemoveDialog.dismiss();
                    }
                });
                yesButton = (Button) confirmRemoveDialog.findViewById(R.id.yes_btn);
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        manager.removeRestaurant(viewHolder.getAdapterPosition());
                        manager.updateRestaurants();
                        //restaurants.remove(viewHolder.getAdapterPosition());
                        //save();
                        restaurantAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        confirmRemoveDialog.dismiss();
                    }
                });
                noButton = (Button) confirmRemoveDialog.findViewById(R.id.no_btn);
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmRemoveDialog.dismiss();
                    }
                });
                confirmRemoveDialog.show();
            }

            /*
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setAlpha(1.0f);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if (actionState == ACTION_STATE_DRAG) {
                    viewHolder.itemView.setAlpha(0.5f);
                }
            }*/
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(restaurantAdapter);
    }

    /*
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("A", "onPauseMessage, SAVE");
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(restaurants);

            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    /*
    @Override
    protected void onResume(){
        super.onResume();
        Log.d("B", "onResumeMessage, LOAD");
        load();
    }

    public void firstRegister(){
        restaurants.add(new Restaurant("2C",
                "Menachem Begin Road 132, Tel Aviv Jaffa",
                "Chef's restaurant",
                BitmapFactory.decodeResource(getResources() , R.drawable.two_c)));

        restaurants.add(new Restaurant("Vitrina",
                "Lilinblum 40, Tel Aviv Jaffa",
                "Burgers",
                BitmapFactory.decodeResource(getResources() , R.drawable.vitrina)));

        restaurants.add(new Restaurant("Tzel tamar",
                "Ashdot Yaakov",
                "Rural restaurant",
                BitmapFactory.decodeResource(getResources() , R.drawable.tzel_tamar)));

        restaurants.add(new Restaurant("Ruben",
                "Jabotinsky 43, Ashdod",
                "Burgers",
                BitmapFactory.decodeResource(getResources() , R.drawable.ruben)));

        restaurants.add(new Restaurant("Space",
                "Hazait 104, Emunim",
                "Meat kitchen",
                BitmapFactory.decodeResource(getResources() , R.drawable.space)));

        restaurants.add(new Restaurant("Soho",
                "Moshe Becker 15, Rishon Lezion",
                "Rest, Food, Sushi",
                BitmapFactory.decodeResource(getResources() , R.drawable.soho)));

        restaurants.add(new Restaurant("Segev express",
                "Eliezer Mazal 16, Rishon Lezion",
                "Chef's restaurant",
                BitmapFactory.decodeResource(getResources() , R.drawable.segev_express)));

        restaurants.add(new Restaurant("Ratatui",
                "Hazait 90, Emunim",
                "Asian, Italian, Fish",
                BitmapFactory.decodeResource(getResources() , R.drawable.ratatui)));

        restaurants.add(new Restaurant("Kisu",
                "Raphael Eitan Road 1, Kiryat Ono",
                "Asian & Sushi Bar",
                BitmapFactory.decodeResource(getResources() , R.drawable.kisu)));

        restaurants.add(new Restaurant("Hudson",
                "Habarzel 27, Tel Aviv-Jaffa",
                "Brasserie",
                BitmapFactory.decodeResource(getResources() , R.drawable.hudson)));

        restaurants.add(new Restaurant("Frame",
                "Azrieli Rishonim Mall",
                "Chef & Sushi Bar",
                BitmapFactory.decodeResource(getResources() , R.drawable.frame)));

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("registered", false);
        editor.apply();
    }

    public void save(){
        try {
            FileOutputStream fos = openFileOutput("restaurants", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(restaurants);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        //List<Restaurant> arr = new ArrayList<>();
        //restaurants = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput("restaurants");
            ObjectInputStream ois = new ObjectInputStream(fis);
            restaurants.addAll((ArrayList<Restaurant>)ois.readObject());
            //arr = (ArrayList<Restaurant>)ois.readObject();
            //Toast.makeText(this, arr.toString(), Toast.LENGTH_SHORT).show();
            //Restaurant restaurant = (Restaurant)ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //return arr;
    }
    */
}
