package com.example.ex1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShowRestaurantActivity extends AppCompatActivity {
    private TextView name;
    private ImageView imageView;
    private TextView address;
    private TextView foodType;
    private List<Restaurant> restaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_restaurant);

        name = (TextView)findViewById(R.id.detailed_name_restaurant);
        imageView = (ImageView)findViewById(R.id.detailed_restaurant_picture);
        address = (TextView)findViewById(R.id.detailed_address);
        foodType = (TextView)findViewById(R.id.detailed_food_type);

        RestaurantManager manager = RestaurantManager.getInstance(this);
        int position = getIntent().getIntExtra("Position", -1);
        restaurants = manager.getRestaurants();
        int size = restaurants.size();
        if(position < size){

            name.setText(restaurants.get(position).getName());
            imageView.setImageBitmap(BitmapFactory.decodeFile(restaurants.get(position).getPath()));
            address.setText(restaurants.get(position).getAddress());
            foodType.setText(restaurants.get(position).getType());
        }
    }

}
