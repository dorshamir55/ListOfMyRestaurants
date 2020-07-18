package com.example.ex1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShowRestaurantActivity extends AppCompatActivity {
    private TextView name;
    private ImageView imageView;
    private TextView address;
    private TextView phoneNumber;
    private List<Restaurant> restaurants = new ArrayList<>();
    private ImageView wazeIv;
    private ImageView callIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_restaurant);

        name = (TextView)findViewById(R.id.detailed_name_restaurant);
        imageView = (ImageView)findViewById(R.id.detailed_restaurant_picture);
        address = (TextView)findViewById(R.id.detailed_address);
        phoneNumber = (TextView)findViewById(R.id.detailed_phone_number);
        callIv = (ImageView)findViewById(R.id.call_image);
        wazeIv = (ImageView)findViewById(R.id.waze_image);

        RestaurantManager manager = RestaurantManager.getInstance(this);
        int position = getIntent().getIntExtra("Position", -1);
        restaurants = manager.getRestaurants();
        int size = restaurants.size();
        if(position < size){

            name.setText(restaurants.get(position).getName());
            imageView.setImageBitmap(BitmapFactory.decodeFile(restaurants.get(position).getPath()));
            address.setText(restaurants.get(position).getAddress());
            phoneNumber.setText(restaurants.get(position).getPhoneNumber());

            callIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = phoneNumber.getText().toString();

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+text));
                    startActivity(intent);
                }
            });

            wazeIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //String text = ((TextView)v).getText().toString();
                    String text = address.getText().toString();

                    try {
                        //String uri = "geo: latitude,longtitude";
                        //startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));

                        String url = "https://waze.com/ul?q="+text;
                        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
                        startActivity( intent );

                        //Intent intent = new Intent(Intent.ACTION_VIEW);
                        //intent.setData(Uri.parse("waze://q=" + text));
                        //startActivity(intent);
                    }catch (ActivityNotFoundException ex){
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://id=com.waze"));
                        startActivity(intent);
                    }
                }
            });
        }

    }

}
